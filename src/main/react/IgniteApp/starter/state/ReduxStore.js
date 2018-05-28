/*
*  configure store
*/
import {
  createStore,
  applyMiddleware,
  combineReducers
} from 'redux'

// https://github.com/rt2zz/redux-persist
import {autoRehydrate} from 'redux-persist'

// init logger facility
import createLogger from 'redux-logger'

// init thunking
import thunkMiddleware from 'redux-thunk'

// for better automation of promise handling
import promise from 'redux-promise-middleware'

// ### BEGIN IMPORT REDUCERS and ACTIONS

// map pins
// import maps from '../reducers/MapReducer'

// show latest
// import activities from '../reducers/ActivityReducer'

// map and region data items
import appStatus from './AppStatusReducer'

// content list data items
// import content from '../reducers/ContentItemReducer'

// user data items
// import roles from '../reducers/RoleReducer'

// user data items
// import userInfo from '../reducers/UserReducer'

// Location data items
// import locations from '../reducers/LocationReducer'

// Location data items
// import categories from '../reducers/CategoryReducer'

// ### END IMPORT REDUCERS and ACTIONS

// these reducers will appear in the State as "xyz:resultingvalue()"
const CombinedReducer = combineReducers({
  appStatus,
//  locations,
//  maps,
//  categories,
//  currentRegion,
//  content,
//  roles,
//  userInfo,
})

// persistStore(store) ...
export default function ConfigureStore(initialState) {
  return createStore(
    CombinedReducer,
     //  autoRehydrate(),  // store and read from disk
    applyMiddleware(
      createLogger(),    // logs actions
      // TODO:     promise(),
      thunkMiddleware,  // lets us dispatch() functions

    )
  )
}
