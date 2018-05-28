/*
 Logged-in User actions
*/

import ApiUtils from '../modules/ApiUtils'
var GLOBAL = require('../Global');
import strings from '../../il8n/il8n'

// BEGIN CREATE USER SECTION
export const CREATE_USER = 'CREATE_USER'
export function createUser(u,p,e) {
  return {
    type: CREATE_USER,
    username:u,
    password:p,
    email:e,
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
    showLoginFailed(strings.bigdecimal_user_already_exists);
  } else if(e.response.status === 500){
    showLoginFailed(strings.bigdecimal_user_unexpected_server_error);
  }
  // alert('CREATE User Failed:' + JSON.stringify(e));
  return {
    type: FAILED_CREATE_USER,
    error:e
  }
}

/*
@FormParam("username") String username,
@FormParam("password") String password,
@FormParam("email") String email,
@FormParam("firstName") String firstName,
@FormParam("lastName") String lastName,
@FormParam("state") String state,
@FormParam("phone") String phone,
@FormParam("zip") String zip,
@FormParam("preferences") String preferences,
@FormParam("hintText") String hintText,
@FormParam("status") Integer status,
@FormParam("create_source") Integer create_source,
@FormParam("signup") Boolean signup,
*/

// big kahuna create code
export function submitCreateUser(username, password, email, navigator) {

  console.log('UserAction submitCreateUser: ' + username + ' : ' + email + ' : ' + navigator)

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(createUser(username, password, email))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/user/-1/'

    try{
        var reqStr =
        'username='+username+
        '&password='+password+
        '&email='+email+

        '&firstName='+''+
        '&lastName='+''+
        '&state='+''+
        '&phone='+''+
        '&zip='+''+
        '&hintText='+'gold'+
        '&preferences=[]'+
        '&status=1'+
        '&create_source=1'+
        '&devicetoken='+'999'

        // var reqStrX = 'username=carboload&password=chukles&devicetoken=2123123'
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
        dispatch(receiveUser(json.username, json))
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
export const SET_LAST_LOGIN = 'SET_LAST_LOGIN'
export const SET_AVATAR_IMAGE = 'SET_AVATAR_IMAGE'
export const SET_LAST_UPDATED = 'SET_LAST_UPDATED'
export const SET_PREFERENCES = 'SET_PREFERENCES'
export const SET_USER_LEVEL = 'SET_USER_LEVEL'
export const SET_USERNAME = 'SET_USERNAME'

// BEGIN LOGIN SECTION
export const LOGIN_USER = 'LOGIN_USER'
export function loginUser(u,p) {
  return {
    type: LOGIN_USER,
    username:u,
    password:p
  }
}

function connectionFailed(msg){
  console.warn('Connection Failed: ' + msg)
}

function showLoginFailed(msg){
  alert('Login Failed. ' + msg); // TODO: do something useful
}

export const FAILED_LOGIN_USER = 'FAILED_LOGIN_USER'
export function failedLoginUser(e) {
  if(typeof(e.response) === 'undefined'){
    // is OK?  connectionFailed('No Response from Server')
  }else if(typeof(e.response.status) === 'undefined'){
    connectionFailed('No Status from Server')
  }else if(e.response.status === 401){
    showLoginFailed(strings.bigdecimal_incorrect_login_credentials);
  }
  // console.warn('LOGIN User Failed:' + JSON.stringify(e));
  return {
    type: FAILED_LOGIN_USER,
    error:e
  }
}

export const RECEIVE_USER = 'RECEIVE_USER'
export function receiveUser(username, json) {
//  alert('UserAction.receivedUser called for: ' + json.username + ' : ' + JSON.stringify(json));

  // set the SESSION ID so that the client will stay logged in
  GLOBAL.JSESSIONID = json.JSESSIONID
  if(GLOBAL.JSESSIONID === null){
    let error = new Error(strings.bigdecimal_user_unexpected_server_error)
    return failedCreateUser(error)
  }

  return {
    type: RECEIVE_USER,
    payload:  json,
    // receivedAt: Date.now()
  }
}

// This method creates a THUNK callback that executes 2 ACTIONS
export function submitLogin(username, password, navigator) {

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(loginUser(username, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/user/-1/login'

    // alert('login: ' + JSON.stringify(url));
    try{
        var reqStr = 'username='+username+'&password='+password+'&devicetoken='+Date().now
        // var reqStrX = 'username=carboload&password=chukles&devicetoken=2123123'
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
        dispatch(receiveUser(json.username, json))
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


export const LOGOUT_USER = 'LOGOUT_USER'
export function logoutUser(id, text) {
  return {
    type: LOGOUT_USER,
    id,
    text
  }
}

export const RESET_PASSWORD = 'RESET_PASSWORD'
export function resetPassword(useremail, password) {
  return {
    type: RESET_PASSWORD,
    useremail,
    password,
  }
}

/**
  initiate password reset
*/
export function submitResetPassword(useremail, password) {
  console.log('resetPassword ACTION called');

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(resetPassword(useremail, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/user/'+useremail+'/password/initiate'

  //    console.log(JSON.stringify(params));

    var reqStr = 'password='+password+'&devicetoken=2123123'

  //  console.log(reqStr)
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
        dispatch(receiveUser(username, json))
      )
      .catch(e => e)
  }
}
