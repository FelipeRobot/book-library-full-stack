#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🔐 Generating SSL Certificates for Development${NC}"
echo "================================================"

# Create SSL directory if it doesn't exist
mkdir -p ssl

# Generate private key
echo -e "${YELLOW}🔑 Generating private key...${NC}"
openssl genrsa -out ssl/key.pem 2048

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Failed to generate private key${NC}"
    exit 1
fi

# Generate certificate signing request
echo -e "${YELLOW}📝 Generating certificate signing request...${NC}"
openssl req -new -key ssl/key.pem -out ssl/cert.csr -subj "/C=US/ST=State/L=City/O=Organization/CN=localhost"

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Failed to generate CSR${NC}"
    exit 1
fi

# Generate self-signed certificate
echo -e "${YELLOW}📜 Generating self-signed certificate...${NC}"
openssl x509 -req -in ssl/cert.csr -signkey ssl/key.pem -out ssl/cert.pem -days 365

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Failed to generate certificate${NC}"
    exit 1
fi

# Clean up CSR file
rm ssl/cert.csr

# Set proper permissions
chmod 600 ssl/key.pem
chmod 644 ssl/cert.pem

echo -e "${GREEN}✅ SSL certificates generated successfully!${NC}"
echo ""
echo -e "${BLUE}📁 Certificate files:${NC}"
echo -e "   🔑 Private Key: ${GREEN}ssl/key.pem${NC}"
echo -e "   📜 Certificate: ${GREEN}ssl/cert.pem${NC}"
echo ""
echo -e "${YELLOW}⚠️  Note: These are self-signed certificates for development only.${NC}"
echo -e "${YELLOW}   For production, use certificates from a trusted CA.${NC}" 