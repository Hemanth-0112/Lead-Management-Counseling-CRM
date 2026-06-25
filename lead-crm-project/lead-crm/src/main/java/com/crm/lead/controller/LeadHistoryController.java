package com.crm.lead.controller;

import com.crm.lead.dto.ApiResponse;
import com.crm.lead.model.LeadHistory;
import com.crm.lead.repository.LeadHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lead-history")
@CrossOrigin(origins = "*")
public class LeadHistoryController {

    private final LeadHistoryRepository leadHistoryRepository;

    @Autowired
    public LeadHistoryController(LeadHistoryRepository leadHistoryRepository) {
        this.leadHistoryRepository = leadHistoryRepository;
    }

    @GetMapping("/lead/{leadId}")
    public ResponseEntity<ApiResponse<List<LeadHistory>>> getHistoryByLead(@PathVariable Long leadId) {
        List<LeadHistory> history = leadHistoryRepository.findByLeadIdOrderByChangedAtDesc(leadId);
        return ResponseEntity.ok(ApiResponse.success("Lead history fetched", history));
    }
}
