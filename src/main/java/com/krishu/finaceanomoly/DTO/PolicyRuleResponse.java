package com.krishu.finaceanomoly.DTO;

import com.krishu.finaceanomoly.ExpenseCategory;
import com.krishu.finaceanomoly.Model.PolicyRule;
import com.krishu.finaceanomoly.PolicyPeriod;
import com.krishu.finaceanomoly.RuleType;

import java.math.BigDecimal;
import java.util.UUID;

public record PolicyRuleResponse(UUID id, RuleType type, ExpenseCategory category,
                                 BigDecimal amountThreshold, BigDecimal categoryLimitThreshold,
                                 Integer duplicateWindowDay,
                                 PolicyPeriod period,String ruleText,Boolean active) {
    public static PolicyRuleResponse from(PolicyRule policy){
        PolicyRuleResponse response=new PolicyRuleResponse(policy.getId(),policy.getType(),policy.getCategory()
                ,policy.getAmountThreshold(),policy.getCategoryThreshold()
                ,policy.getDuplicateWindowDay(),policy.getPeriod(),policy.getRuleText(),policy.isActive());
        return response;
    }
}
