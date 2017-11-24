package com.recorder.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;

import javax.inject.Inject;

import com.recorder.mvp.contract.MyInvestmentContract;

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
}
