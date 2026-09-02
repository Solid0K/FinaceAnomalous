package com.krishu.finaceanomoly.Service;

import com.krishu.finaceanomoly.DTO.CustomPolicyVerdict;
import com.krishu.finaceanomoly.ExpenseStatus;
import com.krishu.finaceanomoly.LLM_Feature.LLMClient;
import com.krishu.finaceanomoly.LogWriter;
import com.krishu.finaceanomoly.Model.AuditLog;
import com.krishu.finaceanomoly.Model.Expense;
import com.krishu.finaceanomoly.Model.PolicyRule;
import com.krishu.finaceanomoly.Repository.AuditLogRepo;
import com.krishu.finaceanomoly.Repository.ExpenseRepo;
import com.krishu.finaceanomoly.Repository.PolicyRuleRepo;
import com.krishu.finaceanomoly.RuleType;
import org.apache.tomcat.util.digester.Rule;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PolicyRuleValidationService {

    private final PolicyRuleRepo policyRuleRepo;
    private final ExpenseRepo expenseRepo;
    private final AuditLogRepo auditLogRepo;
    private final LLMClient llmClient;

    public PolicyRuleValidationService(PolicyRuleRepo policyRuleRepo, ExpenseRepo expenseRepo, AuditLogRepo auditLogRepo, LLMClient llmClient){
        this.policyRuleRepo=policyRuleRepo;
        this.expenseRepo=expenseRepo;
        this.auditLogRepo=auditLogRepo;
        this.llmClient = llmClient;
    }

    public void validate(Expense expense){
        List<PolicyRule> activePolicies=policyRuleRepo.findByActive(true);
        boolean anyViolation=false;

        for(PolicyRule policy:activePolicies){
            String violation=switch(policy.getType()){
                case AMOUNT_LIMIT->checkAmount(expense,policy);
                case CATEGORY_LIMIT->checkCategoryLimit(expense,policy);
                case DUPLICATE_CHECK->checkDuplicateWindow(expense,policy);
                case CUSTOM->checkCustomPolicy(expense,policy);
            };
            if(violation!=null){
                anyViolation=true;
                LogWriter writer=policy.getType() == RuleType.CUSTOM ? LogWriter.AI : LogWriter.SYSTEM;
                Log(expense,"FLAGGED_"+policy.getType(),violation,writer);
            }
        }
        if(anyViolation){
            expense.setStatus(ExpenseStatus.FLAGGED);
        }else{
            expense.setStatus(ExpenseStatus.APPROVED);
            Log(expense,"APPROVED","Passed all Policies",LogWriter.SYSTEM);
        }
    }

    private String checkCustomPolicy(Expense expense, PolicyRule policy) {
        CustomPolicyVerdict validation=llmClient.checkCustomPolicy(expense,policy);
        if(validation.violated()){
            return validation.resoning();
        }
        return null;
    }

    private String checkDuplicateWindow(Expense expense, PolicyRule policy) {
        LocalDate startWindow=expense.getExpenseDate().minusDays(policy.getDuplicateWindowDay());
        LocalDate endWindow=expense.getExpenseDate().plusDays(policy.getDuplicateWindowDay());

        List<Expense> expenseUnderWindow=expenseRepo.
                findByVendorAndAmountAndExpenseDateBetween(expense.getVendor(),expense.getAmount(),startWindow,endWindow);
        boolean duplicateExpense=expenseUnderWindow.stream().anyMatch(e->!e.getId().equals(expense.getId()));
        if(duplicateExpense){
            return String.format("Possible duplicate: same vendor (%s) and amount (%s) within %d days",
                    expense.getVendor(),expense.getAmount(),policy.getDuplicateWindowDay());
        }
        return null;
    }

    private String checkCategoryLimit(Expense expense, PolicyRule policy) {
        if(!expense.getCategory().equals(policy.getCategory())){
            return null;
        }
        LocalDate[] range=resolvePeriod(expense,policy);
        BigDecimal currAmount=expenseRepo.sumOfExpensesByCategory(expense.getCategory(),expense.getId(),range[0],range[1]);
        if(currAmount.add(expense.getAmount()).compareTo(policy.getCategoryThreshold())>0){
            return String.format("Total %s spend (%s) in %s exceeds limit of %s",
                    policy.getCategory(), currAmount.add(expense.getAmount()), policy.getPeriod(), policy.getCategoryThreshold());
        }
        return null;
    }

    private String checkAmount(Expense expense, PolicyRule policy) {
        if(!expense.getCategory().equals(policy.getCategory())){
            return null;
        }
        if(expense.getAmount().compareTo(policy.getAmountThreshold())>0){
            return String.format("Amount %s exceeds max allowed %s for category %s", expense.getAmount(), policy.getAmountThreshold(), policy.getCategory());
        }
        return null;
    }

    private void Log(Expense expense, String action, String message,LogWriter writer) {
        AuditLog auditlog=new AuditLog();
        auditlog.setExpense(expense);
        auditlog.setAction(action);
        auditlog.setWriter(writer);
        auditlog.setDetail(message);
        auditlog.setTimeStamp(LocalDateTime.now());
        auditLogRepo.save(auditlog);
    }

    private LocalDate[] resolvePeriod(Expense expense,PolicyRule policy){
        return switch(policy.getPeriod()){
            case MONTHLY->new LocalDate[]{expense.getExpenseDate().withDayOfMonth(1),expense.getExpenseDate().
                    withDayOfMonth(expense.getExpenseDate().lengthOfMonth())};
            case QUARTERLY -> {
                int quarterStartMonth = ((expense.getExpenseDate().getMonthValue() - 1) / 3) * 3 + 1;
                LocalDate start = LocalDate.of(expense.getExpenseDate().getYear(), quarterStartMonth, 1);
                yield new LocalDate[]{ start, start.plusMonths(3).minusDays(1) };
            }
            case YEARLY->new LocalDate[]{expense.getExpenseDate().withDayOfYear(1),expense.
                    getExpenseDate().withDayOfYear(expense.getExpenseDate().lengthOfYear())};
        };
    }
}
