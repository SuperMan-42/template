package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.PayDetailsModule;

import com.recorder.mvp.ui.activity.PayDetailsActivity;

@ActivityScope
@Component(modules = PayDetailsModule.class, dependencies = AppComponent.class)
public interface PayDetailsComponent {
    void inject(PayDetailsActivity activity);
}
