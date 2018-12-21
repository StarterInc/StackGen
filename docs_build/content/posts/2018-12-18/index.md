---
title: "Troubleshooting"
cover: "images/mission_street.jpeg"
author: "johnmcmahon"
slug: "troubleshooting"
date: "2018-12-18"
category: "Ignite"
tags:
    - ignite
    - support
---
### Troubleshooting Starter Ignite

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

Avoiding the usage of reserved words in Starter Ignite can be difficult as there are quite a few opportunities for code generation to build a forbidden string deep in the codebase.

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
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:1.5.9.RELEASE:run (default-cli) on project ignite: An exception occurred while running. null: InvocationTargetException: Error creating bean with name 'apiResponseApiController' defined in file [../StarterIgnite/gen/target/classes/io/starter/ignite/api/ApiResponseApiController.class]: Unsatisfied dependency expressed through constructor parameter 0; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'io.starter.ignite.api.ApiResponseApiDelegate' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {} -> [Help 1]
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


### Troubleshooting issues with running the generated output code
