package com.krishu.finaceanomoly.DTO;

import com.krishu.finaceanomoly.CustomException.InvalidDecisionException;
import com.krishu.finaceanomoly.ExpenseStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequest(@NotNull ExpenseStatus decision,@Size(max=500) String note) {
    public ReviewRequest {
        if(decision!= ExpenseStatus.APPROVED && decision!=ExpenseStatus.REJECTED){
            throw new InvalidDecisionException("Decision must be APPROVED or REJECTED");
        }
    }
}
