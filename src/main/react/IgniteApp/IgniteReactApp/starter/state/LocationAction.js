/*
 * Auto-generated Location actions
 * 
*/

import ApiUtils from '../modules/ApiUtils'
var GLOBAL = require('../Global');
import strings from '../il8n/il8n'

// BEGIN CREATE LOCATION SECTION

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


export const CREATE_LOCATION = 'CREATE_LOCATION'
export function createLocation(u,p,e) {
  return {
    type: CREATE_LOCATION,
    payload: { u },
    meta: {
        offline: {
        	// effect: ...,
          rollback: { type: 'CREATE_LOCATION_ROLLBACK', meta: { u }}  
         }
      },
    fetching:true,
  }
}

export const FAILED_CREATE_LOCATION = 'FAILED_CREATE_LOCATION'
export function failedCreateLocation(e) {
  if(typeof(e.response) === 'undefined'){
    // fine? connectionFailed('No Response from Server')
  }else  if(typeof(e.response.status) === 'undefined'){
    showLoginFailed('No Status from Server');
  }else  if(e.response.status === 406){
    showLoginFailed(strings.IgniteReactApp_Location_already_exists);
  } else if(e.response.status === 500){
    showLoginFailed(strings.IgniteReactApp_Location_unexpected_server_error);
  }
  // alert('CREATE LOCATION Failed:' + JSON.stringify(e));
  return {
    type: FAILED_CREATE_LOCATION,
    error:e
  }
}

/*
 * @FormParam("Location") String Location,
 * @FormParam("password") String password, @FormParam("email") String email,
 * @FormParam("firstName") String firstName, @FormParam("lastName") String
 * lastName, @FormParam("state") String state, @FormParam("phone") String phone,
 * @FormParam("zip") String zip, @FormParam("preferences") String preferences,
 * @FormParam("hintText") String hintText, @FormParam("status") Integer status,
 * @FormParam("create_source") Integer create_source, @FormParam("signup")
 * Boolean signup,
 */

// big kahuna create code
export function submitCreateLocation(Location, password, email, navigator) {

  console.log('LocationAction submitCreateLocation: ' + Location + ' : ' + email + ' : ' + navigator)

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(createLocation(Location, password, email))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/Location/-1/'

    try{
        var reqStr =
        'Location='+Location+
        '&devicetoken='+'999'

        // var reqStrX =
		// 'Location=carboload&password=chukles&devicetoken=2123123'
    }catch(e){
      showLoginFailed(e);
      return null;
    }

    console.log('LocationAction submitCreateLocation: ' + url + reqStr)

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
        dispatch(receiveLocation(json.Location, json))
        .then(navigator.push({
            id: 'assessments'
        }))
      )
      .catch(e => failedCreateLocation(e))
  }
}

// Step 2.. already signed up and now we gotta do some stuffs action creator
export const SETUP_LOCATION = 'SETUP_LOCATION'
export function onboardLocation(json){
  return{
    type:SETUP_LOCATION,
    payload:json
  }
}

// BEGIN UPDATE SECTION
export const SET_LAST_FETCH = 'SET_LAST_FETCH'
export const SET_AVATAR_IMAGE = 'SET_AVATAR_IMAGE'
export const SET_LAST_UPDATED = 'SET_LAST_UPDATED'
export const SET_PREFERENCES = 'SET_PREFERENCES'
export const SET_LOCATION_LEVEL = 'SET_LOCATION_LEVEL'
export const SET_LOCATIONNAME = 'SET_LOCATIONNAME'

// BEGIN FETCH SECTION
export const FETCH_LOCATION = 'FETCH_LOCATION'
export function fetchLocation(u,p) {
  return {
    type: FETCH_LOCATION,
    Location:u,
    password:p
  }
}

function connectionFailed(msg){
  console.warn('Connection Failed: ' + msg)
}

function showLoginFailed(msg){
  alert('Login Failed. ' + msg); // TODO: do something useful
}

export const FAILED_FETCH_LOCATION = 'FAILED_FETCH_LOCATION'
export function failedLoginLocation(e) {
  if(typeof(e.response) === 'undefined'){
    // is OK? connectionFailed('No Response from Server')
  }else if(typeof(e.response.status) === 'undefined'){
    connectionFailed('No Status from Server')
  }else if(e.response.status === 401){
    showLoginFailed(strings.IgniteReactApp_incorrect_fetch_credentials);
  }
  // console.warn('FETCH LOCATION Failed:' + JSON.stringify(e));
  return {
    type: FAILED_FETCH_LOCATION,
    error:e
  }
}

export const RECEIVE_LOCATION = 'RECEIVE_'
export function receiveLocation(Location, json) {
// alert('LocationAction.receivedLocation called for: ' +
// json.Location + ' : ' + JSON.stringify(json));

  // set the SESSION ID so that the client will stay logged in
  GLOBAL.JSESSIONID = json.JSESSIONID
  if(GLOBAL.JSESSIONID === null){
    let error = new Error(strings.IgniteReactApp_Location_unexpected_server_error)
    return failedCreateLocation(error)
  }

  return {
    type: RECEIVE_,
    payload:  json,
    // receivedAt: Date.now()
  }
}

// This method creates a THUNK callback that executes 2 ACTIONS
export function submitLogin(Location, password, navigator) {

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(fetchLocation(Location, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/Location/-1/fetch'

    // alert('fetch: ' + JSON.stringify(url));
    try{
        var reqStr = 'Location='+Location+'&password='+password+'&devicetoken='+Date().now
        // var reqStrX =
		// 'Location=carboload&password=chukles&devicetoken=2123123'
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
        dispatch(receiveLocation(json.Location, json))
        .then(navigator.push({
            id: 'assessments',
            passProps:{
              assessments:json
            }
        }))
      )
      .catch(e => failedLoginLocation(e))
  }
}


export const LOGOUT_ = 'LOGOUT_'
export function logoutLocation(id, text) {
  return {
    type: LOGOUT_,
    id,
    text
  }
}

export const RESET_PASSWORD = 'RESET_PASSWORD'
export function resetPassword(Locationemail, password) {
  return {
    type: RESET_PASSWORD,
    Locationemail,
    password,
  }
}

/**
 * initiate password reset
 */
export function submitResetPassword(Locationemail, password) {
  console.log('resetPassword ACTION called');

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(resetPassword(Locationemail, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/Location/'+Locationemail+'/password/initiate'

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
        dispatch(receiveLocation(Location, json))
      )
      .catch(e => e)
  }
}
