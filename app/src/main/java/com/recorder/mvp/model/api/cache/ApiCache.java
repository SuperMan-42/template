package com.recorder.mvp.model.api.cache;

import com.recorder.mvp.model.entity.EquityBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

/**
 * Created by hpw on 17-11-20.
 */

public interface ApiCache {

    @LifeCache(duration = 2, timeUnit = TimeUnit.DAYS)
    Observable<Reply<EquityBean>> getEquity(Observable<EquityBean> oUsers, EvictProvider evictProvider);
}
