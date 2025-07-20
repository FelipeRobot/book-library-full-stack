#!/bin/bash

# Script para instalar el driver de PostgreSQL en WildFly
# Se puede usar tanto para desarrollo como para Docker

WILDFLY_HOME=${WILDFLY_HOME:-/opt/wildfly}
DRIVER_VERSION="42.7.2"
DRIVER_URL="https://jdbc.postgresql.org/download/postgresql-${DRIVER_VERSION}.jar"
TEMP_DIR="/tmp"

echo "Instalando driver de PostgreSQL ${DRIVER_VERSION} en WildFly..."

# Descargar el driver si no existe
if [ ! -f "${TEMP_DIR}/postgresql-${DRIVER_VERSION}.jar" ]; then
    echo "Descargando driver de PostgreSQL..."
    wget -q "${DRIVER_URL}" -O "${TEMP_DIR}/postgresql-${DRIVER_VERSION}.jar"
    if [ $? -ne 0 ]; then
        echo "Error: No se pudo descargar el driver de PostgreSQL"
        exit 1
    fi
fi

# Instalar el módulo en WildFly
echo "Instalando módulo en WildFly..."
${WILDFLY_HOME}/bin/jboss-cli.sh --connect --command="module add --name=org.postgresql --resources=${TEMP_DIR}/postgresql-${DRIVER_VERSION}.jar --dependencies=javax.api,javax.transaction.api"

if [ $? -eq 0 ]; then
    echo "Driver de PostgreSQL instalado exitosamente"
else
    echo "Error: No se pudo instalar el módulo en WildFly"
    exit 1
fi

echo "Instalación completada" 