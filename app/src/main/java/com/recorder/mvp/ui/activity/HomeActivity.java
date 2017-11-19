package com.recorder.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.core.base.AdapterViewPager;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
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
import com.recorder.mvp.model.entity.ReferFilter;
import com.recorder.mvp.presenter.HomePresenter;
import com.recorder.mvp.ui.fragment.HomeFragment;
import com.recorder.widget.AutoPageNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.majiajie.pagerbottomtabstrip.NavigationController;

import static com.core.utils.Preconditions.checkNotNull;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {
    @BindView(R.id.navigation)
    AutoPageNavigationView navigation;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar_left)
    View back;
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;
    @BindView(R.id.ll_filter)
    View llFilter;

    private static boolean isFilterOpen = false;
    NavigationController mNavigationController;
    private ArrayList<MultiItemEntity> res = new ArrayList<>();

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
        initHome();
        initFilter();
    }

    private void initFilter() {
        mPresenter.getFilter();
    }

    private void initHome() {
        title("首页");
        mNavigationController = navigation.material()
                .addItem(R.mipmap.ic_nav_theme, CoreUtils.getString(this, R.string.Home))
                .addItem(R.mipmap.ic_nav_theme, CoreUtils.getString(this, R.string.Equity))
                .addItem(R.mipmap.ic_nav_theme, CoreUtils.getString(this, R.string.Private))
                .addItem(R.mipmap.ic_nav_theme, CoreUtils.getString(this, R.string.Dynamic))
                .addItem(R.mipmap.ic_nav_theme, CoreUtils.getString(this, R.string.My))
                .build();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        CharSequence[] strings = new CharSequence[]{CoreUtils.getString(this, R.string.Home), CoreUtils.getString(this, R.string.Equity),
                CoreUtils.getString(this, R.string.Private), CoreUtils.getString(this, R.string.Dynamic), CoreUtils.getString(this, R.string.My)};
        AdapterViewPager adapterViewPager = new AdapterViewPager(getSupportFragmentManager(), fragments, strings);
        viewPager.setAdapter(adapterViewPager);
        mNavigationController.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title(viewPager.getAdapter().getPageTitle(viewPager.getCurrentItem()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    @OnClick({R.id.toolbar_left, R.id.toolbar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left:
                isFilterOpen = !isFilterOpen;
                llFilter.setVisibility(isFilterOpen ? View.VISIBLE : View.GONE);
                break;
            case R.id.toolbar_right:
                break;
        }
    }

    @Override
    public void showFilter(ReferFilter referFilter) {
        HeaderItem headerItem = new HeaderItem("行业", null);
        for (int i = 0; i < 10; i++) {
            headerItem.addSubItem(new ContentItem("全部", (i + 1) % 4 == 0));
        }
        res.add(headerItem);
        HeaderItem headerItem1 = new HeaderItem("轮次", null);
        for (int i = 0; i < 8; i++) {
            headerItem1.addSubItem(new ContentItem("智能", (i + 1) % 4 == 0));
        }
        res.add(headerItem1);
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
        adapter.expandAll();
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
        public boolean is = true;

        public ContentItem(String title, boolean is) {
            this.title = title;
            this.is = is;
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
                            .setVisible(R.id.view_space, lv1.is);
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
            super.onBackPressed();
        }
    }
}
