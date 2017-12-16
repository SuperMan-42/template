package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.google.gson.Gson;
import com.recorder.mvp.contract.MyInvestmentContract;
import com.recorder.mvp.model.api.cache.ApiCache;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.OrderListBean;
import com.recorder.mvp.model.entity.PayPayBean;
import com.recorder.utils.CommonUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;

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
        return Observable.just(mRepositoryManager.obtainRetrofitService(ApiService.class).orderList("1", page, page_size))
                .flatMap(resultObservable -> mRepositoryManager.obtainCacheService(ApiCache.class)
                        .orderList(resultObservable, new EvictProvider(CommonUtils.isEvict(mApplication)))
                        .map(Reply::getData));
    }

    @Override
    public Observable<PayPayBean> orderPay(String orderID, String payment_way) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).orderPay("1", orderID, payment_way);
    }
}
