package com.alucardlogistics.databaseapp.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class LinedEditText(
    context: Context?,
    attrs: AttributeSet?
): AppCompatEditText(context, attrs) {

    private val mRect: Rect = Rect()
    private val mPaint: Paint = Paint()
    override fun onDraw(canvas: Canvas) {

        // get the height of the view
        val height: Int = (this.parent as View).height
        val lineHeight: Int = lineHeight
        val numberOfLines = height / lineHeight
        val r: Rect = mRect
        val paint: Paint = mPaint
        var baseline: Int = getLineBounds(0, r)
        for (i in 0 until numberOfLines) {
            canvas.drawLine(
                r.left.toFloat(),
                (baseline + 1).toFloat(),
                r.right.toFloat(),
                (baseline + 1).toFloat(),
                paint)
            baseline += lineHeight
        }
        super.onDraw(canvas)
    }

    // we need this constructor for LayoutInflater
    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 2F
        mPaint.color = Color.DKGRAY // Color of the lines on paper
    }
}