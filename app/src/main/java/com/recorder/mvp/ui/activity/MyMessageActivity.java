package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.core.widget.recyclerview.SpacesItemDecoration;
import com.recorder.R;
import com.recorder.di.component.DaggerMyMessageComponent;
import com.recorder.di.module.MyMessageModule;
import com.recorder.mvp.contract.MyMessageContract;
import com.recorder.mvp.model.entity.AppMsgsBean;
import com.recorder.mvp.presenter.MyMessagePresenter;
import com.recorder.utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/MyMessageActivity")
public class MyMessageActivity extends BaseActivity<MyMessagePresenter> implements MyMessageContract.View {
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyMessageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myMessageModule(new MyMessageModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_my_message; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("消息");
        mPresenter.appMsgs("1", Constants.PAGE_SIZE);
        recyclerView.getRecyclerView().addItemDecoration(new SpacesItemDecoration(30, 45));
        recyclerView.init(new BaseQuickAdapter<AppMsgsBean.DataEntity.ListEntity, BaseViewHolder>(R.layout.item_message) {
            @Override
            protected void convert(BaseViewHolder holder, AppMsgsBean.DataEntity.ListEntity item) {
                holder.setText(R.id.tv_title, "般若云消息")
                        .setText(R.id.tv_content, item.getContent())
                        .setText(R.id.tv_time, item.getCtime());
//                holder.itemView.setOnClickListener(view1 -> ARouter.getInstance().build("/app/WebActivity")
//                        .withBoolean(Constants.IS_SHOW_RIGHT, false)
//                        .withString(Constants.WEB_URL, item.getContent()).navigation());
            }
        }).openRefresh(page -> mPresenter.appMsgs("1", Constants.PAGE_SIZE))
                .openLoadMore(Constants.PAGE_SIZE_INT, page -> mPresenter.appMsgs(String.valueOf(page), Constants.PAGE_SIZE)).reStart();
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
    public void showAppMsgs(AppMsgsBean.DataEntity data) {
        recyclerView.getAdapter().addData(data.getList());
    }
}
