package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.SearchContract;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.EquityBean;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class SearchModel extends BaseModel implements SearchContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public SearchModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<EquityBean> dealList(String type, String label_id, String round_id, String keyword, String page, String page_size) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).dealList("1", type, label_id, round_id, keyword, page, page_size);
    }
}
