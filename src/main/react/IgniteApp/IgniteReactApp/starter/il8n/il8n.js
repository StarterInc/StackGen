// ES6 module syntax
import LocalizedStrings from 'react-native-localization';

// CommonJS syntax
// let LocalizedStrings  = require ('react-native-localization');

module.exports =  new LocalizedStrings({
    en:require('./en/strings.json'),
});
