package com.krishu.finaceanomoly;

import com.krishu.finaceanomoly.Model.PolicyRule;
import com.krishu.finaceanomoly.Repository.PolicyRuleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CodeSeeder implements CommandLineRunner {

    private final PolicyRuleRepo policyRuleRepo;

    public CodeSeeder(PolicyRuleRepo policyRuleRepo){
        this.policyRuleRepo=policyRuleRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if(policyRuleRepo.count()>0){
            return;
        }

        PolicyRule maxAmount = new PolicyRule();
        maxAmount.setType(RuleType.AMOUNT_LIMIT);
        maxAmount.setCategory(null);
        maxAmount.setAmountThreshold(new BigDecimal("20000"));
        maxAmount.setActive(true);

        PolicyRule mealLimit = new PolicyRule();
        mealLimit.setType(RuleType.CATEGORY_LIMIT);
        mealLimit.setCategory("Meals");
        mealLimit.setCategoryThreshold(new BigDecimal("500"));
        mealLimit.setActive(true);

        PolicyRule duplicateCheck = new PolicyRule();
        duplicateCheck.setType(RuleType.DUPLICATE_CHECK);
        duplicateCheck.setDuplicateWindowDay(7);
        duplicateCheck.setActive(true);

        policyRuleRepo.saveAll(List.of(maxAmount,mealLimit,duplicateCheck));
    }
}
