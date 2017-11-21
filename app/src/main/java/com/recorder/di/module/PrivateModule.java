package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.PrivateContract;
import com.recorder.mvp.model.PrivateModel;

@Module
public class PrivateModule {
    private PrivateContract.View view;

    /**
     * 构建PrivateModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PrivateModule(PrivateContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PrivateContract.View providePrivateView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PrivateContract.Model providePrivateModel(PrivateModel model) {
        return model;
    }
}
