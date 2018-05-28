"use strict";

var React = require("react");
import NextButton from '../components/NextButton';

import strings from '../../il8n/il8n'
import styles from '../AppStyles';

// connect actions to controls
import { connect } from 'react-redux'
import {
  submitResetPassword,
} from '../state/UserAction'

import {
    Component,
    PropTypes,
    StyleSheet,
    Text,
    TextInput,
    TouchableHighlight,
    View,
} from 'react-native';

//Your basic login screen
class ResetPassword extends Component {

  constructor(props) {
      super(props);
     // console.log('ResetPassword state: ' + JSON.stringify(props));
  }

 render() {

   const { lastUpdated , username , password } = this.props

   // alert(username)

   return (
     <View style={styles.loginContainer}>

          <TouchableHighlight  >
          <Text style={ styles.right }>

          </Text>
          </TouchableHighlight>
          <TouchableHighlight onPress={(this.close.bind(this))}>
            <Text style={styles.tiny}>
            {({username} === null ? '<' : username)}
            </Text>
            </TouchableHighlight>


            <Text style={styles.title}>
                Reset Password for <Text style={styles.title}>
                {username}
                </Text>
            </Text>

            <Text style={styles.tiny}>
                PASSWORD
            </Text>
            <TextInput
                placeholder="Password"
                secureTextEntry={true}
                style={[styles.dropShadow, styles.formInput]}
                value={password} />

            <NextButton
              underlayColor="#FFC107"
              onPress={(this.onSubmitPressed.bind(this))} style={[styles.button, styles.dropShadow]}

              // onClick={() => onSubmitPressed(this.state.username, this.state.password)}
//              onPress={(this.onSubmitPressed.bind(this))}
              text='Log In'>
            </NextButton>
          </View>
      );
	}

  close(){
    this.props.navigator.pop();
  }

 onSubmitPressed() {
      const { dispatch, navigator, username, password } = this.props
      dispatch(submitResetPassword(username, this.state.password))
      //  console.log('ResetPassword state:' + JSON.stringify(this.state));
      //   var systemStats = Network.getSystemStats();
      //  console.log("username: " + this.state.username + " password: " + this.state.password);
       navigator.pop();
    }


};

/*
  Define the parameters used by the store to map state of this component


ResetPassword.propTypes = {
  username: PropTypes.string,
  password: PropTypes.string,

// posts: PropTypes.array.isRequired,
  isFetching: PropTypes.bool.isRequired,
  lastUpdated: PropTypes.number,

  dispatch: PropTypes.func.isRequired
}
*/

module.exports = (ResetPassword);
