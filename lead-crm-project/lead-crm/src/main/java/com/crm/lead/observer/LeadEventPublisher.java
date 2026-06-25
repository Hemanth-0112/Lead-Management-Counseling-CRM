package com.crm.lead.observer;

import com.crm.lead.model.Lead;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject in the Observer Design Pattern.
 * Maintains a list of observers and notifies them on lead events.
 */
@Component
public class LeadEventPublisher {

    private final List<LeadObserver> observers = new ArrayList<>();

    public void registerObserver(LeadObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(LeadObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String eventType, Lead lead, String oldValue, String newValue) {
        for (LeadObserver observer : observers) {
            observer.onLeadEvent(eventType, lead, oldValue, newValue);
        }
    }
}
