package com.crm.lead.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CallNoteResponse {
    private Long id;
    private Long leadId;
    private String leadName;
    private Long counselorId;
    private String counselorName;
    private String note;
    private Integer callDurationMinutes;
    private LocalDateTime calledAt;
    private LocalDateTime createdAt;
}
