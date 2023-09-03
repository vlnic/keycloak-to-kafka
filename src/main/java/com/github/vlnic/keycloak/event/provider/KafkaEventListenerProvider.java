package com.github.vlnic.keycloak.event.provider;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerTransaction;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;

public class KafkaEventListenerProvider implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(KafkaEventListenerProvider.class);

    private final EventListenerTransaction tx = new EventListenerTransaction(this::publishAdminEvent, this::publishEvent);

    private final KafkaProducer producer;

    private final String topic;

    private final KafkaConfig config;

    private KeycloakSession session;

    public KafkaEventListenerProvider(KafkaConfig config, KeycloakSession session) {
        this.config = config;
        this.session = session;
        this.topic = config.getDefaultTopic();
        this.producer = new KafkaProducer<>(config.getProperties());
        this.session.getTransactionManager().enlistAfterCompletion(tx);
    }

    @Override
    public void onEvent(Event event) {
        tx.addEvent(event);
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        tx.addAdminEvent(adminEvent, b);
    }

    @Override
    public void close() {

    }

    private void publishEvent(Event event) {
        try {
            EventToRecord eventRecord = new EventToRecord(event, EventToRecord.USER_EVENT);
            ProducerRecord<String, String> record = new ProducerRecord<>(config.getDefaultTopic(), event.getId(), eventRecord.toJson());
            producer.send(record, (md, ex) -> {
                if (ex != null) {
                    log.error("exception occurred in producer for review :" + record.value() + ", exception is " + ex);
                    ex.printStackTrace();
                } else {
                    log.debug("Sent msg to " + md.partition() + " with offset " + md.offset() + " at " + md.timestamp());
                }
            });
            producer.flush();
            producer.close();
        } catch (Exception e) {
            log.error("keycloak-to-kafka: failed produce event: " + e.getMessage());
        }
    }

    private void publishAdminEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        log.info("publish admin event");
    }

    private void produce() {

    }
}
