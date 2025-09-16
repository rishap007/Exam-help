#!/bin/bash

echo "Ì∫Ä Starting EduPlatform Development Environment..."

# Check if .env.local exists
if [ ! -f .env.local ]; then
    echo "‚ùå .env.local file not found!"
    echo "Ì≥ù Please copy .env.example to .env.local and fill in your values"
    echo "   cp .env.example .env.local"
    exit 1
fi

# Load environment variables
source .env.local

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "‚ùå Docker is not running. Please start Docker Desktop."
    exit 1
fi

# Create necessary directories
echo "Ì≥Å Creating necessary directories..."
mkdir -p data/{postgres,redis,uploads} logs

# Build and start services
echo "Ì¥® Building and starting services..."
docker-compose up -d --build

# Wait for services to be healthy
echo "‚è≥ Waiting for services to start..."
sleep 10

# Check service health
echo "Ìø• Checking service health..."
docker-compose ps

echo ""
echo "‚úÖ Development environment started successfully!"
echo ""
echo "Ì≥ä Service URLs:"
echo "   Ìºê Frontend:     http://localhost:3000"
echo "   Ì¥ß Backend API:  http://localhost:8080"
echo "   Ì¥ñ AI Service:   http://localhost:8000"
echo "   Ì∑ÑÔ∏è  Database:     localhost:5432"
echo "   Ì¥¥ Redis:        localhost:6379"
echo "   Ì≥ä PgAdmin:      http://localhost:5050"
echo "   Ì¥ç Redis Admin:  http://localhost:8081"
echo ""
echo "Ì≥ù Useful commands:"
echo "   ÔøΩÔøΩ View logs:    docker-compose logs -f [service-name]"
echo "   Ì∞ö Shell into:   docker-compose exec [service-name] bash"
echo "   Ìªë Stop all:     ./scripts/development/dev-stop.sh"
echo "   Ì¥Ñ Restart:      docker-compose restart [service-name]"

