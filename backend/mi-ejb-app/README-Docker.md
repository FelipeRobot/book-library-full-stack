# 🐳 Book Library - Docker Setup

Este documento describe cómo ejecutar la aplicación Book Library usando Docker.

## 📋 Prerrequisitos

- Docker (versión 20.10 o superior)
- Docker Compose (versión 2.0 o superior)
- Maven (versión 3.6 o superior)

## 🚀 Inicio Rápido

### Opción 1: Script Automático (Recomendado)

```bash
# Construir y desplegar todo automáticamente
./docker-build.sh
```

### Opción 2: Script de Desarrollo

```bash
# Ver comandos disponibles
./dev.sh help

# Construir y desplegar
./dev.sh build

# Iniciar servicios
./dev.sh start

# Probar la API
./dev.sh test

# Ver logs
./dev.sh logs
```

### Opción 3: Comandos Manuales

```bash
# 1. Construir el proyecto
mvn clean package -DskipTests

# 2. Descargar PostgreSQL driver (si no existe)
curl -L -o postgresql-42.7.2.jar https://jdbc.postgresql.org/download/postgresql-42.7.2.jar

# 3. Construir y ejecutar contenedores
docker-compose up --build -d

# 4. Verificar que todo esté funcionando
docker-compose ps
```

## 🌐 URLs de Acceso

Una vez desplegado, puedes acceder a:

- **API REST**: http://localhost:8080/book-library/api
- **Books API**: http://localhost:8080/book-library/api/books
- **WildFly Admin Console**: http://localhost:9990
- **pgAdmin**: http://localhost:5050 (admin@booklibrary.com / admin123)

## 🧪 Probar la API

### Obtener todos los libros
```bash
curl -X GET http://localhost:8080/book-library/api/books
```

### Crear un nuevo libro
```bash
curl -X POST http://localhost:8080/book-library/api/books \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "El Señor de los Anillos",
    "author": "J.R.R. Tolkien",
    "publicationYear": 1954,
    "isbn": "9780261103252"
  }'
```

### Obtener un libro específico
```bash
curl -X GET http://localhost:8080/book-library/api/books/1
```

### Actualizar un libro
```bash
curl -X PUT http://localhost:8080/book-library/api/books/1 \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "El Señor de los Anillos (Edición Actualizada)",
    "author": "J.R.R. Tolkien",
    "publicationYear": 1954,
    "isbn": "9780261103252"
  }'
```

### Eliminar un libro
```bash
curl -X DELETE http://localhost:8080/book-library/api/books/1
```

## 🔧 Comandos Útiles

### Gestión de Contenedores

```bash
# Ver estado de los contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f wildfly
docker-compose logs -f postgres

# Reiniciar servicios
docker-compose restart

# Parar servicios
docker-compose down

# Parar y eliminar volúmenes
docker-compose down -v
```

### Acceso a Contenedores

```bash
# Acceder al shell de WildFly
docker-compose exec wildfly bash

# Conectar a PostgreSQL
docker-compose exec postgres psql -U booklibrary -d booklibrary

# Ver archivos de configuración
docker-compose exec wildfly cat /opt/jboss/wildfly/standalone/configuration/standalone.xml
```

### Limpieza

```bash
# Limpiar contenedores, volúmenes e imágenes
docker-compose down -v
docker system prune -f
docker image prune -f

# Limpiar solo volúmenes de datos
docker volume rm mi-ejb-app_postgres_data
```

## 🏗️ Arquitectura Docker

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   PostgreSQL    │    │    WildFly      │    │    pgAdmin      │
│   (Port 5432)   │◄──►│   (Port 8080)   │    │   (Port 5050)   │
│                 │    │                 │    │                 │
│ - Database      │    │ - Java EE App   │    │ - DB Management │
│ - User: booklib │    │ - REST API      │    │ - Web Interface │
│ - Pass: booklib │    │ - EJB Container │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🔍 Troubleshooting

### Problemas Comunes

1. **Puerto 8080 ocupado**
   ```bash
   # Cambiar puerto en docker-compose.yml
   ports:
     - "8081:8080"  # Usar puerto 8081 en lugar de 8080
   ```

2. **Base de datos no se conecta**
   ```bash
   # Verificar logs de PostgreSQL
   docker-compose logs postgres
   
   # Verificar conectividad
   docker-compose exec wildfly nc -z postgres 5432
   ```

3. **Aplicación no se despliega**
   ```bash
   # Verificar logs de WildFly
   docker-compose logs wildfly
   
   # Verificar que el EAR se copió correctamente
   docker-compose exec wildfly ls -la /opt/jboss/wildfly/standalone/deployments/
   ```

4. **Problemas de memoria**
   ```bash
   # Aumentar memoria en Dockerfile
   ENV JAVA_OPTS="-Xms1024m -Xmx2048m -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=512m"
   ```

### Logs Útiles

```bash
# Logs de aplicación
docker-compose logs wildfly | grep -i error

# Logs de base de datos
docker-compose logs postgres | grep -i error

# Logs de red
docker network ls
docker network inspect mi-ejb-app_booklibrary-network
```

## 📊 Monitoreo

### Métricas de Contenedores
```bash
# Uso de recursos
docker stats

# Información detallada
docker-compose exec wildfly jcmd 1 VM.native_memory
```

### Health Checks
```bash
# Verificar salud de PostgreSQL
docker-compose exec postgres pg_isready -U booklibrary -d booklibrary

# Verificar salud de WildFly
curl -f http://localhost:8080/book-library/api/books
```

## 🔒 Seguridad

### Variables de Entorno
- Las credenciales de base de datos están en `docker-compose.yml`
- Para producción, usar variables de entorno o secrets
- Considerar usar Docker secrets para credenciales sensibles

### Redes
- Los contenedores están en una red aislada
- Solo los puertos necesarios están expuestos
- pgAdmin está en un perfil separado para desarrollo

## 🚀 Producción

Para despliegue en producción:

1. **Usar imágenes específicas de versión**
2. **Configurar variables de entorno**
3. **Usar secrets para credenciales**
4. **Configurar backups de base de datos**
5. **Configurar monitoreo y logging**
6. **Usar un reverse proxy (nginx/traefik)**

```bash
# Ejemplo para producción
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

## 📝 Notas Adicionales

- El driver de PostgreSQL se descarga automáticamente
- La base de datos se inicializa con el script `db/init.sql`
- Los datos persisten en el volumen `postgres_data`
- pgAdmin es opcional y solo para desarrollo
- La aplicación tarda ~2-3 minutos en estar completamente lista 