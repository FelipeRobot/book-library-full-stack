#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
BACKUP_DIR="./db/backups"
DB_NAME=${DB_NAME:-booklibrary}
DB_USER=${DB_USER:-booklibrary}

echo -e "${BLUE}🔄 Database Restore${NC}"
echo "=================="

# Check if PostgreSQL container is running
if ! docker-compose ps postgres | grep -q "Up"; then
    echo -e "${RED}❌ PostgreSQL container is not running${NC}"
    exit 1
fi

# Check if backup file is provided
if [ -z "$1" ]; then
    echo -e "${YELLOW}📋 Available backups:${NC}"
    ls -lh "$BACKUP_DIR"/*.sql.gz 2>/dev/null || {
        echo -e "${RED}❌ No backup files found in $BACKUP_DIR${NC}"
        exit 1
    }
    echo ""
    echo -e "${YELLOW}Usage: $0 <backup_file>${NC}"
    echo -e "${YELLOW}Example: $0 booklibrary_20241201_143022.sql.gz${NC}"
    exit 1
fi

BACKUP_FILE="$1"

# Check if backup file exists
if [ ! -f "$BACKUP_FILE" ]; then
    # Try to find in backup directory
    if [ -f "$BACKUP_DIR/$BACKUP_FILE" ]; then
        BACKUP_FILE="$BACKUP_DIR/$BACKUP_FILE"
    else
        echo -e "${RED}❌ Backup file not found: $1${NC}"
        exit 1
    fi
fi

echo -e "${YELLOW}⚠️  WARNING: This will overwrite the current database!${NC}"
echo -e "${YELLOW}   Database: $DB_NAME${NC}"
echo -e "${YELLOW}   Backup file: $BACKUP_FILE${NC}"
echo ""
read -p "Are you sure you want to continue? (y/N): " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}❌ Restore cancelled${NC}"
    exit 1
fi

echo -e "${YELLOW}🔄 Restoring database from: $BACKUP_FILE${NC}"

# Stop the application to prevent data corruption
echo -e "${YELLOW}⏹️  Stopping application...${NC}"
docker-compose stop wildfly

# Drop and recreate database
echo -e "${YELLOW}🗑️  Dropping existing database...${NC}"
docker-compose exec -T postgres psql -U "$DB_USER" -c "DROP DATABASE IF EXISTS $DB_NAME;"
docker-compose exec -T postgres psql -U "$DB_USER" -c "CREATE DATABASE $DB_NAME;"

# Restore from backup
echo -e "${YELLOW}📦 Restoring from backup...${NC}"
if [[ "$BACKUP_FILE" == *.gz ]]; then
    # Compressed backup
    gunzip -c "$BACKUP_FILE" | docker-compose exec -T postgres psql -U "$DB_USER" -d "$DB_NAME"
else
    # Uncompressed backup
    docker-compose exec -T postgres psql -U "$DB_USER" -d "$DB_NAME" < "$BACKUP_FILE"
fi

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Database restored successfully${NC}"
else
    echo -e "${RED}❌ Database restore failed${NC}"
    exit 1
fi

# Start the application
echo -e "${YELLOW}▶️  Starting application...${NC}"
docker-compose start wildfly

# Wait for application to be ready
echo -e "${YELLOW}⏳ Waiting for application to be ready...${NC}"
until curl -f http://localhost:8080/book-library/api/books >/dev/null 2>&1; do
    sleep 5
done

echo -e "${GREEN}✅ Restore process completed successfully${NC}"
echo -e "${BLUE}🌐 Application is available at: http://localhost:8080/book-library/api/books${NC}" 