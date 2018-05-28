
// TODO: investigate whether this is ever required
// import { combineReducers } from 'redux'

import {
  SET_USERNAME,
  SET_LAST_LOGIN,
  SET_LAST_UPDATED,
  SET_USER_LEVEL,
  SET_AVATAR_IMAGE,
  SET_PREFERENCES,

  CREATE_USER,
  LOGIN_USER,
  LOGOUT_USER,
  FAILED_LOGIN_USER,
  RECEIVE_USER,
  RESET_PASSWORD,
} from '../state/UserAction.js'

// define the state tree for the user
import initialState from "../state/InitialState"
// initialState.userInfo
export default function userInfo(state = {}, action) {

// alert('userInfo Reducer called: ' + JSON.stringify(state) + ' action: ' + JSON.stringify(action.type));
  switch (action.type) {

    case LOGIN_USER:{
      // alert('LOGIN_USER received action:' + action.type + ' username: ' + action.username + ' password: ' + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case CREATE_USER:{
     // ('LOGIN_USER received action:' + action.type + ' username: ' + action.username + ' password: ' + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case RECEIVE_USER:{
     //   alert('RECEIVE_USER received action:' + action.type + ' userInfo: ' + JSON.stringify(action.payload))
      return{
        ...state,
        ...action.payload,
        fetching:false,
      }
      break;
    }

    case FAILED_LOGIN_USER:{
      alert('FAIILED_LOGIN_USER received action:' + action.type)
      return [
        {
          ...state,
          fetching:false,
          error:action.payload,
        }
      ];
      break;
    }

    case LOGOUT_USER:{
      return{
        ...state,
        fetching:false,
        userInfo: {}
      }
      break;
    }

    case RESET_PASSWORD:{
      return{
          ...state,
          fetching:true,
          password:action.payload
        }
        break;
      }

    default:{
      return{
        ...state,
        fetching:false
      }
    }
  }
}

/*

const username = (text) => ({type: SET_USERNAME, text})
const avatarImage = (data) => ({type: SET_AVATAR_IMAGE, data})
const password = (data) => ({type: RESET_PASSWORD, data})
const preferences = (message) => ({type: SET_PREFERENCES, message})
const userLevel = (message) => ({type: SET_USER_LEVEL, message})
const lastUpdated = (message) => ({type: SET_LAST_UPDATED, message})
const lastLogin = (message) => ({type: SET_LAST_LOGIN, message})

export default combineReducers({
  userInfo,
  username,
  avatarImage,
  password,
  preferences,
  userLevel,
  lastUpdated,
  lastLogin
})
*/


// export default userInfo
