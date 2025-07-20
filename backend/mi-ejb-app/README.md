# Book Library EJB Application

Este es un proyecto Java EE que utiliza Enterprise JavaBeans (EJB) para implementar una aplicación de biblioteca de libros.

## Estructura del Proyecto

```
mi-ejb-app/
├── pom.xml                    # POM principal (multi-módulo)
├── ejbs/                      # Módulo EJB
│   ├── pom.xml
│   └── src/main/java/
│       └── com/controllers/
│           └── Controller.java
├── servlets/                  # Módulo de servlets
│   ├── pom.xml
│   └── servlet/
│       ├── pom.xml
│       └── src/main/webapp/
│           ├── index.jsp
│           └── WEB-INF/
│               └── web.xml
└── ear/                       # Módulo EAR (Enterprise Archive)
    ├── pom.xml
    └── src/main/resources/META-INF/
        └── application.xml
```

## Módulos

### 1. EJBs (`ejbs/`)
- Contiene los Enterprise JavaBeans
- Configurado para EJB 4.0 (Jakarta EE)
- Incluye dependencias de Jakarta EJB API y Jakarta Persistence API

### 2. Servlets (`servlets/servlet/`)
- Contiene la aplicación web
- Configurado como WAR (Web Application Archive)
- Incluye dependencias de Jakarta Servlet API

### 3. EAR (`ear/`)
- Archivo Enterprise Archive que empaqueta todos los módulos
- Configura el contexto raíz de la aplicación web como `/booklibrary`

## Tecnologías Utilizadas

- **Java 17**
- **Jakarta EE 10**
- **EJB 4.0**
- **Maven 3.x**

## Compilación y Despliegue

Para compilar el proyecto:

```bash
mvn clean install
```

El archivo EAR resultante se encontrará en `ear/target/ear-1.0.ear`

## Configuración del Servidor de Aplicaciones

Este proyecto está diseñado para ser desplegado en un servidor de aplicaciones compatible con Jakarta EE 10, como:
- WildFly 27+
- GlassFish 7+
- TomEE 10+

## Notas Importantes

1. El proyecto utiliza anotaciones para la configuración de EJBs (no requiere ejb-jar.xml)
2. La configuración web es mínima y se puede extender según necesidades
3. Se recomienda agregar más EJBs para implementar la lógica de negocio completa 

# Book Library Backend - WildFly + PostgreSQL

## Solución de problemas: Backend no expone endpoints REST (404)

Si después de levantar los contenedores el endpoint `/servlet/api/books` devuelve 404, sigue estos pasos para asegurar que el driver y el datasource estén correctamente registrados en WildFly:

### 1. Verifica que el módulo del driver PostgreSQL esté instalado
- El archivo `postgresql-42.7.2.jar` debe estar en `/opt/jboss/wildfly/modules/org/postgresql/main/` dentro del contenedor.
- El archivo `module.xml` debe estar en la misma ruta y tener el nombre de módulo `org.postgresql`.

### 2. Registra el driver PostgreSQL manualmente (solo la primera vez o si hay problemas)

Entra al contenedor WildFly:
```bash
sudo docker exec -it booklibrary-wildfly /bin/bash
```

Luego ejecuta en la CLI de WildFly:
```bash
/opt/jboss/wildfly/bin/jboss-cli.sh --connect
```
Y dentro de la CLI:
```cli
/subsystem=datasources/jdbc-driver=postgresql:add(driver-name="postgresql",driver-module-name="org.postgresql",driver-class-name="org.postgresql.Driver")
```

### 3. Crea el datasource manualmente
En la misma CLI:
```cli
/subsystem=datasources/data-source=PostgresDS:add(jndi-name="java:/PostgresDS",driver-name="postgresql",connection-url="jdbc:postgresql://postgres:5432/booklibrary",user-name="booklibrary",password="booklibrary123",min-pool-size=5,max-pool-size=20,pool-prefill=true,pool-use-strict-min=false,validate-on-match=true,background-validation=true,background-validation-millis=60000,check-valid-connection-sql="SELECT 1",enabled=true)
```

### 4. Recarga la configuración de WildFly
```cli
:reload
```

### 5. Prueba el endpoint REST
Desde tu máquina local:
```bash
curl -i http://localhost:8081/servlet/api/books
```
Deberías recibir un `200 OK` y un JSON con la lista de libros.

---

**Notas:**
- Si quieres que el endpoint sea `/booklibrary/api/books`, revisa el `context-root` en `application.xml` y el nombre del WAR en el EAR.
- Si el datasource o el driver ya existen, elimina primero con:
```cli
/subsystem=datasources/jdbc-driver=postgresql:remove
/subsystem=datasources/data-source=PostgresDS:remove
```
antes de volver a crearlos. 