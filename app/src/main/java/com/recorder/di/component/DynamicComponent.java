package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.DynamicModule;

import com.recorder.mvp.ui.fragment.DynamicFragment;

@ActivityScope
@Component(modules = DynamicModule.class, dependencies = AppComponent.class)
public interface DynamicComponent {
    void inject(DynamicFragment fragment);
}
