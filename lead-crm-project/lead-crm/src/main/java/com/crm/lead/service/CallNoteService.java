package com.crm.lead.service;

import com.crm.lead.dto.CallNoteRequest;
import com.crm.lead.dto.CallNoteResponse;
import com.crm.lead.exception.ResourceNotFoundException;
import com.crm.lead.model.CallNote;
import com.crm.lead.model.Counselor;
import com.crm.lead.model.Lead;
import com.crm.lead.repository.CallNoteRepository;
import com.crm.lead.repository.CounselorRepository;
import com.crm.lead.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CallNoteService {

    private final CallNoteRepository callNoteRepository;
    private final LeadRepository leadRepository;
    private final CounselorRepository counselorRepository;

    @Autowired
    public CallNoteService(CallNoteRepository callNoteRepository,
                           LeadRepository leadRepository,
                           CounselorRepository counselorRepository) {
        this.callNoteRepository = callNoteRepository;
        this.leadRepository = leadRepository;
        this.counselorRepository = counselorRepository;
    }

    @Transactional
    public CallNoteResponse addCallNote(CallNoteRequest request) {
        Lead lead = leadRepository.findById(request.getLeadId())
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + request.getLeadId()));

        CallNote.CallNoteBuilder builder = CallNote.builder()
                .lead(lead)
                .note(request.getNote())
                .callDurationMinutes(request.getCallDurationMinutes())
                .calledAt(request.getCalledAt());

        if (request.getCounselorId() != null) {
            Counselor counselor = counselorRepository.findById(request.getCounselorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Counselor not found with id: " + request.getCounselorId()));
            builder.counselor(counselor);
        }

        return mapToResponse(callNoteRepository.save(builder.build()));
    }

    public List<CallNoteResponse> getNotesByLead(Long leadId) {
        if (!leadRepository.existsById(leadId)) {
            throw new ResourceNotFoundException("Lead not found with id: " + leadId);
        }
        return callNoteRepository.findByLeadIdOrderByCalledAtDesc(leadId)
                .stream().map(this::mapToResponse).toList();
    }

    public List<CallNoteResponse> getNotesByCounselor(Long counselorId) {
        return callNoteRepository.findByCounselorId(counselorId)
                .stream().map(this::mapToResponse).toList();
    }

    private CallNoteResponse mapToResponse(CallNote note) {
        return CallNoteResponse.builder()
                .id(note.getId())
                .leadId(note.getLead().getId())
                .leadName(note.getLead().getName())
                .counselorId(note.getCounselor() != null ? note.getCounselor().getId() : null)
                .counselorName(note.getCounselor() != null ? note.getCounselor().getName() : null)
                .note(note.getNote())
                .callDurationMinutes(note.getCallDurationMinutes())
                .calledAt(note.getCalledAt())
                .createdAt(note.getCreatedAt())
                .build();
    }
}
