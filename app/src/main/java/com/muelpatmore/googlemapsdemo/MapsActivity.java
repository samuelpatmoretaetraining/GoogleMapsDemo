package com.muelpatmore.googlemapsdemo;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muelpatmore.googlemapsdemo.apimodels.JustEatModel;
import com.muelpatmore.googlemapsdemo.apimodels.Restaurant;
import com.muelpatmore.googlemapsdemo.network.AppScheduleProvider;
import com.muelpatmore.googlemapsdemo.network.ScheduleProvider;
import com.muelpatmore.googlemapsdemo.network.ServerConnection;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final static String TAG = "MapsActivity";
    private static LatLng towerBridge = new LatLng(51.5088, -0.0693);

    private GoogleMap mMap;
    private ScheduleProvider scheduler;

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

        getChucksFromApi("e1w");
    }

    private void getChucksFromApi(final String postcode) {
        bin.add(ServerConnection
                .getServerConnection()
                .getChuckList(postcode)
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(new Consumer<JustEatModel>() {
                    @Override
                    public void accept(JustEatModel chuckModel) throws Exception {
                        ArrayList<Restaurant> restaurants = new ArrayList<>(chuckModel.getRestaurants());

                        for (Restaurant r : restaurants) {
                            Log.d(TAG, "Drive distance:"+ r.getDriveDistance());
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
        Double range = 3.0;
        for(Restaurant r : restaurantList) {
            LatLng loc = new LatLng(r.getLatitude(), r.getLongitude());
            if (r.getDriveDistance() < 2.5000) {
                mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title(r.getName())
                        .draggable(true));
            } else {
                Log.i(TAG, r.getName() + "is more than 3.0 away. ("+ r.getDriveDistance()+ ")");
            }
        }
        bin.dispose();
        bin.clear();
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(towerBridge, 12.0f));
    }
}
