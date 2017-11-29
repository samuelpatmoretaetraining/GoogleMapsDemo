package com.muelpatmore.googlemapsdemo.network;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Samuel on 29/11/2017.
 */

public class AppScheduleProvider implements ScheduleProvider {

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler io() {
            return Schedulers.io();
        }
}
