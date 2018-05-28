'use strict';

import React from "react";
import { StyleSheet } from "react-native";

// colorDarkText = "gray";
var colorLightText = "#69F0AE";
var colorHighlightedText = "#DFF2F1"
var colorDarkText = "#484848"
var colorOffWhite = '#FAFAFA'
var colorLightBackground = '#FAFAFA'
var colorPaleBackground = '#17B4A0'
var colorDarkBackground = '#A1C5F7'

var colorDamageTotal = '#018875'
var colorDamageSevere = '#DF3B05'
var colorDamageModerate = '#118675'
var colorDamageMinor = '#DF3B05'
var colorDamageNone = '#333333'

// font size
var sizeGiantText = 28;
var sizeLargeText = 22;
var sizeBigText = 20;
var sizeMediumText = 16;
var sizeSmallText = 14;
var sizeTinyText = 9;
var sizeMicroText = 7;

var iconSize = 40;
export const iconSizeBig = 48;

var colorCallToAction = '#FFC107';

// font weight
var fontWeightLight = "thin";
var fontWeightHeavy = "bold";

// bg color views
var BGDark = '#008975';

// transparent elementStyle
var transparentBGLight = 'rgba(185,246,202,0.5)';
var transparentBGDark =  'rgba(76,175,80,0.5)';

/* Palette teal and amber */
module.exports = StyleSheet.create({
  flexCenter: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  modal: {
    backgroundColor: 'rgba(0,0,0,.8)',
    position: 'absolute',
    top: 0,
    right: 0,
    bottom: 0,
    left: 0
  },


  damageTotalText:{
    color: colorDamageTotal,
    fontSize:sizeMediumText
  },
  damageSevereText:{
    color: colorDamageSevere,
    fontSize:sizeMediumText
  },
  damageModerateText:{
    color: colorDamageModerate,
    fontSize:sizeMediumText
  },
  damageMinorText:{
    color: colorDamageMinor,
    fontSize:sizeMediumText
  },
  damageNoneText:{
    color: colorDamageNone,
    fontSize:sizeMediumText
  },
// breadcrumb navigator
scene: {
  paddingTop: 50,
  flex: 1,
},

breadCrumbNavContainer:{
    height:80,
    marginTop:0,
    backgroundColor:colorDarkBackground,
},

breadCrumbViewContainer:{
    flex: 1,
    marginTop:60,
},

appBreadcrumbButton: {
  // padding:10,
  color:colorLightText,
  // marginVertical:10,
  marginHorizontal:15
},

breadCrumbText:{
    backgroundColor:'transparent',
    fontSize:sizeMediumText,
    fontWeight: fontWeightHeavy,
    color:colorOffWhite,
    padding:6,
},

crumbIconPlaceholder: {
  alignItems:'center',
  justifyContent:'center',
  marginRight:5,
  marginTop:0,
  borderRadius:20,
  width:40,
  height:40,
  borderWidth:3,
  borderColor:colorOffWhite,
  backgroundColor:colorDarkBackground,
},

crumbSeparatorPlaceholder: {
  height:5,
  width:15,
  marginLeft:-2,
  marginTop:18,
  backgroundColor:colorHighlightedText
},

column: {
  flex: 1,
  flexDirection:'column',
},

initialscreen: {
  //paddingTop: 350,
  flex: 1,
  flexDirection:'column',
  justifyContent:'flex-end',
},

errorText: {
  fontWeight:'bold',
  color: 'red',
  alignItems: 'center',
},

inputcontainer: {
  flex: .5,
  backgroundColor: colorLightBackground,
  alignItems: 'center',
  marginBottom: 3,
},

// WebViewComponent
webViewStyle: {
  flex: 1,
  backgroundColor: transparentBGLight,
},

spacerView: {
  height:20,
},

addressBarRow: {
  flexDirection: 'row',
  padding: 8,
},
webView: {
  backgroundColor: transparentBGDark,
  height: 350,
},

formContainer:{
  paddingHorizontal:10,
  margin:0,
  flex:1,
  backgroundColor:colorOffWhite,
  flexDirection:'column'
},

buttonContainerBox:{
  backgroundColor: 'rgba(100,100,100,0.10)',
  borderColor:colorHighlightedText,
  borderRadius:6,
  padding:15,
  // marginLeft:10,
  borderWidth:0,
  margin:5,
  flexDirection: 'row',
  justifyContent: 'space-between',
  marginHorizontal:20,
},


buttonContainer: {
  flexDirection:'row',
  marginVertical:10,
  backgroundColor: 'rgba(20,20,20,0.1)',
  borderColor:colorDarkText,
  marginHorizontal:20,
  borderRadius:6,
  paddingHorizontal:10,
  // marginLeft:10,
  borderWidth:0,
  // margin:5,0
  padding:0,
  alignItems:'center',
  justifyContent: 'space-between',
},

addButton: {

  width: 45,
  height: 45,
  borderRadius: 4,
  margin: 10,
  borderColor: '#00BFA5',
  borderWidth: 1,
  alignItems: 'center',
  justifyContent: 'center',

},

closeButton: {
  position: 'absolute',
  top: 0,
  left: 0,
  backgroundColor: 'rgba(255,255,255,0.1)',
  width: 50,
  height: 50,
  borderRadius: 25,
  margin: 20,
  borderColor: 'gray',
  borderWidth: 1,
  alignItems: 'center',
  justifyContent: 'center',
  elevation:100
},

addressBarTextInput: {
  backgroundColor: transparentBGDark,
  borderColor: 'transparent',
  borderRadius: 3,
  borderWidth: 1,
  height: 24,
  paddingLeft: 10,
  paddingTop: 3,
  paddingBottom: 3,
  flex: 1,
  fontSize: 14,
},
navButton: {
  width: 20,
  padding: 3,
  marginRight: 3,
  alignItems: 'center',
  justifyContent: 'center',
  backgroundColor: transparentBGDark,
  borderColor: 'transparent',
  borderRadius: 3,
},
disabledButton: {
  width: 20,
  padding: 3,
  marginRight: 3,
  alignItems: 'center',
  justifyContent: 'center',
  backgroundColor: transparentBGLight,
  borderColor: 'transparent',
  borderRadius: 3,
},
goButton: {
  height: 24,
  padding: 3,
  marginLeft: 8,
  alignItems: 'center',
  backgroundColor: transparentBGDark,
  borderColor: 'transparent',
  borderRadius: 3,
  alignSelf: 'stretch',
},
statusBar: {
  flexDirection: 'row',
  alignItems: 'center',
  paddingLeft: 5,
  height: 22,
},
statusBarText: {
  color: 'white',
  fontSize: 13,
},
spinner: {
  width: 20,
  marginRight: 6,
},

buttons: {
  flexDirection: 'row',
  height: 30,
  // backgroundColor: 'black',
  alignItems: 'center',
  justifyContent: 'space-between',
},


webViewButton: {
  flex: 0.5,
  width: 0,
  margin: 5,
  borderColor: 'gray',
  borderWidth: 1,
  backgroundColor: 'gray',
},

  // card
  cardRow: {
    flexDirection:'row',
    padding: 0,
  },
  separator: {
    height: 1,
  },

  cardContainer: {
    flex: .1,
    alignSelf: 'stretch',
    alignItems: 'center',
    flexDirection:'row',
    borderRadius: 0,
    marginBottom: 0,
  },
  contentRow: {
    flex:1,
    borderBottomWidth: 0.5,
    borderBottomColor: '#d6d7da',
  },

  titleText: {
    fontSize: sizeMediumText,
    fontWeight: '500',
  },
  arrowNav: {
    fontSize: 24,
    fontWeight: '500',
    color:'#222222',
    marginRight:10
  },
  bodyText: {
    fontSize: 12,
    fontWeight: '500',
  },
  descriptionText: {
    fontSize: 12,
  },

  // appbar
  toolbar: {
    height:100,
    flexDirection:'row',
    justifyContent: 'space-between',
    paddingVertical:20,
    backgroundColor:'#008975',
  },

  appBarRow: {
    top:5,
  },

  toolBarButton: {
    justifyContent: 'center',
    alignItems:'center',
    width:iconSizeBig,
    height:iconSizeBig,
    color:colorLightText,
  },


  appBarButton: {
    // padding:10,
    color:colorLightText,
    marginVertical:10,
    marginHorizontal:15
  },

  actionButton: {
    margin:5,
    justifyContent:  'space-between',
    alignItems:'center',
    color:colorDarkText,
  },

  BackButton: {
    backgroundColor: 'rgba(255,255,255,0.1)',
    width: 40,
    height: 40,
    borderRadius: 20,
    marginTop: 5,
    marginRight: 10,
    //borderColor: 'white',
    //borderWidth: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },

  PattyButton: {
    marginTop:0,
    alignItems:'center',
    height:60,
    //fontSize:sizeBigText,
  },

  bubble: {
    flex: 0,
    flexDirection: 'row',
    alignSelf: 'flex-start',
    backgroundColor: '#FF5A5F',
    paddingVertical: 2,
    paddingHorizontal: 4,
    borderRadius: 3,
    borderColor: '#D23F44',
    borderWidth: 0.5,
  },
  dollar: {
    color: '#fff',
    fontSize: 10,
  },
  amount: {
    color: '#fff',
    fontSize: 13,
  },
  arrow: {
    backgroundColor: 'transparent',
    borderColor: 'transparent',
    borderWidth: 4,
    borderTopColor: '#FF5A5F',
    alignSelf: 'center',
    marginTop: -9,
  },
  arrowBorder: {
    backgroundColor: 'transparent',
    borderColor: 'transparent',
    borderWidth: 4,
    borderTopColor: '#D23F44',
    alignSelf: 'center',
    marginTop: -0.5,
  },
  selectedBubble: {
    backgroundColor: '#4da2ab',
    borderColor: '#007a87',
  },
  selectedArrow: {
    borderTopColor: '#4da2ab',

  },
  selectedArrowBorder: {
    borderTopColor: '#007a87',
  },

  /* shared styles */
  header:{
    backgroundColor: transparentBGDark,
  },
  darkPrimaryColor    :  {
     backgroundColor: '#00796B',
  },
  defaultPrimaryColor : {
    backgroundColor: '#009688',
  },
  lightPrimaryColor   : {
    backgroundColor: '#B2DFDB',
  },
  lightPrimaryTextColor    : {
    color: '#FFFFFF',
  },
  accentColor          : {
    backgroundColor: '#FFC107',
  },
  primaryTextColor    : {
    color: '#212121',
  },
  secondaryTextColor  : {
    color: '#727272',
  },
  dividerColor         : {
    borderColor: '#B6B6B6',
  },


    refreshButton: {
      flex:.25,
      marginHorizontal:20,
      backgroundColor:'blue',
      borderWidth:1,
      height:50,
      borderRadius:25,
      // alignItems: "center",
      // justifyContent:   'center',
      // backgroundColor: 'rgba(0,0,0,0.5)',
      padding: 5,
      margin: 3,

    },

  button: {
    height:50,
    borderRadius:25,
    // alignItems: "stretch",
    justifyContent:   'center',
    backgroundColor: 'rgba(255,255,255,0.1)',
    padding: 15,
    margin: 5,
    marginRight:30,
    marginLeft:30,
  },

  CTAButton:{
    height:300,
      backgroundColor:'#FFC107'
  },

  callToAction:{
    color:colorCallToAction
  },

  map: {
    flex:1,
    flexDirection:'row'
  },

  mapContainer: {
    flex:1,
  },

  mapButton: {
    flex:1,
    alignSelf:'center',
    alignItems: 'center',
  },

  mapButtonContainerSmall: {
    height:30,
    flexDirection:'row',
    backgroundColor: 'rgba(255,255,255,0.2)',
  },

  mapButtonContainer: {
    height:80,
    flexDirection:'row',
    backgroundColor: 'rgba(255,255,255,0.2)',
  },

  buttonBottom: {
    position:'absolute',
    backgroundColor:'#000000',
    bottom:0,
  },
  buttonHighlight: {
    alignItems:       'center',
    justifyContent:   'center',
    backgroundColor:  '#F5A623',
    padding: 15,
    marginLeft:30,
    marginRight:30,
  },
  dropShadow: {
    borderBottomWidth:  1,
    borderLeftWidth:    1,
    borderRightWidth:   1,
    borderTopWidth:     1,
    borderTopColor:     '#B9F6CA',
    borderLeftColor:    '#B9F6CA',
    borderBottomColor:  '#B9F6CA',
    borderRightColor:   '#B9F6CA',
  },
  buttonTextDark: {
    color:colorDarkText,
    fontWeight:'bold',
    fontSize:sizeMediumText
  //  alignSelf:'flex-end'
  },

 buttonText:{
    fontSize: sizeMediumText,
    textAlign:'center',
    fontWeight: '500',
    color:'white'
  },
  avatarContainer: {
    borderColor: '#9B9B9B',
    borderWidth: .2,
    justifyContent: 'center',
    alignItems: 'center'
  },
  avatar: {
    borderRadius: 20,
    padding:5,
    margin:10,
    width: 40,
    height: 40
  },
  messageText: {
    fontSize: sizeMediumText,
    padding: 15,
    marginTop: 50,
    marginLeft: 15,
  },

  label:{
    paddingTop:0,
    fontSize: sizeMediumText,
    color: "#FFFFFF",
    alignSelf: "center"
  },


  loginContainer: {
    flex: 1,
    padding: 30,
    backgroundColor:colorDarkBackground,
    //marginTop: 65,
    alignItems: "stretch"
  },

  right: {
  //  position:'absolute',
    color:'#B9F6CA',
    fontSize: sizeSmallText,
    // position:'absolute',
    marginLeft:180,
  // right:30,
  //  zIndex:100,

  },
  bottom: {
    bottom:0,
  },
  formInput: {
    height: 40,
    borderRadius:2,
    borderWidth:1,
    borderColor:colorPaleBackground,
    alignItems:       'flex-start',
    justifyContent:   'center',
    backgroundColor: 'rgba(255,255,255,0.2)',
    padding: 10,
    margin: 10,
  },


  latlng: {
    width: 200,
    alignItems: 'stretch',
  },

  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  textInput: {
    width: 150,
    height: 20,
    borderWidth: 0.5,
    borderColor: '#aaaaaa',
    fontSize: sizeSmallText,
    padding: 4,
  },
  changeButton: {
    alignSelf: 'center',
    marginTop: 5,
    padding: 3,
    borderWidth: 0.5,
    borderColor: '#777777',
  },

  nextButton: {

    width: 60,
    height: 60,
    borderRadius: 30,
    borderWidth:1,
    borderColor:transparentBGDark,
    alignSelf: 'flex-end',
    alignItems:'center',
    justifyContent: 'center',
    backgroundColor:transparentBGLight,
  },

  nextButtonText: {
    alignItems:       'center',
    justifyContent:   'center',

    color:'#ffffff',
    fontWeight: 'bold',
    fontSize: 20
  },

  menuContainer: {
      marginTop:0,
      paddingTop:0,
      flex:1,
      margin:0,
      height:2000,
      backgroundColor:colorDarkBackground,
    },

  appContainer: {
      flex: 1,
      paddingTop: 0,
      width: undefined,
      height: undefined,
  },

  container: {
    marginTop:0,
    paddingBottom:20,
    backgroundColor:colorLightBackground
  },

  containerDark: {
    marginTop:0,
    paddingBottom:20,
    backgroundColor:colorDarkBackground
  },

  containerLight:{
    flex:1,
    marginTop:0,
    paddingBottom:20,
    backgroundColor:colorHighlightedText
  },

  imageContainer: {
    marginTop:0,
    padding:5,
    backgroundColor:colorDarkBackground
  },

  scrollContainer: {
    //bounceValue:2,
    paddingBottom:100,
    height:undefined
  },

  contentContainer:{
    flex: 1,
  },

  title: {
  //  fontFamily: 'Roboto',
    marginLeft:0,
    marginTop:0,
    fontWeight:'700',
    alignItems: 'center',
    alignSelf: 'center',
    color: colorHighlightedText,
    textAlign:'left',
    justifyContent:'center',
    fontSize: sizeGiantText,
  },

  titleBarText: {
  //  fontFamily: 'Roboto',
    marginLeft:0,
    marginTop:0,
    fontWeight:'700',
    alignItems: 'center',
    alignSelf: 'center',
    color: colorHighlightedText,
    textAlign:'left',
    justifyContent:'center',
    fontSize: sizeGiantText,
  },

  titleDark: {
    // fontFamily: 'Roboto',
    marginLeft:0,
    marginTop:10,
    fontWeight:'700',
    color: colorDarkText,
    textAlign:'left',
    fontSize: sizeSmallText,
  },

  titleDark2: {
    // fontFamily: 'Roboto',
    marginLeft:0,
    // marginTop:10,
    fontWeight:fontWeightHeavy,
    color: colorDarkText,
    textAlign:'left',
    fontSize: sizeLargeText,
  },

  titleFormHeader:{
    // marginLeft:-10,
    marginBottom:10,
    marginTop:20,
    fontWeight:fontWeightHeavy,
    color: colorDarkText,
    fontSize: sizeMediumText,
  },

  title1: {
    marginLeft:10,
    fontWeight:'800',
    color: '#DFF2F1',
    textAlign:'left',
    fontSize: sizeGiantText,
  },

  title2: {
    marginBottom:0,
    marginTop:10,
    marginLeft:10,
    fontWeight:'600',
    color: '#DFF2F1',
    textAlign:'left',
    fontSize: sizeSmallText,
  },

  titleLight:{
    color:'#484848',
    textAlign:'left',
    fontSize: sizeGiantText,
  },

  microFNT: {
    marginLeft:10,
    color:colorDarkText,
    fontSize: sizeMicroText,
  },

  tinyFNT: {
    marginLeft:10,
    color:colorDarkText,
    fontSize: sizeTinyText,
  },

  tiny: {
    // marginLeft:10,
    color:colorDarkText,
    fontSize: sizeSmallText,
    fontWeight:'bold',
  //  marginBottom: 10,
  },
  bigImage: {
    backgroundColor: 'rgba(255,255,255,0.3)',
    flex:1,
    width:undefined,
    margin:0,
    borderColor: 'gray',
    borderWidth: 0,
    alignItems: 'center',
    justifyContent: 'center',
  },

  thumbnail: {
    backgroundColor: 'rgba(255,255,255,0.3)',
    width: 85,
    height: 85,
    borderRadius: 4,
    margin: 10,
    borderColor: 'gray',
    borderWidth: 0,
    alignItems: 'center',
    justifyContent: 'center',
  },

  thumbnailSmall: {
    backgroundColor: 'rgba(255,255,255,0.3)',
    width: 50,
    height: 50,
    borderRadius: 7,
    margin: 5,
    borderColor: 'gray',
    borderWidth: 0,
    alignItems: 'center',
    flexDirection: 'column',
    justifyContent: 'space-between',

  },

});
