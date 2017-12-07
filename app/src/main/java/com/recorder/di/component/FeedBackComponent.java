package com.recorder.di.component;

import com.core.di.component.AppComponent;
import com.core.di.scope.ActivityScope;
import com.recorder.di.module.FeedBackModule;
import com.recorder.mvp.ui.activity.FeedBackActivity;

import dagger.Component;

@ActivityScope
@Component(modules = FeedBackModule.class, dependencies = AppComponent.class)
public interface FeedBackComponent {
    void inject(FeedBackActivity activity);
}
