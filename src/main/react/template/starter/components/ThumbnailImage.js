var React = require("react");
import {
  StyleSheet,
  TouchableOpacity,
  Text,
  Dimensions,
  Image,
  View
} from 'react-native';

import styles from '../AppStyles';
var ThumbnailImage = React.createClass({

  openImageDetail: function(img){
    alert(JSON.stringify(img))
  },

  render: function() {
    var placeholder = require('../assets/img-placeholder.png')
    var stx = styles.thumbnail;
    if(this.props.smallimage){
        var stx = styles.thumbnailSmall;
    }else if(this.props.avatarImage){
      var stx = styles.avatar;
    }

    var {imageSource} = this.props
    if(typeof(imageSource.indexOf) !== 'undefined'){
      if(imageSource.indexOf('http') > -1){
        try{
          imageSource = {uri: imageSource}
        }catch(e){
          // normal
        }
      }
    }
      //alert.log(imageSource)

    return (
      <TouchableOpacity
        onPress={this.props.onPress}
    //  onPress={(typeof(this.props.onPress) != 'undefined' ?
    //   this.props.onPress :
    //   this.openImageDetail(imageSource))}
      >
        <Image
          style={stx}
          source={imageSource}
          defaultSource={placeholder}
        />
        <Text>{this.props.description}</Text>
      </TouchableOpacity>
    );
  }
});


module.exports = ThumbnailImage;


// TODO: popup modal image view
var {
  height: deviceHeight
} = Dimensions.get('window');
var TopModal = React.createClass({
  getInitialState: function() {
    return { offset: new Animated.Value(deviceHeight) }
  },
  componentDidMount: function() {
    Animated.timing(this.state.offset, {
      duration: 100,
      toValue: 0
    }).start();
  },
  closeModal: function() {
    Animated.timing(this.state.offset, {
      duration: 100,
      toValue: deviceHeight
    }).start(this.props.closeModal);
  },

  render: function() {
    return (
        <Animated.View style={[styles.modal, styles.flexCenter, {transform: [{translateY: this.state.offset}]}]}>
          <TouchableOpacity onPress={this.closeModal}>
            <Text style={{color: '#FFF'}}>Close Menu</Text>
          </TouchableOpacity>
        </Animated.View>
    )
  }

});
var App = React.createClass({
    render: function() {
      return (
        <View style={styles.flexCenter}>
          <TouchableOpacity onPress={this.props.openModal}>
            <Text>Open Modal</Text>
          </TouchableOpacity>
        </View>
      )
    }
});
var RouteStack = {
  app: {
    component: App
  }
}
var ModalApp = React.createClass({
  getInitialState: function() {
    return {
      modal: false
    };
  },
  renderScene: function(route, navigator) {
    var Component = route.component;
    return (
      <Component openModal={() => this.setState({modal: true})}/>
    )
  },
  render: function() {
    return (
      <View style={styles.container}>
        <Navigator
          initialRoute={RouteStack.app}
          renderScene={this.renderScene}
        />
        {this.state.modal ? <TopModal closeModal={() => this.setState({modal: false}) }/> : null }
      </View>
    );
  }
});
