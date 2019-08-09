---
title: "Setup SSL for StackGen Service (Spring Boot)"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA11777-640x350.jpg"
slug: "ssl-setup"
needsAuth: false
author: "johnmcmahon"
url: /ssl-setup
category: "tech"
date: "2019-7-01"
tags:
    - docs
    - architecture
    - best practices
    - spring-boot
    - deployment
---

### Setup SSL for Spring Boot

SETUP AWS LINUX

Download RPM and install via local rpm install
https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html

```bash
> sudo yum localinstall
```

https://dzone.com/articles/spring-boot-secured-by-lets-encrypt

Path to /etc/letsencrypt/live/<sitename>/fullchain.pem

```bash
> ./certbot-auto certonly -a standalone -d <hostname>
```

Because Spring Boot does not read the .pem cert types provided by letsencrypt, we need to convert to pk12 format using the following command:

```bash
cd /home/ec2_user/{{artifactId}}/

openssl pkcs12 -export -in /etc/letsencrypt/live/<hostname>/fullchain.pem -inkey /etc/letsencrypt/live/<hostname>/privkey.pem  -out keystore.p12  -name tomcat  -CAfile /etc/letsencrypt/live/<hostname>/chain.pem  -caname root
```

Finally, you need to uncomment the section in the application.properties file for your StackGen instance.

You can do this in the pre-compiled generated output file, then build your deployable StackGen jarfile.

/gen/src/main/resources/application.properties

```bash
# SSL Setup
# see: http://docs.stackgen.io/

# server.port:8443
# security.require-ssl=true
# server.ssl.key-store:/home/ec2_user/{{artifactId}}/keystore.p12
# server.ssl.key-store-password:${adminPassword}
# server.ssl.keyStoreType:PKCS12
# server.ssl.keyAlias:tomcat
```