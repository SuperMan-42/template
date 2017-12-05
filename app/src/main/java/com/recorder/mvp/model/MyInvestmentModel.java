package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.MyInvestmentContract;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.OrderListBean;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class MyInvestmentModel extends BaseModel implements MyInvestmentContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MyInvestmentModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<OrderListBean> orderList(String page, String page_size) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class)
                .orderList("1", page, page_size);
    }
}
