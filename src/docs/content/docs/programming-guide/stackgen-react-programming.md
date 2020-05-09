---
title: "StackGen PRO React Development"
cover: "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA18322-640x350.jpg"
slug: "react-stackgen"
author: "johnmcmahon"
needsAuth: false
url: /wth
category: "tech"
date: "2020-04-29"
tags:
    - programming
    - opensource
    - resources
---
### Programming with StackGen

> Before diving into StackGen React development be sure to (/docs/getting-started/getting-started.html)[setup your environment]

### Modifying the StackGen Generated React User Interface

The StackGen PRO React code generation is intended to save UI/UX developers time and effort by producing a large percentage of the "boilerplate" code required to connect a React app to the StackGen Microservices.

Similar to the Java/Spring (server side) code generation, the React (client side) code generation maps directly to the Swagger definition that you are using to define the code.

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

Then this Swagger definition will generate a StackGen project which contains generated forms and views for your components:

- REST api data loaded by Axios AJAX library
- React Redux (reducers, actions, store) for app data management
- Formik **"UserForm.jsx"** data entry forms using Yup for validations
- List and Table based view components

Here is an example of the "userName" generated code for the User object described in the above OpenAPI schema:

```React
...
 <Form.Group controlId="userName">
      <Form.Label>userName</Form.Label>
      <Field
      	id="userName"
      	name="userName"
      	type="text"
        placeholder="Sparky"
    	 className={`form-control ${
            touched.userName && errors.userName ? "is-invalid" : ""
          }`}
        />
        <ErrorMessage
          component="div"
          name="userName"
          className="invalid-feedback"
        />
    	<Form.Control.Feedback>
    		That userName entry looks good!
    	</Form.Control.Feedback>
    	<Form.Control.Feedback type="invalid">
        	{errors.userName}
        </Form.Control.Feedback>
		<Form.Control.Feedback type="invalid">
			Please enter a valid user userName i.e.: Sparky
		</Form.Control.Feedback>
  </Form.Group>
...

```

The generated React project is intended to be used in a number of ways:

- for the simplest of apps, the project can be used "as-is" providing a convenient interface for the REST apis

- for custom, but smaller projects, the project can be modified "in-place" and enhanced with additional React code. Care must be taken not to overwrite work during code regeneration

- for larger projects, StackGen react code can be output directly into an existing project folder and used inline to a more complex app that uses the forms and components directly from other components in the project

- finally for the most sophisticated use case, the generated React output can be compiled and published as an npm library and included in projects using ``npm install``  During development, these projects can dynamically link working output  ``npm link``


Various features of the StackGen React generated output can be customized using custom input templates, and by selecting different css (Bootstrap) themes and custom libraries.