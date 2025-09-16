#!/bin/bash

# Show logs for specific service or all services
SERVICE=${1:-""}

if [ -z "$SERVICE" ]; then
    echo "í³‹ Showing logs for all services (Ctrl+C to exit)..."
    docker-compose logs -f
else
    echo "í³‹ Showing logs for $SERVICE (Ctrl+C to exit)..."
    docker-compose logs -f "$SERVICE"
fi

