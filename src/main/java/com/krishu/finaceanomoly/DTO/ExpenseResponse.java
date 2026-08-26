package com.krishu.finaceanomoly.DTO;

import com.krishu.finaceanomoly.ExpenseCategory;
import com.krishu.finaceanomoly.ExpenseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponse(UUID id, String vendor, BigDecimal amount, ExpenseCategory category, ExpenseStatus status, String aiReasoning,
                              LocalDateTime createdAt) {
}
