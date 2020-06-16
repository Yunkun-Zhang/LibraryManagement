package com.example.librarymanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R

class FriendList : RecyclerView.Adapter<FriendList.MyHolder> {
    private var list: ArrayList<String>? = null
    private var imagelist: ArrayList<Int>? = null
    private var context: Context? = null
    private var itemClickListener: IKotlinItemClickListener? = null

    constructor (mContext: Context, list: ArrayList<String>?, imagelist: ArrayList<Int>?) {
        this.context = mContext
        this.list = list
        this.imagelist = imagelist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.friend_list, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int = list?.size!!

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder?.text?.text = list!![position]
        holder?.image?.setImageResource(imagelist!![position])

        // 点击事件
        holder.itemView.setOnClickListener {
            itemClickListener!!.onItemClickListener(position)
        }

    }

    class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        // !! 断言
        var text: TextView = itemView!!.findViewById(R.id.name)
        var image: ImageView = itemView!!.findViewById(R.id.portrait)
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
