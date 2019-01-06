import React, { Component } from "react";
import "./About.css";

class About extends Component {
  render() {
    return (


      <div className="about">
        <section className="about">

          <h3>
            About Starter Ignite
          </h3>

          <blockquote>To unlock the potential of builders so that they can focus on changing the world for the better.</blockquote>

            Starter Ignite is a code generator that creates a complete application
            stack using an OpenAPI schema definition.
            <br/>
            We created Ignite after many years of experience with hand-coding web and
            mobile applications.
            <br/>
            <br/>
			      Adopting conventions is useful no matter the language or framework, so
            <br/>
            <br/>
            Also, there are not a lot of choices that give you the same flexibility
            and compatibility as a JEE stack based on industry standard libraries such
            as Spring Boot.
          <br/>
          <br/>
            For many projects, the implementation of database operations, building the
            data object and Object Relational Mapping, and RESTful APIs represent a
            huge percentage of manual coding and maintenance effort.
        </section>
      </div>
    );
  }
}

export default About;
