package com.core.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.core.R;
import com.core.utils.AnimUtil;

public class CustomPopupWindow extends PopupWindow {
    private View mContentView;
    private View mParentView;
    private CustomPopupWindowListener mListener;
    private boolean isOutsideTouch;
    private boolean isFocus;
    private Drawable mBackgroundDrawable;
    private int mAnimationStyle;
    private boolean isWrap;
    private static CustomPopupWindow window;
    private float bgAlpha = 1f;
    private boolean bright = false;

    private CustomPopupWindow(Builder builder) {
        this.mContentView = builder.contentView;
        this.mParentView = builder.parentView;
        this.mListener = builder.listener;
        this.isOutsideTouch = builder.isOutsideTouch;
        this.isFocus = builder.isFocus;
        this.mBackgroundDrawable = builder.backgroundDrawable;
        this.mAnimationStyle = builder.animationStyle;
        this.isWrap = builder.isWrap;
        initLayout();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void initLayout() {
        setContentView(mContentView);
        mListener.initPopupView(mContentView);
        setWidth(isWrap ? LayoutParams.WRAP_CONTENT : LayoutParams.MATCH_PARENT);
        setHeight(isWrap ? LayoutParams.WRAP_CONTENT : LayoutParams.MATCH_PARENT);
        setFocusable(isFocus);
        setOutsideTouchable(isOutsideTouch);
        setBackgroundDrawable(mBackgroundDrawable);
        setClippingEnabled(false);
        if (mAnimationStyle != -1)//如果设置了动画则使用动画
            setAnimationStyle(mAnimationStyle);
    }

    /**
     * 获得用于展示popup内容的view
     *
     * @return
     */
    public View getContentView() {
        return mContentView;
    }

    /**
     * 用于填充contentView,必须传ContextThemeWrapper(比如activity)不然popupwindow要报错
     *
     * @param context
     * @param layoutId
     * @return
     */
    public static View inflateView(ContextThemeWrapper context, int layoutId) {
        return LayoutInflater.from(context)
                .inflate(layoutId, null);
    }

    public void show() {//默认显示到中间
        if (mParentView == null) {
            showAtLocation(mContentView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            showAtLocation(mParentView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    public void show(Activity activity) {//默认显示到中间
        show();
        AnimUtil animUtil = new AnimUtil();
        toggleBright(activity, animUtil);
        setOnDismissListener(() -> toggleBright(activity, animUtil));
    }

    public static final class Builder {
        private View contentView;
        private View parentView;
        private CustomPopupWindowListener listener;
        private boolean isOutsideTouch = true;//默认为true
        private boolean isFocus = true;//默认为true
        private Drawable backgroundDrawable = new ColorDrawable(0x50000000);//默认为透明
        private int animationStyle = R.style.popwindow_anim_default;
        private boolean isWrap;

        private Builder() {
        }

        public Builder contentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder parentView(View parentView) {
            this.parentView = parentView;
            return this;
        }

        public Builder isWrap(boolean isWrap) {
            this.isWrap = isWrap;
            return this;
        }

        public Builder customListener(CustomPopupWindowListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder isOutsideTouch(boolean isOutsideTouch) {
            this.isOutsideTouch = isOutsideTouch;
            return this;
        }

        public Builder isFocus(boolean isFocus) {
            this.isFocus = isFocus;
            return this;
        }

        public Builder backgroundDrawable(Drawable backgroundDrawable) {
            this.backgroundDrawable = backgroundDrawable;
            return this;
        }

        public Builder animationStyle(int animationStyle) {
            this.animationStyle = animationStyle;
            return this;
        }

        public CustomPopupWindow build() {
            if (contentView == null)
                throw new IllegalStateException("contentView is required");
            if (listener == null)
                throw new IllegalStateException("CustomPopupWindowListener is required");

            window = new CustomPopupWindow(this);
            return window;
        }
    }

    public static void killMySelf() {
        if (window != null & window.isShowing()) {
            window.dismiss();
        }
    }

    public interface CustomPopupWindowListener {
        void initPopupView(View contentView);
    }

    private void toggleBright(Activity activity, AnimUtil animUtil) {
        //三个参数分别为： 起始值 结束值 时长  那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(0.7f, 1f, 300);
        animUtil.addUpdateListener(progress -> {
            //此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
            bgAlpha = bright ? progress : (1.7f - progress);//三目运算，应该挺好懂的。
            backgroundAlpha(activity, bgAlpha);//在此处改变背景，这样就不用通过Handler去刷新了。
        });
        animUtil.addEndListner(animator -> {
            //在一次动画结束的时候，翻转状态
            bright = !bright;
            if (!bright && bgAlpha == 1) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND | WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        animUtil.startAnimator();
    }

    /***
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
