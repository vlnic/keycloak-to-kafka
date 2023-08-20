package com.github.vlnic.keycloak.event.provider;

import org.apache.kafka.common.serialization.StringSerializer;
import org.keycloak.Config.Scope;

import org.jboss.logging.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Properties;

public class KafkaConfig {

    private static final Logger log = Logger.getLogger(KafkaEventListenerProvider.class);

    private Properties properties;

    private String topicName;

    public KafkaConfig() {
        Properties props = new Properties();
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        log.info("initialized KafkaConfig");
        this.properties = props;
    }

    public static KafkaConfig createFromScope(Scope cnf) throws UnknownHostException {
        KafkaConfig config = new KafkaConfig();
        config.properties.put(
                "client.id",
                resolveConfigVar(cnf, "client_id", InetAddress.getLocalHost().getHostName())
        );
        config.properties.put(
                "bootstrap.servers",
                resolveConfigVar(cnf, "bootstrap_servers", "")
        );
        config.properties.put("acks", resolveConfigVar(cnf, "acks", "all"));

        return config;
    }

    public static String resolveConfigVar(Scope config, String varName, String defaultValue) {
        String value = defaultValue;
        if (config != null && config.get(varName) != null) {
            value = config.get(varName);
        } else {
            String envVariableName = "KK_TO_KAFKA_" + varName.toUpperCase(Locale.ENGLISH);
            String env = System.getenv(envVariableName);
            if (env != null) {
                value = env;
            }
        }
        return value;
    }

    public Properties getProperties() {
        return properties;
    }
}
