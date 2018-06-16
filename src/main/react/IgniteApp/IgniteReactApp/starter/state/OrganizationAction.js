/*
 * Auto-generated Organization actions
 * 
*/

import ApiUtils from '../modules/ApiUtils'
var GLOBAL = require('../Global');
import strings from '../il8n/il8n'

// BEGIN CREATE ORGANIZATION SECTION

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


export const CREATE_ORGANIZATION = 'CREATE_ORGANIZATION'
export function createOrganization(u,p,e) {
  return {
    type: CREATE_ORGANIZATION,
    payload: { u },
    meta: {
        offline: {
        	// effect: ...,
          rollback: { type: 'CREATE_ORGANIZATION_ROLLBACK', meta: { u }}  
         }
      },
    fetching:true,
  }
}

export const FAILED_CREATE_ORGANIZATION = 'FAILED_CREATE_ORGANIZATION'
export function failedCreateOrganization(e) {
  if(typeof(e.response) === 'undefined'){
    // fine? connectionFailed('No Response from Server')
  }else  if(typeof(e.response.status) === 'undefined'){
    showLoginFailed('No Status from Server');
  }else  if(e.response.status === 406){
    showLoginFailed(strings.IgniteReactApp_Organization_already_exists);
  } else if(e.response.status === 500){
    showLoginFailed(strings.IgniteReactApp_Organization_unexpected_server_error);
  }
  // alert('CREATE ORGANIZATION Failed:' + JSON.stringify(e));
  return {
    type: FAILED_CREATE_ORGANIZATION,
    error:e
  }
}

/*
 * @FormParam("Organization") String Organization,
 * @FormParam("password") String password, @FormParam("email") String email,
 * @FormParam("firstName") String firstName, @FormParam("lastName") String
 * lastName, @FormParam("state") String state, @FormParam("phone") String phone,
 * @FormParam("zip") String zip, @FormParam("preferences") String preferences,
 * @FormParam("hintText") String hintText, @FormParam("status") Integer status,
 * @FormParam("create_source") Integer create_source, @FormParam("signup")
 * Boolean signup,
 */

// big kahuna create code
export function submitCreateOrganization(Organization, password, email, navigator) {

  console.log('OrganizationAction submitCreateOrganization: ' + Organization + ' : ' + email + ' : ' + navigator)

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(createOrganization(Organization, password, email))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/Organization/-1/'

    try{
        var reqStr =
        'Organization='+Organization+
        '&devicetoken='+'999'

        // var reqStrX =
		// 'Organization=carboload&password=chukles&devicetoken=2123123'
    }catch(e){
      showLoginFailed(e);
      return null;
    }

    console.log('OrganizationAction submitCreateOrganization: ' + url + reqStr)

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
        dispatch(receiveOrganization(json.Organization, json))
        .then(navigator.push({
            id: 'assessments'
        }))
      )
      .catch(e => failedCreateOrganization(e))
  }
}

// Step 2.. already signed up and now we gotta do some stuffs action creator
export const SETUP_ORGANIZATION = 'SETUP_ORGANIZATION'
export function onboardOrganization(json){
  return{
    type:SETUP_ORGANIZATION,
    payload:json
  }
}

// BEGIN UPDATE SECTION
export const SET_LAST_FETCH = 'SET_LAST_FETCH'
export const SET_AVATAR_IMAGE = 'SET_AVATAR_IMAGE'
export const SET_LAST_UPDATED = 'SET_LAST_UPDATED'
export const SET_PREFERENCES = 'SET_PREFERENCES'
export const SET_ORGANIZATION_LEVEL = 'SET_ORGANIZATION_LEVEL'
export const SET_ORGANIZATIONNAME = 'SET_ORGANIZATIONNAME'

// BEGIN FETCH SECTION
export const FETCH_ORGANIZATION = 'FETCH_ORGANIZATION'
export function fetchOrganization(u,p) {
  return {
    type: FETCH_ORGANIZATION,
    Organization:u,
    password:p
  }
}

function connectionFailed(msg){
  console.warn('Connection Failed: ' + msg)
}

function showLoginFailed(msg){
  alert('Login Failed. ' + msg); // TODO: do something useful
}

export const FAILED_FETCH_ORGANIZATION = 'FAILED_FETCH_ORGANIZATION'
export function failedLoginOrganization(e) {
  if(typeof(e.response) === 'undefined'){
    // is OK? connectionFailed('No Response from Server')
  }else if(typeof(e.response.status) === 'undefined'){
    connectionFailed('No Status from Server')
  }else if(e.response.status === 401){
    showLoginFailed(strings.IgniteReactApp_incorrect_fetch_credentials);
  }
  // console.warn('FETCH ORGANIZATION Failed:' + JSON.stringify(e));
  return {
    type: FAILED_FETCH_ORGANIZATION,
    error:e
  }
}

export const RECEIVE_ORGANIZATION = 'RECEIVE_'
export function receiveOrganization(Organization, json) {
// alert('OrganizationAction.receivedOrganization called for: ' +
// json.Organization + ' : ' + JSON.stringify(json));

  // set the SESSION ID so that the client will stay logged in
  GLOBAL.JSESSIONID = json.JSESSIONID
  if(GLOBAL.JSESSIONID === null){
    let error = new Error(strings.IgniteReactApp_Organization_unexpected_server_error)
    return failedCreateOrganization(error)
  }

  return {
    type: RECEIVE_,
    payload:  json,
    // receivedAt: Date.now()
  }
}

// This method creates a THUNK callback that executes 2 ACTIONS
export function submitLogin(Organization, password, navigator) {

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(fetchOrganization(Organization, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/Organization/-1/fetch'

    // alert('fetch: ' + JSON.stringify(url));
    try{
        var reqStr = 'Organization='+Organization+'&password='+password+'&devicetoken='+Date().now
        // var reqStrX =
		// 'Organization=carboload&password=chukles&devicetoken=2123123'
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
        dispatch(receiveOrganization(json.Organization, json))
        .then(navigator.push({
            id: 'assessments',
            passProps:{
              assessments:json
            }
        }))
      )
      .catch(e => failedLoginOrganization(e))
  }
}


export const LOGOUT_ = 'LOGOUT_'
export function logoutOrganization(id, text) {
  return {
    type: LOGOUT_,
    id,
    text
  }
}

export const RESET_PASSWORD = 'RESET_PASSWORD'
export function resetPassword(Organizationemail, password) {
  return {
    type: RESET_PASSWORD,
    Organizationemail,
    password,
  }
}

/**
 * initiate password reset
 */
export function submitResetPassword(Organizationemail, password) {
  console.log('resetPassword ACTION called');

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(resetPassword(Organizationemail, password))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/Organization/'+Organizationemail+'/password/initiate'

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
        dispatch(receiveOrganization(Organization, json))
      )
      .catch(e => e)
  }
}
