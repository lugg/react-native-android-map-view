package com.lugg.ReactMapView;

import javax.annotation.Nullable;
import java.util.Map;

import android.util.Log;
import android.location.Location;
import android.os.SystemClock;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ReactProp;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.events.EventDispatcher;

import com.lugg.ReactMapView.events.RegionChangeEvent;
import com.lugg.ReactMapView.events.UserLocationUpdateEvent;

public class ReactMapViewManager extends SimpleViewManager<MapView> {
  public static final String REACT_CLASS = "MapViewAndroid";

  private static final String PROP_SHOWS_USER_LOCATION = "showsUserLocation";
  private static final String PROP_ROTATE_ENABLED = "rotateEnabled";
  private static final String PROP_SCROLL_ENABLED = "scrollEnabled";
  private static final String PROP_ZOOM_ENABLED = "zoomEnabled";

  // the desired zoom level, in the range of 2.0 to 21.0. Values below this range are set to 2.0, and values above it are set to 21.0.
  // Increase the value to zoom in. Not all areas have tiles at the largest zoom levels.
  private static final float DEFAULT_ZOOM_LEVEL = 12.0f;

  private static final float DEFAULT_ANIMATION_DURATION = 100;

  private boolean mFollowsUserLocation = true;
  private boolean mMapLoaded = false;

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  protected MapView createViewInstance(ThemedReactContext context) {
    MapView view = new MapView(context);
    view.onCreate(null);
    view.onResume();

    return view;
  }

  @ReactProp(name = PROP_SHOWS_USER_LOCATION, defaultBoolean = true)
  public void setShowsUserLocation(MapView view, boolean showsUserLocation) {
    GoogleMap map = view.getMap();
    map.getUiSettings().setMyLocationButtonEnabled(showsUserLocation);
    map.setMyLocationEnabled(showsUserLocation);
  }

  @ReactProp(name = PROP_ROTATE_ENABLED, defaultBoolean = false)
  public void setRotateEnabled(MapView view, boolean rotateEnabled) {
    GoogleMap map = view.getMap();
    map.getUiSettings().setRotateGesturesEnabled(rotateEnabled);
  }

  @ReactProp(name = PROP_SCROLL_ENABLED, defaultBoolean = true)
  public void setScrollEnabled(MapView view, boolean scrollEnabled) {
    GoogleMap map = view.getMap();
    map.getUiSettings().setScrollGesturesEnabled(scrollEnabled);
  }

  @ReactProp(name = PROP_ZOOM_ENABLED, defaultBoolean = true)
  public void setZoomEnabled(MapView view, boolean zoomEnabled) {
    GoogleMap map = view.getMap();
    map.getUiSettings().setZoomGesturesEnabled(zoomEnabled);
  }

  @Override
  public @Nullable Map getExportedCustomDirectEventTypeConstants() {
    return MapBuilder.of(
        RegionChangeEvent.EVENT_NAME, MapBuilder.of("registrationName", "onChange"),
        UserLocationUpdateEvent.EVENT_NAME, MapBuilder.of("registrationName", "onUserLocationUpdate")
    );
  }

  @Override
  protected void addEventEmitters(ThemedReactContext reactContext, final MapView view) {
    final GoogleMap map = view.getMap();
    final EventDispatcher eventDispatcher = reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();

    map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
      @Override
      public void onCameraChange(CameraPosition position) {
        if (!mMapLoaded) { return; }

        eventDispatcher.dispatchEvent(
          new RegionChangeEvent(view.getId(), SystemClock.uptimeMillis(), position)
        );
      }
    });

    // https://github.com/facebook/react-native/blob/master/React/Views/RCTMapManager.m#L115
    map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
      @Override
      public void onMyLocationChange(Location location) {
        if (mFollowsUserLocation) {
          map.moveCamera(CameraUpdateFactory.newLatLngZoom(
            new LatLng(location.getLatitude(), location.getLongitude()),
            DEFAULT_ZOOM_LEVEL
          ));

          // Move to user location only for the first time it loads up.
          mFollowsUserLocation = false;
        }

        eventDispatcher.dispatchEvent(
          new UserLocationUpdateEvent(view.getId(), SystemClock.uptimeMillis(), location)
        );
      }
    });

    map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
      @Override
      public void onMapLoaded() {
        mMapLoaded = true;
      }
    });
  }
}
