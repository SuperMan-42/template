package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.PayDetailsContract;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.PayPayBean;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class PayDetailsModel extends BaseModel implements PayDetailsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public PayDetailsModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<PayPayBean> orderPay(String orderID, String payment_way) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).orderPay("1", orderID, payment_way);
    }
}
