package com.recorder.di.component;

import com.core.di.component.AppComponent;
import com.core.di.scope.ActivityScope;
import com.recorder.di.module.AuthModule;
import com.recorder.mvp.ui.activity.AuthActivity;

import dagger.Component;


@ActivityScope
@Component(modules = AuthModule.class, dependencies = AppComponent.class)
public interface AuthComponent {
    void inject(AuthActivity activity);
}
