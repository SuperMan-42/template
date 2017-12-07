package com.recorder.di.component;

import com.core.di.component.AppComponent;
import com.core.di.scope.ActivityScope;
import com.recorder.di.module.MyMessageModule;
import com.recorder.mvp.ui.activity.MyMessageActivity;

import dagger.Component;

@ActivityScope
@Component(modules = MyMessageModule.class, dependencies = AppComponent.class)
public interface MyMessageComponent {
    void inject(MyMessageActivity activity);
}
