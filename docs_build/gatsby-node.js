const path = require("path");
const _ = require("lodash");
const fs = require("fs");
const siteConfig = require("./data/SiteConfig");

const Promise = require(`bluebird`)
const slash = require(`slash`)

const {
  createPaginationPages,
  createLinkedPages
} = require("gatsby-pagination");

exports.onCreateNode = ({ node, actions, getNode }) => {
  const { createNodeField } = actions;
  let slug;
  if (node.internal.type === "MarkdownRemark") {
    const fileNode = getNode(node.parent);
    const parsedFilePath = path.parse(fileNode.relativePath);
    if (
      Object.prototype.hasOwnProperty.call(node, "frontmatter") &&
      Object.prototype.hasOwnProperty.call(node.frontmatter, "slug")
    ) {
      slug = `/${_.kebabCase(node.frontmatter.slug)}`;
    } else if (
      Object.prototype.hasOwnProperty.call(node, "frontmatter") &&
      Object.prototype.hasOwnProperty.call(node.frontmatter, "title")
    ) {
      slug = `/${_.kebabCase(node.frontmatter.title)}`;
    } else if (parsedFilePath.name !== "index" && parsedFilePath.dir !== "") {
      slug = `/${parsedFilePath.dir}/${parsedFilePath.name}/`;
    } else if (parsedFilePath.dir === "") {
      slug = `/${parsedFilePath.name}/`;
    } else {
      slug = `/${parsedFilePath.dir}/`;
    }
    createNodeField({ node, name: "slug", value: slug });
  }
};

// Implement the Gatsby API “createPages”. This is
// called after the Gatsby bootstrap is finished so you have
// access to any information necessary to programmatically
// create pages.
// Will create pages for WordPress pages (route : /{slug})
// Will create pages for WordPress posts (route : /post/{slug})
exports.createPagesWP = ({ graphql, actions }) => {
  const { createPage } = actions
  return new Promise((resolve, reject) => {
    // The “graphql” function allows us to run arbitrary
    // queries against the local WordPress graphql schema. Think of
    // it like the site has a built-in database constructed
    // from the fetched data that you can run queries against.

    // ==== PAGES (WORDPRESS NATIVE) ====
    graphql(
      `
        {
          allWordpressPage {
            edges {
              node {
                id
                slug
                status
                template
              }
            }
          }
        }
      `
    )
      .then(result => {
        if (result.errors) {
          console.log(result.errors)
          reject(result.errors)
        }

        // Create Page pages.
        const pageTemplate = path.resolve("./src/templates/post.jsx")
        // We want to create a detailed page for each
        // page node. We'll just use the WordPress Slug for the slug.
        // The Page ID is prefixed with 'PAGE_'
        _.each(result.data.allWordpressPage.edges, edge => {
          // Gatsby uses Redux to manage its internal state.
          // Plugins and sites can use functions like "createPage"
          // to interact with Gatsby.
          createPage({
            // Each page is required to have a `path` as well
            // as a template component. The `context` is
            // optional but is often necessary so the template
            // can query data specific to each page.
            path: `/${edge.node.slug}/`,
            component: slash(pageTemplate),
            context: {
              id: edge.node.id,
            },
          })
        })
      })
      // ==== END PAGES ====

      // ==== POSTS (WORDPRESS NATIVE AND ACF) ====
      .then(() => {
        graphql(
          `
            {
              allWordpressPost {
                edges {
                  node {
                    id
                    slug
                    status
                    template
                    format
                  }
                }
              }
            }
          `
        ).then(result => {
          if (result.errors) {
            console.log(result.errors)
            reject(result.errors)
          }
          const postTemplate = path.resolve("./src/templates/post.js")
          // We want to create a detailed page for each
          // post node. We'll just use the WordPress Slug for the slug.
          // The Post ID is prefixed with 'POST_'
          _.each(result.data.allWordpressPost.edges, edge => {
            createPage({
              path: `/${edge.node.slug}/`,
              component: slash(postTemplate),
              context: {
                id: edge.node.id,
              },
            })
          })
          resolve()
        })
      })
    // ==== END POSTS ====
  })
};

exports.createPages = ({ graphql, actions }) => {
  const { createPage } = actions;

  return new Promise((resolve, reject) => {
    const indexPage = path.resolve("src/templates/index.jsx");
    const postPage = path.resolve("src/templates/post.jsx");
    const tagPage = path.resolve("src/templates/tag.jsx");
    const categoryPage = path.resolve("src/templates/category.jsx");
    const authorPage = path.resolve("src/templates/author.jsx");

    if (
      !fs.existsSync(
        path.resolve(`content/${siteConfig.blogAuthorDir}/authors/`)
      )
    ) {
      reject(
        "The 'authors' folder is missing within the 'blogAuthorDir' folder."
      );
    }

    resolve(
      graphql(
        `
          {
            allMarkdownRemark(
              limit: 1000
              sort: { fields: [frontmatter___date], order: DESC }
            ) {
              totalCount
              edges {
                node {
                  frontmatter {
                    title
                    tags
                    cover
                    date
                    category
                    author
                  }
                  fields {
                    slug
                  }
                  excerpt
                  timeToRead
                }
              }
            }
          }
        `
      ).then(result => {
        if (result.errors) {
          /* eslint no-console: "off" */
          console.log(result.errors);
          reject(result.errors);
        }

        // Creates Index page
        createPaginationPages({
          createPage,
          edges: result.data.allMarkdownRemark.edges,
          component: indexPage,
          limit: siteConfig.sitePaginationLimit
        });

        // Creates Posts
        createLinkedPages({
          createPage,
          edges: result.data.allMarkdownRemark.edges,
          component: postPage,
          edgeParser: edge => ({
            path: edge.node.fields.slug,
            context: {
              slug: edge.node.fields.slug
            }
          }),
          circular: true
        });

        const tagSet = new Set();
        const tagMap = new Map();
        const categorySet = new Set();
        const authorSet = new Set();
        authorSet.add(siteConfig.blogAuthorId);

        result.data.allMarkdownRemark.edges.forEach(edge => {
          if (edge.node.frontmatter.tags) {
            edge.node.frontmatter.tags.forEach(tag => {
              tagSet.add(tag);

              const array = tagMap.has(tag) ? tagMap.get(tag) : [];
              array.push(edge);
              tagMap.set(tag, array);
            });
          }

          if (edge.node.frontmatter.category) {
            categorySet.add(edge.node.frontmatter.category);
          }

          if (edge.node.frontmatter.author) {
            authorSet.add(edge.node.frontmatter.author);
          }
        });

        const tagFormatter = tag => route =>
          `/tags/${_.kebabCase(tag)}/${route !== 1 ? route : ""}`;
        const tagList = Array.from(tagSet);
        tagList.forEach(tag => {
          // Creates tag pages
          createPaginationPages({
            createPage,
            edges: tagMap.get(tag),
            component: tagPage,
            pathFormatter: tagFormatter(tag),
            limit: siteConfig.sitePaginationLimit,
            context: {
              tag
            }
          });
        });

        const categoryList = Array.from(categorySet);
        categoryList.forEach(category => {
          createPage({
            path: `/categories/${_.kebabCase(category)}/`,
            component: categoryPage,
            context: {
              category
            }
          });
        });

        const authorList = Array.from(authorSet);
        authorList.forEach(author => {
          createPage({
            path: `/author/${_.kebabCase(author)}/`,
            component: authorPage,
            context: {
              author
            }
          });
        });
      })
    );
  });
};
