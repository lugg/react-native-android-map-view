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
  NativeModules
} = React;

var Example = React.createClass({
  componentDidMount: function() {
    this.refs.map.setCenterCoordinateZoomLevel(37.781, -122.395, 15);
  },

  render: function() {
    return (
      <View style={styles.container}>
        <MapViewAndroid
          ref="map"
          style={styles.map}
          onRegionChangeComplete={this.onRegionChangeComplete}/>
      </View>
    );
  },

  onRegionChangeComplete(location) {
    // this.refs['map'].setCenterCoordinate(0, 0);
    console.log(location);
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
