---
title: "Field-Level Encryption"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA11777-640x350.jpg"
slug: "securefield"
needsAuth: false
author: "johnmcmahon"
url: /wth
category: "tech"
date: "2018-12-10"
tags:
    - programming
    - security
    - encryption
    - database
---

### What is SecureField&trade; Technology?

SecureField&trade; is a field-level encryption capability providing an extra layer of data protection for your customer PII (Personally Identifiable Information) data, and any other important, personal, private information which must not fall into unauthorized hands.

When combined with secure web application practices, database encryption, and end-to-end SSL, SecureField&trade; technology is another layer of protection for critical content.

SecureField&trade; is a Java technology from Starter Inc. which allows you to define automatic encryption and decryption for a data field by using a Java annotation in your code.

The encryption algorithm is implemented with Java JCE using a SHA256 key and the following Cipher implementation:

```Java
public static String	CIPHER_NAME	= "AES/CBC/PKCS5PADDING";
```

For the full source code please see:
[SecureEncrypter](https://github.com/StarterInc/StackGen/blob/master/src/main/java/io/starter/ignite/security/crypto/SecureEncrypter.java)

When compiling your project, StackGen uses [Aspect Oriented Technology](https://en.wikipedia.org/wiki/Aspect-oriented_programming) to scan your codebase for [@SecureField](https://github.com/StarterInc/StackGen/blob/master/src/main/java/io/starter/ignite/security/securefield/SecureField.java) annotations -- and when it finds them, wraps all field access in automatic encryption and decryption capability.

#### Advantages of using @SecureField encryption:

- Enabling secure encryption for a field on a Java Object is as simple as adding the @SecureField annotation to the field
- Java classes do not need to have any encryption or security awareness or methods
- Any POJO class can use @SecureField and there is no need to implement methods, interfaces or subclasses
- Encryption and decryption are transparent to your client code -- automatically encrypting the field value when it is set, and decrypting when the field value is accessed
- SecureField&trade; values are encrypted in memory and only decrypted upon access by valid calling code -- hackers leveraging stack dumps and memory inspection will see only ciphertext values

#### Disadvantages to using @SecureField encryption
- encrypted data fields cannot be searched in databases
- there is a small performance cost to each encryption/decryption call (~1-3 milliseconds typically)
- caching cleartext in any way can defeat your encryption security or create a vulnerability
- encrypted data can ONLY be recovered with the same key that it was encrypted with
- applying the encryption aspects adds an additional finicky step to the StackGen build process

> ### WARNING!!
> Encrypted data can ONLY be recovered with the same key that it was encrypted with. Do NOT lose your encryption key!

### Applying @SecureField to your Java

```Java

import io.starter.ignite.secure.SecureField;

...

// StackGen Annotations
  @io.starter.ignite.security.securefield.SecureField(enabled=true)
  protected String accountNumber = null;
```

### Accessing the Ciphertext Value in Code

In the rare case you might need to access ciphertext of a SecureField&trade; at runtime, you can easily use Java Reflection to access the ciphertext values without triggering decryption by the Aspect.

```Java

import io.starter.ignite.secure.SecureField;

...

@SecureField()
private String ssn = ""; // social security number

```

Notes on IDE Support for AspectJ:

If you are using Eclipse, you should install the AspectJ Eclipse Plugin.

You will need to add the update site:

```
AJDT site - http://download.eclipse.org/tools/ajdt/410/dev/update
```

And select:

```
AspectJ Development Tools
```

Once you have installed AspectJ, you will need to add the StackGen.jar to your Aspect Path.

You can then open the "Cross References" View to see all of the places where the Aspects are applied as well as step through the Aspect calls in debugger. 

[./screenshots/AspectJ-Eclipse-User-Cross-References.png]