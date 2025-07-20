# Book Library - Full Stack (Angular + WildFly + PostgreSQL)

## Descripción

Aplicación de biblioteca de libros que permite listar, agregar, editar y borrar libros. El backend está construido con Jakarta EE (WildFly) y PostgreSQL, y el frontend con Angular.

---

## Tecnologías utilizadas
- **Frontend:** Angular 16+, TypeScript
- **Backend:** Jakarta EE 10 (JAX-RS, CDI, EJB), WildFly 36
- **Base de datos:** PostgreSQL 15
- **Contenedores:** Docker, Docker Compose

---

## Instrucciones de instalación y uso

### 1. Clonar el repositorio
```bash
git clone 
cd book-library-java
```

### 2. Levantar el backend y la base de datos
```bash
cd backend/mi-ejb-app
sudo docker compose up -d
```
- Esto levantará WildFly en el puerto **8081** y PostgreSQL en el **5432**.
- Espera ~30 segundos a que WildFly termine de arrancar.

### 3. Probar el backend
Puedes probar el endpoint principal:
```
curl -i http://localhost:8081/servlet/api/books
```
Deberías recibir un `200 OK` y un JSON con la lista de libros.

### 4. Levantar el frontend
```bash
cd frontend/book-library-front
npm install
npm start
```
- Accede a [http://localhost:4200](http://localhost:4200) en tu navegador.

### 5. Usar la aplicación
- Puedes **agregar**, **editar** y **borrar** libros desde la interfaz web.
- Todos los cambios se reflejan en la base de datos PostgreSQL.

---

## Solución de problemas

- Si el frontend muestra error de conexión o CORS, asegúrate de que el backend esté corriendo y que la URL en `book.service.ts` sea:
  ```typescript
  private apiUrl = 'http://localhost:8081/servlet/api/books';
  ```
- Si el backend responde 404, revisa el README del backend (`backend/mi-ejb-app/README.md`) para solución de problemas de datasource y driver.
- Si el backend no arranca, revisa los logs del contenedor WildFly:
  ```bash
  sudo docker logs booklibrary-wildfly | tail -n 100
  ```
- Si necesitas reiniciar todo:
  ```bash
  sudo docker compose down
  sudo docker compose up -d
  ```

---

## Estructura del proyecto

```
book-library-java/
  backend/mi-ejb-app/   # Backend Jakarta EE + WildFly
  frontend/book-library-front/   # Frontend Angular
```

---

## Contacto

Para dudas o soporte, contactar a: Felipe Useche <felipeuseche22@gmail.com>

---

¡Gracias por revisar el proyecto! 