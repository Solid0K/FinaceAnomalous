package com.krishu.finaceanomoly.Controller;

import com.krishu.finaceanomoly.ExpenseStatus;
import com.krishu.finaceanomoly.Service.ExpenseReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/expenseReport")
public class ExpenseReportController {

    private final ExpenseReportService reportService;

    public ExpenseReportController(ExpenseReportService reportService){
        this.reportService=reportService;
    }

    @GetMapping()
    public ResponseEntity<InputStreamResource> FlaggedReport(@RequestParam("status") ExpenseStatus status){
        ByteArrayInputStream inputStream=reportService.generateFlaggedReport(status);
        HttpHeaders header=new HttpHeaders();
        header.set("Content-Disposition","attachment; filename=flagged_expense_report.xlsx");
        return ResponseEntity.ok().headers(header).
                contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).
                body(new InputStreamResource(inputStream));
    }
}
