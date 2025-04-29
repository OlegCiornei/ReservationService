#!/bin/sh

echo "Create topics"

kafka-topics --bootstrap-server kafka:9092 --create --if-not-exists \
  --topic reservation-requests --replication-factor 1 --partitions 1

kafka-topics --bootstrap-server kafka:9092 --create --if-not-exists \
  --topic reservation-status-updates --replication-factor 1 --partitions 1

echo "Topics created"
