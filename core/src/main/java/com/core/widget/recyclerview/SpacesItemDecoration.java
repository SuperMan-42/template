package com.core.widget.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by clevo on 2015/7/27.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int left;
    private int top;

    public SpacesItemDecoration(int left, int top) {
        this.left = left;
        this.top = top;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = left;
        outRect.right = left;
        outRect.bottom = top;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = top;
        }
    }
}
