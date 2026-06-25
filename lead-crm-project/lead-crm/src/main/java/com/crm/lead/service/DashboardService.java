package com.crm.lead.service;

import com.crm.lead.dto.DashboardResponse;
import com.crm.lead.enums.LeadStatus;
import com.crm.lead.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final LeadRepository leadRepository;

    @Autowired
    public DashboardService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public DashboardResponse getDashboard() {
        long totalLeads = leadRepository.count();

        // Counts by status
        long newLeads = leadRepository.countByStatus(LeadStatus.NEW);
        long contactedLeads = leadRepository.countByStatus(LeadStatus.CONTACTED);
        long followUpLeads = leadRepository.countByStatus(LeadStatus.FOLLOW_UP);
        long qualifiedLeads = leadRepository.countByStatus(LeadStatus.QUALIFIED);
        long convertedLeads = leadRepository.countByStatus(LeadStatus.CONVERTED);
        long lostLeads = leadRepository.countByStatus(LeadStatus.LOST);

        // Leads by source
        Map<String, Long> leadsBySource = new HashMap<>();
        List<Object[]> sourceData = leadRepository.countBySource();
        for (Object[] row : sourceData) {
            leadsBySource.put(row[0].toString(), (Long) row[1]);
        }

        // Leads by counselor
        Map<String, Long> leadsByCounselor = new HashMap<>();
        List<Object[]> counselorData = leadRepository.countByCounselor();
        for (Object[] row : counselorData) {
            leadsByCounselor.put(row[0].toString(), (Long) row[1]);
        }

        // Follow-ups today
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
        long followUpToday = leadRepository.findByNextFollowUpBetween(startOfDay, endOfDay).size();

        // Unassigned leads
        long unassignedLeads = leadRepository.searchLeads(null, null, null, null, null, null)
                .stream().filter(l -> l.getAssignedCounselor() == null).count();

        return DashboardResponse.builder()
                .totalLeads(totalLeads)
                .newLeads(newLeads)
                .contactedLeads(contactedLeads)
                .followUpLeads(followUpLeads)
                .qualifiedLeads(qualifiedLeads)
                .convertedLeads(convertedLeads)
                .lostLeads(lostLeads)
                .leadsBySource(leadsBySource)
                .leadsByCounselor(leadsByCounselor)
                .leadsFollowUpToday(followUpToday)
                .unassignedLeads(unassignedLeads)
                .build();
    }
}
