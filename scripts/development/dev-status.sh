#!/bin/bash

echo "� EduPlatform Development Environment Status"
echo "=============================================="
echo ""

# Check Docker
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running"
    exit 1
fi

# Show running containers
echo "� Docker Containers:"
docker-compose ps

echo ""

# Show volumes
echo "� Docker Volumes:"
docker volume ls | grep eduplatform

echo ""

# Show networks
echo "� Docker Networks:"
docker network ls | grep eduplatform

echo ""

# Check service health
echo "� Service Health Checks:"

services=("backend" "ai-service" "postgres" "redis")

for service in "${services[@]}"; do
    if docker-compose ps | grep -q "$service.*Up"; then
        case $service in
            "backend")
                if curl -s http://localhost:8080/actuator/health > /dev/null; then
                    echo "   ✅ $service - Healthy"
                else
                    echo "   ⚠️  $service - Running but not responding"
                fi
                ;;
            "ai-service")
                if curl -s http://localhost:8000/health > /dev/null; then
                    echo "   ✅ $service - Healthy"
                else
                    echo "   ⚠️  $service - Running but not responding"
                fi
                ;;
            "postgres")
                if docker-compose exec -T postgres pg_isready > /dev/null; then
                    echo "   ✅ $service - Healthy"
                else
                    echo "   ⚠️  $service - Not ready"
                fi
                ;;
            "redis")
                if docker-compose exec -T redis redis-cli ping > /dev/null; then
                    echo "   ✅ $service - Healthy"
                else
                    echo "   ⚠️  $service - Not responding"
                fi
                ;;
        esac
    else
        echo "   ❌ $service - Not running"
    fi
done

echo ""

# Show disk usage
echo "� Disk Usage:"
docker system df

