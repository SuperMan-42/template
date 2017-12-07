package com.core.widget.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.core.utils.CoreUtils;

/**
 * Created by hpw on 17-12-7.
 */

public class GirdDecoration extends RecyclerView.ItemDecoration {
    private Paint dividerPaint;
    private int offsets;

    public GirdDecoration(Context context, int offset) {
        dividerPaint = new Paint();
        offsets = CoreUtils.dip2px(context, offset);
        dividerPaint.setColor(Color.TRANSPARENT);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = offsets;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + offsets;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
}
