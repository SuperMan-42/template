package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseMultiItemQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.core.widget.recyclerview.entity.AbstractExpandableItem;
import com.core.widget.recyclerview.entity.MultiItemEntity;
import com.recorder.R;
import com.recorder.di.component.DaggerBackStageManagerComponent;
import com.recorder.di.module.BackStageManagerModule;
import com.recorder.mvp.contract.BackStageManagerContract;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.model.entity.OrderPimanageBean;
import com.recorder.mvp.presenter.BackStageManagerPresenter;
import com.recorder.utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/BackStageManagerActivity")
public class BackStageManagerActivity extends BaseActivity<BackStageManagerPresenter> implements BackStageManagerContract.View {

    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    private ArrayList<MultiItemEntity> res = new ArrayList<>();

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerBackStageManagerComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .backStageManagerModule(new BackStageManagerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_back_stage_manager; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("投后管理");
        mPresenter.orderPimanage();
    }

    @Subscriber(tag = Constants.RETRY_BACKSTAGEMANAGER)
    private void retry(LoginBean loginBean) {
        findViewById(R.id.view_empty).setVisibility(View.GONE);
        mPresenter.orderPimanage();
    }

    @Override
    public void showLoading() {
        CommonUtils.show(avi);
    }

    @Override
    public void hideLoading() {
        CommonUtils.hide(avi);
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
    public void showOrderPimanage(OrderPimanageBean.DataEntity data) {
        if (data.getPi_files().size() == 0)
            return;
        for (OrderPimanageBean.DataEntity.PiFilesEntity piFilesEntity : data.getPi_files()) {
            HeaderItem headerItem = new HeaderItem(piFilesEntity.getDeal_name(), true);
            for (int i = 0; i < piFilesEntity.getFiles().size(); i++) {
                OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity filesEntity = piFilesEntity.getFiles().get(i);
                filesEntity.setFirst(i == 0);
                headerItem.addSubItem(filesEntity);
            }
            res.add(headerItem);
        }
        ExpandableItemAdapter adapter = new ExpandableItemAdapter(res);
        recyclerView.init(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }, adapter, false);
        adapter.expand(0);
    }

    private static class HeaderItem extends AbstractExpandableItem<MultiItemEntity> implements MultiItemEntity {
        public String title;
        public boolean isEquity;

        public HeaderItem(String title, boolean isEquity) {
            this.title = title;
            this.isEquity = isEquity;
        }

        @Override
        public int getItemType() {
            return ExpandableItemAdapter.TYPE_HEADER;
        }

        @Override
        public int getLevel() {
            return 0;
        }
    }

    /**
     * MultiItem adapter 处理所有数据
     */
    public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_CONTENT = 1;

        public ExpandableItemAdapter(List<MultiItemEntity> data) {
            super(data);
            addItemType(TYPE_HEADER, R.layout.item_backstage_header);
            addItemType(TYPE_CONTENT, R.layout.item_backstage_content);
        }

        @Override
        protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
            switch (holder.getItemViewType()) {
                case TYPE_HEADER:
                    final HeaderItem lv0 = (HeaderItem) item;
                    holder.setText(R.id.tv_title, lv0.title)
                            .setImageResource(R.id.im_right, lv0.isExpanded() ? R.drawable.ic_backstage_up : R.drawable.ic_backstage_down);
                    holder.itemView.setOnClickListener(v -> {
                        int pos = holder.getAdapterPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos, false);
                        } else {
                            expand(pos);
                        }
                    });
                    break;
                case TYPE_CONTENT:
                    final OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity lv1 = (OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity) item;
                    holder.setText(R.id.tv_title, lv1.getFile_name())
                            .setText(R.id.tv_time, lv1.getCtime())
                            .setVisible(R.id.view, lv1.getFirst());
                    holder.itemView.setOnClickListener(view -> ARouter.getInstance().build("/app/PdfActivity")
                            .withString(Constants.PDF_HTTP, lv1.getFile()).withString(Constants.PDF_NAME, lv1.getFile_name()).navigation());
                    break;
            }
        }
    }
}
