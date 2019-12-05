---
title: "Troubleshooting"
cover: "images/mission_street.jpeg"
author: "johnmcmahon"
needsAuth: false
url: /wth
slug: "troubleshooting"
date: "2018-12-18"
category: "StackGen"
tags:
    - ignite
    - support
---
### Troubleshooting StackGen

> Before diving into troubleshooting be sure to setup your environment

**Use debug mode while generating code.**  This will greatly increase the verbosity of the output and the detailed information about the code generation process.
```java
 ... -Ddebug=true -DdebugSwagger=true...
```

**Turn on trace debugging for the Generator** The default log file for the app generator is located at:
```
starter-ignite-gen.log
```

**Increase the verbosity of the output log** Change the rootCategory to "TRACE" from the default of "WARN."

***/src/main/resources/application.properties***
```
# Set root category priority to INFO and its only appender to CONSOLE.
log4j.rootCategory=TRACE, CONSOLE, fileAppender
...
log4j.appender.fileAppender.File=logs/starter-ignite-gen.log

```

### Troubleshooting issues with the input Swagger/OpenAPI

Avoiding the usage of reserved words in StackGen can be difficult as there are quite a few opportunities for code generation to build a forbidden string deep in the codebase.

In some cases an auto-generated source file will collide with reserved word handling. This can result in hard to explain uses of reserved words not found in your input Swagger spec.

```
[main] WARN io.swagger.codegen.languages.AbstractJavaCodegen - ApiResponse (reserved word) cannot be used as model name. Renamed to ModelApiResponse
...
```
In this case, everything compiles fine, but there is an exception when running the Spring Boot generated service as there is no matching Autowire implementation for ApiResponseApiDelegate -- it has been renamed to ModelApiResponseApiDelegate causing a naming mismatch.

```
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 26.356 s
[INFO] Finished at: 2018-12-18T12:17:17-08:00
[INFO] Final Memory: 57M/769M
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:1.5.9.RELEASE:run (default-cli) on project ignite: An exception occurred while running. null: InvocationTargetException: Error creating bean with name 'apiResponseApiController' defined in file [../StarterStackGen/gen/target/classes/io/starter/ignite/api/ApiResponseApiController.class]: Unsatisfied dependency expressed through constructor parameter 0; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'io.starter.ignite.api.ApiResponseApiDelegate' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {} -> [Help 1]
```


Some reserved words to avoid in the Swagger specification:
```
// used as internal variables, can collide with parameter names
"localVarPath", "localVarQueryParams", "localVarCollectionQueryParams",
"localVarHeaderParams", "localVarFormParams", "localVarPostBody",
"localVarAccepts", "localVarAccept", "localVarContentTypes",
"localVarContentType", "localVarAuthNames", "localReturnType",
"ApiClient", "ApiException", "ApiResponse", "Configuration", "StringUtil",

// language reserved words
"abstract", "continue", "for", "new", "switch", "assert",
"default", "if", "package", "synchronized", "boolean", "do", "goto", "private",
"this", "break", "double", "implements", "protected", "throw", "byte", "else",
"import", "public", "throws", "case", "enum", "instanceof", "return", "transient",
"catch", "extends", "int", "short", "try", "char", "final", "interface", "static",
"void", "class", "finally", "long", "strictfp", "volatile", "const", "float",
"native", "super", "while", "null")
```

### Troubleshooting issues with compiling the generated output code

One issue that can occur is when regenerating Stacks that have changes to the schema fields but with:

```
java ... -DdbGenDropTable=false
```

In this scenario, the data API and DAO objects are generated from the updated schema, but when the database mappings occur, the columns are based upon the prior schema.

You will see this exhibited as a Compilation failure similar to the following. Note that all the other mapped classes and schema files are OK and only the fields missing via the column changes are not found:

```
[INFO] -------------------------------------------------------------
[ERROR] COMPILATION ERROR :
[INFO] -------------------------------------------------------------
[ERROR] gen/src/main/java/io/starter/stackgen/model/dao/StackgenOrder.java:[57,24] cannot find symbol
symbol:   variable tradeId
location: variable delegate of type io.starter.stackgen.model.Order
[ERROR] gen/src/main/java/io/starter/stackgen/model/dao/StackgenOrder.java:[69,17] cannot find symbol
symbol:   variable tradeId
location: variable delegate of type io.starter.stackgen.model.Order
[ERROR] gen/src/main/java/io/starter/stackgen/model/dao/StackgenOrder.java:[105,33] cannot find symbol
symbol:   variable executionDate
location: variable delegate of type io.starter.stackgen.model.Order
[ERROR] gen/src/main/java/io/starter/stackgen/model/dao/StackgenOrder.java:[121,17] cannot find symbol
symbol:   variable executionDate
location: variable delegate of type io.starter.stackgen.model.Order
[INFO] 4 errors

```

To fix this issue: delete or rename the obsolete table manually in the target database, then re-run the generator. The single table will be re-generated and the columns will correctly reflect the updated schema.


### Troubleshooting issues with running the generated output code

***Java Version Issues***
StackGen is compatible with Java Runtime Environment (JRE) version 1.8 and newer.

Some Linux distros and Cloud servers have outdated versions of Java (ie: 1.6, 1.7) which will need to be upgraded to run StackGen.

Typically this situation will result in the following error output:

```
[ec2-user@instance StackGen]$ bash start.sh
Exception in thread "main" java.lang.UnsupportedClassVersionError: org/springframework/boot/loader/JarLauncher : Unsupported major.minor version 52.0
	at java.lang.ClassLoader.defineClass1(Native Method)
	at java.lang.ClassLoader.defineClass(ClassLoader.java:808)
...
	at sun.launcher.LauncherHelper.checkAndLoadMain(LauncherHelper.java:482)
[ec2-user@instance StackGen]$ java -version
java version "1.7.0_201"
OpenJDK Runtime Environment (amzn-2.6.16.0.78.amzn1-x86_64 u201-b00)
OpenJDK 64-Bit Server VM (build 24.201-b00, mixed mode)
[ec2-user@instance StackGen]$
```

**Upgrading JDK versions on Amazon Linux**
To install "Amazon Coretto 11" (recommended):

Download RPM and install via local rpm install
https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html

then run:

```
> sudo yum localinstall
```

To remove java 1.7 and install OpenJDK 1.8.0:
```
sudo yum install java-1.8.0-openjdk-devel

sudo yum remove java-1.7.0-openjdk

sudo /usr/sbin/alternatives --config java

sudo /usr/sbin/alternatives --config javac

```

***Missing Startup Parameters***

If you do not provide the required parameters on the launcher command line when running StackGen generated apps, you will see an exception containing text similar to the following:

```
... Could not resolve placeholder 'servicePort' in value "${servicePort}" ...

```

The missing parameters should be self explanatory. Please supply the values via System props or command line options:

```
java ... -DservicePort=8443 ... -jar stackgen-version-exec.jar
```

or

```
mvn spring-boot:run ... -DservicePort=8443 ...
```

**Overriding vs. Delegation with DAO Classes**

When using a StackGen "DAO Service" class (by convention named {Objectname}Service) as Java base class, you may encounter issues similar to this when persisting:

```
io.starter.ignite.generator.IgniteException: Could not run INSERT: org.apache.ibatis.exceptions.PersistenceException: 
### Error updating database.  Cause: org.apache.ibatis.reflection.ReflectionException: Illegal overloaded getter method with ambiguous type for property accountNonLocked in class class io.starter.stackgen.SpringUser. This breaks the JavaBeans specification and can cause unpredictable results.
### Cause: org.apache.ibatis.reflection.ReflectionException: Illegal overloaded getter method with ambiguous type for property accountNonLocked in class class io.starter.stackgen.SpringUser. This breaks the JavaBeans specification and can cause unpredictable results.
	at io.starter.stackgen.model.UserService.insert(UserService.java:462)
	at io.starter.ignite.SpringUserTest.userCreated(SpringUserTest.java:19)
...

```

This is caused by code such as:

```

	@Override
	public boolean isCredentialsNonExpired() {

		return super.getCredentialNonExpired();
	}
```

This can be worked around by using a delegate pattern instead of subclassing, if possible:

```

	UserService delegate = new UserService();
	
	@Override
	public boolean isCredentialsNonExpired() {

		return delegate.getCredentialNonExpired().booleanValue(); // <- use a delegate UserService
	}
```

