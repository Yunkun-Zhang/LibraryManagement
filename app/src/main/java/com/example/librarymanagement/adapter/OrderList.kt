package com.example.librarymanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderList : RecyclerView.Adapter<OrderList.MyHolder> {
    private var list: ArrayList<String>? = null
    private var status: ArrayList<String>? = null
    private var context: Context? = null
    private var itemClickListener: IKotlinItemClickListener? = null

    constructor (mContext: Context, list: ArrayList<String>?, status: ArrayList<String>?) {
        this.context = mContext
        this.list = list
        this.status = status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.order_list, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int = list?.size!!

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        holder?.text?.text = list!![position]
        holder?.status?.text = status!![position]

        // 点击事件
        holder.itemView.setOnClickListener {
            itemClickListener!!.onItemClickListener(position)
        }

    }

    class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        // !! 断言
        var text: TextView = itemView!!.findViewById(R.id.date)
        var status: TextView = itemView!!.findViewById(R.id.status)
    }

    // 提供set方法
    fun setOnKotlinItemClickListener(itemClickListener: IKotlinItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    //自定义接口
    interface IKotlinItemClickListener {
        fun onItemClickListener(position: Int)
    }

}
