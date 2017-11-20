package com.recorder.mvp.model.api.cache;

import com.recorder.mvp.model.entity.ReferFilter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

/**
 * Created by hpw on 17-11-20.
 */

public interface Cache {
    @LifeCache(duration = 2, timeUnit = TimeUnit.DAYS)
    Observable<Reply<ReferFilter>> getFilter(Observable<ReferFilter> oUsers, EvictProvider evictProvider);
}
