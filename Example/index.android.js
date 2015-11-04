/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */
'use strict';

var React = require('react-native');
var MapViewAndroid = require('react-native-android-map-view');

var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
} = React;

var Example = React.createClass({
  render: function() {
    return (
      <View style={styles.container}>
        <MapViewAndroid style={styles.map}/>
      </View>
    );
  }
});

var styles = StyleSheet.create({
  container: {
    flex: 1
  },
  map: {
    flex: 1
  }
});

AppRegistry.registerComponent('Example', () => Example);
