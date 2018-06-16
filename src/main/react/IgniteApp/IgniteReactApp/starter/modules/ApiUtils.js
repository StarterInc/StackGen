/**
 * // ApiUtils.js // thanks to:
 * http://www.yoniweisbrod.com/network-requests-in-react-native-using-fetch/
 * 
 * gracefully handle REST api calls
 */

import strings from '../il8n/il8n'

var ApiUtils = {

  checkStatus: function(response) {
    // https://github.com/github/fetch
    if (response.status >= 200 && response.status < 300) {
        // alert(JSON.stringify(response.text()));
      return response;
    } else if (response.status == 401) {
      let error = new Error(strings._incorrect_login_credentials)
      error.response = response;
      throw error;
    } else {
      let error = new Error(response.statusText);
      error.response = response;

        console.warn('ApiUtils.nack! ' + error.response.status + ' : ' + error.responseText + ' : ' + error.toString());
      throw error;
    }
  }
};

export { ApiUtils as default };
