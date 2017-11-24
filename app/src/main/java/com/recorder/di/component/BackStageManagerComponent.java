package com.recorder.di.component;

import com.core.di.component.AppComponent;
import com.core.di.scope.ActivityScope;
import com.recorder.di.module.BackStageManagerModule;
import com.recorder.mvp.ui.activity.BackStageManagerActivity;

import dagger.Component;

@ActivityScope
@Component(modules = BackStageManagerModule.class, dependencies = AppComponent.class)
public interface BackStageManagerComponent {
    void inject(BackStageManagerActivity activity);
}
