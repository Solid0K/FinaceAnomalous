package com.krishu.finaceanomoly.Service;

import com.krishu.finaceanomoly.CustomException.EmailAlreadyExistException;
import com.krishu.finaceanomoly.CustomException.NotFoundException;
import com.krishu.finaceanomoly.CustomException.PasswordMismatchException;
import com.krishu.finaceanomoly.DTO.LoginRequest;
import com.krishu.finaceanomoly.DTO.RegisterRequest;
import com.krishu.finaceanomoly.Model.Client;
import com.krishu.finaceanomoly.Repository.ClientRepo;
import com.krishu.finaceanomoly.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final ClientRepo clientRepo;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(ClientRepo clientRepo, BCryptPasswordEncoder encoder,JwtService jwtService){
        this.clientRepo=clientRepo;
        this.encoder = encoder;
        this.jwtService=jwtService;
    }

    public void clientSignUp(RegisterRequest request) {
        if(clientRepo.existsByEmail(request.email())){
            throw new EmailAlreadyExistException("Email is already in use");
        }
        Client client=new Client();
        client.setName(request.name());
        client.setEmail(request.email());
        client.setPassword(encoder.encode(request.password()));
        client.setRole(Role.EMPLOYEE);
        client.setCreatedAt(LocalDateTime.now());
        clientRepo.save(client);
    }

    public String clientSignIn(LoginRequest request) {
        Client client=clientRepo.findByEmail(request.email()).orElseThrow(()->new NotFoundException("User with email not found"));
        if(!encoder.matches(request.password(),client.getPassword())){
            throw new PasswordMismatchException("Given password is not right");
        }
        return jwtService.generateToken(client.getEmail(),client.getRole());
    }
}
