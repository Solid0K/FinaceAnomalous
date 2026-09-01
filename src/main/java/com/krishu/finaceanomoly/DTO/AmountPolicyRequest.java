package com.krishu.finaceanomoly.DTO;

import com.krishu.finaceanomoly.ExpenseCategory;
import com.krishu.finaceanomoly.RuleType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AmountPolicyRequest(@NotNull ExpenseCategory category,
                                  @NotNull @DecimalMin(value="0.01",message="amount cant be negative") BigDecimal amountThreshold) {
}
