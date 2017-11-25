package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.recorder.R;
import com.recorder.di.component.DaggerNewPasswordComponent;
import com.recorder.di.module.NewPasswordModule;
import com.recorder.mvp.contract.NewPasswordContract;
import com.recorder.mvp.presenter.NewPasswordPresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/NewPasswordActivity")
public class NewPasswordActivity extends BaseActivity<NewPasswordPresenter> implements NewPasswordContract.View {

    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_next)
    EditText etPasswordNext;

    @Autowired
    String mobile;
    @Autowired
    String code;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerNewPasswordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .newPasswordModule(new NewPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_new_password; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        title("找回密码");
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.empty);
    }

    @OnClick(R.id.tv_do)
    public void onViewClicked() {
        CoreUtils.hideSoftInput(this);
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            CoreUtils.snackbarText(CoreUtils.getString(this, R.string.text_password_no_null));
            return;
        }
        if (TextUtils.isEmpty(etPasswordNext.getText().toString()) || !etPassword.getText().toString().equals(etPasswordNext.getText().toString())) {
            CoreUtils.snackbarText(CoreUtils.getString(this, R.string.text_password_no));
            return;
        }
        mPresenter.userForgetpwd(mobile, code, etPassword.getText().toString());
    }
}
