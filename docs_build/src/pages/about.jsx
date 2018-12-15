import React, { Component } from "react";
import Helmet from "react-helmet";
import About from "../components/About/About";
import config from "../../data/SiteConfig";

class AboutPage extends Component {
  render() {
    return (

      <Drawer className="home-template" isOpen={this.state.menuOpen}>
      <Helmet title={`About | ${config.siteTitle}`} />

        <SEO postEdges={nodes} />

        {/* The blog navigation links */}
        <Navigation config={config} onClose={this.handleOnClose} />

        <SiteWrapper>
          {/* All the main content gets inserted here */}
          <div className="home-template">
            {/* The big featured header */}
            <MainHeader cover={config.siteCover}>
              <MainNav overlay={config.siteCover}>
                <BlogLogo logo={config.siteLogo} title={config.siteTitle} />
                <MenuButton
                  navigation={config.siteNavigation}
                  onClick={this.handleOnClick}
                />
              </MainNav>
              <div className="vertical">
                <div className="main-header-content inner">
                  <PageLogo/>
                  <PageTitle text={config.siteTitle} />
                  <PageDescription text={config.siteDescription} />
                  <SocialMediaIcons
                    urls={config.siteSocialUrls}
                    color="currentColor"
                  />
                </div>
              </div>
              <Link
                className="scroll-down icon-arrow-left"
                to="content"
                data-offset="-45"
                spy
                smooth
                duration={500}
              >
                <span className="hidden">Scroll Down</span>
              </Link>
            </MainHeader>

            <About />
          </div>

          {/* The tiny footer at the very bottom */}
          <Footer
            copyright={config.copyright}
            promoteGatsby={config.promoteGatsby}
          />
        </SiteWrapper>
      </Drawer>
    );
  }
}

export default AboutPage;
