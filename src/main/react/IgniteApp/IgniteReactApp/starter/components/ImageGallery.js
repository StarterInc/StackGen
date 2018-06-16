var React = require("react");
import {
  StyleSheet,
  TouchableOpacity,
  Text,
  ScrollView,
  View
} from 'react-native';

import styles from '../AppStyles';
import ThumbnailImage from '../components/ThumbnailImage';

// http://ionicframework.com/docs/v2/ionicons/
import Icon from 'react-native-vector-icons/Ionicons';

// connect actions to controls
import { connect } from 'react-redux'
import {
  receiveContentItem,
} from '../state/ContentAction'

var ImageGallery = React.createClass({

  render: function() {
    const { content } = this.props
    var pos = 0
    return (
      <View>
      <View style= />
      <Text style={[styles.tiny,{marginVertical:10}]}>Gallery</Text>

        <ScrollView
          horizontal={true}
        >
            {(typeof(content) != 'undefined' ?
              content.map(contentItem => (
                // ((pos % 3 == 0) ? <View style={styles.row}> : null)
                  <ThumbnailImage
                    key={contentItem.id}
                    smallimage={true}
                    imageSource={contentItem.url}
                    description={contentItem.description}
                  />
                // ((pos % 3 == 0) ? </View> : null)
              ))
            :
            <View/>
          )}
        </ScrollView>
    </View>
    );
  }
});

function mapStateToProps(state, ownProps) {
	var { assessments } = state
	var { currentAssessment } = assessments

  if(typeof(currentAssessment) == 'undefined'){
    var { currentAssessment } = ownProps
  }

  var cx = currentAssessment.content
  var thisContent = state.content

// handle updating the gallery from state if new upload
// handle 'state.content' being an array or single item
  if(typeof(thisContent.length) != 'undefined'){
    var ct = [
      ...thisContent,
      ...cx,
    ]
  }else if(thisContent.fetching){
    var ct = cx
  }else{
  //    debugger
    var ct = [
      thisContent,
      ...cx,
    ]
  }

  return{
    content:ct
  }
}

module.exports = connect(
  mapStateToProps,
  // mapDispatchToProps
)(ImageGallery);
