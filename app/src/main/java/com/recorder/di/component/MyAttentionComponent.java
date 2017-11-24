package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.MyAttentionModule;

import com.recorder.mvp.ui.activity.MyAttentionActivity;

@ActivityScope
@Component(modules = MyAttentionModule.class, dependencies = AppComponent.class)
public interface MyAttentionComponent {
    void inject(MyAttentionActivity activity);
}
