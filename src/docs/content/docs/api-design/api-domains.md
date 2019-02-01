---
title: "Mastering API Domains and Achieving Max Interop"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA18322-640x350.jpg"
slug: "advanced-topics"
author: "johnmcmahon"
needsAuth: false
url: /wth
category: "tech"
date: "2019-01-14"
tags:
    - docs
    - architecture
    - best practices
    - resources
---

### What are "Domains"

One of the mandates of StackGen is **Max Interop**

> **Max Interop**: to enable and encourage interoperability with as many external systems as possible.

Because of this requirement, StackGen is designed for extensibility and enhancement in every practical way.

The following non-exhaustive list of ways  that you can extend StackGen or integrate it into existing stacks:

<blockquote>

- Generate standalone Microservices to provide specific functionality for web and mobile apps

- Generate Java Data Objects and matching MySQL Database for a desktop or server based applications

- **PRO** Generate and launch a cluster of inter-related Microservices as well as a Management Console to

- Build a custom React component that is bound to a generated REST endpoint and embed in existing applications

- **PRO** Generate, deploy, and launch a complete full-stack ReactJS/React Native app with a clustered fault tolerant AWS Cloud Spring Boot backend, replicated, fault tolerant RDS/Aurora Database and encrypted SecureField encryption to protect customer private information

</blockquote>

> Logic and Data Domains can be "Merged" into large APIs

**Domains** in StackGen terminology refers to the scope of a functional area or field. In the case of Plugins and extending the capabilities of your APIs with existing Schemas, Data Models, and Logic, a **Domain** means the Plugin and the area of concern that the Plugin provides functionality for.

For example: the "eCommerce" domain includes all functionality related to purchasing items and services online, payments, shopping cart, customer self-service and logic for various workflows: request order refund, returns, downloads and unlocks, etc.

So to build a full eCommerce app, you would need the plugin for the "eCommerce" domain, which would in turn install any missing required base plugins such as:

- "StarterStackGen" : User and ACL, Groups, Preferences
- "StarterConnect" : email, SMS and feeds/subs, GitHub
- "StarterSocialConnect" : Facebook, Twitter, Slack, BitBucket (optional)
- "StarterWorkflows" : Coming Soon (optional)

Generated apps are built ready to deploy on all major cloud platforms, production servers, development and testing environments without modification.

### How to Develop Merged Specification APIs

> TODO: writeup

### How to Reconcile Collisions and Side Effects

> TODO: writeup

> Regenerate Early, Regenerate Often
