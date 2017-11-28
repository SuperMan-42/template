/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.core.widget.autolayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.utils.AutoLayoutHelper;

/**
 * ================================================
 * 实现 AndroidAutoLayout 规范的 {@link ScrollView}
 * 可使用 MVP_generator_solution 中的 AutoView 模版生成各种符合 AndroidAutoLayout 规范的 {@link View}
 * ================================================
 */
public class AutoScrollView extends ScrollView {
    public interface TranslucentListener {
        void onTranslucent(float alpha, int t);
    }

    private TranslucentListener translucentListener;
    private int mScrollY;

    private AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    public AutoScrollView(Context context) {
        super(context);
    }

    public AutoScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode())
            mHelper.adjustChildren();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mScrollY = t;
        super.onScrollChanged(l, t, oldl, oldt);
        if (translucentListener != null) {
            // alpha = 滑出去的高度/(screenHeight/3);
            float heightPixels = getContext().getResources().getDisplayMetrics().heightPixels;
            float scrollY = getScrollY();//该值 大于0
            float alpha = scrollY / (heightPixels / 3);// 0~1  透明度是1~0
            //这里选择的screenHeight的1/3 是alpha改变的速率 （根据你的需要你可以自己定义）
            if (alpha >= 0 && alpha <= 1) {
                translucentListener.onTranslucent(alpha, t);
            } else {
                translucentListener.onTranslucent(1, t);
            }
        }
    }

    public boolean isTop() {
        return mScrollY <= 0;
    }

    public void setTranslucentListener(TranslucentListener listener) {
        this.translucentListener = listener;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AutoScrollView.LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ScrollView.LayoutParams
            implements AutoLayoutHelper.AutoLayoutParams {
        private AutoLayoutInfo mAutoLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
        }

        @Override
        public AutoLayoutInfo getAutoLayoutInfo() {
            return mAutoLayoutInfo;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }
}
