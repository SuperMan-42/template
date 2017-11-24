package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.LoginContract;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.LoginBean;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public LoginModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<LoginBean> login(String mobile, String password) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).login("1", mobile, password);
    }
}
