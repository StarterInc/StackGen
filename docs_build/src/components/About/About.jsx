import React, { Component } from "react";
import "./About.css";

class About extends Component {
  render() {
    return (


      <div className="about">
        <section className="about">

          <h1>
            About Starter Ignite
          </h1>
            Starter Ignite is a code generator that creates a complete application
            stack using an OpenAPI schema definition.

            We created Ignite after many years of experience with hand-coding web and
            mobile applications. App development requires coding a ton of "plumbing" code --
            code that is repetitive "boilerplate" and which is error prone and expensive
            to maintain and

            App frameworks such as RoR allow for a "coding by convention" approach to
            rapidly building apps, however the choice of framework can be a limitation
            when it inevitable enhancements are required.

			Adopting conventions is useful no matter the language or framework, so 
            Also, there are not a lot of choices that give you the same flexibility
            and compatibility as a JEE stack based on industry standard libraries such
            as Spring Boot.

            For many projects, the implementation of database operations, building the
            data object and Object Relational Mapping, and RESTful APIs represent a
            huge percentage of manual coding and maintenance effort.

            Furthermore, Ignite supports an agile business with a platform that encourages
            change without requiring major refactoring or rewrites, and where changes
            and enhancements have a minimal impact on production systems.
        </section>
      </div>
    );
  }
}

export default About;
