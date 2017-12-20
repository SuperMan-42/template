package com.recorder.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
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
import com.recorder.mvp.model.api.Api;
import com.recorder.mvp.model.entity.AppStartBean;
import com.recorder.mvp.model.entity.AuthGetBean;
import com.recorder.mvp.model.entity.Bean;
import com.recorder.mvp.model.entity.UserInfoBean;
import com.recorder.mvp.presenter.AuthInfoPresenter;
import com.recorder.utils.CommonUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

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
    @BindView(R.id.im_cover)
    ImageView imCover;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_root)
    View llRoot;
    @BindView(R.id.toolbar_left)
    View back;

    AppStartBean bean;
    File positive;
    File other;
    AuthGetBean.DataEntity dataEntity = new AuthGetBean.DataEntity();
    boolean isCheck = false;

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

    @SuppressLint("SetTextI18n")
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
            tvAgree.setText("勾选并承诺" + bean.getData().getUser_auth_prompt().getOrgan_auth());
        } else if (authType == 2) {
            tvAgree.setText("勾选并承诺" + bean.getData().getUser_auth_prompt().getConformity_auth());
        } else if (authType == 1) {
            tvAgree.setText("勾选并承诺" + bean.getData().getUser_auth_prompt().getZc_auth());
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
                if (dataEntity.getIs_modify_file()) {
                    holder.itemView.setOnClickListener(view -> {
                        if (etName.hasFocus()) {
                            etName.clearFocus();
                        }
                        if (etId.hasFocus()) {
                            etId.clearFocus();
                        }
                        if (etContact.hasFocus()) {
                            etContact.clearFocus();
                        }
                        CoreUtils.hideSoftInput(AuthInfoActivity.this);
                        PictureSelector.create(AuthInfoActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .selectionMode(item.getKey() ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)
                                .maxSelectNum(item.getKey() ? 5 - getItemCount() : 1)
                                .previewImage(true)
                                .previewEggs(true)
                                .theme(R.style.picture_hx_style)
                                .forResult(holder.getAdapterPosition() + (item.getKey() ? 0 : 10));
                    });
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

    @OnClick({R.id.im_positive, R.id.im_other, R.id.im_agree, R.id.tv_next, R.id.tv_go_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_positive:
                CoreUtils.hideSoftInput(this);
                if (dataEntity.getIs_modify()) {
                    CommonUtils.pictureSingle(this, IM_POSITIVE);
                }
                break;
            case R.id.im_other:
                CoreUtils.hideSoftInput(this);
                if (dataEntity.getIs_modify()) {
                    CommonUtils.pictureSingle(this, IM_OTHER);
                }
                break;
            case R.id.im_agree:
                isCheck = !isCheck;
                imAgree.setImageResource(isCheck ? R.drawable.ic_item_buy_selector : R.drawable.ic_item_buy);
                break;
            case R.id.tv_next:
                doNext();
                break;
            case R.id.tv_go_home:
//                EventBus.getDefault().post(0, Constants.HOME_INDEX);
//                ARouter.getInstance().build("/app/HomeActivity").navigation();
//                EventBus.getDefault().post(new Object(), Constants.FINISH);
                killMyself();
                break;
        }
    }

    private void doNext() {
        if (!isCheck) {
            CoreUtils.snackbarText(getString(R.string.text_agree));
            return;
        }
        List<Bean<Boolean>> data = recyclerview.getAdapter().getData();
        if (data.size() > 1 && data.get(data.size() - 1).getKey()) {
            data.remove(data.size() - 1);
        }
        if (data.size() > 0) {
            if (authType == 3) {
                mPresenter.upload(value(etName, dataEntity.getTrue_name()), value(etId, dataEntity.getId_card()), value(etContact, dataEntity.getOrgan_contact()), value(positive, dataEntity.getOrgan_license()), bean.getData().getUser_auth_prompt().getOrgan_auth(), data);
            } else if (authType == 2) {
                mPresenter.upload(authType, value(etName, dataEntity.getTrue_name()), value(etId, dataEntity.getId_card()), value(positive, dataEntity.getIdcard_imgf()), value(other, dataEntity.getIdcard_imgb()), bean.getData().getUser_auth_prompt().getConformity_auth(), data);
            } else if (authType == 1) {
                mPresenter.upload(authType, value(etName, dataEntity.getTrue_name()), value(etId, dataEntity.getId_card()), value(positive, dataEntity.getIdcard_imgf()), value(other, dataEntity.getIdcard_imgb()), bean.getData().getUser_auth_prompt().getZc_auth(), data);
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
        isCheck = data.getCheck().equals("1");
        dataEntity = data;
        if (authType == 3) {
            etName.setText(data.getOrgan_name());
            isModify(etName, data.getOrgan_name());
            etId.setText(data.getOrgan_legal_person());
            isModify(etId, data.getOrgan_legal_person());
            etContact.setText(data.getOrgan_contact());
            isModify(etContact, data.getOrgan_contact());
            CoreUtils.imgLoader(this, data.getOrgan_license(), R.drawable.id_organ, imPositive);
        } else {
            etName.setText(data.getTrue_name());
            isModify(etName, data.getTrue_name());
            etId.setText(data.getId_card());
            isModify(etId, data.getId_card());
            CoreUtils.imgLoader(this, data.getIdcard_imgf(), R.drawable.id_0, imPositive);
            CoreUtils.imgLoader(this, data.getIdcard_imgb(), R.drawable.id_1, imOther);
        }
        imAgree.setImageResource(data.getCheck().equals("1") ? R.drawable.ic_item_buy_selector : R.drawable.ic_item_buy);
        List<Bean<Boolean>> list = new ArrayList<>();
        for (String url : data.getAssets()) {
            list.add(new Bean<>(false, url, null));
        }
        recyclerview.getAdapter().setNewData(list);
        if (list.size() < 4) {
            recyclerview.getAdapter().addData(new Bean<>(true, null, null));
        }
    }

    @Override
    public void showSuccess(int type) {
        back.setVisibility(View.INVISIBLE);
        if (dataEntity.getIs_modify_survey()) {
            String url;
            String token = BCache.getInstance().getString(Constants.TOKEN);
            String uid = new Gson().fromJson(BCache.getInstance().getString(Constants.USER_INFO), UserInfoBean.class).getData().getUserID();
            if (type == 3) {
                url = Api.WEB_DOMAIN + "survey/organ-detail/?token=" + token + "&u=" + uid + "&type=" + authType + "&survey={\"1\":\"A\",\"2\":\"B\"}#/agency";
            } else {
                url = Api.WEB_DOMAIN + "survey/person-detail/?token=" + token + "&u=" + uid + "&type=" + authType + "&survey={\"1\":\"A\",\"2\":\"B\"}#/questionnaire";
            }
            ARouter.getInstance().build("/app/WebActivity")
                    .withBoolean(Constants.IS_SHOW_RIGHT, false)
                    .withString(Constants.WEB_URL, url).navigation();
        }
    }

    @Subscriber(tag = Constants.AUTH_INFO_SUCCESS)
    public void success(String string) {
        CoreUtils.imgLoader(this, R.drawable.ic_result_success, imCover);
        tvTitle.setText(CoreUtils.getString(this, R.string.text_authinfo_title_success));
        tvContent.setText(R.string.text_authinfo_success);
        tvContent.setTextColor(Color.parseColor("#333333"));
        tvGoAuthentication.setText("个人中心");
        title("提交成功");

        tvGoHome.setText("返  回");
        flDialog.setVisibility(View.VISIBLE);
        flDialog.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        tvGoAuthentication.setOnClickListener(view -> {
            EventBus.getDefault().post(4, Constants.HOME_INDEX);
            ARouter.getInstance().build("/app/HomeActivity").navigation();
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showFail(String msg) {
        back.setVisibility(View.INVISIBLE);
        CoreUtils.imgLoader(this, R.drawable.ic_result_fail, imCover);
        tvTitle.setText(CoreUtils.getString(this, R.string.text_authinfo_title_fail));
        tvContent.setText(getString(R.string.text_authinfo_fail) + msg);
        tvContent.setTextColor(Color.parseColor("#FF5701"));
        tvGoAuthentication.setText("重新提交");
        title("提交失败");

        tvGoHome.setText("返  回");
        flDialog.setVisibility(View.VISIBLE);
        flDialog.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        tvGoAuthentication.setOnClickListener(view -> {
            flDialog.setVisibility(View.GONE);
            flDialog.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        });
    }

    private void isModify(EditText editText, String string) {
        if (editText == etName && TextUtils.isEmpty(string)) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            CoreUtils.openSoftInputForced(etName);
        }
        editText.setEnabled(TextUtils.isEmpty(string) || dataEntity.getIs_modify());
    }

    private String value(EditText editText, String string) {
        if (TextUtils.isEmpty(string)) {
            return editText.getText().toString();
        } else {
            if (!dataEntity.getIs_hint().equals("success")) {
                return editText.getText().toString();
            } else {
                return null;
            }
        }
    }

    private Object value(File file, String string) {
        if (file != null) {
            return file;
        } else {
            return string.split("com/")[1];
        }
    }
}
