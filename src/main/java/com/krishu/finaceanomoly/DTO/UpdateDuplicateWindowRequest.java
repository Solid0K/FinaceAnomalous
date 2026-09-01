package com.krishu.finaceanomoly.DTO;

import jakarta.validation.constraints.NotNull;

public record UpdateDuplicateWindowRequest(@NotNull Integer updatedDuplicateWindow) {
}
