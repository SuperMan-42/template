package com.recorder.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseFragment;
import com.core.di.component.AppComponent;
import com.core.http.imageloader.ImageLoader;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.recorder.R;
import com.recorder.di.component.DaggerHomeComponent;
import com.recorder.di.module.HomeModule;
import com.recorder.mvp.contract.HomeContract;
import com.recorder.mvp.model.entity.ReferFilter;
import com.recorder.mvp.presenter.HomePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/HomeFragment")
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {

    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        View view = CoreUtils.inflate(getContext(), R.layout.item_home_header);
        BGABanner banner = view.findViewById(R.id.banner);
        List<String> list = new ArrayList<>();
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f19cc0079.jpg");
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1ac12d1d.jpg");
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1bad97d1.jpg");
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1c83c228.jpg");
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1d53e3dd.jpg");
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1e37fea9.jpg");
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1ef4d709.jpg");
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f20b3ea10.jpg");
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f21927f8d.jpg");
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        banner.setAdapter((banner1, itemView, model, position) -> CoreUtils.imgLoader(getContext(), (String) model, (ImageView) itemView));
        banner.setData(list, null);
        recyclerView.init(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_home, list) {
            @Override
            protected void convert(BaseViewHolder holder, String item) {
                CoreUtils.imgLoader(getContext(), item, holder.getView(R.id.im_pic));
                holder.itemView.setOnClickListener(view1 -> ARouter.getInstance().build("/app/LoginActivity").navigation());
            }
        }, false);
        recyclerView.addHeaderView(view);
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
    public void showFilter(ImageLoader imageLoader, ReferFilter referFilter) {

    }

}
