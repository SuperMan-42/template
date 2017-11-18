package com.recorder.di.component;

import com.core.di.component.AppComponent;
import com.core.di.scope.ActivityScope;
import com.recorder.di.module.HomeModule;
import com.recorder.mvp.ui.activity.HomeActivity;
import com.recorder.mvp.ui.fragment.HomeFragment;

import dagger.Component;

@ActivityScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeActivity activity);

    void inject(HomeFragment fragment);
}