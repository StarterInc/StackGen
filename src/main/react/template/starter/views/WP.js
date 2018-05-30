/**
 * WP App v 0.2
 */
'use strict';

import React, {Component} from 'react';

import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  WebView,
  ScrollView,
  TouchableHighlight,
} from 'react-native';

import strings from '../il8n/il8n'
import styles from '../AppStyles';

import WPCard from '../components/WPCard';

import AppBar from '../components/AppBar';

// The URL for the `posts` endpoint provided by WP JSON API
var WP_HOST = 'http://starter.io/'

var REQUEST_URL = WP_HOST + 'wp-json/wp/v2/posts/?filter[category_name]=Mobile%20Accessible&_embed';

// '?filter[orderby]=rand&filter[per_page]=1';

var WP = React.createClass({
  getInitialState: function() {
    return {
      //article is initially set to null so that the loading message shows
      articles: null,
    };
  },

  // Automatically called by react when this component has finished mounting.
  componentDidMount: function() {
    this.fetchData();
  },

  // This is where the magic happens! Fetches the data from our API and updates the application state.
  fetchData: function() {

    this.setState({
      // we'll also set article to null when loading new articles so that the loading message shows
      articles: null,
    });

    var request = new Request(REQUEST_URL, {
      method: 'GET',
     // mode: 'cors',
     // redirect: 'follow',
      // body: reqStr
    });

    fetch(request)
      .then((response) => response.json())
      .then((responseData) => {

        // this.setState() will cause the new data to be applied to the UI that is created by the `render` function below
        this.setState({
          articles: responseData
        });
        console.log(JSON.stringify(this.state.articles));
      })
      .done();
  },

  // instead of immediately rendering the template, we now check if there is data in the 'article' variable
  // and render a loading view if it's empty, or the 'article' template if there is data
  render: function() {
    if ( !this.state.articles ) {
      return this.renderLoadingView();
    }
    return this.renderArticles();
  },
  // the loading view template just shows the message "Loading..."
  renderLoadingView: function() {
    return (
      <View style={styles.container}>
        <Text>
          Loading...
        </Text>
      </View>
    );
  },
  
  {{=<% %>=}}
  // this is the original render function, now renamed to renderArticles, which will render our main template
  renderArticles: function() {
    return (
      <View style={[styles.container, {flex: 1, flexDirection: 'column', justifyContent: 'space-between'}]}>

      <AppBar title={strings.<%appname%>_help_training} navigator={this.props.navigator} closebutton={true}/>

        <ScrollView
          horizontal={false}
          decelerationRate={10}
          pagingEnabled={false}
          contentInset={{bottom: 300}}
          // snapToInterval={GLOBAL.SCREEN_WIDTH*0.5}
           snapToAlignment="start"
          contentContainerStyle={styles.wrapper}
          >
            {this.state.articles.map(article => (
              <WPCard
                ref= {article.id}
                key= {article.id}
                title = {article.title.rendered}
                // title2 = {article.excerpt.plaintext}
              //  description={JSON.stringify(article._embedded)}
            //     superTitle={article._embedded.author.name}
                WPImageSource={WP_HOST + '/wp-json/wp/v2/media/' + article.featured_media}
                // imageSource={'http://{{appname}}.online/wp-json/wp/v2/media/' + article.featured_media}
                // description={assessment.description}
                // onPress={(this.openSiteDetail.bind(this))}
              >

              <WebView
                ref={article.id + '_web'}
                automaticallyAdjustContentInsets={false}
                style={[styles.container,{flex:1, marginTop:-40,height:200, paddingHorizontal:20}]}
                // onNavigationStateChange='null'
                source={{html:'<html><body style="font-family:helvetica; font-size:12;">' + article.content.rendered + '</body></html>'}}
                // startInLoadingState={true}
                scalesPageToFit={this.state.scalesPageToFit}
              >
              </WebView>
              </WPCard>
            ))}
        </ScrollView>

      </View>
    );
  }
  <%={{ }}=%>
});

var Dimensions = require('Dimensions');
var windowSize = Dimensions.get('window');

module.exports = WP  // connect(mapStateToProps)(Login);
