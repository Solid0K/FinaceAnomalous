package com.krishu.finaceanomoly.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(@NotNull String vendor, @NotNull @DecimalMin(value="0.01",message="Amount should be positive") BigDecimal amount
        , @NotNull String currency
        , @NotNull @PastOrPresent(message="Expense couldn't be future") LocalDate expenseDate
        ,@NotNull @Size(max=500,message="desc should be in 500 words") String description) {}
