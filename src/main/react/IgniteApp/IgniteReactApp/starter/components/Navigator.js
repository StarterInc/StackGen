/**
 * 
 */
'use strict';

import React, {Component, View} from 'react';
import {
  Dimensions,
  Navigator,
  ScrollView,
  StyleSheet,
  Text,
  MapView,
  TouchableHighlight,
} from 'react-native';

const {
    Provider,
    connect,
} = require('react-redux');

import styles from '../AppStyles';

var stateData = require('../state/InitialState');

import strings from '../il8n/il8n'

// var BreadcrumbNav = require('./BreadcrumbNav');
// var NavigationBar = require('./NavigationBar');

// views
// var MediaDetail = require('../views/MediaDetail');
// var MediaPlayerComponent = require('../components/MediaPlayerComponent');
/*
 * var ResetPassword = require('../views/ResetPassword'); 
 * require('../components/ContentItemListView'); var CameraView=
 * require('../views/CameraView'); var PolygonMapViewComponent =
 * require('../components/PolygonMapViewComponent'); var RegionMapView =
 * require('../views/UserSettings'); var ImagePicker =
 * require('../components/ImagePicker'); var ModalCapture =
 * require('../components/ModalCapture'); var HamburgerMenu =
 * require('../components/HamburgerMenu');
 * 
 * var MapViewComponent = require('../components/MapViewComponent');
 */

 
var CreateUser = require('../views/CreateUser');
var WebViewComponent = require('../views/WebViewComponent');
var WP = require('../views/WP');
var About = require('../views/About');
var Messenger = require('../views/Messenger');
var Login = require('../views/Login');

class NavButton extends React.Component {
  render() {
    return (
      <TouchableHighlight
        style={[styles.button, styles.dropShadow]}
        underlayColor="#FFC107"
        onPress={this.props.onPress}>
        <Text style={styles.buttonText}>{this.props.text}</Text>
      </TouchableHighlight>
    );
  }
}

class NavMenu extends React.Component {
  render() {
    return (
      <ScrollView
        contentContainerStyle={styles.initialscreen}
        style={styles.scrollContainer}
        >
      
 		
      
           <NavButton
           onPress={() => {
             this.props.navigator.push({ id: 'WP' });
           }}
           text='WordPress'
         />


        <NavButton
          onPress={() => {
            this.props.navigator.push({ id: 'ide' });
          }}
          text='ide'
        />
        <NavButton
          onPress={() => {
            this.props.navigator.push({ id: 'login' });
          }}
          text={strings._log_in}
        />
        <NavButton
          onPress={() => {
            this.props.navigator.push({ id: 'createUser' });
          }}
          text={strings._create_account}
        />
        <NavButton
          onPress={() => {
            this.props.navigator.push({ id: 'messenger' });
          }}
          text={'messenger'}
        />
        <NavButton
          onPress={() => {
            this.props.navigator.push({ id: 'IgniteTemplate' });
          }}
          text={'IgniteTemplate'}
        />
      </ScrollView>
    );
  }
}
var {
  width,
  height
} = Dimensions.get('window');

var Navigation = React.createClass({

  componentWillMount(props) {
      this.state = stateData;
      // this.state = props.store.state
      // console.log('init Navigator with state: ' +
		// JSON.stringify(this.state))
  },

  statics: {
    title: '<Navigator>',
    description: 'JS-implemented navigation',
  },

  renderScene: function(route, nav) {
    switch (route.id) {
    
    
    
 		

    
      case 'messenger':
        return <Messenger store={this.store} navigator={nav} {...route.passProps}  stateData={this.state}/>;

        // TODO: FIX
      case 'media-player':
        return <Messenger store={this.store} navigator={nav} {...route.passProps}  stateData={this.state}/>;

      case 'team-edit':
        return <TeamEdit store={this.store} navigator={nav} {...route.passProps} stateData={this.state}/>;

      case 'user-settings':
        return <UserSettings store={this.store} navigator={nav} {...route.passProps} stateData={this.state}/>;

      case 'cameraview':
          return <CameraView store={this.store} navigator={nav} {...route.passProps} />;

      case 'activities':
        return <Activities store={this.store} navigator={nav} {...route.passProps} />;

      case 'hamburgerMenu':
        return <HamburgerMenu store={this.store} navigator={nav} {...route.passProps} />;

      case 'assessmentDetail':
        return <AssessmentDetail store={this.store} navigator={nav} {...route.passProps} />;

        // TODO: FIX
      case 'mediaDetail':
        return <Messenger store={this.store} navigator={nav} {...route.passProps} />;

      case 'assessments':
        return <Assessments store={this.store} navigator={nav} {...route.passProps} stateData={this.state}/>;

      case 'collections':
        return <Collections store={this.store} navigator={nav} {...route.passProps} stateData={this.state}/>;

      case 'sites':
        return <Sites store={this.store} navigator={nav} {...route.passProps} />;

      case 'objectEdit':
        return <ObjectEdit store={this.store} navigator={nav} {...route.passProps} />;

      case 'siteEdit':
        return <SiteEdit store={this.store} navigator={nav} {...route.passProps} />;

      case 'siteDetail':
        return <SiteDetail store={this.store} navigator={nav} {...route.passProps} />;
      case 'regionview':
        return <RegionView store={this.store} navigator={nav} {...route.passProps} />;
      case 'content':
        return <ContentItemListView  store={this.store}
          url='http://.online' navigator={nav} {...route.passProps} />;
      case 'ide':
          return <WebViewComponent  store={this.store}
            url='http://34.193.150.49/wp-content/uploads/starter_hype.html' title='Help & Settings' navigator={nav} {...route.passProps} />;
      
      case 'help':
        return <WebViewComponent  store={this.store}
          url='http://34.193.150.49/wp-content/uploads/starter_hype.html' title='Help & Settings' navigator={nav} {...route.passProps} />;
     
      case 'ignite':
            return <IgniteTemplate  store={this.store} title='IGNITE' navigator={nav} {...route.passProps} />;
      
      case 'faq':
        return <WebViewComponent  store={this.store}
          url='http://.online/frequently-asked-questions' title='Training' navigator={nav} {...route.passProps} />;
      
      case 'navbar':
        return <NavigationBar  store={this.store} url='' navigator={nav} {...route.passProps} />;
      case 'breadcrumbs':
        return <BreadcrumbNav  store={this.store} navigator={nav} parentNav={nav} {...route.passProps} />;
      case 'WP':
        return <WP store={this.store} navigator={nav} {...route.passProps} requestedType='posts' />;
      case 'about':
        return <About store={this.store} navigator={nav} {...route.passProps} requestedType='about' />;
      case 'resetPassword':
          return <ResetPassword  store={this.store} navigator={nav} {...route.passProps} />;
      case 'createUser':
        return <CreateUser  store={this.store} navigator={nav} {...route.passProps} />;
      case 'IgniteTemplate':
        return <IgniteTemplate  store={this.store} navigator={nav} {...route.passProps} />;
      case 'login':
        return <Login  store={this.store} navigator={nav} {...route.passProps} />;
      case 'maps':
        return <MapViewComponent  store={this.store} navigator={nav} {...route.passProps} />;
      case 'imagepicker':
        return <ImagePicker  store={this.store} navigator={nav} {...route.passProps} />;
      case 'modalcapture':
        return <ModalCapture  store={this.store} navigator={nav} {...route.passProps} />;
      case 'polygonmaps':
        return <PolygonMapViewComponent store={this.store} navigator={nav} {...route.passProps}  />;
      case 'regionmaps':
        return <RegionMapView store={this.store} navigator={nav} {...route.passProps}  />;

      default:
        return (
          <NavMenu
            message={route.message}
            navigator={nav}
            {...route.passProps}
            onExampleExit={this.props.onExampleExit}
          />
        );
    }
  },

  
  render: function() {
    if(false){ // typeof(this.props.username) == 'undefined'){
      return (
        <Login  store={this.store} nav={navigator}/>
      );
    }else{
      return (
        <Navigator
          ref={this._setNavigatorRef}
          style={styles.navigatorContainer}
          initialRoute=
          renderScene={this.renderScene}
          configureScene={(route) => {
            if (route.sceneConfig) {
              return route.sceneConfig;
            }

            /*
			 * Available options are: - Navigator.SceneConfigs.PushFromRight
			 * (default) - Navigator.SceneConfigs.FloatFromRight -
			 * Navigator.SceneConfigs.FloatFromLeft -
			 * Navigator.SceneConfigs.FloatFromBottom -
			 * Navigator.SceneConfigs.FloatFromBottomAndroid -
			 * Navigator.SceneConfigs.FadeAndroid -
			 * Navigator.SceneConfigs.HorizontalSwipeJump -
			 * Navigator.SceneConfigs.HorizontalSwipeJumpFromRight -
			 * Navigator.SceneConfigs.VerticalUpSwipeJump -
			 * Navigator.SceneConfigs.VerticalDownSwipeJump
			 */

            if(route.type === 'modal') {
             return Navigator.SceneConfigs.VerticalDownSwipeJump
            }
            return Navigator.SceneConfigs.HorizontalSwipeJump;
          }}
        />
      );
    }
  },
  

  componentWillUnmount: function() {
    this._listeners && this._listeners.forEach(listener => listener.remove());
  },

  _setNavigatorRef: function(navigator) {
    if (navigator !== this._navigator) {
      this._navigator = navigator;
      if (navigator) {
        var callback = (event) => {
          console.log(
            `Navigation: event ${event.type}`,
            {
              route: JSON.stringify(event.data.route),
              target: event.target,
              type: event.type,
            }
          );
        };
        // Observe focus change events from the owner.
        this._listeners = [
          navigator.navigationContext.addListener('willfocus', callback),
          navigator.navigationContext.addListener('didfocus', callback),
        ];
      }
    }
  },
});

Navigation = connect(state => state)(Navigation);

Navigation.external = true;

module.exports = Navigation;
