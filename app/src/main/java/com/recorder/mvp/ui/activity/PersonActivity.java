package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.google.gson.Gson;
import com.recorder.Constants;
import com.recorder.R;
import com.recorder.di.component.DaggerPersonComponent;
import com.recorder.di.module.PersonModule;
import com.recorder.mvp.contract.PersonContract;
import com.recorder.mvp.model.entity.UserInfoBean;
import com.recorder.mvp.presenter.PersonPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/PersonActivity")
public class PersonActivity extends BaseActivity<PersonPresenter> implements PersonContract.View {

    UserInfoBean.DataEntity userInfoBean;
    @Autowired(name = Constants.USER_INFO)
    String userinfo;
    @BindView(R.id.im_avatar)
    CircleImageView imAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_weixin)
    TextView tvWeixin;
    @BindView(R.id.tv_address)
    TextView tvAddress;

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
        ARouter.getInstance().inject(this);
        userInfoBean = new Gson().fromJson(userinfo, UserInfoBean.DataEntity.class);
        CoreUtils.imgLoaderCircle(this, "http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f19cc0079.jpg", imAvatar);
        tvUserName.setText(userInfoBean.getUser_name());
        tvIntro.setText(userInfoBean.getIntro());
        tvMobile.setText(userInfoBean.getMobile());
        tvEmail.setText(userInfoBean.getEmail());
        tvWeixin.setText(userInfoBean.getWeixin());
        tvAddress.setText(userInfoBean.getAddress());
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

    @OnClick({R.id.rl_user_name, R.id.rl_intro, R.id.rl_email, R.id.rl_weixin, R.id.rl_address, R.id.rl_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_user_name:
                ARouter.getInstance().build("/app/UserModifyActivity").withString("key", "用户名").withBoolean("isIntro", false).navigation();
                break;
            case R.id.rl_intro:
                ARouter.getInstance().build("/app/UserModifyActivity").withString("key", "个人介绍").withBoolean("isIntro", true).navigation();
                break;
            case R.id.rl_email:
                ARouter.getInstance().build("/app/UserModifyActivity").withString("key", "邮箱").withBoolean("isIntro", false).navigation();
                break;
            case R.id.rl_weixin:
                ARouter.getInstance().build("/app/UserModifyActivity").withString("key", "微信").withBoolean("isIntro", false).navigation();
                break;
            case R.id.rl_address:
                ARouter.getInstance().build("/app/UserModifyActivity").withString("key", "邮寄地址").withBoolean("isIntro", false).navigation();
                break;
            case R.id.rl_password:
                ARouter.getInstance().build("/app/ModifyPasswordActivity").navigation();
                break;
        }
    }
}
