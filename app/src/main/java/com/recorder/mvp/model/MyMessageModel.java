package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.core.utils.DeviceUtils;
import com.google.gson.Gson;
import com.recorder.mvp.contract.MyMessageContract;
import com.recorder.mvp.model.api.cache.ApiCache;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.AppMsgsBean;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;

@ActivityScope
public class MyMessageModel extends BaseModel implements MyMessageContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MyMessageModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<AppMsgsBean> appMsgs(String page, String page_size) {
        return Observable.just(mRepositoryManager.obtainRetrofitService(ApiService.class).appMsgs("1", page, page_size))
                .flatMap(resultObservable -> mRepositoryManager.obtainCacheService(ApiCache.class)
                        .appMsgs(resultObservable, new EvictProvider(DeviceUtils.netIsConnected(mApplication)))
                        .map(Reply::getData));
    }
}
