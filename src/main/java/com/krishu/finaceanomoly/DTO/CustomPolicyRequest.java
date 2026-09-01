package com.krishu.finaceanomoly.DTO;

import jakarta.validation.constraints.NotNull;

public record CustomPolicyRequest(@NotNull String ruleText) {
}
