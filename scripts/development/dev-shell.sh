#!/bin/bash

# Open shell in specific service container
SERVICE=${1:-"backend"}

echo "Ì∞ö Opening shell in $SERVICE container..."

# Check if service is running
if ! docker-compose ps | grep -q "$SERVICE.*Up"; then
    echo "‚ùå Service $SERVICE is not running!"
    echo "Available running services:"
    docker-compose ps --services
    exit 1
fi

# Determine shell based on service
case $SERVICE in
    "backend")
        docker-compose exec "$SERVICE" bash
        ;;
    "frontend"|"ai-service")
        docker-compose exec "$SERVICE" sh
        ;;
    "postgres")
        docker-compose exec "$SERVICE" psql -U $DB_USERNAME -d $DB_NAME
        ;;
    "redis")
        docker-compose exec "$SERVICE" redis-cli
        ;;
    *)
        docker-compose exec "$SERVICE" sh
        ;;
esac

