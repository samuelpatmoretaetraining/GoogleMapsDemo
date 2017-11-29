package com.muelpatmore.googlemapsdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.muelpatmore.googlemapsdemo.apimodels.JustEatModel;
import com.muelpatmore.googlemapsdemo.apimodels.Restaurant;
import com.muelpatmore.googlemapsdemo.network.AppScheduleProvider;
import com.muelpatmore.googlemapsdemo.network.ScheduleProvider;
import com.muelpatmore.googlemapsdemo.network.ServerConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final static String TAG = "MapsActivity";
    private static LatLng towerBridge = new LatLng(51.5088, -0.0693);

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private ScheduleProvider scheduler;
    private LatLng myLocation = towerBridge;

    private CompositeDisposable bin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bin = new CompositeDisposable();
        scheduler = new AppScheduleProvider();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Logic to handle location object
                            Log.i(TAG, "Lat: "+location.getLatitude()+", Lng: "+location.getLongitude());
                        }
                    }
                });


        getChucksFromApi("e1w");


    }



    private void getChucksFromApi(final String postcode) {
        bin.add(ServerConnection
                .getJustEatServerConnection()
                .getJustEatList(postcode)
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(new Consumer<JustEatModel>() {
                    @Override
                    public void accept(JustEatModel chuckModel) throws Exception {
                        ArrayList<Restaurant> restaurants = new ArrayList<>(chuckModel.getRestaurants());

                        for (Restaurant r : restaurants) {
                            Log.d(TAG, "Drive distance:" + r.getDriveDistance());
                        }
                        Log.d(TAG, restaurants.size() + " total restaurants in " + postcode);
                        addLocationsToMap(restaurants);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    private void addLocationsToMap(ArrayList<Restaurant> restaurantList) {
        double range = 1.50;
        int count = 0;
        for (Restaurant r : restaurantList) {
            LatLng loc = new LatLng(r.getLatitude(), r.getLongitude());
            if (r.getDriveDistance() != null && r.getDriveDistance() < range) {
                count++;
                mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title(r.getName())
                        .draggable(true));
            }
        }
        Log.i(TAG, count+" restaurants found.");
        Toast.makeText(this, count+" restaurants found.", Toast.LENGTH_SHORT).show();
        bin.dispose();
        bin.clear();
    }


    private String getPostcodeFromLatLng(LatLng position) {
        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> address = null;
        try {
            address = geoCoder.getFromLocation(position.latitude, position.longitude, 1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (address.size() > 0) {
            String postCode = address.get(0).getPostalCode();
            for (Address a : address) {
                Log.d(TAG, a.getPhone());
            }
            return postCode;
        }
        return null;
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
        mMap.addMarker(new MarkerOptions()
                .position(towerBridge)
                .title("Marker in Sydney")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(towerBridge, 16.0f));
    }
}
