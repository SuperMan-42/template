package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.ModifyPasswordContract;
import com.recorder.mvp.model.api.service.ApiService;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class ModifyPasswordModel extends BaseModel implements ModifyPasswordContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ModifyPasswordModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<Object> smsCode(String mobile, String type, String captcha) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).smsCode("1", mobile, type, captcha);
    }

    @Override
    public Observable<Object> userModifypwd(String old_password, String password, String code) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).userModifypwd("1", old_password, password, code);
    }
}
