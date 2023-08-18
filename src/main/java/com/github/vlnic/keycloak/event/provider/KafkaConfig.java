package com.github.vlnic.keycloak.event.provider;

public class KafkaConfig {
    private static final String DEFAULT_TOPIC_NAME = "keycloak_events";

    protected String kafkaVersion;

    protected String userEventTopic;

    protected String adminEventTopic;

    public KafkaConfig() {

    }

    public KafkaConfig setUserEventTopic(String topic) {
        this.userEventTopic = topic;
        return this;
    }

    public KafkaConfig setAdminEventTopic(String topic) {
        this.adminEventTopic = topic;
        return this;
    }

    public KafkaConfig setKafkaVersion(String kafkaVersion) {
        this.kafkaVersion = kafkaVersion;
        return this;
    }

    public String getKafkaVersion() {
        return kafkaVersion;
    }

    public String getAdminEventTopic() {
        return adminEventTopic;
    }

    public String getUserEventTopic() {
        return userEventTopic;
    }
    public String defaultTopicName() {
        return "keycloak_events";
    }
    public String getTopic() {
        return "keycloack-events";
    }
}
