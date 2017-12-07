package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.UploadContract;
import com.recorder.mvp.model.UploadModel;

@Module
public class UploadModule {
    private UploadContract.View view;

    /**
     * 构建UploadModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public UploadModule(UploadContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UploadContract.View provideUploadView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    UploadContract.Model provideUploadModel(UploadModel model) {
        return model;
    }
}
