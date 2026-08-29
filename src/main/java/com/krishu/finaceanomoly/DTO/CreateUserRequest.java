package com.krishu.finaceanomoly.DTO;

import com.krishu.finaceanomoly.Role;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(@NotNull String name, @NotNull String email,
                                @NotNull String password,@NotNull Role role) {
}
