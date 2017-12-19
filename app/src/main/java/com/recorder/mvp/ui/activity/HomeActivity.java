package com.recorder.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.core.base.AdapterViewPager;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.http.imageloader.ImageLoader;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseMultiItemQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.core.widget.recyclerview.entity.AbstractExpandableItem;
import com.core.widget.recyclerview.entity.MultiItemEntity;
import com.recorder.R;
import com.recorder.di.component.DaggerHomeComponent;
import com.recorder.di.module.HomeModule;
import com.recorder.mvp.contract.HomeContract;
import com.recorder.mvp.model.entity.DealFilter;
import com.recorder.mvp.model.entity.HomeRecommendBean;
import com.recorder.mvp.presenter.HomePresenter;
import com.recorder.mvp.ui.fragment.DynamicFragment;
import com.recorder.mvp.ui.fragment.EquityFragment;
import com.recorder.mvp.ui.fragment.HomeFragment;
import com.recorder.mvp.ui.fragment.MyFragment;
import com.recorder.mvp.ui.fragment.PrivateFragment;
import com.recorder.utils.CommonUtils;
import com.recorder.widget.AutoPageNavigationView;
import com.recorder.widget.AutoViewPager;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/HomeActivity")
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {

    @BindView(R.id.navigation)
    AutoPageNavigationView navigation;
    @BindView(R.id.viewPager)
    AutoViewPager viewPager;
    @BindView(R.id.toolbar_left)
    View back;
    @BindView(R.id.im_left)
    ImageView imLeft;
    @BindView(R.id.toolbar_right)
    View search;
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;
    @BindView(R.id.ll_filter)
    View llFilter;
    @BindView(R.id.view_empty)
    View viewEmpty;

    private List<String> lablesList = new ArrayList<>();
    private List<String> lablesNameList = new ArrayList<>();
    private List<String> roundList = new ArrayList<>();
    private List<String> roundNameList = new ArrayList<>();
    private static boolean isFilterOpen = false;
    NavigationController mNavigationController;
    private ArrayList<MultiItemEntity> res = new ArrayList<>();
    private static long firstTime = 0;
    private int position = 0;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_home; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "xRXgNId4ct4tDpNrC5BOAGsb");
        imLeft.setImageResource(R.drawable.title_fliter);
        ExpandableItemAdapter adapter = new ExpandableItemAdapter(res);
        final GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == ExpandableItemAdapter.TYPE_CONTENT ? 1 : manager.getSpanCount();
            }
        });
        recyclerView.init(manager, adapter, false);
        recyclerView.getRecyclerView().addItemDecoration(new SimpleDividerDecoration(this));
        if (getLastCustomNonConfigurationInstance() == null) {
            mPresenter.getPermissons();
            mPresenter.dealFilter();
        } else {
            recyclerView.getAdapter().setNewData((List) getLastCustomNonConfigurationInstance());
        }
        initHome();
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Subscriber(tag = Constants.HOME_INDEX)
    public void init(int index) {
        mNavigationController.setSelect(index);
    }

    private void initHome() {
        title("般若云");
        back.setVisibility(View.INVISIBLE);
        mNavigationController = navigation.custom()
                .addItem(newItem(R.drawable.bottom_home, R.drawable.bottom_home_selector, CoreUtils.getString(this, R.string.Home)))
                .addItem(newItem(R.drawable.bottom_equity, R.drawable.bottom_equity_selector, CoreUtils.getString(this, R.string.Equity)))
                .addItem(newItem(R.drawable.bottom_private, R.drawable.bottom_private_selector, CoreUtils.getString(this, R.string.Private)))
                .addItem(newItem(R.drawable.bottom_dynamic, R.drawable.bottom_dynamic_selector, CoreUtils.getString(this, R.string.Dynamic)))
                .addItem(newItem(R.drawable.bottom_my, R.drawable.bottom_my_selector, CoreUtils.getString(this, R.string.My)))
                .build();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(EquityFragment.newInstance());
        fragments.add(PrivateFragment.newInstance());
        fragments.add(DynamicFragment.newInstance());
        fragments.add(MyFragment.newInstance());
        CharSequence[] strings = new CharSequence[]{CoreUtils.getString(this, R.string.Home), CoreUtils.getString(this, R.string.Equity),
                CoreUtils.getString(this, R.string.Private), CoreUtils.getString(this, R.string.Dynamic), CoreUtils.getString(this, R.string.My)};
        AdapterViewPager adapterViewPager = new AdapterViewPager(getSupportFragmentManager(), fragments, strings);
        viewPager.setAdapter(adapterViewPager);
        viewPager.setOffscreenPageLimit(0);
        mNavigationController.setupWithViewPager(viewPager);
        mNavigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                position = index;
                viewEmpty.setVisibility(View.GONE);
                search.setVisibility(index == 1 || index == 2 ? View.VISIBLE : View.INVISIBLE);
                back.setVisibility(index == 1 || index == 2 ? View.VISIBLE : View.INVISIBLE);
                CoreUtils.obtainRxCache(getApplicationContext()).remove("isClear");
                title(viewPager.getAdapter().getPageTitle(viewPager.getCurrentItem()));
                findViewById(R.id.toolbar).setVisibility(index == 4 ? View.GONE : View.VISIBLE);
                if (index == 4) {
                    findViewById(R.id.view_empty).setVisibility(View.GONE);
                }
                if (index == 1 || index == 2) {
                    resetFilter();
                }
            }

            @Override
            public void onRepeat(int index) {

            }
        });
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return CoreUtils.obtainRxCache(this).get("res");
    }

    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable, checkedDrawable, text);
        normalItemView.setTextDefaultColor(0xFF878EA3);
        normalItemView.setTextCheckedColor(0xFF3F56DC);
        return normalItemView;
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

    @OnClick({R.id.toolbar_left, R.id.toolbar_right, R.id.tv_filter_reset, R.id.tv_filter_do})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left:
                isFilterOpen = !isFilterOpen;
                llFilter.setVisibility(isFilterOpen ? View.VISIBLE : View.GONE);
                break;
            case R.id.toolbar_right:
                switch (mNavigationController.getSelected()) {
                    case 1:
                        ARouter.getInstance().build("/app/SearchActivity").withBoolean(Constants.IS_EQUITY, true).navigation();
                        break;
                    case 2:
                        ARouter.getInstance().build("/app/SearchActivity").withBoolean(Constants.IS_EQUITY, false).navigation();
                        break;
                }
                break;
            case R.id.tv_filter_reset:
                resetFilter();
                break;
            case R.id.tv_filter_do:
                lablesList.clear();
                lablesNameList.clear();
                roundList.clear();
                roundNameList.clear();
                for (Object entity : recyclerView.getAdapter().getData()) {
                    if (entity instanceof ContentItem && ((ContentItem) entity).isSelector) {
                        if (((ContentItem) entity).isLable) {
                            lablesList.add(((ContentItem) entity).id);
                            lablesNameList.add(((ContentItem) entity).title);
                        } else {
                            roundList.add(((ContentItem) entity).id);
                            roundNameList.add(((ContentItem) entity).title);
                        }
                    }
                }
                isFilterOpen = !isFilterOpen;
                llFilter.setVisibility(isFilterOpen ? View.VISIBLE : View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("lables", CommonUtils.toStringFromList(lablesList, ","));
                bundle.putString("lablesName", CommonUtils.toStringFromList(lablesNameList, ","));
                bundle.putString("round", CommonUtils.toStringFromList(roundList, ","));
                bundle.putString("roundName", CommonUtils.toStringFromList(roundNameList, ","));
                switch (mNavigationController.getSelected()) {
                    case 1:
                        EventBus.getDefault().post(bundle, "equityfragment");
                        break;
                    case 2:
                        EventBus.getDefault().post(bundle, "privatefragment");
                        break;
                }
                viewEmpty.setVisibility(View.GONE);
                break;
        }
    }

    private void resetFilter() {
        if (recyclerView.getAdapter() == null)
            return;
        for (Object entity : recyclerView.getAdapter().getData()) {
            if (entity instanceof ContentItem) {
                ((ContentItem) entity).isSelector = false;
            }
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void showFilter(ImageLoader imageLoader, DealFilter.DataEntity data) {
        HeaderItem headerItem = new HeaderItem("行业", null);
        for (int i = 0; i < data.getLabels().size(); i++) {
            headerItem.addSubItem(new ContentItem(data.getLabels().get(i).getName(), data.getLabels().get(i).getId(), (i + 1) % 4 == 0, false, true));
        }
        res.add(headerItem);
        HeaderItem headerItem1 = new HeaderItem("轮次", null);
        for (int i = 0; i < data.getRounds().size(); i++) {
            headerItem1.addSubItem(new ContentItem(data.getRounds().get(i).getName(), data.getRounds().get(i).getId(), (i + 1) % 4 == 0, false, false));
        }
        res.add(headerItem1);
        recyclerView.getAdapter().setNewData(res);
        recyclerView.getAdapter().expandAll();
        CoreUtils.obtainRxCache(this).put("res", res);
    }

    @Override
    public void showHomeRecomment(HomeRecommendBean.DataEntity dataEntity) {

    }

    private static class HeaderItem extends AbstractExpandableItem<MultiItemEntity> implements MultiItemEntity {
        public String title;

        public HeaderItem(String title, MultiItemEntity type) {
            if (type != null)
                addSubItem(type);
            this.title = title;
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

    private static class ContentItem implements MultiItemEntity {
        public String title;
        public String id;
        public boolean is = true;
        public boolean isSelector = false;
        public boolean isLable;

        public ContentItem(String title, String id, boolean is, boolean isSelector, boolean isLable) {
            this.title = title;
            this.id = id;
            this.is = is;//是否是边缘
            this.isSelector = isSelector;//是否选中
            this.isLable = isLable;//是否是lable
        }

        @Override
        public int getItemType() {
            return ExpandableItemAdapter.TYPE_CONTENT;
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
            addItemType(TYPE_HEADER, R.layout.item_filter_header);
            addItemType(TYPE_CONTENT, R.layout.item_filter_content);
        }

        @Override
        protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
            switch (holder.getItemViewType()) {
                case TYPE_HEADER:
                    final HeaderItem lv0 = (HeaderItem) item;
                    holder.setText(R.id.tv_filter_header, lv0.title)
                            .setVisible(R.id.view_space, holder.getAdapterPosition() != 0);
                    break;
                case TYPE_CONTENT:
                    final ContentItem lv1 = (ContentItem) item;
                    holder.setText(R.id.tv_filter_content, lv1.title)
                            .setTextColor(R.id.tv_filter_content, Color.parseColor(lv1.isSelector ? "#3F56DC" : "#333333"))
                            .setVisible(R.id.view_space, lv1.is);
                    holder.getView(R.id.tv_filter_content).setSelected(lv1.isSelector);
                    holder.itemView.setOnClickListener(view -> {
                        lv1.isSelector = !lv1.isSelector;
                        holder.getView(R.id.tv_filter_content).setSelected(lv1.isSelector);
                        holder.setTextColor(R.id.tv_filter_content, Color.parseColor(lv1.isSelector ? "#3F56DC" : "#333333"));
                    });
                    break;
            }
        }
    }

    private class SimpleDividerDecoration extends RecyclerView.ItemDecoration {
        private Paint dividerPaint;

        public SimpleDividerDecoration(Context context) {
            dividerPaint = new Paint();
            dividerPaint.setColor(context.getResources().getColor(R.color.transparent));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = 25;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int childCount = parent.getChildCount();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            for (int i = 0; i < childCount - 1; i++) {
                View view = parent.getChildAt(i);
                float top = view.getBottom();
                float bottom = view.getBottom() + 25;
                c.drawRect(left, top, right, bottom, dividerPaint);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isFilterOpen) {
            llFilter.setVisibility(View.GONE);
            isFilterOpen = !isFilterOpen;
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 1000) {//如果两次按键时间间隔大于1000毫秒，则不退出
                CoreUtils.snackbarText(CoreUtils.getString(this, R.string.text_exit));
                firstTime = secondTime;//更新firstTime
            } else {
                CoreUtils.exitApp();
                overridePendingTransition(0, R.anim.zoom_out);
            }
        }
    }
}
