package com.samsia.roleplay
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.samsia.roleplay.viewmodel.BaseViewModel
import com.samsia.roleplay.viewmodel.ListModel

class BaseAdapter<T>(private val viewModel: ListModel<T>) : BaseBindAdapter<BaseAdapter.ViewHolder, T>() {

    override fun getListModel(): ListModel<T> {
        return viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false, this)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, position, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return viewModel.getItemType(position)
    }

    override fun getItemCount(): Int {
        return getListModel().getItemCount()
    }

    class ViewHolder(private val binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: BaseViewModel, position: Int, data: Any?) {
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.position, position)
            binding.setVariable(BR.data, data)
            binding.executePendingBindings()
        }
    }
}