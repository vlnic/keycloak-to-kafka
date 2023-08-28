package com.github.vlnic.keycloak.event.provider;

import org.json.JSONException;
import org.keycloak.events.Event;

import java.io.Serializable;
import org.json.JSONObject;

public class EventToRecord extends Event implements Serializable {
    public static final String USER_EVENT = "user_event";

    public static final String ADMIN_EVENT = "admin_event";

    private String type;

    public EventToRecord(Event event, String eventType) {
        this.setClientId(event.getClientId());
        this.setDetails(event.getDetails());
        this.setError(event.getError());
        this.setIpAddress(event.getIpAddress());
        this.setRealmId(event.getRealmId());
        this.setSessionId(event.getSessionId());
        this.setTime(event.getTime());
        this.setType(event.getType());
        this.setUserId(event.getUserId());
        this.type = eventType;
    }

    public String toJson() {
        JSONObject record = new JSONObject();
        try {
            record.put("event_type", this.type);
            record.put("client_id", this.getClientId());
            record.put("error", this.getError());
            record.put("time", Long.valueOf(this.getTime()));
            record.put("ip_address", this.getIpAddress());
            record.put("realm_id", this.getRealmId());
            record.put("session_id", this.getSessionId());
            record.put("details", this.getDetails());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return record.toString();
    }
}
