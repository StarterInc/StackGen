'use strict';

// fix 'self.fetch' issue: https://github.com/facebook/react-native/issues/9599
if(typeof global.self === "undefined")
{
    global.self = global;
}

  module.exports = {
    JSESSIONID : -1,
    STORE_KEY: 'TODO',
    MAJOR_VERSION : '.1',
    MINOR_VERSION : '0.1.27',
    API_HOST: 'https://api.{{appname}}.online/',
    //API_HOST : 'http://192.168.2.116:8080/',
  //  API_HOST : 'http://localhost:8080/',
    API_VERSION : '1.0',
    COLOR: {
      ORANGE: '#FF9900',
      DARKBLUE: '#0F3274',
      LIGHTBLUE: '#6EA8DA',
      DARKGRAY: '#999',
  },
};
