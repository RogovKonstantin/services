package com.example.demo.services.Impl;

import com.example.demo.services.dtos.AuditLogDTO;
import com.example.demo.models.AuditLog;
import com.example.demo.models.Listing;
import com.example.demo.repositories.AuditLogRepository;
import com.example.demo.repositories.ListingRepository;
import com.example.demo.services.AuditLogService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ListingRepository listingRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AuditLogServiceImpl(AuditLogRepository auditLogRepository, ListingRepository listingRepository, ModelMapper modelMapper) {
        this.auditLogRepository = auditLogRepository;
        this.listingRepository = listingRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO) {
        AuditLog auditLog = modelMapper.map(auditLogDTO, AuditLog.class);

        Listing listing = listingRepository.findById(auditLogDTO.getListingId())
                .orElseThrow(() -> new RuntimeException("Listing not found"));
        auditLog.setListing(listing);

        AuditLog savedAuditLog = auditLogRepository.save(auditLog);
        return modelMapper.map(savedAuditLog, AuditLogDTO.class);
    }


    @Override
    public AuditLogDTO getAuditLogById(UUID id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuditLog not found"));
        return modelMapper.map(auditLog, AuditLogDTO.class);
    }

    @Override
    public List<AuditLogDTO> getAllAuditLogs() {
        List<AuditLog> auditLogs = auditLogRepository.findAll();
        return auditLogs.stream()
                .map(auditLog -> modelMapper.map(auditLog, AuditLogDTO.class))
                .collect(Collectors.toList());
    }
}
