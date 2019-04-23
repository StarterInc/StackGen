const clay = require('clay-css');
const path = require('path');

module.exports = {
	plugins: [
		'gatsby-plugin-meta-redirect',
		'gatsby-transformer-try-examples',
		{
			resolve: 'gatsby-plugin-sass',
			options: {
				precision: 8,
				includePaths: clay
					.includePaths
					.concat(
						path.join(
							clay.includePaths[0],
							'node_modules'
						)
					),
			},
		},
		{
			resolve: 'gatsby-source-filesystem',
			options: {
				name: 'packages',
				path: `${__dirname}/content`,
			},
		},
		{
			resolve: 'gatsby-mdx',
			options: {
				extensions: ['.mdx', '.md'],
				gatsbyRemarkPlugins: [
					{
						resolve: path.resolve(__dirname, './plugins/gatsby-remark-code-label-extractor'),
					},
					{
						resolve: path.resolve(__dirname, './plugins/gatsby-remark-foreach-icons'),
					},
					{
						resolve: 'gatsby-remark-prismjs',
						pluginOptions: {
							classPrefix: 'gatsby-code-',
						},
					},
					{
						resolve: path.resolve(__dirname, './plugins/gatsby-remark-use-clipboard'),
					},
					{
						resolve: path.resolve(__dirname, './plugins/gatsby-remark-api-table'),
					},
				],
			},
		},
		{
			resolve: 'gatsby-plugin-google-analytics',
			options: {
				trackingId: process.env.GA_TRACKING_ID,
			},
		},
		'gatsby-plugin-react-helmet',
		{
			resolve: 'gatsby-plugin-manifest',
			options: {
				name: 'StackGen',
				short_name: 'StackGen',
				start_url: '/',
				background_color: '#EFEFEF',
				theme_color: '#222222',
				display: 'minimal-ui',
				icons: [
					{
						"src": "/favicons/favicon-16x16.png",
						"sizes": "16x16",
						"type": "image/png"
					},
					{
						"src": "/favicons/favicon-32x32.png",
						"sizes": "32x32",
						"type": "image/png"
					}
				]
			},
		},
		{
			resolve: 'gatsby-plugin-offline',
			options: {
				globPatterns: ['**/*.{js,jpg,png,gif,html,css,svg}'],
			},
		},
		'gatsby-plugin-zopfli'
	],
};
