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


  render: function() {
    return (
      <Icon
        style={styles.appBarButton}
        size={32}
        name="ios-close-circle-outline"
        onPress={this.props.onPress}
      />
    );
  }
});

module.exports = CloseButton;
