#!/bin/bash

#################################################################################################
# Bash Shell script to execute psql commands to create fitbitconnect database from docker image #
#################################################################################################

# Set the values for database variables
postgresUser="postgres"
dockerImageName="postgres"
dockerContainerName="postgres-docker"
postgresPassword="fitbitConnect"
databaseName="fitbitconnect"

# Check if docker is running
if [ ! "$(docker -v)" ]; then
  echo "Docker is not running."
  exit 1
fi

echo "dockerImageName: ${dockerImageName}"
echo "dockerContainerName: ${dockerContainerName}"
echo "postgresPassword: ${postgresPassword}"
echo "databaseName: ${databaseName}"

# Check if docker container exists -> if not, download and run it
if [ ! "$(docker container ls -a | grep ${dockerContainerName})" ]; then
    echo "-> Downloading image ${dockerImageName}/starting container ${dockerContainerName}..."
    # Download and start postgres docker image
    docker run --name ${dockerContainerName} -e POSTGRES_PASSWORD=${postgresPassword} -p 5432:5432 -d ${dockerImageName}
    echo "Downloaded/started."

# Check if docker container is running -> if not, start it
elif [ ! "$(docker ps -q -f name=${dockerContainerName})" ]; then
    echo "-> Starting container ${dockerContainerName}..."
    # Start docker postgres container
    docker container start ${dockerContainerName}
    echo "Started."
fi

# Create database and grant privileges
if [[ "$OSTYPE" == "cygwin" ]]; then
        # POSIX compatibility layer and Linux environment emulation for Windows
        echo "cygwin - not implemented - database will not be created..."

elif [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "linux-gnu" ]]; then
        # Lightweight shell and GNU utilities compiled for Windows (part of MinGW)
        echo "$OSTYPE"
        echo "=> Connecting to container ${dockerContainerName} and creating database..."

        # Check if fitbitconnect database exists -> if not, create it
        if [ ! "$(docker exec postgres-docker psql -U ${postgresUser} -c "\l" | grep fitbitconnect)" ]; then
            echo "-> Creating database..."
            docker exec ${dockerContainerName} psql -U ${postgresUser} -c "CREATE DATABASE ${databaseName}"
            echo "Created."

            echo "-> Granting privileges to user on database..."
            docker exec ${dockerContainerName} psql -U ${postgresUser} -c "GRANT ALL PRIVILEGES ON DATABASE ${databaseName} TO ${postgresUser}"
            echo "Granted."
            echo "=> Database created and all privileges granted."
        else
            echo "=> Database already exists."
        fi

        if [[ "$OSTYPE" == "msys" ]]; then
            echo "=> Grepping IPv4 address for docker image..."
            ipconfig | grep "IPv4 Address" | tail -1
        fi
else
        # Unknown.
        echo "Unknown - database will not be created..."
fi

#Examples:
#Execute psql commands:
#Note: you can also add -h hostname -U username in the below commands.

#psql -U postgres -c "CREATE DATABASE fitbitconnect"
#psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE fitbitconnect TO postgres"

#psql -d $database -c "CREATE TABLE public.tbl_students(rno integer, name character varying)"
#psql -d $database -c "INSERT INTO public.tbl_students VALUES (1,'Luka'),(2,'Marko'),(3,'Robert'),(4,'Matea'),(5,'Ana')"
#psql -d $database -c "SELECT *FROM public.tbl_students"

#Assign table count to variable
#TableCount=$(psql -d $database -t -c "select count(1) from public.tbl_students")

#Print the value of variable
#echo "Total table records count....:"$TableCount
