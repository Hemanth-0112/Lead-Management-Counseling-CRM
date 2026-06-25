package com.crm.lead.controller;

import com.crm.lead.dto.ApiResponse;
import com.crm.lead.dto.LeadRequest;
import com.crm.lead.dto.LeadResponse;
import com.crm.lead.enums.LeadSource;
import com.crm.lead.enums.LeadStatus;
import com.crm.lead.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leads")
@CrossOrigin(origins = "*")
public class LeadController {

    private final LeadService leadService;

    @Autowired
    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<LeadResponse>> createLead(@Valid @RequestBody LeadRequest request) {
        LeadResponse response = leadService.createLead(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lead created successfully", response));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<LeadResponse>>> getAllLeads() {
        return ResponseEntity.ok(ApiResponse.success("Leads fetched successfully", leadService.getAllLeads()));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeadResponse>> getLeadById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Lead fetched successfully", leadService.getLeadById(id)));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LeadResponse>> updateLead(@PathVariable Long id,
                                                                  @Valid @RequestBody LeadRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Lead updated successfully", leadService.updateLead(id, request)));
    }

    // UPDATE STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<LeadResponse>> updateStatus(@PathVariable Long id,
                                                                    @RequestBody Map<String, String> body) {
        LeadStatus status = LeadStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(ApiResponse.success("Status updated successfully", leadService.updateLeadStatus(id, status)));
    }

    // ASSIGN TO COUNSELOR
    @PatchMapping("/{leadId}/assign/{counselorId}")
    public ResponseEntity<ApiResponse<LeadResponse>> assignLead(@PathVariable Long leadId,
                                                                  @PathVariable Long counselorId) {
        return ResponseEntity.ok(ApiResponse.success("Lead assigned successfully", leadService.assignLead(leadId, counselorId)));
    }

    // SCHEDULE FOLLOW-UP
    @PatchMapping("/{id}/follow-up")
    public ResponseEntity<ApiResponse<LeadResponse>> scheduleFollowUp(@PathVariable Long id,
                                                                        @RequestBody Map<String, String> body) {
        LocalDateTime followUpTime = LocalDateTime.parse(body.get("followUpTime"));
        return ResponseEntity.ok(ApiResponse.success("Follow-up scheduled", leadService.scheduleFollowUp(id, followUpTime)));
    }

    // SEARCH & FILTER
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<LeadResponse>>> searchLeads(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) Long counselorId,
            @RequestParam(required = false) String course,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String keyword) {

        LeadStatus leadStatus = status != null ? LeadStatus.valueOf(status.toUpperCase()) : null;
        LeadSource leadSource = source != null ? LeadSource.valueOf(source.toUpperCase()) : null;

        List<LeadResponse> results = leadService.searchLeads(leadStatus, leadSource, counselorId, course, city, keyword);
        return ResponseEntity.ok(ApiResponse.success("Search results fetched", results));
    }

    // GET BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<LeadResponse>>> getByStatus(@PathVariable String status) {
        LeadStatus leadStatus = LeadStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(ApiResponse.success("Leads fetched by status", leadService.getLeadsByStatus(leadStatus)));
    }

    // GET BY COUNSELOR
    @GetMapping("/counselor/{counselorId}")
    public ResponseEntity<ApiResponse<List<LeadResponse>>> getByCounselor(@PathVariable Long counselorId) {
        return ResponseEntity.ok(ApiResponse.success("Leads fetched by counselor", leadService.getLeadsByCounselor(counselorId)));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        return ResponseEntity.ok(ApiResponse.success("Lead deleted successfully", null));
    }
}
