package com.recorder.mvp.model.api.service;

import com.recorder.mvp.model.entity.ReferFilter;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by hpw on 17-11-18.
 */

public interface Service {
    @GET("refer/filter")
    Observable<ReferFilter> getFilter();
}
