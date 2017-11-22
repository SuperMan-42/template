package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.BuyModule;

import com.recorder.mvp.ui.activity.BuyActivity;

@ActivityScope
@Component(modules = BuyModule.class, dependencies = AppComponent.class)
public interface BuyComponent {
    void inject(BuyActivity activity);
}
