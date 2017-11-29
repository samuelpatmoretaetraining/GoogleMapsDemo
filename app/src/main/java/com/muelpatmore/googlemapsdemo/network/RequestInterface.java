package com.muelpatmore.googlemapsdemo.network;

import com.muelpatmore.googlemapsdemo.apimodels.ChuckModel;
import com.muelpatmore.googlemapsdemo.network.Constants;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

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
    @GET(Constants.QUERY)
    Observable<ChuckModel> getChuckList();
}
