package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.CashierModule;

import com.recorder.mvp.ui.activity.CashierActivity;

@ActivityScope
@Component(modules = CashierModule.class, dependencies = AppComponent.class)
public interface CashierComponent {
    void inject(CashierActivity activity);
}
