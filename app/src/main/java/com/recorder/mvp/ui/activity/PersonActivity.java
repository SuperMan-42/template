package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.recorder.R;
import com.recorder.di.component.DaggerPersonComponent;
import com.recorder.di.module.PersonModule;
import com.recorder.mvp.contract.PersonContract;
import com.recorder.mvp.presenter.PersonPresenter;

import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/PersonActivity")
public class PersonActivity extends BaseActivity<PersonPresenter> implements PersonContract.View {

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerPersonComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .personModule(new PersonModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_person; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("个人资料");
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

    @OnClick({R.id.rl_1, R.id.rl_2, R.id.rl_3, R.id.rl_4, R.id.rl_5, R.id.rl_6, R.id.rl_7, R.id.rl_8})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_1:
                break;
            case R.id.rl_2:
                break;
            case R.id.rl_3:
                break;
            case R.id.rl_4:
                break;
            case R.id.rl_5:
                break;
            case R.id.rl_6:
                break;
            case R.id.rl_7:
                break;
            case R.id.rl_8:
                ARouter.getInstance().build("/app/ModifyPasswordActivity").navigation();
                break;
        }
    }
}
