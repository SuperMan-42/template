package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.DealRecommendContract;
import com.recorder.mvp.model.DealRecommendModel;

@Module
public class DealRecommendModule {
    private DealRecommendContract.View view;

    /**
     * 构建DealRecommendModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DealRecommendModule(DealRecommendContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DealRecommendContract.View provideDealRecommendView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DealRecommendContract.Model provideDealRecommendModel(DealRecommendModel model) {
        return model;
    }
}
