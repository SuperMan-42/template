package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

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
import com.recorder.di.component.DaggerMyAttentionComponent;
import com.recorder.di.module.MyAttentionModule;
import com.recorder.mvp.contract.MyAttentionContract;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.model.entity.UserFollowListBean;
import com.recorder.mvp.presenter.MyAttentionPresenter;
import com.recorder.utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/MyAttentionActivity")
public class MyAttentionActivity extends BaseActivity<MyAttentionPresenter> implements MyAttentionContract.View {
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyAttentionComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myAttentionModule(new MyAttentionModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_my_attention; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("我的关注");
        mPresenter.userFollowlist("1", Constants.PAGE_SIZE);
        recyclerView.init(new BaseQuickAdapter<UserFollowListBean.DataEntity.ListEntity, BaseViewHolder>(R.layout.item_equity) {
            @Override
            protected void convert(BaseViewHolder holder, UserFollowListBean.DataEntity.ListEntity item) {
                CoreUtils.imgLoader(getApplication(), item.getCover(), R.drawable.ic_list, holder.getView(R.id.im_cover));
                holder.setText(R.id.tv_deal_name, item.getDeal_name())
                        .setText(R.id.tv_brief, item.getBrief())
                        .setText(R.id.tv_labels, item.getLabels())
                        .setText(R.id.tv_round, item.getRound())
                        .setText(R.id.tv_online_str, item.getOnline_str())
                        .setVisible(R.id.tv_is_group, item.getIs_group().equals("1"))
                        .setVisible(R.id.tv_brief, !item.getIs_group().equals("1"));
                ((LinearLayout) holder.getView(R.id.ll_tag)).setOrientation(item.getIs_group().equals("1") ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
                UserFollowListBean.DataEntity.ListEntity.ViewFooterEntity viewFooterEntity = item.getView_footer();
                if (viewFooterEntity.getConsult() != null || viewFooterEntity.getFocus() != null || viewFooterEntity.getView() != null) {
                    holder.setText(R.id.tv_view, String.valueOf(viewFooterEntity.getView()))
                            .setText(R.id.tv_focus, String.valueOf(viewFooterEntity.getFocus()))
                            .setText(R.id.tv_consult, String.valueOf(viewFooterEntity.getConsult()))
                            .setVisible(R.id.ll_view_footer, true);
                } else {
                    holder.setVisible(R.id.ll_view_footer, false);
                }
                holder.itemView.setOnClickListener(view1 -> ARouter.getInstance().build("/app/EquityDetailsActivity")
                        .withBoolean(Constants.IS_EQUITY, true).withString(Constants.DEAL_ID, item.getDealID()).navigation());//TODO 少一个字段
            }
        }).openRefresh(page -> mPresenter.userFollowlist("1", Constants.PAGE_SIZE))
                .openLoadMore(Constants.PAGE_SIZE_INT, page -> mPresenter.userFollowlist(String.valueOf(page), Constants.PAGE_SIZE)).reStart();
    }

    @Subscriber(tag = Constants.RETRY_MYATTENTION)
    private void retry(LoginBean loginBean) {
        findViewById(R.id.view_empty).setVisibility(View.GONE);
        mPresenter.userFollowlist("1", Constants.PAGE_SIZE);
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
    public void showUserFollowList(UserFollowListBean.DataEntity data) {
        recyclerView.getAdapter().addData(data.getList());
    }
}
