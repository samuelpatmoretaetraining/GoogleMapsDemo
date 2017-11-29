package com.muelpatmore.googlemapsdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.muelpatmore.googlemapsdemo.apimodels.justeat.JustEatModel;
import com.muelpatmore.googlemapsdemo.apimodels.justeat.Restaurant;
import com.muelpatmore.googlemapsdemo.apimodels.postcodes.PostcodeResultModel;
import com.muelpatmore.googlemapsdemo.network.AppScheduleProvider;
import com.muelpatmore.googlemapsdemo.network.ScheduleProvider;
import com.muelpatmore.googlemapsdemo.network.ServerConnection;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final static String TAG = "MapsActivity";
    private static LatLng towerBridge = new LatLng(51.5088, -0.0693);

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private ScheduleProvider scheduler;
    private LatLng myLoc = towerBridge; //default instantiation
    ArrayList<Restaurant> currentRestaurantData;

    private CompositeDisposable bin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
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
                            myLoc = new LatLng(location.getLatitude(),location.getLongitude());
                        }
                    }
                });


        getPostcodeFromLatLng(myLoc);
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
                        currentRestaurantData = restaurants;
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
        currentRestaurantData = restaurantList;
        double range = 1.00;
        int count = 0;
        for (Restaurant r : restaurantList) {
            LatLng loc = new LatLng(r.getLatitude(), r.getLongitude());
            if (r.getDriveDistance() != null && r.getDriveDistance() < range) {
                count++;
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title(r.getName())
                        .draggable(true));
                marker.setTag(restaurantList.indexOf(r));

            }
        }
        Log.i(TAG, count + " restaurants found.");
        Toast.makeText(this, count + " restaurants found.", Toast.LENGTH_SHORT).show();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int position = (int)(marker.getTag());
                infoSnackBar(currentRestaurantData.get(position));
                return false;
            }
        });
    }


    private void getPostcodeFromLatLng(LatLng position) {
        final String postcode = "e1w";
        bin.add(ServerConnection.getPostcodeServerConnection()
                .getPostCode(Double.toString(position.longitude),
                            Double.toString(position.latitude))
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(new Consumer<PostcodeResultModel>() {
                    @Override
                    public void accept(PostcodeResultModel postcodeResultModel) throws Exception {
                        String apiPostcode = postcodeResultModel.getResult().get(0).getPostcode();
                        if (apiPostcode == null) {
                            Toast.makeText(MapsActivity.this, "Default location used.", Toast.LENGTH_SHORT).show();
                            getChucksFromApi(postcode);
                        } else {
                            getChucksFromApi(apiPostcode);
                            Toast.makeText(MapsActivity.this, "Searching in "+apiPostcode, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    private void infoSnackBar(Restaurant restaurant) {
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Test", Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setVisibility(View.INVISIBLE);

//        // Inflate our custom view
//        View snackView = LayoutInflater.inflate(R.layout.my_snackbar, null);
//        // Configure the view
//        ImageView imageView = (ImageView) snackView.findViewById(R.id.image);
//        imageView.setImageBitmap(image);
//        TextView textViewTop = (TextView) snackView.findViewById(R.id.text);
//        textViewTop.setText(text);
//        textViewTop.setTextColor(Color.WHITE);
//
//        // Add the view to the Snackbar's layout
//        layout.addView(snackView, 0);
//        // Show the Snackbar
        snackbar.show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bin.clear();
        bin.dispose();
    }
}
