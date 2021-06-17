package com.samsia.roleplay.viewmodel

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EditTextViewModel: TextWatcher {

    private val _endIcon = MutableLiveData<TabIcon>()
    val endIcon: LiveData<TabIcon> = _endIcon

    var actionIcon = TabIcon()

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val icon = if (TextUtils.isEmpty(s)) null else actionIcon
        _endIcon.value = icon
    }

    class TabIcon() {
        var iconRes = 0
        var action: TabIconListener? = null
    }

    interface TabIconListener {
        fun onTab(text: String)
    }

}
