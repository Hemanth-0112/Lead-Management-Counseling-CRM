package com.crm.lead.controller;

import com.crm.lead.dto.ApiResponse;
import com.crm.lead.dto.CallNoteRequest;
import com.crm.lead.dto.CallNoteResponse;
import com.crm.lead.service.CallNoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/call-notes")
@CrossOrigin(origins = "*")
public class CallNoteController {

    private final CallNoteService callNoteService;

    @Autowired
    public CallNoteController(CallNoteService callNoteService) {
        this.callNoteService = callNoteService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CallNoteResponse>> addNote(@Valid @RequestBody CallNoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Call note added", callNoteService.addCallNote(request)));
    }

    @GetMapping("/lead/{leadId}")
    public ResponseEntity<ApiResponse<List<CallNoteResponse>>> getNotesByLead(@PathVariable Long leadId) {
        return ResponseEntity.ok(ApiResponse.success("Call notes fetched", callNoteService.getNotesByLead(leadId)));
    }

    @GetMapping("/counselor/{counselorId}")
    public ResponseEntity<ApiResponse<List<CallNoteResponse>>> getNotesByCounselor(@PathVariable Long counselorId) {
        return ResponseEntity.ok(ApiResponse.success("Call notes fetched", callNoteService.getNotesByCounselor(counselorId)));
    }
}
