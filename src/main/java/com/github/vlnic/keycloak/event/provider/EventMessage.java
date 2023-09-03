package com.github.vlnic.keycloak.event.provider;

public interface EventMessage {
    String messageType();

    String toJson();
}
