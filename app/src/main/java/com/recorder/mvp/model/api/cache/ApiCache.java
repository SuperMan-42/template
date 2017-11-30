package com.recorder.mvp.model.api.cache;

import com.recorder.mvp.model.entity.DealDetailBean;
import com.recorder.mvp.model.entity.DealFilter;
import com.recorder.mvp.model.entity.EquityBean;
import com.recorder.mvp.model.entity.HomeRecommendBean;
import com.recorder.mvp.model.entity.NewsListBean;
import com.recorder.mvp.model.entity.UserFollowListBean;
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

    Observable<Reply<EquityBean>> dealList(Observable<EquityBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<UserInfoBean>> userInfo(Observable<UserInfoBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<NewsListBean>> newsList(Observable<NewsListBean> resultObservable, EvictProvider evictProvider);

    @LifeCache(duration = 2, timeUnit = TimeUnit.HOURS)
    Observable<Reply<DealFilter>> dealFilter(Observable<DealFilter> resultObservable, EvictProvider evictProvider);

    Observable<Reply<HomeRecommendBean>> homeRecommend(Observable<HomeRecommendBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<DealDetailBean>> dealDetail(Observable<DealDetailBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<UserFollowListBean>> userFollowlist(Observable<UserFollowListBean> resultObservable, EvictProvider evictProvider);
}
