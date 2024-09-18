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

    private AuditLogRepository auditLogRepository;

    private ListingRepository listingRepository;

    private ModelMapper modelMapper;

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
    public AuditLogDTO updateAuditLog(UUID id, AuditLogDTO auditLogDTO) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuditLog not found"));

        modelMapper.map(auditLogDTO, auditLog);
        AuditLog updatedAuditLog = auditLogRepository.save(auditLog);
        return modelMapper.map(updatedAuditLog, AuditLogDTO.class);
    }

    @Override
    public void deleteAuditLog(UUID id) {
        auditLogRepository.deleteById(id);
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

    @Autowired
    public void setAuditLogRepository(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Autowired
    public void setListingRepository(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
