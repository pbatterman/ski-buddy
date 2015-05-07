package com.example.zkrasner.skibuddy;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;


public class MapActivity extends Activity implements LocationListener{
    private GoogleMap googleMap;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        String[] friends = getIntent().getExtras().getStringArray("friendlocations");
        username = getIntent().getExtras().getString("username");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMyLocationEnabled(true);

            for (String s: friends) {
                String[] split = s.split(":");
                String name = split[0];
                double lat = Double.parseDouble(split[1]);
                double lng = Double.parseDouble(split[2]);
                dropFriendPin(new LatLng(lat, lng), name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dropFriendPin(LatLng ll, String name){
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        Marker TP = googleMap.addMarker(new MarkerOptions().
                position(ll).title(name));
    }

    @Override
    public void onLocationChanged(Location location) {

        final double latitude = location.getLatitude();

        // Getting longitude of the current location
        final double longitude = location.getLongitude();

        ParseQuery friendQuery = new ParseQuery("accounts");
        friendQuery.whereEqualTo("username", username);
        friendQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                             @Override
                                             public void done(ParseObject object, ParseException e) {
                                                 object.put("Lat", latitude);
                                                 object.put("Lng", longitude);
                                                 object.saveInBackground();
                                             }
                                         });
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}