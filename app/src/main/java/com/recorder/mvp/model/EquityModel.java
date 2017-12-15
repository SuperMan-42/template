package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.EquityContract;
import com.recorder.mvp.model.api.cache.ApiCache;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.EquityBean;
import com.recorder.utils.CommonUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;

@ActivityScope
public class EquityModel extends BaseModel implements EquityContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public EquityModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<EquityBean> dealList(String type, String label_id, String round_id, String keyword, String page, String page_size) {
        return Observable.just(mRepositoryManager.obtainRetrofitService(ApiService.class).dealList("1", type, label_id, round_id, keyword, page, page_size))
                .flatMap(resultObservable -> mRepositoryManager.obtainCacheService(ApiCache.class)
                        .dealList(resultObservable, new EvictProvider(CommonUtils.isEvict(mApplication)))
                        .map(Reply::getData));
    }
}
