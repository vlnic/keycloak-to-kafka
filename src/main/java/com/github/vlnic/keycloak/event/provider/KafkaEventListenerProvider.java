package com.github.vlnic.keycloak.event.provider;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerTransaction;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;

public class KafkaEventListenerProvider implements EventListenerProvider {

    private final KafkaConfig cnf;

    private static final Logger log = Logger.getLogger(KafkaEventListenerProvider.class);

    private final EventListenerTransaction tx = new EventListenerTransaction(this::publishAdminEvent, this::publishEvent);

    private KafkaProducer producer;

    public KafkaEventListenerProvider(KafkaConfig config, KeycloakSession session) {
        this.cnf = config;
        this.producer = new KafkaProducer(config.getProperties());
        session.getTransactionManager().enlistAfterCompletion(tx);
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
        log.info("publish common event");
        EventToRecord eventRecord = new EventToRecord(event);
        producer.send(
                new ProducerRecord("user_events", event.hashCode(), eventRecord),
                new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e != null) {
                            log.error("Send failed for record: {}", eventRecord, e);
                        }
                    }
                }
        );
    }

    private void publishAdminEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        log.info("publish admin event");
    }

    public KafkaProducer getProducer() {
        return producer;
    }
}
