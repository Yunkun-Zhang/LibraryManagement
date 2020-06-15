package com.example.librarymanagement.adapter;

import android.content.Context;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.librarymanagement.R;

import java.util.List;

public class FriendList extends RecyclerView.Adapter<FriendList.MyViewHolder>{
    private Context context;
    private List< String > list;
    private List<Integer> imagelist;
    private View inflater;
    //构造方法，传入数据
    public FriendList (Context context, List<String> list, List<Integer> imagelist){
        this.context = context;
        this.list = list;
        this.imagelist = imagelist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.friend_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //将数据和控件绑定
        holder.textView.setText(list.get(position));
        holder.portrait.setImageResource(imagelist.get(position));
    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return list.size();
    }

    //内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView portrait;
        public MyViewHolder (View itemView) {
            super(itemView);
            textView = (TextView) itemView . findViewById (R.id.name);
            portrait = (ImageView) itemView . findViewById(R.id.portrait);
        }
    }
}