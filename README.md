# Starter StackGen&trade; Code Generator

![StackGen CI](https://github.com/StarterInc/StackGen/workflows/StackGen%20CI/badge.svg?branch=master)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FStarterInc%2FStackGen.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2FStarterInc%2FStackGen?ref=badge_shield)

## Generate Enterprise Stacks from OpenAPI -- CodeGen for Coders

Take an input OpenAPI/Swagger spec file and generate a full-stack Spring Boot microservice.

Generates Spring MVC/Model Objects and publishes via REST apis. Full stack RESTful MicroServices using simple YAML files to define your data models.

### Don't refactor... Regenerate!

 Generate an app from swagger YAML
 - YAML -> Swagger client
 - Swagger Client -> entity classes
 - Swagger Client -> DML for database
 - Swagger Client -> ReactJS/ReactNative/React360 screens

#### Introduction

Starter StackGen can be used either as a Java command-line program, or as a library called from your own Java code.

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

#### Development Best Practices

- Fork the StackGen project and leverage github with Conventional Commits  [Conventional Commits](https://conventionalcommits.org/)

- Only connect to database server via https.

- To improve DB security, disallow WAN connections to the Database via GRANT permissions, then use an SSH tunnel to connect to the database for local testing.

> TODO: document DB tunneling over SSH

- Use [SecureField&trade;](securefield) configuration for any PII (Personal Identifying Information) data to protect user privacy.

Keep in mind SecureField&trade;s cannot be used in searches, so consider adding appropriate keyword-based search fields.

#### Starter StackGen Code Generation Steps:

1. Swagger Schema -> Java POJOs
2. Java POJOs -> SQL Database Tables
2. SQL Database -> ORM Mapping Data Objects (MyBatis)
3. ORM Mapping Data Objects Wrapped by StackGen Model Objects
4. Use StackGen Model Objects in your Java code (optional)
5. Start Spring Boot microservice and access Model Objects via REST apis  (optional)

#### Step 1: Create an Swagger Schema for your Application

Generating a Starter StackGen application requires that you provide/create an
OpenAPI schema that defines the application's objects, RESTful APIs and the schema
for your SQL database.

[Learn about OpenAPI](http://openapi.org/)

Inputting your Swagger schema to Starter StackGen will result in the creation of a
Java Maven application residing in the generator output folder ("./gen" by default).

  **[Apache Maven](https://maven.apache.org)** is a popular open source Java build tool which manages dependencies, builds the appliction, and allows for
  running Java programs with test and service automation.

  **[MyBatis ORM](http://blog.mybatis.org)** is a popular open source Java Object Relational Mapping library which allows you to define simple and complex Data Objects that map to your SQL database (in this case MySQL).  Using MyBatis, you can easily insert, update, delete and search for data in your Database using convenient Java Data Objects. If you are new to MyBatis, you should check out this [Excellent Tutorial](http://zetcode.com/db/mybatis/).

Before running Starter StackGen on your Swagger spec, you will need to create a Database (MySQL) and a default schema.

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


You should see the Starter StackGen banner and assuming the application starts correctly,
you will see output similar to the following on the console:

```
[main] INFO springfox.documentation.spring.web.scanners.ApiListingReferenceScanner - Scanning for api listing references
Dec 07, 2018 10:38:18 AM org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer start
INFO: Tomcat started on port(s): 8080 (http)
Dec 07, 2018 10:38:18 AM io.starter.ignite.invoker.Swagger2SpringBoot logStarted
INFO: Started Swagger2SpringBoot in 20.395 seconds (JVM running for 38.247)
```

#### Step 3: Access the Running Application

Once launched, your new Starter StackGen application is ready to serve JSON data from then
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
Starter StackGen also generates a jar file which is usable as a dependency in your
projects without running the Spring Boot RESTful service.

So you can use your generated Java objects from within your application directly,
through a RESTful api/service or both, depending upon your application architecture.

5. TODO: enable monitoring
6. TODO: describe app testing

#### Resources

The benefits of declaring your Object and API definitions in a human-readable text file format, and regenerating as much code as possible when changes are made, is even more compelling as application complexity increases.


## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FStarterInc%2FStackGen.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2FStarterInc%2FStackGen?ref=badge_large)
