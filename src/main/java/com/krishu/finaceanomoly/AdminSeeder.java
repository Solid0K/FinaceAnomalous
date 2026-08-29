package com.krishu.finaceanomoly;

import com.krishu.finaceanomoly.Model.Client;
import com.krishu.finaceanomoly.Repository.ClientRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final ClientRepo clientRepo;
    private final BCryptPasswordEncoder encoder;

    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;

    public AdminSeeder(ClientRepo clientRepo,BCryptPasswordEncoder encoder){
        this.clientRepo=clientRepo;
        this.encoder=encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(clientRepo.existsByEmail(adminEmail)){
            return;
        }
        Client admin=new Client();
        admin.setName("System_admin");
        admin.setEmail(adminEmail);
        admin.setPassword(encoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        admin.setCreatedAt(LocalDateTime.now());
        clientRepo.save(admin);
    }
}
