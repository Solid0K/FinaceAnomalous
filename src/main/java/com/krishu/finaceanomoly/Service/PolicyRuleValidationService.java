package com.krishu.finaceanomoly.Service;

import com.krishu.finaceanomoly.ExpenseStatus;
import com.krishu.finaceanomoly.LogWriter;
import com.krishu.finaceanomoly.Model.AuditLog;
import com.krishu.finaceanomoly.Model.Expense;
import com.krishu.finaceanomoly.Model.PolicyRule;
import com.krishu.finaceanomoly.Repository.AuditLogRepo;
import com.krishu.finaceanomoly.Repository.ExpenseRepo;
import com.krishu.finaceanomoly.Repository.PolicyRuleRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PolicyRuleValidationService {

    private final PolicyRuleRepo policyRuleRepo;
    private final ExpenseRepo expenseRepo;
    private final AuditLogRepo auditLogRepo;

    public PolicyRuleValidationService(PolicyRuleRepo policyRuleRepo,ExpenseRepo expenseRepo,AuditLogRepo auditLogRepo){
        this.policyRuleRepo=policyRuleRepo;
        this.expenseRepo=expenseRepo;
        this.auditLogRepo=auditLogRepo;
    }

    public void validate(Expense expense){
        List<PolicyRule> activePolicies=policyRuleRepo.findByActive(true);
        boolean anyViolation=false;

        for(PolicyRule policy:activePolicies){
            String violation=switch(policy.getType()){
                case AMOUNT_LIMIT->checkAmount(expense,policy);
                case CATEGORY_LIMIT->checkCategoryLimit(expense,policy);
                case DUPLICATE_CHECK->checkDuplicateWindow(expense,policy);
            };
            if(violation!=null){
                anyViolation=true;
                Log(expense,"FLAGGED_"+policy.getType(),violation);
            }
        }
        if(anyViolation){
            expense.setStatus(ExpenseStatus.FLAGGED);
        }else{
            expense.setStatus(ExpenseStatus.APPROVED);
            Log(expense,"APPROVED","Passed all Policies");
        }
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
        if(expense.getCategory()==null){
            return null;
        }
        if(!expense.getCategory().equalsIgnoreCase(policy.getCategory())){
            return null;
        }
        BigDecimal currAmount=expenseRepo.sumOfExpensesByCategory(expense.getCategory(),expense.getId());
        if((currAmount.add((expense.getAmount()))).compareTo(policy.getCategoryThreshold())>0){
            return String.format("Amount %s rupees will make CategoryTotal %s rupees exceeds the Threshold_Category_Amount %s rupees"
                    ,expense.getAmount(),currAmount,policy.getCategoryThreshold());
        }
        return null;
    }

    private String checkAmount(Expense expense, PolicyRule policy) {
        if(expense.getAmount().compareTo(policy.getAmountThreshold())>0){
            return String.format("Amount %s exceed the threshold amount %s",expense.getAmount(),policy.getAmountThreshold());
        }
        return null;
    }

    private void Log(Expense expense, String action, String message) {
        AuditLog auditlog=new AuditLog();
        auditlog.setExpense(expense);
        auditlog.setAction(action);
        auditlog.setWriter(LogWriter.SYSTEM);
        auditlog.setDetail(message);
        auditlog.setTimeStamp(LocalDateTime.now());
        auditLogRepo.save(auditlog);
    }
}
