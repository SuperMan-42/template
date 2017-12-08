package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.UploadContract;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.ImageUploadBean;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

@ActivityScope
public class UploadModel extends BaseModel implements UploadContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public UploadModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<ImageUploadBean> imageUpload(List<MultipartBody.Part> images) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).imageUpload("1", images);
    }

    @Override
    public Observable<Object> orderProof(String orderID, String proof) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).orderProof("1", orderID, proof);
    }
}
