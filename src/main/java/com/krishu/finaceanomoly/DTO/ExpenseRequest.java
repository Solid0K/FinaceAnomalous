package com.krishu.finaceanomoly.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(String vendor, BigDecimal amount, String currency
        , LocalDate expenseDate, String submittedBy, String description) {}
