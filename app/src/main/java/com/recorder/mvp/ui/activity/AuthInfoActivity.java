package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.integration.cache.BCache;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.core.widget.recyclerview.GridSpacingItemDecoration;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.recorder.R;
import com.recorder.di.component.DaggerAuthInfoComponent;
import com.recorder.di.module.AuthInfoModule;
import com.recorder.mvp.contract.AuthInfoContract;
import com.recorder.mvp.model.entity.AppStartBean;
import com.recorder.mvp.model.entity.AuthGetBean;
import com.recorder.mvp.model.entity.Bean;
import com.recorder.mvp.presenter.AuthInfoPresenter;
import com.recorder.utils.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/AuthInfoActivity")
public class AuthInfoActivity extends BaseActivity<AuthInfoPresenter> implements AuthInfoContract.View {
    private final static int IM_POSITIVE = 100;
    private final static int IM_OTHER = 101;

    @Autowired(name = Constants.AUTH_TYPE)
    int authType;

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.im_positive)
    ImageView imPositive;
    @BindView(R.id.tv_positive)
    TextView tvPositive;
    @BindView(R.id.im_other)
    ImageView imOther;
    @BindView(R.id.tv_other)
    TextView tvOther;
    @BindView(R.id.im_agree)
    ImageView imAgree;
    @BindView(R.id.tv_agree)
    TextView tvAgree;
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerview;
    @BindView(R.id.tv_go_authentication)
    TextView tvGoAuthentication;
    @BindView(R.id.tv_go_home)
    TextView tvGoHome;
    @BindView(R.id.fl_dialog)
    View flDialog;

    AppStartBean bean;
    File positive;
    File other;

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
        ARouter.getInstance().inject(this);
        title("填写基本信息");
        mPresenter.authGet(authType);
        bean = new Gson().fromJson(BCache.getInstance().getString(Constants.APPSTART), AppStartBean.class);
        if (authType == 3) {
            etName.setHint("机构名称");
            etId.setHint("机构法人");
            etContact.setVisibility(View.VISIBLE);
            tv1.setText("请上传营业执照(必填)");
            imPositive.setImageResource(R.drawable.id_organ);
            tvPositive.setText("上传营业执照");
            imOther.setVisibility(View.GONE);
            tvOther.setVisibility(View.GONE);
            tvAgree.setText(bean.getData().getUser_auth_prompt().getOrgan_auth());
        } else if (authType == 2) {
            tvAgree.setText(bean.getData().getUser_auth_prompt().getConformity_auth());
        } else if (authType == 1) {
            tvAgree.setText(bean.getData().getUser_auth_prompt().getZc_auth());
        }
        List<Bean<Boolean>> list = new ArrayList<>();
        list.add(new Bean<>(true, null, null));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setAutoMeasureEnabled(true);
        recyclerview.init(layoutManager, new BaseQuickAdapter<Bean<Boolean>, BaseViewHolder>(R.layout.item_upload, list) {
            @Override
            protected void convert(BaseViewHolder holder, Bean<Boolean> item) {
                if (item.getKey()) {
                    holder.setImageResource(R.id.im_upload, R.drawable.ic_upload);
                } else {
                    CoreUtils.imgLoader(getApplicationContext(), item.getValue(), holder.getView(R.id.im_upload));
                }
                holder.itemView.setOnClickListener(view -> PictureSelector.create(AuthInfoActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(item.getKey() ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)
                        .maxSelectNum(item.getKey() ? 5 - getItemCount() : 1)
                        .previewImage(true)
                        .previewEggs(true)
                        .theme(R.style.picture_hx_style)
                        .forResult(holder.getAdapterPosition() + (item.getKey() ? 0 : 10)));
                holder.itemView.setOnLongClickListener(view -> {
                    int index = 0;
                    for (Bean<Boolean> bean : getData()) {
                        if (!bean.getKey()) {
                            index++;
                        }
                    }
                    if (index == 4) {
                        addData(new Bean<>(true, null, null));
                        notifyItemRangeChanged(holder.getAdapterPosition(), 4);
                    }
                    if (!item.getKey()) {
                        recyclerview.remove(holder.getAdapterPosition());
                    }
                    return true;
                });
            }
        }, false);
        recyclerview.getRecyclerView().addItemDecoration(new GridSpacingItemDecoration(4, 45, false));
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

    @OnClick({R.id.im_positive, R.id.im_other, R.id.im_agree, R.id.tv_next, R.id.tv_go_authentication, R.id.tv_go_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_positive:
                CommonUtils.pictureSingle(this, IM_POSITIVE);
                break;
            case R.id.im_other:
                CommonUtils.pictureSingle(this, IM_OTHER);
                break;
            case R.id.im_agree:
                break;
            case R.id.tv_next:
                doNext();
                break;
            case R.id.tv_go_authentication:
                break;
            case R.id.tv_go_home:
                break;
        }
    }

    private void doNext() {
        List<Bean<Boolean>> data = recyclerview.getAdapter().getData();
        if (data.size() > 1 && data.get(data.size() - 1).getKey()) {
            data.remove(data.size() - 1);
        }
        if (data.size() > 0) {
            if (authType == 3) {
                mPresenter.upload(etName.getText().toString(), etId.getText().toString(), etContact.getText().toString(), positive, bean.getData().getUser_auth_prompt().getOrgan_auth(), data);
            } else if (authType == 2) {
                mPresenter.upload(authType, etName.getText().toString(), etId.getText().toString(), positive, other, bean.getData().getUser_auth_prompt().getConformity_auth(), data);
            } else if (authType == 1) {
                mPresenter.upload(authType, etName.getText().toString(), etId.getText().toString(), positive, other, bean.getData().getUser_auth_prompt().getZc_auth(), data);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IM_POSITIVE:
                    List<LocalMedia> positiveList = PictureSelector.obtainMultipleResult(data);
                    positive = new File(positiveList.get(0).getPath());
                    CoreUtils.imgLoader(this, positiveList.get(0).getPath(), imPositive);
                    break;
                case IM_OTHER:
                    List<LocalMedia> otherList = PictureSelector.obtainMultipleResult(data);
                    other = new File(otherList.get(0).getPath());
                    CoreUtils.imgLoader(this, otherList.get(0).getPath(), imOther);
                    break;
                default:
                    List<LocalMedia> picList = PictureSelector.obtainMultipleResult(data);
                    List<Bean<Boolean>> dataList = new ArrayList<>();
                    for (LocalMedia localMedia : picList) {
                        dataList.add(new Bean<>(false, localMedia.getPath(), null));
                    }
                    if (requestCode >= 10) {
                        recyclerview.getAdapter().getData().set(requestCode - 10, new Bean<>(false, picList.get(0).getPath(), null));
                        recyclerview.getAdapter().notifyItemRangeChanged(requestCode - 10, 1);
                    } else {
                        recyclerview.getAdapter().addData(requestCode, dataList);
                        recyclerview.getAdapter().notifyItemRangeChanged(requestCode, 4);
                        if (dataList.size() >= 4 - requestCode) {
                            recyclerview.remove(4);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void showAuthGet(AuthGetBean.DataEntity data) {
        if (authType == 3) {
            etName.setText(data.getOrgan_name());
            etId.setText(data.getOrgan_legal_person());
            etContact.setText(data.getOrgan_contact());
            CoreUtils.imgLoader(this, data.getOrgan_license(), R.drawable.id_organ, imPositive);
        } else {
            etName.setText(data.getTrue_name());
            etId.setText(data.getId_card());
            CoreUtils.imgLoader(this, data.getIdcard_imgf(), R.drawable.id_0, imPositive);
            CoreUtils.imgLoader(this, data.getIdcard_imgb(), R.drawable.id_1, imOther);
        }
        imAgree.setImageResource(data.getCheck().equals("1") ? R.drawable.ic_item_buy_selector : R.drawable.ic_item_buy);
    }
}
