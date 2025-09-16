#!/bin/bash

echo "� Starting EduPlatform Development Environment..."

# Check if .env.local exists
if [ ! -f .env.local ]; then
    echo "❌ .env.local file not found!"
    echo "� Please copy .env.example to .env.local and fill in your values"
    echo "   cp .env.example .env.local"
    exit 1
fi

# Load environment variables
source .env.local

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker Desktop."
    exit 1
fi

# Create necessary directories
echo "� Creating necessary directories..."
mkdir -p data/{postgres,redis,uploads} logs

# Build and start services
echo "� Building and starting services..."
docker-compose up -d --build

# Wait for services to be healthy
echo "⏳ Waiting for services to start..."
sleep 10

# Check service health
echo "� Checking service health..."
docker-compose ps

echo ""
echo "✅ Development environment started successfully!"
echo ""
echo "� Service URLs:"
echo "   � Frontend:     http://localhost:3000"
echo "   � Backend API:  http://localhost:8080"
echo "   � AI Service:   http://localhost:8000"
echo "   �️  Database:     localhost:5432"
echo "   � Redis:        localhost:6379"
echo "   � PgAdmin:      http://localhost:5050"
echo "   � Redis Admin:  http://localhost:8081"
echo ""
echo "� Useful commands:"
echo "   �� View logs:    docker-compose logs -f [service-name]"
echo "   � Shell into:   docker-compose exec [service-name] bash"
echo "   � Stop all:     ./scripts/development/dev-stop.sh"
echo "   � Restart:      docker-compose restart [service-name]"

