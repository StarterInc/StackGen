---
title: "FAQ"
order: 7
cover: "images/mission_street.jpeg"
author: "johnmcmahon"
needsAuth: false
url: /wth
slug: "faq"
date: "2018-12-19"
category: "StackGen"
tags:
    - ignite
    - support
---
### Frequently Asked Questions

> Below are answers to some of the most common questions.

### Is StackGen Free?

Yes. It is freely downloadable from GitHub and licensed under the AGPL.

### Who should use StackGen?

Tech savvy business users and developers who want to rapidly design and deploy RESTful apis with maximum portability and agility.

### Does Starter offer support for StackGen?

Yes! Support for StackGen will be offered by Starter Inc. after the initial production release.

### Can I run my StackGen Services in the Cloud?

In the near future, there will be a StackGen Cloud offering.

## apiCloud.co

### Can I run my StackGen Services on my own Computers?

Yes! We strive for maximum portability and no vendor lock-in: you can run your apps in the cloud or on premise or both.

**At Starter, we believe you should control your digital destiny!**

### What are the benefits to using StackGen?

- Incredibly fast design and deployment of robust web Services
- Agile from the ground up, don't refactor, regenerate!
- AWS Cloud-enabled for rapid deployment with zero overhead
- Best-in-class architecture delivers high performance with bank-level security

### What are the benefits to upgrading to Starter Automator.ai?
(coming Q1 2019)

- All the benefits of StackGen
- Web based iBPM with StackGen-powered Workflows
- AI-enabled decision engine
- Spreadsheet Logic Modules

### What are the downsides to using StackGen?

- Currently we only support Java Spring and Java Jersey2 server applications
- Product is still in beta (Q4 2018)

### What are the alternatives to using StackGen?

- Custom application Development
- Build automation platform
- Online app generation services (ie :APICurio)
- AWS Lambda
- IBM Cloud

### What is the StackGen Roadmap?

What does this mean when I see it in the code?
```
// TODO: implement some cool new feature
```

### How can I support the development of StackGen?

- We are currently building out our paid service offerings, but in the meantime if you want to support the StackGen open source project financially, we are setup to accept donations via PayPal.

<< donate via paypal >>

### What if I need feature X -- can I subsidize development of StackGen features that I require?

Perhaps!  Contact us at info@starter.io

### How does StackGen generate code?

We use 3 code generation steps with 3 different implementations.

Why? Because the 3 main components of the app -- Swagger CodeGen, MyBatis Generated DAOs and Mappings, and the StackGen SecureField and DataField classes all take a different approach to code generation.

Additionally, Mustache is used by both the Swagger CodeGen and the StackGen React code to generate React content from Mustache template files.

- [Mustache](https://github.com/spullara/mustache.java)
- [JavaPoet](https://github.com/square/javapoet)
- [Swagger CodeGen](https://github.com/swagger-api/swagger-codegen)
- [MyBatis Generator](https://github.com/mybatis/generator)

The advantage of our current approach is it is pluggable to some degree.

A disadvantage is that there is some increased complexity -- to really understand and work with the StackGen source code, you will need to grok the Swagger Codegen project, the MyBatis Generator, and the Starter code which uses JavaPoet to build up Java classes programmatically.

In the future we may decide to replace technology and settle on a single code generation paradigm, or perhaps a hybrid approach with Mustache + JavaPoet.
