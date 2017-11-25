package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.NewPasswordContract;
import com.recorder.mvp.model.api.service.ApiService;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class NewPasswordModel extends BaseModel implements NewPasswordContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public NewPasswordModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<Object> userForgetpwd(String mobile, String code, String password) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).userForgetpwd("1", mobile, code, password);
    }
}
