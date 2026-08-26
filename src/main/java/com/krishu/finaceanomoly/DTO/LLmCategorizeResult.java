package com.krishu.finaceanomoly.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishu.finaceanomoly.ExpenseCategory;

public record LLmCategorizeResult(ExpenseCategory category,
                                  Boolean anomalyDetected,
                                  Double confidence,
                                  String reasoning) {
    @JsonCreator
    public LLmCategorizeResult(@JsonProperty("category") String category,@JsonProperty("anomalyDetected") Boolean anomalyDetected
            ,@JsonProperty("confidence") Double confidence,@JsonProperty("reasoning") String reasoning){
        this(ExpenseCategory.getCategory(category),anomalyDetected,confidence,reasoning);
    }
}
