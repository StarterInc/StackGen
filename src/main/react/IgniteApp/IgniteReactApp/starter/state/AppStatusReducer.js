
import {
  RECEIVE_APP_STATUS,
  FETCH_APP_STATUS
} from '../state/AppStatusAction.js'

// define the state tree for the user
import initialState from "../state/InitialState"

export default function AppStatusReducer(state = initialState.appStatus, action) {
  //console.log('AppStatus Reducer called: ' + JSON.stringify(state) + ' action: ' + JSON.stringify(action))
  switch (action.type) {
    case FETCH_APP_STATUS:{
      return{
        ...state,
        fetching:true,
      }
      break;
    }

    case RECEIVE_APP_STATUS:{
      return{
        ...state,
        ...action.payload,
        fetching:false,
      }
      break;
    }

    default:
      return state
  }
}
