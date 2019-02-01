---
title: "StackGen Service Security"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA18322-640x350.jpg"
slug: "ignite-service-security"
author: "johnmcmahon"
needsAuth: false
url: /wth
category: "tech"
date: "2019-01-15"
tags:
    - docs
    - architecture
    - best practices
    - security
---

### What We Mean When We Talk About **SECURITY**

One of the mandates of StackGen is **High Security**

> **High Security**: in Computer Systems concept of a system designed and operated with a focus on security and best practices.

Because of this requirement, StackGen is designed for security in many ways.

The following non-exhaustive list of ways  that StackGen implements and/or supports a security best practice or industry standard:

<blockquote>

-
- **PRO** Generate and launch a cluster of related Microservices as well as a Management Console to

- **PRO** Generate, deploy, and launch a complete full-stack ReactJS/React Native app with a clustered fault tolerant AWS Cloud Spring Boot backend with [END-TO-END SSL](#endToEndSSL) replicated, fault tolerant [ENCRYPTED DATABASE](#encryptedDatabase) RDS/Aurora Database and field level encrypted SecureField encryption to protect customer private information should somehow access controls to underlying table data are breached.

</blockquote>

> There is no such thing as "Secure Computing": Always define your terms and know what is at risk.

[**END TO END SSL**](endToEndSSL) StackGen in many if not most cases will be used within the context of a Web-tier application. In whatever configuration is used, production systems should use end to end SSL encryption to protect data "in flight."

In the past, sophisticated attackers have breached private networks and gained access to network traffic. This can happen in accidental ways as well such as in a case of connecting to a server over an unencrypted public WiFi connection for example.

With a "sniffer" on the network, hackers can read and store any data that travels over the shared network hardware (routers ethernet hubs, servers etc.) and read the contents.

In the case of cleartext or poorly encrypted CipherText, this is the digital equivalent of leaving a bag full of cash on a train platform.

By encrypting the communications channel and data transfer between systems, SSL allows you to safely communicate between the services in your stack such as the connection between your app server and your database server.

It is typicaly to install an SSL cert in a proxy service like Apache or NGINX and then subsequently use https

AWS supports end-to-end encryption between EC2, S3, and RDS as well as other services to ensure that no snooping can occur between the layers of your application stack.

> There is no such thing as "Secure Computing": Always define your terms and know what is at risk.

[**ENCRYPTED DATABASE**](encryptedDatabase) StackGen assuming you are using RDS via Aurora etc. on Amazon AWS, you will be able to encrypt the database files on disk using a simple setting.

This extra level of data assurance has little downside in most applications although it may complicate snapshots and replication between availability zones.

### SPRING-BOOT SECURITY configuration

Out of the box, StackGen uses a default Spring Boot configuration intended for development environments only.

Prior to deployment of a generated StackGen service, appropriate review of the WebApplictionConfiguration settings should be reviewed and changed to appropriately secure your application.




### SPRING-BOOT SECURITY configuration


> Regenerate Early, Regenerate Often
