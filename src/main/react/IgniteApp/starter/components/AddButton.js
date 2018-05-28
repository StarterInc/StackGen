var React = require("react");
import {
  StyleSheet,
  TouchableOpacity,
  Text,
  View
} from 'react-native';

import styles from '../AppStyles';
// http://ionicframework.com/docs/v2/ionicons/
import Icon from 'react-native-vector-icons/Ionicons';

var AddButton = React.createClass({

  propTypes: {
    onPress: React.PropTypes.func,
  },

  render: function() {
    return (
      <TouchableOpacity style={(styles.row, styles.buttonContainer)}
      onPress={this.props.onPress}
      >
          <Icon
            size={40}
            name="ios-add-outline"
            onPress={this.props.onPress}
          />
          <TouchableOpacity
              onPress={this.props.onPress}
              underlayColor="#FFC107"
          >
          <Text
               style={styles.buttonTextDark}
             >{this.props.text}
            </Text>
        </TouchableOpacity>
      </TouchableOpacity>
    );
  }
});

module.exports = AddButton;
