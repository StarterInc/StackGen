import React, { Component } from "react";
import { Link } from "gatsby";
import "./ApiCloudLogo.css";

class ApiCloudLogo extends Component {
  render() {
    const { apicloud_logo, apicloud_url, apicloud_title } = this.props;
    if (logo) {
      return (
        <Link className="apicloud-logo" to={url || "/"}>
          {/* style={{ boxShadow: "none" }}> */}
          <img src={apicloud_logo} alt={apicloud_title} />
        </Link>
      );
    }
    return null;
  }
}

export default ApiCloudLogo;
