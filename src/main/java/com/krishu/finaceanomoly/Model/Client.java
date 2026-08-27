package com.krishu.finaceanomoly.Model;

import com.krishu.finaceanomoly.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable=false,unique=true)
    private String email;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime createdAt;
}
