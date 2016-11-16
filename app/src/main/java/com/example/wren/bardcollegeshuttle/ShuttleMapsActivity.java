package com.example.wren.bardcollegeshuttle;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShuttleMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //Kline towards Hannafords Marker
        LatLng klineTowardHannaford = new LatLng(42.022227, -73.908498);
        mMap.addMarker(new MarkerOptions().position(klineTowardHannaford).title("Kline Stop Towards Hannafords"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(klineTowardHannaford));

        //Kline towards Tivoli Marker
        LatLng klineTowardTivoli = new LatLng(42.022669, -73.908263);
        mMap.addMarker(new MarkerOptions().position(klineTowardTivoli).title("Kline Stop Towards Tivoli"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(klineTowardTivoli));

        //Ward Gate towards Hannaford Marker
        LatLng wardGateTowardHannafords = new LatLng(42.027007, -73.905934);
        mMap.addMarker(new MarkerOptions().position(wardGateTowardHannafords).title("Ward Gate Stop Towards Hannafords"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(wardGateTowardHannafords));

        //Robbins Marker
        LatLng robbinsStop = new LatLng(42.029348, -73.904594);
        mMap.addMarker(new MarkerOptions().position(robbinsStop).title("Robbins Stop"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(robbinsStop));

        //Tivoli Marker
        
        //Hannafords Towards Tivoli Marker
        LatLng hannafordsStop = new LatLng(41.979889, -73.880896);
        mMap.addMarker(new MarkerOptions().position(hannafordsStop).title("Hannafords Stop Towards Tivoli"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hannafordsStop));


    }
}
