package com.crm.lead.observer;

import com.crm.lead.model.Lead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer: Sends notifications (logs) on important lead events.
 * In a real system, this would send emails/SMS/push notifications.
 */
@Component
public class LeadNotificationObserver implements LeadObserver {

    private static final Logger logger = LoggerFactory.getLogger(LeadNotificationObserver.class);

    @Autowired
    public LeadNotificationObserver(LeadEventPublisher publisher) {
        publisher.registerObserver(this);
    }

    @Override
    public void onLeadEvent(String eventType, Lead lead, String oldValue, String newValue) {
        switch (eventType) {
            case "STATUS_CHANGED" ->
                logger.info("[NOTIFICATION] Lead '{}' (ID: {}) status changed: {} -> {}",
                        lead.getName(), lead.getId(), oldValue, newValue);
            case "ASSIGNED" ->
                logger.info("[NOTIFICATION] Lead '{}' (ID: {}) assigned to counselor: {}",
                        lead.getName(), lead.getId(), newValue);
            case "FOLLOW_UP_SCHEDULED" ->
                logger.info("[NOTIFICATION] Follow-up scheduled for lead '{}' (ID: {}) at {}",
                        lead.getName(), lead.getId(), newValue);
            case "LEAD_CREATED" ->
                logger.info("[NOTIFICATION] New lead created: '{}' (ID: {})",
                        lead.getName(), lead.getId());
            default ->
                logger.info("[NOTIFICATION] Event '{}' on lead '{}' (ID: {})",
                        eventType, lead.getName(), lead.getId());
        }
    }
}
