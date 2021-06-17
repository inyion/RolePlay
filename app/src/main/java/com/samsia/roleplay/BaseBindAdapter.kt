package com.samsia.roleplay

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingComponent
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samsia.roleplay.viewmodel.ListModel
import java.util.*

abstract class BaseBindAdapter<VH : RecyclerView.ViewHolder, T>
    : RecyclerView.Adapter<VH>(), DataBindingComponent, ItemTouchCallback.OnItemMoveListener {

    override fun getCommonBindingComponent(): CommonBindingComponent {
        return bindingComponent
    }

    override fun getBaseBindAdapter(): BaseBindAdapter<VH, T> {
        return this
    }

    val bindingComponent = CommonBindingComponent()
    var itemTouchHelper: ItemTouchHelper? = null

    abstract fun getListModel(): ListModel<T>

    fun getItem(position: Int): T {
        return getListModel().getItem(position)
    }

    fun setItemTouchHandle(view: RecyclerView) {
        val callback = ItemTouchCallback(this)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper!!.attachToRecyclerView(view)
    }

    fun getItemHandleHelper(): ItemTouchHelper? {
        return itemTouchHelper
    }

    override fun getItemCount(): Int {
        return getListModel().getItemCount()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(getListModel().getItemList(), fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    @BindingAdapter("preItemLoad")
    fun preItemLoad(view: RecyclerView, itemCount: LiveData<Int>?) {
        if (itemCount != null && itemCount.value != null && itemCount.value!! >= 0) {
            view.adapter!!.notifyItemRangeInserted(0, itemCount.value!!)
            view.adapter!!.notifyItemChanged(itemCount.value!!)
            Log.i("BaseAdapter", "preItemLoad " + itemCount.value!!)
        }
    }

    //    @Deprecated("setItems", ReplaceWith("view.adapter!!.notifyDataSetChanged()"))
    @BindingAdapter("setItems")
    fun setItems(view: RecyclerView, items: LiveData<ArrayList<T>>?) {
        if (items != null && items.value != null && items.value!!.size > 0) {

//            if ((view.getSafeContext() as ViewModelActivity).viewModel is ChatModel) {
//                val lastPosition = items.value!!.size - 1
//                try {
//                    view.layoutManager!!.scrollToPosition(lastPosition)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                Log.i("BaseAdapter", "scrollPosition = $lastPosition")
//            } else {
//            }

            // activity 가 create -> resumed 까지 는 notifyDataSetChanged call 하지 않아도 알아서 처리됨
            // create 때 호출 하면 느려짐 .. resumed 후에는 따로 notifyDataSetChanged 호출하도록
            //if (view.adapter != null) {
            //    view.adapter!!.notifyDataSetChanged()
            //}

            Log.i("BaseAdapter", "setItems ")
        }
    }

    @BindingAdapter("notifyChange") // resume pause 값을 받고있는데.. 수정이 필요할듯 지금은 아이디어가 생각이 안남..
    fun notifyChange(view: RecyclerView, notifyChange: LiveData<Int>) {
        if (notifyChange.value != null) {
//            commonBindingComponent.invokeHtmlView(notifyChange.value!!)
            if (notifyChange.value == 1) {
                view.adapter?.notifyDataSetChanged()
                Log.i("BaseAdapter", "notifyChange ")
            }
        }
    }

    @BindingAdapter("notifyItemInserted")
    fun notifyItemInserted(view: RecyclerView, itemIndex: LiveData<Int>?) {
        if (itemIndex != null && itemIndex.value != null && itemIndex.value!! >= 0) {
            view.layoutManager!!.scrollToPosition(itemIndex.value!!)

            val startIndex = itemIndex.value!! - 1
            if (!view.itemAnimator!!.isRunning) {
                view.adapter!!.notifyItemRangeChanged(startIndex, itemIndex.value!!)
            } else {
                view.adapter!!.notifyDataSetChanged()
            }

            Log.i("BaseAdapter", "notifyItemInserted $startIndex," + itemIndex.value!!)
        }
    }

    @BindingAdapter("notifyItemRangeInserted")
    fun notifyItemRangeInserted(view: RecyclerView, itemCount: LiveData<Int>?) {
        if (itemCount != null && itemCount.value != null && itemCount.value!! >= 0) {
//            if ((view.getSafeContext() as ViewModelActivity).viewModel is ChatModel) {
//                view.layoutManager!!.scrollToPosition(view.adapter!!.itemCount - 1)
//            }

            val endIndex = view.adapter!!.itemCount - 1
            val startIndex = endIndex - itemCount.value!!
            if (!view.itemAnimator!!.isRunning) {
                view.adapter!!.notifyItemRangeChanged(startIndex, endIndex)
            } else {
                view.adapter!!.notifyDataSetChanged()
            }

            Log.i("BaseAdapter", "notifyItemRangeInserted $startIndex, $endIndex")
        }
    }

    @BindingAdapter("notifyItemRangeChanged")
    fun notifyItemRangeChanged(view: RecyclerView, query: LiveData<String>?) {
        if (query != null && query.value != null) {
            val startIndex = query.value!!.split("~")[0]
            val endIndex = query.value!!.split("~")[1]
            if (!view.itemAnimator!!.isRunning) {
                view.adapter!!.notifyItemRangeChanged(startIndex.toInt(), endIndex.toInt())
            } else {
                view.adapter!!.notifyDataSetChanged()
            }

            Log.i("BaseAdapter", "notifyItemRangeChanged $startIndex, $endIndex")
        }
    }

    @BindingAdapter("notifyItemChange") // resume pause 값을 받고있는데.. 수정이 필요할듯 지금은 아이디어가 생각이 안남..
    fun notifyItemChange(view: RecyclerView, itemIndex: LiveData<Int>?) {
        if (itemIndex != null && itemIndex.value != null && itemIndex.value!! >= 0) {
            view.adapter!!.notifyItemChanged(itemIndex.value!!)
            Log.i("BaseAdapter", "notifyItemChange " + itemIndex.value!!)
        }
    }

    @BindingAdapter("scrollPosition")
    fun scrollPosition(view: RecyclerView, position: LiveData<Int>?) {
        if (position != null && position.value != null && position.value!! >= 0) {
            try {
//                if ((view.getSafeContext() as ViewModelActivity).viewModel is ChatModel) {
//                    (view.layoutManager!! as LinearLayoutManager).scrollToPositionWithOffset(position.value!!, -9999)
//                } else {
                    view.smoothScrollToPosition(position.value!!)
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Log.i("BaseAdapter", "scrollPosition " + position.value!!)
        }
    }

    @BindingAdapter("longClick")
    fun setLongClick(view: View, item: T) {
        view.setOnLongClickListener { v ->
            getListModel().onLongClick(view, item)
            false
        }
    }

}
