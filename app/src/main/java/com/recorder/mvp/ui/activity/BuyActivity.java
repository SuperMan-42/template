package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;

import com.recorder.di.component.DaggerBuyComponent;
import com.recorder.di.module.BuyModule;
import com.recorder.mvp.contract.BuyContract;
import com.recorder.mvp.presenter.BuyPresenter;

import com.recorder.R;

import static com.core.utils.Preconditions.checkNotNull;

public class BuyActivity extends BaseActivity<BuyPresenter> implements BuyContract.View {

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerBuyComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .buyModule(new BuyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_buy; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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
