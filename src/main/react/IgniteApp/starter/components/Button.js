var React = require("react");
import {
  StyleSheet,
  TouchableHighlight,
  Text,
  View
} from 'react-native';

var HEADER = '#3b5998';
var BGWASH = 'rgba(255,255,255,0.8)';
var DISABLED_WASH = 'rgba(255,255,255,0.25)';
import styles from '../AppStyles';

var Button = React.createClass({

  propTypes: {
    style: View.propTypes.style,
    elementStyle: View.propTypes.style,
  },

  _handlePress: function() {
    if (this.props.enabled !== false && this.props.onPress) {
      this.props.onPress();
    }
  },
  render: function() {
    return (
      <TouchableHighlight
        onPress={this._handlePress}
        underlayColor={styles.colorCallToAction}
        style={[styles.button, styles.dropShadow]}
        >
          <Text style={styles.buttonText}>{this.props.text}</Text>
      </TouchableHighlight>
    );
  }
});

module.exports = Button;
