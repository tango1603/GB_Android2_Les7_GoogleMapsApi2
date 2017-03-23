package com.example.ilya.gb_android2_les7_googlemapsapi;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Activity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager mLocManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Location manager instance
        mLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapsActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item != null ? item.getItemId() : 0;
        // Map type - Normal
        if (id == R.id.menu_map_mode_normal) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        // Map type - Satellite
        if (id == R.id.menu_map_mode_satellite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        }
        // Map type - Terrain
        if (id == R.id.menu_map_mode_terrain) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            return true;
        }

        // Show traffic, or not
        if (id == R.id.menu_map_traffic) {
            mMap.setTrafficEnabled(!mMap.isTrafficEnabled());
        }

        // My Location
        if (id == R.id.menu_map_location && mMap.isMyLocationEnabled()) {

            // Get last know location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }

            final Location loc = mLocManager.getLastKnownLocation(
                    LocationManager.PASSIVE_PROVIDER);

            // If location available
            if (loc != null) {
                // Create LatLng object for Maps
                LatLng target = new LatLng(loc.getLatitude(), loc.getLongitude());
                // Defines a camera move. An object of this type can be used to modify a map's camera
                // by calling moveCamera()
                CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(target, 15F);
                // Move camera to point with Animation
                mMap.animateCamera(camUpdate);
            }
        }

        // Add point to map
        if (id == R.id.menu_map_point_new) {
            // Add a marker in Moscow, and move the camera
            LatLng uln = new LatLng(54.316073, 48.441306);
            mMap.addMarker(new MarkerOptions().position(uln).title("Marker in Ulyanovsk"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(uln));
            moveCamera(uln, 11F);
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Move Map's camera to specified target.
     * */
    private void moveCamera(LatLng target, float zoom) {
        if (target == null || zoom < 1) return;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, zoom));
    }
}
