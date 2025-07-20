#!/bin/bash

# Script para configurar PostgreSQL para Book Library

echo "Configurando PostgreSQL para Book Library..."

# Verificar si PostgreSQL está instalado
if ! command -v psql &> /dev/null; then
    echo "PostgreSQL no está instalado. Instalando..."
    sudo apt update
    sudo apt install -y postgresql postgresql-contrib
fi

# Verificar si PostgreSQL está ejecutándose
if ! sudo systemctl is-active --quiet postgresql; then
    echo "Iniciando PostgreSQL..."
    sudo systemctl start postgresql
    sudo systemctl enable postgresql
fi

# Ejecutar el script SQL como usuario postgres
echo "Creando base de datos y usuario..."
sudo -u postgres psql -f db/setup-database.sql

if [ $? -eq 0 ]; then
    echo "PostgreSQL configurado exitosamente"
    echo "Base de datos: book_library"
    echo "Usuario: bookuser"
    echo "Contraseña: bookpass"
else
    echo "Error al configurar PostgreSQL"
    exit 1
fi 