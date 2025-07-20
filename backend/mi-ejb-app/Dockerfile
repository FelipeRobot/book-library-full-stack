# Use WildFly latest as base image
FROM jboss/wildfly:latest

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

# Copy PostgreSQL driver
COPY postgresql-42.7.2.jar /opt/jboss/wildfly/standalone/deployments/

# Create a script to wait for database and configure datasource
RUN echo '#!/bin/bash\n\
echo "Waiting for PostgreSQL to be ready..."\n\
sleep 10\n\
echo "PostgreSQL should be ready!"\n\
\n\
echo "Configuring datasource..."\n\
/opt/jboss/wildfly/bin/jboss-cli.sh --connect --file=/opt/jboss/wildfly/bin/configure-datasource.cli\n\
\n\
echo "Starting WildFly..."\n\
exec /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0' > /opt/jboss/wildfly/bin/start.sh

# Make the script executable
RUN chmod +x /opt/jboss/wildfly/bin/start.sh

# Expose ports
EXPOSE 8080 9990

# Set the entrypoint
ENTRYPOINT ["/opt/jboss/wildfly/bin/start.sh"] 