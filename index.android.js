'use strict'

var React = require('react-native');
var {
  NativeMethodsMixin,
  PropTypes,
  NativeModules,
  requireNativeComponent
} = React;

var RCTUIManager = NativeModules.UIManager;

var NativeMapView = requireNativeComponent('MapViewAndroid', null);

var MAP_VIEW_REF = 'mapView';

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

  setCenterCoordinate(latitude, longitude) {
    RCTUIManager.dispatchViewManagerCommand(
      React.findNodeHandle(this),
      RCTUIManager.MapViewAndroid.Commands.setCenterCoordinate,
      [latitude, longitude, false]
    );
  },

  setCenterCoordinateAnimated(latitude, longitude) {
    RCTUIManager.dispatchViewCommand(
      React.findNodeHandle(this),
      RCTUIManager.MapViewAndroid.Commands.setCenterCoordinate,
      [latitude, longitude, true]
    );
  },

  setZoomLevel(zoom) {
    RCTUIManager.dispatchViewCommand(
      React.findNodeHandle(this),
      RCTUIManager.MapViewAndroid.Commands.setZoomLevel,
      [zoom, false]
    );
  },

  setZoomLevelAnimated(zoom) {
    RCTUIManager.dispatchViewCommand(
      React.findNodeHandle(this),
      RCTUIManager.MapViewAndroid.Commands.setZoomLevel,
      [zoom, true]
    );
  },

  setCenterCoordinateZoomLevel(latitude, longitude, zoom) {
    RCTUIManager.dispatchViewCommand(
      React.findNodeHandle(this),
      RCTUIManager.MapViewAndroid.Commands.setCenterCoordinateZoomLevel,
      [latitude, longitude, zoom, false]
    );
  },

  setCenterCoordinateZoomLevelAnimated(latitude, longitude, zoom) {
    RCTUIManager.dispatchViewCommand(
      React.findNodeHandle(this),
      RCTUIManager.MapViewAndroid.Commands.setCenterCoordinateZoomLevel,
      [latitude, longitude, zoom, true]
    );
  },

  render() {
    return (
      <NativeMapView
        ref={MAP_VIEW_REF}
        onChange={this._onChange}
        onUserLocationUpdate={this._onUserLocationUpdate}
        {...this.props}/>
    );
  }
});

module.exports = MapViewAndroid;
