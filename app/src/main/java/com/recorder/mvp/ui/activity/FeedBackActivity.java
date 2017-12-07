package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.recorder.R;
import com.recorder.di.component.DaggerFeedBackComponent;
import com.recorder.di.module.FeedBackModule;
import com.recorder.mvp.contract.FeedBackContract;
import com.recorder.mvp.presenter.FeedBackPresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/FeedBackActivity")
public class FeedBackActivity extends BaseActivity<FeedBackPresenter> implements FeedBackContract.View {

    @BindView(R.id.im_right)
    ImageView imRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.toolbar_right)
    RelativeLayout toolbarRight;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.et_phone)
    EditText etPhone;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerFeedBackComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .feedBackModule(new FeedBackModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_feed_back; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
//        AndroidBug5497Workaround.assistActivity(this);
        title("意见反馈");
        toolbarRight.setVisibility(View.VISIBLE);
        imRight.setVisibility(View.INVISIBLE);
        tvRight.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.toolbar_right)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etContent.getText().toString())) {
            CoreUtils.snackbarText(getString(R.string.text_feedback_content));
            return;
        }
        mPresenter.appFeedback(etContent.getText().toString(), etPhone.getText().toString());
        CoreUtils.hideSoftInput(this);
    }
}
