package com.github.vlnic.keycloak.event.provider;

import org.apache.kafka.common.serialization.StringSerializer;
import org.keycloak.Config.Scope;

import org.jboss.logging.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Properties;
import org.apache.kafka.clients.producer.ProducerConfig;

public class KafkaConfig {

    private static final Logger log = Logger.getLogger(KafkaEventListenerProvider.class);

    private Properties properties;

    private String topicName;

    public KafkaConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "PLAIN");
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
        String authToken = resolveConfigVar(cnf, "kafka_auth_token", "");
        String username = resolveConfigVar(cnf, "kafka_username", "");

        config.properties.put("sasl.jaas.config", prepareSaslConfig(cnf));

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

    private static String prepareSaslConfig(Scope cnf) {
        String authToken = resolveConfigVar(cnf, "kafka_auth_token", "");
        String username = resolveConfigVar(cnf, "kafka_username", "");

        return System.out.printf(
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=%s\\password=%s",
                username,
                authToken
        ).toString();
    }

    public Properties getProperties() {
        return properties;
    }
}
