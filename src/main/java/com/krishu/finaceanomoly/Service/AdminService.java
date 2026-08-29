package com.krishu.finaceanomoly.Service;

import com.krishu.finaceanomoly.CustomException.EmailAlreadyExistException;
import com.krishu.finaceanomoly.DTO.CreateUserRequest;
import com.krishu.finaceanomoly.DTO.UserResponse;
import com.krishu.finaceanomoly.Model.Client;
import com.krishu.finaceanomoly.Repository.ClientRepo;
import com.krishu.finaceanomoly.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdminService {

    private final ClientRepo clientRepo;
    private final BCryptPasswordEncoder encoder;

    public AdminService(ClientRepo clientRepo, BCryptPasswordEncoder encoder){
        this.clientRepo=clientRepo;
        this.encoder = encoder;
    }

    public UserResponse createUser(CreateUserRequest request){
        if(clientRepo.existsByEmail(request.email())){
            throw new EmailAlreadyExistException("Email is already in use");
        }
        Client client=new Client();
        client.setName(request.name());
        client.setEmail(request.email());
        client.setPassword(encoder.encode(request.password()));
        client.setRole(request.role());
        client.setCreatedAt(LocalDateTime.now());
        Client savedClient=clientRepo.save(client);
        return UserResponse.from(savedClient);
    }
}
