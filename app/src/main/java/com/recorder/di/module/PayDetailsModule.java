package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.PayDetailsContract;
import com.recorder.mvp.model.PayDetailsModel;

@Module
public class PayDetailsModule {
    private PayDetailsContract.View view;

    /**
     * 构建PayDetailsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PayDetailsModule(PayDetailsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PayDetailsContract.View providePayDetailsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PayDetailsContract.Model providePayDetailsModel(PayDetailsModel model) {
        return model;
    }
}
