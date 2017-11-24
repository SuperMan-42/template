package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.core.widget.recyclerview.SpacesItemDecoration;
import com.recorder.R;
import com.recorder.di.component.DaggerMyInvestmentComponent;
import com.recorder.di.module.MyInvestmentModule;
import com.recorder.mvp.contract.MyInvestmentContract;
import com.recorder.mvp.presenter.MyInvestmentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/MyInvestmentActivity")
public class MyInvestmentActivity extends BaseActivity<MyInvestmentPresenter> implements MyInvestmentContract.View {
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyInvestmentComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myInvestmentModule(new MyInvestmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_my_investment; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("我的投资");
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
        recyclerView.init(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_my_investment, list) {
            @Override
            protected void convert(BaseViewHolder holder, String item) {
                CoreUtils.imgLoader(MyInvestmentActivity.this, item, holder.getView(R.id.im_cover));
                holder.itemView.setOnClickListener(view1 -> ARouter.getInstance().build("/app/LoginActivity").navigation());
            }
        }, false);
        recyclerView.getRecyclerView().addItemDecoration(new SpacesItemDecoration(0, 36));
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
}
