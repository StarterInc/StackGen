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

### Let's Talk About **Security**

One of the mandates of StackGen is maintaining execllent **Security**

> **High Security**: in Computer Systems the concept of a system that is designed and operated with adherence to a thorough and modern set of security best practices.

StackGen is designed for security in many ways.

The following non-exhaustive list of ways  that StackGen implements and/or supports a security best practice or industry standard:

<blockquote>

- **PRO** Generate and launch a cluster of related Microservices as well as a Management Console to track security-related parameters and logs, and isolate and identify suspicious behavior at the app level

- **PRO** Generate, deploy, and launch a fault tolerant [ENCRYPTED DATABASE](#encryptedDatabase) RDS/Aurora Database with field level encrypted SecureField encryption to protect customer private information should somehow access controls to underlying table data be breached.

</blockquote>

> Let's be frank: There is no such thing as "100% Secure Computing". Therefore, always define security in terms of what the cost benefit of and effectiveness of approaches -- and always know what is at risk.

[**END TO END SSL**](endToEndSSL) StackGen in most cases will be used within the context of a Web-tier application. In whatever configuration is used, production systems should use end to end SSL encryption to protect data "in flight."

StackGen service instances launched via AWS are by default configured with auto-renewing SSL certificates and thus a valid SSL-secured endpoint for your data.

**The Risks of Cleartext Data**

In the past, sophisticated attackers have breached private networks and gained access to network traffic. This can happen in accidental ways as well such as in a case of connecting to a server over an unencrypted public WiFi connection for example.

With a "sniffer" on the network, hackers can read and store any data that travels over the shared network hardware (routers ethernet hubs, servers etc.) and read the contents.

In the case of cleartext or poorly encrypted CipherText, this is the digital equivalent of leaving a bag full of cash on a train platform.

By encrypting the communications channel and data transfer between systems, SSL allows you to safely communicate between the services in your stack such as the connection between your app server and your database server.

It is typicaly to install an SSL cert in a proxy service like Apache or NGINX and then subsequently use https

AWS supports end-to-end encryption between EC2, S3, and RDS as well as other services to ensure that no snooping can occur between the layers of your application stack.

[**ENCRYPTED DATABASE**](encryptedDatabase) StackGen assuming you are using RDS via Aurora etc. on Amazon AWS, you will be able to encrypt the database files on disk using a simple setting.

This extra level of data assurance has little downside in most applications although it may complicate snapshots and replication between availability zones.

### SPRING-BOOT SECURITY configuration

> NOTE: Out of the box, StackGen uses a default Spring Boot configuration intended for **development** environments only.

Prior to deployment of a generated StackGen service, appropriate review of the WebApplictionConfiguration settings should be reviewed and changed to appropriately secure your application.

### SPRING-BOOT SECURITY configuration

```


```

> Regenerate Early, Regenerate Often
