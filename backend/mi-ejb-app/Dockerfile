# Use WildFly latest as base image
FROM quay.io/wildfly/wildfly:latest

# Set environment variables
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m"
ENV DB_HOST=postgres
ENV DB_PORT=5432
ENV DB_NAME=booklibrary
ENV DB_USER=booklibrary
ENV DB_PASSWORD=booklibrary123

# Copy the EAR file
COPY ear/target/ear-1.0.ear /opt/jboss/wildfly/standalone/deployments/

# Copy database configuration script
COPY configure-datasource.cli /opt/jboss/wildfly/bin/

# Copy PostgreSQL driver and create module
RUN mkdir -p /opt/jboss/wildfly/modules/org/postgresql/main/
COPY postgresql-42.7.2.jar /opt/jboss/wildfly/modules/org/postgresql/main/
COPY module.xml /opt/jboss/wildfly/modules/org/postgresql/main/

# Create a script to wait for database and configure datasource
COPY --chown=jboss:root start.sh /opt/jboss/wildfly/bin/
RUN chmod +x /opt/jboss/wildfly/bin/start.sh

# Expose ports
EXPOSE 8080 9990

# Set the entrypoint
ENTRYPOINT ["/opt/jboss/wildfly/bin/start.sh"] 