#Starter Ignite React

##Enabling Document Driven Development (DDD) for the 2020s.

Generate data-driven React Native apps from an OpenAPI spec file.

 Generate an app from swagger YAML
 *   - YAML -> Swagger client
 *   - Swagger Client -> entity classes
 *   - Swagger Client -> DML for database
 *   - Swagger CLient -> React-native JS screens

#Generation Steps:

##INIT
Clear out the generation output folder
	
##SWAGGER
Generate OpenApi (Swqgger) clients

##DB
Generate Database via corresponding DML statements to create a SQL database
execute DB creation, connect and test

##ORM
generate MyBatis client classes
XML configuration file

##ENHANCE JAVA MODEL
annotated wrapper with encryption (optional), logging, annotations
create wrapper classes which delegates calls to/from api to the mybatis entity

##CLIENTS
generate React Redux Mobile apps
run unit tests

##CI/DEPLOY
TODO
