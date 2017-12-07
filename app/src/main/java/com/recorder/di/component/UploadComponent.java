package com.recorder.di.component;

import com.core.di.component.AppComponent;
import com.core.di.scope.ActivityScope;
import com.recorder.di.module.UploadModule;
import com.recorder.mvp.ui.activity.UploadActivity;

import dagger.Component;

@ActivityScope
@Component(modules = UploadModule.class, dependencies = AppComponent.class)
public interface UploadComponent {
    void inject(UploadActivity activity);
}
