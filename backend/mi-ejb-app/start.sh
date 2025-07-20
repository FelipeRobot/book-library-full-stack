#!/bin/bash

set -e

echo "Starting WildFly..."
/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &

# Esperar a que WildFly esté listo para aceptar conexiones CLI
until /opt/jboss/wildfly/bin/jboss-cli.sh --connect --commands=":read-attribute(name=server-state)" | grep -q running; do
  echo "Esperando a que WildFly esté en estado 'running'..."
  sleep 5
done

echo "Waiting for PostgreSQL to be ready..."
sleep 10
echo "PostgreSQL should be ready!"

# Reintentar la configuración del datasource hasta que el driver esté disponible
MAX_ATTEMPTS=10
ATTEMPT=1
while [ $ATTEMPT -le $MAX_ATTEMPTS ]; do
  echo "Intento $ATTEMPT: Configurando datasource y driver..."
  if /opt/jboss/wildfly/bin/jboss-cli.sh --connect --file=/opt/jboss/wildfly/bin/configure-datasource.cli; then
    echo "Datasource y driver configurados correctamente."
    break
  else
    echo "Fallo al configurar datasource/driver. Reintentando en 5 segundos..."
    sleep 5
    ATTEMPT=$((ATTEMPT+1))
  fi
done

if [ $ATTEMPT -gt $MAX_ATTEMPTS ]; then
  echo "No se pudo configurar el datasource/driver después de varios intentos."
  exit 1
fi

echo "Configuration complete. Keeping container running..."
wait 