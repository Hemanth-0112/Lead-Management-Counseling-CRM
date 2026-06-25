package com.crm.lead.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class DashboardResponse {
    private long totalLeads;
    private long newLeads;
    private long contactedLeads;
    private long followUpLeads;
    private long qualifiedLeads;
    private long convertedLeads;
    private long lostLeads;
    private Map<String, Long> leadsBySource;
    private Map<String, Long> leadsByCounselor;
    private long leadsFollowUpToday;
    private long unassignedLeads;
}
