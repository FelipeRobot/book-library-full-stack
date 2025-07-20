#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Book Library - Docker Build & Deploy${NC}"
echo "=================================="

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
echo -e "${YELLOW}📋 Checking prerequisites...${NC}"

if ! command_exists docker; then
    echo -e "${RED}❌ Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi

if ! command_exists docker-compose; then
    echo -e "${RED}❌ Docker Compose is not installed. Please install Docker Compose first.${NC}"
    exit 1
fi

if ! command_exists mvn; then
    echo -e "${RED}❌ Maven is not installed. Please install Maven first.${NC}"
    exit 1
fi

echo -e "${GREEN}✅ All prerequisites are installed${NC}"

# Clean previous builds
echo -e "${YELLOW}🧹 Cleaning previous builds...${NC}"
mvn clean -q
echo -e "${GREEN}✅ Clean completed${NC}"

# Build the project
echo -e "${YELLOW}🔨 Building the project...${NC}"
mvn package -DskipTests -q
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Build failed${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Build completed${NC}"

# Download PostgreSQL driver if not exists
if [ ! -f "postgresql-42.7.2.jar" ]; then
    echo -e "${YELLOW}📥 Downloading PostgreSQL driver...${NC}"
    curl -L -o postgresql-42.7.2.jar https://jdbc.postgresql.org/download/postgresql-42.7.2.jar
    if [ $? -ne 0 ]; then
        echo -e "${RED}❌ Failed to download PostgreSQL driver${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ PostgreSQL driver downloaded${NC}"
fi

# Stop and remove existing containers
echo -e "${YELLOW}🛑 Stopping existing containers...${NC}"
docker compose down -v
echo -e "${GREEN}✅ Existing containers stopped${NC}"

# Build and start containers
echo -e "${YELLOW}🐳 Building and starting Docker containers...${NC}"
docker compose up --build -d

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Docker containers failed to start${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Docker containers started successfully${NC}"

# Wait for services to be ready
echo -e "${YELLOW}⏳ Waiting for services to be ready...${NC}"

# Wait for PostgreSQL
echo -e "${BLUE}📊 Waiting for PostgreSQL...${NC}"
until docker compose exec -T postgres pg_isready -U booklibrary -d booklibrary >/dev/null 2>&1; do
    sleep 2
done
echo -e "${GREEN}✅ PostgreSQL is ready${NC}"

# Wait for WildFly
echo -e "${BLUE}🦅 Waiting for WildFly...${NC}"
until curl -f http://localhost:8080/book-library/api/books >/dev/null 2>&1; do
    sleep 5
done
echo -e "${GREEN}✅ WildFly is ready${NC}"

echo ""
echo -e "${GREEN}🎉 Deployment completed successfully!${NC}"
echo "=================================="
echo -e "${BLUE}📱 Application URLs:${NC}"
echo -e "   🌐 API Base URL: ${GREEN}http://localhost:8080/book-library/api${NC}"
echo -e "   📚 Books API: ${GREEN}http://localhost:8080/book-library/api/books${NC}"
echo -e "   🛠️  WildFly Admin: ${GREEN}http://localhost:9990${NC}"
echo -e "   📊 pgAdmin: ${GREEN}http://localhost:5050${NC} (admin@booklibrary.com / admin123)"
echo ""
echo -e "${BLUE}🔧 Useful commands:${NC}"
echo -e "   📋 View logs: ${YELLOW}docker-compose logs -f${NC}"
echo -e "   🛑 Stop services: ${YELLOW}docker-compose down${NC}"
echo -e "   🔄 Restart services: ${YELLOW}docker-compose restart${NC}"
echo -e "   🧹 Clean everything: ${YELLOW}docker-compose down -v && docker system prune -f${NC}"
echo ""
echo -e "${BLUE}🧪 Test the API:${NC}"
echo -e "   ${YELLOW}curl -X GET http://localhost:8080/book-library/api/books${NC}"
echo -e "   ${YELLOW}curl -X POST http://localhost:8080/book-library/api/books \\${NC}"
echo -e "     -H 'Content-Type: application/json' \\${NC}"
echo -e "     -d '{\"title\":\"Test Book\",\"author\":\"Test Author\",\"publicationYear\":2024,\"isbn\":\"1234567890123\"}'${NC}" 