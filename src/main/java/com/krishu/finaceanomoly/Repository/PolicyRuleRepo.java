package com.krishu.finaceanomoly.Repository;

import com.krishu.finaceanomoly.ExpenseCategory;
import com.krishu.finaceanomoly.Model.PolicyRule;
import com.krishu.finaceanomoly.RuleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PolicyRuleRepo extends JpaRepository<PolicyRule, UUID> {
    List<PolicyRule> findByActive(boolean active);
    boolean existsByTypeAndCategoryAndActive(RuleType type, ExpenseCategory category, boolean active);
    List<PolicyRule> findByType(RuleType type);
}
