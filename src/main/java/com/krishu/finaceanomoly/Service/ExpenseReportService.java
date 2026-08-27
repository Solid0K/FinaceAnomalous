package com.krishu.finaceanomoly.Service;

import com.krishu.finaceanomoly.ExpenseStatus;
import com.krishu.finaceanomoly.Model.AuditLog;
import com.krishu.finaceanomoly.Model.Expense;
import com.krishu.finaceanomoly.Repository.AuditLogRepo;
import com.krishu.finaceanomoly.Repository.ExpenseRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExpenseReportService {

    private final ExpenseRepo expenseRepo;
    private final AuditLogRepo auditLogRepo;

    public ExpenseReportService(ExpenseRepo expenseRepo, AuditLogRepo auditLogRepo) {
        this.expenseRepo = expenseRepo;
        this.auditLogRepo = auditLogRepo;
    }

    public ByteArrayInputStream generateFlaggedReport(ExpenseStatus status) {
        List<Expense> flagged = expenseRepo.findByStatus(status);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet expenseSheet = workbook.createSheet("Flagged Expenses");
            writeExpenseSheet(workbook, expenseSheet, flagged);

            Sheet auditSheet = workbook.createSheet("Audit Trail");
            writeAuditSheet(workbook, auditSheet, flagged);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    private void writeExpenseSheet(Workbook workbook, Sheet sheet, List<Expense> expenses) {
        CellStyle headerStyle = createHeaderStyle(workbook);

        Row header = sheet.createRow(0);
        String[] columns = {"ID", "Vendor", "Amount", "Currency", "Category", "Status", "Expense Date", "Submitted By", "AI Reasoning"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Expense e : expenses) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(e.getId().toString());
            row.createCell(1).setCellValue(e.getVendor());
            row.createCell(2).setCellValue(e.getAmount().doubleValue());
            row.createCell(3).setCellValue(e.getCurrency());
            row.createCell(4).setCellValue(e.getCategory().name());
            row.createCell(5).setCellValue(e.getStatus().name());
            row.createCell(6).setCellValue(e.getExpenseDate().toString());
            row.createCell(7).setCellValue(e.getSubmittedBy());
            row.createCell(8).setCellValue(e.getAiReasoning() != null ? e.getAiReasoning() : "");
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void writeAuditSheet(Workbook workbook, Sheet sheet, List<Expense> flaggedExpenses) {
        CellStyle headerStyle = createHeaderStyle(workbook);

        Row header = sheet.createRow(0);
        String[] columns = {"Expense ID", "Vendor", "Writer", "Action", "Detail", "Timestamp"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Expense expense : flaggedExpenses) {
            List<AuditLog> logs = auditLogRepo.findByExpenseIdOrderByTimeStampAsc(expense.getId());
            for (AuditLog log : logs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(expense.getId().toString());
                row.createCell(1).setCellValue(expense.getVendor());
                row.createCell(2).setCellValue(log.getWriter().name());
                row.createCell(3).setCellValue(log.getAction());
                row.createCell(4).setCellValue(log.getDetail());
                row.createCell(5).setCellValue(log.getTimeStamp().toString());
            }
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}
