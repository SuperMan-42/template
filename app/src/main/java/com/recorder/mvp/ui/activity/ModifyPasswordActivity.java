package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;

import com.recorder.di.component.DaggerModifyPasswordComponent;
import com.recorder.di.module.ModifyPasswordModule;
import com.recorder.mvp.contract.ModifyPasswordContract;
import com.recorder.mvp.presenter.ModifyPasswordPresenter;

import com.recorder.R;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/ModifyPasswordActivity")
public class ModifyPasswordActivity extends BaseActivity<ModifyPasswordPresenter> implements ModifyPasswordContract.View {

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerModifyPasswordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .modifyPasswordModule(new ModifyPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_modify_password; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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
