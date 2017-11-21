package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.AuthInfoContract;
import com.recorder.mvp.model.AuthInfoModel;

@Module
public class AuthInfoModule {
    private AuthInfoContract.View view;

    /**
     * 构建AuthInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AuthInfoModule(AuthInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AuthInfoContract.View provideAuthInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AuthInfoContract.Model provideAuthInfoModel(AuthInfoModel model) {
        return model;
    }
}
