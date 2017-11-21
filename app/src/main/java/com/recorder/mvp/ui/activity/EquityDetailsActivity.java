package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;

import com.recorder.di.component.DaggerEquityDetailsComponent;
import com.recorder.di.module.EquityDetailsModule;
import com.recorder.mvp.contract.EquityDetailsContract;
import com.recorder.mvp.presenter.EquityDetailsPresenter;

import com.recorder.R;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/EquityDetailsActivity")
public class EquityDetailsActivity extends BaseActivity<EquityDetailsPresenter> implements EquityDetailsContract.View {

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerEquityDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .equityDetailsModule(new EquityDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_equity_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {

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
