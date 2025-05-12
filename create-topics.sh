#!/bin/sh

echo "Create Kafka topics"

kafka-topics --bootstrap-server kafka:29092 --create --if-not-exists \
  --topic reservation-requests --replication-factor 1 --partitions 1

kafka-topics --bootstrap-server kafka:29092 --create --if-not-exists \
  --topic try-reserve-seats --replication-factor 1 --partitions 1

kafka-topics --bootstrap-server kafka:29092 --create --if-not-exists \
  --topic seats-reserved --replication-factor 1 --partitions 1

kafka-topics --bootstrap-server kafka:29092 --create --if-not-exists \
  --topic reservation-status-updates --replication-factor 1 --partitions 1

echo "All topics created"
