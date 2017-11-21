package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.NewPasswordContract;
import com.recorder.mvp.model.NewPasswordModel;

@Module
public class NewPasswordModule {
    private NewPasswordContract.View view;

    /**
     * 构建NewPasswordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public NewPasswordModule(NewPasswordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    NewPasswordContract.View provideNewPasswordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    NewPasswordContract.Model provideNewPasswordModel(NewPasswordModel model) {
        return model;
    }
}
