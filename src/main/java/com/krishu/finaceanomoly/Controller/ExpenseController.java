package com.krishu.finaceanomoly.Controller;

import com.krishu.finaceanomoly.DTO.ExpenseRequest;
import com.krishu.finaceanomoly.DTO.ExpenseResponse;
import com.krishu.finaceanomoly.ExpenseStatus;
import com.krishu.finaceanomoly.Service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService=expenseService;
    }

    @PostMapping()
    public ResponseEntity<ExpenseResponse> createExpense(@RequestBody ExpenseRequest request){
        return ResponseEntity.ok(expenseService.createExpense(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ExpenseResponse>> bulkCreation(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(expenseService.creatExpenseInBulk(file));
    }

    @GetMapping()
    public ResponseEntity<List<ExpenseResponse>> getExpenses(){
        return ResponseEntity.ok(expenseService.getExpenses());
    }

    @GetMapping("/byStatus")
    public ResponseEntity<List<ExpenseResponse>> getExpenseByStatus(@RequestParam("status") ExpenseStatus status){
        return ResponseEntity.ok(expenseService.getExpenseByStatus(status));
    }

    @GetMapping("/oneExpense/{expenseId}")
    public ResponseEntity<ExpenseResponse> getOneExpense(@PathVariable UUID expenseId){
        return ResponseEntity.ok(expenseService.getOneExpense(expenseId));
    }
}
