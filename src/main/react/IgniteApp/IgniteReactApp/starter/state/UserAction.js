/*
 * Auto-generated User actions
 * 
*/

import ApiUtils from '../modules/ApiUtils'
var GLOBAL = require('../Global');
import strings from '../il8n/il8n'

// BEGIN CREATE USER SECTION

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


export const CREATE_USER = 'CREATE_USER'
export function createUser(u,p,e) {
  return {
    type: CREATE_USER,
    payload: { u },
    meta: {
        offline: {
        	// effect: ...,
          rollback: { type: 'CREATE_USER_ROLLBACK', meta: { u }}  
         }
      },
    fetching:true,
  }
}

export const FAILED_CREATE_USER = 'FAILED_CREATE_USER'
export function failedCreateUser(e) {
  if(typeof(e.response) === 'undefined'){
    // fine? connectionFailed('No Response from Server')
  }else  if(typeof(e.response.status) === 'undefined'){
    showLoginFailed('No Status from Server');
  }else  if(e.response.status === 406){
    showLoginFailed(strings.IgniteReactApp_User_already_exists);
  } else if(e.response.status === 500){
    showLoginFailed(strings.IgniteReactApp_User_unexpected_server_error);
  }
  // alert('CREATE USER Failed:' + JSON.stringify(e));
  return {
    type: FAILED_CREATE_USER,
    error:e
  }
}

/*
 * @FormParam("User") String User,
 * @FormParam("password") String password, @FormParam("email") String email,
 * @FormParam("firstName") String firstName, @FormParam("lastName") String
 * lastName, @FormParam("state") String state, @FormParam("phone") String phone,
 * @FormParam("zip") String zip, @FormParam("preferences") String preferences,
 * @FormParam("hintText") String hintText, @FormParam("status") Integer status,
 * @FormParam("create_source") Integer create_source, @FormParam("signup")
 * Boolean signup,
 */

// big kahuna create code
export function submitCreateUser(User, password, email, navigator) {

  console.log('UserAction submitCreateUser: ' + User + ' : ' + email + ' : ' + navigator)

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(createUser(User, password, email))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/User/-1/'

    try{
        var reqStr =
        'User='+User+
        '&devicetoken='+'999'

        // var reqStrX =
		// 'User=carboload&password=chukles&devicetoken=2123123'
    }catch(e){
      showLoginFailed(e);
      return null;
    }

    console.log('UserAction submitCreateUser: ' + url + reqStr)

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
        dispatch(receiveUser(json.User, json))
        .then(navigator.push({
            id: 'assessments'
        }))
      )
      .catch(e => failedCreateUser(e))
  }
}

// Step 2.. already signed up and now we gotta do some stuffs action creator
export const SETUP_USER = 'SETUP_USER'
export function onboardUser(json){
  return{
    type:SETUP_USER,
    payload:json
  }
}

// BEGIN UPDATE SECTION
export const SET_LAST_FETCH = 'SET_LAST_FETCH'
export const SET_AVATAR_IMAGE = 'SET_AVATAR_IMAGE'
export const SET_LAST_UPDATED = 'SET_LAST_UPDATED'
export const SET_PREFERENCES = 'SET_PREFERENCES'
export const SET_USER_LEVEL = 'SET_USER_LEVEL'
export const SET_USERNAME = 'SET_USERNAME'

// BEGIN FETCH SECTION
export const FETCH_USER = 'FETCH_USER'
export function fetchUser(u,p) {
  return {
    type: FETCH_USER,
    User:u,
    password:p
  }
}

function connectionFailed(msg){
  console.warn('Connection Failed: ' + msg)
}

function showLoginFailed(msg){
  alert('Login Failed. ' + msg); // TODO: do something useful
}

export const FAILED_FETCH_USER = 'FAILED_FETCH_USER'
export function failedLoginUser(e) {
  if(typeof(e.response) === 'undefined'){
    // is OK? connectionFailed('No Response from Server')
  }else if(typeof(e.response.status) === 'undefined'){
    connectionFailed('No Status from Server')
  }else if(e.response.status === 401){
    showLoginFailed(strings.IgniteReactApp_incorrect_fetch_credentials);
  }
  // console.warn('FETCH USER Failed:' + JSON.stringify(e));
  return {
    type: FAILED_FETCH_USER,
    error:e
  }
}

export const RECEIVE_USER = 'RECEIVE_'
export function receiveUser(User, json) {
// alert('UserAction.receivedUser called for: ' +
// json.User + ' : ' + JSON.stringify(json));

  // set the SESSION ID so that the client will stay logged in
  GLOBAL.JSESSIONID = json.JSESSIONID
  if(GLOBAL.JSESSIONID === null){
    let error = new Error(strings.IgniteReactApp_User_unexpected_server_error)
    return failedCreateUser(error)
  }

  return {
    type: RECEIVE_,
    payload:  json,
    // receivedAt: Date.now()
  }
}

// This method creates a THUNK callback that executes 2 ACTIONS
export function submitLogin(User, password, navigator) {

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(fetchUser(User, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/User/-1/fetch'

    // alert('fetch: ' + JSON.stringify(url));
    try{
        var reqStr = 'User='+User+'&password='+password+'&devicetoken='+Date().now
        // var reqStrX =
		// 'User=carboload&password=chukles&devicetoken=2123123'
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
        dispatch(receiveUser(json.User, json))
        .then(navigator.push({
            id: 'assessments',
            passProps:{
              assessments:json
            }
        }))
      )
      .catch(e => failedLoginUser(e))
  }
}


export const LOGOUT_ = 'LOGOUT_'
export function logoutUser(id, text) {
  return {
    type: LOGOUT_,
    id,
    text
  }
}

export const RESET_PASSWORD = 'RESET_PASSWORD'
export function resetPassword(Useremail, password) {
  return {
    type: RESET_PASSWORD,
    Useremail,
    password,
  }
}

/**
 * initiate password reset
 */
export function submitResetPassword(Useremail, password) {
  console.log('resetPassword ACTION called');

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(resetPassword(Useremail, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/User/'+Useremail+'/password/initiate'

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
        dispatch(receiveUser(User, json))
      )
      .catch(e => e)
  }
}
