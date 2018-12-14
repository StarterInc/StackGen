---
title: "Getting Started"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA17474-640x350.jpg"
date: "2017-03-23"
slug: "getting-started"
category: "tech"
tags:
    - tag
    - tutorials
    - hands-on guides
---
#### Introduction

Starter Ignite can be used either as a Java command-line program, or as a library called from your own Java code.

The primary means of controlling the output of the program is via command line arguments.

```
java -jar StarterIgnite-1.0.0.jar -DSWAGGER_LANG=spring -DSWAGGER_LIB=resteasy ...
```

These settings are converted to System properties within the Ignite program and are used to configure the output from the program.

```java

// main output type
// (ie: spring, jersey2)
configurator.setLang(SWAGGER_LANG);

// the JSON serialization library to use
// (ie: jersey2, resteasy, resttemplate)
configurator.setLibrary(SWAGGER_LIB);
```

#### Step 0: Download and Install Ignite Dev Prerequisites

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
- Clone Ignite github project

Linus/OSX:
```
git clone https://github.com/StarterInc/Ignite.git
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

> NOTE: Currently Ignite supports generation of MySQL database schemas.

**Roadmap** Support for PostgreSQL, Oracle, and noSQL databases TBD

#### Development Best Practices

- Fork the Ignite project and leverage github with Conventional Commits  [Conventional Commits](https://conventionalcommits.org/)

- Only connect to database server via https.

- To improve DB security, disallow WAN connections to the Database via GRANT permissions, then use an SSH tunnel to connect to the database for local testing.

> TODO: document DB tunneling over SSH

- Use [SecureField&trade;](securefield) configuration for any PII (Personal Identifying Information) data to protect user privacy.

Keep in mind SecureField&trade;s cannot be used in searches, so consider adding appropriate keyword-based search fields.

#### Starter Ignite Code Generation Steps:

1. Swagger Schema -> Java POJOs
2. Java POJOs -> SQL Database Tables
2. SQL Database -> ORM Mapping Data Objects (MyBatis)
3. ORM Mapping Data Objects Wrapped by Ignite Model Objects
4. Use Ignite Model Objects in your Java code (optional)
5. Start Spring Boot microservice and access Model Objects via REST apis  (optional)

#### Step 1: Create an Swagger Schema for your Application

Generating a Starter Ignite application requires that you provide/create an
OpenAPI schema that defines the application's objects, RESTful APIs and the schema
for your SQL database.

[Learn about OpenAPI](http://openapi.org/)

Inputting your Swagger schema to Starter Ignite will result in the creation of a
Java Maven application residing in the generator output folder ("./gen" by default).

  **[Apache Maven](https://maven.apache.org)** is a popular open source Java build tool which manages dependencies, builds the appliction, and allows for
  running Java programs with test and service automation.

  **[MyBatis ORM](http://blog.mybatis.org)** is a popular open source Java Object Relational Mapping library which allows you to define simple and complex Data Objects that map to your SQL database (in this case MySQL).  Using MyBatis, you can easily insert, update, delete and search for data in your Database using convenient Java Data Objects. If you are new to MyBatis, you should check out this [Excellent Tutorial](http://zetcode.com/db/mybatis/).

Before running Starter Ignite on your Swagger spec, you will need to create a Database (MySQL) and a default schema.

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

You should see the Starter Ignite banner and assuming the application starts correctly,
you will see output similar to the following on the console:

```
[main] INFO springfox.documentation.spring.web.scanners.ApiListingReferenceScanner - Scanning for api listing references
Dec 07, 2018 10:38:18 AM org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer start
INFO: Tomcat started on port(s): 8080 (http)
Dec 07, 2018 10:38:18 AM io.starter.ignite.invoker.Swagger2SpringBoot logStarted
INFO: Started Swagger2SpringBoot in 20.395 seconds (JVM running for 38.247)
```

#### Step 3: Access the Running Application

Once launched, your new Starter Ignite application is ready to serve JSON data from then
RESTful endpoints that were defined in the OpenAPI schema.

1. Open a browser: [http://localhost:8080](http://localhost:8080)
2. Verify that the RESTful endpoints match your expectations from the input file
3. If there are problems with your REST api, you can easily fix the OpenAPI schema
and then re-run the Ignite generation script.

Now is the time to correct any obvious errors in the Schema as it will speed up further
development the more complete and correct your underlying application is.

If you have set the option to overwrite the Database tables if they already exist,
then any data you have entered in the database will be saved off in the renamed
original table.

Encrypted data can only be recovered from SecureFields&trade; columns if the SecureFields
setting is enabled on the field in the OpenAPI schema.

4. As you can see the application data can be accessed from the REST api, however
Starter Ignite also generates a jar file which is usable as a dependency in your
projects without running the Spring Boot RESTful service.

So you can use your generated Java objects from within your application directly,
through a RESTful api/service or both, depending upon your application architecture.

5. TODO: enable monitoring
6. TODO: describe app testing

#### Resources

Using Starter Ignite, you should experience a significant boost in productivity
when building database applications and services.

The benefits of declaring your Object and API definitions in a human-readable
text file format, and regenerating as much of the "plumbing" code as possible
when changes are made, become even more compelling as application complexity
increases.

Build on robust, industry-standard libraries and platforms such as Swagger CodeGen
Apache Spring, and MyBatis ORM, Starter Ignite aims to give you a solid foundation for
building sophisticated and secure applications for any purpose.

##### Coming Soon: Automator.ai&trade;
*Starter Automator.AI*: Looking for even more rapid development with even less coding?

Starter Automator.AI&trade; is an LCDP (Low Code Development Platform) which combines
the power of Starter Ignite&trade; with the massive productivity of an iBPM.

Starter Automator.AI Exclusive Features:
- Graphical Modeling and Design of App Workflows
- Enhanced data security using the Starter SecureField&trade; encryption plugin
- Data and API Modeling using Starter Ignite&trade; Code generation
- Business-friendly spreadsheet logic and reporting plugin
- AI-enhanced Decisioning
