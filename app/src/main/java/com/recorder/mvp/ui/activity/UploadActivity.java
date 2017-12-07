package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.core.widget.recyclerview.GirdDecoration;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;
import com.recorder.R;
import com.recorder.di.component.DaggerUploadComponent;
import com.recorder.di.module.UploadModule;
import com.recorder.mvp.contract.UploadContract;
import com.recorder.mvp.model.entity.Bean;
import com.recorder.mvp.presenter.UploadPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/UploadActivity")
public class UploadActivity extends BaseActivity<UploadPresenter> implements UploadContract.View {
    @Autowired(name = Constants.ORDER_PROOF)
    boolean isOrderProof;//如果是上传打款凭证需要Eventbus.post否则不用

    @BindView(R.id.im_right)
    ImageView imRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.toolbar_right)
    RelativeLayout toolbarRight;
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerUploadComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .uploadModule(new UploadModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_upload; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("上传凭证");
        ARouter.getInstance().inject(this);
        toolbarRight.setVisibility(View.VISIBLE);
        imRight.setVisibility(View.INVISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        if (isOrderProof) {
            tvTitle.setText("请上传转账凭证（银行汇款照片或网上银行回单截图）");
        } else {
            tvTitle.setText("请上传金融资产不低于300万元或者最近三年个人年均收入不低于50万元的个人资产证明，认证通过后会短信通知您再进行购买。");
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
        recyclerView.init(layoutManager, new BaseQuickAdapter<Bean<Boolean>, BaseViewHolder>(R.layout.item_upload, list) {
            @Override
            protected void convert(BaseViewHolder holder, Bean<Boolean> item) {
                CoreUtils.imgLoader(getApplicationContext(), item.getKey() ? R.drawable.ic_upload : item.getValue(), holder.getView(R.id.im_upload));
                holder.itemView.setOnClickListener(view -> PictureSelector.create(UploadActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(item.getKey() ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)
                        .maxSelectNum(item.getKey() ? 5 - getItemCount() : 1)
                        .previewImage(true)
                        .previewEggs(true)
                        .theme(R.style.picture_hx_style)
                        .forResult(holder.getAdapterPosition() + (item.getKey() ? 0 : 10)));
                holder.itemView.setOnLongClickListener(view -> {
                    if (!item.getKey())
                        remove(holder.getAdapterPosition());
                    if (getItemCount() == 3) {
                        addData(new Bean<>(true, null, null));
                        notifyItemRangeChanged(holder.getAdapterPosition(), 4);
                    }
                    return true;
                });
            }
        }, false);
        recyclerView.getRecyclerView().addItemDecoration(new GirdDecoration(this, 15));
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
        Logger.d("hpw=> adapter ");
        Logger.json(new Gson().toJson(recyclerView.getAdapter().getData()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> picList = PictureSelector.obtainMultipleResult(data);
            List<Bean<Boolean>> dataList = new ArrayList<>();
            for (LocalMedia localMedia : picList) {
                dataList.add(new Bean<>(false, localMedia.getPath(), null));
            }
            if (requestCode >= 10) {
                recyclerView.getAdapter().getData().set(requestCode - 10, new Bean<>(false, picList.get(0).getPath(), null));
                recyclerView.getAdapter().notifyItemRangeChanged(requestCode - 10, 1);
            } else {
                recyclerView.getAdapter().addData(requestCode, dataList);
                recyclerView.getAdapter().notifyItemRangeChanged(requestCode, 4);
                if (dataList.size() >= 4 - requestCode) {
                    recyclerView.remove(4);
                }
            }
//            File file = new File(PictureSelector.obtainMultipleResult(data).get(0).getPath());
//            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
//            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            builder.addFormDataPart("images", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
//            List<MultipartBody.Part> parts = builder.build().parts();
//            mPresenter.imageUpload(parts, file.getPath());
        }
    }
}
