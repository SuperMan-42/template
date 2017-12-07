package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.integration.cache.BCache;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.recorder.R;
import com.recorder.di.component.DaggerPersonComponent;
import com.recorder.di.module.PersonModule;
import com.recorder.mvp.contract.PersonContract;
import com.recorder.mvp.model.entity.UserInfoBean;
import com.recorder.mvp.presenter.PersonPresenter;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/PersonActivity")
public class PersonActivity extends BaseActivity<PersonPresenter> implements PersonContract.View {
    private static final int IM_AVATAR = 0;
    UserInfoBean.DataEntity userInfoBean;

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
        userInfoBean = new Gson().fromJson(BCache.getInstance().getString(Constants.USER_INFO), UserInfoBean.class).getData();
        CoreUtils.imgLoaderCircle(this, userInfoBean.getAvatar(), imAvatar, R.drawable.ic_person);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfoBean = new Gson().fromJson(BCache.getInstance().getString(Constants.USER_INFO), UserInfoBean.class).getData();
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

    @OnClick({R.id.rl_avatar, R.id.rl_user_name, R.id.rl_intro, R.id.rl_email, R.id.rl_weixin, R.id.rl_address, R.id.rl_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_avatar:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)
                        .theme(R.style.picture_hx_style)
                        .enableCrop(true)
                        .circleDimmedLayer(true)
                        .compress(true)
                        .showCropGrid(false)
                        .forResult(IM_AVATAR);
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IM_AVATAR:
                    // 图片选择结果回调
                    File file = new File(PictureSelector.obtainMultipleResult(data).get(0).getCompressPath());
                    CoreUtils.imgLoaderCircle(this, file, imAvatar, imAvatar.getDrawable());
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
                    RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    builder.addFormDataPart("images", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                    List<MultipartBody.Part> parts = builder.build().parts();
//                    RequestBody typeBody = RequestBody.create(MediaType.parse("text/plain"), "2");
                    mPresenter.imageUpload(parts, file.getPath());
                    break;
            }
        }
    }
}
