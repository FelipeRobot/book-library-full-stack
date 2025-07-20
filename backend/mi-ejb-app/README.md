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