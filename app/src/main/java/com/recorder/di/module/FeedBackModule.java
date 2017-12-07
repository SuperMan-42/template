package com.recorder.di.module;

import com.core.di.scope.ActivityScope;
import com.recorder.mvp.contract.FeedBackContract;
import com.recorder.mvp.model.FeedBackModel;

import dagger.Module;
import dagger.Provides;

@Module
public class FeedBackModule {
    private FeedBackContract.View view;

    /**
     * 构建FeedBackModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FeedBackModule(FeedBackContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FeedBackContract.View provideFeedBackView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FeedBackContract.Model provideFeedBackModel(FeedBackModel model) {
        return model;
    }
}
