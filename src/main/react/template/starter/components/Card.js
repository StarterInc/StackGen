'use strict';

var React = require('react');

import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity
} from 'react-native';

import ThumbnailImage from '../components/ThumbnailImage';

import styles from '../AppStyles';

// http://ionicframework.com/docs/v2/ionicons/
import Icon from 'react-native-vector-icons/Ionicons';

var Card = React.createClass({
  propTypes: {
    title: React.PropTypes.string,
    description: React.PropTypes.string,
  //  onPress:React.PropTypes.function
  },

  getInitialState: function() {
    return {description: (null: ?string)};
  },

  setThumbNailFromWP: function(src){
    fetch(src)
    .then((json) => console.log("TODO: FIX Card.js WP Thumb: " + JSON.stringify(json)) )
    .catch(e => alert('Card.render could not handle WP thumbnail: ' + e))

  },

  {{=<% %>=}}
  render: function() {
    var style='title';
    if(this.props.smallimage){
      style='titleLight'
    }

    // handle thumbnaiil from wordpress api -- "Featured Image"
    if(typeof(this.props.WPImageSource) != 'undefined'){
      this.setThumbNailFromWP(this.props.WPImageSource)
    }

    var description;
    if (this.props.description) {
      description =
        <Text style={styles.descriptionText}>
          {this.props.description}
        </Text>;
    }
    var image;
    if(typeof(this.props.imageSource) != 'undefined'){

       if(typeof(this.props.avatarImage) != 'undefined'){
         image =
          <ThumbnailImage imageSource={this.props.imageSource} avatarImage={this.props.avatarImage}/>;
       }else{
         image =
          <ThumbnailImage imageSource={this.props.imageSource} smallimage={this.props.smallimage}/>;
       }
    }else if(typeof(this.props.iconStyle) != 'undefined'){
          image =
          <Icon
            style={styles.actionButton}
            size={40}
            onPress={this.props.onPress}
            name={this.props.iconStyle}
          />;
    }else{
      image = <View style={{width:10}} />;
    }

    var onPressLink;
    if(typeof(this.props.onPress)!='undefined'){
      onPressLink = <Icon size={30} style={[styles.appBarButton,{marginTop:20}]} name='ios-arrow-forward-outline' onPress={this.props.onPress} />
    }
    return (
      <TouchableOpacity style={[styles.cardContainer,{alignItems:'flex-start', paddingHorizontal:10}]} onPress={this.props.onPress}>

      { image }

        <View style={styles.contentRow}  onPress={this.props.onPress}>
          <View style={styles.cardRow}  onPress={this.props.onPress}>
            <Text style={[styles.descriptionText,{marginTop:0}]}  onPress={this.props.onPress} >
              {this.props.superTitle}
            </Text>
          </View>
          <Text style={styles.titleText}  onPress={this.props.onPress}>
            {this.props.title}
          </Text>
            <Text style={styles.descriptionText}  onPress={this.props.onPress}>
              {this.props.title2}
            </Text>
            <View style={styles.cardRow}  onPress={this.props.onPress}>
              <Text style={{flex:1, marginTop:5, marginBottom:5  }}  onPress={this.props.onPress}>
                {description}
              </Text>

            </View>

            {this.props.children}

        </View>
          {onPressLink}
      </TouchableOpacity>
    );
  }
  <%={{ }}=%>
});

module.exports = Card;
