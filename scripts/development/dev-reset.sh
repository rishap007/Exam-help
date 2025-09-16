#!/bin/bash

echo "Ì¥Ñ Resetting EduPlatform Development Environment..."

# Warning message
echo "‚ö†Ô∏è  WARNING: This will:"
echo "   Ì∑ëÔ∏è  Delete all database data"
echo "   Ì∑ëÔ∏è  Delete all Redis cache"
echo "   Ì∑ëÔ∏è  Delete all uploaded files"
echo "   Ì∑ëÔ∏è  Reset all Docker volumes"
echo ""

read -p "Are you sure you want to continue? (y/N): " confirm
if [[ $confirm != "y" && $confirm != "Y" ]]; then
    echo "‚ùå Reset cancelled."
    exit 0
fi

# Stop services
echo "Ìªë Stopping services..."
docker-compose down

# Remove volumes and data
echo "Ì∑ëÔ∏è  Removing volumes and data..."
docker-compose down -v
docker volume prune -f

# Remove local data directories
rm -rf data/postgres/* data/redis/* data/uploads/* logs/*

# Rebuild and restart
echo "Ì¥® Rebuilding and starting services..."
docker-compose up -d --build --force-recreate

echo "‚úÖ Environment reset completed!"

