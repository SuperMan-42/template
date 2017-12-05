package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.CashierContract;
import com.recorder.mvp.model.CashierModel;

@Module
public class CashierModule {
    private CashierContract.View view;

    /**
     * 构建CashierModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CashierModule(CashierContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CashierContract.View provideCashierView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CashierContract.Model provideCashierModel(CashierModel model) {
        return model;
    }
}
