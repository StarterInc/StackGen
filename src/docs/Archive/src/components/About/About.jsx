import React, { Component } from "react";
import "./About.css";

class About extends Component {
  render() {
    return (


      <div className="about">
        <section className="about">

          <h3>
            About Starter StackGen
          </h3>

          <h2>Starter Inc. Mission Statement:</h2>
          <blockquote>To unlock the potential of builders so that they can focus on changing the world for the better.</blockquote>

            Starter StackGen is a code generator that creates a complete application
            stack using an OpenAPI schema definition.
            <br/>
            We created StackGen after many years of experience with hand-coding web and
            mobile applications.
            <br/>
            <br/>
			      Adopting conventions is useful no matter the language or framework, so
            <br/>
            <br/>
            Among the many choices of application stack components, there are few that provide the
            combination of flexibility, compatibility, security, capability, cloud and enterprise support
            as a JEE stack based on industry standard libraries such as Spring Boot and Apache Tomcat.
            <br/>
            <br/>
            On the front end, we are not alone in our decision to go with React applications.
            <br/>
            <br/>
            As of 2019, Node.js, ReactJS, ReactNative, and now React360 form a compelling suite of
            front end technologies. By generating web, mobile and VR apps using Node and React, Starter StackGen users
            publish feature rich, component-based apps on the vast majority of devices
            and reach the most users possible.
          <br/>
          <br/>
            Generating code is a huge win For many projects. In the real world, the implementation
            of database operations, building the data object models and Object Relational Mapping,
            then publishing as REST APIs represent a huge percentage of manual coding and maintenance effort.
            <br/>
            <br/>
            By somewhat alleviating the pain of buiding and maintaining manual coding chores,
            Starter StackGen helps to unlock your potential so you can change the world for the better.
        </section>
      </div>
    );
  }
}

export default About;
