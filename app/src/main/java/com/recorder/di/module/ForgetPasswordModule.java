package com.recorder.di.module;

import com.core.di.scope.ActivityScope;
import com.recorder.mvp.contract.ForgetPasswordContract;
import com.recorder.mvp.model.ForgetPasswordModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ForgetPasswordModule {
    private ForgetPasswordContract.View view;

    /**
     * 构建ForgetPasswordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ForgetPasswordModule(ForgetPasswordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ForgetPasswordContract.View provideForgetPasswordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ForgetPasswordContract.Model provideForgetPasswordModel(ForgetPasswordModel model) {
        return model;
    }
}
