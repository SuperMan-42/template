package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.recorder.R;
import com.recorder.di.component.DaggerBackStageManagerComponent;
import com.recorder.di.module.BackStageManagerModule;
import com.recorder.mvp.contract.BackStageManagerContract;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.presenter.BackStageManagerPresenter;

import org.simple.eventbus.Subscriber;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/BackStageManagerActivity")
public class BackStageManagerActivity extends BaseActivity<BackStageManagerPresenter> implements BackStageManagerContract.View {

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerBackStageManagerComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .backStageManagerModule(new BackStageManagerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_back_stage_manager; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Subscriber(tag = Constants.RETRY_BACKSTAGEMANAGER)
    private void retry(LoginBean loginBean) {
        findViewById(R.id.view_empty).setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        CoreUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        CoreUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
