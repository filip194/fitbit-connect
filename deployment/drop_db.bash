#!/bin/bash

####################################################
# Bash Shell script to drop fitbitconnect database #
####################################################

# Set the values for database variables
postgresUser="postgres"
dockerImageName="postgres"
dockerContainerName="postgres-docker"
postgresPassword="fitbitConnect"
databaseName="fitbitconnect"

echo "dockerImageName: ${dockerImageName}"
echo "dockerContainerName: ${dockerContainerName}"
echo "postgresPassword: ${postgresPassword}"
echo "databaseName: ${databaseName}"

echo "-> Deleting database..."
docker exec ${dockerContainerName} psql -U ${postgresUser} -c "DROP DATABASE ${databaseName}"
echo "Deleted."
