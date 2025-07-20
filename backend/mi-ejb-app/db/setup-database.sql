-- Script para configurar la base de datos PostgreSQL para Book Library
-- Ejecutar como usuario postgres

-- Crear la base de datos
CREATE DATABASE book_library;

-- Crear el usuario
CREATE USER bookuser WITH PASSWORD 'bookpass';

-- Otorgar permisos al usuario
GRANT ALL PRIVILEGES ON DATABASE book_library TO bookuser;

-- Conectar a la base de datos book_library
\c book_library;

-- Otorgar permisos adicionales
GRANT ALL ON SCHEMA public TO bookuser;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO bookuser;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO bookuser;

-- Configurar permisos para futuras tablas
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO bookuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO bookuser; 