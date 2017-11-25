package com.recorder.di.component;

import com.core.di.component.AppComponent;
import com.core.di.scope.ActivityScope;
import com.recorder.di.module.UserModifyModule;
import com.recorder.mvp.ui.activity.UserModifyActivity;

import dagger.Component;

@ActivityScope
@Component(modules = UserModifyModule.class, dependencies = AppComponent.class)
public interface UserModifyComponent {
    void inject(UserModifyActivity activity);
}
