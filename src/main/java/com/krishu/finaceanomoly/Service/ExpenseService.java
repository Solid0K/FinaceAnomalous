package com.krishu.finaceanomoly.Service;

import com.krishu.finaceanomoly.CustomException.NotFoundException;
import com.krishu.finaceanomoly.DTO.ExpenseRequest;
import com.krishu.finaceanomoly.DTO.ExpenseResponse;
import com.krishu.finaceanomoly.ExpenseStatus;
import com.krishu.finaceanomoly.Model.Expense;
import com.krishu.finaceanomoly.Repository.ExpenseRepo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseService {

    private final ExpenseRepo expenseRepo;
    private final PolicyRuleValidationService policyRuleValidationService;

    public ExpenseService(ExpenseRepo expenseRepo, PolicyRuleValidationService policyRuleValidationService){
        this.expenseRepo=expenseRepo;
        this.policyRuleValidationService = policyRuleValidationService;
    }

    public ExpenseResponse createExpense(ExpenseRequest request) {
        Expense expense=new Expense();
        expense.setVendor(request.vendor());
        expense.setAmount(request.amount());
        expense.setCurrency(request.currency());
        expense.setExpenseDate(request.expenseDate());
        expense.setDescription(request.description());
        expense.setSubmittedBy(request.submittedBy());
        expense.setCreatedAt(LocalDateTime.now());
        expense.setStatus(ExpenseStatus.PENDING);
        expense.setCategory(request.category());
        Expense savedExpense=expenseRepo.save(expense);
        return mapToResponse(ValidationOfExpense(savedExpense));
    }

    public List<ExpenseResponse> creatExpenseInBulk(MultipartFile file) throws IOException {
        List<Expense> expenses=new ArrayList<>();
        CSVFormat csvformat=CSVFormat.DEFAULT.builder().
                setHeader().setSkipHeaderRecord(true).setIgnoreHeaderCase(true).setTrim(true).build();
        try(Reader reader=new InputStreamReader(file.getInputStream());
            CSVParser parser=new CSVParser(reader,csvformat)){
            for(CSVRecord record:parser){
                Expense expense=new Expense();
                expense.setVendor(record.get("vendor"));
                expense.setAmount(new BigDecimal(record.get("amount")));
                expense.setCurrency(record.get("currency"));
                expense.setExpenseDate(LocalDate.parse(record.get("expenseDate")));
                expense.setDescription(record.get("description"));
                expense.setSubmittedBy(record.get("submittedBy"));
                expense.setCreatedAt(LocalDateTime.now());
                expense.setStatus(ExpenseStatus.PENDING);
                expenses.add(expense);
            }
        }
        List<Expense> savedExpenses=expenseRepo.saveAll(expenses);
        List<Expense> updatedExpense=savedExpenses.stream().map(this::ValidationOfExpense).toList();
        return updatedExpense.stream().map(this::mapToResponse).toList();
    }

    public List<ExpenseResponse> getExpenses() {
        List<Expense> allExpenses=expenseRepo.findAll();
        return allExpenses.stream().map(this::mapToResponse).toList();
    }

    public List<ExpenseResponse> getExpenseByStatus(ExpenseStatus status) {
        return expenseRepo.findByStatus(status).stream().map(this::mapToResponse).toList();
    }

    public ExpenseResponse getOneExpense(UUID expenseId) {
        Expense expense=expenseRepo.findById(expenseId).orElseThrow(()->new NotFoundException("Expense not found"));
        return mapToResponse(expense);
    }

    private ExpenseResponse mapToResponse(Expense expense){
        return new ExpenseResponse(expense.getId(),expense.getVendor(),
                expense.getAmount(),expense.getCategory(),expense.getStatus(),expense.getAiReasoning(),expense.getCreatedAt());
    }

    private Expense ValidationOfExpense(Expense expense){
        policyRuleValidationService.validate(expense);
        return expenseRepo.save(expense);
    }
}
