---
title: "Designing Starter Ignite Services with Swagger"
cover: "images/mission_street.jpeg"
author: "johnmcmahon"
slug: "swagger-design"
date: "2018-12-20"
category: "Ignite"
tags:
    - ignite
    - support
---
### About Ignite and Swagger Specification

> At it's most elemental, Starter Ignite is an application/service generator which uses a mostly standard Swagger specification file to define the Object and REST endpoints.

### Requirements of a Valid Ignite Swagger Specification

Every Starter Ignite Swagger Specfication requires some basic information in order to function properly:

- at least one Model specification under the root "definitions"

```java

definitions:
...
User:
    required:
    - id
    - username
    - email
    properties:
      id:
        id:
        type: "integer"
        format: "int64"
      fingerprint:
        type: string
        example: DK$DFSJaraDD
        value: secure
        x-starter-secureField: true
      ssn:
        description: This is the value
        type: string
        example: 111-22-3333
        maxLength: 10
        minLength: 10
        value: secure
        x-starter-secureField: true
      username:
        type: string
        example: Sparky
        x-starter-dataField: component=UserName
      homePage:
        type: string
        format: url
        example:  https://www.acme-corp.com
      social:
        type: string
        format: url
        example:  https://twitter.com/StarterIO
    xml:
      name: "User"
...  
```

### Ignite-Specific Customizations to the Swagger Specification

Starter Ignite Swagger Extensions:

x-starter-secureField: true

x-ignite-dataField: component=<somecomponent>


// skip this model
x-codegen-ignore

// TODO ?
x-codegen-import-mapping
