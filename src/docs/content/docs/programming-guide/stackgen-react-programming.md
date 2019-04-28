---
title: "StackGen PRO React Development"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA18322-640x350.jpg"
slug: "react-stackgen"
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
### Programming with StackGen

> Before diving into StackGen React development be sure to (/docs/getting-started/getting-started.html)[setup your environment

### Modifying the StackGen Generated React User Interface

The StackGen PRO React code generation is intended to save UI/UX developers time and effort by producing a large percentage of the "boilerplate" code required to connect a React app to the StackGen Microservices.

Similar to the React (server side) code generation, the React (client side) code generation maps directly to the Swagger definition that you are using to define the code.

The generated React code for a single domain object such as the "User" object consists of a number of React and Redux ReactScript source files.

/src/actions

/src/components

/src/actions


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

Then this Swagger definition will generate a StackGen project which contains the **"UserService.React"** data model service class.

```React
package io.starter.stackgen.model;
...
/**
 * Starter Ignite 'ReactGen' Generated Class:
 * Apr/13/2019 10:40:29 -0700 */
@Service("userApiDelegate")
public class UserService implements UserApiDelegate {
  public User UserBean = new User();

```

This UserService class is intended to be the "extension point" for React developers to access the data model and extend the functionality via server-side React.


> NOTE: the paths and package names are configurable, example code will refer to the default values

```React
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

Adding functionality and aggregating database operations are straightforward Plain Old React inheritance.
