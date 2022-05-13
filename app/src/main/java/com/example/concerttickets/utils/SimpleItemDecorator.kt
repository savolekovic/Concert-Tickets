package com.example.concerttickets.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class SimpleItemDecorator(var space: Int, var isHorizontalLayout: Boolean) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (isHorizontalLayout) {
            outRect.bottom = space
            outRect.right = space
            outRect.left = space
            outRect.top = space
        } else {
            outRect.bottom = space
            if (parent.getChildAdapterPosition(view) == 0) outRect.top = space else outRect.top = 0
        }
    }


}