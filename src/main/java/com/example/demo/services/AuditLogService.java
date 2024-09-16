package com.example.demo.services;

import com.example.demo.services.dtos.AuditLogDTO;
import java.util.List;
import java.util.UUID;

public interface AuditLogService {
    AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO);
    AuditLogDTO updateAuditLog(UUID id, AuditLogDTO auditLogDTO);
    void deleteAuditLog(UUID id);
    AuditLogDTO getAuditLogById(UUID id);
    List<AuditLogDTO> getAllAuditLogs();
}
