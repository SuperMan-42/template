package com.recorder.di.component;

import com.core.di.component.AppComponent;
import com.core.di.scope.ActivityScope;
import com.recorder.di.module.InvestmentHelpModule;
import com.recorder.mvp.ui.activity.InvestmentHelpActivity;

import dagger.Component;

@ActivityScope
@Component(modules = InvestmentHelpModule.class, dependencies = AppComponent.class)
public interface InvestmentHelpComponent {
    void inject(InvestmentHelpActivity activity);
}
