package com.recorder.mvp.model.api.cache;

import com.recorder.mvp.model.entity.AppMsgsBean;
import com.recorder.mvp.model.entity.AppStartBean;
import com.recorder.mvp.model.entity.AppVersionBean;
import com.recorder.mvp.model.entity.AuthGetBean;
import com.recorder.mvp.model.entity.DealDetailBean;
import com.recorder.mvp.model.entity.DealFilter;
import com.recorder.mvp.model.entity.EquityBean;
import com.recorder.mvp.model.entity.HelpContentBean;
import com.recorder.mvp.model.entity.HelpListBean;
import com.recorder.mvp.model.entity.HomeRecommendBean;
import com.recorder.mvp.model.entity.NewsListBean;
import com.recorder.mvp.model.entity.OrderListBean;
import com.recorder.mvp.model.entity.OrderPimanageBean;
import com.recorder.mvp.model.entity.UserAuthInfoBean;
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

    Observable<Reply<EquityBean>> dealEquityList(Observable<EquityBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<EquityBean>> dealList(Observable<EquityBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<UserInfoBean>> userInfo(Observable<UserInfoBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<NewsListBean>> newsList(Observable<NewsListBean> resultObservable, EvictProvider evictProvider);

    @LifeCache(duration = 2, timeUnit = TimeUnit.HOURS)
    Observable<Reply<DealFilter>> dealFilter(Observable<DealFilter> resultObservable, EvictProvider evictProvider);

    Observable<Reply<HomeRecommendBean>> homeRecommend(Observable<HomeRecommendBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<DealDetailBean>> dealDetail(Observable<DealDetailBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<UserFollowListBean>> userFollowlist(Observable<UserFollowListBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<OrderPimanageBean>> orderPimanage(Observable<OrderPimanageBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<AppStartBean>> appStart(Observable<AppStartBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<HelpListBean>> helpList(Observable<HelpListBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<HelpContentBean>> helpContent(Observable<HelpContentBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<UserAuthInfoBean>> userAuthInfo(Observable<UserAuthInfoBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<AuthGetBean>> authGet(Observable<AuthGetBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<AppMsgsBean>> appMsgs(Observable<AppMsgsBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<OrderListBean>> orderList(Observable<OrderListBean> resultObservable, EvictProvider evictProvider);

    Observable<Reply<AppVersionBean>> appVersion(Observable<AppVersionBean> resultObservable, EvictProvider evictProvider);
}
