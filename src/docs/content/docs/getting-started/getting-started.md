---
title: "Getting Started"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA17474-640x350.jpg"
date: "2017-03-23"
author: "johnmcmahon"
needsAuth: false
url: /wth
slug: "getting-started"
category: "tech"
tags:
    - tag
    - tutorials
    - hands-on guides
---

#### Introduction

StackGen can be used either as a Java command-line program, or as a library called from your own Java code.

The primary means of controlling the output of the program is via command line arguments.

```
java -jar StarterStackGen-1.0.0.jar -DSWAGGER_LANG=spring -DSWAGGER_LIB=resteasy ...
```

These settings are converted to System properties within the StackGen program and are used to configure the output from the program.

```java

// main output type
// (ie: spring, jersey2)
configurator.setLang(SWAGGER_LANG);

// the JSON serialization library to use
// (ie: jersey2, resteasy, resttemplate)
configurator.setLibrary(SWAGGER_LIB);
```

#### Step 0: Download and Install StackGen Dev Prerequisites

- Java8+ JDK Installed
[Linux] (TODO LINUX) | [OSX] (TODO OSX) | [Windows] (TODO WIN)

- Apache Maven Installed
[Linux] (TODO LINUX) | [Windows] (TODO WIN)
INSTALL Maven (for building latest and running Spring boot)

Linux:
```
sudo apt-get Maven
```

- Install git

Linux:
```
sudo apt-get git
```
- Clone StackGen github project

Linus/OSX:
```
git clone https://github.com/StarterInc/StackGen.git
```

- Install Node and NPM
[Linux] (TODO LINUX) | [OSX] (TODO OSX) | [Windows] (TODO WIN)

OSX (Homebrew install prerequisite):
```
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
brew install node
```

- (optional) Install IDE(s)

- (optional) Install MySQL Workbench
[Linux] (TODO LINUX) | [OSX] (TODO OSX) | [Windows] (TODO WIN)

- (optional) Create LightSail AWS Development Server
[Linux] (TODO LINUX) | [OSX] (TODO OSX) | [Windows] (TODO WIN)

> NOTE: Currently StackGen supports generation of MySQL database schemas.

**Roadmap** Support for PostgreSQL, Oracle, and noSQL databases TBD

#### Development Best Practices

- Fork the StackGen project and leverage github with Conventional Commits  [Conventional Commits](https://conventionalcommits.org/)

- Only connect to database server via https.

- To improve DB security, disallow WAN connections to the Database via GRANT permissions, then use an SSH tunnel to connect to the database for local testing.

> TODO: document DB tunneling over SSH

- Use [SecureField&trade;](securefield) configuration for any PII (Personal Identifying Information) data to protect user privacy.

Keep in mind SecureField&trade;s cannot be used in searches, so consider adding appropriate keyword-based search fields.

#### StackGen Code Generation Steps:

1. Swagger Schema -> Java POJOs
2. Java POJOs -> SQL Database Tables
2. SQL Database -> ORM Mapping Data Objects (MyBatis)
3. ORM Mapping Data Objects Wrapped by StackGen Model Objects
4. Use StackGen Model Objects in your Java code (optional)
5. Start Spring Boot microservice and access Model Objects via REST apis  (optional)

#### Step 1: Create an Swagger Schema for your Application

Generating a StackGen application requires that you provide/create an
OpenAPI schema that defines the application's objects, RESTful APIs and the schema
for your SQL database.

[Learn about OpenAPI](http://openapi.org/)

Inputting your Swagger schema to StackGen will result in the creation of a
Java Maven application residing in the generator output folder ("./gen" by default).

  **[Apache Maven](https://maven.apache.org)** is a popular open source Java build tool which manages dependencies, builds the appliction, and allows for
  running Java programs with test and service automation.

  **[MyBatis ORM](http://blog.mybatis.org)** is a popular open source Java Object Relational Mapping library which allows you to define simple and complex Data Objects that map to your SQL database (in this case MySQL).  Using MyBatis, you can easily insert, update, delete and search for data in your Database using convenient Java Data Objects. If you are new to MyBatis, you should check out this [Excellent Tutorial](http://zetcode.com/db/mybatis/).

Before running StackGen on your Swagger spec, you will need to create a Database (MySQL) and a default schema.

We recommend the use of an AWS LightSail Database instance to run the database and using MySQL Workbench free database management utility to work with the data.

Additionally, you may consider connecting over an SSH tunnel but for now that is left out of the scope of this Tutorial.

#### Step 2: Build and Launch your Generated Application

After generating your application, you can treat the output as you would any standard
Maven Java application.

One of the benefits of using Maven is the dependence on standardized file paths
and project defaults, so the layout of the project files should be familiar to anyone
who has worked with Maven in the past.

```
gen
├── src
│   └── main
│       ├── java
│       │   └── io
│       │       ├── starter
│       │       │   └── ignite
│       │       │       ├── api
│       │       │       ├── invoker
│       │       │       └── model
│       │       │           └── dao <-- THIS IS WHERE THE Data Object Classes Reside
│       │       └── swagger
│       │           └── configuration
│       └── resources
│           └── io
│               └── starter
│                   └── ignite
│                       └── sqlmaps <-- Generated MyBatis ORM Mappings
└── target <-- Maven Build Output
```

To build the project after generation, use the following commands from the command line:

```
mvn clean install -U
```

If you have selected the "spring-boot" (default) application framework, you can run
the service with a single line of code:

```
cd <project_folder>gen
mvn spring-boot:run
```
To distribute the spring boot service, you can simply copy the deployment jar from the "target" folder to your server and launch it directly as an executable jar:

```
testbed$ java -jar -Dio.starter.ignite.secure_key=W43sdfNpOSDF0madfT40wwjTaaadfaeQo449Qa9rygfrm5fX21Dg= -DRDS_HOSTNAME=db.yourco.com -DRDS_DB_NAME=ignite -DRDS_USERNAME=igniteuser -DRDS_PASSWORD=**********  ignite-1.0.1-exec.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.9.RELEASE)

Dec 22, 2018 12:43:47 PM io.starter.ignite.invoker.Swagger2SpringBoot logStarting
INFO: Starting Swagger2SpringBoot v1.0.1 on Johns-MacBook.local with PID 22593 (/Users/john/workspace2019/testbed/ignite-1.0.1-exec.jar started by john in /Users/john/workspace2019/testbed)
```


You should see the StackGen banner and assuming the application starts correctly,
you will see output similar to the following on the console:

```
[main] INFO springfox.documentation.spring.web.scanners.ApiListingReferenceScanner - Scanning for api listing references
Dec 07, 2018 10:38:18 AM org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer start
INFO: Tomcat started on port(s): 8080 (http)
Dec 07, 2018 10:38:18 AM io.starter.ignite.invoker.Swagger2SpringBoot logStarted
INFO: Started Swagger2SpringBoot in 20.395 seconds (JVM running for 38.247)
```

#### Step 3: Access the Running Application

Once launched, your new StackGen application is ready to serve JSON data from then
RESTful endpoints that were defined in the OpenAPI schema.

1. Open a browser: [http://localhost:8080](http://localhost:8080)
2. Verify that the RESTful endpoints match your expectations from the input file
3. If there are problems with your REST api, you can easily fix the OpenAPI schema
and then re-run the StackGen generation script.

Now is the time to correct any obvious errors in the Schema as it will speed up further
development the more complete and correct your underlying application is.

If you have set the option to overwrite the Database tables if they already exist,
then any data you have entered in the database will be saved off in the renamed
original table.

Encrypted data can only be recovered from SecureFields&trade; columns if the SecureFields
setting is enabled on the field in the OpenAPI schema.

4. As you can see the application data can be accessed from the REST api, however
StackGen also generates a jar file which is usable as a dependency in your
projects without running the Spring Boot RESTful service.

So you can use your generated Java objects from within your application directly,
through a RESTful api/service or both, depending upon your application architecture.

5. TODO: enable monitoring
6. TODO: describe app testing

### Configuration Variables

```
ADD_GEN_CLASS_NAME= "Service"
ALTER_TABLE= "ALTER TABLE"
ANON_USERID= null
API_CLASSES= "/projects/StarterStackGen/gen/src/main/java/io/starter/ignite/api/"
API_PACKAGE= "io.starter.ignite.api" (id=22)
API_PACKAGE_DIR= "/io/starter/ignite/api/" (id=23)
ARTIFACT_ID= "ignite" (id=27)
ARTIFACT_VERSION= "1.0.1" (id=28)
AWS_ACCESS_KEY= null
AWS_SECRET_KEY= null
CIPHER_NAME= "AES/CBC/PKCS5PADDING" (id=29)
CONFIG_FILE= "/projects/StarterStackGen/src/resources/swagger_config.json" (id=30)
CREATE_TABLE= "CREATE TABLE" (id=31)
CREATE_TABLE_BEGIN_BLOCK= "(" (id=32)
CREATE_TABLE_END_BLOCK= ");" (id=34)
DATE_FORMAT= java.text.SimpleDateFormat  (id=36)
DB_NAME= "ignite" (id=41)
DB_PASSWORD= "xxxxxxxxxx" (id=42)
DB_TIMEOUT= 10000
DB_URL= "db.us-west-2.rds.amazonaws.com" (id=43)
DB_USER= "igniteuser" (id=44)
DEBUG= false
DISABLE_DATA_FIELD_ASPECT= true
DISABLE_SECURE_FIELD_ASPECT= false
DROP_EXISTING_TABLES= true
DROP_TABLE= "DROP TABLE" (id=45)
FOLDER_SKIP_LIST= java.util.ArrayList<E>  (id=46)
IGNITE_API_PACKAGE= "io.starter.ignite.api" (id=22)
IGNITE_DATAMODEL_PACKAGE= "io.starter.ignite.model.dao" (id=54)
IGNITE_GEN_MODEL_CRUD_OPS= "igniteGenerateCRUDOps" (id=55)
IGNITE_GEN_MODEL_ENHANCEMENTS= "igniteGenerateModelEnhancements" (id=56)
IGNITE_MAJOR_VERSION= 1
IGNITE_MINOR_VERSION= 1
INVOKER_PACKAGE= "io.starter.ignite.invoker" (id=57)
JAVA_GEN_ARCHIVE_FOLDER= "/archive/gen" (id=58)
JAVA_GEN_ARCHIVE_PATH= "/projects/StarterStackGen/archive/gen" (id=59)
JAVA_GEN_FOLDER= "/gen" (id=60)
JAVA_GEN_PATH= "/projects/StarterStackGen/gen" (id=61)
JAVA_GEN_RESOURCES_FOLDER= "/projects/StarterStackGen/gen/src/main/resources" (id=62)
JAVA_GEN_SRC= java.io.File  (id=63)
JAVA_GEN_SRC_FOLDER= "/projects/StarterStackGen/gen/src/main/java" (id=65)
JNDI_DB_LOOKUP_STRING= "jndi/ignite" (id=66)
KEY_SIZE= 256
KEYGEN_INSTANCE_NAME= "AES" (id=67)
logger= org.slf4j.impl.SimpleLogger  (id=68)
LONG_DATE_FORMAT= "MMM/d/yyyy HH:mm:ss Z" (id=73)
MODEL_CLASSES= "/projects/StarterStackGen/gen/src/main/java/io/starter/ignite/model/dao/" (id=74)
MODEL_DAO_PACKAGE_DIR= "/io/starter/ignite/model/dao/" (id=75)
MODEL_PACKAGE= "io.starter.ignite.model.dao" (id=54)
MODEL_PACKAGE_DIR= "/io/starter/ignite/model/" (id=76)
MYBATIS_CLASS_PREFIX= "StackGen" (id=77)
MYBATIS_CONFIG= "/projects/StarterStackGen/src/resources/templates/MyBatisConfig.xml" (id=78)
MYBATIS_CONFIG_OUT= "/projects/StarterStackGen/gen/src/main/resources/MyBatisConfig.xml" (id=79)
MYBATIS_GEN_CONFIG= "/projects/StarterStackGen/src/resources/templates/MyBatisGeneratorConfig.xml" (id=80)
MYBATIS_GEN_CONFIG_OUT= "/projects/StarterStackGen/src/resources/MyBatisGeneratorConfig.xml" (id=81)
MYBATIS_MODEL_CLASSES= "/projects/StarterStackGen/gen/src/main/java/io/starter/ignite/model/dao/" (id=74)
ORG_PACKAGE= "io.starter.ignite" (id=82)
OUTPUT_DIR= "/projects/StarterStackGen/gen" (id=61)
PLUGIN_FOLDER= "/projects/StarterStackGen/src/resources/plugins"
REACT_APP_NAME= "MyLittlePony"
REACT_APP_OUTPUT_FOLDER= "/projects/StarterStackGen/tmp/react/"
REACT_EXPORT_FOLDER= "/projects/StarterStackGen/REACT_EXPORT"
REACT_TEMPLATE_APP_FOLDER= "/projects/StarterStackGen/src/main/react/StackGenApp/"
REACT_TEMPLATE_FOLDER= "/projects/StarterStackGen/src/main/react/template/"
RENAME_TABLE_SUFFIX= "BK_"
ROOT_FOLDER= "/projects/StarterStackGen"
S3_STARTER_MEDIA_BUCKET= null
S3_STARTER_MEDIA_FOLDER= null
S3_STARTER_SERVICE= null
S3_STARTER_SYSTEM_BUCKET= null
SCHEMA_NAME= "ignite"
SECRET_KEY= "W3ngNBCp80mgG0wwjTslfeQoG2hQa9ryqbemTEX01Wg="
SECURE_KEY_PROPERTY= "io.starter.ignite.secure_key"
SESSION_VAR_SQLSESSION= null
SESSION_VAR_USER= null
SETTING_COLUMNS_UPPERCASED= false
SKIP_LIST= java.util.ArrayList<E>
skipDBGen= false
skipMybatis= false
SOURCE_MAIN= "/src/main"
SOURCE_RESOURCES= "/src/resources"
SPEC_LOCATION= "/projects/StarterStackGen/src/resources/openapi_specs/"
SPRING_DELEGATE= "ApiDelegate"
SQL_MAPS_PATH= "io/starter/ignite/sqlmaps/"
SWAGGER_LANG= "spring"
SWAGGER_LIB= "spring-boot"
TABLE_NAME_PREFIX= "ignite$"
VERBOSE= false
WEB_JS_ROOT= "/src/main/webapp/js"
WEB_ROOT= "/src/main/webapp"
WP_PLUGIN_ROOT= "/src/main/src/main/wp"

```


#### Resources

Using StackGen, you should experience a significant boost in productivity
when building database applications and services.

The benefits of declaring your Object and API definitions in a human-readable
text file format, and regenerating as much of the "plumbing" code as possible
when changes are made, become even more compelling as application complexity
increases.

Build on robust, industry-standard libraries and platforms such as Swagger CodeGen
Apache Spring, and MyBatis ORM, StackGen aims to give you a solid foundation for
building sophisticated and secure applications for any purpose.

##### Coming Soon: Automator.ai&trade;
*Starter Automator.AI*: Looking for even more rapid development with even less coding?

Starter Automator.AI&trade; is an LCDP (Low Code Development Platform) which combines
the power of StackGen&trade; with the massive productivity of an iBPM.

Starter Automator.AI Exclusive Features:
- Graphical Modeling and Design of App Workflows
- Enhanced data security using the Starter SecureField&trade; encryption plugin
- Data and API Modeling using StackGen&trade; Code generation
- Business-friendly spreadsheet logic and reporting plugin
- AI-enhanced Decisioning
