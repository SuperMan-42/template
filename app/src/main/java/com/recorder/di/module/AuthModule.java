package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.AuthContract;
import com.recorder.mvp.model.AuthModel;

@Module
public class AuthModule {
    private AuthContract.View view;

    /**
     * 构建AuthModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AuthModule(AuthContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AuthContract.View provideAuthView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AuthContract.Model provideAuthModel(AuthModel model) {
        return model;
    }
}
