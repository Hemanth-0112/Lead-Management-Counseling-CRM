package com.crm.lead.controller;

import com.crm.lead.dto.ApiResponse;
import com.crm.lead.dto.CounselorRequest;
import com.crm.lead.dto.CounselorResponse;
import com.crm.lead.service.CounselorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/counselors")
@CrossOrigin(origins = "*")
public class CounselorController {

    private final CounselorService counselorService;

    @Autowired
    public CounselorController(CounselorService counselorService) {
        this.counselorService = counselorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CounselorResponse>> createCounselor(@Valid @RequestBody CounselorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Counselor created successfully", counselorService.createCounselor(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CounselorResponse>>> getAllCounselors() {
        return ResponseEntity.ok(ApiResponse.success("Counselors fetched", counselorService.getAllCounselors()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CounselorResponse>>> getActiveCounselors() {
        return ResponseEntity.ok(ApiResponse.success("Active counselors fetched", counselorService.getActiveCounselors()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CounselorResponse>> getCounselorById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Counselor fetched", counselorService.getCounselorById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CounselorResponse>> updateCounselor(@PathVariable Long id,
                                                                            @Valid @RequestBody CounselorRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Counselor updated", counselorService.updateCounselor(id, request)));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<CounselorResponse>> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Counselor status toggled", counselorService.toggleActiveStatus(id)));
    }
}
