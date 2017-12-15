package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.recorder.R;
import com.recorder.di.component.DaggerUserModifyComponent;
import com.recorder.di.module.UserModifyModule;
import com.recorder.mvp.contract.UserModifyContract;
import com.recorder.mvp.presenter.UserModifyPresenter;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/UserModifyActivity")
public class UserModifyActivity extends BaseActivity<UserModifyPresenter> implements UserModifyContract.View {
    @Autowired
    String key;
    @Autowired
    boolean isIntro;
    @Autowired
    String content;

    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.et_intro)
    EditText etIntro;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.ll_intro)
    LinearLayout llIntro;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerUserModifyComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .userModifyModule(new UserModifyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_user_modify; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        title(key);
        llIntro.setVisibility(isIntro ? View.VISIBLE : View.GONE);

        if (isIntro) {
            etIntro.setFocusable(true);
            etIntro.setFocusableInTouchMode(true);
            etIntro.requestFocus();
            etIntro.setText(content);
            etIntro.setSelection(content.length());
            CoreUtils.openSoftInputForced(etIntro);
        } else {
            etInfo.setFocusable(true);
            etInfo.setFocusableInTouchMode(true);
            etInfo.requestFocus();
            etInfo.setText(content);
            etInfo.setSelection(content.length());
            CoreUtils.openSoftInputForced(etInfo);
        }

        etInfo.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                switch (key) {
                    case "用户名":
                        mPresenter.userModify("user_name", etInfo.getText().toString(), null, null, null, null, null);
                        break;
                    case "邮箱":
                        mPresenter.userModify("email", null, null, etInfo.getText().toString(), null, null, null);
                        break;
                    case "微信":
                        mPresenter.userModify("weixin", null, null, null, etInfo.getText().toString(), null, null);
                        break;
                    case "邮寄地址":
                        mPresenter.userModify("address", null, null, null, null, etInfo.getText().toString(), null);
                        break;
                }
                CoreUtils.hideSoftInput(etInfo);
                return true;
            }
            return false;
        });

        etIntro.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                mPresenter.userModify("intro", null, etIntro.getText().toString(), null, null, null, null);
                CoreUtils.hideSoftInput(etIntro);
                return true;
            }
            return false;
        });

        etIntro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvNum.setText(editable.length() + "/20");
            }
        });
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
