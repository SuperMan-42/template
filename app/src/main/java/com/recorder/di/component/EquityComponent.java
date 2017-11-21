package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.EquityModule;

import com.recorder.mvp.ui.fragment.EquityFragment;

@ActivityScope
@Component(modules = EquityModule.class, dependencies = AppComponent.class)
public interface EquityComponent {
    void inject(EquityFragment fragment);
}
