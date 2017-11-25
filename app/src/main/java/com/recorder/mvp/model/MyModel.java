package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.core.utils.DeviceUtils;
import com.google.gson.Gson;
import com.recorder.mvp.contract.MyContract;
import com.recorder.mvp.model.api.cache.ApiCache;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.UserInfoBean;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;

@ActivityScope
public class MyModel extends BaseModel implements MyContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MyModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<UserInfoBean> userInfo() {
        return Observable.just(mRepositoryManager.obtainRetrofitService(ApiService.class).userInfo("1"))
                .flatMap(resultObservable -> mRepositoryManager.obtainCacheService(ApiCache.class)
                        .userInfo(resultObservable, new EvictProvider(DeviceUtils.netIsConnected(mApplication)))
                        .map(Reply::getData));
    }
}
