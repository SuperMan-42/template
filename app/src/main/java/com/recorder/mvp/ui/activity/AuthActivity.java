package com.recorder.mvp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.recorder.R;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/app/AuthActivity")
public class AuthActivity extends BaseActivity {

    @BindView(R.id.im_head)
    ImageView imHead;
    @BindView(R.id.tv_auth)
    TextView tvAuth;
    @BindView(R.id.im_head1)
    ImageView imHead1;
    @BindView(R.id.tv_auth1)
    TextView tvAuth1;
    @BindView(R.id.im_head2)
    ImageView imHead2;
    @BindView(R.id.tv_auth2)
    TextView tvAuth2;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_auth;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.cl_1, R.id.cl_2, R.id.cl_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_1:
                ARouter.getInstance().build("/app/AuthInfoActivity").navigation();
                break;
            case R.id.cl_2:
                ARouter.getInstance().build("/app/AuthInfoActivity").navigation();
                break;
            case R.id.cl_3:
                ARouter.getInstance().build("/app/AuthInfoActivity").navigation();
                break;
        }
    }
}
