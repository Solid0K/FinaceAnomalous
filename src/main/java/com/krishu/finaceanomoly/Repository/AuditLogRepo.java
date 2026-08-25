package com.krishu.finaceanomoly.Repository;

import com.krishu.finaceanomoly.LogWriter;
import org.springframework.data.jpa.repository.JpaRepository;
import com.krishu.finaceanomoly.Model.AuditLog;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepo extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByExpenseId(UUID expenseId);
    List<AuditLog> findByExpenseIdOrderByTimeStampAsc(UUID expenseId);
    List<AuditLog> findByWriterOrderByTimeStampDesc(LogWriter writer);
    List<AuditLog> findAllByOrderByTimeStampDesc();
}
