package com.crm.lead.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CounselorResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private boolean active;
    private LocalDateTime createdAt;
    private long totalLeads;
}
