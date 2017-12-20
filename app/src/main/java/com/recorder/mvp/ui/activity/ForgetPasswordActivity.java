package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.recorder.R;
import com.recorder.di.component.DaggerForgetPasswordComponent;
import com.recorder.di.module.ForgetPasswordModule;
import com.recorder.mvp.contract.ForgetPasswordContract;
import com.recorder.mvp.presenter.ForgetPasswordPresenter;
import com.recorder.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/ForgetPasswordActivity")
public class ForgetPasswordActivity extends BaseActivity<ForgetPasswordPresenter> implements ForgetPasswordContract.View {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_pic_code)
    EditText etPicCode;
    @BindView(R.id.im_pic_code)
    ImageView imPicCode;
    @BindView(R.id.ll_pic_code)
    LinearLayout llPicCode;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerForgetPasswordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .forgetPasswordModule(new ForgetPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_forget_password; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.empty);
    }

    @OnClick({R.id.tv_get_code, R.id.tv_next, R.id.im_pic_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                CoreUtils.hideSoftInput(this);
                if (!CommonUtils.isPhone(etPhone.getText().toString())) {
                    CoreUtils.snackbarText(CoreUtils.getString(this, R.string.text_phone));
                    return;
                }
                mPresenter.smsCode(etPhone.getText().toString(), "2", null, imPicCode, llPicCode);
                break;
            case R.id.tv_next:
                CoreUtils.hideSoftInput(this);
                if (!CommonUtils.isPhone(etPhone.getText().toString())) {
                    CoreUtils.snackbarText(CoreUtils.getString(this, R.string.text_phone));
                    return;
                }
                if (TextUtils.isEmpty(etCode.getText().toString())) {
                    CoreUtils.snackbarText(CoreUtils.getString(this, R.string.text_code));
                    return;
                }
                mPresenter.smsVerify(etPhone.getText().toString(), etCode.getText().toString(), "2");
                break;
            case R.id.im_pic_code:
//                verify();
                break;
        }
    }
}
