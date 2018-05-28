'use strict';

import React from 'react';
import { StatusBar, StyleSheet, View, Image, MapView, Text, Platform, Navigator, TouchableHighlight } from "react-native";

// our main nav controller
var Navigation = require('./components/Navigator/Navigator');
var styles = require('./AppStyles');

// provides an easy way to test experimental features... TODO: remove after v.1.0
// var TestScreen = require('./test/TestScreen');

// toggle test screen launch mode
/// var testing = false;

// TODO: fix var initialState = require('../state/InitialState');

export default function () {
//  console.log('AppRender store: ' + JSON.stringify(this.props.store))
  return (
    <View style={styles.appContainer}>
    <StatusBar
      // showHideTransition enum('fade', 'slide')
      // networkActivityIndicatorVisible={true}
       backgroundColor="#00796B" //adnroid only
      // animated="true"
      // barStyle="light-content"
   />
      <Image style={styles.appContainer} source={require( '../starter/assets/background.png')} >
         <Navigation store={this.props.store} />
      </Image>
    </View>
  );
}
