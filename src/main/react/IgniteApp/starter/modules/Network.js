var React = require("react");
import {
  View,
} from 'react-native';

import ApiUtils from './ApiUtils'
import LocalStorage from './LocalStorage';

var GLOBAL = require('../Global');

var isConnected = false;


// BEGIN NORMALIZR

import { normalize, Schema, arrayOf } from 'normalizr';

const assessments = new Schema('assessments');
const user = new Schema('users');

assessments.define({
  author: user,
  contributors: arrayOf(user)
});

const ServerActionCreators = {

  // These are two different XHR endpoints with different response schemas.
  // We can use the schema objects defined earlier to express both of them:

  receiveOneAsessment(response) {

    // Here, the response is an object containing data about one assessments.
    // Passing the assessments schema as second parameter to normalize() lets it
    // correctly traverse the response tree and gather all entities:

    // BEFORE:
    // {
    //   id: 1,
    //   title: 'Some Article',
    //   author: {
    //     id: 7,
    //     name: 'Dan'
    //   },
    //   contributors: [{
    //     id: 10,
    //     name: 'Abe'
    //   }, {
    //     id: 15,
    //     name: 'Fred'
    //   }]
    // }
    //
    // AFTER:
    // {
    //   result: 1,                    // <--- Note object is referenced by ID
    //   entities: {
    //     assessments: {
    //       1: {
    //         author: 7,              // <--- Same happens for references to
    //         contributors: [10, 15]  // <--- other entities in the schema
    //         ...}
    //     },
    //     users: {
    //       7: { ... },
    //       10: { ... },
    //       15: { ... }
    //     }
    //   }
    // }

    response = normalize(response, assessments);

    AppDispatcher.handleServerAction({
      type: ActionTypes.RECEIVE_ONE_ASSESSMENT,
      response
    });
  },

  receiveAllAsessments(response) {

    // Here, the response is an object with the key 'assessments' referencing
    // an array of assessments objects. Passing { assessments: arrayOf(assessments) } as
    // second parameter to normalize() lets it correctly traverse the response
    // tree and gather all entities:

    // BEFORE:
    // {
    //   assessments: [{
    //     id: 1,
    //     title: 'Some Article',
    //     author: {
    //       id: 7,
    //       name: 'Dan'
    //     },
    //     ...
    //   },
    //   ...
    //   ]
    // }
    //
    // AFTER:
    // {
    //   result: {
    //    assessments: [1, 2, ...]     // <--- Note how object array turned into ID array
    //   },
    //   entities: {
    //     assessments: {
    //       1: { author: 7, ... }, // <--- Same happens for references to other entities in the schema
    //       2: { ... },
    //       ...
    //     },
    //     users: {
    //       7: { ... },
    //       ..
    //     }
    //   }
    // }

    response = normalize(response, {
      assessments: arrayOf(assessments)
    });

    AppDispatcher.handleServerAction({
      type: ActionTypes.RECEIVE_ALL_ASSESSMENTS,
      response
    });
  }
}

// END NORMALIZR


var Network = {

   getInitialState: function() {
     return {
       data: {results:{}},
     };
   },

   componentDidMount: function() {
     this.checkConnection();
   },

   getSystemStats: function(){
    var uri = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/system/stats';
    return fetch(uri)
         .then(ApiUtils.checkStatus)
         .then(response => response.json())
         .then(isConnected = true)
         .catch(e => e)
   },

  checkConnection: function() {
    var uri = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/system/systemcheck';
    var responseJSON = fetch(uri)
      .then(ApiUtils.checkStatus)
      .then(response => response.json())
      .then(isConnected = true)
      .catch(e => e)

  },

  getItems: function(pv) {

    var itemtype = pv.type;
    var uri = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/'+ itemtype +'/-1/list';
       if(isConnected){
        return fetch(uri)
          .then(ApiUtils.checkStatus)
          .then(LocalStorage.saveData)
          .then(response => response.json())
          .catch(e => e)
      }else{
        // TODO
        return LocalStorage.getItems(uri);
      }
  },

  /*
    create the POST body from an object array
  */
  concatenatePostRequest: function(postParams){
    return postParams.join("&");
  },

  postCommand: function(itemType, itemId, commandName) {
       var uri = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/'+ itemType +'/'+itemId+'/' + commandName;
       if(isConnected){
          return fetch(uri)
            .then(ApiUtils.checkStatus)
            .then(LocalStorage.saveData)
            .then(response => response.json())
            .catch(e => e)
        }else{
            return LocalStorage.getItems(uri);
        }
    },
}

export { Network as default };
