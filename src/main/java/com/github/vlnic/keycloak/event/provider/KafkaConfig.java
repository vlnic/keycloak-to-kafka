package com.github.vlnic.keycloak.event.provider;

import org.apache.kafka.common.serialization.StringSerializer;
import org.keycloak.Config.Scope;

import org.jboss.logging.Logger;

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

        log.info("initialized KafkaConfig");
        this.properties = props;
    }

    public static KafkaConfig createFromScope(Scope cnf) {
        KafkaConfig config = new KafkaConfig();
        config.properties.put(
                "bootstrap.servers",
                resolveConfigVar(cnf, "kafka_bootstrap_servers", "")
        );
        config.topicName = resolveConfigVar(cnf, "kafka_topic_name", "keycloak-events");

        config.properties.put("acks", resolveConfigVar(cnf, "acks", "all"));
        if (resolveConfigVar(cnf, "kafka_security_protocol", null) != null) {
            config.properties.put("security.protocol", "SASL_SSL");
            config.properties.put("sasl.mechanism", "PLAIN");
            config.properties.put("sasl.jaas.config", prepareSaslConfig(cnf));
        }
        config.properties.put("retries", 3);
        config.properties.put("max.request.size", 1024 * 1024);

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
        String streamPoolId = resolveConfigVar(cnf, "kafka_stream_pool_id", "");
        String tenancyName = resolveConfigVar(cnf, "kafka_tenancy_name", "");

        return System.out.printf(
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=%s\"/%s/%s\"password=\"%s\";",
                tenancyName,
                username,
                streamPoolId,
                authToken
        ).toString();
    }

    public Properties getProperties() {
        return properties;
    }

    public String getTopicName() { return topicName; }
}
