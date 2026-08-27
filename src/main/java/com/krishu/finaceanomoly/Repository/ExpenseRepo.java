package com.krishu.finaceanomoly.Repository;

import com.krishu.finaceanomoly.ExpenseCategory;
import com.krishu.finaceanomoly.ExpenseStatus;
import com.krishu.finaceanomoly.Model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepo extends JpaRepository<Expense, UUID> {
    List<Expense> findByVendorAndAmountAndExpenseDateBetween(String vendor, BigDecimal amount, LocalDate start, LocalDate end);
    List<Expense> findByStatus(ExpenseStatus status);
    @Query("select coalesce(sum(e.amount),0) from Expense e where e.category=:category and e.id!=:expenseId and e.expenseDate between :start and :end and e.status=APPROVED")
    BigDecimal sumOfExpensesByCategory(@Param("category") ExpenseCategory category,
                                       @Param("expenseId") UUID expenseId,@Param("start") LocalDate start,@Param("end") LocalDate end);
    List<Expense> findExpenseByExpenseDateBetween(LocalDate start,LocalDate end);
}
