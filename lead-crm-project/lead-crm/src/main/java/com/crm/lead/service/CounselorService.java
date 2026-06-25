package com.crm.lead.service;

import com.crm.lead.dto.CounselorRequest;
import com.crm.lead.dto.CounselorResponse;
import com.crm.lead.exception.ResourceNotFoundException;
import com.crm.lead.model.Counselor;
import com.crm.lead.repository.CounselorRepository;
import com.crm.lead.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final LeadRepository leadRepository;

    @Autowired
    public CounselorService(CounselorRepository counselorRepository, LeadRepository leadRepository) {
        this.counselorRepository = counselorRepository;
        this.leadRepository = leadRepository;
    }

    @Transactional
    public CounselorResponse createCounselor(CounselorRequest request) {
        Counselor counselor = Counselor.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .department(request.getDepartment())
                .active(true)
                .build();
        return mapToResponse(counselorRepository.save(counselor));
    }

    public CounselorResponse getCounselorById(Long id) {
        Counselor counselor = counselorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Counselor not found with id: " + id));
        return mapToResponse(counselor);
    }

    public List<CounselorResponse> getAllCounselors() {
        return counselorRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public List<CounselorResponse> getActiveCounselors() {
        return counselorRepository.findByActive(true).stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public CounselorResponse updateCounselor(Long id, CounselorRequest request) {
        Counselor counselor = counselorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Counselor not found with id: " + id));
        counselor.setName(request.getName());
        counselor.setEmail(request.getEmail());
        counselor.setPhone(request.getPhone());
        counselor.setDepartment(request.getDepartment());
        return mapToResponse(counselorRepository.save(counselor));
    }

    @Transactional
    public CounselorResponse toggleActiveStatus(Long id) {
        Counselor counselor = counselorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Counselor not found with id: " + id));
        counselor.setActive(!counselor.isActive());
        return mapToResponse(counselorRepository.save(counselor));
    }

    private CounselorResponse mapToResponse(Counselor counselor) {
        long totalLeads = leadRepository.findByAssignedCounselorId(counselor.getId()).size();
        return CounselorResponse.builder()
                .id(counselor.getId())
                .name(counselor.getName())
                .email(counselor.getEmail())
                .phone(counselor.getPhone())
                .department(counselor.getDepartment())
                .active(counselor.isActive())
                .createdAt(counselor.getCreatedAt())
                .totalLeads(totalLeads)
                .build();
    }
}
