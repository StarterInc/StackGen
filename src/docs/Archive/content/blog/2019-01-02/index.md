---
title: "Don't Refactor, Regenerate!"
banner: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA18322-640x350.jpg"
slug: "dont-refactor-regenerate"
author: "johnmcmahon"
needsAuth: false
category: "tech"
date: "2019-01-02"
tags:
    - docs
    - codegen
    - business case
---

### Build Early, Build Often

Code generation frameworks allow for a much needed reboot of the trusty Agile methodology -- but only if you are opinionated about workflow and methodology.

Agile development is iterative and works best when there is minimal friction between requirements and the actual implementation.

In other words Agile is most effective in a mythical world where software development estimates are always accurate and features can be added predictably with minimal implementation expense (which can be thought of also as execution risk.)

Code generation lets you stick a finger in the eye of unpredictability by sectioning off any and all code that can and should be generated methodically by robots, from the "value added" code.

> Value added code is the code encompassing the business logic and proprietary features of your product.

When it is possible to regenerate and rebuild an entire application with the addition of each feature, it is possible to achieve a rapid iterative dev cycle with high quality and more predictable time estimates.

Backed by extensive unit testing, functionality can be accreted without technical debt.

However this presents a classic situation where humans can be the bottleneck.  You cannot expect to regenerate large portions of your source code without also rigorously designing for the changes your roadmap dictates.

Flexible and sustainable architecture becomes even more critical when the goal is rapid yet stable accretion of features and enhancements.

Putting some of the time saved from Code Generation back into the design and requirements phase of the project, and crucially designing a killer OpenAPI specfication for all of your services from Day One.

Another area of opinionated focus should be in your organizational setup and especially in how you handle VCS (git) repos.

We are huge fans of "convention over configuration" and the beauty of frameworks... so essentially we preach the modular setup familiar to Maven modular builds, one repo per service which has become a standard.

Unlike awkward [MonoRepos](https://medium.com/@mattklein123/monorepos-please-dont-e9a279be011b), one-repo-per module projects are built in a modular way, encouraging smart reuse of code and safe pluggable implementations that can be upgraded without fuss.

Yes, dependency hell can certainly still apply but at least when you are dealing with Maven you have the ability to micromanage to whatever degree you require.

Because of this, code management is predictable as source code for generated apps can be downloaded and modified and extended by client code, and stored separately in any VCS of choice.
