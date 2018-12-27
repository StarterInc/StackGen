module.exports = {
  blogPostDir: "posts", // The name of directory that contains your posts.
  blogAuthorDir: "authors", // The name of directory that contains your 'authors' folder.
  blogAuthorId: "johnmcmahon", // The default and fallback author ID used for blog posts without a defined author.
  siteTitle: "Ignite", // Site title.
  siteTitleAlt: "Starter Ignite Code Generation Platform", // Alternative site title for SEO.
  siteLogo:
    "https://github.com/StarterInc/Ignite/blob/gh-pages/logos/logo-512x512.png?raw=true", // Logo used for SEO and manifest. e.g. "/logos/logo-1024.png",
  siteUrl: "https://starterinc.github.io", // Domain of your website without pathPrefix.
  pathPrefix: "/Ignite", // Prefixes all links. For cases when deployed to example.github.io/gatsby-starter-casper/.
  siteDescription:
    "Starter Ignite Documentation Site", // Website description used for RSS feeds/meta description tag.
  siteCover:
    "https://www.jpl.nasa.gov/spaceimages/images/wallpaper/PIA19964-640x350.jpg", // Optional, the cover image used in header for home page. e.g: "/images/blog-cover.jpg",
  siteNavigation: true, // If navigation is enabled the Menu button will be visible
  siteRss: "/rss.xml", // Path to the RSS file.
  siteRssAuthor: "John McMahon", // The author name used in the RSS file
  // siteFBAppID: "1825356251115265", // optional, sets the FB Application ID for using app insights
  sitePaginationLimit: 10, // The max number of posts per page.
  googleAnalyticsID: "UA-x-1", // GA tracking ID.
  // disqusShortname: "https-vagr9k-github-io-gatsby-advanced-starter", // enables Disqus comments, visually deviates from original Casper theme.
  siteSocialUrls: [
    "https://github.com/StarterInc/Ignite",
    "https://twitter.com/TechnoCharms",
    "mailto:info@starter.io"
  ],
  postDefaultCategoryID: "Tech", // Default category for posts.
  // Links to social profiles/projects you want to display in the navigation bar.
  userLinks: [
    {
      label: "GitHub",
      url: "https://github.com/StarterInc/Ignite",
      iconClassName: "fa fa-github" // Disabled, see Navigation.jsx
    },
    {
      label: "Twitter",
      url: "https://twitter.com/TechnoCharms",
      iconClassName: "fa fa-twitter" // Disabled, see Navigation.jsx
    },
    {
      label: "Swagger Editor",
      url: "openapi-gui/index.html",
      iconClassName: "fa fa-close" // Disabled, see Navigation.jsx
    },
    {
      label: "Email",
      url: "mailto:info@starter.io",
      iconClassName: "fa fa-envelope" // Disabled, see Navigation.jsx
    }
  ],
  // Copyright string for the footer of the website and RSS feed.
  copyright: {
    label: "Starter Ignite", // Label used before the year
    year: "2019" // optional, set specific copyright year or range of years, defaults to current year
    // url: "https://www.gatsbyjs.org/" // optional, set link address of copyright, defaults to site root
  },
  themeColor: "#ff9900", //  "#c62828", // Used for setting manifest and progress theme colors.
  backgroundColor: "#e0e0e0", // Used for setting manifest background color.
  promoteGatsby: true // Enables the GatsbyJS promotion information in footer.
};
