package com.example.demo.services.Impl;

import com.example.demo.services.dtos.AuditLogDTO;
import com.example.demo.models.AuditLog;
import com.example.demo.models.Listing;
import com.example.demo.repositories.AuditLogRepository;
import com.example.demo.repositories.ListingRepository;
import com.example.demo.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Override
    public AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(auditLogDTO.getAction());
        auditLog.setDetails(auditLogDTO.getDetails());

        Listing listing = listingRepository.findById(auditLogDTO.getListingId())
                .orElseThrow(() -> new RuntimeException("Listing not found"));
        auditLog.setListing(listing);

        AuditLog savedAuditLog = auditLogRepository.save(auditLog);
        return convertToDTO(savedAuditLog);
    }

    @Override
    public AuditLogDTO updateAuditLog(UUID id, AuditLogDTO auditLogDTO) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuditLog not found"));

        auditLog.setAction(auditLogDTO.getAction());
        auditLog.setDetails(auditLogDTO.getDetails());

        AuditLog updatedAuditLog = auditLogRepository.save(auditLog);
        return convertToDTO(updatedAuditLog);
    }

    @Override
    public void deleteAuditLog(UUID id) {
        auditLogRepository.deleteById(id);
    }

    @Override
    public AuditLogDTO getAuditLogById(UUID id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuditLog not found"));
        return convertToDTO(auditLog);
    }

    @Override
    public List<AuditLogDTO> getAllAuditLogs() {
        List<AuditLog> auditLogs = auditLogRepository.findAll();
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private AuditLogDTO convertToDTO(AuditLog auditLog) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(auditLog.getId());
        dto.setAction(auditLog.getAction());
        dto.setDetails(auditLog.getDetails());
        dto.setListingId(auditLog.getListing().getId());
        return dto;
    }
}
