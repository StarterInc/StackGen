var React = require("react");
import {
  StyleSheet,
  View,
  PixelRatio,
  TouchableHighlight,
  Image,
  Animated,
  Text,
} from 'react-native';

import Button from './Button'
import UserInfo from './UserInfo'
import styles from '../AppStyles'
import strings from '../../il8n/il8n'

import ModalCapture from './ModalCapture'

// http://ionicframework.com/docs/v2/ionicons/
import Icon from 'react-native-vector-icons/Ionicons';

class AppBar extends React.Component{

  constructor(props: any) {
    super(props);
    this.state = {
      ...this.state,
      showCameraModal: false,
      bounceValue: new Animated.Value(0),
    };
  }

  openHamburgerMenu(e){
    console.log('openHamburgerMenu');
    this.props.navigator.push({
        id: 'hamburgerMenu',
        type:'modal'
    });
  }

  close(){
    this.props.navigator.pop();
  }

  openCameraVideoModal(e){
    this.props.navigator.push({
      id:'modalcapture'
    })
  }
    /*
      source={{uri: 'http://bigdecimal.online/wp-content/uploads/2016/05/background_elevator.png'}}
    */

    /*
    <Animated.Image // Base: Image, Text, View
      // source={{uri: 'http://bigdecimal.online/wp-content/uploads/2016/05/background_elevator.png'}}
      // source={require('../../assets/background.png')}
      style={{
        height:100,
        opacity:1,
        flex: 1,
        transform: [                        // `transform` is an ordered array
          {scale: this.state.bounceValue},  // Map `bounceValue` to `scale`
        ]
      }}
    />
    */

  render(): ReactElement {
  //    const {username, userInfo} = this.state;
      return (
        <View style={styles.toolbar}>

        {(this.props.closebutton ?

          <Icon
            style={styles.appBarButton}
            size={34}
            name="ios-arrow-back-outline"
            onPress={(this.close.bind(this))}
          />
          :
          <Icon
            style={styles.appBarButton}
            size={34}
            name="md-menu"
            onPress={(this.openHamburgerMenu.bind(this))}
          />
        )}

        <View style={{marginTop:10, marginLeft:-50}}>
              <Text style={styles.title}>
                  {this.props.title}
              </Text>
              <Text style={styles.tiny}>
              {this.props.title2}
              </Text>
            </View>

            <View style={{height:20}} />
        </View>
    );
  }

  fadeIn(delay, from = 0) {
    const {anim } = this.state;
    return {
      opacity: anim.interpolate({
        inputRange: [delay, Math.min(delay + 1500, 3000)],
        outputRange: [0, 1],
        extrapolate: 'clamp',
      }),
      transform: [{
        translateY: anim.interpolate({
          inputRange: [delay, Math.min(delay + 1500, 3000)],
          outputRange: [from, 0],
          extrapolate: 'clamp',
        }),
      }],
    };
  }

  componentDidMount() {
      this.state.bounceValue.setValue(1.5);     // Start large
      Animated.spring(                          // Base: spring, decay, timing
        this.state.bounceValue,                 // Animate `bounceValue`
        {
          toValue: 1,                         // Animate to smaller size
          friction: 30,                          // Bouncier spring
        }
       ).start();                                // Start the animation
  }
}

module.exports = AppBar;
