package com.recorder.di.component;

import com.core.di.scope.ActivityScope;

import dagger.Component;

import com.core.di.component.AppComponent;

import com.recorder.di.module.PdfModule;

import com.recorder.mvp.ui.activity.PdfActivity;

@ActivityScope
@Component(modules = PdfModule.class, dependencies = AppComponent.class)
public interface PdfComponent {
    void inject(PdfActivity activity);
}
