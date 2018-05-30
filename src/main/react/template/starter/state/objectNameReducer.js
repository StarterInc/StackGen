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

  SET_{{objectname_upper}},
  SET_LAST_UPDATED,
  FETCH_{{objectname_upper}},

  CREATE_{{objectname_upper}},
  RECEIVE_{{objectname_upper}},

  FAILED_{{objectname_upper}},
  FAILED_FETCH_{{objectname_upper}},
  
  LOGOUT_{{objectname_upper}},
  RESET_PASSWORD

} from '../state/{{objectname}}Action.js'

// define the state tree for the {{objectname}}
import initialState from "../state/InitialState"

function {{objectname}}Reducer(state = initialState, action) {

alert('{{objectname}}Info Reducer called: ' );
// action: ' + JSON.stringify(action.type));
  switch (action.type) {

    case FETCH_{{objectname_upper}}:{
      // alert('FETCH_{{objectname_upper}} received action:' + action.type + '
		// {{objectname_upper}}: ' + action.{{objectname_upper}} + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case CREATE_{{objectname_upper}}:{
     // ('FETCH_{{objectname_upper}} received action:' + action.type + '
		// {{objectname_upper}}: ' + action.{{objectname_upper}} + ' password: '
		// + action.password)
      return{
          ...state,
          fetching:true,
        }
        break;
    }

    case RECEIVE_{{objectname_upper}}:{
     // alert('RECEIVE_{{objectname_upper}} received action:' + action.type +
		// ' {{objectname_upper}}Info: ' + JSON.stringify(action.payload))
      return{
        ...state,
        ...action.payload,
        fetching:false,
      }
      break;
    }

    case FAILED_FETCH_{{objectname_upper}}:{
      alert('FAIILED_FETCH_{{objectname_upper}} received action:' + action.type)
      return [
        {
          ...state,
          fetching:false,
          error:action.payload,
        }
      ];
      break;
    }

    case LOGOUT_{{objectname_upper}}:{
      return{
        ...state,
        fetching:false,
        {{objectname}}Info: {}
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
const {{objectname}}ModeledReducer = modeled({{objectname}}Reducer, ' {{objectname}}');

export default {{objectname}}ModeledReducer;
