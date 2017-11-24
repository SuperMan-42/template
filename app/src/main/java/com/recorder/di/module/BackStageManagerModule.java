package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.BackStageManagerContract;
import com.recorder.mvp.model.BackStageManagerModel;

@Module
public class BackStageManagerModule {
    private BackStageManagerContract.View view;

    /**
     * 构建BackStageManagerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public BackStageManagerModule(BackStageManagerContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    BackStageManagerContract.View provideBackStageManagerView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    BackStageManagerContract.Model provideBackStageManagerModel(BackStageManagerModel model) {
        return model;
    }
}
