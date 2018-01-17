package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.BuyIntentionModule;

import com.recorder.mvp.ui.activity.BuyIntentionActivity;

@ActivityScope
@Component(modules = BuyIntentionModule.class, dependencies = AppComponent.class)
public interface BuyIntentionComponent {
    void inject(BuyIntentionActivity activity);
}
