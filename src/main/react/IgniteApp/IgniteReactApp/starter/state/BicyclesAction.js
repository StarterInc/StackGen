/*
 * Auto-generated Bicycles actions
 * 
*/

import ApiUtils from '../modules/ApiUtils'
var GLOBAL = require('../Global');
import strings from '../il8n/il8n'

// BEGIN CREATE BICYCLES SECTION

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


export const CREATE_BICYCLES = 'CREATE_BICYCLES'
export function createBicycles(u,p,e) {
  return {
    type: CREATE_BICYCLES,
    payload: { u },
    meta: {
        offline: {
        	// effect: ...,
          rollback: { type: 'CREATE_BICYCLES_ROLLBACK', meta: { u }}  
         }
      },
    fetching:true,
  }
}

export const FAILED_CREATE_BICYCLES = 'FAILED_CREATE_BICYCLES'
export function failedCreateBicycles(e) {
  if(typeof(e.response) === 'undefined'){
    // fine? connectionFailed('No Response from Server')
  }else  if(typeof(e.response.status) === 'undefined'){
    showLoginFailed('No Status from Server');
  }else  if(e.response.status === 406){
    showLoginFailed(strings.IgniteReactApp_Bicycles_already_exists);
  } else if(e.response.status === 500){
    showLoginFailed(strings.IgniteReactApp_Bicycles_unexpected_server_error);
  }
  // alert('CREATE BICYCLES Failed:' + JSON.stringify(e));
  return {
    type: FAILED_CREATE_BICYCLES,
    error:e
  }
}

/*
 * @FormParam("Bicycles") String Bicycles,
 * @FormParam("password") String password, @FormParam("email") String email,
 * @FormParam("firstName") String firstName, @FormParam("lastName") String
 * lastName, @FormParam("state") String state, @FormParam("phone") String phone,
 * @FormParam("zip") String zip, @FormParam("preferences") String preferences,
 * @FormParam("hintText") String hintText, @FormParam("status") Integer status,
 * @FormParam("create_source") Integer create_source, @FormParam("signup")
 * Boolean signup,
 */

// big kahuna create code
export function submitCreateBicycles(Bicycles, password, email, navigator) {

  console.log('BicyclesAction submitCreateBicycles: ' + Bicycles + ' : ' + email + ' : ' + navigator)

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(createBicycles(Bicycles, password, email))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/Bicycles/-1/'

    try{
        var reqStr =
        'Bicycles='+Bicycles+
        '&devicetoken='+'999'

        // var reqStrX =
		// 'Bicycles=carboload&password=chukles&devicetoken=2123123'
    }catch(e){
      showLoginFailed(e);
      return null;
    }

    console.log('BicyclesAction submitCreateBicycles: ' + url + reqStr)

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
        dispatch(receiveBicycles(json.Bicycles, json))
        .then(navigator.push({
            id: 'assessments'
        }))
      )
      .catch(e => failedCreateBicycles(e))
  }
}

// Step 2.. already signed up and now we gotta do some stuffs action creator
export const SETUP_BICYCLES = 'SETUP_BICYCLES'
export function onboardBicycles(json){
  return{
    type:SETUP_BICYCLES,
    payload:json
  }
}

// BEGIN UPDATE SECTION
export const SET_LAST_FETCH = 'SET_LAST_FETCH'
export const SET_AVATAR_IMAGE = 'SET_AVATAR_IMAGE'
export const SET_LAST_UPDATED = 'SET_LAST_UPDATED'
export const SET_PREFERENCES = 'SET_PREFERENCES'
export const SET_BICYCLES_LEVEL = 'SET_BICYCLES_LEVEL'
export const SET_BICYCLESNAME = 'SET_BICYCLESNAME'

// BEGIN FETCH SECTION
export const FETCH_BICYCLES = 'FETCH_BICYCLES'
export function fetchBicycles(u,p) {
  return {
    type: FETCH_BICYCLES,
    Bicycles:u,
    password:p
  }
}

function connectionFailed(msg){
  console.warn('Connection Failed: ' + msg)
}

function showLoginFailed(msg){
  alert('Login Failed. ' + msg); // TODO: do something useful
}

export const FAILED_FETCH_BICYCLES = 'FAILED_FETCH_BICYCLES'
export function failedLoginBicycles(e) {
  if(typeof(e.response) === 'undefined'){
    // is OK? connectionFailed('No Response from Server')
  }else if(typeof(e.response.status) === 'undefined'){
    connectionFailed('No Status from Server')
  }else if(e.response.status === 401){
    showLoginFailed(strings.IgniteReactApp_incorrect_fetch_credentials);
  }
  // console.warn('FETCH BICYCLES Failed:' + JSON.stringify(e));
  return {
    type: FAILED_FETCH_BICYCLES,
    error:e
  }
}

export const RECEIVE_BICYCLES = 'RECEIVE_'
export function receiveBicycles(Bicycles, json) {
// alert('BicyclesAction.receivedBicycles called for: ' +
// json.Bicycles + ' : ' + JSON.stringify(json));

  // set the SESSION ID so that the client will stay logged in
  GLOBAL.JSESSIONID = json.JSESSIONID
  if(GLOBAL.JSESSIONID === null){
    let error = new Error(strings.IgniteReactApp_Bicycles_unexpected_server_error)
    return failedCreateBicycles(error)
  }

  return {
    type: RECEIVE_,
    payload:  json,
    // receivedAt: Date.now()
  }
}

// This method creates a THUNK callback that executes 2 ACTIONS
export function submitLogin(Bicycles, password, navigator) {

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(fetchBicycles(Bicycles, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/Bicycles/-1/fetch'

    // alert('fetch: ' + JSON.stringify(url));
    try{
        var reqStr = 'Bicycles='+Bicycles+'&password='+password+'&devicetoken='+Date().now
        // var reqStrX =
		// 'Bicycles=carboload&password=chukles&devicetoken=2123123'
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
        dispatch(receiveBicycles(json.Bicycles, json))
        .then(navigator.push({
            id: 'assessments',
            passProps:{
              assessments:json
            }
        }))
      )
      .catch(e => failedLoginBicycles(e))
  }
}


export const LOGOUT_ = 'LOGOUT_'
export function logoutBicycles(id, text) {
  return {
    type: LOGOUT_,
    id,
    text
  }
}

export const RESET_PASSWORD = 'RESET_PASSWORD'
export function resetPassword(Bicyclesemail, password) {
  return {
    type: RESET_PASSWORD,
    Bicyclesemail,
    password,
  }
}

/**
 * initiate password reset
 */
export function submitResetPassword(Bicyclesemail, password) {
  console.log('resetPassword ACTION called');

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(resetPassword(Bicyclesemail, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/Bicycles/'+Bicyclesemail+'/password/initiate'

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
        dispatch(receiveBicycles(Bicycles, json))
      )
      .catch(e => e)
  }
}
