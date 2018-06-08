# Starter Ignite React

## Document Driven Development (DDD) for the win.

Generate data-driven React Native apps from an OpenAPI spec file.

 Generate an app from swagger YAML
 *   - YAML -> Swagger client
 *   - Swagger Client -> entity classes
 *   - Swagger Client -> DML for database
 *   - Swagger CLient -> React-native JS screens

# Generation Steps:

## INIT
Clear out the generation output folder
	
## SWAGGER
Generate OpenApi (Swqgger) clients

## DB
Generate Database via corresponding DML statements to create a SQL database
execute DB creation, connect and test

## ORM
generate MyBatis client classes
XML configuration file

## ENHANCED JAVA MODEL
annotated wrapper with encryption (optional), logging, annotations
create wrapper classes which delegates calls to/from api to the mybatis entity

## CLIENTS
generate React Redux Mobile apps
run unit tests

## CI/DEPLOY
TODO

# Getting Started

## Edit your OpenAPI Specification File
TODO: make dropfolder user configurable
Dropfolder is /<installationdir>/src/resources/openapi_specs/

## Run the generator via Maven task

mvn clean install -DMYBATIS_MAIN=${project_loc}/gen/src/ -DMYBATIS_JAVA=${project_loc}/gen/src/main/java/io/starter/ignite/model/ -DRDS_HOSTNAME=${database_hostname} -DRDS_DB_NAME=ignite -DRDS_USERNAME=igniteuser -DRDS_PASSWORD=${database_password}

## Collect output
Server App output is
 /<installationdir>/gen/
React Native App output is
 /<installationdir>/REACT_EXPORT
 
 
