'use strict';

import App from './common/components/App';
import React from 'react';
import ReactDOM from 'react-dom';

// Do this once before any other code in your app
import 'babel-polyfill'

import {
  Router,
  Route,
  hashHistory
} from 'react-router';

// CSS
require('./starter/components/styles/normalize.css');
require('./starter/components/styles/main.css');

var content = document.getElementById('content');

ReactDOM.render((
  <Router history={hashHistory}>
    <Route path="/" component={App} />
  </Router>
), content);
