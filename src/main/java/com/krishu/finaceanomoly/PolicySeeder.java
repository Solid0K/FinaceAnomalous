package com.krishu.finaceanomoly;

import com.krishu.finaceanomoly.Model.PolicyRule;
import com.krishu.finaceanomoly.Repository.PolicyRuleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class PolicySeeder implements CommandLineRunner {

    private final PolicyRuleRepo policyRuleRepo;

    public PolicySeeder(PolicyRuleRepo policyRuleRepo) {
        this.policyRuleRepo = policyRuleRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (policyRuleRepo.count() > 0) {
            return;
        }

        List<PolicyRule> rules = new ArrayList<>();

        rules.add(amountLimitRule(ExpenseCategory.TRAVEL, "25000"));
        rules.add(amountLimitRule(ExpenseCategory.MEAL, "1000"));
        rules.add(amountLimitRule(ExpenseCategory.SOFTWARE, "15000"));
        rules.add(amountLimitRule(ExpenseCategory.OFFICE_SUPPLIES, "5000"));
        rules.add(amountLimitRule(ExpenseCategory.ACCOMMODATION, "20000"));
        rules.add(amountLimitRule(ExpenseCategory.OTHER, "10000"));

        rules.add(categoryLimitRule(ExpenseCategory.MEAL, "3000", PolicyPeriod.MONTHLY));

        rules.add(duplicateCheckRule(7));

        policyRuleRepo.saveAll(rules);
    }

    private PolicyRule amountLimitRule(ExpenseCategory category, String threshold) {
        PolicyRule rule = new PolicyRule();
        rule.setType(RuleType.AMOUNT_LIMIT);
        rule.setCategory(category);
        rule.setAmountThreshold(new BigDecimal(threshold));
        rule.setActive(true);
        return rule;
    }

    private PolicyRule categoryLimitRule(ExpenseCategory category, String threshold, PolicyPeriod period) {
        PolicyRule rule = new PolicyRule();
        rule.setType(RuleType.CATEGORY_LIMIT);
        rule.setCategory(category);
        rule.setCategoryThreshold(new BigDecimal(threshold));
        rule.setPeriod(period);
        rule.setActive(true);
        return rule;
    }

    private PolicyRule duplicateCheckRule(int windowDays) {
        PolicyRule rule = new PolicyRule();
        rule.setType(RuleType.DUPLICATE_CHECK);
        rule.setDuplicateWindowDay(windowDays);
        rule.setActive(true);
        return rule;
    }
}
