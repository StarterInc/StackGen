/*
  The AppStatus, Sites, and Maps data
*/

import ApiUtils from '../modules/ApiUtils'
var GLOBAL = require('../Global');

export const FETCH_APP_STATUS = 'FETCH_APP_STATUS'
export function requestAppStatus() {
  return {
    type: FETCH_APP_STATUS,
    lastUpdated: Date.now()
  }
}

export const RECEIVE_APP_STATUS = 'RECEIVE_APP_STATUS'
function receiveAppStatus(json) {
  console.log('AppStatusAction received app status from server: ' + JSON.stringify(json));
  return {
    type: RECEIVE_APP_STATUS,
    payload: json,
  }
}

export function submitAppStatus() {
  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform
    dispatch(requestAppStatus())
    var uri = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/system/stats';
//    console.log(uri);

    var request = new Request(uri, {
      method: 'GET',
      credentials: 'same-origin', headers: {

        // 'Accept': 'application/json', // x-www-form-urlencoded',
        'Content-Type': 'application/x-www-form-urlencoded'
      },
    });
    fetch(request)
      .then(ApiUtils.checkStatus)
      .then(response => response.json())
      .then(json =>
        // We can dispatch many times!
        // Here, we update the app state with the results of the API call.
        dispatch(receiveAppStatus(json))
      )
      .catch(e => e)
  }
}
