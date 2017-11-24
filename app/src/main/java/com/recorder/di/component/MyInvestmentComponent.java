package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.MyInvestmentModule;

import com.recorder.mvp.ui.activity.MyInvestmentActivity;

@ActivityScope
@Component(modules = MyInvestmentModule.class, dependencies = AppComponent.class)
public interface MyInvestmentComponent {
    void inject(MyInvestmentActivity activity);
}
