package com.krishu.finaceanomoly.Repository;

import com.krishu.finaceanomoly.Model.PolicyRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PolicyRuleRepo extends JpaRepository<PolicyRule, UUID> {
    List<PolicyRule> findByActive(boolean active);
}
