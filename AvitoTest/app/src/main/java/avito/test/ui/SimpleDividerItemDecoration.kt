package avito.test.ui

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import avito.test.R

public class SimpleDividerItemDecoration(resources: Resources) : RecyclerView.ItemDecoration() {
    private val mDivider: Drawable

    init {
        mDivider = resources.getDrawable(R.drawable.line_divider)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.getPaddingLeft()
        val right = parent.getWidth() - parent.getPaddingRight()

        val childCount = parent.getChildCount()
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)

            val params = child.getLayoutParams() as RecyclerView.LayoutParams

            val top = child.getBottom() + params.bottomMargin
            val bottom = top + mDivider.getIntrinsicHeight()

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}