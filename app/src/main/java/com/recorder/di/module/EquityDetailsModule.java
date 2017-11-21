package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.EquityDetailsContract;
import com.recorder.mvp.model.EquityDetailsModel;

@Module
public class EquityDetailsModule {
    private EquityDetailsContract.View view;

    /**
     * 构建EquityDetailsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public EquityDetailsModule(EquityDetailsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    EquityDetailsContract.View provideEquityDetailsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    EquityDetailsContract.Model provideEquityDetailsModel(EquityDetailsModel model) {
        return model;
    }
}
