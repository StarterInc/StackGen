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

var BackButton = React.createClass({


  render: function() {
    return (

      <Icon
        style={styles.appBarButton}
        size={32}
        name="md-left-arrow"
        onPress={this.props.onPress}
      />

    );
  }
});

module.exports = BackButton;
