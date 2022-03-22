package com.example.dragonhelpertext1.CustomControl

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.widget.AppCompatTextView

class AdaptionSizeTextView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    AppCompatTextView(context!!, attrs, defStyleAttr), OnGlobalLayoutListener {
    override fun onGlobalLayout() {
        //获取当前字体行数，当行数大于1，字体（px）减一，递归直到符合
        val lineCount = lineCount
        if (lineCount > 1) {
            var textSize = textSize
            textSize--
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        }
    }

    init {
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }
}