package com.github.vlnic.keycloak.event.provider;

import org.keycloak.events.Event;

import java.io.Serializable;

public class EventToRecord extends Event implements Serializable {

    public EventToRecord(Event event) {
        this.setClientId(event.getClientId());
        this.setDetails(event.getDetails());
        this.setError(event.getError());
        this.setIpAddress(event.getIpAddress());
        this.setRealmId(event.getRealmId());
        this.setSessionId(event.getSessionId());
        this.setTime(event.getTime());
        this.setType(event.getType());
        this.setUserId(event.getUserId());
    }
}
