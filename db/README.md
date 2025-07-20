# 📚 Base de Datos - Book Library

Este directorio contiene los scripts de base de datos para la aplicación Book Library.

## 🗄️ Esquema de Base de Datos

### Tabla `books`
```sql
CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    year INT,
    isbn VARCHAR(13) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Tabla `users` (para futuras funcionalidades)
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Tabla `loans` (para futuras funcionalidades)
```sql
CREATE TABLE loans (
    id SERIAL PRIMARY KEY,
    book_id INT NOT NULL,
    user_id INT NOT NULL,
    loan_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    return_date TIMESTAMP,
    due_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 🚀 Cómo Ejecutar el Script

### PostgreSQL
```bash
# Conectar a PostgreSQL
psql -U postgres -d postgres

# Ejecutar el script
\i db/init.sql
```

### MySQL/MariaDB
```bash
# Conectar a MySQL
mysql -u root -p

# Crear la base de datos
CREATE DATABASE book_library;
USE book_library;

# Ejecutar el script (necesitarás adaptar la sintaxis)
source db/init.sql;
```

### Docker (PostgreSQL)
```bash
# Ejecutar PostgreSQL en Docker
docker run --name book-library-db \
  -e POSTGRES_DB=book_library \
  -e POSTGRES_USER=bookuser \
  -e POSTGRES_PASSWORD=bookpass \
  -p 5432:5432 \
  -d postgres:15

# Ejecutar el script
docker exec -i book-library-db psql -U bookuser -d book_library < db/init.sql
```

## 📊 Datos de Ejemplo

El script incluye 5 libros de ejemplo:
- Don Quijote de la Mancha
- El Señor de los Anillos
- Cien años de soledad
- 1984
- El Principito

## 🔧 Características del Script

- ✅ **Creación de tablas** con restricciones apropiadas
- ✅ **Índices** para mejorar el rendimiento
- ✅ **Datos de ejemplo** para pruebas
- ✅ **Triggers** para actualizar automáticamente `updated_at`
- ✅ **Verificación** de que las tablas se crearon correctamente

## 🔗 Próximos Pasos

1. **Configurar la conexión** en la aplicación EJB
2. **Crear entidades JPA** para las tablas
3. **Implementar DAOs** para acceso a datos
4. **Actualizar endpoints** para usar la base de datos real

## 📝 Notas

- El script usa sintaxis PostgreSQL
- Para MySQL/MariaDB, necesitarás adaptar algunas funciones
- Los triggers y funciones son específicos de PostgreSQL 