#!/bin/bash

echo "í»‘ Stopping EduPlatform Development Environment..."

# Stop all services
docker-compose down

echo "âœ… All services stopped successfully!"

# Optional: Clean up
read -p "í·¹ Do you want to remove unused Docker resources? (y/N): " cleanup
if [[ $cleanup == "y" || $cleanup == "Y" ]]; then
    echo "í·¹ Cleaning up Docker resources..."
    docker system prune -f
    echo "âœ… Cleanup completed!"
fi

