package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.EquityDetailsModule;

import com.recorder.mvp.ui.activity.EquityDetailsActivity;

@ActivityScope
@Component(modules = EquityDetailsModule.class, dependencies = AppComponent.class)
public interface EquityDetailsComponent {
    void inject(EquityDetailsActivity activity);
}
