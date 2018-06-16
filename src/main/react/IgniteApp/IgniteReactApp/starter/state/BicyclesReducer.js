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

  SET_BICYCLES,
  SET_LAST_UPDATED,
  FETCH_BICYCLES,

  CREATE_BICYCLES,
  RECEIVE_BICYCLES,

  FAILED_BICYCLES,
  FAILED_FETCH_BICYCLES,
  
  LOGOUT_BICYCLES,
  RESET_PASSWORD

} from '../state/BicyclesAction.js'

// define the state tree for the Bicycles
import initialState from "../state/InitialState"

function BicyclesReducer(state = initialState, action) {

alert('BicyclesInfo Reducer called: ' );
// action: ' + JSON.stringify(action.type));
  switch (action.type) {

    case FETCH_BICYCLES:{
      // alert('FETCH_BICYCLES received action:' + action.type + '
		// BICYCLES: ' + action.BICYCLES + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case CREATE_BICYCLES:{
     // ('FETCH_BICYCLES received action:' + action.type + '
		// BICYCLES: ' + action.BICYCLES + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case RECEIVE_BICYCLES:{
     // alert('RECEIVE_BICYCLES received action:' + action.type +
		// ' BICYCLESInfo: ' + JSON.stringify(action.payload))
      return{
        ...state,
        ...action.payload,
        fetching:false,
      }
      break;
    }

    case FAILED_FETCH_BICYCLES:{
      alert('FAIILED_FETCH_BICYCLES received action:' + action.type)
      return [
        {
          ...state,
          fetching:false,
          error:action.payload,
        }
      ];
      break;
    }

    case LOGOUT_BICYCLES:{
      return{
        ...state,
        fetching:false,
        BicyclesInfo: {}
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
const BicyclesModeledReducer = modeled(BicyclesReducer, ' Bicycles');

export default BicyclesModeledReducer;
