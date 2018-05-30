/*
 * Auto-generated {{objectname}} actions
 * 
*/

import ApiUtils from '../modules/ApiUtils'
var GLOBAL = require('../Global');
import strings from '../il8n/il8n'

// BEGIN CREATE {{objectname_upper}} SECTION

/* TOOD: implement offline updates
const action = userId => ({
  type: 'FOLLOW_USER',
  payload: { userId },
  meta: {
    offline: {
      effect: //...,
      rollback: { type: 'FOLLOW_USER_ROLLBACK', meta: { userId }}  
     }
  }
});

*/


export const CREATE_{{objectname_upper}} = 'CREATE_{{objectname_upper}}'
export function create{{objectname}}(u,p,e) {
  return {
    type: CREATE_{{objectname_upper}},
    payload: { u },
    meta: {
        offline: {
        	// effect: ...,
          rollback: { type: 'CREATE_{{objectname_upper}}_ROLLBACK', meta: { u }}  
         }
      },
    fetching:true,
  }
}

export const FAILED_CREATE_{{objectname_upper}} = 'FAILED_CREATE_{{objectname_upper}}'
export function failedCreate{{objectname}}(e) {
  if(typeof(e.response) === 'undefined'){
    // fine? connectionFailed('No Response from Server')
  }else  if(typeof(e.response.status) === 'undefined'){
    showLoginFailed('No Status from Server');
  }else  if(e.response.status === 406){
    showLoginFailed(strings.{{appname}}_{{objectname}}_already_exists);
  } else if(e.response.status === 500){
    showLoginFailed(strings.{{appname}}_{{objectname}}_unexpected_server_error);
  }
  // alert('CREATE {{objectname_upper}} Failed:' + JSON.stringify(e));
  return {
    type: FAILED_CREATE_{{objectname_upper}},
    error:e
  }
}

/*
 * @FormParam("{{objectname}}") String {{objectname}},
 * @FormParam("password") String password, @FormParam("email") String email,
 * @FormParam("firstName") String firstName, @FormParam("lastName") String
 * lastName, @FormParam("state") String state, @FormParam("phone") String phone,
 * @FormParam("zip") String zip, @FormParam("preferences") String preferences,
 * @FormParam("hintText") String hintText, @FormParam("status") Integer status,
 * @FormParam("create_source") Integer create_source, @FormParam("signup")
 * Boolean signup,
 */

// big kahuna create code
export function submitCreate{{objectname}}({{objectname}}, password, email, navigator) {

  console.log('{{objectname}}Action submitCreate{{objectname}}: ' + {{objectname}} + ' : ' + email + ' : ' + navigator)

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(create{{objectname}}({{objectname}}, password, email))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/{{objectname}}/-1/'

    try{
        var reqStr =
        '{{objectname}}='+{{objectname}}+
        '&devicetoken='+'999'

        // var reqStrX =
		// '{{objectname}}=carboload&password=chukles&devicetoken=2123123'
    }catch(e){
      showLoginFailed(e);
      return null;
    }

    console.log('{{objectname}}Action submitCreate{{objectname}}: ' + url + reqStr)

    var request = new Request(url, {
      method: 'PUT',
      credentials: 'same-origin', headers: {

        // 'Accept': 'application/json', // x-www-form-urlencoded',
        'Content-Type': 'application/x-www-form-urlencoded'
      },
     // mode: 'cors',
     // redirect: 'follow',
      body: reqStr
    });

    fetch(request)
      .then(ApiUtils.checkStatus)
      .then(response => response.json())
      .then(json =>

        // We can dispatch many times!
        // Here, we update the app state with the results of the API call.
        dispatch(receive{{objectname}}(json.{{objectname}}, json))
        .then(navigator.push({
            id: 'assessments'
        }))
      )
      .catch(e => failedCreate{{objectname}}(e))
  }
}

// Step 2.. already signed up and now we gotta do some stuffs action creator
export const SETUP_{{objectname_upper}} = 'SETUP_{{objectname_upper}}'
export function onboard{{objectname}}(json){
  return{
    type:SETUP_{{objectname_upper}},
    payload:json
  }
}

// BEGIN UPDATE SECTION
export const SET_LAST_FETCH = 'SET_LAST_FETCH'
export const SET_AVATAR_IMAGE = 'SET_AVATAR_IMAGE'
export const SET_LAST_UPDATED = 'SET_LAST_UPDATED'
export const SET_PREFERENCES = 'SET_PREFERENCES'
export const SET_{{objectname_upper}}_LEVEL = 'SET_{{objectname_upper}}_LEVEL'
export const SET_{{objectname_upper}}NAME = 'SET_{{objectname_upper}}NAME'

// BEGIN FETCH SECTION
export const FETCH_{{objectname_upper}} = 'FETCH_{{objectname_upper}}'
export function fetch{{objectname}}(u,p) {
  return {
    type: FETCH_{{objectname_upper}},
    {{objectname}}:u,
    password:p
  }
}

function connectionFailed(msg){
  console.warn('Connection Failed: ' + msg)
}

function showLoginFailed(msg){
  alert('Login Failed. ' + msg); // TODO: do something useful
}

export const FAILED_FETCH_{{objectname_upper}} = 'FAILED_FETCH_{{objectname_upper}}'
export function failedLogin{{objectname}}(e) {
  if(typeof(e.response) === 'undefined'){
    // is OK? connectionFailed('No Response from Server')
  }else if(typeof(e.response.status) === 'undefined'){
    connectionFailed('No Status from Server')
  }else if(e.response.status === 401){
    showLoginFailed(strings.{{appname}}_incorrect_fetch_credentials);
  }
  // console.warn('FETCH {{objectname_upper}} Failed:' + JSON.stringify(e));
  return {
    type: FAILED_FETCH_{{objectname_upper}},
    error:e
  }
}

export const RECEIVE_{{objectname_upper}} = 'RECEIVE_{{objectname_upperaa}}'
export function receive{{objectname}}({{objectname}}, json) {
// alert('{{objectname}}Action.received{{objectname}} called for: ' +
// json.{{objectname}} + ' : ' + JSON.stringify(json));

  // set the SESSION ID so that the client will stay logged in
  GLOBAL.JSESSIONID = json.JSESSIONID
  if(GLOBAL.JSESSIONID === null){
    let error = new Error(strings.{{appname}}_{{objectname}}_unexpected_server_error)
    return failedCreate{{objectname}}(error)
  }

  return {
    type: RECEIVE_{{objectname_upperaa}},
    payload:  json,
    // receivedAt: Date.now()
  }
}

// This method creates a THUNK callback that executes 2 ACTIONS
export function submitLogin({{objectname}}, password, navigator) {

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(fetch{{objectname}}({{objectname}}, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/{{objectname}}/-1/fetch'

    // alert('fetch: ' + JSON.stringify(url));
    try{
        var reqStr = '{{objectname}}='+{{objectname}}+'&password='+password+'&devicetoken='+Date().now
        // var reqStrX =
		// '{{objectname}}=carboload&password=chukles&devicetoken=2123123'
    }catch(e){
      showLoginFailed(e);
      return null;
    }
    // console.log(reqStr)

    var request = new Request(url, {
      method: 'POST',
      credentials: 'same-origin',
      headers: {

        // 'Accept': 'application/json', // x-www-form-urlencoded',
        'Content-Type': 'application/x-www-form-urlencoded'
      },
     // mode: 'cors',
     // redirect: 'follow',
      body: reqStr
    });

    fetch(request)
      .then(ApiUtils.checkStatus)
      .then(response => response.json())
      .then(json =>

        // We can dispatch many times!
        // Here, we update the app state with the results of the API call.
        dispatch(receive{{objectname}}(json.{{objectname}}, json))
        .then(navigator.push({
            id: 'assessments',
            passProps:{
              assessments:json
            }
        }))
      )
      .catch(e => failedLogin{{objectname}}(e))
  }
}


export const LOGOUT_{{objectname_upperaa}} = 'LOGOUT_{{objectname_upperaa}}'
export function logout{{objectname}}(id, text) {
  return {
    type: LOGOUT_{{objectname_upperaa}},
    id,
    text
  }
}

export const RESET_PASSWORD = 'RESET_PASSWORD'
export function resetPassword({{objectname}}email, password) {
  return {
    type: RESET_PASSWORD,
    {{objectname}}email,
    password,
  }
}

/**
 * initiate password reset
 */
export function submitResetPassword({{objectname}}email, password) {
  console.log('resetPassword ACTION called');

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(resetPassword({{objectname}}email, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/{{objectname}}/'+{{objectname}}email+'/password/initiate'

  // console.log(JSON.stringify(params));

    var reqStr = 'password='+password+'&devicetoken=2123123'

  // console.log(reqStr)
    var request = new Request(url, {
      method: 'POST',
      credentials: 'same-origin', headers: {

        // 'Accept': 'application/json', // x-www-form-urlencoded',
        'Content-Type': 'application/x-www-form-urlencoded'
      },
     // mode: 'cors',
     // redirect: 'follow',
      body: reqStr
    });

    fetch(request)
      .then(ApiUtils.checkStatus)
      .then(response => response.json())
      .then(json =>

        // We can dispatch many times!
        // Here, we update the app state with the results of the API call.
        dispatch(receive{{objectname}}({{objectname}}, json))
      )
      .catch(e => e)
  }
}
