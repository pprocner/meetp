package com.meetp.gameViewMap;

import com.meetp.R;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MapViewMain extends MapActivity{
	
	private final static String TAG = "Map Activity";
	MapController mapController;
	MapView mapView;
	
	protected void onCreate(Bundle InstanceState){
		super.onCreate(InstanceState);
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.id.mapView);
	   mapView.setBuiltInZoomControls(true);	   
	   configureMapView();
	}

	private void configureMapView() {
		mapController = mapView.getController();
	   mapController.setZoom(16);
	   int lat = (int) (52.220333379392365 * 1E6);
		int lng = (int) (21.010751724243164 * 1E6);
	   mapController.animateTo(new GeoPoint(lat,lng));
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mapmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case(R.id.showMe): {
				centerMapToMyPossition();
				break;
			}
			case(R.id.showMyStats): {
				
				break;
			}
			case(R.id.showTeamStats): {
				
				break;
			}
		}		
		return false;	
	}

	private void centerMapToMyPossition() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			int lat = (int) (location.getLatitude());
			int lng = (int) (location.getLongitude());
			mapController.animateTo(new GeoPoint(lat,lng));
		} else {
			Toast t = Toast.makeText(this, "Nie można znaleźć położenia", Toast.LENGTH_LONG);
			t.show();
		}
		
	}
}
