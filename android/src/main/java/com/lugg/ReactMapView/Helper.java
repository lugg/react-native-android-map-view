package com.lugg.ReactMapView;

import com.google.android.gms.maps.MapView;

public class Helper {
  private static MapView mMapView = null;

  public static void setMapView(MapView mapView) {
    mMapView = mapView;
  }

  public static MapView getMapView() {
    return mMapView;
  }
}
