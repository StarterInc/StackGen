var React = require("react");
import {
  StyleSheet,
  TouchableHighlight,
  Text,
  Image,
  View
} from 'react-native';

import styles from '../AppStyles';

import Card from '../components/Card';

var UserInfo = React.createClass({

  render: function() {
    return (
      <View style={styles.row}>
        <Card
          ref= {this.props.key}
          key= {this.props.key}
          title={this.props.username}
          title2={this.props.useremail}
          superTitle={this.props.inviteStatus}
          // imageSource={this.props.imageSource}
          // avatarImage={true}
          iconStyle='ios-contacts-outline'
          onPress={this.props.onPress}
        >
        </Card>
      </View>
    );
  }
});

module.exports = UserInfo;
