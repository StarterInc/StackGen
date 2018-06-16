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

  SET_ORGANIZATION,
  SET_LAST_UPDATED,
  FETCH_ORGANIZATION,

  CREATE_ORGANIZATION,
  RECEIVE_ORGANIZATION,

  FAILED_ORGANIZATION,
  FAILED_FETCH_ORGANIZATION,
  
  LOGOUT_ORGANIZATION,
  RESET_PASSWORD

} from '../state/OrganizationAction.js'

// define the state tree for the Organization
import initialState from "../state/InitialState"

function OrganizationReducer(state = initialState, action) {

alert('OrganizationInfo Reducer called: ' );
// action: ' + JSON.stringify(action.type));
  switch (action.type) {

    case FETCH_ORGANIZATION:{
      // alert('FETCH_ORGANIZATION received action:' + action.type + '
		// ORGANIZATION: ' + action.ORGANIZATION + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case CREATE_ORGANIZATION:{
     // ('FETCH_ORGANIZATION received action:' + action.type + '
		// ORGANIZATION: ' + action.ORGANIZATION + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case RECEIVE_ORGANIZATION:{
     // alert('RECEIVE_ORGANIZATION received action:' + action.type +
		// ' ORGANIZATIONInfo: ' + JSON.stringify(action.payload))
      return{
        ...state,
        ...action.payload,
        fetching:false,
      }
      break;
    }

    case FAILED_FETCH_ORGANIZATION:{
      alert('FAIILED_FETCH_ORGANIZATION received action:' + action.type)
      return [
        {
          ...state,
          fetching:false,
          error:action.payload,
        }
      ];
      break;
    }

    case LOGOUT_ORGANIZATION:{
      return{
        ...state,
        fetching:false,
        OrganizationInfo: {}
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
const OrganizationModeledReducer = modeled(OrganizationReducer, ' Organization');

export default OrganizationModeledReducer;
