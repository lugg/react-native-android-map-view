package com.lugg.ReactMapView.events;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class RegionChangeEvent extends Event<RegionChangeEvent> {

  public static final String EVENT_NAME = "topChange";

  private final CameraPosition mPosition;

  public RegionChangeEvent(int viewId, long timestampMs, CameraPosition position) {
    super(viewId, timestampMs);
    mPosition = position;
  }

  @Override
  public String getEventName() {
    return EVENT_NAME;
  }

  private float getBearing() {
    return mPosition.bearing;
  }

  private float getTilt() {
    return mPosition.tilt;
  }

  private float getZoom() {
    return mPosition.zoom;
  }

  private double getLatitude() {
    return mPosition.target.latitude;
  }

  private double getLongitude() {
    return mPosition.target.longitude;
  }

  @Override
  public void dispatch(RCTEventEmitter rctEventEmitter) {
    rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
  }

  private WritableMap serializeEventData() {
    WritableMap eventData = Arguments.createMap();
    eventData.putDouble("zoom", getZoom());
    eventData.putDouble("latitude", getLatitude());
    eventData.putDouble("longitude", getLongitude());
    eventData.putDouble("tilt", getTilt());
    eventData.putDouble("bearing", getBearing());
    return eventData;
  }
}
