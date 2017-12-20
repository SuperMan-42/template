package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.FeedBackContract;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.utils.CommonUtils;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class FeedBackModel extends BaseModel implements FeedBackContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public FeedBackModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<Object> appFeedback(String content, String contact) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).appFeedback("1", content, CommonUtils.isNull(contact));
    }
}
