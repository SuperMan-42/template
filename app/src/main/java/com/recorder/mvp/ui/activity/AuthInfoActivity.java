package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.recorder.R;
import com.recorder.di.component.DaggerAuthInfoComponent;
import com.recorder.di.module.AuthInfoModule;
import com.recorder.mvp.contract.AuthInfoContract;
import com.recorder.mvp.presenter.AuthInfoPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/AuthInfoActivity")
public class AuthInfoActivity extends BaseActivity<AuthInfoPresenter> implements AuthInfoContract.View {
    private final static int IM_POSITIVE = 0;
    private final static int IM_OTHER = 1;
    private final static int IM_RECYCLERVIEW = 2;

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.im_positive)
    ImageView imPositive;
    @BindView(R.id.im_other)
    ImageView imOther;
    @BindView(R.id.im_agree)
    ImageView imAgree;
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerview;
    @BindView(R.id.tv_go_authentication)
    TextView tvGoAuthentication;
    @BindView(R.id.tv_go_home)
    TextView tvGoHome;
    @BindView(R.id.fl_dialog)
    View flDialog;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerAuthInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .authInfoModule(new AuthInfoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_auth_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.im_positive, R.id.im_other, R.id.im_agree, R.id.tv_next, R.id.tv_go_authentication, R.id.tv_go_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_positive:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_hx_style)
                        .forResult(IM_POSITIVE);
                break;
            case R.id.im_other:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_hx_style)
                        .forResult(IM_OTHER);
                break;
            case R.id.im_agree:
                break;
            case R.id.tv_next:
                flDialog.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_go_authentication:
                break;
            case R.id.tv_go_home:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 图片选择结果回调
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    break;
                case IM_POSITIVE:
                    // 图片选择结果回调
                    List<LocalMedia> positiveList = PictureSelector.obtainMultipleResult(data);
                    CoreUtils.imgLoader(this, positiveList.get(0).getPath(), imPositive);
                    break;
                case IM_OTHER:
                    // 图片选择结果回调
                    List<LocalMedia> otherList = PictureSelector.obtainMultipleResult(data);
                    CoreUtils.imgLoader(this, otherList.get(0).getPath(), imOther);
                    break;
            }
        }
    }
}
