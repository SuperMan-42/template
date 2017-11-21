package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.PrivateModule;

import com.recorder.mvp.ui.fragment.PrivateFragment;

@ActivityScope
@Component(modules = PrivateModule.class, dependencies = AppComponent.class)
public interface PrivateComponent {
    void inject(PrivateFragment fragment);
}
