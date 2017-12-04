package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.core.utils.DeviceUtils;
import com.google.gson.Gson;
import com.recorder.mvp.contract.EquityDetailsContract;
import com.recorder.mvp.model.api.cache.ApiCache;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.DealDetailBean;
import com.recorder.mvp.model.entity.PayCheckBean;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;

@ActivityScope
public class EquityDetailsModel extends BaseModel implements EquityDetailsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public EquityDetailsModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<DealDetailBean> dealDetail(String dealID) {
        return Observable.just(mRepositoryManager.obtainRetrofitService(ApiService.class).dealDetail("1", dealID))
                .flatMap(resultObservable -> mRepositoryManager.obtainCacheService(ApiCache.class)
                        .dealDetail(resultObservable, new EvictProvider(DeviceUtils.netIsConnected(mApplication)))
                        .map(Reply::getData));
    }

    @Override
    public Observable<Object> dealConsult(String dealID) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).dealConsult("1", dealID);
    }

    @Override
    public Observable<Object> dealUnfollow(String dealID) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).dealUnfollow("1", dealID);
    }

    @Override
    public Observable<Object> dealFollow(String dealID) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).dealFollow("1", dealID);
    }

    @Override
    public Observable<PayCheckBean> payCheck(String dealID) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).payCheck("1", dealID);
    }
}
