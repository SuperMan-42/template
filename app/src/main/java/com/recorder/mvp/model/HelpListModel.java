package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.core.utils.DeviceUtils;
import com.google.gson.Gson;
import com.recorder.mvp.contract.HelpListContract;
import com.recorder.mvp.model.api.cache.ApiCache;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.HelpContentBean;
import com.recorder.mvp.model.entity.HelpListBean;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;

@ActivityScope
public class HelpListModel extends BaseModel implements HelpListContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public HelpListModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<HelpListBean> helpList() {
        return Observable.just(mRepositoryManager.obtainRetrofitService(ApiService.class).helpList("1"))
                .flatMap(resultObservable -> mRepositoryManager.obtainCacheService(ApiCache.class)
                        .helpList(resultObservable, new EvictProvider(DeviceUtils.netIsConnected(mApplication)))
                        .map(Reply::getData));
    }

    @Override
    public Observable<HelpContentBean> helpContent(String helperID) {
        return Observable.just(mRepositoryManager.obtainRetrofitService(ApiService.class).helpContent("1", helperID))
                .flatMap(resultObservable -> mRepositoryManager.obtainCacheService(ApiCache.class)
                        .helpContent(resultObservable, new EvictProvider(DeviceUtils.netIsConnected(mApplication)))
                        .map(Reply::getData));
    }
}
