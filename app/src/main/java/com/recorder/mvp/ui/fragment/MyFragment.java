package com.recorder.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
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
import com.recorder.di.component.DaggerMyComponent;
import com.recorder.di.module.MyModule;
import com.recorder.mvp.contract.MyContract;
import com.recorder.mvp.model.entity.Bean;
import com.recorder.mvp.presenter.MyPresenter;

import java.util.ArrayList;
import java.util.List;

import app.dinus.com.itemdecoration.GridDividerItemDecoration;
import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

public class MyFragment extends BaseFragment<MyPresenter> implements MyContract.View {
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;

    public static MyFragment newInstance() {

        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMyComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myModule(new MyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        List<Bean> list = new ArrayList<>();
        list.add(new Bean("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f19cc0079.jpg", "我的消息"));
        list.add(new Bean("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1ac12d1d.jpg", "投资帮助"));
        list.add(new Bean("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1bad97d1.jpg", "设置"));
        list.add(new Bean("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1c83c228.jpg", "推荐项目"));
        list.add(new Bean("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1d53e3dd.jpg", "推荐给朋友"));
        list.add(new Bean("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1e37fea9.jpg", "投资人认证"));
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.init(layoutManager, new BaseQuickAdapter<Bean, BaseViewHolder>(R.layout.item_my, list) {
            @Override
            protected void convert(BaseViewHolder holder, Bean item) {
                CoreUtils.imgLoader(getContext(), item.getKey(), holder.getView(R.id.im_pic));
                holder.setText(R.id.tv_content, item.getValue());
                holder.itemView.setOnClickListener(view1 -> ARouter.getInstance().build("/app/EquityDetailsActivity").navigation());
            }
        }, false);
        GridDividerItemDecoration dividerItemDecoration = new GridDividerItemDecoration(getActivity(), GridDividerItemDecoration.GRID_DIVIDER_VERTICAL);
        dividerItemDecoration.setVerticalDivider(CoreUtils.getDrawablebyResource(getContext(), R.drawable.bga_divider));
        dividerItemDecoration.setHorizontalDivider(CoreUtils.getDrawablebyResource(getContext(), R.drawable.bga_divider));
        recyclerView.getRecyclerView().addItemDecoration(dividerItemDecoration);
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
}