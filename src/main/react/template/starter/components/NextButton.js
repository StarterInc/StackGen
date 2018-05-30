var React = require("react");
import {
  StyleSheet,
  TouchableHighlight,
  Text,
  View
} from 'react-native';

import styles from '../AppStyles';
// http://ionicframework.com/docs/v2/ionicons/
import Icon from 'react-native-vector-icons/Ionicons';


var CloseButton = React.createClass({

  propTypes: {
    onPress: React.PropTypes.func,
  },

  render: function() {
    return (

      <TouchableHighlight style={styles.nextButton}
        onPress={this.props.onPress}
      >
          <Icon
            size={48}
            name="ios-arrow-forward-outline"
            onPress={this.props.onPress}
          />
      </TouchableHighlight>
    );
  }
});

module.exports = CloseButton;
