#!/bin/bash

####################################################
# Bash Shell script to stop fitbitconnect database #
####################################################

#Set the values for database variables
dockerContainerName="postgres-docker"
echo "dockerContainerName: ${dockerContainerName}"

echo "-> Stopping docker container..."
docker container stop ${dockerContainerName}
echo "Stopped."