# Keycloak event listener for Apache Kafka

##### A Keycloak SPI plugin that publishes events to a Apache Kafka server.

#### Configuration
###### Recommended: OPTION 1: just configure **ENVIRONMENT VARIABLES**
- `KK_TO_KAFKA_URL` - default: *localhost*
- `KK_TO_KAFKA_SECURITY_PROTOCOL` - default: NULL
- `KK_TO_KAFKA_BOOTSTRAP_SERVERS` - default: localhost:9092