package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.HelpListContract;
import com.recorder.mvp.model.HelpListModel;

@Module
public class HelpListModule {
    private HelpListContract.View view;

    /**
     * 构建HelpListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HelpListModule(HelpListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HelpListContract.View provideHelpListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HelpListContract.Model provideHelpListModel(HelpListModel model) {
        return model;
    }
}
