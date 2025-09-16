#!/bin/bash

echo "� Resetting EduPlatform Development Environment..."

# Warning message
echo "⚠️  WARNING: This will:"
echo "   �️  Delete all database data"
echo "   �️  Delete all Redis cache"
echo "   �️  Delete all uploaded files"
echo "   �️  Reset all Docker volumes"
echo ""

read -p "Are you sure you want to continue? (y/N): " confirm
if [[ $confirm != "y" && $confirm != "Y" ]]; then
    echo "❌ Reset cancelled."
    exit 0
fi

# Stop services
echo "� Stopping services..."
docker-compose down

# Remove volumes and data
echo "�️  Removing volumes and data..."
docker-compose down -v
docker volume prune -f

# Remove local data directories
rm -rf data/postgres/* data/redis/* data/uploads/* logs/*

# Rebuild and restart
echo "� Rebuilding and starting services..."
docker-compose up -d --build --force-recreate

echo "✅ Environment reset completed!"

