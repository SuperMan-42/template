package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.MyInvestmentContract;
import com.recorder.mvp.model.MyInvestmentModel;

@Module
public class MyInvestmentModule {
    private MyInvestmentContract.View view;

    /**
     * 构建MyInvestmentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyInvestmentModule(MyInvestmentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyInvestmentContract.View provideMyInvestmentView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyInvestmentContract.Model provideMyInvestmentModel(MyInvestmentModel model) {
        return model;
    }
}
