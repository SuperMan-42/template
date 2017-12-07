package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.HelpListModule;

import com.recorder.mvp.ui.activity.HelpListActivity;

@ActivityScope
@Component(modules = HelpListModule.class, dependencies = AppComponent.class)
public interface HelpListComponent {
    void inject(HelpListActivity activity);
}
