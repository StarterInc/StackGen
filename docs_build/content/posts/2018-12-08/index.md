---
title: "Swagger and OpenAPI"
cover: "images/mission_street.jpeg"
author: "johnmcmahon"
slug: "swagger-and-openapi"
date: "2018-12-08"
category: "OpenAPI"
tags:
    - ignite
    - v1
    - OpenAPI
    - Swagger
    - CodeGen
---

### Introduction to Swagger and OpenAPI Specification and Docs.

> Swagger REST api specification format was adopted as a standard and is now migrating to OpenAPI standard

You may have heard of "Swagger" or seen the brightly colored auto-generated Swagger REST api documentation:

![alt text](https://github.com/StarterInc/Ignite/blob/gh-pages/images/CoinBot-Swagger.png?raw=true "Swagger REST api docs")

Initially conceived as a way to document REST apis, swagger was increasingly used to generate the actual code behind the REST apis themselves.

The Swagger specification is a simple YAML or JSON document which defines the REST endpoints (the URLs used to access the API data) as well as the type of data returned by the REST api.

The ability to declare and define an Application's data objects and REST services with a simple, human readable file format is a powerful paradigm for development in general.

##### Why Swagger?
SmartBear came up with the [Swagger](https://swagger.io) specification a number of years ago and saw a huge increase in REST apis being released across the technology industry.

As REST apis proliferated and became the de-facto backend technology powering mobile and web applications, it became clear that there were multiple areas of need for management and automation. These concerns included standardization of REST apis across platforms, supporting agile requirements development, and the management of rapidly changing client applications.

The time-consuming work of maintaining REST apis -- many of which were simple extensions of database functionality -- drove a wave of development towards code generation and "design-first" development.

Since much of what we use REST apis for can fondly referred to as [CRUD Operations](CRUD operations) a large amount of functionality have been automated via code generating tools such as MyBatis and with heavily annotated scanning/decorating frameworks like Java Spring.

> #### A Historical Sidenote
 As a big proponent of [Service Design](https://en.wikipedia.org/wiki/Service_design) I would say that I always put design first, but what always eventually lagged development was **documentation** and it was the need for generating up-to-date technical documentation for [ExtenXLS](https://en.wikipedia.org/wiki/ExtenXLS) and [Sheetster](https://en.wikipedia.org/wiki/Sheetster) which led me and the team at Extentech to develop one of the very early REST api / Java document site generators.

> In our case we were needed to leverage the JavaDoc in the codebase so that we could document functionality at the source code level and know that it would be reflected and updated throughout.

> Our approach was to convert our JavaDoc to WADL -- an XML variant designed for describing Web Services -- and then to apply a complex XSLT to generate a static version of the code as a somewhat readable and attractive html that for REST apis provided **sample code** and testable endpoints. We ran it from our main build script -- ant -- and presto :fresh docs!

Luckily for us, the team at SmartBear took this very same problem on and crushed it. And we now have Swagger!

#### Show me the Swagger!

Here is a simple example:

```
swagger: "2.0"
info:
  description: "This is a chainring server"
  version: "1.0.0"
  title: "Swagger Chainring"
  termsOfService: "http://starter.io/terms/"
  contact:
    email: "admin@starter.io"
  license:
    name: "Apache 2.0"
    url: "http://www.apache.org/licenses/LICENSE-2.0.html"
host: "chainring.pro"
basePath: "/v1"
tags:
- name: "trade"
  description: "Everything about your trades"
  externalDocs:
    description: "Find out more"
    url: "http://starter.io"
- name: "order"
  description: "Access to chainring orders"
- name: "user"
  description: "Operations about user"
  externalDocs:
    description: "Find out more about our order"
    url: "http://starter.io"
schemes:
- "https"
- "http"
paths:
  /trade:
    post:
      tags:
      - "trade"
      summary: "Add a new trade to the order"
      description: ""
      operationId: "addtrade"
      consumes:
      - "application/json"
      - "application/xml"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - in: "body"
        name: "body"
        description: "trade object that needs to be added to the order"
        required: true
        schema:
          $ref: "#/definitions/trade"
      responses:
        405:
          description: "Invalid input"
      security:
      - chainring_auth:
        - "write:trades"
        - "read:trades"

...

definitions:
  Account:
    type: "object"
    properties:
      id:
        type: "integer"
        format: "int64"
      accountId:
        type: "integer"
        format: "int64"
      value:
        type: "integer"
        format: "int32"
      institutionName:
        type: "string"
      accountNumber:
        type: "string"
      routingId:
        type: "string"    
      modifiedDate:
        type: "string"
        format: "date-time"
      status:
        type: "string"
        description: "Account Status"
        enum:
        - "ready"
        - "closed"
        - "executed"
      verified:
        type: "boolean"
        default: false
    xml:
      name: "Account"

```
Swagger CodeGen has a set of templates covering many of the world's most popular languages and libraries

### Swagger CodeGen and Ignite

Starter Ignite includes a customized version of Swagger CodeGen in order to convert the input OpenAPI/Swagger Specification file into the initial set of Java POJOs (Plain Old Java Objects)

Ignite uses either the SpringBoot or the Java Jersey2 CodeGen output to create full blown application stacks which include Database, Encryption, and AI capabilities.

### Resources

Recommended reading and resources for Swagger and OpenAPI:

* [OpenAPI Initiative](https://www.openapis.org)
* [SwaggerHub Directory](https://app.swaggerhub.com/search)
* [Swagger CodeGen Project](https://github.com/swagger-api/swagger-codegen)
* [A Brief History of the OpenAPI Specification](https://dev.to/mikeralphson/a-brief-history-of-the-openapi-specification-3g27)
