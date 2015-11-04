'use strict'

var React = require('react-native');
var {
  NativeMethodsMixin,
  PropTypes,
  NativeModules,
  requireNativeComponent
} = React;

var NativeMapView = requireNativeComponent('MapViewAndroid', null);

var MapViewAndroid = React.createClass({
  mixins: [NativeMethodsMixin],

  propTypes: {
    scrollEnabled: PropTypes.bool,
    rotateEnabled: PropTypes.bool,
    zoomEnabled: PropTypes.bool,
    showsUserLocation: PropTypes.bool,
    onRegionChangeComplete: PropTypes.func,
    onUserLocationUpdate: PropTypes.func
  },

  getDefaultProps() {
    return {
      showsUserLocation: true,
      rotateEnabled: false,
      scrollEnabled: true,
      zoomEnabled: true
    }
  },

  _onChange(event) {
    this.props.onRegionChangeComplete && this.props.onRegionChangeComplete(event.nativeEvent);
  },

  _onUserLocationUpdate(event) {
    this.props.onUserLocationUpdate && this.props.onUserLocationUpdate(event.nativeEvent);
  },

  render() {
    return (
      <NativeMapView
        onChange={this._onChange}
        onUserLocationUpdate={this._onUserLocationUpdate}
        {...this.props}/>
    );
  }
});

module.exports = MapViewAndroid;
