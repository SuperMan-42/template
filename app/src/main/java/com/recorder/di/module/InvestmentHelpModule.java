package com.recorder.di.module;

import com.core.di.scope.ActivityScope;
import com.recorder.mvp.contract.InvestmentHelpContract;
import com.recorder.mvp.model.InvestmentHelpModel;

import dagger.Module;
import dagger.Provides;

@Module
public class InvestmentHelpModule {
    private InvestmentHelpContract.View view;

    /**
     * 构建InvestmentHelpModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public InvestmentHelpModule(InvestmentHelpContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InvestmentHelpContract.View provideInvestmentHelpView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    InvestmentHelpContract.Model provideInvestmentHelpModel(InvestmentHelpModel model) {
        return model;
    }
}
