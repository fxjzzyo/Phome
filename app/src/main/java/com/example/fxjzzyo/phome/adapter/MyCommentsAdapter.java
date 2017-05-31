package com.example.fxjzzyo.phome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.javabean.Comment;
import com.example.fxjzzyo.phome.javabean.Global;
import com.loopj.android.image.SmartImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class MyCommentsAdapter extends BaseAdapter {
private List<Comment> comments;
        private Context context;
    private LayoutInflater layoutInflater;


    public MyCommentsAdapter(Context context,List<Comment> comments){
        this.comments =comments;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }
    public void setDataChanged(List<Comment> comments){
        this.comments = comments;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       ViewHolder viewHolder;
        if(view==null)
        {
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.comment_list_item_layout, viewGroup, false);
            viewHolder.ivHead = (SmartImageView) view.findViewById(R.id.iv_head);
            viewHolder.ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
            viewHolder.tvName = (TextView) view.findViewById(R.id.tv_user_name);
            viewHolder.tvTime = (TextView) view.findViewById(R.id.tv_comment_time);
            viewHolder.tvComment = (TextView) view.findViewById(R.id.tv_comment);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Comment com = comments.get(i);
        viewHolder.ivHead.setImageUrl(Global.phonePicUrl+"?picName="+com.getUserId()+"&type=userHead",R.drawable.me_unselected);
        viewHolder.tvName.setText(com.getUserName());
        viewHolder.tvTime.setText(com.getCommentTime());
        viewHolder.ratingBar.setRating(Float.valueOf(com.getRate()));
        viewHolder.tvComment.setText(com.getComment());

        return view;
    }

    class ViewHolder{
        SmartImageView ivHead;
        TextView tvName;
        RatingBar ratingBar;
        TextView tvTime;
        TextView tvComment;


    }
}
