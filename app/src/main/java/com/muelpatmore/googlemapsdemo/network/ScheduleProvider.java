package com.muelpatmore.googlemapsdemo.network;

import io.reactivex.Scheduler;

/**
 * Created by Samuel on 29/11/2017.
 */

public interface ScheduleProvider {
    Scheduler ui();

    Scheduler computation();

    Scheduler io();
}
