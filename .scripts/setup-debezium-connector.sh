#!/bin/bash

# Register Debezium MySQL source connector for the transactional outbox pattern.
# This connector tails the outbox_events table and publishes the payload field
# to the fixed Kafka topic order-topic, while copying event_type into a header.

set -e

KAFKA_CONNECT_URL="${KAFKA_CONNECT_URL:-http://localhost:8083}"

echo "Waiting for Kafka Connect to be ready..."
until curl -s -f "${KAFKA_CONNECT_URL}/" > /dev/null 2>&1; do
    sleep 2
done
echo "Kafka Connect is ready."

CONNECTOR_NAME="mysql-outbox-source"

echo "Registering Debezium MySQL source connector..."

curl -i -X POST \
    -H "Accept:application/json" \
    -H "Content-Type:application/json" \
    "${KAFKA_CONNECT_URL}/connectors/" \
    -d '{
        "name": "mysql-outbox-source",
        "config": {
            "connector.class": "io.debezium.connector.mysql.MySqlConnector",
            "tasks.max": "1",
            "database.hostname": "mysql",
            "database.port": "3306",
            "database.user": "root",
            "database.password": "Password",
            "database.server.id": "184054",
            "database.server.name": "mysql-server",
            "database.include.list": "restaurant_db",
            "table.include.list": "restaurant_db.outbox_events",
            "topic.prefix": "dbz",
            "schema.history.internal.kafka.bootstrap.servers": "kafka:29092",
            "schema.history.internal.kafka.topic": "dbz_schema_history",
            "key.converter": "org.apache.kafka.connect.json.JsonConverter",
            "key.converter.schemas.enable": "false",
            "value.converter": "org.apache.kafka.connect.json.JsonConverter",
            "value.converter.schemas.enable": "false",
            "transforms": "outbox",
            "transforms.outbox.type": "io.debezium.transforms.outbox.EventRouter",
            "transforms.outbox.table.field.event.id": "id",
            "transforms.outbox.table.field.event.key": "id",
            "transforms.outbox.table.field.event.type": "event_type",
            "transforms.outbox.table.field.event.payload": "payload",
            "transforms.outbox.route.by.field": "aggregate_type",
            "transforms.outbox.route.topic.replacement": "order-topic",
            "transforms.outbox.value.field": "payload"
        }
    }'

echo ""
echo "Connector 'mysql-outbox-source' registered successfully."
echo "Debezium will begin tailing the outbox_events table and publishing to order-topic."
