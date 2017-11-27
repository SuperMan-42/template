package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.PersonContract;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.ImageUploadBean;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@ActivityScope
public class PersonModel extends BaseModel implements PersonContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public PersonModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<ImageUploadBean> imageUpload(RequestBody type, List<MultipartBody.Part> images) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).imageUpload("1", type, images);
    }

    @Override
    public Observable<Object> userModify(String field, String user_name, String intro, String email, String weixin, String address, String avatar) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).userModify("1", field, user_name, intro, email, weixin, address, avatar);
    }
}
