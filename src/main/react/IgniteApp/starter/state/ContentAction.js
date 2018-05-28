
export const ADD_CONTENT_ITEM = 'ADD_CONTENT_ITEM'
export const EDIT_CONTENT_ITEM = 'EDIT_CONTENT_ITEM'
export const REMOVE_CONTENT_ITEM = 'REMOVE_CONTENT_ITEM'
export const RECEIVE_CONTENT_ITEM = 'RECEIVE_CONTENT_ITEM'
export const RECEIVE_CONTENT_ITEM_LIST = 'RECEIVE_CONTENT_ITEM_LIST'

export const UPLOAD_CONTENT_ITEM = 'UPLOAD_CONTENT_ITEM'
export const UPLOAD_CONTENT_ITEM_SUCCESS = 'UPLOAD_CONTENT_ITEM_SUCCESS'
export const UPLOAD_CONTENT_ITEM_FAILURE = 'UPLOAD_CONTENT_ITEM_FAILURE'
export const UPLOAD_CONTENT_ITEM_PROGRESS = 'UPLOAD_CONTENT_ITEM_PROGRESS'

export const LOAD_CONTENT_ITEMS_SUCCESS = 'LOAD_CONTENT_ITEMS_SUCCESS'
export const LOAD_CONTENT_ITEMS_FAILURE = 'LOAD_CONTENT_ITEMS_FAILURE'
export const LOAD_CONTENT_ITEMS_REQUEST = 'LOAD_CONTENT_ITEMS_REQUEST'

var React = require("react");
var {
StyleSheet,
Component,
View,
DeviceEventEmitter,
} = React;

import ApiUtils from '../modules/ApiUtils'
var GLOBAL = require('../Global');
import strings from '../../il8n/il8n'

// var RNUploader = require('NativeModules').RNUploader;

// CRASHES IOS!
// TODO: Fix import RNFetchBlob from 'react-native-fetch-blob'

export function addContentItem(){ // fire event
  return {
    type: ADD_CONTENT_ITEM
  }
}

export function receiveContentItem(json){
  return {
    type: RECEIVE_CONTENT_ITEM,
    fetching:false,
    payload:json,
  }
}

export function receiveContentItemList(json){
  return {
    type: RECEIVE_CONTENT_ITEM_LIST,
    fetching:false,
    payload:json,
  }
}

export function uploadProgress(text){
  return {
    type: UPLOAD_CONTENT_ITEM_PROGRESS,
    payload: {
      uploadProgress: text
    }
  }
}

export function receiveContentUpload(json){
  alert('File Uploaded OK!')
  return {
    type: UPLOAD_CONTENT_ITEM_SUCCESS,
    json,
    fetching:false
  }
}

/*
  main thunk for creating a new uploaded content item

  data: binary file data base64?
  fileSize: 458129,
  width: 640,
  height: 428,
  uri: 'file:///m/n/y/data/Containers/Data/Application/5E8BB3E3-83E2-414A-988C-5B5FCA5575D4/Documents/E9C9457A-5D55-4814-A1F9-0651710B47DE.jpg',
  origURL: 'assets-library://asset/asset.JPG?id=9F983DBA-EC35-42B8-8773-B597CF782EDD&ext=JPG' }

  // we add:
  description:
  longitude:
  latitude:

// optional
  mimetype
  copyright:
  license:



  data object MAY have the following:
  The Response Object

key	iOS	Android	Description
didCancel	OK	OK	Informs you if the user cancelled the process
error	OK	OK	Contains an error message, if there is one
data	OK	OK	The base64 encoded image data (photos only)
uri	OK	OK	The uri to the local file asset on the device (photo or video)
origURL	OK	-	The URL of the original asset in photo library, if it exists
isVertical	OK	OK	Will be true if the image is vertically oriented
width	OK	OK	Image dimensions
height	OK	OK	Image dimensions
fileSize	OK	OK	The file size (photos only)
type	-	OK	The file type (photos only)
fileName	-	OK	The file name (photos only)
path	-	OK	The file path
latitude	-	OK	Latitude metadata, if available
longitude	-	OK	Longitude metadata, if available
timestamp	-	OK	Timestamp metadata, if available, in ISO8601 UTC format

  SERVER WANTS:

	public String create(@FormParam("author") int author,
	@FormParam("device") int device,
	@FormParam("copyright") String copyright,
	@FormParam("description") String description,
	@FormParam("url") String url,
	@FormParam("categories") String categories,
	@Context HttpServletRequest servletRequest,
	@Context HttpServletResponse servletResponse,
	@FormParam("longitude") Double longitude,
	@FormParam("latitude") Double latitude,
	@FormParam("mimeType") String mimeType,
	@FormParam("width") Long width, @FormParam("height") Long height)

*/
export function submitAddContentItem(description, data, currentPosition, json, dispatch, navigator) {

  if(typeof(currentPosition)!='undefined' &&
    ('undefined' != typeof currentPosition.coords)){
    var { latitude, longitude } = currentPosition.coords
  } else {
    var { latitude, longitude } = data
  }

  var urlx = ''
  var mimeType = ''
  // TODO: track down this json.json business...
  if(typeof(json.json) != 'undefined'){
    urlx = json.json.url
    mimeType = json.json.mimeType
  }

  // alert('submitAddContentItem: ' + JSON.stringify(json))

  // console.log('ContentAction.addContentItem called: ' + description + ' data:' + JSON.stringify(data) + ' ')

  // Thunk middleware knows how to handle functions
  return function (dispatch) {
    // First dispatch: the app state is updated to inform
    dispatch(addContentItem())
    var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/content/-1/'
  //  var formData = new FormData();
  // formData.append("author", "3");
  // formData.append("device", "Mobile"); // number 123456 is immediately converted to a string "123456"
    try{
        var reqStr =
        'author=-1' + // handled by server
        '&device=-1' + // handled by server
        '&copyright=TBD' + // TODO:
        '&description=' + (typeof(description)!='undefined' ? description : -1) +
        '&url=' + urlx +
        '&categories=' + (typeof(data.categories)!='undefined' ? data.categories : '-1') +
        '&longitude=' +  longitude +
        '&latitude=' +  latitude +
        '&mimeType=' + mimeType + // handled by server
        '&width=' + (typeof(data.width)!='undefined' ? data.width : -1) +
        '&height=' + (typeof(data.height)!='undefined' ? data.height : -1) +
        '&parentId=' + (typeof(data.parentId)!='undefined' ? data.parentId : -1) +
        '&parentType=' +  (typeof(data.parentType)!='undefined' ? data.parentType : -1)

        // var reqStrX = 'username=carboload&password=chukles&devicetoken=2123123'
    }catch(e){
      alert("submitAddContentItem create URL Failed:" + e);
      return null;
    }

// alert('ContentItemAction submitCreateContentItem: ' + JSON.stringify(data))

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
          dispatch(receiveContentItem(json)))
      .catch(e => failedCreateContentItem(e))
    }
  }

export function failedCreateContentItem(e){
  console.log('ContentAction.failedCreateContentItem: ' + JSON.stringify(e))
}

/*

SERVER WANTS:

@PUT
	@Path("upload")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.MULTIPART_FORM_DATA,
			MediaType.APPLICATION_OCTET_STREAM })
	public String upload(
			@FormDataParam("binary_content") FormDataContentDisposition fileDetail,
			@FormDataParam("binary_content") InputStream uploadedInputStream,
			@FormDataParam("userImage") int userImage,

*/

export function submitUploadContentItem(currentAssessment, description, image, dispatch, navigator) {
  const { currentPosition, imageSource , videoSource, fileSize} = image;

  // debugger

  //console.log('ContentAction.submitUploadContentItem called: ' + description + ' data:' + JSON.stringify(image))
  // Thunk middleware knows how to handle functions.
  return function (dispatch) {
      // First dispatch: the app state is updated to inform
      dispatch(addContentItem())
      var url = GLOBAL.API_HOST + GLOBAL.API_VERSION + '/content/-1/upload'
      if(videoSource != null){
        url += "/" + videoSource.substr(videoSource.lastIndexOf(".")+1).toLowerCase()
        src = videoSource;
        src = videoSource.substr(7) // strip the 'file://' scheme
      }else{
        url += "/" + imageSource.uri.substr(imageSource.uri.lastIndexOf(".")+1).toLowerCase() // supply the type
        src = imageSource.uri
      }
  //     alert(url)

    /*  TODO: FIX
    RNFetchBlob.fetch('POST', url, {
        'Content-Type' : 'multipart/form-data',
              'Content-Type' : 'application/octet-stream'
          }, RNFetchBlob.wrap(src))
       // listen to upload progress event
      .uploadProgress((written, total) => {
          //dispatch(uploadProgress(
            //  'uploaded:'  + (written / total)
          //))
          console.log('uploaded:'  + (written / total))
      })


      // listen to download progress event
      .progress((received, total) => {
        console.log('progress:' + (received / total)) })
      .then(response => response.json())
       .then(json =>
           dispatch(receiveContentUpload(json)))
       .then(json =>
           dispatch(submitAddContentItem(description, image, currentPosition, json, dispatch, navigator)))
       .catch(e => failedUploadItem(e))

     }
     */
}
}

// SOMETHING IN THIS SECTION CAUSES IOS CRASH WITH LARGE / LOCAL IMAGE UPLOADS
export function getMediaType(contentURL){
    var mtp = 'text/html' // default

    if (contentURL.toLowerCase().indexOf('jpg') > -1)
        mtp = 'image/jpeg';
    else if (contentURL.toLowerCase().indexOf('png') > -1)
        mtp = 'image/png';
    else if (contentURL.toLowerCase().indexOf('jpeg') > -1)
        mtp = 'image/jpeg';
    else if (contentURL.toLowerCase().indexOf('bmp') > -1)
        mtp = 'image/bmp';
    else if (contentURL.toLowerCase().indexOf('gif') > -1)
        mtp = 'image/gif';
    else if (contentURL.toLowerCase().indexOf('m4a') > -1)
        mtp = 'audio/mp4';
    else if (contentURL.toLowerCase().indexOf('mov') > -1)
        mtp = 'video/quicktime';

    return mtp;
}


export function failedUploadItem(e){
  alert('ContentAction.failedUploadItem: ' + JSON.stringify(e))
}


export function editContentItem(id, text) {
  return {
    type: EDIT_CONTENT_ITEM,
    id,
    text
  }
}

export function removeContentItem(id) {
  return {
    type: REMOVE_CONTENT_ITEM,
    id
  }
}

export function loadContentItemsSuccess(userId, response) {
  return {
    type: LOAD_CONTENT_ITEMS_SUCCESS,
    userId,
    response
  }
}

export function loadContentItemsFailure(userId, error) {
  return {
    type: LOAD_CONTENT_ITEMS_FAILURE,
    userId,
    error
  }
}

export function loadContentItemsRequest(userId) {
  return {
    type: LOAD_CONTENT_ITEMS_REQUEST,
    userId
  }
}
