package com.recorder.di.component;

import com.core.di.component.AppComponent;
import com.core.di.scope.ActivityScope;
import com.recorder.di.module.NewPasswordModule;
import com.recorder.mvp.ui.activity.NewPasswordActivity;

import dagger.Component;

@ActivityScope
@Component(modules = NewPasswordModule.class, dependencies = AppComponent.class)
public interface NewPasswordComponent {
    void inject(NewPasswordActivity activity);
}
