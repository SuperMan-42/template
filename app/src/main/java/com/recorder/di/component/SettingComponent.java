package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.SettingModule;

import com.recorder.mvp.ui.activity.SettingActivity;

@ActivityScope
@Component(modules = SettingModule.class, dependencies = AppComponent.class)
public interface SettingComponent {
    void inject(SettingActivity activity);
}
