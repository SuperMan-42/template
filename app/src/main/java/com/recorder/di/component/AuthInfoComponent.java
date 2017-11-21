package com.recorder.di.component;

import com.core.di.component.AppComponent;
import com.core.di.scope.ActivityScope;
import com.recorder.di.module.AuthInfoModule;
import com.recorder.mvp.ui.activity.AuthInfoActivity;

import dagger.Component;

@ActivityScope
@Component(modules = AuthInfoModule.class, dependencies = AppComponent.class)
public interface AuthInfoComponent {
    void inject(AuthInfoActivity activity);
}
