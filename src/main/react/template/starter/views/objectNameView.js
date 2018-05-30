// ./views/{{objectname}}View.js
import React from 'react'

import { 
	Control,
	Field,
	Form,
	Errors,
	DatePickerIOS,
	PickerIOS,
	Switch,
	MapView,
	actions
} from 'react-redux-form/native'

import {
  View,
  Text,
  Button
} from 'react-native'

// import GLOBAL from '../Global'
import styles from '../AppStyles'
import strings from '../il8n/il8n'

// http://ionicframework.com/docs/v2/ionicons/
import Icon from 'react-native-vector-icons/Ionicons';
import IconButton from '../components/IconButton';

// TODO: move validations to a module
const required = (val) => val && val.length;
const maxLength = (len) => (val) => val.length <= len;
const isNumber = (val) => !isNaN(Number(val));

/**
 * Auto-Generated {{objectname}} Data Entry View
 * 
 */
var {{objectname}}:{
	{{#variables}}
		{{variablename}}: '{{variableval}}',
	{{/variables}}
}

// / END {{objectname}}_upper

class {{objectname}}View extends React.Component {

  constructor(props) {
    super(props);
    this.props = props;
	// alert(JSON.stringify(props.stateData))
  }	
  handleSubmit(user) {		
	  // dispatch(actions.submit('user', somePromise));
  }

  {{=<% %>=}}

  render() {
	//
    return (
      <View style={styles.loginContainer}>

      <Text>Update <%objectname%></Text>
        
        <Form model=".<%objectname%>" onSubmit={(vals) => alert(JSON.stringify(vals))}>
        <%#variables%>
        <View style={styles.row}>
			<Text style={styles.titleFormHeader}><%variablename%> : <%variableval%>	</Text>
			<Control.TextInput
			    style={styles.formInput}
				model=".<%variablename%>"  
	        	validators={{
	                required,
	                maxLength: maxLength(15)
	            }}
	        />
			<Errors
				style={styles.formInput}
				model=".<%variablename%>"  
		        show="touched"
		        messages={{
		          required: 'Required',
		          validEmail: 'Invalid <%variablename%> value',
		       }}
		     />
		</View>
		
		<%/variables%>
	        
          <IconButton
	          icon='md-checkmark-circle-outline'
	          text={strings.<%appname%>_camera_take_video}
	          // onPress={this.selectVideoTapped.bind(this)}
           />
          
          </Form>
      </View>
    )
  }

  <%={{ }}=%>
}


// module.exports = connect(mapStateToProps)({{objectname}}View);

module.exports = {{objectname}}View
