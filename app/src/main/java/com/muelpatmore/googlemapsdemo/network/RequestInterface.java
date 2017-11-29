package com.muelpatmore.googlemapsdemo.network;

import com.muelpatmore.googlemapsdemo.apimodels.JustEatModel;
import com.muelpatmore.googlemapsdemo.apimodels.geolocate.GeoLocateModel;
import com.muelpatmore.googlemapsdemo.network.utils.Constants;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Samuel on 29/11/2017.
 */

public interface RequestInterface {


    @Headers({
            Constants.HEADER_ACCEPT_TENANT,
            Constants.HEADER_ACCEPT_LANGUAGE,
            Constants.HEADER_AUTHORIZATION,
            Constants.HEADER_HOST
    })
    @GET(Constants.RESTAURANT_QUERY)
    Observable<JustEatModel> getJustEatList(@Query("q") String postcode);

    @POST(Constants.GEOLOCATE_QUERY)
    Observable<GeoLocateModel> getGeolocationData(@Query("key") String apiKey);
}
