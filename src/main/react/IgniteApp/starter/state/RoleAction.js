/*
 Logged-in Role actions
*/

import ApiUtils from '../modules/ApiUtils'
import strings from '../../il8n/il8n'
var GLOBAL = require('../Global');

// BEGIN CREATE ROLE SECTION
export const CREATE_ROLE = 'CREATE_ROLE'
export function createRole(u,p,e) {
  return {
    type: CREATE_ROLE,
    name:u,
    system_role:p,
    size_max:e,
    fetching:true
  }
}

/*
@FormParam("name") String name,
@FormParam("system_role") String system_role,
@FormParam("size_max") String size_max,
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
export function submitCreateRole(name, navigator) {

//  alert('submitCreateRole: ' + name  + ' : ' + navigator)

  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform

    dispatch(createRole(name, system_role, size_max))
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/role/-1/'

    try{
        var reqStr =
        'name='+name+
        '&system_role=0' +
        '&size_max=100'

        // var reqStrX = 'name=carboload&system_role=chukles&devicetoken=2123123'
    }catch(e){
      showJoinFailed(e);
      return null;
    }

    console.log('RoleAction submitCreateRole: ' + url + reqStr)

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
        dispatch(receiveRole(json.name, json)))
        .then(navigator.push({
            id: 'user-settings'
        }))
      .catch(e => failedCreateRole(e))
  }
}

export const FAILED_CREATE_ROLE = 'FAILED_CREATE_ROLE'
	export function failedCreateRole(e) {
	  console.log('Role Create Failed:' + JSON.stringify(e));
	  return {
	    type: FAILED_CREATE_ROLE,
	  }
	}


// Step 2.. already signed up and now we gotta do some stuffs action creator
export const SETUP_ROLE = 'SETUP_ROLE'
export function onboardRole(json){
  return{
    type:SETUP_ROLE,
    payload:json
  }
}

// BEGIN UPDATE SECTION
// export const SET_AVATAR_IMAGE = 'SET_AVATAR_IMAGE'
// export const SET_LAST_UPDATED = 'SET_LAST_UPDATED'
// export const SET_PREFERENCES = 'SET_PREFERENCES'
// export const SET_ROLE_LEVEL = 'SET_ROLE_LEVEL'

export const SET_ROLE_NAME = 'SET_ROLE_NAME'

// BEGIN LOGIN SECTION

export const JOIN_ROLE = 'JOIN_ROLE'
export function joinRole() {
  return {
    type: JOIN_ROLE,
  }
}

export const FAILED_JOIN_ROLE = 'FAILED_JOIN_ROLE'
export function failedJoinRole(e) {
  console.log('Join Role Failed:' + JSON.stringify(e));
  return {
    type: FAILED_JOIN_ROLE,
  }
}

export const RECEIVE_ROLE = 'RECEIVE_ROLE'
export function receiveRole(name, json) {
  // alert('RoleAction.receivedRole called for: ' + json.name + ' : ' + JSON.stringify(json));

  return {
    type: RECEIVE_ROLE,
    payload:  json,
    name:name,
    fetching:false
    // receivedAt: Date.now()
  }
}

// This method creates a THUNK callback that executes 2 ACTIONS
export function submitJoin(name, role, dispatch, navigator) {

  return function (dispatch) {

    dispatch(joinRole())

  //  alert(JSON.stringify(name))

    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/role/'+role.id+'/adduserbyname'
    try{
        var reqStr = 'userNameOrEmail=' + name +
         '&roleName=' + role.name +
         '&roleReadOnly=' + role.read_only

    }catch(e){
      showJoinFailed(e);
      return null;
    }

    // alert('RoleAction.submitJoin: ' + url + reqStr);

    var request = new Request(url, {
      method: 'PUT',
      credentials: 'same-origin', headers: {

        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: reqStr
    });
    fetch(request)
      .then(ApiUtils.checkStatus)
      .then(response => response.json())
      .then(json =>
        dispatch(receiveRole(name, json))
      )
      .then( json => alert('Invite Successfully Sent'))
      .catch(e => failedJoinRole(e))
  }
}

export const RESIGN_ROLE = 'RESIGN_ROLE'
export function logoutRole(id, text) {
  return {
    type: RESIGN_ROLE,
    id,
    text
  }
}

export const REQUEST_ROLES = 'REQUEST_ROLES'
export function requestRoles() {
  return {
    type: REQUEST_ROLES,
    fetching:true,
    lastUpdated: Date.now()
  }
}

export const RECEIVE_ROLES = 'RECEIVE_ROLES'
export function receiveRoles(json) {
   // console.log('RoleAction.receiveRoles called for : ' + JSON.stringify(json));
  return {
    type: RECEIVE_ROLES,
    payload:  json,
    // receivedAt: Date.now()
  }
}

export function submitRequestRoleList(roleId) {
  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform
    dispatch(requestRoles())

    if(typeof(roleId)!=='undefined'){
      var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/role/'+ roleId +'/';
    }else{
      var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/role/-1/list?query=';
    }
  //   alert('fetching roles with url: ' + url);

    var request = new Request(url, {
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
        dispatch(receiveRoles(json))
        // .then(alert('Roles Received'))
      )
      .catch(e => {alert('failed to get role list: ' + e)})
  }
}
