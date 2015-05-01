package com.example.zkrasner.skibuddy;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;


public class MapActivity extends Activity {
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        String[] friends = getIntent().getExtras().getStringArray("friendlocations");
        String username = getIntent().getExtras().getString("username");
        if (friends != null) {
            System.out.println(friends.length);
        } else {
            System.out.println(0 + " something fucked up");
        }
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            for (String s: friends) {
                System.out.println(s);
                String[] split = s.split(":");
                String name = split[0];
                double lat = Double.parseDouble(split[1]);
                double lng = Double.parseDouble(split[2]);
                dropFriendPin(new LatLng(lat, lng), name);
            }
            dropFriendPin(new LatLng(-33.867, 151.206), username);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dropFriendPin(LatLng ll, String name){
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        Marker TP = googleMap.addMarker(new MarkerOptions().
                position(ll).title(name));
    }

}