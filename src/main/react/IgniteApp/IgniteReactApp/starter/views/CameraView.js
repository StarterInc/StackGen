import React from "react";
import ReactNative from "react-native";

const {} = React;

const {} = ReactNative;

const {
  CameraViewManager
} = ReactNative;

const {
  CameraViewManager
} = ReactNative;

const {
  CameraViewManager
} = ReactNative;

const {
  CameraViewManager
} = ReactNative;

const {
  CameraViewManager
} = ReactNative;

const {
  CameraViewManager
} = ReactNative;

const {
  CameraViewManager
} = ReactNative;

import IconButton from '../components/IconButton';

class CameraView extends React.Component {

  constructor(props: any) {
    super(props);
    props = props;
//    console.log(props.navigator);
  }

  state = {
    avatarSource: null,
    videoSource: null
  };

  selectPhotoTapped() {
    const options = {
      title: 'Photo Picker',
      takePhotoButtonTitle: 'Take Photo...',
      chooseFromLibraryButtonTitle: 'Choose from Library...',
      quality: 0.5,
      maxWidth: 300,
      maxHeight: 300,
      storageOptions: {
        skipBackup: true
      },
      allowsEditing: true
    };

    CameraViewManager.showCameraView(options, (response) => {
      console.log('Response = ', response);

      if (response.didCancel) {
        console.log('User cancelled photo picker');
      }
      else if (response.error) {
        console.log('CameraViewManager Error: ', response.error);
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

        console.log(JSON.stringify(source));
        this.setState({
          avatarSource: source
        });
      }
    });
  }

  selectVideoTapped() {
    const options = {
      title: 'Video Picker',
      takePhotoButtonTitle: 'Take Video...',
      mediaType: 'video',
      videoQuality: 'medium'
    };

    CameraViewManager.showCameraView(options, (response) => {
      console.log('Response = ', response);

      if (response.didCancel) {
        console.log('User cancelled video picker');
      }
      else if (response.error) {
        console.log('CameraViewManager Error: ', response.error);
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

  close(props){
    props.navigator.pop();
  }

  render() {
    return (
      <View style={styles.container}>
      <Image source={this.state.avatarSource} style={[styles.container]}/>
      <TouchableOpacity
              onPress={() => this.close(this.props)}
              style={styles.closeButton}>
                <Text style=>X</Text>
            </TouchableOpacity>


            <IconButton
              icon='md-camera'
              text={strings._camera_take_picture}
              onPress={this.selectPhotoTapped.bind(this)}

            ></IconButton>

            <IconButton
              icon='md-video'
              text={strings._camera_take_video}
              onPress={this.selectVideoTapped.bind(this)}

            ></IconButton>

        <TouchableOpacity onPress={this.selectPhotoTapped.bind(this)}>
          <View style={[styles.avatar, styles.avatarContainer, {marginBottom: 20}]}>
          { this.state.avatarSource === null ? <Text>Select a Photo</Text> :
            <Image style={styles.avatar} source={this.state.avatarSource} />
          }
          </View>
        </TouchableOpacity>
        <TouchableOpacity onPress={this.selectVideoTapped.bind(this)}>
          <View style={[styles.avatar, styles.avatarContainer]}>
            <Text>Select a Video</Text>
          </View>
        </TouchableOpacity>
        { this.state.videoSource &&
          <Text style=>{this.state.videoSource}</Text>
        }
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF'
  },
   closeButton: {
      position: 'absolute',
      bottom: 30,
      right: 10,
      backgroundColor: 'rgba(255,255,255,0.3)',
      width: 50,
      height: 50,
      borderRadius: 25,
      margin: 20,
      borderColor: 'gray',
      borderWidth: 1,
      alignItems: 'center',
      justifyContent: 'center',
    },
  avatarContainer: {
    borderColor: '#9B9B9B',
    borderWidth: 1 / PixelRatio.get(),
    justifyContent: 'center',
    alignItems: 'center'
  },
  avatar: {
    borderRadius: 35,
    width: 70,
    height: 70
  }
});

// React.AppRegistry.registerComponent('Example', () => Example);
module.exports = CameraView;
