package com.crm.lead.dto;

import com.crm.lead.enums.LeadSource;
import com.crm.lead.enums.LeadStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class LeadResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String course;
    private String city;
    private LeadStatus status;
    private LeadSource source;
    private String assignedCounselorName;
    private Long assignedCounselorId;
    private LocalDateTime nextFollowUp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
