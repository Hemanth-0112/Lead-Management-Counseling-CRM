package com.crm.lead.observer;

import com.crm.lead.model.Lead;
import com.crm.lead.model.LeadHistory;
import com.crm.lead.repository.LeadHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer: Records lead change history automatically.
 */
@Component
public class LeadHistoryObserver implements LeadObserver {

    private final LeadHistoryRepository leadHistoryRepository;

    @Autowired
    public LeadHistoryObserver(LeadHistoryRepository leadHistoryRepository,
                                LeadEventPublisher publisher) {
        this.leadHistoryRepository = leadHistoryRepository;
        publisher.registerObserver(this);
    }

    @Override
    public void onLeadEvent(String eventType, Lead lead, String oldValue, String newValue) {
        LeadHistory history = LeadHistory.builder()
                .lead(lead)
                .fieldChanged(eventType)
                .oldValue(oldValue)
                .newValue(newValue)
                .changedBy("SYSTEM")
                .remarks("Auto-tracked by Observer")
                .build();
        leadHistoryRepository.save(history);
    }
}
