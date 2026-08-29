package com.krishu.finaceanomoly.Repository;

import com.krishu.finaceanomoly.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepo extends JpaRepository<Client, UUID> {
    Optional<Client> findByEmail(String email);
    boolean existsByEmail(String email);
}
