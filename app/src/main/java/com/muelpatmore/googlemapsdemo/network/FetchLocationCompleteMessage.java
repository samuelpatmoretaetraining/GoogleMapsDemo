package com.muelpatmore.googlemapsdemo.network;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Samuel on 30/11/2017.
 */

public class FetchLocationCompleteMessage {

    private static final String TAG = "FetchLocationComplete";

    //Address address;
    public Double latitude, longitude;
    public String fullString, countryCode, countryName, locality, subLocality, postCode, phone;
    public LatLng coordinates = null;
    public ArrayList<String> addresses;


    public FetchLocationCompleteMessage(Address address) {
        //this.address = address;
        fullString = address.toString();
        if(address.hasLatitude() && address.hasLongitude()) {
            latitude = address.getLatitude();
            longitude = address.getLongitude();
            coordinates = new LatLng(latitude, longitude);
        }
        countryCode = address.getCountryCode();
        countryName = address.getCountryName();
        locality = address.getLocality();
        subLocality = address.getSubLocality();
        postCode = address.getPostalCode();
        phone = address.getPhone();


        addresses = new ArrayList<String>();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            addresses.add(address.getAddressLine(i));
        }

        Log.i(TAG, "Created with: "+address.toString());
    }
}
