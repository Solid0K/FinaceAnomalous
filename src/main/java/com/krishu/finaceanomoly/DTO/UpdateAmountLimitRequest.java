package com.krishu.finaceanomoly.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateAmountLimitRequest(@NotNull @DecimalMin(value="0.01",message="amount cant be negative") BigDecimal newAmountThreshold) {
}
