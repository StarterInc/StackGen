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
import strings from '../../il8n/il8n'

// http://ionicframework.com/docs/v2/ionicons/
import Icon from 'react-native-vector-icons/Ionicons';

// REDUX
// connect actions to controls
import { connect } from 'react-redux'

import {
  submitAddContentItem,
  submitUploadContentItem
} from '../state/ContentAction'

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

/**
* The form processing component
*/
import t from 'tcomb-form-native';
var Form = t.form.Form;

// here we are: define your domain model
var UploadItem = t.struct({
  name: t.String,              // a required string
});

class ImagePicker extends Component {

  constructor(props) {
    super(props);
    this.currentAssessment = this.props.currentAssessment
    this.currentLocation = this.props.currentLocation

    this.state = {
      imageSource: null,
      videoSource: null,
      currentPosition: initialPosition
    };

    // FORM options
    this.options = {
      auto: 'placeholders',
      enablesReturnKeyAutomatically: 'true',

      fields: {
        name: {
          // name field configuration here..
          label: strings.bigdecimal_assessment_name,
          autoFocus: true,
          placeholder: strings.bigdecimal_assessment_name_placeholder,
          error: strings.bigdecimal_assessment_name_form_error,
            returnKeyType: 'next',
           onSubmitEditing: () => this.refs.form.getComponent('description').refs.input.focus(),
          //onBlur: () => {
            // {(event) => this.state.name = event.nativeEvent.text}
            // console.log('onBlur: ' + this.state.name);
          //}
        },
      }
    }
  }

  /*

  Picker Options

  option	iOS	Android	Info
  title	OK	OK	Specify null or empty string to remove the title
  cancelButtonTitle	OK	OK
  takePhotoButtonTitle	OK	OK	Specify null or empty string to remove this button
  chooseFromLibraryButtonTitle	OK	OK	Specify null or empty string to remove this button
  customButtons	OK	OK	An array containing objects with the name and title of buttons
  cameraType	OK	-	'front' or 'back'
  mediaType	OK	OK	'photo', 'video', or 'mixed' on iOS, 'photo' or 'video' on Android
  maxWidth	OK	OK	Photos only
  maxHeight	OK	OK	Photos only
  quality	OK	OK	0 to 1, photos only
  videoQuality	OK	OK	'low', 'medium', or 'high' on iOS, 'low' or 'high' on Android
  durationLimit	OK	OK	Max video recording time, in seconds
  rotation	-	OK	Photos only, 0 to 360 degrees of rotation
  allowsEditing	OK	-	bool - enables built in iOS functionality to resize the image after selection
  noData	OK	OK	If true, disables the base64 data field from being generated (greatly improves performance on large photos)
  storageOptions	OK	OK	If this key is provided, the image will get saved in the Documents directory on iOS, and the Pictures directory on Android (rather than a temporary directory)
  storageOptions.skipBackup	OK	-	If true, the photo will NOT be backed up to iCloud
  storageOptions.path	OK	-	If set, will save image at /Documents/[path] rather than the root
  storageOptions.cameraRoll	OK	-	If true, the cropped photo will be saved to the iOS Camera roll.
  */

  selectPhotoTapped() {
    const options = {
      title: strings.bigdecimal_photo_picker,
      takePhotoButtonTitle: strings.bigdecimal_camera_take_picture,
      cancelPhotoButtonTitle: "Cancel",
      cancelButtonTitle: "Cancel",
      chooseFromLibraryButtonTitle: strings.bigdecimal_camera_select_from_gallery,
      quality: 1,
       maxWidth: 4300,
       maxHeight: 4300,
      //customButtons: [
      //  {name: 'fb', title: 'Choose Photo from Facebook'},
      //],
      // cameraType:'back',
      noData:true, // because we allow upload direct from storage using RNFetchBlob
      storageOptions: {
        skipBackup: false,
        path: 'BigDecimal-media/images/',
        cameraRoll: true,
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
            // https://medium.com/@scottdixon/react-native-creating-a-custom-module-to-upload-camera-roll-images-7a3c26bac309#.pgf83dnl2
            /*if(typeof(response.uri) != 'undefined'){
              ReadImageData.readImage(response.uri, (image) => {
                console.log("REAL IMAGE DATA: " + image); // data!
              });
            }*/
          }
          // console.log(JSON.stringify(source));
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
        title: strings.bigdecimal_camera_video_picker,
        takePhotoButtonTitle: strings.bigdecimal_camera_video_take_video,

        cancelButtonTitle: "Cancel",

        durationLimit:10000,
        mediaType: 'video',
        videoQuality: 'high',
        cameraType:'back',
        noData:true, // because we allow upload direct from storage using RNFetchBlob
        storageOptions: {
          skipBackup: false,
          path: 'BigDecimal-media/videos/',
          cameraRoll: true,
        },
        allowsEditing: true
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
     // ImagePicker Redux hook up progress bar

          if(typeof(this.description) == 'undefined'){
            alert('Please enter a caption')
            return
          }
     //     debugger
          if(this.state.imageSource == null){
            alert('Please select an image')
            return
          }
      const { dispatch, navigator, region } = this.props

      if(this.initialPosition != null){
        this.state.longitude = this.initialPosition.coords.longitude
        this.state.latitude = this.initialPosition.coords.longitude
      }else if(typeof(region) != 'undefined'){
        this.state.latitude = region.latitude
        this.state.longitude = region.longitude
      }

      if(typeof(this.currentAssessment) != 'undefined'){
        this.state.parentId = this.currentAssessment.id
        this.state.parentType = 'resource' // map to the assessment
      }else if( typeof(this.currentLocation) != 'undefined'){
        this.state.parentId = this.currentLocation.id
        this.state.parentType = 'location' // map to the assessment
      }

      // start with the upload of the file
      dispatch(submitUploadContentItem(this.currentAssessment, this.description, this.state, dispatch, navigator))

      // TODO: refresh state data
      this.props.navigator.pop();
      // dispatch(submitAddContentItem(this.description, this.state, dispatch, navigator))
    }

/**
  // video button
  <View style={[styles.column, {}]}>
    <IconButton
      icon="ios-videocam-outline"
      text={strings.bigdecimal_camera_take_video}
      onPress={this.selectVideoTapped.bind(this)}
    />
    { this.state.videoSource &&
      <Text style={styles.tiny}>{this.state.videoSource}</Text>
    }
  </View>
*/

  render() {
    const { currentAssessment } = this.props
    // debugger
    return (
      <View style={[{flex: 1, flexDirection: 'column', justifyContent: 'space-between'}]}>

      {(!this.props.hideAppBar ?

      <View style={styles.toolbar} >

          <Icon
            style={styles.appBarButton}
            size={30}
            name="md-arrow-back"
            onPress={(this.close.bind(this))}
          />

          <Text style={styles.title}>{strings.bigdecimal_camera_media}</Text>

          <View style={{marginRight:20}}/>

      </View>
      :
      null
      )}

      <View style={styles.formContainer}>
      <Text style={styles.titleFormHeader}>
        {strings.bigdecimal_camera_media}
      </Text>
      <TextInput
        placeholder={strings.bigdecimal_camera_description_placeholder}
        autoCapitalize={'none'}
        autoCorrect={true}
        style={[styles.formInput]}
        onChange={(event) => this.description = event.nativeEvent.text}
        defaultValue={this.description}
       />

    <View style={{paddingHorizontal:0}}>

        <Text style={[styles.tinyFNT, { marginBottom:0, alignSelf:'flex-start'}]}>
          {strings.bigdecimal_camera_instructions_online}
        </Text>

          <View style={[styles.row]}>

            <IconButton
              icon="ios-camera-outline"
              text={strings.bigdecimal_camera_take_picture_button}
              onPress={this.selectPhotoTapped.bind(this)}
            />
               { this.state.imageSource === null ? <Text></Text> :
               <Image style={[styles.thumbnailSmall,{marginTop:10}]} source={this.state.imageSource} />
               }
          </View>
        </View>

        <View style={[styles.column, {paddingHorizontal:0, marginTop:0}]}>

          <IconButton
            icon={'ios-cloud-upload-outline'}
            text={strings.bigdecimal_upload_online} // TODO: implement offline save queue
            onPress={(this.onSubmitPressed.bind(this))}
           />

         {(initialPosition !== null ?
           <View style={[styles.column, {margin:20}]}>
           <Text style={styles.microFNT}>Current GPS accuracy: {JSON.stringify(initialPosition.coords.accuracy)}</Text>
           <Text style={styles.microFNT}>Current Latitude: {JSON.stringify(initialPosition.coords.latitude)}</Text>
           <Text style={styles.microFNT}>Current Longitude: {JSON.stringify(initialPosition.coords.longitude)}</Text>
           <Text style={styles.microFNT}>Current Speed: {JSON.stringify(initialPosition.coords.speed)}</Text>
           </View>
         :
           <Text style={[styles.errorText,styles.tinyFNT]}>{strings.bigdecimal_device_no_gps}</Text>
         )}
         </View>
         </View>


       </View>
    );
  }
}

/*
no video for now
<IconButton
  icon="ios-videocam-outline"
  text={strings.bigdecimal_camera_take_video_button}
  onPress={this.selectVideoTapped.bind(this)}
/>
{ this.state.videoSource &&
  <Text style={styles.tiny}>{this.state.videoSource}</Text>
}

*/

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
  const { userInfo, locations, currentAssessment } = state
  const { region } = locations
 // console.log('Login.js calling mapStateToProps userinfo: ' + JSON.stringify(userInfo));

  return {
    // userInfo,
    currentAssessment,
    region
  }
}

module.exports = connect()(ImagePicker);
// END REDUX
