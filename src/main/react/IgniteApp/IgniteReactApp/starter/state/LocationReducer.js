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

  SET_LOCATION,
  SET_LAST_UPDATED,
  FETCH_LOCATION,

  CREATE_LOCATION,
  RECEIVE_LOCATION,

  FAILED_LOCATION,
  FAILED_FETCH_LOCATION,
  
  LOGOUT_LOCATION,
  RESET_PASSWORD

} from '../state/LocationAction.js'

// define the state tree for the Location
import initialState from "../state/InitialState"

function LocationReducer(state = initialState, action) {

alert('LocationInfo Reducer called: ' );
// action: ' + JSON.stringify(action.type));
  switch (action.type) {

    case FETCH_LOCATION:{
      // alert('FETCH_LOCATION received action:' + action.type + '
		// LOCATION: ' + action.LOCATION + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case CREATE_LOCATION:{
     // ('FETCH_LOCATION received action:' + action.type + '
		// LOCATION: ' + action.LOCATION + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case RECEIVE_LOCATION:{
     // alert('RECEIVE_LOCATION received action:' + action.type +
		// ' LOCATIONInfo: ' + JSON.stringify(action.payload))
      return{
        ...state,
        ...action.payload,
        fetching:false,
      }
      break;
    }

    case FAILED_FETCH_LOCATION:{
      alert('FAIILED_FETCH_LOCATION received action:' + action.type)
      return [
        {
          ...state,
          fetching:false,
          error:action.payload,
        }
      ];
      break;
    }

    case LOGOUT_LOCATION:{
      return{
        ...state,
        fetching:false,
        LocationInfo: {}
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
const LocationModeledReducer = modeled(LocationReducer, ' Location');

export default LocationModeledReducer;
