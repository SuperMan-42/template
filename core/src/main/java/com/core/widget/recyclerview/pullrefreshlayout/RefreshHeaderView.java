package com.core.widget.recyclerview.pullrefreshlayout;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.R;

/**
 * Created by Aspsine on 2015/11/5.
 */
public class RefreshHeaderView extends RelativeLayout implements SwipeTrigger, SwipeRefreshTrigger {

    private TextView tvRefresh;

    private ImageView ivRefresh;

    private AnimationDrawable mAnimDrawable;

    private Animation mTwinkleAnim;

    private int mTriggerOffset;


    public RefreshHeaderView(Context context) {
        super(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTriggerOffset = context.getResources().getDimensionPixelOffset(R.dimen.dimen_100dp);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivRefresh = findViewById(R.id.ivRefresh);
        tvRefresh = findViewById(R.id.tvRefresh);
        mAnimDrawable = (AnimationDrawable) ivRefresh.getBackground();
    }

    @Override
    public void onRefresh() {
        if (!mAnimDrawable.isRunning()) {
            mAnimDrawable.start();
        }
    }

    @Override
    public void onPrepare() {
        tvRefresh.setText(getContext().getString(R.string.refresh_pull));
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
    }

    @Override
    public void onRelease() {
        if (!mAnimDrawable.isRunning()) {
            mAnimDrawable.start();
        }
        tvRefresh.setText(getContext().getString(R.string.refresh_down));
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onReset() {
        mAnimDrawable.stop();
        tvRefresh.setText(getContext().getString(R.string.refresh_pull));
    }
}
