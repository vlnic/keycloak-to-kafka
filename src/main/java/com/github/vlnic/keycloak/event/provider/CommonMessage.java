package com.github.vlnic.keycloak.event.provider;

import com.dslplatform.json.DslJson;
import org.json.JSONException;
import org.json.JSONObject;
import org.keycloak.events.Event;

public class CommonMessage implements EventMessage {

    private final Event sourceEvent;

    public CommonMessage(Event event) {
        this.sourceEvent = event;
    }

    @Override
    public String messageType() {
        return "common";
    }

    @Override
    public String toJson() {
        DslJson<Object> json = new DslJson<Object>();
        
        JSONObject record = new JSONObject();
        try {
            record.put("event_type", "common");
            record.put("client_id", sourceEvent.getClientId());
            record.put("error", sourceEvent.getError());
            record.put("time", Long.valueOf(sourceEvent.getTime()));
            record.put("ip_address", sourceEvent.getIpAddress());
            record.put("realm_id", sourceEvent.getRealmId());
            record.put("session_id", sourceEvent.getSessionId());
            record.put("details", sourceEvent.getDetails());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return record.toString();
    }
}
