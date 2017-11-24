package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.PersonModule;

import com.recorder.mvp.ui.activity.PersonActivity;

@ActivityScope
@Component(modules = PersonModule.class, dependencies = AppComponent.class)
public interface PersonComponent {
    void inject(PersonActivity activity);
}
