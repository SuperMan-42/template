package com.recorder.di.module;

import com.core.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.recorder.mvp.contract.PdfContract;
import com.recorder.mvp.model.PdfModel;

@Module
public class PdfModule {
    private PdfContract.View view;

    /**
     * 构建PdfModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PdfModule(PdfContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PdfContract.View providePdfView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PdfContract.Model providePdfModel(PdfModel model) {
        return model;
    }
}
