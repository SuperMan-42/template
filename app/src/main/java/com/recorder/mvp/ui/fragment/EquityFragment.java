package com.recorder.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseFragment;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.recorder.R;
import com.recorder.di.component.DaggerEquityComponent;
import com.recorder.di.module.EquityModule;
import com.recorder.mvp.contract.EquityContract;
import com.recorder.mvp.model.entity.EquityBean;
import com.recorder.mvp.presenter.EquityPresenter;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

public class EquityFragment extends BaseFragment<EquityPresenter> implements EquityContract.View {
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;

    public static EquityFragment newInstance() {
        EquityFragment fragment = new EquityFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerEquityComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .equityModule(new EquityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equity, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.getEquity(null, null, null, null, null, null);
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行
     * setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
     * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }

    @Override
    public void showEquity(EquityBean equityBean) {
        recyclerView.init(new BaseQuickAdapter<EquityBean.DataEntity.ListEntity, BaseViewHolder>(R.layout.item_equity, equityBean.getData().getList()) {
            @Override
            protected void convert(BaseViewHolder holder, EquityBean.DataEntity.ListEntity item) {
                CoreUtils.imgLoader(getContext(), "http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg", holder.getView(R.id.im_cover));
                holder.setText(R.id.tv_deal_name, item.getDeal_name())
                        .setText(R.id.tv_brief, item.getBrief())
                        .setText(R.id.tv_labels, item.getLabels().get(0))
                        .setText(R.id.tv_round, item.getRound())
                        .setText(R.id.tv_online_str, item.getOnline_str());
                EquityBean.DataEntity.ListEntity.ViewFooterEntity viewFooterEntity = item.getView_footer();
                if (viewFooterEntity != null) {
                    holder.setText(R.id.tv_view, String.valueOf(viewFooterEntity.getView()))
                            .setText(R.id.tv_focus, String.valueOf(viewFooterEntity.getFocus()))
                            .setText(R.id.tv_consult, String.valueOf(viewFooterEntity.getConsult()))
                            .setVisible(R.id.ll_view_footer, true);
                } else {
                    holder.setVisible(R.id.ll_view_footer, false);
                }
                holder.itemView.setOnClickListener(view1 -> ARouter.getInstance().build("/app/EquityDetailsActivity").navigation());
            }
        }, false);
    }
}
