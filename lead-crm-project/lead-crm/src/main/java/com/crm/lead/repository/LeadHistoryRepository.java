package com.crm.lead.repository;

import com.crm.lead.model.LeadHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadHistoryRepository extends JpaRepository<LeadHistory, Long> {
    List<LeadHistory> findByLeadIdOrderByChangedAtDesc(Long leadId);
}
