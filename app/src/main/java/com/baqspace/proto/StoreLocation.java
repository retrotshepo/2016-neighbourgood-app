package com.baqspace.proto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.baqspace.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Tshepo Lebusa on 23/10/2016.
 */
public class StoreLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    double latitude = 0;
    double longitude = 0;

    User session = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.marketLocation);
        mapFragment.getMapAsync(this);

        session = (User) getIntent().getSerializableExtra("session");
        System.out.println("STORE: " + session.traderName);

        latitude = session.trLatitude;
        longitude = session.trLongitude;





        //System.out.println("Lat: " + latitude + "\nLong: " + longitude);

        setUpMapIfNeeded();
        Toast.makeText(StoreLocation.this, "Please tap on pin and open maps at the bottom of your screen", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            //mMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map).getMap();


            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move
        // the camera.
        //LatLng ngm = new LatLng(-26.194220, 28.035110);

        latitude = -26.1828204;
        longitude = 27.9958558;
        LatLng ngm = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(ngm).title("TUTTO FOOD"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ngm));
    }


    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("currentSession", session);
        startActivity(intent);
    }
}
