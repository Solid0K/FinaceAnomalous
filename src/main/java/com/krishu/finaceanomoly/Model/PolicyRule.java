package com.krishu.finaceanomoly.Model;

import com.krishu.finaceanomoly.ExpenseCategory;
import com.krishu.finaceanomoly.PolicyPeriod;
import com.krishu.finaceanomoly.RuleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class PolicyRule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RuleType type;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;
    private BigDecimal amountThreshold;
    private BigDecimal categoryThreshold;
    private Integer duplicateWindowDay;
    private boolean active;
    @Enumerated(EnumType.STRING)
    private PolicyPeriod period;
}
