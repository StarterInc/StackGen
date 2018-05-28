import {
  ADD_LOCATION,
  ADD_LOCATIONS,
  EDIT_LOCATION,
  REMOVE_LOCATION,
  RECEIVE_LOCATION,
  RECEIVE_LOCATIONS,

  LOAD_LOCATIONS_SUCCESS,
  LOAD_LOCATIONS_FAILURE,
  LOAD_LOCATIONS_REQUEST,
} from '../state/LocationAction'

import initialState from "../state/InitialState"

function LocationReducer(state = [], action) {
  console.log('LocationReducer called: ' + JSON.stringify(state) + ' action: ' + JSON.stringify(action))

  switch (action.type) {

      case ADD_LOCATION:{
       // ('LOGIN_USER received action:' + action.type + ' username: ' + action.username + ' password: ' + action.password)
        return{
            ...state,
            fetching:true,
          }
          break;
      }
/*
      case ADD_LOCATIONS:{
       // ('LOGIN_USER received action:' + action.type + ' username: ' + action.username + ' password: ' + action.password)
        return{
            ...state,
            fetching:true,
          }
          break;
      }
*/

      case RECEIVE_LOCATIONS:{
        return [
          ...state,
          ...action.payload,
        ]
        break;
      }

      case RECEIVE_LOCATION:{
       //   alert('RECEIVE_USER received action:' + action.type + ' userInfo: ' + JSON.stringify(action.payload))
        return{
          ...state,
          ...action.payload,
          fetching:false,
        }
        break;
      }

      case EDIT_LOCATION:{
        return{
          ...state,
          ...action.payload,
          fetching:false,
        }
        break;
      }

      case REMOVE_LOCATION:{
/*
        items: [
            ...state.items.slice(0, action.payload),
            ...state.items.slice(action.payload + 1)
        ],
        */
        return state.map((Location, index) => {
          if (index === action.index) {
            return Object.assign({}, Location, {
              completed: true
            })
          }
          return Location
        })
        break;
      }

    default:{
      return state
    }
  }
}

export default LocationReducer
