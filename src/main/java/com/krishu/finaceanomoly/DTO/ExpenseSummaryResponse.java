package com.krishu.finaceanomoly.DTO;

import com.krishu.finaceanomoly.ExpenseCategory;
import com.krishu.finaceanomoly.ExpenseStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record ExpenseSummaryResponse(BigDecimal totalAmount, Long totalCount,
                                     Map<ExpenseCategory,BigDecimal> amountByCategory, Map<ExpenseStatus,Long> byStatus, LocalDate StartDate,
                                     LocalDate EndDate) {
}
