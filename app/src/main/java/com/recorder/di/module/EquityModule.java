package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.EquityContract;
import com.recorder.mvp.model.EquityModel;

@Module
public class EquityModule {
    private EquityContract.View view;

    /**
     * 构建EquityModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public EquityModule(EquityContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    EquityContract.View provideEquityView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    EquityContract.Model provideEquityModel(EquityModel model) {
        return model;
    }
}
