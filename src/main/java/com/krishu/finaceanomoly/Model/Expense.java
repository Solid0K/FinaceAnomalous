package com.krishu.finaceanomoly.Model;

import com.krishu.finaceanomoly.ExpenseStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String vendor;
    private BigDecimal amount;
    private String currency;
    private LocalDate expenseDate;
    private String submittedBy;
    private String description;
    private String category;

    @Enumerated(EnumType.STRING)
    private ExpenseStatus status;

    private String aiCategory;
    private String aiConfidence;
    private String aiAnomalyFlag;
    private String aiReasoning;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
