package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
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
import com.recorder.mvp.presenter.MyMessagePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/MyMessageActivity")
public class MyMessageActivity extends BaseActivity<MyMessagePresenter> implements MyMessageContract.View {
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;

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
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        recyclerView.init(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_message, list) {
            @Override
            protected void convert(BaseViewHolder holder, String item) {
                holder.setText(R.id.tv_title, "股权类产品有了新的项目")
                        .setText(R.id.tv_content, "上线了共享单车、健康饮品等项目，快去查看吧;上线了共享单车、健康饮品等项目，快去查看吧")
                        .setText(R.id.tv_time, "1小时前");
                holder.itemView.setOnClickListener(view1 -> ARouter.getInstance().build("/app/WebActivity")
                        .withBoolean(Constants.IS_SHOW_RIGHT, false)
                        .withString(Constants.WEB_URL, "上线了共享单车、健康饮品等项目，快去查看吧").navigation());
            }
        }, false);
        recyclerView.getRecyclerView().addItemDecoration(new SpacesItemDecoration(30, 45));
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
