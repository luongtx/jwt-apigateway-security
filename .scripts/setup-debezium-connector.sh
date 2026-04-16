#!/bin/bash

# Register Debezium MySQL source connector for the transactional outbox pattern.
# This connector tails the outbox_events table and publishes the payload field
# to the fixed Kafka topic

set -e

KAFKA_CONNECT_URL="${KAFKA_CONNECT_URL:-http://localhost:8083}"
CONNECTOR_CONFIG_FILE=".scripts/config/debezium-mysql-outbox.json"

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
    --data "@${CONNECTOR_CONFIG_FILE}"
