/**
 * Generated REDUX Reducer
 */
import { modeled } from 'react-redux-form';

/*
 * TOOD: implement offline updates // optimistically update the state, revert on
 * rollback const followingUsersReducer = (state, action) { switch(action.type) {
 * case 'FOLLOW_USER': return { ...state, [action.payload.userId]: true }; case
 * 'FOLLOW_USER_ROLLBACK': return omit(state, [action.payload.userId]); default:
 * return state; } }
 */
import {

  SET_USER,
  SET_LAST_UPDATED,
  FETCH_USER,

  CREATE_USER,
  RECEIVE_USER,

  FAILED_USER,
  FAILED_FETCH_USER,
  
  LOGOUT_USER,
  RESET_PASSWORD

} from '../state/UserAction.js'

// define the state tree for the User
import initialState from "../state/InitialState"

function UserReducer(state = initialState, action) {

alert('UserInfo Reducer called: ' );
// action: ' + JSON.stringify(action.type));
  switch (action.type) {

    case FETCH_USER:{
      // alert('FETCH_USER received action:' + action.type + '
		// USER: ' + action.USER + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case CREATE_USER:{
     // ('FETCH_USER received action:' + action.type + '
		// USER: ' + action.USER + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case RECEIVE_USER:{
     // alert('RECEIVE_USER received action:' + action.type +
		// ' USERInfo: ' + JSON.stringify(action.payload))
      return{
        ...state,
        ...action.payload,
        fetching:false,
      }
      break;
    }

    case FAILED_FETCH_USER:{
      alert('FAIILED_FETCH_USER received action:' + action.type)
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
        UserInfo: {}
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

// Decorated modeled reducer
const UserModeledReducer = modeled(UserReducer, ' User');

export default UserModeledReducer;
