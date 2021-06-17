package com.samsia.roleplay

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.samsia.roleplay.viewmodel.BaseViewModel
import com.samsia.roleplay.viewmodel.ListModel
import com.samsia.roleplay.viewmodel.TempViewModel

abstract class ViewModelActivity : AppCompatActivity() {

    var viewModel: BaseViewModel = TempViewModel()
    lateinit var viewBinding: ViewDataBinding
    var bindingComponent: DataBindingComponent? = null
    var savedInstanceState: Bundle? = null
    val createTime: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.savedInstanceState = savedInstanceState
        onCreate()
        initViewModel()
        viewBinding.executePendingBindings()
    }

    abstract fun onCreate()

    override fun onBackPressed() {
        if (viewModel.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        viewModel.onDispose()
        super.onDestroy()
    }

    override fun onStop() {
        viewModel.onStop()
        super.onStop()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewDataBinding> setContentLayout(layoutId: Int): T {

        if (bindingComponent == null) {
            bindingComponent = if (viewModel is ListModel<*>) {
                BaseAdapter(viewModel as ListModel<*>)
            } else {
                CommonBindingComponent()
            }
        }

        viewBinding = DataBindingUtil.setContentView(this, layoutId, bindingComponent)
        viewBinding.setVariable(BR.viewModel, viewModel)
        viewBinding.lifecycleOwner = this
        return viewBinding as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <VM : ViewModel> setLayout(layoutId: Int, modelClass: Class<VM>): VM {
        val vm = ViewModelProviders.of(this).get(modelClass)
        this.viewModel = vm as BaseViewModel

        bindingComponent = if (vm is ListModel<*>) {
            BaseAdapter(vm)
        } else {
            CommonBindingComponent()
        }

        viewBinding = DataBindingUtil.setContentView(this, layoutId, bindingComponent)

        if (bindingComponent is BaseAdapter<*>) {
            val listView = viewBinding.root.findViewById<View>(R.id.list_view)
            if (listView != null) {
                (listView as RecyclerView).adapter = bindingComponent as BaseAdapter<*>
            }
        }

        viewBinding.setVariable(BR.viewModel, viewModel)
        viewBinding.lifecycleOwner = this
        return viewModel as VM
    }

    private fun initViewModel() {
        viewModel.init()
    }

}
