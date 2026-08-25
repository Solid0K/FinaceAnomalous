package com.krishu.finaceanomoly.Controller;

import com.krishu.finaceanomoly.DTO.LogResponse;
import com.krishu.finaceanomoly.LogWriter;
import com.krishu.finaceanomoly.Service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/logs")
public class AuditLogController {

    private final AuditLogService logService;

    public AuditLogController(AuditLogService logService){
        this.logService=logService;
    }

    @GetMapping()
    public ResponseEntity<List<LogResponse>> getAllLogs(){
        return ResponseEntity.ok(logService.getAllLogs());
    }

    @GetMapping("/byExpense/{expenseId}")
    public ResponseEntity<List<LogResponse>> getLogsByExpense(@PathVariable UUID expenseId){
        return ResponseEntity.ok(logService.getLogsByExpense(expenseId));
    }

    @GetMapping("/byWriter/{writer}")
    public ResponseEntity<List<LogResponse>> getLogsByWriter(@PathVariable LogWriter writer){
        return ResponseEntity.ok(logService.getLogsByWriter(writer));
    }
}
