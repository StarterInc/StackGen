export const ADD_LOCATION = 'ADD_LOCATION'
export const ADD_LOCATIONS = 'ADD_LOCATIONS'
export const EDIT_LOCATION = 'EDIT_LOCATION'
export const REMOVE_LOCATION = 'REMOVE_LOCATION'
export const RECEIVE_LOCATION = 'RECEIVE_LOCATION'
export const RECEIVE_LOCATIONS = 'RECEIVE_LOCATIONS'

export const LOAD_LOCATIONS_SUCCESS = 'LOAD_LOCATIONS_SUCCESS'
export const LOAD_LOCATIONS_FAILURE = 'LOAD_LOCATIONS_FAILURE'
export const LOAD_LOCATIONS_REQUEST = 'LOAD_LOCATIONS_REQUEST'

import RNFetchBlob from 'react-native-fetch-blob'
import ApiUtils from '../modules/ApiUtils'
var GLOBAL = require('../Global');
import strings from '../../il8n/il8n'

import { // to show activity after upload
  submitRequestActivityList
} from '../state/ActivityAction'

/**
  Basic Action Creator Methods
*/
export function addLocationItemList(){ // fire event
  return {
    type: ADD_LOCATIONS
  }
}

export function receiveLocationItemList(json){
  return {
    type: RECEIVE_LOCATIONS,
    payload:json,
  }
}

/**
  Basic Action Creator Methods
*/
export function addLocationItem(){ // fire event
  return {
    type: ADD_LOCATION
  }
}

export function receiveLocationItem(json){
  return {
    type: RECEIVE_LOCATION,
    json,
  }
}

export function receiveLocationUpload(json){
  return {
    type: UPLOAD_LOCATION_SUCCESS,
    json,
  }
}

export function editLocationItemSuccess(navigator){
  navigator.push({
    idx:3,
    id: 'regionmapview',
    title: strings.bigdecimal_map_key_map,
  })
}

export function editLocationItem() {
  return {
    type: EDIT_LOCATION
  }
}

export function removeLocationItem(id) {
  return {
    type: REMOVE_LOCATION,
    id
  }
}

export function loadLocationItemsSuccess(userId, response) {
  return {
    type: LOAD_LOCATIONS_SUCCESS,
    userId,
    response
  }
}

export function loadLocationItemsFailure(userId, error) {
  return {
    type: LOAD_LOCATIONS_FAILURE,
    userId,
    error
  }
}

export function loadLocationItemsRequest() {
  return {
    type: LOAD_LOCATIONS_REQUEST,
  }
}

/*
 get location list
*/
export function submitRequestLocationList() {
  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform
    dispatch(loadLocationItemsRequest())
  //  debugger
    if(typeof(locationId)!='undefined'){
      var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/location/'+ locationId +'/';
    }else{
      var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/location/-1/list?query=';
    }

  //   alert('fetching locations with url: ' + url);

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
        dispatch(receiveLocationItemList(json))
      )
      .catch(e => {console.log('Failed to get location list: ' + e)})
  }
}

// iterate -- i like the transactional nature,
// redux middleware should handle failures and retries
export function submitAddLocationItemList(data, region, dispatch, navigator) {
  const { markers } = data;

//  debugger // TODO: remove

  markers.map(marker => (
     dispatch(submitAddLocationItem(data, marker, region, dispatch, navigator))
  ))
  return function (dispatch){
    return addLocationItemList
  }
}

/*
  main thunk for creating a new uploaded location item

  SERVER WANTS:
    @FormParam("name") String locationname,
		@FormParam("type") String locationType,
		@FormParam("thumbnail") String thumbnail,
		@FormParam("damageLevel") String status,
		@FormParam("notes") String notes,
		@FormParam("assessmentId") int resourceId, // assign to assessment
		@FormParam("longitude") Float longitude,
		@FormParam("latitude") Float latitude,
*/
export function submitAddLocationItem(data, marker, region, dispatch, navigator) {
  // Thunk middleware knows how to handle functions.
  const { currentAssessment } = data
  if(region.id == -1){
    // alert(currentAssessment.id)
    region.id = (typeof(currentAssessment) != 'undefined' ? currentAssessment.id : -1)
  }
// debugger
  return function (dispatch) {
    // First dispatch: the app state is updated to inform
    dispatch(addLocationItem())
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/location/-1/'
    try{
        var reqStr =
        'name=' + marker.calloutDescription +
        '&type=' + 'site' +
        '&thumbnail='  + '' +
        '&damageLevel=' + (typeof(data.categories)!='undefined' ? data.categories : -1) +
        '&notes='       + (typeof(data.notes)!='undefined' ? data.notes : '') +
        '&resourceId='  + region.id +  // assessmentId +
        '&longitude='   +  marker.coordinate.longitude +
        '&latitude='    +  marker.coordinate.latitude+
        '&parentId=' + (typeof(currentAssessment)!='undefined' ? currentAssessment.id : -1) +
        '&parentType=' +  (typeof(currentAssessment)!='undefined' ? 'resource' : 'none')


    }catch(e){
       alert("submitAddLocationItem create URL Failed:" + e);
      return null;
    }

    var request = new Request(url, {
      method: 'PUT',
      credentials: 'same-origin', headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: reqStr
    });

    // debugger

    fetch(request)
      .then(ApiUtils.checkStatus)
      .then(response => response.json())
      .then(json =>
          dispatch(receiveLocationItem(json)))
      //  alert("added " + marker.calloutDescription + " OK!")
      //  dispatch(submitRequestActivityList(dispatch, navigator))
      .catch(e => failedCreateLocationItem(e))
    }
  }

export function failedCreateLocationItem(e){
  console.log('LocationAction.failedCreateLocationItem: ' + JSON.stringify(e))
}

/*
.then(json =>
    navigator.push({
      id:'siteDetail',
      passProps: {
        json
      }
    })
*/

/*
  main thunk for a new uploaded location item
*/
export function submitUpdateLocationItem(data, region, dispatch, navigator) {
  //   alert('LocationAction.updateLocationItem called: ' + JSON.stringify(marker))
   // debugger // TODO: remove
  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
    // First dispatch: the app state is updated to inform
    dispatch(editLocationItem())
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/location/'+region.id+'/'
    try{
        var reqStr =
        'name=' + region.description +
        '&type=' + data.regionType +
        '&thumbnail='  + // TODO: get JPG from google maps api
        '&damageLevel=' + data.categories // TODO: get from damageLevel
        '&notes='       + // TODO: get notes +
        '&resourceId='  + region.id +  // assessmentId +
        '&longitude='   + region.longitude +
        '&latitude='    + region.latitude
    }catch(e){
       alert("submitUpdateLocationItem create URL Failed:" + e);
      return null;
    }

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
          dispatch(receiveLocationItem(json)))
      .then(editLocationItemSuccess(navigator))
      .catch(e => failedUpdateLocationItem(e))
    }
  }

export function failedUpdateLocationItem(e){
  console.log('LocationAction.failedUpdateLocationItem: ' + JSON.stringify(e))
}
