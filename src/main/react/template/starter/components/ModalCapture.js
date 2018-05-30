import React from "react";
import ReactNative from "react-native";

const {
  Component
} = React;

const {} = ReactNative;

const {
  ImagePickerManager
} = ReactNative;

import CloseButton from '../components/CloseButton';
import IconButton from '../components/IconButton';
import AddButton from '../components/AddButton';
import styles from '../AppStyles';
import strings from '../il8n/il8n'

// REDUX
// connect actions to controls
import { connect } from 'react-redux'

import {
  submitAddContentItem,
} from '../state/ContentItemAction'


var initialPosition = null;

// get the current location
navigator.geolocation.getCurrentPosition(
  (position) => {
    initialPosition = position;
    console.log(JSON.stringify(initialPosition));
  },
  (error) => console.log('RegionMapView GEOLOCATION: ' + error.message),
  {enableHighAccuracy: true, timeout: 20000, maximumAge: 1000}
);

class ModalCapture extends Component {

  constructor(props) {
    super(props);
    this.state = {
      imageSource: null,
      videoSource: null,
      currentPosition: initialPosition
    };
  }

  selectPhotoTapped() {
    const options = {
      title: 'Photo Picker',
      takePhotoButtonTitle: 'Take Photo...',
      chooseFromLibraryButtonTitle: 'Choose from Library...',
      quality: 1,
  //    maxWidth: 300,
  //    maxHeight: 300,
      storageOptions: {
        skipBackup: !true
      },
      allowsEditing: true
    };

      ImagePickerManager.showImagePicker(options, (response) => {
        console.log('Response = ', response);

        if (response.didCancel) {
          console.log('User cancelled photo picker');
        }
        else if (response.error) {
          console.log('ImagePickerManager Error: ', response.error);
        }
        else if (response.customButton) {
          console.log('User tapped custom button: ', response.customButton);
        }
        else {
          // You can display the image using either:
          //const source = {uri: 'data:image/jpeg;base64,' + response.data, isStatic: true};
          var source;
          if (Platform.OS === 'android') {
            source = {uri: response.uri, isStatic: true};
          } else {
            source = {uri: response.uri.replace('file://', ''), isStatic: true};
          }

      //    console.log(JSON.stringify(source));
          this.setState({
            ...response,
            // data:response.data, // can't here
            imageSource: source
          });
        }
      });
    }

    selectVideoTapped() {
      const options = {
        title: 'Video Picker',
        takePhotoButtonTitle: 'Take Video...',
        mediaType: 'video',
        videoQuality: 'high'
      };

      ImagePickerManager.showImagePicker(options, (response) => {
        console.log('Response = ', response);

        if (response.didCancel) {
          console.log('User cancelled video picker');
        }
        else if (response.error) {
          console.log('ImagePickerManager Error: ', response.error);
        }
        else if (response.customButton) {
          console.log('User tapped custom button: ', response.customButton);
        }
        else {
          this.setState({
            videoSource: response.uri
          });
        }
      });
    }

    close(){
      this.props.navigator.pop();
    }

   onSubmitPressed() {

      const { dispatch, navigator } = this.props

      if(this.initialPosition != null){
        this.state.longitude = this.initialPosition.coords.longitude
        this.state.latitude = this.initialPosition.coords.longitude
      }
      dispatch(submitAddContentItem(this.description, this.state, dispatch, navigator))
    }

   {{=<% %>=}}
  render() {
    return (
      <View style={[styles.containerDark, {flex: 1, flexDirection: 'column', justifyContent: 'space-between'}]}>

      <View style={styles.toolbar} >
          <IconButton
            style={styles.appBarButton}
            size={30}
            icon="md-arrow-back"
            onPress={(this.close.bind(this))}
          />
          <Text style={styles.title}>{strings.{{appname}}_camera_media}</Text>
          <View style={{height:10, width:30}}/>
      </View>


      <Text style={[styles.tiny, {paddingHorizontal:30}]}>{strings.{{appname}}_camera_description}</Text>

      <Image
        source={this.state.imageSource}
        style={styles.imageContainer}>

            <TextInput
              placeholder={strings.{{appname}}_camera_description_placeholder}
              autoCapitalize={'none'}
              autoCorrect={true}
              onChange={(event) => this.description = event.nativeEvent.text}
              style={[styles.dropShadow, styles.formInput]}
              value={this.description}
             />

      </Image>

    <View style={[styles.formContainer, {paddingHorizontal:30, paddingTop:10}]}>

        <Text style={styles.title2}>
          {strings.{{appname}}_camera_instructions_online}
        </Text>

          <View style={[styles.row, {}]}>
              <View style={[styles.column, {}]}>
                <IconButton
                  icon="ios-camera-outline"
                  text={strings.{{appname}}_camera_take_picture}
                  onPress={this.selectPhotoTapped.bind(this)}
                />

                { this.state.imageSource === null ? <Text></Text> :
                  <Image style={[styles.thumbnail,{marginTop:-15}]} source={this.state.imageSource} />
                }
              </View>

              <View style={[styles.column, {}]}>

                <IconButton
                  icon="ios-videocam-outline"
                  text={strings.{{appname}}_camera_take_video}
                  onPress={this.selectVideoTapped.bind(this)}
                />

                { this.state.videoSource &&
                  <Text style={styles.tiny}>{this.state.videoSource}</Text>
                }
              </View>
          </View>
        </View>

        <View style={[styles.column, {paddingHorizontal:30, marginTop:0}]}>
          <AddButton
            text={strings.{{appname}}_upload_online} // TODO: implement offline save queue
            onPress={(this.onSubmitPressed.bind(this))}
           />

         {(initialPosition !== null ?
           <View style={styles.column}>
           <Text style={styles.tinyFNT}>Current GPS accuracy: {JSON.stringify(initialPosition.coords.accuracy)}</Text>
           <Text style={styles.tinyFNT}>Current Latitude: {JSON.stringify(initialPosition.coords.latitude)}</Text>
           <Text style={styles.tinyFNT}>Current Longitude: {JSON.stringify(initialPosition.coords.longitude)}</Text>
           <Text style={styles.tinyFNT}>Current Speed: {JSON.stringify(initialPosition.coords.speed)}</Text>
           </View>
         :
           <Text style={[styles.errorText,styles.tinyFNT]}>{strings.{{appname}}_device_no_gps}</Text>
         )}
         </View>

       </View>
    );
  }
  <%={{ }}=%>
}



// BEGIN REDUX


/*
  Define the parameters used by the store to map state of this component


Login.propTypes = {
  username: PropTypes.string,
  password: PropTypes.string,
  userInfo: PropTypes.object,
  // isAdmin: PropTypes.bool.isRequired,
  // content: PropTypes.array.isRequired,
  fetching: PropTypes.bool.isRequired,
  lastUpdated: PropTypes.number,
  dispatch: PropTypes.func.isRequired
}
*/

function mapStateToProps(state) {
  const { userInfo } = state
 // console.log('Login.js calling mapStateToProps userinfo: ' + JSON.stringify(userInfo));

  return {
    userInfo
  }
}

module.exports = connect()(ModalCapture);
// END REDUX
