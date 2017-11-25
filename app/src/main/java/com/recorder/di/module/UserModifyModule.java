package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.UserModifyContract;
import com.recorder.mvp.model.UserModifyModel;

@Module
public class UserModifyModule {
    private UserModifyContract.View view;

    /**
     * 构建UserModifyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public UserModifyModule(UserModifyContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UserModifyContract.View provideUserModifyView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    UserModifyContract.Model provideUserModifyModel(UserModifyModel model) {
        return model;
    }
}
