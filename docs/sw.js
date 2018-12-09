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
    "url": "webpack-runtime-9fca3b991fe8a0408a54.js"
  },
  {
    "url": "app-d973a4eaa32cdc966ed9.js"
  },
  {
    "url": "component---node-modules-gatsby-plugin-offline-app-shell-js-f0d992c50fc48516c86b.js"
  },
  {
    "url": "index.html",
    "revision": "6adc4b905ad4e92eaecb142dea1be1c4"
  },
  {
    "url": "offline-plugin-app-shell-fallback/index.html",
    "revision": "a4a4d3004d5abccb8722acb54428d507"
  },
  {
    "url": "component---src-templates-index-jsx.897eade8854b27d10c06.css"
  },
  {
    "url": "component---src-templates-index-jsx-62a2c39c8949e06a4129.js"
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
    "url": "static/d/408/path---index-6a9-qt74JpEGoxmntPyRMra3tfwplIc.json",
    "revision": "2c679c36fac27e6464a3b1b4e9d3e60f"
  },
  {
    "url": "static/d/520/path---offline-plugin-app-shell-fallback-a-30-c5a-NZuapzHg3X9TaN1iIixfv1W23E.json",
    "revision": "c2508676a2f33ea9f1f0bf472997f9a0"
  },
  {
    "url": "manifest.webmanifest",
    "revision": "062ba5a5acafbee03efd6cf9890cf9bc"
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