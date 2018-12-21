---
title: "Auto-Generated Ignite Fields"
cover: "images/mission_street.jpeg"
author: "johnmcmahon"
slug: "ignite-auto-generated-fields"
date: "2018-12-17"
category: "Ignite"
tags:
    - ignite
    - architecture
    - configuration
---
### Built-in Auto Generated Fields

> Starter Ignite adds a set of built-in fields to every generated object and database.

This is a configurable setting however much of the functionality improvements require the existence of these field’s.

In the future we will add the ability to toggle these Fields on a per object basis for situations where additional overhead is not desired.

Currently the auto generated ignite fields are as follows:

Modified_date
Created_date
Owner_ID
ID
Key version

These fields are not available in the rest API by default however they will be returned in JSON payload.
