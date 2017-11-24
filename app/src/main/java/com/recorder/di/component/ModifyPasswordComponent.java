package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.ModifyPasswordModule;

import com.recorder.mvp.ui.activity.ModifyPasswordActivity;

@ActivityScope
@Component(modules = ModifyPasswordModule.class, dependencies = AppComponent.class)
public interface ModifyPasswordComponent {
    void inject(ModifyPasswordActivity activity);
}
