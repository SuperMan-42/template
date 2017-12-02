package com.recorder.di.component;

import com.core.di.component.AppComponent;
import com.core.di.scope.ActivityScope;
import com.recorder.di.module.DealRecommendModule;
import com.recorder.mvp.ui.activity.DealRecommendActivity;

import dagger.Component;

@ActivityScope
@Component(modules = DealRecommendModule.class, dependencies = AppComponent.class)
public interface DealRecommendComponent {
    void inject(DealRecommendActivity activity);
}
