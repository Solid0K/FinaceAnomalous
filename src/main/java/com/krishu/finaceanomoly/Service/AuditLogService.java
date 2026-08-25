package com.krishu.finaceanomoly.Service;

import com.krishu.finaceanomoly.DTO.LogResponse;
import com.krishu.finaceanomoly.LogWriter;
import com.krishu.finaceanomoly.Model.AuditLog;
import com.krishu.finaceanomoly.Repository.AuditLogRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuditLogService {

    private final AuditLogRepo auditlogRepo;

    public AuditLogService(AuditLogRepo auditlogRepo){
        this.auditlogRepo=auditlogRepo;
    }

    public List<LogResponse> getAllLogs() {
        List<AuditLog> allLogs=auditlogRepo.findAllByOrderByTimeStampDesc();
        return allLogs.stream().map(LogResponse::from).toList();
    }

    public List<LogResponse> getLogsByExpense(UUID expenseId) {
        List<AuditLog> logs=auditlogRepo.findByExpenseIdOrderByTimeStampAsc(expenseId);
        return logs.stream().map(LogResponse::from).toList();
    }

    public List<LogResponse> getLogsByWriter(LogWriter writer) {
        List<AuditLog> logsByWriter=auditlogRepo.findByWriterOrderByTimeStampDesc(writer);
        return logsByWriter.stream().map(LogResponse::from).toList();
    }
}
