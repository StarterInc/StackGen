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

importScripts("workbox-v3.6.2/workbox-sw.js");
workbox.setConfig({modulePathPrefix: "workbox-v3.6.2"});

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
    "url": "webpack-runtime-17f7ced8a87b567d59fd.js"
  },
  {
    "url": "app-7a6f119bc1d43ed9b3db.js"
  },
  {
    "url": "component---node-modules-gatsby-plugin-offline-app-shell-js-f0d992c50fc48516c86b.js"
  },
  {
    "url": "index.html",
    "revision": "65721856da37b7308d5c71ed034e22a5"
  },
  {
    "url": "offline-plugin-app-shell-fallback/index.html",
    "revision": "87a7cda512326df2621a7952d606e0b3"
  },
  {
    "url": "component---src-templates-index-jsx.897eade8854b27d10c06.css"
  },
  {
    "url": "component---src-templates-index-jsx-bc91c60511b95e38cdc6.js"
  },
  {
    "url": "0-2a77a8dd8ba801b5a806.js"
  },
  {
    "url": "1-f891b5c196d82154ce95.js"
  },
  {
    "url": "14-0ddcf1081e572d0344b9.js"
  },
  {
    "url": "3-191fc202d23313df650f.js"
  },
  {
    "url": "static/d/119/path---index-6a9-8t3Y08NCLQd2v1WZKv4Snhx4nuk.json",
    "revision": "f3c5be852d929c2ae8ee0f15cfd3814a"
  },
  {
    "url": "static/d/520/path---offline-plugin-app-shell-fallback-a-30-c5a-NZuapzHg3X9TaN1iIixfv1W23E.json",
    "revision": "c2508676a2f33ea9f1f0bf472997f9a0"
  },
  {
    "url": "manifest.webmanifest",
    "revision": "64380e333e2a13c9c3465b6446633be0"
  }
].concat(self.__precacheManifest || []);
workbox.precaching.suppressWarnings();
workbox.precaching.precacheAndRoute(self.__precacheManifest, {});

workbox.routing.registerNavigationRoute("/offline-plugin-app-shell-fallback/index.html", {
  whitelist: [/^[^?]*([^.?]{5}|\.html)(\?.*)?$/],
  blacklist: [/\?(.+&)?no-cache=1$/],
});

workbox.routing.registerRoute(/\.(?:png|jpg|jpeg|webp|svg|gif|tiff|js|woff|woff2|json|css)$/, workbox.strategies.staleWhileRevalidate(), 'GET');
workbox.routing.registerRoute(/^https:/, workbox.strategies.networkFirst(), 'GET');
"use strict";

/* global workbox */
self.addEventListener("message", function (event) {
  var api = event.data.api;

  if (api === "gatsby-runtime-cache") {
    var resources = event.data.resources;
    var cacheName = workbox.core.cacheNames.runtime;
    event.waitUntil(caches.open(cacheName).then(function (cache) {
      return Promise.all(resources.map(function (resource) {
        return cache.add(resource).catch(function (e) {
          // ignore TypeErrors - these are usually due to
          // external resources which don't allow CORS
          if (!(e instanceof TypeError)) throw e;
        });
      }));
    }));
  }
});