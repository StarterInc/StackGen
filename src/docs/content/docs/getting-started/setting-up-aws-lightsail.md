---
title: "Setting Up Amazon LightSail"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA17474-640x350.jpg"
date: "2018-03-02"
author: "johnmcmahon"
needsAuth: false
url: /setting-up-aws-lightsail
slug: "setting-up-aws-lightsail"
category: "deployment"
tags:
    - deployment
    - cloud
    - aws
    - tutorials
    - hands-on guides
---

#### Introduction

Amazon LightSail is a great option to build and run Microservice APIs and Apps built with StackGen

The ability to quickly launch a reliable, standard Linux server means you can have your StackGen API up and running
quickly.

This guide assumes you have already built and tested a StackGen generated project and have the "name-exec-1.0.0.jar" executable jar ready to upload.


#### Step 1: Login to Amazon LightSail

Click here:

[Amazon LightSail Dashboard](https://lightsail.aws.amazon.com/ls/webapp/home/instances)

```

```

> These settings are converted to System properties within the StackGen program and are used to configure the output from the program.

```

```

#### Step 1: Update the Amazon Linux version of OpenJDK (Java)


**Amazon Linux**
To remove java 1.7 and install OpenJDK 1.8.0:

```
sudo yum install java-1.8.0-openjdk-devel

sudo yum remove java-1.7.0-openjdk

sudo /usr/sbin/alternatives --config java

sudo /usr/sbin/alternatives --config javac

```

#### Step Upload jar to Server

Download the key to connect to your LightSail instance from the AWS website and save it in the ".ssh" folder in your home directory on your local computer.

1. Use SSH and login to the instance:

```

```

2. Create the StackGen home directory on the LightSail instance:

```

```

3. Using "scp" command and the path to the key file you will be able to push the jar to the server.

```

```


#### Resources

**About Amazon AWS LightSail**
