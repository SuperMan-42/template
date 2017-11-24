package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.PersonContract;
import com.recorder.mvp.model.PersonModel;

@Module
public class PersonModule {
    private PersonContract.View view;

    /**
     * 构建PersonModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PersonModule(PersonContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PersonContract.View providePersonView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PersonContract.Model providePersonModel(PersonModel model) {
        return model;
    }
}
