package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.integration.cache.BCache;
import com.core.utils.CoreUtils;
import com.google.gson.Gson;
import com.recorder.Constants;
import com.recorder.R;
import com.recorder.di.component.DaggerModifyPasswordComponent;
import com.recorder.di.module.ModifyPasswordModule;
import com.recorder.mvp.contract.ModifyPasswordContract;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.presenter.ModifyPasswordPresenter;
import com.recorder.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/ModifyPasswordActivity")
public class ModifyPasswordActivity extends BaseActivity<ModifyPasswordPresenter> implements ModifyPasswordContract.View {

    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;

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
        title("修改密码");
        ARouter.getInstance().inject(this);
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

    @OnClick({R.id.tv_get_code, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.tv_next:
                userModifypwd();
                break;
        }
    }

    private void userModifypwd() {
        CoreUtils.hideSoftInput(this);
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            CoreUtils.snackbarText(CoreUtils.getString(this, R.string.text_password_no_null));
            return;
        }
        mPresenter.userModifypwd(etPassword.getText().toString(), etNewPassword.getText().toString(), etCode.getText().toString());
    }

    public void getCode() {
        CoreUtils.hideSoftInput(this);
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            CoreUtils.snackbarText(CoreUtils.getString(this, R.string.text_password_no_null));
            return;
        }
        mPresenter.smsCode(new Gson().fromJson(BCache.getInstance().getString(Constants.LOGIN_INFO), LoginBean.class).getData().getMobile(), "3", null);
    }
}
