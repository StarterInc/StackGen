"use strict"

import React, {Component} from 'react';

import {
  ScrollView,
  PropTypes,
  StyleSheet,
  Text,
  TextInput,
  TouchableHighlight,
  View,
} from 'react-native'

// codepush!!
import CodePush from "react-native-code-push";

import About from '../screens/About'
import NextButton from '../components/NextButton'
// http://ionicframework.com/docs/v2/ionicons/
import Icon from 'react-native-vector-icons/Ionicons';

import GLOBAL from '../Global'
import styles from '../AppStyles'
import strings from '../../il8n/il8n'

/**
* The form processing component
*/
import t from 'tcomb-form-native';
var Form = t.form.Form;

// here we are: define your domain model
var User = t.struct({
  username: t.String,              // a required string
  password: t.String,  // an optional string
});

// FORM options
var options = {
  auto: 'placeholders',
  fields: {
    username: {
      // name field configuration here..
      label: strings.bigdecimal_username,
      autoFocus: true,
      placeholder: strings.bigdecimal_name_placeholder,
      //onBlur: () => {
        // {(event) => this.state.name = event.nativeEvent.text}
        // console.log('onBlur: ' + this.state.name);
      //}
    },
    password:{
      label: strings.bigdecimal_password,
    },
  },
  i18n: {
    optional: strings.bigdecimal_optional,   // suffix added to optional fields
    required: strings.bigdecimal_required,               // suffix added to required fields
  }
}; // optional rendering options (see documentation)
// END FORM SCHEMA


// REDUX
// connect actions to controls
import { connect } from 'react-redux'

import {
  logoutUser,
  submitResetPassword,
  submitLogin,
  receiveUser
} from '../state/UserAction'

import {
  submitAppStatus
} from '../state/AppStatusAction'

//Your basic login screen
class Login extends Component {

  componentWillMount(){
  // TODO: use for testing:
  // this.props.dispatch(receiveUser(this.props.username,this.props.userInfo))
  }

  loadStatus(){
      this.props.dispatch(submitAppStatus())
  }

  loadAssessments(){
  }

  updateApp(){
    CodePush.sync({installMode: CodePush.InstallMode.ON_NEXT_RESUME});
  }

  loadActivities(){

  }
 render() {
   // const { lastUpdated } = this.props
   const { username,  password, fetching, lastUpdated, userInfo, appStatus } = this.props

   if(typeof(userInfo.preferences) !== 'undefined'){  // user is logged in

     return null

   }else if(!false){ // TODO: enable the 'control panel'
     return(
       <View style={[styles.formContainer,{paddingTop:10}]}>


       <Text style={styles.titleFormHeader}>{strings.bigdecimal_usersettings}</Text>


         <ScrollView
           contentContainerStyle={styles.initialscreen}
           style={styles.scrollContainer}
           >

          <TouchableHighlight
           style={[styles.button, styles.dropShadow]}
           underlayColor="#FFC107"
           onPress={(this.updateApp.bind(this))}
           >
          <Text>UPDATE APP</Text>
          </TouchableHighlight>

            <TouchableHighlight
             style={[styles.button, styles.dropShadow]}
             underlayColor="#FFC107"
             onPress={(this.loadAssessments.bind(this))}
             >

            <Text>{strings.bigdecimal_assessments}</Text>
            </TouchableHighlight>

            <TouchableHighlight
             style={[styles.button, styles.dropShadow]}
             underlayColor="#FFC107"
             onPress={(this.loadStatus.bind(this))}
             >
            <Text>STATUS</Text>
            </TouchableHighlight>

            <TouchableHighlight
             style={[styles.button, styles.dropShadow]}
             underlayColor="#FFC107"
             onPress={(this.loadActivities.bind(this))}
             >
            <Text>Load Activities</Text>
            </TouchableHighlight>

            <Text style={styles.tiny}>App version: {GLOBAL.MAJOR_VERSION}.{GLOBAL.MINOR_VERSION}</Text>
            <Text style={styles.tiny}>API version: {GLOBAL.API_VERSION}</Text>
            <Text style={styles.tiny}>Server version: {GLOBAL.MAJOR_VERSION}.{GLOBAL.MINOR_VERSION}</Text>
            <Text style={styles.tiny}>Session ID: {GLOBAL.JSESSIONID}</Text>
            <Text style={styles.tiny}>Server Stats: {JSON.stringify(appStatus.entries)}</Text>
          </ScrollView>
         </View>
     )
   }else{
     return (
        <View style={styles.loginContainer}>


        <About />
        <View style={styles.row}>
           <Icon
              style={styles.iconStyle}
              size={40}
              name='md-arrow-back'
              onPress={(this.close.bind(this))}
           />

           <Text style={styles.title}>
               {strings.bigdecimal_login}
           </Text>

          <TouchableHighlight
            onPress={(this.onResetPassword.bind(this))}
          >
          <Text style={styles.title2}>{strings.bigdecimal_reset_password}</Text>
          </TouchableHighlight>


        </View>

          <Text style={styles.tiny}>
              {strings.bigdecimal_username}
           <Text>
           {(typeof(username) !== 'undefined' ? 'Already logged in as: ' + username : '')}
           </Text>
          </Text>

          <TextInput
            placeholder={strings.bigdecimal_username}
            autoCapitalize={'none'}
            autoCorrect={false}
            autoFocus={true}
            onChange={(event) => this.username = event.nativeEvent.text}
            style={styles.formInput}
            value={userInfo.username}
          />

          <Text style={styles.tiny}>
              {strings.bigdecimal_password}
          </Text>

          <TextInput
              placeholder={strings.bigdecimal_password}
              secureTextEntry={true}
              autoCapitalize={'none'}
              autoCorrect={false}
              onChange={(event) => this.password = event.nativeEvent.text}
              style={[styles.dropShadow, styles.formInput]}
             value={userInfo.password}
           />

          <Text
            underlayColor="#FFC107"
            onPress={(this.onCheckServerStatus.bind(this))}
            >
            {JSON.stringify(userInfo.preferences)}
            </Text>

          <NextButton
            underlayColor="#FFC107"
            onPress={(this.onSubmitPressed.bind(this))}
            style={[styles.button, styles.dropShadow]}
            text='Log In'>
          </NextButton>

        </View>
      );
      }
	}

onResetPassword(){

  const { username, password, navigator, dispatch, loggedInUser } = this.props

  navigator.push({
      id: 'resetPassword',
      passProps: {
        username: this.username,
        password: this.password
      },
  });

}

close(){
  this.props.navigator.pop();
}

onCheckServerStatus(){
  const { dispatch } = this.props

// AppStatusAction.js
  dispatch(submitAppStatus());
}

select(state) {
  return state.userInfo.id
}

 onSubmitPressed() {

    const { dispatch, loggedInUser, navigator } = this.props

    //alert("Login submit: " + this.username);

    dispatch(submitLogin(this.username, this.password, navigator))
  }

  showLoginFailed(){
    alert("LOGIN FAILED");
  }

};


// BEGIN REDUX


/*
  Define the parameters used by the store to map state of this component


Login.propTypes = {
  username: PropTypes.string,
  password: PropTypes.string,
  userInfo: PropTypes.object,
  // isAdmin: PropTypes.bool.isRequired,
  // content: PropTypes.array.isRequired,
  fetching: PropTypes.bool.isRequired,
  lastUpdated: PropTypes.number,
  dispatch: PropTypes.func.isRequired
}
*/

function mapStateToProps(state) {

  const { userInfo , appStatus } = state

  const { username , password, fetching, lastUpdated } = userInfo

 // console.log('Login.js calling mapStateToProps userinfo: ' + JSON.stringify(userInfo));

  return {
    username,
    password,
    fetching,
    lastUpdated,
    userInfo,
    appStatus
  }
}

module.exports = connect(mapStateToProps)(Login);
// END REDUX
