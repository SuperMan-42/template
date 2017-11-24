package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.MyAttentionContract;
import com.recorder.mvp.model.MyAttentionModel;

@Module
public class MyAttentionModule {
    private MyAttentionContract.View view;

    /**
     * 构建MyAttentionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyAttentionModule(MyAttentionContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyAttentionContract.View provideMyAttentionView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyAttentionContract.Model provideMyAttentionModel(MyAttentionModel model) {
        return model;
    }
}
