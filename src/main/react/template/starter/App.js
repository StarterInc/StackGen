/*
*  App is where we init our cross-platform data store
*/
'use strict';

const React = require("react");
import {
    View,
    StyleSheet,
    AsyncStorage,
    AppRegistry,
    Text
} from 'react-native';

const {
    Provider,
    connect,
} = require('react-redux');

console.disableYellowBox = !true;

// get the initial app state
// const AppState = require('AppState');

// https://github.com/rt2zz/redux-persist
// import { persistStore } from 'redux-persist'

// include takes care of store init
import configureStore from './state/ReduxStore'
const store = configureStore(getInitialState());
/*
persistStore(store, {storage: AsyncStorage}, () => {
  console.log('restored')
})*/

// CodePush!!
// TODO: re-enable when CodePush supports latest React Native
// import CodePush from "react-native-code-push";

// FOR DEPLOYMENT...
// console.ignoredYellowBox = true;
// console.ignoredRedBox = true;

// react-native
// if(false) // TODO: persist state
  //persistStore(store, {storage: AsyncStorage})


// TODO: rehydrate from local store, then update from server ...
function getInitialState(){
  return require('./state/InitialState');
}

// cross platform render component
import Render from './AppRender';
import styles from './AppStyles';

import { Component } from 'react';

// TODO: Find a more conventional home for this...
// create a global debug output
console.log = function(message){
  console.warn('Message: ' + message);
}

class App extends React.Component {

  componentDidMount() {
//     console.log('App.js store getstate(): ' + JSON.stringify(store.getState()))
    // AppState.addEventListener('change', this.handleAppStateChange);

    // store.dispatch(loadContentItemsRequest());

    // TODO: updateInstallation({version});
    // TODO: CodePush.sync({installMode: CodePush.InstallMode.ON_NEXT_RESUME});
  }

  componentWillUnmount() {
    AppState.removeEventListener('change', this.handleAppStateChange);
  }

  handleAppStateChange(appState) {
    if (appState === 'active') {
      console.log('App.js State Changed!');
      //  store.dispatch(loadContentItemsRequest());
    // TODO: re-enable
    //  CodePush.sync({installMode: CodePush.InstallMode.ON_NEXT_RESUME});
    }
  }

  render() {
    return (
        <Provider store={store}>
        <View style={styles.appContainer}>
            {Render.call(this, this.props, this.state)}
        </View>
        </Provider>
    );
  }

/*
TODO: re-enable
  checkForUpdate(){
    // Prompt the user when an update is available
    // and then display a "downloading" modal
    CodePush.sync({ updateDialog: true },
      (status) => {
          switch (status) {
              case CodePush.SyncStatus.DOWNLOADING_PACKAGE:
                  // Show "downloading" modal
                  break;
              case CodePush.SyncStatus.INSTALLING_UPDATE:
                  // Hide "downloading" modal
                  break;
          }
      },
      ({ receivedBytes, totalBytes, }) => {
        // Update download modal progress
      }
    );
  }

CodePushStatusDidChange(status) {
        switch(status) {
            case CodePush.SyncStatus.CHECKING_FOR_UPDATE:
                console.log("Checking for updates.");
                break;
            case CodePush.SyncStatus.DOWNLOADING_PACKAGE:
                console.log("Downloading package.");
                break;
            case CodePush.SyncStatus.INSTALLING_UPDATE:
                console.log("Installing update.");
                break;
            case CodePush.SyncStatus.UP_TO_DATE:
                console.log("Installing update.");
                break;
            case CodePush.SyncStatus.UPDATE_INSTALLED:
                console.log("Update installed.");
                break;
        }
    }

    CodePushDownloadDidProgress(progress) {
        console.log(progress.receivedBytes + " of " + progress.totalBytes + " received.");
    }
*/
}

// wrap App in CodePush
// App = CodePush({ updateDialog: true, installMode: CodePush.InstallMode.IMMEDIATE })(App);

// register and export
module.exports = App;

AppRegistry.registerComponent('IgniteReact', () => App);
