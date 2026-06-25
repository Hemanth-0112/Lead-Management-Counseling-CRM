package com.crm.lead.observer;

import com.crm.lead.model.Lead;

public interface LeadObserver {
    void onLeadEvent(String eventType, Lead lead, String oldValue, String newValue);
}
