package com.krishu.finaceanomoly.DTO;

import com.krishu.finaceanomoly.ExpenseCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DuplicateWindowPolicyRequest(@NotNull @Min(1) Integer duplicateWindowDay) {
}
