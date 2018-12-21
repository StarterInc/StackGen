<div align="center">
    <img src="https://github.com/StarterInc/Ignite/blob/gh-pages/logos/logo-1024.png?raw=true" alt="Logo"/>
</div>

# Starter Ignite App Generator

## OpenAPI and Document Driven Development (DDD)

Generate full stack React Native apps with a custom REST api using a text file to define your model.

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

# Prerequisites
To get started with Starter Ignite you will need to have a minimum:

*   Java9 JDK
*   Maven
*   git
*   (optional) React Native
*   (optional) MacOSX / XCode (for iOS apps)
*   MySQL instance

To quickly setup a MySQL instance locally install from this page:

Alternatively you can run a MySQL instance in the cloud inexpensively.

Run a MySQL server on AWS Lightsail for $5/mo.
https://lightsail.aws.amazon.com/

## React Native Setup
To build a react native app, you need to have the latest react native libraries and tooling installed including:

*   node
*   npm
*   react native

    "eslint": "^4.19.1",
    "react": "16.3.1",
    "react-dom": "^16.4.0",
    "react-native": "~0.55.2",
    "react-native-communications": "^2.2.1",
    "react-native-gifted-messenger": "^0.1.4",
    "react-native-localization": "^1.0.9",
    "react-native-vector-icons": "^4.6.0",
    "react-redux": "^5.0.7",
    "react-redux-form": "^1.16.9",
    "redux": "^4.0.0",
    "redux-logger": "^3.0.6",
    "redux-persist": "^5.10.0",
    "redux-promise-middleware": "^5.1.1",
    "redux-thunk": "^2.3.0",
    "react-native-code-push": "^1.17.0-beta",
    "react-native-fetch-blob": "^0.10.2",
    "tcomb-form-native": "^0.6.4"

# Getting Started

## Edit your OpenAPI Specification File
Dropfolder is /<installationdir>/src/resources/openapi_specs/

## Run the generator via Maven task

mvn clean install -DMYBATIS_MAIN=${project_loc}/gen/src/ -DMYBATIS_JAVA=${project_loc}/gen/src/main/java/io/starter/ignite/model/ -DRDS_HOSTNAME=${database_hostname} -DRDS_DB_NAME=ignite -DRDS_USERNAME=igniteuser -DRDS_PASSWORD=${database_password}

## Collect output
Server App output is
${project.dir}/gen/
React Native App output is
${project.dir}/REACT_EXPORT
