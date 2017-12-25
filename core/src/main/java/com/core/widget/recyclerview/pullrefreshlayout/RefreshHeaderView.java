package com.core.widget.recyclerview.pullrefreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.R;
import com.cunoraz.gifview.library.GifView;

/**
 * Created by Aspsine on 2015/11/5.
 */
public class RefreshHeaderView extends RelativeLayout implements SwipeTrigger, SwipeRefreshTrigger {

    private TextView tvRefresh;

    private GifView ivRefresh;

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
    }

    @Override
    public void onRefresh() {
        if (!ivRefresh.isPlaying()) {
            ivRefresh.play();
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
        if (!ivRefresh.isPlaying()) {
            ivRefresh.play();
        }
        tvRefresh.setText(getContext().getString(R.string.refresh_down));
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onReset() {
        if (!ivRefresh.isPaused()) {
            ivRefresh.pause();
        }
        tvRefresh.setText(getContext().getString(R.string.refresh_pull));
    }
}
