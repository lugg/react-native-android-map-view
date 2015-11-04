package com.lugg.ReactMapView.events;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import android.location.Location;

public class UserLocationUpdateEvent extends Event<UserLocationUpdateEvent> {

  public static final String EVENT_NAME = "topUserLocationUpdate";

  private final Location mLocation;

  public UserLocationUpdateEvent(int viewId, long timestampMs, Location location) {
    super(viewId, timestampMs);
    mLocation = location;
  }

  @Override
  public String getEventName() {
    return EVENT_NAME;
  }

  private double getLatitude() {
    return mLocation.getLatitude();
  }

  private double getLongitude() {
    return mLocation.getLongitude();
  }

  @Override
  public void dispatch(RCTEventEmitter rctEventEmitter) {
    rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
  }

  private WritableMap serializeEventData() {
    WritableMap eventData = Arguments.createMap();
    eventData.putDouble("latitude", getLatitude());
    eventData.putDouble("longitude", getLongitude());
    return eventData;
  }
}
