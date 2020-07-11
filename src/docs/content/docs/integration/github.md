---
title: "Integrating StackGen codegen with Github"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA17474-640x350.jpg"
date: "2020-06-27"
author: "johnmcmahon"
needsAuth: false
url: /wtx
slug: "integrating-github"
category: "tech"
tags:
    - codegen
    - tutorials
    - hands-on guides
    - 3rd party integrations
---

#### Introduction

The most common way to integrate StackGen CodeGen with app development is via GitHub.

These days, we refer to DevOps practices that center on GitHub as "GitOps" -- and by putting GitHub at the center of your development pipeline, you can leverage the many benefits of that platform and reframe the methodology of development as one that centers on a single source of truth for code, bringing the Agile discipline of change tracking and code review applied to as much of the stack as possible (including docs, build scripts, and even website copy and design.)

With this focus on git as the center hub of a development lifecycle wheel, then StackGen takes it's place in the cycle by using GitHub as the Source of Truth for code templates and artifacts, automated docs, build scripts, and even project management.

- Create and initialize a complete GitHub Repo containing generated project
- Create SprintBoot Back End App
- Create React Front End App
- Create GitHub actions build script and triggers to build and test it all
- Create GitHub pages marketing and docs Gatsby site
- Create GitHub project and issues for post-generation steps
- Create GitHub team and notifications for CodeGen events