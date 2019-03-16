/**
 * Welcome to your Workbox-powered service worker!
 *
 * You'll need to register this file in your web app and you should
 * disable HTTP caching for this file too.
 * See https://goo.gl/nhQhGp
 *
 * The rest of the code is auto-generated. Please don't update this file
 * directly; instead, make changes to your Workbox build configuration
 * and re-run your build process.
 * See https://goo.gl/2aRDsh
 */

importScripts("workbox-v3.6.3/workbox-sw.js");
workbox.setConfig({modulePathPrefix: "workbox-v3.6.3"});

workbox.core.setCacheNameDetails({prefix: "gatsby-plugin-offline"});

workbox.skipWaiting();
workbox.clientsClaim();

/**
 * The workboxSW.precacheAndRoute() method efficiently caches and responds to
 * requests for URLs in the manifest.
 * See https://goo.gl/S9QRab
 */
self.__precacheManifest = [
  {
    "url": "0-7162297fa70efc84b2e1.js"
  },
  {
    "url": "0-7c34fb889697121b78eb.js"
  },
  {
    "url": "1-724165bac4acabe0036c.js"
  },
  {
    "url": "404.html",
    "revision": "183a0702edff143a3cbb4b0782390de4"
  },
  {
    "url": "404/index.html",
    "revision": "398f03850e42d9c34be878e3463fa874"
  },
  {
    "url": "api_schema/apidocs.html",
    "revision": "500f2589ce59daf18a8313d3b60c5a02"
  },
  {
    "url": "api_schema/index.html",
    "revision": "fad6718545018562cc6f6af6004ce63f"
  },
  {
    "url": "app-2cf20ee8043d3497ac80.js"
  },
  {
    "url": "app-3b0c4799024117fbdfc0.js"
  },
  {
    "url": "app-48cf6b2ad16fc1a1fe11.js"
  },
  {
    "url": "app-492480ee260937cf8963.js"
  },
  {
    "url": "app-595c36642589e503ba5c.js"
  },
  {
    "url": "app-ad9c9fcedf708c958df6.js"
  },
  {
    "url": "app-f79b3374270089e42ef8.js"
  },
  {
    "url": "app.0d569b3a5b4ddf5367bb.css"
  },
  {
    "url": "blog/2017-03-17/index.html",
    "revision": "a4725e014503190fb5f1586a6b8c3204"
  },
  {
    "url": "blog/2018-12-08/index.html",
    "revision": "aa278194c5326e3c4ccfe295695ba382"
  },
  {
    "url": "blog/2018-12-14/index.html",
    "revision": "db8c401db53c1ef4fbcadaaed5705963"
  },
  {
    "url": "blog/2019-01-02/index.html",
    "revision": "b90739c30c8304e4d106ab64a5ef8fa6"
  },
  {
    "url": "blog/index.html",
    "revision": "819293fe0038d95ea9e379bc62bf6ce5"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-4-aa-7987-de-2354-b-2-e-272-b-22-d-71819-a-94-b-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-1fb195cba0e08b13e496.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-4-aa-7987-de-2354-b-2-e-272-b-22-d-71819-a-94-b-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-26442b1a93d4500e4866.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-4-aa-7987-de-2354-b-2-e-272-b-22-d-71819-a-94-b-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-6302d5d08001e2e90f3a.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-4-aa-7987-de-2354-b-2-e-272-b-22-d-71819-a-94-b-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-b00d782994632c73dcdf.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-4-aa-7987-de-2354-b-2-e-272-b-22-d-71819-a-94-b-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-bebd51ffa65a9aeae883.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-5-c-99519-e-43-d-7-bd-6-d-2771-e-717590-a-0-c-5-c-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-15c1c96a8508b5dd16e9.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-5-c-99519-e-43-d-7-bd-6-d-2771-e-717590-a-0-c-5-c-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-29fccb54442e893a61c0.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-5-c-99519-e-43-d-7-bd-6-d-2771-e-717590-a-0-c-5-c-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-315fdda3abaf35316f44.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-5-c-99519-e-43-d-7-bd-6-d-2771-e-717590-a-0-c-5-c-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-6a33cac95bb82f851b13.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-5-c-99519-e-43-d-7-bd-6-d-2771-e-717590-a-0-c-5-c-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-8773b20bbf948da9c6f4.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-5-c-99519-e-43-d-7-bd-6-d-2771-e-717590-a-0-c-5-c-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-f91728493ee169ca80d8.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-992-b-861-af-49-eeb-2-c-80-ebdfd-5-d-2-da-9-c-13-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-67ad09084a3e0dd8b99b.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-992-b-861-af-49-eeb-2-c-80-ebdfd-5-d-2-da-9-c-13-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-9f6c3877f849edd3e97a.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-992-b-861-af-49-eeb-2-c-80-ebdfd-5-d-2-da-9-c-13-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-d0394ac82e8dc084b97b.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-992-b-861-af-49-eeb-2-c-80-ebdfd-5-d-2-da-9-c-13-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-db12d9f7261f469733b1.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-992-b-861-af-49-eeb-2-c-80-ebdfd-5-d-2-da-9-c-13-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-deb18c4e5a4ed28f428e.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-992-b-861-af-49-eeb-2-c-80-ebdfd-5-d-2-da-9-c-13-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-ee0735d9c1af15138458.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-cc-5-ab-3250-c-2-e-67-fb-771106-aad-5-fccbfa-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-0b1cd9ff5a5518eb063e.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-cc-5-ab-3250-c-2-e-67-fb-771106-aad-5-fccbfa-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-0b830979867bcd2f18af.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-cc-5-ab-3250-c-2-e-67-fb-771106-aad-5-fccbfa-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-119f55c81f54cfb53379.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-cc-5-ab-3250-c-2-e-67-fb-771106-aad-5-fccbfa-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-612a6b210f14364361d2.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-cc-5-ab-3250-c-2-e-67-fb-771106-aad-5-fccbfa-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-8a6695f7c7e9e3a41dea.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-cc-5-ab-3250-c-2-e-67-fb-771106-aad-5-fccbfa-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-ab05376ec88badaa572e.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-cc-5-ab-3250-c-2-e-67-fb-771106-aad-5-fccbfa-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-c52a2303fa100c8cfb19.js"
  },
  {
    "url": "component---gatsby-cache-gatsby-mdx-mdx-wrappers-dir-cc-5-ab-3250-c-2-e-67-fb-771106-aad-5-fccbfa-scope-hash-3010-b-3-badc-54-a-9-dfa-4-a-4-c-80-e-419-a-41-b-2-js-ed12aec33c579449ce46.js"
  },
  {
    "url": "component---node-modules-gatsby-plugin-offline-app-shell-js-894adcd5a41bd22c3fd7.js"
  },
  {
    "url": "component---src-pages-404-js-3e6c3838429432187050.js"
  },
  {
    "url": "component---src-pages-404-js-d941eaa64fb1124c02be.js"
  },
  {
    "url": "component---src-pages-index-js-113302a26c220f5c7973.js"
  },
  {
    "url": "component---src-pages-index-js-35d989f42b5a1a7ef7b5.js"
  },
  {
    "url": "component---src-pages-index-js-369dd77f0555169d929b.js"
  },
  {
    "url": "component---src-pages-index-js-3907ccf739f45ed6b7b4.js"
  },
  {
    "url": "component---src-pages-index-js-7b8757d12933deb14456.js"
  },
  {
    "url": "docs/api-design/api-design.html",
    "revision": "219cd55987d9c4d45c0651594417d48c"
  },
  {
    "url": "docs/api-design/api-domains.html",
    "revision": "2beaa8f4b433c2a9cfdc224d8cef7b90"
  },
  {
    "url": "docs/api-design/index.html",
    "revision": "96b6a5cba19d3743e519bcdcf2038300"
  },
  {
    "url": "docs/architecture/architecture-diagram.html",
    "revision": "8210dc1800d268b0a46d9819651442d6"
  },
  {
    "url": "docs/architecture/auto-gen-fields.html",
    "revision": "589f8b68484e37869a03047f7a437da2"
  },
  {
    "url": "docs/architecture/index.html",
    "revision": "c49daef2964b93895fccb889d0bf2dec"
  },
  {
    "url": "docs/architecture/json-data-objects.html",
    "revision": "d67d895a61fe4965e369024bd00110a9"
  },
  {
    "url": "docs/databases/configuring-jdbc.html",
    "revision": "241a769ebbbc8c4d6d3f26647f4557f5"
  },
  {
    "url": "docs/databases/index.html",
    "revision": "ac02a79d26802cf88e8b8509d325ba5b"
  },
  {
    "url": "docs/faq.html",
    "revision": "d4e656017bab4127d05701b6f94dbdb9"
  },
  {
    "url": "docs/getting-started/getting-started.html",
    "revision": "2360a753af32323ac39f0400b0cf973d"
  },
  {
    "url": "docs/getting-started/index.html",
    "revision": "2a0fcc8fcba92aa31224666784366425"
  },
  {
    "url": "docs/getting-started/next-steps.html",
    "revision": "b787b33c2183b6cf567a1c197371df7e"
  },
  {
    "url": "docs/getting-started/setting-up-aws-lightsail.html",
    "revision": "99e63416b793302510c46cf96ba0dc17"
  },
  {
    "url": "docs/index.html",
    "revision": "294f2a2678afb08f35333ac00e682d21"
  },
  {
    "url": "docs/introducing.html",
    "revision": "abe726cd274478d6f0c0c4d5f4291340"
  },
  {
    "url": "docs/security/index.html",
    "revision": "c49daef2964b93895fccb889d0bf2dec"
  },
  {
    "url": "docs/security/secure-field.html",
    "revision": "93817eecfcd4c3fa57e2d23103c31d63"
  },
  {
    "url": "docs/security/security.html",
    "revision": "bc3fe3843f41050e75e8456ef81a55fd"
  },
  {
    "url": "docs/support/index.html",
    "revision": "c49daef2964b93895fccb889d0bf2dec"
  },
  {
    "url": "docs/support/submitting-an-issue.html",
    "revision": "e5b524c2b3c0d33ee78bde1be539c07b"
  },
  {
    "url": "docs/support/troubleshooting.html",
    "revision": "cc38c393396675e0c001a3491ccc4ad5"
  },
  {
    "url": "favicons/android-chrome-192x192.png",
    "revision": "3d3cbaae692cf6ff891b67f14b531d5f"
  },
  {
    "url": "favicons/android-chrome-512x512.png",
    "revision": "4573455f45700cd418a805f7469b4e4b"
  },
  {
    "url": "favicons/apple-touch-icon.png",
    "revision": "671c294fa0a0fe8b7fb78920ea822f6f"
  },
  {
    "url": "favicons/favicon-16x16.png",
    "revision": "3c1570ff32d1b034f990c54d83868bec"
  },
  {
    "url": "favicons/favicon-32x32.png",
    "revision": "671c294fa0a0fe8b7fb78920ea822f6f"
  },
  {
    "url": "favicons/mstile-150x150.png",
    "revision": "1880e70b06e11d5215e63164943b042e"
  },
  {
    "url": "favicons/safari-pinned-tab.svg",
    "revision": "5c372b99ae7d79bde040912d7b1423c2"
  },
  {
    "url": "idb-keyval-iife.min.js"
  },
  {
    "url": "images/bg_top_ignite.png",
    "revision": "09f6a322cf95dabe0bdf6b9fc5d67779"
  },
  {
    "url": "images/blog-cover.jpg",
    "revision": "0c7e5ead41992c743be536ba1c4c9ebe"
  },
  {
    "url": "images/blog-cover.png",
    "revision": "e590dc8a64437412b1eac62786011a23"
  },
  {
    "url": "images/CoinBot-Account-OpenAPI.png",
    "revision": "cea7822aef8bf4e4deae6a30aeb95ba5"
  },
  {
    "url": "images/CoinBot-Account-Swagger-Object.png",
    "revision": "b832fcbcc3f2c43b12a012467d1133a8"
  },
  {
    "url": "images/CoinBot-Account-Table.png",
    "revision": "168c203a60b03860e465a4d46d06a9d2"
  },
  {
    "url": "images/CoinBot-Swagger.png",
    "revision": "53c42ff564b4bd7b5f0c418b9738efaf"
  },
  {
    "url": "images/CoinBot-User-Table-SecureField-Encryption.png",
    "revision": "b9c3fa35bd36a750de007f6c7eed71dc"
  },
  {
    "url": "images/favicon-32x32.png",
    "revision": "067bea6a52135263ddf428ecfbfb3bee"
  },
  {
    "url": "images/forklift-truck-materials-handling-retro_MkIwxw8u.png",
    "revision": "bf706790bca510506eeb7335c92f077d"
  },
  {
    "url": "images/forklift-truck-materials-handling-retro.png",
    "revision": "bf706790bca510506eeb7335c92f077d"
  },
  {
    "url": "images/home/bg_white.svg",
    "revision": "8202c365f8e7c0c66e8659cf4128bb12"
  },
  {
    "url": "images/home/chevron-right.svg",
    "revision": "8217a1a51ec873eb5d3dc3c8df882e12"
  },
  {
    "url": "images/home/file-download.svg",
    "revision": "89ab14fe5c7d6d420f5152a4ec58dd6f"
  },
  {
    "url": "images/home/gatsby.svg",
    "revision": "7db9b0eabb2aed554e582eacd11c760b"
  },
  {
    "url": "images/home/GitHub-Mark-64px.svg",
    "revision": "0c9e7db5843df9e272a46cbd3edd541e"
  },
  {
    "url": "images/home/github.svg",
    "revision": "bfb4433f238438ab6653c9fb6b5c6630"
  },
  {
    "url": "images/home/lexicon_symbol.svg",
    "revision": "af04fbfe0f5d6d78ac878316cfde2191"
  },
  {
    "url": "images/home/library03.svg",
    "revision": "74cbacdf2dee585c92e8c50de17edf49"
  },
  {
    "url": "images/home/liferay_logo_black.svg",
    "revision": "3d49825c9e986aa248d65d8938c7b289"
  },
  {
    "url": "images/home/liferay_logo.svg",
    "revision": "bb8cb92775e09b2a64403eef4127d319"
  },
  {
    "url": "images/home/liferay-logo-full-color.svg",
    "revision": "477c64b74be4c9813085f1a9cffcd0b1"
  },
  {
    "url": "images/home/news.svg",
    "revision": "7394371beebb3e0d7317f331af713b33"
  },
  {
    "url": "images/home/participate02.svg",
    "revision": "be3c2b002dd665be7dd6a5dce116aba9"
  },
  {
    "url": "images/home/patternbg 2.svg",
    "revision": "4bb776dec4d86148e7e5fb7a987c359f"
  },
  {
    "url": "images/home/patternbg.svg",
    "revision": "ff9571b509158b13290443653b140c34"
  },
  {
    "url": "images/home/Slack_Mark_Monochrome_White.svg",
    "revision": "b0190cf80857806605374a7259b77750"
  },
  {
    "url": "images/home/tools.svg",
    "revision": "581eab1463a629446bb7012e3337a9b8"
  },
  {
    "url": "images/home/tools02.svg",
    "revision": "0ca8cba1e693b4077a7bda122b44fb59"
  },
  {
    "url": "images/home/twitter.svg",
    "revision": "ec1092805f4b8a49e407a1d0c3070ece"
  },
  {
    "url": "images/home/what.svg",
    "revision": "45c5c3204935536507e23ae6429ae789"
  },
  {
    "url": "images/home/why.svg",
    "revision": "28360c994b2bd2b6fba2247e0aafe9e7"
  },
  {
    "url": "images/icons/add-cell.svg",
    "revision": "14a5db1d421b926e55961fd13c901ea2"
  },
  {
    "url": "images/icons/add-column.svg",
    "revision": "37b2349292d696b1a305c4e0f5203a65"
  },
  {
    "url": "images/icons/add-role.svg",
    "revision": "0a5767e47411b333b8490b51b4fcfddd"
  },
  {
    "url": "images/icons/add-row.svg",
    "revision": "72bc46ec77e54027c0ce225647faa607"
  },
  {
    "url": "images/icons/adjust.svg",
    "revision": "3805e9296c23c2150e8c482631ca1469"
  },
  {
    "url": "images/icons/align-center.svg",
    "revision": "eea2df853ed4af375229bcabcc70d1ec"
  },
  {
    "url": "images/icons/align-image-center.svg",
    "revision": "6995542a106f0b4ef3c9b74fa3c09fcf"
  },
  {
    "url": "images/icons/align-image-left.svg",
    "revision": "e81c873d9ab3f9a9f1c67608882c6d13"
  },
  {
    "url": "images/icons/align-image-right.svg",
    "revision": "8042cbe1d1fc854e5825ebb2fe81a224"
  },
  {
    "url": "images/icons/align-justify.svg",
    "revision": "94dec4b0a89b68f352d455126a5de3e7"
  },
  {
    "url": "images/icons/align-left.svg",
    "revision": "4ff8ddf29e610be3532c4c0dbedc68ae"
  },
  {
    "url": "images/icons/align-right.svg",
    "revision": "eb609b43b197e6a83fc5f39a146a032f"
  },
  {
    "url": "images/icons/analytics.svg",
    "revision": "634c57d2c52f532d4077c7b61a522079"
  },
  {
    "url": "images/icons/angle-down.svg",
    "revision": "054f03a552c7a53c03bffea1b412c8d6"
  },
  {
    "url": "images/icons/angle-left.svg",
    "revision": "959997ceca757754cab55d3bb3598d33"
  },
  {
    "url": "images/icons/angle-right.svg",
    "revision": "f023389ef107426eeeff7e1209536e28"
  },
  {
    "url": "images/icons/angle-up.svg",
    "revision": "4708f3c380e3c997241960e777d74ef8"
  },
  {
    "url": "images/icons/announcement.svg",
    "revision": "9271a551f3f5a47426e68d93e35a5635"
  },
  {
    "url": "images/icons/api-lock.svg",
    "revision": "9689354dbc712863f85a7b36e4517f9b"
  },
  {
    "url": "images/icons/api-web.svg",
    "revision": "fba14e92e46296268b2d73ccb9fa819e"
  },
  {
    "url": "images/icons/archive.svg",
    "revision": "203790f2a78692afb337e2b82c2c0576"
  },
  {
    "url": "images/icons/arrow-up-full.svg",
    "revision": "d2ca9c2f8a380b48eb5cb8126edb2f9f"
  },
  {
    "url": "images/icons/asterisk.svg",
    "revision": "db2b9ddf40b978994776a17c5d3ea343"
  },
  {
    "url": "images/icons/audio.svg",
    "revision": "55637108b6479b0c30da2a930e370f78"
  },
  {
    "url": "images/icons/autosize.svg",
    "revision": "1aeec8733e8e0b63b6dbbb09c0b1f178"
  },
  {
    "url": "images/icons/bars.svg",
    "revision": "6f5f2cbe7dd5dc4bf8297e7f53e50211"
  },
  {
    "url": "images/icons/bell-off.svg",
    "revision": "b507340b5189cc6efcf8be79e5aa2312"
  },
  {
    "url": "images/icons/bell-on.svg",
    "revision": "0d6a1039c54ef68efc6b547afedfbb2b"
  },
  {
    "url": "images/icons/blogs.svg",
    "revision": "f923f224fbd065c1b9c15ef73e4d06ad"
  },
  {
    "url": "images/icons/bold.svg",
    "revision": "51346907efa7b845137db7901ca92f01"
  },
  {
    "url": "images/icons/bookmarks.svg",
    "revision": "43b9b14b6578e9535e0e76b9683a2a03"
  },
  {
    "url": "images/icons/box-container.svg",
    "revision": "75e82d35511ab5bc5c02a8f82866a61a"
  },
  {
    "url": "images/icons/breadcrumb.svg",
    "revision": "611f59e14616e3f2b6c73a2e8723b6e9"
  },
  {
    "url": "images/icons/calendar.svg",
    "revision": "8e6ed76bcd1a55a89bec4b783c67b8e8"
  },
  {
    "url": "images/icons/camera.svg",
    "revision": "6065a364bc658654b00db8efe6bcd860"
  },
  {
    "url": "images/icons/cards.svg",
    "revision": "5d87f88d3adbcbb350d140cb4b4558d2"
  },
  {
    "url": "images/icons/cards2.svg",
    "revision": "52ae90d71bd78882d6e2db76c4e0d049"
  },
  {
    "url": "images/icons/caret-bottom-l.svg",
    "revision": "e4638f41200aeeb3732c110f73c30233"
  },
  {
    "url": "images/icons/caret-bottom.svg",
    "revision": "f2f11f688314414710e3df1925a794bf"
  },
  {
    "url": "images/icons/caret-double-l.svg",
    "revision": "43f62d8cfe7d4cc091cea435e9827105"
  },
  {
    "url": "images/icons/caret-double.svg",
    "revision": "d4182052a37fb5a3d019241a2ea246f1"
  },
  {
    "url": "images/icons/caret-left-l.svg",
    "revision": "88b2b9d84cff0039d69db24e82913a90"
  },
  {
    "url": "images/icons/caret-left.svg",
    "revision": "5a12950c032f17b055b312211e39a48c"
  },
  {
    "url": "images/icons/caret-right-l.svg",
    "revision": "8f40279ed934bea3c7a8df11cf36842b"
  },
  {
    "url": "images/icons/caret-right.svg",
    "revision": "07bccb057f15271c36866d40b072ef3d"
  },
  {
    "url": "images/icons/caret-top-l.svg",
    "revision": "6ac6c6f7dc3a371784853facc1e45eb8"
  },
  {
    "url": "images/icons/caret-top.svg",
    "revision": "eb0a868f1011c40b2222fc84d38e0a18"
  },
  {
    "url": "images/icons/categories.svg",
    "revision": "cd8d76f44ed37e0864e14cd2668cd053"
  },
  {
    "url": "images/icons/chain-broken.svg",
    "revision": "55fb658780ad585456b088a560805557"
  },
  {
    "url": "images/icons/change.svg",
    "revision": "1609475b98b3efa54987d8937e79f1f7"
  },
  {
    "url": "images/icons/check-circle-full.svg",
    "revision": "95ab317d99c7d959a6c889dfd5567ea6"
  },
  {
    "url": "images/icons/check-circle.svg",
    "revision": "4a78c432e11ec5d6122afc4ddff5bbbb"
  },
  {
    "url": "images/icons/check-square.svg",
    "revision": "2ad57ad663e5fa7c8292feaaff47ba74"
  },
  {
    "url": "images/icons/check.svg",
    "revision": "a5ba59b4a73cfb36e25d7a0e89ad0d03"
  },
  {
    "url": "images/icons/chip.svg",
    "revision": "7a1d9e3bbf5c663444c6e06db3ca00c9"
  },
  {
    "url": "images/icons/code.svg",
    "revision": "bd307515d89e736734cd0fa72129184c"
  },
  {
    "url": "images/icons/cog.svg",
    "revision": "b3d0ea07e5ae10d8805539806b125288"
  },
  {
    "url": "images/icons/columns.svg",
    "revision": "c9f88eaa067fb35e70aa86724120aa3a"
  },
  {
    "url": "images/icons/comments.svg",
    "revision": "6f17bde2f5c072a08f49448b3c6cf940"
  },
  {
    "url": "images/icons/community.svg",
    "revision": "889b89ce35fdbf543316fd8cbd56c590"
  },
  {
    "url": "images/icons/compress.svg",
    "revision": "e72e7bcce5f8cac297d4e0760546e415"
  },
  {
    "url": "images/icons/control-panel.svg",
    "revision": "a7b223121d628580e0c0804746f016c6"
  },
  {
    "url": "images/icons/custom-field.svg",
    "revision": "4ea7a7dad35bcf8c44335befb128375f"
  },
  {
    "url": "images/icons/custom-size.svg",
    "revision": "98d796eebb84cc9297f7a0be29db819a"
  },
  {
    "url": "images/icons/cut.svg",
    "revision": "97c1ab2b1ab311ca0403f8677b8ce331"
  },
  {
    "url": "images/icons/date.svg",
    "revision": "5dfab6451ed3f5de67ec9fa1dbd2e9a2"
  },
  {
    "url": "images/icons/decimal.svg",
    "revision": "cffb9e1a0035c6a162ea9eae6701799b"
  },
  {
    "url": "images/icons/desktop.svg",
    "revision": "410657f7b9ae578eff0c5fa6603dfe4a"
  },
  {
    "url": "images/icons/devices.svg",
    "revision": "83aa6b5d1a9028fd2836398d1a4231dc"
  },
  {
    "url": "images/icons/diagram.svg",
    "revision": "fc2c9e832fb1739ab65dd8cc5ba53fe9"
  },
  {
    "url": "images/icons/diary.svg",
    "revision": "9321ae591055f561457a83ee25492f49"
  },
  {
    "url": "images/icons/document.svg",
    "revision": "ebf4e5487d920bf23a9cf3f6e3e05d4b"
  },
  {
    "url": "images/icons/documents-and-media.svg",
    "revision": "1e538ea92cb8ee0556cdbab85a6f1a9d"
  },
  {
    "url": "images/icons/download.svg",
    "revision": "ee074384d80c7acee336bfa3c8c4fd9e"
  },
  {
    "url": "images/icons/drag.svg",
    "revision": "292095a9648547a5dddd04ec68054c64"
  },
  {
    "url": "images/icons/dynamic-data-list.svg",
    "revision": "5f64b5db481570e3984e5de8e2fea1bb"
  },
  {
    "url": "images/icons/dynamic-data-mapping.svg",
    "revision": "ff76b6cd2656f6c031bb098a2a2788aa"
  },
  {
    "url": "images/icons/edit-layout.svg",
    "revision": "ad497d869155acac555a2d9e1fa95e14"
  },
  {
    "url": "images/icons/effects.svg",
    "revision": "3f7f3f479408f983eb29f2f9e96080ab"
  },
  {
    "url": "images/icons/ellipsis-h.svg",
    "revision": "a64268f216b6f06abde8b0e3513c8b73"
  },
  {
    "url": "images/icons/ellipsis-v.svg",
    "revision": "e29bdcf433d7e9ad416ab41232546c80"
  },
  {
    "url": "images/icons/embed.svg",
    "revision": "96df92ededf31e43f42960e7ba22ccb7"
  },
  {
    "url": "images/icons/envelope-closed.svg",
    "revision": "2b2f1db449b3ad05ae56ac96ef557341"
  },
  {
    "url": "images/icons/envelope-open.svg",
    "revision": "cd28873398a453150e890cbcf66d5cc6"
  },
  {
    "url": "images/icons/environment-connected.svg",
    "revision": "ace8c64c8261b5b2cfd4356acc43cdff"
  },
  {
    "url": "images/icons/environment-disconnected.svg",
    "revision": "04ad58ed2dbda094d6da819ff876b2a1"
  },
  {
    "url": "images/icons/environment.svg",
    "revision": "20ea3ff8c25e487414b20a511b710a87"
  },
  {
    "url": "images/icons/exclamation-circle.svg",
    "revision": "b26fad550b62e6d3436f58aa09601c93"
  },
  {
    "url": "images/icons/exclamation-full.svg",
    "revision": "d5b8b74e663682e03f2cd12c2b816a6c"
  },
  {
    "url": "images/icons/expand.svg",
    "revision": "2c8432d28f0c8581f46d2d09f4e1f3b0"
  },
  {
    "url": "images/icons/file-script.svg",
    "revision": "5d4eb453fa7fb541c4d24cdc04dae7b8"
  },
  {
    "url": "images/icons/file-template.svg",
    "revision": "1f9b0f4f9fec3077c764f5e0cbaee06d"
  },
  {
    "url": "images/icons/file-xsl.svg",
    "revision": "2360d89a9d7d9850dbe36494c9f878a4"
  },
  {
    "url": "images/icons/filter.svg",
    "revision": "f52c74e72d36982197dfb5c23a5a58a6"
  },
  {
    "url": "images/icons/flag-empty.svg",
    "revision": "8afb2f569080a219333515d95daa9d7a"
  },
  {
    "url": "images/icons/flag-full.svg",
    "revision": "35fb79c5d3e3417eda94c8f3e0db25f2"
  },
  {
    "url": "images/icons/folder.svg",
    "revision": "43000a48c1fd1bd1d5d8b5772f2eba98"
  },
  {
    "url": "images/icons/format.svg",
    "revision": "c7cdd4ac32f34bff5ef6dccd9141a5d8"
  },
  {
    "url": "images/icons/forms.svg",
    "revision": "92e62c115ccf5da388d81ab83ed35b78"
  },
  {
    "url": "images/icons/full-size.svg",
    "revision": "6e06211a183a0ae3868450095ac70fde"
  },
  {
    "url": "images/icons/geolocation.svg",
    "revision": "ac648e8f3c5dcf8f2c5b30708601ab5e"
  },
  {
    "url": "images/icons/globe.svg",
    "revision": "4cfd68ecce6a6c6748588f85d636c0aa"
  },
  {
    "url": "images/icons/grid.svg",
    "revision": "6cda53b02a018080a35c7e02bd9d2203"
  },
  {
    "url": "images/icons/h1.svg",
    "revision": "c2f0923656901e714206ab2fb043ba25"
  },
  {
    "url": "images/icons/h2.svg",
    "revision": "b6f274fdabca4720c25c68e7251d4f6b"
  },
  {
    "url": "images/icons/hashtag.svg",
    "revision": "0c16b6b4ede677296f4a24b90f79abec"
  },
  {
    "url": "images/icons/heart.svg",
    "revision": "448720de229f33dc905651c6d86fad56"
  },
  {
    "url": "images/icons/hidden.svg",
    "revision": "b1b0d4b992971009daa1a92c372a4613"
  },
  {
    "url": "images/icons/home.svg",
    "revision": "7858e347d4d20bc6ce4a8b1536110e30"
  },
  {
    "url": "images/icons/horizontal-scroll.svg",
    "revision": "bb38743fc4228d9463f44d255b6c57d1"
  },
  {
    "url": "images/icons/hr.svg",
    "revision": "fee19d14356aa9b09741d1ddb3807bf9"
  },
  {
    "url": "images/icons/icons.svg",
    "revision": "19e3f7ad0de88f9c8cfb1ee63f898147"
  },
  {
    "url": "images/icons/import-export.svg",
    "revision": "12bb97a606f1e1408fe5f167fa4c922e"
  },
  {
    "url": "images/icons/indent-less.svg",
    "revision": "d9191f3adfdca2c75110829aaed6a6e9"
  },
  {
    "url": "images/icons/indent-more.svg",
    "revision": "3b9178a5564f59911dea3026ded866e1"
  },
  {
    "url": "images/icons/info-book.svg",
    "revision": "1ba22b843b1f4ec74f9ee59a86780184"
  },
  {
    "url": "images/icons/info-circle-open.svg",
    "revision": "f6ec5c1c79d117933222804d9d91d21b"
  },
  {
    "url": "images/icons/info-circle.svg",
    "revision": "f3c8b0b04427f0142678df2a9a76963e"
  },
  {
    "url": "images/icons/info-panel-closed.svg",
    "revision": "a4df62e33112e7c72d0339a4c9adf59e"
  },
  {
    "url": "images/icons/info-panel-open.svg",
    "revision": "5552a2997bba252628347555d525a984"
  },
  {
    "url": "images/icons/information-live.svg",
    "revision": "6ebaa56e3346f906b0d1318470620c71"
  },
  {
    "url": "images/icons/integer.svg",
    "revision": "29662bece063d5faa2603f599591fc43"
  },
  {
    "url": "images/icons/italic.svg",
    "revision": "58b06741af0664971b99c2d97e966777"
  },
  {
    "url": "images/icons/link.svg",
    "revision": "9cccff055bd63d2939b60372cd245864"
  },
  {
    "url": "images/icons/list-ol.svg",
    "revision": "d3e8bcf6d42c9435c204011755c7de35"
  },
  {
    "url": "images/icons/list-ul.svg",
    "revision": "d9b945e7b64d0b55c0efa11198500195"
  },
  {
    "url": "images/icons/list.svg",
    "revision": "b0e003429944cc74b5776b9b2f7323cd"
  },
  {
    "url": "images/icons/live.svg",
    "revision": "a0c438801cf80199b2ea7958b815d936"
  },
  {
    "url": "images/icons/lock-dots.svg",
    "revision": "f92045ab9e34ca7f7b3302f8ce6f9b1b"
  },
  {
    "url": "images/icons/lock.svg",
    "revision": "e822688b1e2414c6bf944e6663c8de0d"
  },
  {
    "url": "images/icons/logout.svg",
    "revision": "668fb1832e0221c54e59e757501a58a4"
  },
  {
    "url": "images/icons/magic.svg",
    "revision": "a88a0f859d70c62fde0c16691f003afc"
  },
  {
    "url": "images/icons/mark-as-answer.svg",
    "revision": "09c598f7923206b190b6999571a128df"
  },
  {
    "url": "images/icons/mark-as-question.svg",
    "revision": "f770d314546b826d125476863f0a9de9"
  },
  {
    "url": "images/icons/merge.svg",
    "revision": "45b22bffc6a2bed7d3b9b71ab93ac3e2"
  },
  {
    "url": "images/icons/message-boards.svg",
    "revision": "2ca22486011d045c2516163375b163b0"
  },
  {
    "url": "images/icons/message.svg",
    "revision": "964507144def35c4c30d096a6b48c15b"
  },
  {
    "url": "images/icons/mobile-device-rules.svg",
    "revision": "8597991f8cb76ca45711a2c0dba7f4bb"
  },
  {
    "url": "images/icons/mobile-landscape.svg",
    "revision": "a7463f73089a97a82fe504bda2a7fd42"
  },
  {
    "url": "images/icons/mobile-portrait.svg",
    "revision": "4c47319572a8052848a5bfa5241b94b8"
  },
  {
    "url": "images/icons/moon.svg",
    "revision": "1d0304c5233eecadcf004d3febaaa667"
  },
  {
    "url": "images/icons/move.svg",
    "revision": "86a32488855e468b8b37aad59bee7940"
  },
  {
    "url": "images/icons/myspace.svg",
    "revision": "fe86c5c6528b3d27df0aa698b448a1c8"
  },
  {
    "url": "images/icons/number.svg",
    "revision": "b29facb24ffbe12053154691467d3e4b"
  },
  {
    "url": "images/icons/oauth.svg",
    "revision": "ab2b80ed28be9329e5042245ad1d6f72"
  },
  {
    "url": "images/icons/open-id.svg",
    "revision": "af72480d1f6b90b013fbfb140fafbf8b"
  },
  {
    "url": "images/icons/order-arrow-down.svg",
    "revision": "0a1c6a8eb41cd1240739188273d5cd19"
  },
  {
    "url": "images/icons/order-arrow-up.svg",
    "revision": "a0d56b9b8c23ba7d2d512d47afc6442c"
  },
  {
    "url": "images/icons/order-arrow.svg",
    "revision": "a8f89698e194cefa2bd4cabe8e364714"
  },
  {
    "url": "images/icons/organizations.svg",
    "revision": "d69c22228932f3ef1aeeb3f001d7e377"
  },
  {
    "url": "images/icons/page-template.svg",
    "revision": "723d782303ab8a999ca2a6458e465441"
  },
  {
    "url": "images/icons/page.svg",
    "revision": "8a538adcd3c0a2a61e2d2f9a125a2d46"
  },
  {
    "url": "images/icons/pages-tree.svg",
    "revision": "7506d86c60365140b13879dfae8125a7"
  },
  {
    "url": "images/icons/paperclip.svg",
    "revision": "1688e8457bf646cfe7a6471f623bd74c"
  },
  {
    "url": "images/icons/paragraph.svg",
    "revision": "de12236aff11427aee91734abf105d9b"
  },
  {
    "url": "images/icons/password-policies.svg",
    "revision": "2bdde57559b31090f060c87ed97df44e"
  },
  {
    "url": "images/icons/paste.svg",
    "revision": "aed4d78974dc3092822d0dc9ae6b5adf"
  },
  {
    "url": "images/icons/pause.svg",
    "revision": "ee66f8454e988a9079df3a55ca415cd3"
  },
  {
    "url": "images/icons/pencil.svg",
    "revision": "70682e99d7a5b049ad5953e8b47cab46"
  },
  {
    "url": "images/icons/phone.svg",
    "revision": "2e3c51b127c4f6c07738db72a9664d95"
  },
  {
    "url": "images/icons/picture.svg",
    "revision": "9c67c066737faa9a5a65ba4369dbecf0"
  },
  {
    "url": "images/icons/play.svg",
    "revision": "e4d14656b4eb5b6e942096f5f5a984bf"
  },
  {
    "url": "images/icons/plug.svg",
    "revision": "5029cf560aec18d54d83d44a93bc40f1"
  },
  {
    "url": "images/icons/plus-squares.svg",
    "revision": "75aae9924b4c20d50223479e12a51cd2"
  },
  {
    "url": "images/icons/plus.svg",
    "revision": "f5f19247df51af27dcfa85a090bc7f8e"
  },
  {
    "url": "images/icons/polls.svg",
    "revision": "f6f55482adbc38f912b4f900d2364a93"
  },
  {
    "url": "images/icons/print.svg",
    "revision": "b33dc70b4b88c5c976cbe0591abc8f29"
  },
  {
    "url": "images/icons/product-menu-closed.svg",
    "revision": "1e3a03b2a0f8d9187114f14ac9083418"
  },
  {
    "url": "images/icons/product-menu-open.svg",
    "revision": "d5e589f9a0701f74836e4d9950267a71"
  },
  {
    "url": "images/icons/product-menu.svg",
    "revision": "213aebfc3935feed5454b234f46c7c39"
  },
  {
    "url": "images/icons/propagation.svg",
    "revision": "3ab989a91b22c51e27a5161723b634d2"
  },
  {
    "url": "images/icons/question-circle-full.svg",
    "revision": "1c9cf4deed98cdb3ad8c40f69c94d3c5"
  },
  {
    "url": "images/icons/question-circle.svg",
    "revision": "ed79cb993444d2af90fcd3fd46c5dcd6"
  },
  {
    "url": "images/icons/quote-left.svg",
    "revision": "d2e1b53416948bed1552fff4ecce0826"
  },
  {
    "url": "images/icons/quote-right.svg",
    "revision": "e551340614381824c4c0adf7919f0cef"
  },
  {
    "url": "images/icons/radio-button.svg",
    "revision": "d8e47e919ac994ee61d3e6352b87704a"
  },
  {
    "url": "images/icons/redo.svg",
    "revision": "a181b3b7caa581f52ced52453e66921f"
  },
  {
    "url": "images/icons/reload.svg",
    "revision": "b0f3fc06f731892d32f321652871b775"
  },
  {
    "url": "images/icons/remove-role.svg",
    "revision": "2039f43b07a80b4a88e47b4e3f8d26db"
  },
  {
    "url": "images/icons/remove-style.svg",
    "revision": "e08f8741cee1fce5425878b711dce5e0"
  },
  {
    "url": "images/icons/reply.svg",
    "revision": "de911afc863bd6c1c0548be743d52518"
  },
  {
    "url": "images/icons/repository.svg",
    "revision": "b18fc48955f231f2755cfe99ae24f2db"
  },
  {
    "url": "images/icons/restore.svg",
    "revision": "3b5de9d316ea61a9deedf4678fa15353"
  },
  {
    "url": "images/icons/rss-full.svg",
    "revision": "edeb90897db18affc752aa717855b147"
  },
  {
    "url": "images/icons/rss.svg",
    "revision": "e42ded15a58ac63c2147a2e0593682c2"
  },
  {
    "url": "images/icons/rules.svg",
    "revision": "48da8a910486a081d80616f89b028288"
  },
  {
    "url": "images/icons/search.svg",
    "revision": "5ed4833bf60adbd4d2ed1fd952c17ab6"
  },
  {
    "url": "images/icons/select-from-list.svg",
    "revision": "e00d25183a97d6e93ee4d5a2af18ad0b"
  },
  {
    "url": "images/icons/select.svg",
    "revision": "1adb5267cf9650917c238fe088219ea9"
  },
  {
    "url": "images/icons/separator.svg",
    "revision": "1a96c639d2510def11d55541e58ac34a"
  },
  {
    "url": "images/icons/share-alt.svg",
    "revision": "682981cbd4f9ed155b1c75ecf55b0df4"
  },
  {
    "url": "images/icons/share.svg",
    "revision": "0d7404140584a4b880efa1e8c3ecb63d"
  },
  {
    "url": "images/icons/sheets.svg",
    "revision": "a596c484bc7ab9eaf08b7bd6f0ed2388"
  },
  {
    "url": "images/icons/shopping-cart.svg",
    "revision": "751989e172cb79f66cf79e2e164421a0"
  },
  {
    "url": "images/icons/shortcut.svg",
    "revision": "34c832ce1dc5c8b69191a7c09174adca"
  },
  {
    "url": "images/icons/simulation-menu-closed.svg",
    "revision": "ef991d19a31912ee81798ec045a2cac6"
  },
  {
    "url": "images/icons/simulation-menu-open.svg",
    "revision": "a2105310cb95782e5b0d6abbc807924e"
  },
  {
    "url": "images/icons/simulation-menu.svg",
    "revision": "47a717115928ed0c5d94a6dfdc2cbb35"
  },
  {
    "url": "images/icons/site-template.svg",
    "revision": "72280c5e960d602ac57cfcd3328e4d41"
  },
  {
    "url": "images/icons/sites.svg",
    "revision": "a0774162cf7a33660fdd7861953640bb"
  },
  {
    "url": "images/icons/social-facebook.svg",
    "revision": "5c38dd869093dafddb58feb3bdc8676c"
  },
  {
    "url": "images/icons/social-linkedin.svg",
    "revision": "fc7e70995870d2b04b2b9958c6dd7e78"
  },
  {
    "url": "images/icons/staging.svg",
    "revision": "6df8d38bbd9709fe32e369dbc0c52ed2"
  },
  {
    "url": "images/icons/star-half.svg",
    "revision": "0de10cdd2ec0f4bc131f253e93eb986c"
  },
  {
    "url": "images/icons/star-o.svg",
    "revision": "5e6a68dea5c327b4fbc7ce5f955cb970"
  },
  {
    "url": "images/icons/star.svg",
    "revision": "0adc260c5548659ccc254a0e05eaf174"
  },
  {
    "url": "images/icons/sticky.svg",
    "revision": "83dffe9ca3ffd4cff8fbaab09f1810b1"
  },
  {
    "url": "images/icons/strikethrough.svg",
    "revision": "8a122ba7b87b788671a6842d6ad79089"
  },
  {
    "url": "images/icons/subscript.svg",
    "revision": "db6198633de52d9f9cacf3c44dda3816"
  },
  {
    "url": "images/icons/suitcase.svg",
    "revision": "f0268e360ab0707b2f33418a5c0f4101"
  },
  {
    "url": "images/icons/sun.svg",
    "revision": "3c4a6081a3daad27733c06c587ccac6a"
  },
  {
    "url": "images/icons/superscript.svg",
    "revision": "6dbbc9fc5a1ea4f2787df9045aebb541"
  },
  {
    "url": "images/icons/table.svg",
    "revision": "5cac30067f1884c31ab291d7bfe4f568"
  },
  {
    "url": "images/icons/table2.svg",
    "revision": "881c665066ba4c687de446fa3c15efe5"
  },
  {
    "url": "images/icons/tablet-landscape.svg",
    "revision": "e8b200d8e8c6f943cfb481711eed0977"
  },
  {
    "url": "images/icons/tablet-portrait.svg",
    "revision": "fe11a99b612f2c87b4124f664ea24b96"
  },
  {
    "url": "images/icons/tag.svg",
    "revision": "1d0cbe122a60c80777298ba6455e6314"
  },
  {
    "url": "images/icons/text-editor.svg",
    "revision": "bac1b52209a5dcd3c228b2ac7ce4daa9"
  },
  {
    "url": "images/icons/text.svg",
    "revision": "f5fb5430d0a00795028ce20ddaa3f12a"
  },
  {
    "url": "images/icons/textbox.svg",
    "revision": "66793b0a68247d3da1a7a4eaab08d083"
  },
  {
    "url": "images/icons/third-party.svg",
    "revision": "ab9539b534de370a951e976f70a60094"
  },
  {
    "url": "images/icons/thumbs-down.svg",
    "revision": "124ab551ca0f8f313a39f8800cb39f59"
  },
  {
    "url": "images/icons/thumbs-up-arrow.svg",
    "revision": "0038ee697bc64cf7c27d12d646f0143b"
  },
  {
    "url": "images/icons/thumbs-up.svg",
    "revision": "a4156ee36006a0f99bb8e6784cfe2360"
  },
  {
    "url": "images/icons/time.svg",
    "revision": "408d61e5d543350873366319c0952989"
  },
  {
    "url": "images/icons/times-circle.svg",
    "revision": "782e705ee8f78f34d62f958a99d662c2"
  },
  {
    "url": "images/icons/times.svg",
    "revision": "ab0a19c61a25a6c0995ffd4d36ab5799"
  },
  {
    "url": "images/icons/transform.svg",
    "revision": "3cb894717fb05f12084222da7638f36d"
  },
  {
    "url": "images/icons/trash.svg",
    "revision": "157e750980c50ea68fe752dd2bccf18d"
  },
  {
    "url": "images/icons/twitter.svg",
    "revision": "03d90cbb650e971aac8b1525d72292b8"
  },
  {
    "url": "images/icons/underline.svg",
    "revision": "39280ac1fc168b432002f2621f98f3e0"
  },
  {
    "url": "images/icons/undo.svg",
    "revision": "878dd82d323150fdd4e5db8de19a635b"
  },
  {
    "url": "images/icons/unlock.svg",
    "revision": "316b6f5f6a6451ec9d5ff52fa9c11c32"
  },
  {
    "url": "images/icons/upload.svg",
    "revision": "10412633d9a3665fe6cd6b008c82f574"
  },
  {
    "url": "images/icons/urgent.svg",
    "revision": "4eb3e0052634e032b4a2c6c46e3eb692"
  },
  {
    "url": "images/icons/user.svg",
    "revision": "1d4d2d0c148fada47556d2fb1ddd2781"
  },
  {
    "url": "images/icons/users.svg",
    "revision": "94ce8ee278e19233c87bef45b2ffb10b"
  },
  {
    "url": "images/icons/vertical-scroll.svg",
    "revision": "c4a68bdbfbb0a627d7740ea2b07962b3"
  },
  {
    "url": "images/icons/video.svg",
    "revision": "344cf5898bf1f645e5658f0a3af6d5bb"
  },
  {
    "url": "images/icons/view.svg",
    "revision": "4a478292acb1267adc64cd8aa6d5a577"
  },
  {
    "url": "images/icons/vocabulary.svg",
    "revision": "c0268765c7443e892f42daf3054da330"
  },
  {
    "url": "images/icons/warning-full.svg",
    "revision": "c8d1a04915b938cc6316d018f329d6b1"
  },
  {
    "url": "images/icons/warning.svg",
    "revision": "06ff3058c5283ac58273f32e92509c7b"
  },
  {
    "url": "images/icons/web-content.svg",
    "revision": "6d471b57186a93bc1003e10c1b0d77c3"
  },
  {
    "url": "images/icons/wiki-page.svg",
    "revision": "7e5dab9e5562c7a53eafaaf88f893664"
  },
  {
    "url": "images/icons/wiki.svg",
    "revision": "cbfbcbe3d67cc267e9b1221c9673c2d0"
  },
  {
    "url": "images/icons/workflow.svg",
    "revision": "bdc8f7f16552b55efa866387c8bd53ed"
  },
  {
    "url": "images/IgniteLogoBig.png",
    "revision": "29e5125cec83038e6307a6f31dcd61cf"
  },
  {
    "url": "images/matanzasgroup_web_homepage copy.png",
    "revision": "271e225236b3ee9762fd059240861e0d"
  },
  {
    "url": "images/matanzasgroup_web_homepage.png",
    "revision": "cda40fc573500810c31141822e9ed51d"
  },
  {
    "url": "images/screenshot-dev-amal copy.png",
    "revision": "181cba9bede3817e392ef19e20e138aa"
  },
  {
    "url": "images/screenshot-dev-amal.png",
    "revision": "17ee96ef1b68df2a780ce37c4a3c68b5"
  },
  {
    "url": "index.html",
    "revision": "7ef3ec8bb12dc804ee6b9f12169a94d1"
  },
  {
    "url": "logos/apicloud.png",
    "revision": "a0b2c68b4fcfe9cce967292b6538ce65"
  },
  {
    "url": "logos/BIG.png",
    "revision": "fcdf286fb6df613a812ec25286022ff0"
  },
  {
    "url": "logos/logo-1024.png",
    "revision": "0178849240f82b529333bcbc74d83594"
  },
  {
    "url": "logos/logo-192x192.png",
    "revision": "3ebd8e1dcc365130ae7e128e16d7cb70"
  },
  {
    "url": "logos/logo-48.png",
    "revision": "7bbaa5c53b80f48628af88e7dfd52216"
  },
  {
    "url": "logos/logo-512x512.png",
    "revision": "fda8054aa5cca600a9a4c853b9d89806"
  },
  {
    "url": "logos/stackgen-glyph-light.svg",
    "revision": "dad5d19a35a01bc05358c0a8b6293060"
  },
  {
    "url": "logos/stackgen-glyph.svg",
    "revision": "d2611f792138edbabeadb48e5c33495c"
  },
  {
    "url": "logos/StackGen-Logo2019.png",
    "revision": "ee9c8ba89835c74c43861a0e8a5aca13"
  },
  {
    "url": "logos/StackGenGLYPH-2019.png",
    "revision": "33a55607018e28dd246f3c70c7c9ae22"
  },
  {
    "url": "logos/StackGenLogo-2019.png",
    "revision": "d4cbd58e26737133af48d02d2ee892f5"
  },
  {
    "url": "logos/starter_logo_horizontal_2015_v1.png",
    "revision": "6e9566601f3fc45d0f6f1120c8d3ee7a"
  },
  {
    "url": "logos/starter_logo_horizontal_halfsize.png",
    "revision": "e6da60b70850be90344cfc1e1bc695cf"
  },
  {
    "url": "logos/starter_logo_vertical_color.png",
    "revision": "ac74575a8dbaadfd263fb8bfbea939c6"
  },
  {
    "url": "logos/starter_logo_vertical_color@x2.png",
    "revision": "c93545f68f12c4b8fc35c927adc3f821"
  },
  {
    "url": "logos/starter_logo_vertical_grayscale.png",
    "revision": "4e214e6529b351496e6e4962e3b3a28e"
  },
  {
    "url": "logos/starter_logo_vertical_grayscale@x2.png",
    "revision": "d44bd81778ce2f52670a3e46571b5fc7"
  },
  {
    "url": "offline-plugin-app-shell-fallback/index.html",
    "revision": "7316cfbddb9849f4adb60dadc3e8d70a"
  },
  {
    "url": "onboarding/four.html",
    "revision": "3ba2b02e0ef66c3060120a4347b0c672"
  },
  {
    "url": "onboarding/index.html",
    "revision": "d61fcaf5d40b50f67a16ec12b4a6e6b8"
  },
  {
    "url": "onboarding/one.html",
    "revision": "a2a572c9f4f0c065bc7c90faa192e076"
  },
  {
    "url": "onboarding/success.html",
    "revision": "00bf908d99b4f9deeff57333ba5e0f32"
  },
  {
    "url": "onboarding/three.html",
    "revision": "91530df8aab7bf0703d917fdf2c6e51b"
  },
  {
    "url": "onboarding/two.html",
    "revision": "f56823f1160a1d72e0debff800797b8d"
  },
  {
    "url": "openapi-gui/css/app.css"
  },
  {
    "url": "openapi-gui/css/balloon.min.css"
  },
  {
    "url": "openapi-gui/css/buefy.min.css"
  },
  {
    "url": "openapi-gui/css/font-awesome.min.css"
  },
  {
    "url": "openapi-gui/css/img/jsoneditor-icons.svg",
    "revision": "8e7baace3c3e7575c8f1e0fca9aa9a9d"
  },
  {
    "url": "openapi-gui/css/jsoneditor.min.css"
  },
  {
    "url": "openapi-gui/data/static.js"
  },
  {
    "url": "openapi-gui/fonts/fontawesome-webfont.svg",
    "revision": "912ec66d7572ff821749319396470bde"
  },
  {
    "url": "openapi-gui/img/screenshot.png",
    "revision": "b65de4ac4c9e60093876b2dabbca43b4"
  },
  {
    "url": "openapi-gui/index.html",
    "revision": "e4e2a2fc4c3797316b9b48a8b9179b28"
  },
  {
    "url": "openapi-gui/index.js"
  },
  {
    "url": "openapi-gui/js/buefy.min.js"
  },
  {
    "url": "openapi-gui/js/jquery.min.js"
  },
  {
    "url": "openapi-gui/js/js-yaml.min.js"
  },
  {
    "url": "openapi-gui/js/jsoneditor.min.js"
  },
  {
    "url": "openapi-gui/js/jsonpatch.js"
  },
  {
    "url": "openapi-gui/js/markdown-it.min.js"
  },
  {
    "url": "openapi-gui/js/tree.vg.js"
  },
  {
    "url": "openapi-gui/js/validators.min.js"
  },
  {
    "url": "openapi-gui/js/vega.min.js"
  },
  {
    "url": "openapi-gui/js/vue.min.js"
  },
  {
    "url": "openapi-gui/js/vuelidate.min.js"
  },
  {
    "url": "openapi-gui/js/wordcloud.vg.js"
  },
  {
    "url": "openapi-gui/openapi-gui.js"
  },
  {
    "url": "openapi-gui/pub/css/darkula.css"
  },
  {
    "url": "openapi-gui/pub/css/obsidian.css"
  },
  {
    "url": "openapi-gui/pub/css/print_overrides.css"
  },
  {
    "url": "openapi-gui/pub/css/print.css"
  },
  {
    "url": "openapi-gui/pub/css/screen_overrides.css"
  },
  {
    "url": "openapi-gui/pub/css/screen.css"
  },
  {
    "url": "openapi-gui/pub/css/theme_overrides.css"
  },
  {
    "url": "openapi-gui/rescue.html",
    "revision": "881d7cd007ee28d49b0515c287c1c395"
  },
  {
    "url": "openapi-gui/source/fonts/slate.svg",
    "revision": "dc7d054a3e663458c5e1963384ac90dc"
  },
  {
    "url": "openapi-gui/source/images/logo.png",
    "revision": "0a5a0e86200ae38c82205110db4d67dd"
  },
  {
    "url": "openapi-gui/source/images/navbar.png",
    "revision": "79ee59c0d413e82b16caa328d44efbe6"
  },
  {
    "url": "openapi-gui/source/javascripts/all_nosearch.js"
  },
  {
    "url": "openapi-gui/source/javascripts/all.js"
  },
  {
    "url": "openapi-gui/source/javascripts/app/_lang.js"
  },
  {
    "url": "openapi-gui/source/javascripts/app/_search.js"
  },
  {
    "url": "openapi-gui/source/javascripts/app/_toc.js"
  },
  {
    "url": "openapi-gui/source/javascripts/lib/_energize.js"
  },
  {
    "url": "openapi-gui/source/javascripts/lib/_imagesloaded.min.js"
  },
  {
    "url": "openapi-gui/source/javascripts/lib/_jquery.highlight.js"
  },
  {
    "url": "openapi-gui/source/javascripts/lib/_jquery.js"
  },
  {
    "url": "openapi-gui/source/javascripts/lib/_lunr.js"
  },
  {
    "url": "openapi-gui/src/api-definition/api-definition.js"
  },
  {
    "url": "openapi-gui/src/api-definition/output.html",
    "revision": "54383b68eda6fa0d0a9c0734d6a9b8ff"
  },
  {
    "url": "openapi-gui/src/api-yaml/api-yaml.js"
  },
  {
    "url": "openapi-gui/src/api-yaml/output.html",
    "revision": "058fa76d68e17a57f8f57c571bd670eb"
  },
  {
    "url": "openapi-gui/src/app/app.js"
  },
  {
    "url": "openapi-gui/src/app/gui.html",
    "revision": "386786ee077a520c649d562db7428ed5"
  },
  {
    "url": "openapi-gui/src/app/gui.js"
  },
  {
    "url": "openapi-gui/src/items/items.html",
    "revision": "eec485ed392631dedd1c0678912dbd46"
  },
  {
    "url": "openapi-gui/src/items/items.js"
  },
  {
    "url": "openapi-gui/src/method/method.html",
    "revision": "0bfa5af8e95d83014d1eca5ffa945761"
  },
  {
    "url": "openapi-gui/src/method/method.js"
  },
  {
    "url": "openapi-gui/src/parameter/parameter.html",
    "revision": "f26f09bd8a02ed2e792ef915904fb84e"
  },
  {
    "url": "openapi-gui/src/parameter/parameter.js"
  },
  {
    "url": "openapi-gui/src/resource/resource.html",
    "revision": "50ff96c0626ac13966b071059653e2a3"
  },
  {
    "url": "openapi-gui/src/resource/resource.js"
  },
  {
    "url": "openapi-gui/vis/tree.html",
    "revision": "cb733797a906c0524265e6a17fe728aa"
  },
  {
    "url": "openapi-gui/vis/wordcloud.html",
    "revision": "f18a5f4d3a4f459166a4570301cdf7ec"
  },
  {
    "url": "pages-manifest-3039e3747e7a0b785a91.js"
  },
  {
    "url": "pages-manifest-3918f79d5064ab104b9d.js"
  },
  {
    "url": "pages-manifest-4aec8cdbd964e14f1e19.js"
  },
  {
    "url": "pages-manifest-6d6e4653105586d098f5.js"
  },
  {
    "url": "pages-manifest-8f1a95fbe047320b0da3.js"
  },
  {
    "url": "pages-manifest-94a68891e02716ad6126.js"
  },
  {
    "url": "pages-manifest-a99725c539e9fe1ac0e3.js"
  },
  {
    "url": "pages-manifest-bafaf58cdb6d3c461c99.js"
  },
  {
    "url": "samples/survey/index.html",
    "revision": "7ef9053b3fc9b84031efe70092cddc4e"
  },
  {
    "url": "samples/survey/myConfluenceMacro.css"
  },
  {
    "url": "starter_logo_horizontal_2015_v1.png",
    "revision": "6e9566601f3fc45d0f6f1120c8d3ee7a"
  },
  {
    "url": "starter_logo_horizontal_halfsize.png",
    "revision": "e6da60b70850be90344cfc1e1bc695cf"
  },
  {
    "url": "starter_logo_vertical_color.png",
    "revision": "ac74575a8dbaadfd263fb8bfbea939c6"
  },
  {
    "url": "starter_logo_vertical_color@x2.png",
    "revision": "c93545f68f12c4b8fc35c927adc3f821"
  },
  {
    "url": "starter_logo_vertical_grayscale.png",
    "revision": "4e214e6529b351496e6e4962e3b3a28e"
  },
  {
    "url": "starter_logo_vertical_grayscale@x2.png",
    "revision": "d44bd81778ce2f52670a3e46571b5fc7"
  },
  {
    "url": "updates/index.html",
    "revision": "b6ffa7e3387e6a65d8d0b880d10a22e3"
  },
  {
    "url": "webpack-runtime-524707ad39aac4c0f3ca.js"
  },
  {
    "url": "webpack-runtime-62d99cf0ff65a35ed8e9.js"
  },
  {
    "url": "webpack-runtime-8772cf7c9f412b5cc947.js"
  },
  {
    "url": "webpack-runtime-8a9b5f0a9f3d52a0b53b.js"
  },
  {
    "url": "webpack-runtime-aaad70be6708b03a54d0.js"
  },
  {
    "url": "webpack-runtime-aba5fa8fb4c3e59fb1dc.js"
  },
  {
    "url": "webpack-runtime-e03b910b8c71def30eec.js"
  },
  {
    "url": "webpack-runtime-e0efbca92eda753574ff.js"
  }
].concat(self.__precacheManifest || []);
workbox.precaching.suppressWarnings();
workbox.precaching.precacheAndRoute(self.__precacheManifest, {});

workbox.routing.registerRoute(/(\.js$|\.css$|static\/)/, workbox.strategies.cacheFirst(), 'GET');
workbox.routing.registerRoute(/^https?:.*\.(png|jpg|jpeg|webp|svg|gif|tiff|js|woff|woff2|json|css)$/, workbox.strategies.staleWhileRevalidate(), 'GET');
workbox.routing.registerRoute(/^https?:\/\/fonts\.googleapis\.com\/css/, workbox.strategies.staleWhileRevalidate(), 'GET');

/* global importScripts, workbox, idbKeyval */

importScripts(`idb-keyval-iife.min.js`)
const WHITELIST_KEY = `custom-navigation-whitelist`

const navigationRoute = new workbox.routing.NavigationRoute(({ event }) => {
  const { pathname } = new URL(event.request.url)

  return idbKeyval.get(WHITELIST_KEY).then((customWhitelist = []) => {
    // Respond with the offline shell if we match the custom whitelist
    if (customWhitelist.includes(pathname)) {
      const offlineShell = `/offline-plugin-app-shell-fallback/index.html`
      const cacheName = workbox.core.cacheNames.precache

      return caches.match(offlineShell, { cacheName }).then(cachedResponse => {
        if (!cachedResponse) {
          return fetch(offlineShell).then(response => {
            if (response.ok) {
              return caches.open(cacheName).then(cache =>
                // Clone is needed because put() consumes the response body.
                cache.put(offlineShell, response.clone()).then(() => response)
              )
            } else {
              return fetch(event.request)
            }
          })
        }

        return cachedResponse
      })
    }

    return fetch(event.request)
  })
})

workbox.routing.registerRoute(navigationRoute)

let updatingWhitelist = null

function rawWhitelistPathnames(pathnames) {
  if (updatingWhitelist !== null) {
    // Prevent the whitelist from being updated twice at the same time
    return updatingWhitelist.then(() => rawWhitelistPathnames(pathnames))
  }

  updatingWhitelist = idbKeyval
    .get(WHITELIST_KEY)
    .then((customWhitelist = []) => {
      pathnames.forEach(pathname => {
        if (!customWhitelist.includes(pathname)) customWhitelist.push(pathname)
      })

      return idbKeyval.set(WHITELIST_KEY, customWhitelist)
    })
    .then(() => {
      updatingWhitelist = null
    })

  return updatingWhitelist
}

function rawResetWhitelist() {
  if (updatingWhitelist !== null) {
    return updatingWhitelist.then(() => rawResetWhitelist())
  }

  updatingWhitelist = idbKeyval.set(WHITELIST_KEY, []).then(() => {
    updatingWhitelist = null
  })

  return updatingWhitelist
}

const messageApi = {
  whitelistPathnames(event) {
    let { pathnames } = event.data

    pathnames = pathnames.map(({ pathname, includesPrefix }) => {
      if (!includesPrefix) {
        return `${pathname}`
      } else {
        return pathname
      }
    })

    event.waitUntil(rawWhitelistPathnames(pathnames))
  },

  resetWhitelist(event) {
    event.waitUntil(rawResetWhitelist())
  },
}

self.addEventListener(`message`, event => {
  const { gatsbyApi } = event.data
  if (gatsbyApi) messageApi[gatsbyApi](event)
})
