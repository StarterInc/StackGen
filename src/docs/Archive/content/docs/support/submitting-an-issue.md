---
title: "Submitting an Issue"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA18322-640x350.jpg"
author: "johnmcmahon"
needsAuth: false
url: /wth
slug: "submitting-tickets"
date: "2019-02-01"
category: "StackGen"
tags:
    - StackGen
    - procedures
    - support
---
### Submitting a StackGen Issue

> Before diving into troubleshooting be sure to setup your environment

### Issue Submission Checklist

1. Provide a highly detailed description of the problem including but not limited to:
- narrative description of the issue and impact details
- the OS and versions of any servers and clients involved in the issue
- any database(s) type and versions "mysql-5.7.23.R4"
- environment details such as: "Cloud hosted EC2 instance running Ubuntu 14"
- copy/paste any error messages, or stacktraces that you have noticed

2. Create a zipfile (archive) of the following:
- The OpenAPI/Swagger spec file (YAML/JSON) used to generate the Stack with the problem
- A full database dump (ie: mysqldump) of the database SCHEMA ONLY (NOT DATA) created for the Stack (if any)
- The **/gen/target/xxxx-exec-x.x.x.jar** generated for the Stack
- The latest logfile(s) showing any stacktraces or error/warning messages related to the issue
- Screenshot(s) of the problem including UI -- please blur out sensitive information!
