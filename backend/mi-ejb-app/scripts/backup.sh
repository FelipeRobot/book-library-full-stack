#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
BACKUP_DIR="./db/backups"
RETENTION_DAYS=${BACKUP_RETENTION_DAYS:-7}
DB_NAME=${DB_NAME:-booklibrary}
DB_USER=${DB_USER:-booklibrary}
DB_HOST=${DB_HOST:-postgres}
DB_PORT=${DB_PORT:-5432}

# Create backup directory if it doesn't exist
mkdir -p "$BACKUP_DIR"

# Generate backup filename with timestamp
BACKUP_FILE="$BACKUP_DIR/booklibrary_$(date +%Y%m%d_%H%M%S).sql"

echo -e "${BLUE}🗄️  Database Backup${NC}"
echo "=================="

# Check if PostgreSQL container is running
if ! docker-compose ps postgres | grep -q "Up"; then
    echo -e "${RED}❌ PostgreSQL container is not running${NC}"
    exit 1
fi

echo -e "${YELLOW}📦 Creating backup: $BACKUP_FILE${NC}"

# Create backup
if docker-compose exec -T postgres pg_dump -U "$DB_USER" -d "$DB_NAME" > "$BACKUP_FILE"; then
    echo -e "${GREEN}✅ Backup created successfully${NC}"
    
    # Compress backup
    gzip "$BACKUP_FILE"
    echo -e "${GREEN}✅ Backup compressed: ${BACKUP_FILE}.gz${NC}"
    
    # Show backup size
    BACKUP_SIZE=$(du -h "${BACKUP_FILE}.gz" | cut -f1)
    echo -e "${BLUE}📊 Backup size: $BACKUP_SIZE${NC}"
else
    echo -e "${RED}❌ Backup failed${NC}"
    exit 1
fi

# Clean old backups
echo -e "${YELLOW}🧹 Cleaning old backups (older than $RETENTION_DAYS days)...${NC}"
find "$BACKUP_DIR" -name "*.sql.gz" -type f -mtime +$RETENTION_DAYS -delete

# List remaining backups
echo -e "${BLUE}📋 Remaining backups:${NC}"
ls -lh "$BACKUP_DIR"/*.sql.gz 2>/dev/null || echo "No backups found"

echo -e "${GREEN}✅ Backup process completed${NC}" 