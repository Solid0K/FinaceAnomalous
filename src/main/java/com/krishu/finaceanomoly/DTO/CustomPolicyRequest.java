package com.krishu.finaceanomoly.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomPolicyRequest(@NotBlank @Size(max=200) String ruleText) {
}
