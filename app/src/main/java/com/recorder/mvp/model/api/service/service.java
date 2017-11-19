package com.recorder.mvp.model.api.service;

import com.recorder.mvp.model.entity.ReferFilter;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hpw on 17-11-18.
 */

public interface service {
    @GET("refer/filter")
    Observable<ReferFilter> getFilter(@Query("version") int version);
}
