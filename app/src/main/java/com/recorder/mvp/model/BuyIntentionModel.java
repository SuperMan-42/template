package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.BuyIntentionContract;
import com.recorder.mvp.model.api.service.ApiService;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class BuyIntentionModel extends BaseModel implements BuyIntentionContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public BuyIntentionModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<Object> orderIntention(String dealID, String amount) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).orderIntention("1", dealID, amount);
    }
}
