package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.BuyIntentionContract;
import com.recorder.mvp.model.BuyIntentionModel;

@Module
public class BuyIntentionModule {
    private BuyIntentionContract.View view;

    /**
     * 构建BuyIntentionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public BuyIntentionModule(BuyIntentionContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    BuyIntentionContract.View provideBuyIntentionView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    BuyIntentionContract.Model provideBuyIntentionModel(BuyIntentionModel model) {
        return model;
    }
}
