#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to show usage
show_usage() {
    echo -e "${BLUE}🔧 Book Library - Development Script${NC}"
    echo "Usage: $0 [COMMAND]"
    echo ""
    echo "Commands:"
    echo -e "  ${GREEN}build${NC}     - Build and deploy the application"
    echo -e "  ${GREEN}start${NC}     - Start the application"
    echo -e "  ${GREEN}stop${NC}      - Stop the application"
    echo -e "  ${GREEN}restart${NC}   - Restart the application"
    echo -e "  ${GREEN}logs${NC}      - Show application logs"
    echo -e "  ${GREEN}clean${NC}     - Clean everything (containers, volumes, images)"
    echo -e "  ${GREEN}status${NC}    - Show status of containers"
    echo -e "  ${GREEN}shell${NC}     - Open shell in WildFly container"
    echo -e "  ${GREEN}db${NC}        - Connect to PostgreSQL database"
    echo -e "  ${GREEN}test${NC}      - Test the API endpoints"
    echo -e "  ${GREEN}help${NC}      - Show this help message"
}

# Function to check if containers are running
check_containers() {
    if ! docker compose ps | grep -q "Up"; then
        echo -e "${RED}❌ Containers are not running. Use '$0 start' to start them.${NC}"
        return 1
    fi
    return 0
}

# Function to test API
test_api() {
    echo -e "${BLUE}🧪 Testing API endpoints...${NC}"
    
    # Test GET /books
    echo -e "${YELLOW}📚 Testing GET /books...${NC}"
    curl -s -X GET http://localhost:8080/book-library/api/books | jq '.' 2>/dev/null || curl -s -X GET http://localhost:8080/book-library/api/books
    
    echo ""
    
    # Test POST /books
    echo -e "${YELLOW}➕ Testing POST /books...${NC}"
    curl -s -X POST http://localhost:8080/book-library/api/books \
        -H 'Content-Type: application/json' \
        -d '{
            "title": "Docker Test Book",
            "author": "Docker Author",
            "publicationYear": 2024,
            "isbn": "1234567890124"
        }' | jq '.' 2>/dev/null || curl -s -X POST http://localhost:8080/book-library/api/books \
        -H 'Content-Type: application/json' \
        -d '{"title": "Docker Test Book", "author": "Docker Author", "publicationYear": 2024, "isbn": "1234567890124"}'
    
    echo ""
    
    # Test validation error
    echo -e "${YELLOW}❌ Testing validation error (empty title)...${NC}"
    curl -s -X POST http://localhost:8080/book-library/api/books \
        -H 'Content-Type: application/json' \
        -d '{"title": "", "author": "Test Author", "publicationYear": 2024, "isbn": "1234567890125"}' | jq '.' 2>/dev/null || curl -s -X POST http://localhost:8080/book-library/api/books \
        -H 'Content-Type: application/json' \
        -d '{"title": "", "author": "Test Author", "publicationYear": 2024, "isbn": "1234567890125"}'
}

# Main script logic
case "$1" in
    "build")
        echo -e "${BLUE}🚀 Building and deploying application...${NC}"
        ./docker-build.sh
        ;;
    "start")
        echo -e "${BLUE}▶️  Starting application...${NC}"
        docker compose up -d
        echo -e "${GREEN}✅ Application started${NC}"
        ;;
    "stop")
        echo -e "${BLUE}⏹️  Stopping application...${NC}"
        docker compose down
        echo -e "${GREEN}✅ Application stopped${NC}"
        ;;
    "restart")
        echo -e "${BLUE}🔄 Restarting application...${NC}"
        docker compose restart
        echo -e "${GREEN}✅ Application restarted${NC}"
        ;;
    "logs")
        echo -e "${BLUE}📋 Showing logs...${NC}"
        docker compose logs -f
        ;;
    "clean")
        echo -e "${BLUE}🧹 Cleaning everything...${NC}"
        docker compose down -v
        docker system prune -f
        docker image prune -f
        echo -e "${GREEN}✅ Clean completed${NC}"
        ;;
    "status")
        echo -e "${BLUE}📊 Container status:${NC}"
        docker compose ps
        ;;
    "shell")
        if check_containers; then
            echo -e "${BLUE}🐚 Opening shell in WildFly container...${NC}"
            docker compose exec wildfly bash
        fi
        ;;
    "db")
        if check_containers; then
            echo -e "${BLUE}🗄️  Connecting to PostgreSQL...${NC}"
            docker compose exec postgres psql -U booklibrary -d booklibrary
        fi
        ;;
    "test")
        if check_containers; then
            test_api
        fi
        ;;
    "help"|"")
        show_usage
        ;;
    *)
        echo -e "${RED}❌ Unknown command: $1${NC}"
        echo ""
        show_usage
        exit 1
        ;;
esac 