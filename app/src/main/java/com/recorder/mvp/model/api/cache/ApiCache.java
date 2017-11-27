package com.recorder.mvp.model.api.cache;

import com.recorder.mvp.model.entity.DealFilter;
import com.recorder.mvp.model.entity.EquityBean;
import com.recorder.mvp.model.entity.NewsListBean;
import com.recorder.mvp.model.entity.UserInfoBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

/**
 * Created by hpw on 17-11-20.
 */

public interface ApiCache {

    Observable<Reply<EquityBean>> getEquity(Observable<EquityBean> equityBeanObservable, EvictProvider evictProvider);

    Observable<Reply<UserInfoBean>> userInfo(Observable<UserInfoBean> userInfoBeanObservable, EvictProvider evictProvider);

    Observable<Reply<NewsListBean>> newsList(Observable<NewsListBean> newsListBeanObservable, EvictProvider evictProvider);

    @LifeCache(duration = 2, timeUnit = TimeUnit.HOURS)
    Observable<Reply<DealFilter>> dealFilter(Observable<DealFilter> newsListBeanObservable, EvictProvider evictProvider);
}
