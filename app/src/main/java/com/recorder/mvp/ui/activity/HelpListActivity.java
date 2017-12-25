package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.recorder.R;
import com.recorder.di.component.DaggerHelpListComponent;
import com.recorder.di.module.HelpListModule;
import com.recorder.mvp.contract.HelpListContract;
import com.recorder.mvp.model.entity.HelpContentBean;
import com.recorder.mvp.model.entity.HelpListBean;
import com.recorder.mvp.presenter.HelpListPresenter;
import com.recorder.utils.CommonUtils;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/HelpListActivity")
public class HelpListActivity extends BaseActivity<HelpListPresenter> implements HelpListContract.View {

    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerview;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerHelpListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .helpListModule(new HelpListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_help_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("投资帮助");
        mPresenter.helpList();
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
    public void showHelpList(HelpListBean.DataEntity data) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setAutoMeasureEnabled(true);
        recyclerview.init(manager, new BaseQuickAdapter<HelpListBean.DataEntity.ListEntity, BaseViewHolder>(R.layout.item_help_list, data.getList()) {
            @Override
            protected void convert(BaseViewHolder holder, HelpListBean.DataEntity.ListEntity item) {
                holder.setText(R.id.tv_title, item.getTitle());
                holder.itemView.setOnClickListener(view1 -> mPresenter.helpContent(item));
            }
        }, false);
        recyclerview.getRecyclerView().addItemDecoration(CommonUtils.linearDivider(this, 45));
    }

    @Override
    public void showHelpContent(HelpContentBean.DataEntity data, HelpListBean.DataEntity.ListEntity entity) {
        ARouter.getInstance().build("/app/WebActivity")
                .withBoolean(Constants.IS_SHOW_RIGHT, false)
                .withString(Constants.TITLE, entity.getTitle())
                .withString(Constants.WEB_URL, data.getContent()).greenChannel().navigation();
    }
}
