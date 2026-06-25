package com.crm.lead.dto;

import com.crm.lead.enums.LeadSource;
import com.crm.lead.enums.LeadStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LeadRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String course;
    private String city;
    private LeadStatus status;
    private LeadSource source;
    private Long counselorId;
    private LocalDateTime nextFollowUp;
}
