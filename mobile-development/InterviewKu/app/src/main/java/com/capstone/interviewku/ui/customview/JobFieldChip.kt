package com.capstone.interviewku.ui.customview

import android.content.Context
import android.util.AttributeSet
import com.capstone.interviewku.data.network.response.JobField
import com.google.android.material.chip.Chip

class JobFieldChip : Chip {
    var jobField: JobField? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(
        context,
        attrs,
        defStyleAttr
    )
}