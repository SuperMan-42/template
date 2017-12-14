package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.ForgetPasswordContract;
import com.recorder.mvp.model.api.service.ApiService;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

@ActivityScope
public class ForgetPasswordModel extends BaseModel implements ForgetPasswordContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ForgetPasswordModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<Object> smsVerify(String mobile, String code, String type) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).smsVerify("1", mobile, code, type);
    }

    @Override
    public Observable<ResponseBody> verify(String token) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).verify("1", token);
    }
}
