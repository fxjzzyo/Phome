package com.example.fxjzzyo.phome.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.activity.CommentActivity;
import com.example.fxjzzyo.phome.activity.PhoneDetailActivity;
import com.example.fxjzzyo.phome.adapter.MyCommentsAdapter;
import com.example.fxjzzyo.phome.beanDao.PhoneDao;
import com.example.fxjzzyo.phome.javabean.Comment;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.Phone;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String phoneId;
    private ListView lvComment;
    private List<Comment> comments;
    private MyCommentsAdapter myCommentsAdapter;
    private CustomProgressDialog progressDialog;
    private BroadcastReceiver mBroadcastReceiver;

    public CommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(String param1) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phoneId = getArguments().getString(ARG_PARAM1);
        }
        progressDialog = CustomProgressDialog.newInstance(getActivity(), "加载中...", false, null);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        initEvent();
    }

    private void initEvent() {

        lvComment.setAdapter(myCommentsAdapter);
        //注册广播
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("receive broad commentfragment");
                String str = intent.getStringExtra("broad_key");
                if(str.equals("comment_success"))
                {
                    //从网络请求最新数据
                    getDataFromNet(phoneId);
                }


            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("update_data");
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void initData() {

        comments = new ArrayList<>();
        myCommentsAdapter = new MyCommentsAdapter(getActivity(), comments);
        //根据phoneId从网络获取评论数据
        getDataFromNet(phoneId);
    }

    private void getDataFromNet(String phoneId) {
        if (Global.isNetAvailable) {
            //设置进度框
           progressDialog.show();
            new GetCommentThread(phoneId).start();
        } else {
            Toast.makeText(CommentFragment.this.getActivity(), "网络不可用！", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * 用于网络请求数据的子线程
     */
    class GetCommentThread extends Thread {
        private String phoneId;

        public GetCommentThread(String phoneId) {
            this.phoneId = phoneId;
        }

        @Override
        public void run() {
            PhoneDao mPhoneDao = new PhoneDao();
            String result = mPhoneDao.getCommentById(Global.getCommentUrl, phoneId);
            if (result != null)//获取数据成功
            {
                Message msg = new Message();
                msg.what = SUCCESS;
                Bundle bundle = new Bundle();
                bundle.putString("comment", result);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what = FAILED;
                mHandler.sendMessage(msg);
            }
        }
    }

    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    MyUtil.toastMessage(CommentFragment.this.getActivity(), "获取数据成功！");
                    //获取json字符串
                    Bundle bundle = msg.getData();
                    String jsonComment = (String) bundle.get("comment");
                    //解析json数据
                    parseJson(jsonComment);
                    //更新List

                    myCommentsAdapter.setDataChanged(comments);

                    break;

                case FAILED:
                    MyUtil.toastMessage(CommentFragment.this.getActivity(), "获取数据失败！");
                    //TODO
                    //下拉刷新，重新加载
                    break;
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

            }

        }

        //解析json数据
        private void parseJson(String jsonComment) {
            List<Comment> comment = JSON.parseArray(jsonComment, Comment.class);
            if (comment != null) {
                comments = comment;
            }
        }
    };

    private void initView() {
        View view = getView();
        lvComment = (ListView) view.findViewById(R.id.lv_comment);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击后调到书写评论页面
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra("phoneId", phoneId);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消注册广播
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
}
