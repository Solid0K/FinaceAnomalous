package com.krishu.finaceanomoly.DTO;

import com.krishu.finaceanomoly.Model.Client;

import java.util.UUID;

public record UserResponse(UUID id, String name, String email, String password) {
    public static UserResponse from(Client client){
        return new UserResponse(client.getId(),client.getName(),client.getEmail(),client.getPassword());
    }
}
