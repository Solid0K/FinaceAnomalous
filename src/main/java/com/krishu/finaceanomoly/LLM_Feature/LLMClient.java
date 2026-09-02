package com.krishu.finaceanomoly.LLM_Feature;

import com.krishu.finaceanomoly.DTO.CustomPolicyVerdict;
import com.krishu.finaceanomoly.DTO.LLmCategorizeResult;
import com.krishu.finaceanomoly.Model.Expense;
import com.krishu.finaceanomoly.Model.PolicyRule;

public interface LLMClient {
    LLmCategorizeResult categorize(Expense expense);
    CustomPolicyVerdict checkCustomPolicy(Expense expense, PolicyRule policy);
}
