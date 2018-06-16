'use strict';

import React, {
  Component,
} from 'react';
import {
  Linking,
  Platform,
  ActionSheetIOS,
  Dimensions,
  View,
  Text,
  Navigator,
} from 'react-native';

import styles from '../AppStyles';
import AppBar from '../components/AppBar';
import strings from '../il8n/il8n'

var GiftedMessenger = require('react-native-gifted-messenger');
var Communications = require('react-native-communications');


var STATUS_BAR_HEIGHT = Navigator.NavigationBar.Styles.General.StatusBarHeight;
if (Platform.OS === 'android') {
  // var ExtraDimensions = require('react-native-extra-dimensions-android');
  var STATUS_BAR_HEIGHT = 20; //  ExtraDimensions.get('STATUS_BAR_HEIGHT');
}


class Messenger extends Component {

  constructor(props) {
    super(props);

    this._isMounted = false;
    this._messages = this.getInitialMessages();

    this.state = {
      messages: this._messages,
      isLoadingEarlierMessages: false,
      typingMessage: '',
      allLoaded: false,
    };

  }

  componentDidMount() {
    this._isMounted = true;

    setTimeout(() => {
      this.setState({
        typingMessage: 'BigDecimal Admin is typing a message...',
      });
    }, 1000); // simulating network

    setTimeout(() => {
      this.setState({
        typingMessage: '',
      });
    }, 3000); // simulating network


    setTimeout(() => {
      this.handleReceive({
        text: 'We also found traces of silver and zinc oxide.',
        name: 'Starter Agent',
        image: {uri: 'http://i0.wp.com/starter.io/wp-content/uploads/2015/05/starter_logo_2015_vertictal_1.png?resize=201%2C300'},
        position: 'left',
        date: new Date(),
        uniqueId: Math.round(Math.random() * 10000), // simulating server-side unique id generation
      });
    }, 3300); // simulating network


    setTimeout(() => {
      this.setState({
        typingMessage: 'Indiana Jones is typing a message...',
      });
    }, 4000); // simulating network

    setTimeout(() => {
      this.setState({
        typingMessage: '',
      });
    }, 4500); // simulating network


    setTimeout(() => {
      this.handleReceive({
        text: "My contact is awaiting further instructions",
        name: 'Pence Hamilton',
        image: {uri: 'https://images.duckduckgo.com/iu/?u=http%3A%2F%2Fscout.rustedmagick.com%2Fsg1.gif&f=1'},
        position: 'right',
        date: new Date(2016, 3, 14, 13, 1),
        uniqueId: Math.round(Math.random() * 10000), // simulating server-side unique id generation
      });
    }, 5000); // simulating network


    setTimeout(() => {
      this.setState({
        typingMessage: 'Farfeg Newton is typing a message...',
      });
    }, 6000); // simulating network

    setTimeout(() => {
      this.setState({
        typingMessage: '',
      });
    }, 7500); // simulating network

    setTimeout(() => {
      this.handleReceive({
        text: "While we wait for analysis, we can coordinate with Atlas One.",
        name: 'Indiana Jones',
        image: {uri: 'https://images.duckduckgo.com/iu/?u=http%3A%2F%2Fscout.rustedmagick.com%2Fsg1.gif&f=1'},
        position: 'right',
        date: new Date(2016, 3, 14, 13, 1),
        uniqueId: Math.round(Math.random() * 10000), // simulating server-side unique id generation
      });
    }, 8000); // simulating network


    setTimeout(() => {
      this.setState({
        typingMessage: 'Spencer Reed is typing a message...',
      });
    }, 6000); // simulating network

    setTimeout(() => {
      this.setState({
        typingMessage: '',
      });
    }, 7500); // simulating network


    setTimeout(() => {
      this.handleReceive({
            text: "Time to celebrate?!",
            name: 'Max Headroom',
            image: {uri: 'http://.online/wp-content/uploads/2016/07/iu.jpeg'},
            position: 'left',
            date: new Date(2016, 3, 14, 13, 1),
            uniqueId: Math.round(Math.random() * 10000), // simulating server-side unique id generation
          });
        }, 8000); // simulating network

  }

  componentWillUnmount() {
    this._isMounted = false;
  }

  getInitialMessages() {
    return [
      {
        text: 'Were you successful in recovering any artifacts?',
        name: 'Chief Inspector',
        image: {uri: 'https://facebook.github.io/react/img/logo_og.png'},
        position: 'left',
        date: new Date(2016, 3, 14, 13, 0),
        uniqueId: Math.round(Math.random() * 10000), // simulating server-side unique id generation
      },
      {
        text: "Yes, I recovered the golden statue!",
        name: 'Indiana Jones',
        image: {uri: 'https://images.duckduckgo.com/iu/?u=http%3A%2F%2Fscout.rustedmagick.com%2Fsg1.gif&f=1'},
        position: 'right',
        date: new Date(2016, 3, 14, 13, 1),
        uniqueId: Math.round(Math.random() * 10000), // simulating server-side unique id generation
      },
    ];
  }

  setMessageStatus(uniqueId, status) {
    let messages = [];
    let found = false;

    for (let i = 0; i < this._messages.length; i++) {
      if (this._messages[i].uniqueId === uniqueId) {
        let clone = Object.assign({}, this._messages[i]);
        clone.status = status;
        messages.push(clone);
        found = true;
      } else {
        messages.push(this._messages[i]);
      }
    }

    if (found === true) {
      this.setMessages(messages);
    }
  }

  setMessages(messages) {
    this._messages = messages;

    // append the message
    this.setState({
      messages: messages,
    });
  }

  handleSend(message = {}) {

    // Your logic here
    // Send message.text to your server

    message.uniqueId = Math.round(Math.random() * 10000); // simulating server-side unique id generation
    this.setMessages(this._messages.concat(message));

    // mark the sent message as Seen
    setTimeout(() => {
      this.setMessageStatus(message.uniqueId, 'Seen'); // here you can replace 'Seen' by any string you want
    }, 1000);

    // if you couldn't send the message to your server :
    // this.setMessageStatus(message.uniqueId, 'ErrorButton');
  }

  onLoadEarlierMessages() {

    // display a loader until you retrieve the messages from your server
    this.setState({
      isLoadingEarlierMessages: true,
    });

    // Your logic here
    // Eg: Retrieve old messages from your server

    // IMPORTANT
    // Oldest messages have to be at the begining of the array
    var earlierMessages = [
      {
        text: 'React Native enables you to build world-class application experiences on native platforms using a consistent developer experience based on JavaScript and React. https://github.com/facebook/react-native',
        name: 'BigDecimal Admin',
        image: {uri: 'https://facebook.github.io/react/img/logo_og.png'},
        position: 'left',
        date: new Date(2016, 0, 1, 20, 0),
        uniqueId: Math.round(Math.random() * 10000), // simulating server-side unique id generation
      },
      {
        text: 'This is a touchable phone number 0606060606 parsed by taskrabbit/react-native-parsed-text',
        name: 'Awesome Developer',
        image: {uri: 'https://images.duckduckgo.com/iu/?u=http%3A%2F%2Fscout.rustedmagick.com%2Fsg1.gif&f=1'},
        position: 'right',
        date: new Date(2016, 0, 2, 12, 0),
        uniqueId: Math.round(Math.random() * 10000), // simulating server-side unique id generation
      },
    ];

    setTimeout(() => {
      this.setMessages(earlierMessages.concat(this._messages)); // prepend the earlier messages to your list
      this.setState({
        isLoadingEarlierMessages: false, // hide the loader
        allLoaded: true, // hide the `Load earlier messages` button
      });
    }, 1000); // simulating network

  }

  handleReceive(message = {}) {
    // make sure that your message contains :
    // text, name, image, position: 'left', date, uniqueId
    this.setMessages(this._messages.concat(message));
  }

  onErrorButtonPress(message = {}) {
    // Your logic here
    // re-send the failed message

    // remove the status
    this.setMessageStatus(message.uniqueId, '');
  }

  // will be triggered when the Image of a row is touched
  onImagePress(message = {}) {
    // Your logic here
    // Eg: Navigate to the user profile
  }

  
  render() {
    return (
      <View style={[styles.container, {flex: 1, flexDirection: 'column', justifyContent: 'space-between'}]}>

      <GiftedMessenger
        ref={(c) => this._GiftedMessenger = c}

        styles={{
          bubbleRight: {
            marginLeft: 70,
            backgroundColor: '#007aff',
          },
        }}

        autoFocus={!false}
        messages={this.state.messages}
        handleSend={this.handleSend.bind(this)}
        onErrorButtonPress={this.onErrorButtonPress.bind(this)}
        maxHeight={Dimensions.get('window').height - Navigator.NavigationBar.Styles.General.NavBarHeight - STATUS_BAR_HEIGHT}

        loadEarlierMessagesButton={!this.state.allLoaded}
        onLoadEarlierMessages={this.onLoadEarlierMessages.bind(this)}

        senderName='McMayhem'
        senderImage={null}
        onImagePress={this.onImagePress}
        displayNames={true}

        parseText={true} // enable handlePhonePress, handleUrlPress and handleEmailPress
        handlePhonePress={this.handlePhonePress}
        handleUrlPress={this.handleUrlPress}
        handleEmailPress={this.handleEmailPress}

        isLoadingEarlierMessages={this.state.isLoadingEarlierMessages}

        typingMessage={this.state.typingMessage}
      />

      </View>
    );
  }
  

  handleUrlPress(url) {
    Linking.openURL(url);
  }

  // TODO
  // make this compatible with Android
  handlePhonePress(phone) {
    if (Platform.OS !== 'android') {
      var BUTTONS = [
        'Text message',
        'Call',
        'Cancel',
      ];
      var CANCEL_INDEX = 2;

      ActionSheetIOS.showActionSheetWithOptions({
        options: BUTTONS,
        cancelButtonIndex: CANCEL_INDEX
      },
      (buttonIndex) => {
        switch (buttonIndex) {
          case 0:
            Communications.phonecall(phone, true);
            break;
          case 1:
            Communications.text(phone);
            break;
        }
      });
    }
  }

  handleEmailPress(email) {
    Communications.email(email, null, null, null, null);
  }

}


module.exports = Messenger;
