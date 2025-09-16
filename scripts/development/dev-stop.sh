#!/bin/bash

echo "� Stopping EduPlatform Development Environment..."

# Stop all services
docker-compose down

echo "✅ All services stopped successfully!"

# Optional: Clean up
read -p "� Do you want to remove unused Docker resources? (y/N): " cleanup
if [[ $cleanup == "y" || $cleanup == "Y" ]]; then
    echo "� Cleaning up Docker resources..."
    docker system prune -f
    echo "✅ Cleanup completed!"
fi

