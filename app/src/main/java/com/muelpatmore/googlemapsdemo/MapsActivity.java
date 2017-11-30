package com.muelpatmore.googlemapsdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final static String TAG = "MapsActivity";
    private final static LatLng DEFAULT_LOCATION = new LatLng(51.5088, -0.0693); // default location

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private ScheduleProvider mScheduleProvider;
    private LatLng myLoc = DEFAULT_LOCATION; //default instantiation
    private ArrayList<Restaurant> mRestaurantArrayList;

    private CompositeDisposable bin;


    @SuppressLint("MissingPermission") // permissions handled b
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bin = new CompositeDisposable();
        mScheduleProvider = new AppScheduleProvider();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        checkPermissionsFineLocation();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.i(TAG, "Lat: "+location.getLatitude()+", Lng: "+location.getLongitude());
                            myLoc = new LatLng(location.getLatitude(),location.getLongitude());
                            getPostcodeFromLatLng(myLoc);
                        }
                    }
                });
    }



    private void getChucksFromApi(final String postcode) {
        bin.add(ServerConnection
                .getJustEatServerConnection()
                .getJustEatList(postcode)
                .observeOn(mScheduleProvider.ui())
                .subscribeOn(mScheduleProvider.io())
                .subscribe(new Consumer<JustEatModel>() {
                    @Override
                    public void accept(JustEatModel chuckModel) throws Exception {
                        ArrayList<Restaurant> restaurants = new ArrayList<>(chuckModel.getRestaurants());
                        mRestaurantArrayList = restaurants;
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
        mRestaurantArrayList = restaurantList;
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
                infoSnackBar(mRestaurantArrayList.get(position));
                return false;
            }
        });
    }


    private void getPostcodeFromLatLng(LatLng position) {
        final String postcode = "e1w";
        bin.add(ServerConnection.getPostcodeServerConnection()
                .getPostCode(Double.toString(position.longitude),
                            Double.toString(position.latitude))
                .observeOn(mScheduleProvider.ui())
                .subscribeOn(mScheduleProvider.io())
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
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the default Snackbar TextView
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View snackView = inflater.inflate(R.layout.snackbar_layout, null);

        // Configure the view
        TextView tvRestaurantName = (TextView) snackView.findViewById(R.id.tvRestaurantName);
        tvRestaurantName.setText(restaurant.getName());
        Log.i(TAG, restaurant.getName()+" selected");

        TextView tvRatingAverage = (TextView) findViewById(R.id.tvRatingAverage);;
        Log.e(TAG, "TV null pointer: "+tvRatingAverage);
        String averageRating = Double.toString(restaurant.getRatingAverage());
        Log.w(TAG, "Null pointer spot: "+averageRating.getClass().getName());
        if(averageRating != null) {tvRatingAverage.setText(averageRating);}

        TextView tvOpenNow = (TextView) findViewById(R.id.tvOpenNow);
        String openNow = restaurant.getIsOpenNow() ? "Open now" : "Closed";
        if(openNow != null) {tvOpenNow.setText(openNow);}

        ImageView ivRestaurantImage = (ImageView) snackView.findViewById(R.id.ivRestaurantImage);
        String restaurantImageUrl = restaurant.getLogo().get(0).getStandardResolutionURL();
        if (restaurantImageUrl != null) {
            Picasso
                    .with(this)
                    .load(restaurant.getLogo().get(0).getStandardResolutionURL())
                    .resize(100, 100) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                    .into(ivRestaurantImage);
        }

        // Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
        // Show the Snackbar
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
                .position(DEFAULT_LOCATION)
                .title("Marker in Sydney")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 16.0f));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bin.clear();
        bin.dispose();
    }

    /**
     * Ensure user has given app permission to use the device's COARSE_LOCATION.
     */
    private void checkPermissionsCoarseLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);
            return;
        }
    }


    /**
     * Ensure user has given app permission to use the device's FINE_LOCATION.
     */
    private void checkPermissionsFineLocation() {
        checkPermissionsCoarseLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);
            return;
        }
    }

    /**
     * Ensure user has given app permission to use the device's INTERNET connection.
     */
    private void checkPermissionsInternet() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},0);
            return;
        }
    }
}
