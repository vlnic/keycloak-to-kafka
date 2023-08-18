package com.github.vlnic.keycloak.event.provider;

import javax.annotation.Nullable;
import java.util.HashMap;

public class KafkaProviderConfig {
    private static final String DEFAULT_TOPIC_NAME = "keycloak_events";

    @Nullable
    protected String userEventTopic;

    @Nullable
    protected String adminEventTopic;

    public KafkaProviderConfig(HashMap properties) {
        this.adminEventTopic = (String) properties.get("admin_event_topic");
        this.userEventTopic = (String) properties.get("user_event_topic");
    }

    public KafkaProviderConfig setUserEventTopic(String topic) {
        this.userEventTopic = topic;
        return this;
    }

    public KafkaProviderConfig setAdminEventTopic(String topic) {
        this.adminEventTopic = topic;
        return this;
    }

    public String getAdminEventTopic() {
        if (adminEventTopic == null) {
            return DEFAULT_TOPIC_NAME;
        }
        return adminEventTopic;
    }

    public String getUserEventTopic() {
        if (userEventTopic == null) {
            return DEFAULT_TOPIC_NAME;
        }
        return userEventTopic;
    }
}
