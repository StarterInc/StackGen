'use strict';

var React = require('react');
var ReactNative = require("react");

var {
  Navigator,
  StyleSheet,
  ScrollView,
  View,
  Text,
  TouchableHighlight,
  TouchableOpacity
} = ReactNative;

// http://ionicframework.com/docs/v2/ionicons/
import Icon from 'react-native-vector-icons/Ionicons';

import styles from '../../AppStyles';
import CloseButton from '../../components/CloseButton';
var AssessmentForm1 = require('../../screens/AssessmentForm1');
var AssessmentDetail = require('../../screens/AssessmentDetail');
var RegionMapView = require('../../screens/RegionMapView');
var Messenger = require('../../screens/Messenger');
var TeamEdit = require('../../screens/TeamEdit');
var UserSettings = require('../../screens/UserSettings');
var AssessmentForm2 = require('../../screens/AssessmentForm2');
var Geolocation = require('Geolocation');
var DDPrimaryHazard = require('../../screens/DDPrimaryHazard');
var DDBuildingUse = require('../../screens/DDBuildingUse');
import strings from '../../../il8n/il8n'

var _getRandomRoute = function() {
  return {
    title: '#' + Math.ceil(Math.random() * 1000),
    id:'random'
  };
};

var initialPosition = null;
// get the current location
navigator.geolocation.getCurrentPosition(
  (position) => {
    initialPosition = position;
    console.log(JSON.stringify(initialPosition));
  },
  (error) => console.error('BreadCrumbNav get GEOLOCATION: ' + error.message),
  {enableHighAccuracy: true, timeout: 20000, maximumAge: 1000}
);

var LATITUDE = 0.0;
var LONGITUDE = 0.0;
var LATITUDE_DELTA = 0.0;
var LONGITUDE_DELTA = 0.0;

// define the state tree
const state = {
  pos:0,
  // screens
  screens:[

    {
      idx:0,
      id: 'assessmentform1',
      title: strings.bigdecimal_new_assessment,
    },{
      idx:1,
      id: 'imagepicker',
      title: strings.bigdecimal_buildings_sites,
    },{
      idx:2,
      id: 'team-edit',
      title: strings.bigdecimal_team_pick,
    },{
      idx:3,
      id: 'regionmapview',
      title: strings.bigdecimal_map_key_map,
    },
   /* {
      idx:4,
      id: 'assessmentDetail',
      title: strings.bigdecimal_assessment_creation_complete
    }*/
  ]
}

var _getPreviousRoute = function() {
//  alert(state.pos);
  if(state.pos >=1){
	   state.pos--
  }
  return state.screens[state.pos]
};

var _getThisRoute = function(route){
  state.pos = route.idx
  let ret = state.screens[state.pos]
  // alert(state.pos + ':' + JSON.stringify(ret))
  return ret;
}

var _getNextRoute = function() {
  // alert(state.pos);
  if(state.pos < state.screens.length-1){
	   state.pos++
  }else{
    state.pos = state.screens.length-1;
  }
  return state.screens[state.pos]
};

class NavButton extends React.Component {
  render() {
    return (
      <TouchableHighlight
        style={styles.button}
        underlayColor={styles.colorCallToAction}
        onPress={this.props.onPress}>
        <Text style={styles.buttonText}>{this.props.text}</Text>
      </TouchableHighlight>
    );
  }
}

class BreadCrumbNavBarTop extends React.Component {
  render() {
    const { navigator } = this.props
    return (
      <Icon
        style={[styles.appBreadcrumbButton]}
        size={42}
        name="ios-arrow-back-outline"
        onPress={() => navigator.push(_getNextRoute())}
      />
      );
    }
}

var BreadcrumbNavWizard = React.createClass({
  componentWillMount: function() {
    this._navBarRouteMapper = {
      leftContentForRoute: function(route, navigator) {
        return (
        (route.idx > 0 ?
          <Icon
            size={48}
            name="ios-arrow-forward-outline"
            onPress={() => navigator.push(_getPreviousRoute())}
          />
          :
          <View style={{height:20}} />
          )
        );
      },
      rightContentForRoute: function(route, navigator) {
        return (
          ((route.idx < state.screens.length-1) && (route.idx > 0)  ?
          <Icon
            style={[styles.appBreadcrumbButton]}
            size={42}
            name="ios-arrow-forward-outline"
            onPress={() => navigator.push(_getNextRoute())}
          />
          :
          <View style={{height:20}} />
          )
        );
      },
      titleContentForRoute: function(route, navigator) {
        return (
            <Text style={[styles.title, styles.row, { flex:1, marginTop:5, marginLeft:15 }]}>{route.title}
            </Text>
        );
      },
      iconForRoute: function(route, navigator) {
        return (
          <TouchableOpacity
            onPress={() => { navigator.popToRoute(_getThisRoute(route))}}
            style={styles.crumbIconPlaceholder}
          >
          <Text style={styles.breadCrumbText}>{route.idx + 1}</Text>
          </TouchableOpacity>
        );
      },
      separatorForRoute: function(route, navigator) {
        return (
          <View
            style={styles.crumbSeparatorPlaceholder}
          />
        );
      }
    };
  },

/*
    <NavButton
      onPress={() => { navigator.immediatelyResetRouteStack([_getRandomRoute(), _getRandomRoute()]); }}
      text="Reset w/ 2 scenes"
    />
*/
renderScene: function(route, nav) {
  state.pos = route.idx // reset idx
  switch (route.id) {

    case 'assessmentDetail':
      return (
        <View style={styles.breadCrumbViewContainer}>
          <BreadCrumbNavBarTop style={styles.breadCrumbNavContainer} navigator={nav} />
          <AssessmentDetail store={this.store} navigator={nav} stateData={state.assessment} {...route.passProps}  />
        </View>
        );

      case 'messenger':
        return (
          <View style={styles.breadCrumbViewContainer}>
            <BreadCrumbNavBarTop style={styles.breadCrumbNavContainer} navigator={nav} />
            <Messenger store={this.store} navigator={nav} stateData={state.assessment} />
            </View>
          );

      case 'team-edit':
        return (
          <View style={styles.breadCrumbViewContainer}>
            <BreadCrumbNavBarTop style={styles.breadCrumbNavContainer} navigator={nav} />
            <TeamEdit store={this.store} navigator={nav} stateData={state.assessment} />
          </View>
        );

      case 'user-settings':
        return (
          <View style={styles.breadCrumbViewContainer}>
            <BreadCrumbNavBarTop style={styles.breadCrumbNavContainer} navigator={nav} />
            <UserSettings store={this.store} navigator={nav} stateData={state.assessment.roles[0]} />
          </View>
        );

      case 'imagepicker':
        return (
          <View style={styles.breadCrumbViewContainer}>
            <BreadCrumbNavBarTop style={styles.breadCrumbNavContainer} navigator={nav} />
            <AssessmentForm2 store={this.store} navigator={nav} stateData={state.assessment} />
          </View>
          );

      case 'regionmapview':
        return (
          <View  style={[styles.mapContainer, {flex: 1, flexDirection: 'column', justifyContent: 'space-between'}]}>
            <BreadCrumbNavBarTop style={styles.breadCrumbNavContainer} navigator={nav} />
            <RegionMapView store={this.store} navigator={navigator} stateData={state.assessment} {...route.passProps} topNav={this.props.navigator} />
          </View>
          );

      case 'dropdown_primary_hazard':
      return (
        <View style={styles.breadCrumbViewContainer}>
          <BreadCrumbNavBarTop style={styles.breadCrumbNavContainer} navigator={nav} />
           <DDPrimaryHazard store={this.store} navigator={nav} {...route.passProps}  />
        </View>
        );

      case 'dropdown_building_use':
        return (
          <View style={styles.breadCrumbViewContainer}>
            <BreadCrumbNavBarTop style={styles.breadCrumbNavContainer} navigator={nav} />
             <DDBuildingUse store={this.store} navigator={nav} {...route.passProps}  />
          </View>
          );

      case 'assessmentform1':
        return (
          <View style={styles.breadCrumbViewContainer}>
            <BreadCrumbNavBarTop style={styles.breadCrumbNavContainer} navigator={nav} />
            <AssessmentForm1 store={this.store} navigator={nav} stateData={state.assessment} />
          </View>
          );
      default:
          return (
            <View style={styles.breadCrumbViewContainer}>
              <BreadCrumbNavBarTop style={styles.breadCrumbNavContainer} navigator={nav} />
              <AssessmentForm1 store={this.store} navigator={nav} stateData={state.assessment} />
            </View>
            );
      }
    },

  _renderScene: function(route, nav) {
    return (
      {}
    );
  },

  render: function() {
    return (
      <Navigator
        ref='nav'
        initialRoute={
          state.screens[0]
        }
        renderScene={this.renderScene}
        configureScene={(route) => {
          if (route.sceneConfig) {
            return route.sceneConfig;
          }
          /*
           * Available options are:
           *
           *  - Navigator.SceneConfigs.PushFromRight (default)
           *  - Navigator.SceneConfigs.FloatFromRight
           *  - Navigator.SceneConfigs.FloatFromLeft
           *  - Navigator.SceneConfigs.FloatFromBottom
           *  - Navigator.SceneConfigs.FloatFromBottomAndroid
           *  - Navigator.SceneConfigs.FadeAndroid
           *  - Navigator.SceneConfigs.HorizontalSwipeJump
           *  - Navigator.SceneConfigs.HorizontalSwipeJumpFromRight
           *  - Navigator.SceneConfigs.VerticalUpSwipeJump
           *  - Navigator.SceneConfigs.VerticalDownSwipeJump
           */
          return Navigator.SceneConfigs.HorizontalSwipeJumpFromRight;
        }}
        navigationBar={
          <Navigator.BreadcrumbNavigationBar
            style={styles.breadCrumbNavContainer}
            routeMapper={this._navBarRouteMapper}
          />
        }
      />
    );
  },
});

module.exports = BreadcrumbNavWizard;
