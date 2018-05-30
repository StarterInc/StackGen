/*
*  configure store
*/
import {
  createStore,
  applyMiddleware,
  combineReducers,
  compose
} from 'redux'

// TODO: implement offline :
// https://github.com/jevakallio/redux-offline#quick-start
// import { offline } from 'redux-offline';
// import offlineConfig from 'redux-offline/lib/defaults';

/*
type OfflineAction = {
  type: string,
  payload: any,
  meta: {
   offline: {
     effect: any,
     commit: Action,
     rollback: Action
  }
}
*/

import { combineForms } from 'react-redux-form';

// https://github.com/rt2zz/redux-persist
import {autoRehydrate} from 'redux-persist'

// init logger facility
import createLogger from 'redux-logger'

// init thunking
import thunkMiddleware from 'redux-thunk'

// for better automation of promise handling
import promise from 'redux-promise-middleware'

// ### BEGIN IMPORT REDUCERS and ACTIONS

// begin userdata object state
{{#objects}}

// generated {{objectname}} Ignite Reducers
import {{objectname}}Reducer from '../state/{{objectname}}Reducer'

{{/objects}} 		

// map pins
// import maps from '../reducers/MapReducer'

// show latest
// import activities from '../reducers/ActivityReducer'

// map and region data items
// import appStatus from './AppStatusReducer'

// ### END IMPORT REDUCERS and ACTIONS

// define the state tree for the {{variablename}}
import initialState from "../state/InitialState"

const formReducer= combineForms({

	// begin userdata object state
	{{#objects}}
	{{objectname}}Reducer,
	{{/objects}} 		

});
 
// these reducers will appear in the State as "xyz:resultingvalue()"
const CombinedReducer = combineReducers(
    {
      formReducer,
    // appStatus,
    // locations,
    // maps,
    // categories,
    // currentRegion,
    // roles,
  }
)

// persistStore(store) ...
export default function ConfigureStore(initialState) {
  return createStore(
    CombinedReducer,
     // autoRehydrate(), // store and read from disk
    applyMiddleware(
      createLogger(),    // logs actions
      // TODO: promise(),
      thunkMiddleware,  // lets us dispatch() functions
    )
  )
}
