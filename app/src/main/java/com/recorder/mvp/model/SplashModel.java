package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.SplashContract;
import com.recorder.mvp.model.api.cache.ApiCache;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.AppStartBean;
import com.recorder.mvp.model.entity.AppVersionBean;
import com.recorder.utils.CommonUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;

@ActivityScope
public class SplashModel extends BaseModel implements SplashContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public SplashModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<AppStartBean> appStart() {
        return Observable.just(mRepositoryManager.obtainRetrofitService(ApiService.class).appStart("1"))
                .flatMap(resultObservable -> mRepositoryManager.obtainCacheService(ApiCache.class)
                        .appStart(resultObservable, new EvictProvider(CommonUtils.isEvict(mApplication)))
                        .map(Reply::getData));
    }

    @Override
    public Observable<AppVersionBean> appVersion() {
        return Observable.just(mRepositoryManager.obtainRetrofitService(ApiService.class).appVersion("1"))
                .flatMap(resultObservable -> mRepositoryManager.obtainCacheService(ApiCache.class)
                        .appVersion(resultObservable, new EvictProvider(CommonUtils.isEvict(mApplication)))
                        .map(Reply::getData));
    }
}
