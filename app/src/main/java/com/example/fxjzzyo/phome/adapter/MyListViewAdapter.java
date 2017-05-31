package com.example.fxjzzyo.phome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.Phone;
import com.loopj.android.image.SmartImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class MyListViewAdapter extends BaseAdapter {
    private List<Phone> phoneList;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public MyListViewAdapter(Context context, List<Phone> phoneList) {
        this.context = context;
        this.phoneList = phoneList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setDataChanged(List<Phone> phoneList) {
        this.phoneList = phoneList;
//        this.notifyDataSetInvalidated();
        this.notifyDataSetChanged();
    }
    public void addDataChanged(List<Phone> phoneList) {
        this.phoneList.addAll(phoneList);
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {

        return phoneList.size();
    }

    @Override
    public Object getItem(int i) {
        return phoneList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mViewHolder;
        if (view == null) {
            mViewHolder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.list_item, viewGroup, false);
            mViewHolder.ivPic = (SmartImageView) view.findViewById(R.id.iv_pic);
            mViewHolder.ivLove = (ImageView) view.findViewById(R.id.iv_love);
            mViewHolder.ivComment = (ImageView) view.findViewById(R.id.iv_comment);
            mViewHolder.tvPhoneName = (TextView) view.findViewById(R.id.tv_phone_name);
            mViewHolder.tvRate = (TextView) view.findViewById(R.id.tv_rate);
            mViewHolder.tvPrice = (TextView) view.findViewById(R.id.tv_price);
            mViewHolder.tvCommentCount = (TextView) view.findViewById(R.id.tv_comment_count);
            mViewHolder.tvLoveCount = (TextView) view.findViewById(R.id.tv_love_count);
            view.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        Phone mPhone = phoneList.get(i);
        System.out.println("i----->" + i);
        System.out.println("phoneid----->" + mPhone.getPhoneId());

        int commentCount = mPhone.getPhoneCommentCount();
        int loveCount = mPhone.getPhoneLoveCount();
//        mViewHolder.ivPic.setImageResource(R.drawable.phone1);
        System.out.println("picurl----->" + Global.phonePicUrl + "?picName=" + mPhone.getPhoneId() + "&type=phonePic");
        mViewHolder.ivPic.setImageUrl(Global.phonePicUrl + "?picName=" + mPhone.getPhoneId() + "&type=phonePic");

        mViewHolder.tvPhoneName.setText(mPhone.getPhoneName());
        mViewHolder.tvPrice.setText(mPhone.getPhonePrice() + "元");
        mViewHolder.tvRate.setText(mPhone.getPhoneRate()+"星");


        if (commentCount > 0) {
            mViewHolder.ivComment.setSelected(true);
            if (commentCount > 99) {
                mViewHolder.tvCommentCount.setText("99+");
            } else {
                mViewHolder.tvCommentCount.setText(commentCount + "\t");
            }


        } else//为0
        {
            mViewHolder.ivComment.setSelected(false);
            mViewHolder.tvCommentCount.setText("0\t");
        }

        if (loveCount > 0) {
            mViewHolder.ivLove.setSelected(true);
            if (loveCount > 99) {
                mViewHolder.tvLoveCount.setText("99+");
            } else {
                mViewHolder.tvLoveCount.setText(loveCount + "\t");
            }

        } else {
            mViewHolder.ivLove.setSelected(false);
            mViewHolder.tvLoveCount.setText("0\t");
        }
        return view;
    }

    class ViewHolder {
        private SmartImageView ivPic;
        private ImageView ivLove;
        private ImageView ivComment;
        private TextView tvPhoneName;
        private TextView tvRate;
        private TextView tvPrice;
        private TextView tvCommentCount;
        private TextView tvLoveCount;

    }
}
