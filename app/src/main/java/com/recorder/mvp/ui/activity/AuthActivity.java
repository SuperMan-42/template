package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.CustomPopupWindow;
import com.recorder.R;
import com.recorder.di.component.DaggerAuthComponent;
import com.recorder.di.module.AuthModule;
import com.recorder.mvp.contract.AuthContract;
import com.recorder.mvp.model.entity.UserAuthInfoBean;
import com.recorder.mvp.presenter.AuthPresenter;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/AuthActivity")
public class AuthActivity extends BaseActivity<AuthPresenter> implements AuthContract.View {

    @BindView(R.id.tag_left)
    ImageView tagLeft;
    @BindView(R.id.tag_right)
    ImageView tagRight;
    @BindView(R.id.im_auth)
    ImageView imAuth;
    @BindView(R.id.tag_left2)
    ImageView tagLeft2;
    @BindView(R.id.tag_right2)
    ImageView tagRight2;
    @BindView(R.id.im_auth2)
    ImageView imAuth2;
    @BindView(R.id.tag_left3)
    ImageView tagLeft3;
    @BindView(R.id.tag_right3)
    ImageView tagRight3;
    @BindView(R.id.im_auth3)
    ImageView imAuth3;
    @BindView(R.id.cl_1)
    View cl1;
    @BindView(R.id.cl_2)
    View cl2;
    @BindView(R.id.cl_3)
    View cl3;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerAuthComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .authModule(new AuthModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_auth;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mPresenter.userAuthInfo();
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

    @Subscriber(tag = Constants.FINISH)
    private void kill(Object object) {
        killMyself();
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Subscriber(tag = Constants.AUTH_TYPE)
    private void upload(Object object) {
        mPresenter.userAuthInfo();
    }

    @Override
    public void showUserAuthInfo(UserAuthInfoBean.DataEntity data) {
        UserAuthInfoBean.DataEntity.ZcAuthEntity zcAuthEntity = data.getZc_auth();
        imAuth.setVisibility(data.getZc_auth().getIs_auditing() ? View.VISIBLE : View.GONE);
        tagLeft.setImageResource(data.getZc_auth().getStatus() == 4 ? R.drawable.auth_zc_1 : R.drawable.auth_zc_0);
        tagRight.setImageResource(data.getZc_auth().getFile_status() == 2 ? R.drawable.auth_right_1 : R.drawable.auth_right_0);
        if (zcAuthEntity.getStatus() == 0 || zcAuthEntity.getStatus() == 3 || zcAuthEntity.getStatus() == 5) {
            //跳至认证页面(需输入详细信息的认证页面)
            cl1.setOnClickListener(view -> ARouter.getInstance().build("/app/AuthInfoActivity").withInt(Constants.AUTH_TYPE, zcAuthEntity.getType()).navigation());
        } else if (zcAuthEntity.getStatus() == 1 || zcAuthEntity.getStatus() == 2 || (zcAuthEntity.getStatus() == 4 && zcAuthEntity.getFile_status() == 1)) {
            //提示用户审核中
            cl1.setOnClickListener(view -> showDialog());
        } else if (zcAuthEntity.getStatus() == 4 && (zcAuthEntity.getFile_status() == 0 || zcAuthEntity.getFile_status() == 3)) {
            //跳至只有上传资产认证界面
            cl1.setOnClickListener(view -> ARouter.getInstance().build("/app/UploadActivity").withInt(Constants.AUTH_TYPE, zcAuthEntity.getType()).withBoolean(Constants.ORDER_PROOF, false).navigation());
        }

        UserAuthInfoBean.DataEntity.ConformityAuthEntity conformityAuthEntity = data.getConformity_auth();
        imAuth2.setVisibility(data.getConformity_auth().getIs_auditing() ? View.VISIBLE : View.GONE);
        tagLeft2.setImageResource(data.getConformity_auth().getStatus() == 4 ? R.drawable.auth_conformity_1 : R.drawable.auth_conformity_0);
        tagRight2.setImageResource(data.getConformity_auth().getFile_status() == 2 ? R.drawable.auth_right_1 : R.drawable.auth_right_0);
        if (conformityAuthEntity.getStatus() == 0 || conformityAuthEntity.getStatus() == 3 || conformityAuthEntity.getStatus() == 5) {
            //跳至认证页面(需输入详细信息的认证页面)
            cl2.setOnClickListener(view -> ARouter.getInstance().build("/app/AuthInfoActivity").withInt(Constants.AUTH_TYPE, conformityAuthEntity.getType()).navigation());
        } else if (conformityAuthEntity.getStatus() == 1 || conformityAuthEntity.getStatus() == 2 || (conformityAuthEntity.getStatus() == 4 && conformityAuthEntity.getFile_status() == 1)) {
            //提示用户审核中
            cl2.setOnClickListener(view -> showDialog());
        } else if (conformityAuthEntity.getStatus() == 4 && (conformityAuthEntity.getFile_status() == 0 || conformityAuthEntity.getFile_status() == 3)) {
            //跳至只有上传资产认证界面
            cl2.setOnClickListener(view -> ARouter.getInstance().build("/app/UploadActivity").withInt(Constants.AUTH_TYPE, conformityAuthEntity.getType()).withBoolean(Constants.ORDER_PROOF, false).navigation());
        }

        UserAuthInfoBean.DataEntity.OrganAuthEntity organAuthEntity = data.getOrgan_auth();
        imAuth3.setVisibility(data.getOrgan_auth().getIs_auditing() ? View.VISIBLE : View.GONE);
        tagLeft3.setImageResource(data.getOrgan_auth().getStatus() == 4 ? R.drawable.auth_organ_1 : R.drawable.auth_organ_0);
        tagRight3.setImageResource(data.getOrgan_auth().getFile_status() == 2 ? R.drawable.auth_right_1 : R.drawable.auth_right_0);
        if (organAuthEntity.getStatus() == 0 || organAuthEntity.getStatus() == 3 || organAuthEntity.getStatus() == 5) {
            //跳至认证页面(需输入详细信息的认证页面)
            cl3.setOnClickListener(view -> ARouter.getInstance().build("/app/AuthInfoActivity").withInt(Constants.AUTH_TYPE, organAuthEntity.getType()).navigation());
        } else if (organAuthEntity.getStatus() == 1 || organAuthEntity.getStatus() == 2 || (organAuthEntity.getStatus() == 4 && organAuthEntity.getFile_status() == 1)) {
            //提示用户审核中
            cl3.setOnClickListener(view -> showDialog());
        } else if (organAuthEntity.getStatus() == 4 && (organAuthEntity.getFile_status() == 0 || organAuthEntity.getFile_status() == 3)) {
            //跳至只有上传资产认证界面
            cl3.setOnClickListener(view -> ARouter.getInstance().build("/app/UploadActivity").withInt(Constants.AUTH_TYPE, organAuthEntity.getType()).withBoolean(Constants.ORDER_PROOF, false).navigation());
        }
    }

    public void showDialog() {
        CustomPopupWindow.builder().contentView(CoreUtils.inflate(this, R.layout.layout_dialog_one_button)).isOutsideTouch(false)
                .customListener(contentView -> {
                    ((TextView) contentView.findViewById(R.id.tv_title)).setText("提示");
                    ((TextView) contentView.findViewById(R.id.tv_content)).setText("用户审核中");
                    ((TextView) contentView.findViewById(R.id.tv_sure)).setText("确定");
                    contentView.findViewById(R.id.tv_sure).setOnClickListener(view -> CustomPopupWindow.killMySelf());
                }).build().show();
    }
}
