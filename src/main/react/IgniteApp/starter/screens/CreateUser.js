"use strict";

import styles from '../AppStyles';
import AddButton from '../components/AddButton';
import strings from '../../il8n/il8n'

// connect actions to controls
import { connect } from 'react-redux'

import React, {
    Component,
} from 'react';

import{
    PropTypes,
    StyleSheet,
    Text,
    TextInput,
    TouchableHighlight,
    View,
} from 'react-native';

import IconButton from '../components/IconButton';
import NextButton from '../components/NextButton';
// http://ionicframework.com/docs/v2/ionicons/
import Icon from 'react-native-vector-icons/Ionicons';

var initialPosition = null;

// get the current location
navigator.geolocation.getCurrentPosition(
  (position) => {
    initialPosition = position;
    console.log(JSON.stringify(initialPosition));
  },
  (error) => console.log('RegionMapView GEOLOCATION: ' + error.message),
  {enableHighAccuracy: true, timeout: 20000, maximumAge: 1000}
);

/**
* The form processing component
*/
import t from 'tcomb-form-native';
var Form = t.form.Form;

// here we are: define your domain model
var User = t.struct({
  username: t.String,              // a required string
  email: t.String,  // an optional string
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
    email:{
      label: strings.bigdecimal_useremail,
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

import {
  logoutUser,
  submitCreateUser
} from '../state/UserAction'

//Your basic login screen
class CreateUser extends Component {

  constructor(props) {
    super(props);
  }

  handleCreate(userInfo){
    this.props.dispatch(submitCreateUser(this.username, this.password, this.email, this.props.navigator));
  }

  render() {
    const { username, email, userInfo , fetching } = this.props
    return (

      <View style={styles.loginContainer}>
          <View style={styles.row}>

          <Icon
             style={styles.iconStyle}
             size={40}
             name='md-arrow-back'
             onPress={(this.close.bind(this))}
          />


            <Text style={styles.title}>
                {strings.bigdecimal_create_account}
            </Text>
          </View>
            <Text style={styles.tiny}>
                {strings.bigdecimal_username}
            </Text>
            <TextInput
                placeholder={strings.bigdecimal_username_placeholder}
                autoCapitalize={'none'}
                autoCorrect={false}
                autoFocus={true}
                onChange={(event) => this.username = event.nativeEvent.text}
                style={styles.formInput}
                value={username} />
            <Text style={styles.tiny}>
                {strings.bigdecimal_useremail}
            </Text>
            <TextInput
                placeholder={strings.bigdecimal_email_placeholder}
                autoCapitalize={'none'}
                autoCorrect={false}
                onChange={(event) => this.email = event.nativeEvent.text}
                style={styles.formInput}
                value={email} />

            <Text style={styles.tiny}>
                {strings.bigdecimal_password}
            </Text>
            <TextInput
                placeholder={strings.bigdecimal_password_placeholder}
                secureTextEntry={true}
                autoCapitalize={'none'}
                autoCorrect={false}
                onChange={(event) => this.password = event.nativeEvent.text}
                style={[styles.dropShadow, styles.formInput]}
            />

            <NextButton
              underlayColor="#FFC107"
              onPress={(this.handleCreate.bind(this))}
              text='Create New'>
            </NextButton>

            <TouchableHighlight
              onPress={(this.showFAQLink.bind(this))}
            >
            <Text style={[styles.bottom, styles.title2]}>
                {strings.bigdecimal_help_faq}
            </Text>
            </TouchableHighlight>
          </View>
      );
	}

  showFAQLink(){
    this.props.navigator.push({id:'documentation'});
  }

/*
TODO: implement redux form

            <Form model="userInfo"
              onSubmit={(userInfo) => this.handleSubmit(userInfo)}>

              <Field model="userInfo.username">
              <Text>uSERNAMEXXX</Text>
                <TextInput />
              </Field>


              <Field model="userInfo.password">
                <Text>PasswordXXX</Text>
                  <TextInput />
              </Field>

            </Form>


*/



  close(){
      this.props.navigator.pop();
  }
};

/*
  Define the parameters used by the store to map state of this component
*/

function mapStateToProps(state) {
  return {
    userInfo: state.userInfo,
    fetching: state.fetching
  };
}

module.exports = connect(mapStateToProps)(CreateUser);
