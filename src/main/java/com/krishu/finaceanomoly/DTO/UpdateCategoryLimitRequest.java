package com.krishu.finaceanomoly.DTO;

import com.krishu.finaceanomoly.PolicyPeriod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateCategoryLimitRequest(@NotNull @DecimalMin(value="0.01",message="amount cant be negative") BigDecimal newCategoryLimit,
                                  @NotNull PolicyPeriod updatePeriod){
}
