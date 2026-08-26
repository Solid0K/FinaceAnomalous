package com.krishu.finaceanomoly.LLM_Feature;

import com.krishu.finaceanomoly.DTO.LLmCategorizeResult;
import com.krishu.finaceanomoly.Model.Expense;

public interface LLMClient {
    LLmCategorizeResult categorize(Expense expense);
}
