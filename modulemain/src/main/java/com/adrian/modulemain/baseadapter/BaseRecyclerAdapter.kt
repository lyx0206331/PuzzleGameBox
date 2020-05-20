package com.adrian.modulemain.baseadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 * author:RanQing
 * date:2019/6/8 0008 3:34
 * description: RecyclerView适配器中，直接用布局文件的Id来使用view，无需调用findViewById.请参考RecyclerViewActivity.kt
 **/

open class BaseRecyclerAdapter<M>(
    @LayoutRes val itemLayoutId: Int, list: Collection<M>? = null,
    bind: (BaseRecyclerAdapter<M>.() -> Unit)? = null
) : RecyclerView.Adapter<BaseRecyclerAdapter.CommonViewHolder>() {

    init {
//        if (bind != null) {
//            apply { bind }
//        }
//        bind?.apply { this }
    }

    private var dataList = mutableListOf<M>()

    var onItemClickListener: ((v: View, position: Int) -> Unit)? = null
    var onItemLongClickListener: ((v: View, position: Int) -> Boolean) = { _, _ -> false }

    var onBindViewHolder: ((holder: CommonViewHolder, position: Int) -> Unit)? = null

    /**
     * 设置新数据，会清空旧数据
     */
    fun setData(list: Collection<M>): Boolean {
        var result: Boolean
        dataList.clear()
        list.apply {
            result = dataList.addAll(this)
        }
        return result
    }

    /**
     * 根据索引获取一条数据
     */
    fun getItem(position: Int) = dataList[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
        val viewHolder = CommonViewHolder(itemView)
        itemView.setOnClickListener {
            onItemClickListener?.invoke(it, viewHolder.adapterPosition)
        }
        itemView.setOnLongClickListener {
            return@setOnLongClickListener onItemLongClickListener.invoke(it, viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        if (onBindViewHolder != null) {
            onBindViewHolder!!.invoke(holder, position)
        } else {
            bindData(holder, position)
        }
    }

    open fun bindData(holder: CommonViewHolder, position: Int) {}

    class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View = itemView
    }
}