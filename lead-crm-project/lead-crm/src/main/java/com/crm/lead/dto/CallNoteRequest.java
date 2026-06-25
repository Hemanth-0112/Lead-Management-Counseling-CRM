package com.crm.lead.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CallNoteRequest {

    @NotNull(message = "Lead ID is required")
    private Long leadId;

    private Long counselorId;

    @NotBlank(message = "Note is required")
    private String note;

    private Integer callDurationMinutes;

    private LocalDateTime calledAt;
}
