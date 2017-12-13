package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.http.imageloader.ImageLoader;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.CustomPopupWindow;
import com.recorder.R;
import com.recorder.di.component.DaggerRegisterComponent;
import com.recorder.di.module.RegisterModule;
import com.recorder.mvp.contract.ForgetPasswordContract;
import com.recorder.mvp.contract.RegisterContract;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.presenter.RegisterPresenter;
import com.recorder.utils.CommonUtils;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/RegisterActivity")
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View, ForgetPasswordContract.View {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_next)
    EditText etPasswordNext;
    @BindView(R.id.fl_dialog)
    FrameLayout flDialog;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRegisterComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .registerModule(new RegisterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_register; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("注册");
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
        if (message.equals(CoreUtils.getString(this, R.string.text_smsCode))) {
            CommonUtils.getCode(tvGetCode);
        }
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

    @OnClick({R.id.tv_get_code, R.id.tv_next, R.id.tv_go_authentication, R.id.tv_go_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.tv_next:
                doRegister();
                break;
            case R.id.tv_go_authentication:
                ARouter.getInstance().build("/app/AuthActivity").navigation();
                break;
            case R.id.tv_go_home:
                EventBus.getDefault().post(0, Constants.HOME_INDEX);
                ARouter.getInstance().build("/app/HomeActivity").navigation();
                break;
        }
    }

    private void doRegister() {
        if (!CommonUtils.isPhone(etPhone.getText().toString())) {
            CoreUtils.snackbarText(CoreUtils.getString(this, R.string.text_phone));
            return;
        }
        if (!etPassword.getText().toString().equals(etPasswordNext.getText().toString())) {
            CustomPopupWindow.builder().contentView(CoreUtils.inflate(this, R.layout.layout_dialog_one_button)).isOutsideTouch(false)
                    .customListener(contentView -> contentView.findViewById(R.id.tv_sure).setOnClickListener(view -> CustomPopupWindow.killMySelf())).build().show();
            return;
        }
        CoreUtils.hideSoftInput(etPhone);
        mPresenter.registerUser(etPhone.getText().toString(), etPassword.getText().toString(), etCode.getText().toString());
    }

    @Override
    public void showRegisterSuccess(ImageLoader imageLoader, LoginBean loginBean) {
        title("提交成功");
        flDialog.setVisibility(View.VISIBLE);
        flDialog.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    public void getCode() {
        CoreUtils.hideSoftInput(this);
        if (!CommonUtils.isPhone(etPhone.getText().toString())) {
            CoreUtils.snackbarText(CoreUtils.getString(this, R.string.text_phone));
            return;
        }
        CommonUtils.getCode(tvGetCode);
        mPresenter.smsCode(etPhone.getText().toString(), "1", null);
    }
}
