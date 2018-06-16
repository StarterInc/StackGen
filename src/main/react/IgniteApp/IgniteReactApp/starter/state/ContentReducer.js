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

  SET_CONTENT,
  SET_LAST_UPDATED,
  FETCH_CONTENT,

  CREATE_CONTENT,
  RECEIVE_CONTENT,

  FAILED_CONTENT,
  FAILED_FETCH_CONTENT,
  
  LOGOUT_CONTENT,
  RESET_PASSWORD

} from '../state/ContentAction.js'

// define the state tree for the Content
import initialState from "../state/InitialState"

function ContentReducer(state = initialState, action) {

alert('ContentInfo Reducer called: ' );
// action: ' + JSON.stringify(action.type));
  switch (action.type) {

    case FETCH_CONTENT:{
      // alert('FETCH_CONTENT received action:' + action.type + '
		// CONTENT: ' + action.CONTENT + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case CREATE_CONTENT:{
     // ('FETCH_CONTENT received action:' + action.type + '
		// CONTENT: ' + action.CONTENT + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case RECEIVE_CONTENT:{
     // alert('RECEIVE_CONTENT received action:' + action.type +
		// ' CONTENTInfo: ' + JSON.stringify(action.payload))
      return{
        ...state,
        ...action.payload,
        fetching:false,
      }
      break;
    }

    case FAILED_FETCH_CONTENT:{
      alert('FAIILED_FETCH_CONTENT received action:' + action.type)
      return [
        {
          ...state,
          fetching:false,
          error:action.payload,
        }
      ];
      break;
    }

    case LOGOUT_CONTENT:{
      return{
        ...state,
        fetching:false,
        ContentInfo: {}
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
const ContentModeledReducer = modeled(ContentReducer, ' Content');

export default ContentModeledReducer;
