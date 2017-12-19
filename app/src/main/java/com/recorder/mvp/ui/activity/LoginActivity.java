package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.jaeger.library.StatusBarUtil;
import com.recorder.R;
import com.recorder.di.component.DaggerLoginComponent;
import com.recorder.di.module.LoginModule;
import com.recorder.mvp.contract.LoginContract;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.presenter.LoginPresenter;
import com.recorder.utils.CommonUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/LoginActivity")
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @Autowired(name = Constants.RETRY_WHEN_LOGIN_OR_AUTH)
    String retry;

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Subscriber(tag = Constants.FINISH)
    private void kill(Object object) {
        killMyself();
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

    @OnClick({R.id.im_close, R.id.tv_login, R.id.tv_forget_password, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_close:
                finish();
                break;
            case R.id.tv_login:
                doLogin();
                break;
            case R.id.tv_forget_password:
                ARouter.getInstance().build("/app/ForgetPasswordActivity").navigation();
                break;
            case R.id.register:
                ARouter.getInstance().build("/app/RegisterActivity").navigation();
                break;
        }
    }

    private void doLogin() {
        if (!CommonUtils.isPhone(etPhone.getText().toString())) {
            CoreUtils.snackbarText("手机号格式错误");
            return;
        }
        CoreUtils.hideSoftInput(etPhone);
        mPresenter.login(etPhone.getText().toString(), etPassword.getText().toString());
    }

    @Override
    public void showLoginSuccess(LoginBean loginBean) {
        CoreUtils.snackbarText("登录成功");
        EventBus.getDefault().post(loginBean, retry);
        killMyself();
    }
}
