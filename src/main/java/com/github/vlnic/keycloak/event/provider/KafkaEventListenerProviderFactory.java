package com.github.vlnic.keycloak.event.provider;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class KafkaEventListenerProviderFactory implements EventListenerProviderFactory{

    private Config.Scope cnf;

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        KafkaConfig config = KafkaConfig.createFromScope(this.cnf);
        return new KafkaEventListenerProvider(config, keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {
        this.cnf = scope;
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {}

    @Override
    public String getId() {
        return "keycloak-to-kafka";
    }
}
