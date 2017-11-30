package com.muelpatmore.googlemapsdemo.network.utils;

/**
 * Created by Samuel on 29/11/2017.
 */

public class Constants {

    public static final int SUCCESS_RESULT = 0;

    public static final int FAILURE_RESULT = 1;

    private static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";

    static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

    static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    //Just eat example request:  https://public.je-apis.com/restaurants?q=se19
    public static final String JUST_EAT_BASE_URL = "https://public.je-apis.com/";
    public static final String RESTAURANT_QUERY = "restaurants";

    public static final String HEADER_ACCEPT_TENANT = "Accept-Tenant: uk";
    public static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language: en-GB";
    public static final String HEADER_AUTHORIZATION = "Authorization: Basic VGVjaFRlc3RBUEk6dXNlcjI=";
    public static final String HEADER_HOST = "Host: public.je-apis.com";

    //Google geolocate example:
    //https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyArTxZ9Y_dairdwLEp8Rpix5G5SeDxPd_w
    public static final String GOOGLE_BASE_USL = "https://www.googleapis.com/";
    public static final String GEOLOCATE_QUERY = "geolocation/v1/geolocate";
    public static final String GOOGLE_API_KEY = "AIzaSyArTxZ9Y_dairdwLEp8Rpix5G5SeDxPd_w";
    // spare string AIzaSyBX4okVoB-lGinjzSaY1L1I_AOC3l5gvrs


    //Postcode lookup
    //https:api.postcodes.io/postcodes?lon=-0.0693&lat=51.5088
    public static final String POSTCODE_BASE_URL = "https:api.postcodes.io/";
    public static final String POSTCODE_QUERY = "postcodes";


    /**
     * Prevent instantiation of this utility class
     */
    private Constants() {}
}
