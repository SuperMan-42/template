package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.HomeContract;
import com.recorder.mvp.model.api.cache.Cache;
import com.recorder.mvp.model.api.service.Service;
import com.recorder.mvp.model.entity.ReferFilter;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;

@ActivityScope
public class HomeModel extends BaseModel implements HomeContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public HomeModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<ReferFilter> getFilter() {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Service.class)
                .getFilter())
                .flatMap(resultObservable -> mRepositoryManager.obtainCacheService(Cache.class)
                        .getFilter(resultObservable, new EvictProvider(false))
                        .map(Reply::getData));
    }
}