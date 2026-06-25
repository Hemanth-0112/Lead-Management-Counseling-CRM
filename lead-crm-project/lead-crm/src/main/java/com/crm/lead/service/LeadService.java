package com.crm.lead.service;

import com.crm.lead.dto.*;
import com.crm.lead.enums.LeadSource;
import com.crm.lead.enums.LeadStatus;
import com.crm.lead.exception.ResourceNotFoundException;
import com.crm.lead.model.Counselor;
import com.crm.lead.model.Lead;
import com.crm.lead.observer.LeadEventPublisher;
import com.crm.lead.repository.CounselorRepository;
import com.crm.lead.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final CounselorRepository counselorRepository;
    private final LeadEventPublisher eventPublisher;

    @Autowired
    public LeadService(LeadRepository leadRepository,
                       CounselorRepository counselorRepository,
                       LeadEventPublisher eventPublisher) {
        this.leadRepository = leadRepository;
        this.counselorRepository = counselorRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public LeadResponse createLead(LeadRequest request) {
        Lead lead = Lead.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .course(request.getCourse())
                .city(request.getCity())
                .status(request.getStatus() != null ? request.getStatus() : LeadStatus.NEW)
                .source(request.getSource())
                .nextFollowUp(request.getNextFollowUp())
                .build();

        if (request.getCounselorId() != null) {
            Counselor counselor = counselorRepository.findById(request.getCounselorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Counselor not found with id: " + request.getCounselorId()));
            lead.setAssignedCounselor(counselor);
        }

        Lead saved = leadRepository.save(lead);
        eventPublisher.notifyObservers("LEAD_CREATED", saved, null, saved.getStatus().name());
        return mapToResponse(saved);
    }

    public LeadResponse getLeadById(Long id) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
        return mapToResponse(lead);
    }

    public List<LeadResponse> getAllLeads() {
        return leadRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public LeadResponse updateLead(Long id, LeadRequest request) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));

        lead.setName(request.getName());
        lead.setEmail(request.getEmail());
        lead.setPhone(request.getPhone());
        lead.setCourse(request.getCourse());
        lead.setCity(request.getCity());

        if (request.getNextFollowUp() != null) {
            String oldFollowUp = lead.getNextFollowUp() != null ? lead.getNextFollowUp().toString() : null;
            lead.setNextFollowUp(request.getNextFollowUp());
            eventPublisher.notifyObservers("FOLLOW_UP_SCHEDULED", lead, oldFollowUp, request.getNextFollowUp().toString());
        }

        Lead saved = leadRepository.save(lead);
        return mapToResponse(saved);
    }

    @Transactional
    public LeadResponse updateLeadStatus(Long id, LeadStatus newStatus) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));

        String oldStatus = lead.getStatus().name();
        lead.setStatus(newStatus);
        Lead saved = leadRepository.save(lead);

        eventPublisher.notifyObservers("STATUS_CHANGED", saved, oldStatus, newStatus.name());
        return mapToResponse(saved);
    }

    @Transactional
    public LeadResponse assignLead(Long leadId, Long counselorId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + leadId));
        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new ResourceNotFoundException("Counselor not found with id: " + counselorId));

        String oldCounselor = lead.getAssignedCounselor() != null ? lead.getAssignedCounselor().getName() : "Unassigned";
        lead.setAssignedCounselor(counselor);
        Lead saved = leadRepository.save(lead);

        eventPublisher.notifyObservers("ASSIGNED", saved, oldCounselor, counselor.getName());
        return mapToResponse(saved);
    }

    @Transactional
    public LeadResponse scheduleFollowUp(Long id, LocalDateTime followUpTime) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));

        String oldFollowUp = lead.getNextFollowUp() != null ? lead.getNextFollowUp().toString() : null;
        lead.setNextFollowUp(followUpTime);
        Lead saved = leadRepository.save(lead);

        eventPublisher.notifyObservers("FOLLOW_UP_SCHEDULED", saved, oldFollowUp, followUpTime.toString());
        return mapToResponse(saved);
    }

    public List<LeadResponse> searchLeads(LeadStatus status, LeadSource source,
                                           Long counselorId, String course,
                                           String city, String keyword) {
        return leadRepository.searchLeads(status, source, counselorId, course, city, keyword)
                .stream().map(this::mapToResponse).toList();
    }

    public List<LeadResponse> getLeadsByStatus(LeadStatus status) {
        return leadRepository.findByStatus(status).stream().map(this::mapToResponse).toList();
    }

    public List<LeadResponse> getLeadsByCounselor(Long counselorId) {
        return leadRepository.findByAssignedCounselorId(counselorId).stream().map(this::mapToResponse).toList();
    }

    public void deleteLead(Long id) {
        if (!leadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lead not found with id: " + id);
        }
        leadRepository.deleteById(id);
    }

    private LeadResponse mapToResponse(Lead lead) {
        return LeadResponse.builder()
                .id(lead.getId())
                .name(lead.getName())
                .email(lead.getEmail())
                .phone(lead.getPhone())
                .course(lead.getCourse())
                .city(lead.getCity())
                .status(lead.getStatus())
                .source(lead.getSource())
                .assignedCounselorId(lead.getAssignedCounselor() != null ? lead.getAssignedCounselor().getId() : null)
                .assignedCounselorName(lead.getAssignedCounselor() != null ? lead.getAssignedCounselor().getName() : null)
                .nextFollowUp(lead.getNextFollowUp())
                .createdAt(lead.getCreatedAt())
                .updatedAt(lead.getUpdatedAt())
                .build();
    }
}
