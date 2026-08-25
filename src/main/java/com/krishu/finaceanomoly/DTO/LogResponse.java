package com.krishu.finaceanomoly.DTO;

import com.krishu.finaceanomoly.LogWriter;
import com.krishu.finaceanomoly.Model.AuditLog;

import java.time.LocalDateTime;
import java.util.UUID;

public record LogResponse(UUID id, UUID expenseId, LogWriter writer, String action, String detail,
                          LocalDateTime timestamp) {
    public static LogResponse from(AuditLog log){
        return new LogResponse(
                log.getId(),
                log.getExpense().getId(),
                log.getWriter(),
                log.getAction(),
                log.getDetail(),
                log.getTimeStamp()
        );
    }
}
