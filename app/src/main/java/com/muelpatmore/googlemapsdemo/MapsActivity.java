package com.muelpatmore.googlemapsdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
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
import com.muelpatmore.googlemapsdemo.network.FetchLocationCompleteMessage;
import com.muelpatmore.googlemapsdemo.network.ScheduleProvider;
import com.muelpatmore.googlemapsdemo.network.ServerConnection;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final static String TAG = "MapsActivity";
    private final static LatLng DEFAULT_LOCATION = new LatLng(51.5088, -0.0693); // default location
    private final static String DEFAULT_POSTCODE = "e1w"; // default postcode value

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private ScheduleProvider mScheduleProvider;
    private Marker myLocation;
    private LatLng myLoc = DEFAULT_LOCATION; //default instantiation
    private ArrayList<Restaurant> mRestaurantArrayList;
    private LatLng lastLatLng = DEFAULT_LOCATION;
    private String lastPostcode = DEFAULT_POSTCODE;

    private CompositeDisposable bin;


    /**
     * @Override
     * Initialises data and starts process to search by the user's location.
     * @param savedInstanceState stored state of the app before it stopped.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        EventBus.getDefault().register(this);
        bin = new CompositeDisposable();
        mScheduleProvider = new AppScheduleProvider();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        searchRestaurantsByUserLocation();
    }
    /**
     * Retrieve user location by GPS then pass this down the lookup chain.
     * If location cannot be found a default location is used.
     */
    public void searchRestaurantsByUserLocation() {
        checkPermissionsCoarseLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(this,
                        new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(@NotNull Location location) {
                                    if (location != null) {
                                        Log.i(TAG, "Lat: "+location.getLatitude()+", Lng: "+location.getLongitude());
                                        myLoc = new LatLng(location.getLatitude(),location.getLongitude());
                                        Toast.makeText(MapsActivity.this,
                                                Double.toString(myLoc.latitude)+", "+ Double.toString(myLoc.longitude),
                                                Toast.LENGTH_SHORT).show();
                                        //getPostcodeFromLatLng(myLoc); // old method DEPRECATED
                                        Log.i(TAG, "posting to intent service");
                                        startIntentService(location);

                                    } else {
                                        myLoc = DEFAULT_LOCATION;
                                        Toast.makeText(MapsActivity.this, "Default location used", Toast.LENGTH_SHORT).show();
                                    }
                                    plotUserLocation(myLoc);
                                }

                            });
    }

    private void startIntentService(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FetchLocationCompleteMessage event) {
        Log.i(TAG, "Message recieved from intent service.");
        if (event.coordinates != null) {
            this.lastLatLng = event.coordinates;
        }
        if (event.postCode != null) {
            this.lastPostcode = event.postCode;
        } else {
            Toast.makeText(this, "Cannot resolve postcode from GPS.", Toast.LENGTH_SHORT).show();
            getPostcodeFromLatLng(lastLatLng);
        }


        plotUserLocation(lastLatLng);
        getRestaurantListFromApi(lastPostcode);
    }

    /**
     *
     * @param myLoc
     */
    private void plotUserLocation(LatLng myLoc) {
        if (myLocation != null) {
            myLocation.remove();
        }
        myLocation = mMap.addMarker(new MarkerOptions()
            .position(myLoc)
            .title("Your location")
            .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16.0f));
    }

    /**
     * Makes connection to JustEat server API for a list of restaurants in the selected postcode.
     * Results are auto-added to the map;
     * @param postcode String postcode in which restaurants are being searched for.
     */
    private void getRestaurantListFromApi(final String postcode) {
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

    /**
     * Take a list of Restaurant objects and add each to the GoogleMaps by location.
     * @param restaurantList
     */
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
        Toast.makeText(this, count + " restaurants found.", Toast.LENGTH_SHORT).show();

        // set click listener to open a Snackbar on click.
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!marker.equals(myLocation)) {
                    int position = (int)(marker.getTag());
                    infoSnackBar(mRestaurantArrayList.get(position));
                }
                return false;
            }
        });
    }


    private void getPostcodeFromLatLng(LatLng position) {
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
                            getRestaurantListFromApi(DEFAULT_POSTCODE);
                        } else {
                            getRestaurantListFromApi(apiPostcode);
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
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Test", 8000);
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

        TextView tvRatingAverage = (TextView) snackView.findViewById(R.id.tvRatingAverage);;
        Log.e(TAG, "TV null pointer: "+tvRatingAverage);
        String averageRating = Double.toString(restaurant.getRatingAverage());
        if(averageRating != null) {tvRatingAverage.setText(averageRating);}

        TextView tvOpenNow = (TextView) snackView.findViewById(R.id.tvOpenNow);
        String openNow = restaurant.getIsOpenNow() ? "Open now" : "Closed";
        if(openNow != null) {tvOpenNow.setText(openNow);}

        ImageView ivRestaurantImage = (ImageView) snackView.findViewById(R.id.ivRestaurantImage);
        String restaurantImageUrl = restaurant.getLogo().get(0).getStandardResolutionURL();
        if (restaurantImageUrl != null) {
            Picasso
                .with(this)
                .load(restaurant.getLogo().get(0).getStandardResolutionURL())
                .resize(100, 100)
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
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
