# 📚 Book Library API Documentation

## Base URL
```
http://localhost:8080/booklibrary/api
```

## Endpoints

### 📖 Libros

#### GET /books
Obtiene todos los libros disponibles.

**URL:** `GET /books`

**Respuesta:**
```json
{
  "message": "Lista de libros",
  "status": "success",
  "data": ["Libro 1", "Libro 2", "Libro 3"]
}
```

#### GET /books/{id}
Obtiene un libro específico por su ID.

**URL:** `GET /books/1`

**Respuesta:**
```json
{
  "id": "1",
  "info": "Book ID: 1",
  "status": "success"
}
```

#### POST /books
Crea un nuevo libro.

**URL:** `POST /books`

**Body:**
```json
{
  "title": "El Señor de los Anillos",
  "author": "J.R.R. Tolkien",
  "isbn": "978-84-450-7179-3",
  "year": 1954
}
```

**Respuesta:**
```json
{
  "message": "Libro creado exitosamente",
  "status": "success",
  "data": {
    "title": "El Señor de los Anillos",
    "author": "J.R.R. Tolkien",
    "isbn": "978-84-450-7179-3",
    "year": 1954
  }
}
```

#### PUT /books/{id}
Actualiza un libro existente.

**URL:** `PUT /books/1`

**Body:**
```json
{
  "title": "El Señor de los Anillos - Edición Actualizada",
  "author": "J.R.R. Tolkien",
  "isbn": "978-84-450-7179-3",
  "year": 1954
}
```

**Respuesta:**
```json
{
  "message": "Libro actualizado exitosamente",
  "status": "success",
  "id": "1",
  "data": {
    "title": "El Señor de los Anillos - Edición Actualizada",
    "author": "J.R.R. Tolkien",
    "isbn": "978-84-450-7179-3",
    "year": 1954
  }
}
```

#### DELETE /books/{id}
Elimina un libro.

**URL:** `DELETE /books/1`

**Respuesta:**
```json
{
  "message": "Libro eliminado exitosamente",
  "status": "success",
  "id": "1"
}
```

### 👥 Usuarios

#### GET /users
Obtiene todos los usuarios.

**URL:** `GET /users`

**Respuesta:**
```json
{
  "message": "Lista de usuarios",
  "status": "success",
  "data": ["Usuario 1", "Usuario 2", "Usuario 3"]
}
```

#### GET /users/{id}
Obtiene un usuario específico por su ID.

**URL:** `GET /users/1`

**Respuesta:**
```json
{
  "id": "1",
  "name": "Usuario 1",
  "email": "usuario1@example.com",
  "status": "success"
}
```

#### POST /users
Crea un nuevo usuario.

**URL:** `POST /users`

**Body:**
```json
{
  "name": "Juan Pérez",
  "email": "juan.perez@example.com",
  "password": "password123"
}
```

**Respuesta:**
```json
{
  "message": "Usuario creado exitosamente",
  "status": "success",
  "data": {
    "name": "Juan Pérez",
    "email": "juan.perez@example.com",
    "password": "password123"
  }
}
```

#### POST /users/login
Inicia sesión de un usuario.

**URL:** `POST /users/login`

**Body:**
```json
{
  "username": "admin",
  "password": "password"
}
```

**Respuesta exitosa:**
```json
{
  "message": "Login exitoso",
  "status": "success",
  "token": "jwt-token-example"
}
```

**Respuesta de error:**
```json
{
  "error": "Credenciales inválidas"
}
```

## Códigos de Estado HTTP

- **200 OK**: Operación exitosa
- **201 Created**: Recurso creado exitosamente
- **400 Bad Request**: Solicitud malformada
- **401 Unauthorized**: Credenciales inválidas
- **404 Not Found**: Recurso no encontrado
- **500 Internal Server Error**: Error interno del servidor

## Ejemplos de Uso con cURL

### Obtener todos los libros
```bash
curl -X GET http://localhost:8080/booklibrary/api/books
```

### Obtener un libro específico
```bash
curl -X GET http://localhost:8080/booklibrary/api/books/1
```

### Crear un nuevo libro
```bash
curl -X POST http://localhost:8080/booklibrary/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Don Quijote",
    "author": "Miguel de Cervantes",
    "isbn": "978-84-376-0494-7",
    "year": 1605
  }'
```

### Actualizar un libro
```bash
curl -X PUT http://localhost:8080/booklibrary/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Don Quijote - Edición Especial",
    "author": "Miguel de Cervantes",
    "isbn": "978-84-376-0494-7",
    "year": 1605
  }'
```

### Eliminar un libro
```bash
curl -X DELETE http://localhost:8080/booklibrary/api/books/1
```

### Iniciar sesión
```bash
curl -X POST http://localhost:8080/booklibrary/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password"
  }'
```

## Próximos Pasos

1. **Implementar persistencia de datos** con JPA y base de datos
2. **Agregar autenticación JWT** real
3. **Implementar validación de datos** con Bean Validation
4. **Agregar logging** para debugging
5. **Crear tests unitarios** y de integración
6. **Implementar paginación** para listas grandes
7. **Agregar filtros y búsqueda** avanzada 