package com.samsia.roleplay.viewmodel

import android.content.Context
import com.samsia.roleplay.R
import com.samsia.roleplay.data.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class TodoViewModel : ListModel<Todo>() {
    private lateinit var todoDao: TodoDao
    val editText = EditTextViewModel()

    override fun init() {
        val actionIcon = EditTextViewModel.TabIcon()
        actionIcon.iconRes = R.drawable.icon_plus
        actionIcon.action = object: EditTextViewModel.TabIconListener {
            override fun onTab(text: String) {
                val currentTime: Date = Calendar.getInstance().time
                val todo = Todo(null, text, "할일", currentTime.time.toString())
                addDisposable(todoDao.insertTodo(todo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        addItem(todo)
                    })
            }
        }
        editText.actionIcon = actionIcon

        addDisposable(todoDao.getTodoList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                setItemList(ArrayList(list))
                notifyChange()
            })
    }

    override fun getItemType(position: Int): Int {
        return R.layout.todo_item
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun onStop() {
    }

    override fun initDatabase(context: Context) {
        todoDao = AppDatabase.getInstance(context).todoDao()
    }

    fun delete(data: Todo) {
        addDisposable(todoDao.deleteTodo(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                removeItem(data)
            })
    }
}
