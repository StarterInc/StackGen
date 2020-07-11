---
title: "Integrating Stripe for Payments"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA17474-640x350.jpg"
date: "2020-06-27"
author: "johnmcmahon"
needsAuth: false
url: /wtx
slug: "integrating-stripe-payments"
category: "tech"
tags:
    - ecommerce
    - tutorials
    - hands-on guides
    - 3rd party integrations
---

## Introduction

StackGen apps are often used to build ecommerce products -- which require a payment gateway and other means by which to accept payments online.

One of the popular choices is STRIPE (www.stripe.com) which has some advantages when used by StackGen:

- Stripe accepts a wide variety of payment options
- provides a great api experience for developers
- plays well with Java and React: 
https://stripe.com/docs/stripe-js/react

In terms of pricing, the various options we considered cost about the same.  For example Stripe cost the same per transaction as both Square and PayPal.

#### Integrating STRIPE with SpringBoot StackGen Backend

Since we are dealing with Spring and a well tested Java library from Stripe, we can use Maven to do the heavy lifting and just start coding the Java eCommerce entry points right in our StackGen app project.

```bash
    <dependency>
        <groupId>com.stripe</groupId>
        <artifactId>stripe-java</artifactId>
        <version>19.27.0</version>
    </dependency>
```

Create a Java file in your project which will be a Spring @Controller that will manage the payment process:

```bash
- src
    - main
        - java
            - yourpackage
                > CheckoutController.java
```                

For more information and to try out our SpringBoot integration process, check out this article:

https://www.baeldung.com/java-stripe-api