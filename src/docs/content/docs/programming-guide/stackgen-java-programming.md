---
title: "StackGen Java Development"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA18322-640x350.jpg"
slug: "java-stackgen"
author: "johnmcmahon"
needsAuth: false
url: /wth
category: "tech"
date: "2019-03-17"
tags:
    - programming
    - opensource
    - resources
---
### Programming Java Apps with StackGen PRO

> Before diving into StackGen Java development be sure to [/docs/getting-started/getting-started.html](setup your environment)

### Using StackGen Generated Data Objects in Java

One of the more useful areas of StackGen is the developer-friendly database persistence code that is generated and accessible directly from your Java code.

Extending the StackGen generated output directly is doable, but it is recommended to subclass the generated data model "Service" class in most cases.

The data model "Service" classes are predictably named "xxxService".

For example, if your Swagger input file contained a data model definition for "User:"

```Swagger
...
User:
    required:
    - username
    - password
    properties:
      governmentId:
        description: a 10 digit government ID (encrypted)
        type: string
        example: 1112233334
        maxLength: 10
        minLength: 10
        x-starter-secureField: true
      userName:
        type: string
        example: Sparky
      password:
        type: string
        example: HardToGuess1980
        minLength: 10
...
```

Then this Swagger definition will generate a StackGen project which contains the **"UserService.Java"** data model service class.

```Java
package io.starter.stackgen.model;
...
/**
 * Starter Ignite 'JavaGen' Generated Class:
 * Apr/13/2019 10:40:29 -0700 */
@Service("userApiDelegate")
public class UserService implements UserApiDelegate {
  public User UserBean = new User();

```

This UserService class is intended to be the "extension point" for Java developers to access the data model and extend the functionality via server-side Java.


> NOTE: the paths and package names are configurable, example code will refer to the default values

```Java
// loading a User from the database is a few lines of code
UserService u = new UserService();
u.setId(100l); // load the user with ID "100"
u.load();

// you can also get a handle to the MyBatis "Select by Example"
// functionality to create advanced selection criteria
ContactMethodService cmx = new ContactMethodService();
cmx.getSelectByExample().createCriteria().andOwnerIdEqualTo(100l);
cmx.load();
```

At this point in the code, you can handle most data operations without touching a line of SQL or any direct JDBC calls.

Adding functionality and aggregating database operations are straightforward Plain Old Java inheritance.
