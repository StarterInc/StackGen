'use strict';

import React, {Component} from 'react';

import {
  StyleSheet,
  Text,
  TextInput,
  TouchableWithoutFeedback,
  TouchableOpacity,
  View,
  WebView,
} from 'react-native';

import styles from '../AppStyles';
import AppBar from '../components/AppBar';
import Icon from 'react-native-vector-icons/FontAwesome';

var HEADER = '#3b5998';
var BGWASH = 'rgba(255,255,255,0.8)';
var DISABLED_WASH = 'rgba(255,255,255,0.25)';

var TEXT_INPUT_REF = 'urlInput';
var WEBVIEW_REF = 'webview';
var DEFAULT_URL = 'http://tumult.com/hype/gallery/ProfileBadge/ProfileBadge.html';

var WebViewComponent = React.createClass({

  getInitialState: function() {
    if(this.props.url != null){
      DEFAULT_URL = this.props.url;
    }

    return {
      url:  DEFAULT_URL,
      status: 'No Page Loaded',
      backButtonEnabled: false,
      forwardButtonEnabled: false,
      loading: true,
      scalesPageToFit: true,
    };
  },

  inputText: '',

  handleTextInputChange: function(event) {
    var url = event.nativeEvent.text;
    if (!/^[a-zA-Z-_]+:/.test(url)) {
      url = 'http://' + url;
    }
    this.inputText = url;
  },

  renderCloseButton() {
    return (
      <TouchableOpacity
        onPress={this.close}
        style={styles.closeButton}>
          <Text style={[fontWeight: 'bold', fontSize: 20 ]}>X</Text>
      </TouchableOpacity>
    );
  },


  render: function() {
    this.inputText = this.state.url;

    return (
      <View style={[styles.container, {flex: 1, flexDirection: 'column', justifyContent: 'space-between'}]}>
      <AppBar title={this.props.title} navigator={this.props.navigator}/>
      <View style={[styles.webViewStyle]}>

        <View style={[styles.addressBarRow]}>

          <TouchableOpacity
            onPress={this.goBack}
            style={this.state.backButtonEnabled ? styles.navButton : styles.disabledButton}>
            <Text>
               {'<'}
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={this.goForward}
            style={this.state.forwardButtonEnabled ? styles.navButton : styles.disabledButton}>
            <Text>
              {'>'}
            </Text>
          </TouchableOpacity>
          <TextInput
            ref={TEXT_INPUT_REF}
            autoCapitalize="none"
            defaultValue={this.state.url}
            onSubmitEditing={this.onSubmitEditing}
            onChange={this.handleTextInputChange}
            clearButtonMode="while-editing"
            style={styles.addressBarTextInput}
          />
          <TouchableOpacity onPress={this.pressGoButton}>
            <View style={styles.goButton}>
              <Text>
                 Go!
              </Text>
            </View>
          </TouchableOpacity>
        </View>
        <WebView
          userAgent={"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/602.3.12 (KHTML, like Gecko) Version/10.0.2 Safari/602.3.12"}
          ref={WEBVIEW_REF}
          automaticallyAdjustContentInsets={false}
          style={styles.webView}
          // source=
          javaScriptEnabled={true}
          domStorageEnabled={true}
          decelerationRate="normal"
          onNavigationStateChange={this.onNavigationStateChange}
          onShouldStartLoadWithRequest={this.onShouldStartLoadWithRequest}
          startInLoadingState={true}
          scalesPageToFit={this.state.scalesPageToFit}
        >
        </WebView>
        {Component && this.renderCloseButton()}
        <View style={styles.statusBar}>
          <Text style={styles.statusBarText}>{this.state.status}</Text>
        </View>
      </View>
      </View>
    );
  },


  close: function(){
    this.props.navigator.pop();
  },

  goBack: function() {
    this.refs[WEBVIEW_REF].goBack();
  },

  goForward: function() {
    this.refs[WEBVIEW_REF].goForward();
  },

  reload: function() {
    this.refs[WEBVIEW_REF].reload();
  },

  onShouldStartLoadWithRequest: function(event) {
    // Implement any custom loading logic here, don't forget to return!
    return true;
  },

  onNavigationStateChange: function(navState) {
    this.setState({
      backButtonEnabled: navState.canGoBack,
      forwardButtonEnabled: navState.canGoForward,
      url: navState.url,
      status: navState.title,
      loading: navState.loading,
      scalesPageToFit: true
    });
  },

  onSubmitEditing: function(event) {
    this.pressGoButton();
  },

  pressGoButton: function() {
    var url = this.inputText.toLowerCase();
    if (url === this.state.url) {
      this.reload();
    } else {
      this.setState({
        url: url,
      });
    }
    // dismiss keyboard
    this.refs[TEXT_INPUT_REF].blur();
  },

});

var WebViewButton = React.createClass({
  _handlePress: function() {
    if (this.props.enabled !== false && this.props.onPress) {
      this.props.onPress();
    }
  },
  render: function() {
    return (
      <TouchableWithoutFeedback onPress={this._handlePress}>
        <View style={[styles.webViewButton, this.props.enabled ? {} : styles.buttonDisabled]}>
          <Text style={styles.buttonText}>{this.props.text}</Text>
        </View>
      </TouchableWithoutFeedback>
    );
  }
});



var ScaledWebView = React.createClass({

  getInitialState: function() {
    return {
      scalingEnabled: true,
    }
  },

  
  render: function() {
    return (
      <View style={{flex: 1, flexDirection: 'column', justifyContent: 'space-between'}}>
        <WebView
          source={{uri: 'http://{{appname}}.online/'}}   
          scalesPageToFit={this.state.scalingEnabled}
        />
        <View style={styles.buttons}>
        { this.state.scalingEnabled ?
          <WebViewButton
            text="Scaling:ON"
            enabled={true}
            onPress={() => this.setState({scalingEnabled: false})}
          /> :
          <WebViewButton
            text="Scaling:OFF"
            enabled={true}
            onPress={() => this.setState({scalingEnabled: true})}
          /> }
        </View>
      </View>
    );
   },
  }
  
)

const HTML = `
<!DOCTYPE html>\n
<html>
  <head>
    <title>Hello Static World</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=320, user-scalable=no">
    <style type="text/css">
      body {
        margin: 0;
        padding: 0;
        font: 62.5% arial, sans-serif;
        background: #ccc;
      }
      h1 {
        padding: 45px;
        margin: 0;
        text-align: center;
        color: #33f;
      }
    </style>
  </head>
  <body>
    <h1>Hello Static World</h1>
  </body>
</html>
`;
/*
 * exports.examples = [ { title: 'Simple Browser', render(): ReactElement {
 * return <WebViewExample />; } }, { title: 'Scale Page to Fit', render():
 * ReactElement { return <ScaledWebView/>; } }, { title: 'Bundled HTML',
 * render(): ReactElement { return ( <WebView style= source={require('./helloworld.html')} scalesPageToFit={true} /> ); } }, {
 * title: 'Static HTML', render(): ReactElement { return ( <WebView style= source=
 * scalesPageToFit={true} /> ); } }, { title: 'POST Test', render():
 * ReactElement { return ( <WebView style= source= scalesPageToFit={false} /> ); } } ];
 */

module.exports = WebViewComponent;
