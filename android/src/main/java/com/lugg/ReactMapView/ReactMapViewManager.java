package com.lugg.ReactMapView;

import javax.annotation.Nullable;
import java.util.Map;

import android.util.Log;
import android.location.Location;
import android.os.SystemClock;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.common.MapBuilder;

import com.lugg.ReactMapView.events.RegionChangeEvent;
import com.lugg.ReactMapView.events.UserLocationUpdateEvent;

public class ReactMapViewManager extends SimpleViewManager<MapView> {

  public static final String REACT_CLASS = "MapViewAndroid";

  private static final String PROP_SHOWS_USER_LOCATION = "showsUserLocation";
  private static final String PROP_ROTATE_ENABLED = "rotateEnabled";
  private static final String PROP_SCROLL_ENABLED = "scrollEnabled";
  private static final String PROP_ZOOM_ENABLED = "zoomEnabled";

  public static final int COMMAND_SET_CENTER_COORDINATE = 1;
  public static final int COMMAND_SET_ZOOM_LEVEL = 3;
  public static final int COMMAND_SET_CENTER_COORDINATE_ZOOM_LEVEL = 5;

  private static final String LOG_TAG = "ReactNative";

  // the desired zoom level, in the range of 2.0 to 21.0. Values below this range are set to 2.0, and values above it are set to 21.0.
  // Increase the value to zoom in. Not all areas have tiles at the largest zoom levels.
  private static final float DEFAULT_ZOOM_LEVEL = 12.0f;

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

    if (view.getMap() == null) {
      logError("GoogleMap is null. It's likely Google Play Services is not installed.");
    }

    return view;
  }

  //
  // Properties
  //

  @ReactProp(name = PROP_SHOWS_USER_LOCATION, defaultBoolean = true)
  public void setShowsUserLocation(MapView view, final boolean showsUserLocation) {
    GoogleMap map = view.getMap();

    if (map != null) {
      map.setMyLocationEnabled(true);
      map.getUiSettings().setMyLocationButtonEnabled(true);
    }
  }

  @ReactProp(name = PROP_ROTATE_ENABLED, defaultBoolean = false)
  public void setRotateEnabled(MapView view, final boolean rotateEnabled) {
    GoogleMap map = view.getMap();

    if (map != null) {
      map.getUiSettings().setRotateGesturesEnabled(rotateEnabled);
    }
  }

  @ReactProp(name = PROP_SCROLL_ENABLED, defaultBoolean = true)
  public void setScrollEnabled(MapView view, final boolean scrollEnabled) {
    GoogleMap map = view.getMap();

    if (map != null) {
      map.getUiSettings().setScrollGesturesEnabled(scrollEnabled);
    }
  }

  @ReactProp(name = PROP_ZOOM_ENABLED, defaultBoolean = true)
  public void setZoomEnabled(MapView view, final boolean zoomEnabled) {
    GoogleMap map = view.getMap();

    if (map != null) {
      map.getUiSettings().setZoomGesturesEnabled(zoomEnabled);
    }
  }


  //
  // ViewManager Overrides
  //

  @Override
  public @Nullable Map getExportedCustomDirectEventTypeConstants() {
    return MapBuilder.of(
      UserLocationUpdateEvent.EVENT_NAME, MapBuilder.of("registrationName", "onUserLocationUpdate")
    );
  }

  @Override
  protected void addEventEmitters(ThemedReactContext reactContext, final MapView view) {
    final GoogleMap map = view.getMap();
    if (map == null) return;

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

    map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
      @Override
      public void onMyLocationChange(Location location) {
        // https://github.com/facebook/react-native/blob/master/React/Views/RCTMapManager.m#L115
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

  @Override
  public Map<String, Integer> getCommandsMap() {
    return MapBuilder.of(
      "setCenterCoordinate",
      COMMAND_SET_CENTER_COORDINATE,
      "setZoomLevel",
      COMMAND_SET_ZOOM_LEVEL,
      "setCenterCoordinateZoomLevel",
      COMMAND_SET_CENTER_COORDINATE_ZOOM_LEVEL);
  }

  @Override
  public void receiveCommand(MapView view, int commandType, @Nullable ReadableArray args) {
    Assertions.assertNotNull(view);
    Assertions.assertNotNull(args);
    switch(commandType) {
      case COMMAND_SET_CENTER_COORDINATE: {
        this.setCenterCoordinate(
          view,
          args.getDouble(0),
          args.getDouble(1),
          args.getBoolean(2));
        return;
      }
      case COMMAND_SET_ZOOM_LEVEL: {
        this.setZoomLevel(
          view,
          new Double(args.getDouble(0)).floatValue(),
          args.getBoolean(1));
        return;
      }
      case COMMAND_SET_CENTER_COORDINATE_ZOOM_LEVEL: {
        this.setCenterCoordinateZoomLevel(
          view,
          args.getDouble(0),
          args.getDouble(1),
          new Double(args.getDouble(2)).floatValue(),
          args.getBoolean(3));
      }
      default: {
        throw new IllegalArgumentException(String.format(
          "Unsupported command %d received by %s.",
          commandType,
          getClass().getSimpleName()));
      }
    }
  }

  //
  // Instance Methods
  //

  private void setCenterCoordinate(MapView view, double latitude, double longitude, boolean animated) {
    GoogleMap map = view.getMap();
    if (map == null) return;

    CameraUpdate update = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));

    if (animated) {
      map.animateCamera(update);
    } else {
      map.moveCamera(update);
    }
  }

  private void setCenterCoordinateZoomLevel(MapView view, double latitude, double longitude, float zoom, boolean animated) {
    GoogleMap map = view.getMap();
    if (map == null) return;

    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom);

    if (animated) {
      map.animateCamera(update);
    } else {
      map.moveCamera(update);
    }
  }

  private void setZoomLevel(MapView view, float zoom, boolean animated) {
    GoogleMap map = view.getMap();
    if (map == null) return;

    CameraUpdate update = CameraUpdateFactory.zoomTo(zoom);

    if (animated) {
      map.animateCamera(update);
    } else {
      map.moveCamera(update);
    }
  }

  private void logError(String message) {
    Log.e(LOG_TAG, message);
  }
}
