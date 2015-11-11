package com.lugg.ReactMapView;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;

import java.util.Map;
import android.util.Log;

public class ReactMapViewModule extends ReactContextBaseJavaModule {

  public ReactMapViewModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "MapViewAndroid";
  }


  @ReactMethod
  public void testing(String message) {
    Log.d("ReactNative", Helper.getMapView().toString());
  }

  @ReactMethod
  public void setCenterCoordinate(Float latitude, Float longitude) {
    MapView mapView = Helper.getMapView();
    if (mapView == null) return;

    GoogleMap map = mapView.getMap();
    if (map == null) return;


  }

  @ReactMethod
  public void setCenterCoordinateAnimated(Float latitude, Float longitude) {
    MapView mapView = Helper.getMapView();
    if (mapView == null) return;

    GoogleMap map = mapView.getMap();
    if (map == null) return;

    Log.d("ReactNative", "" + latitude + "" + longitude);
  }

  public void setCenterCoordinateZoomLevel(Float latitude, Float longitude, Float zoomLevel) {
    MapView mapView = Helper.getMapView();
    if (mapView == null) return;

    GoogleMap map = mapView.getMap();
    if (map == null) return;
  }

  public void setCenterCoordinateZoomLevelAnimated(Float latitude, Float longitude, Float zoomLevel) {
    MapView mapView = Helper.getMapView();
    if (mapView == null) return;

    GoogleMap map = mapView.getMap();
    if (map == null) return;
  }
}
