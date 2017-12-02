package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.DealRecommendContract;
import com.recorder.mvp.model.api.service.ApiService;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class DealRecommendModel extends BaseModel implements DealRecommendContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public DealRecommendModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<Object> dealRecommend(String deal_name, String industry, String requirement, String contact, String phone, String business, String team) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).dealRecommend("1", deal_name, industry, requirement, contact, phone, business, team);
    }
}
