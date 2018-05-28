"use strict";

var React = require("react");

import {
    AsyncStorage,
} from 'react-native';

/**
    Utility class for saving and retrieving data from local storage
*/

var DEFAULT_KEY = "BigDecimal_local_data";

var LocalStorage = {

    getInitialState: function() {
        return { };
    },

    componentDidMount: function() {
        AsyncStorage.getItem(DEFAULT_KEY).then((value) => {
            this.setState({DEFAULT_KEY: value});
        }).done();

        console.log("RETRIEVED STATE: " + this.state);
    },

    // save all JSON responses locally
    saveData: function(response) {
        var val = JSON.stringify(response.text());
        console.log(val);
        AsyncStorage.setItem(DEFAULT_KEY, val);
        this.setState({DEFAULT_KEY: val});
        console.log("SET STATE: " + val);
    },

};

export { LocalStorage as default };
