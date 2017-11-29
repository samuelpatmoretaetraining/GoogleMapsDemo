package com.muelpatmore.googlemapsdemo.network.utils;

/**
 * Created by Samuel on 29/11/2017.
 */

public class Constants {

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



    /**
     * Prevent instantiation of this utility class
     */
    private Constants() {}
}
