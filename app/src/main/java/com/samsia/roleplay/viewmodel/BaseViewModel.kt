package com.samsia.roleplay.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*

abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable() // 한페이지 안에서의 범주인경우
    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun onDispose() {
        compositeDisposable.dispose()
    }

    abstract fun init()

    abstract fun onBackPressed(): Boolean

    abstract fun onStop()

    open fun onResume() { }

    open fun initDatabase(context: Context) { }
}
