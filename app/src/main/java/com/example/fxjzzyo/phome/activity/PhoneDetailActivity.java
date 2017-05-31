package com.example.fxjzzyo.phome.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.adapter.MyPagerAdapter;
import com.example.fxjzzyo.phome.beanDao.PhoneDao;
import com.example.fxjzzyo.phome.beanDao.UserDao;
import com.example.fxjzzyo.phome.fragment.CommentFragment;
import com.example.fxjzzyo.phome.fragment.GeneralFragment;
import com.example.fxjzzyo.phome.fragment.ParamaterFragment;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.Phone;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.kekstudio.dachshundtablayout.indicators.DachshundIndicator;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class PhoneDetailActivity extends AppCompatActivity {



    private ViewPager viewPager;
    private DachshundTabLayout tabLayout;
    private MyPagerAdapter myPagerAdapter;
    private List<Fragment> mFragmentList;
    private int phoneId;
    private Phone phone;
    private ImageView ivLove,ivComment;
    private TextView tvLoveCount,tvCommentCount;
    private boolean isSelected;//是否选择喜欢按钮
    private String type;//查询的类型，love,unlove,check
    private BroadcastReceiver mBroadcastReceiver;

    public static PhoneDetailActivity phoneDetailActivityInstance;
    private CustomProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        initView();
        initData();
        initEvent();



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }

        return true;
    }
    private void initEvent() {
        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fragmetn跳到第三个
                viewPager.setCurrentItem(2);
            }
        });
        ivLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.user==null)
                {
                    MyUtil.toastMessage(PhoneDetailActivity.this,"还没有登录！");
                    return;
                }
                if(Global.isNetAvailable)
                {
                    if(isSelected)
                    {
                        //设置不喜欢
                        //上传到服务器
                        getInfoFromNet("unlove");

                    }else
                    {
                        //设置喜欢
                        getInfoFromNet("love");
                    }
                }else {
                    MyUtil.toastMessage(PhoneDetailActivity.this,"网络不可用！");
                }

            }
        });
        //注册广播
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String str = intent.getStringExtra("broad_key");
                if(str.equals("comment_success"))//评论数加一
                {
                   int comCount = phone.getPhoneCommentCount();
                    comCount++;
                    tvCommentCount.setText(comCount+"");
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("update_data");
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void initData() {
        this.phoneDetailActivityInstance = this;
        //如果已经登录，则判断是否已经喜欢该手机
        if(Global.user!=null)
        {
            getInfoFromNet("check");

        }
        //获取传过来的phone
       Bundle bundle =  getIntent().getBundleExtra("phone");
       phone = (Phone) bundle.get("phone");
        phoneId = phone.getPhoneId();
        //设置标题为手机名称
setTitle(phone.getPhoneName());
        //初始化喜爱数和评论数
        tvCommentCount.setText(phone.getPhoneCommentCount()+"");
        tvLoveCount.setText(phone.getPhoneLoveCount()+"");
        //初始化fragment
        mFragmentList = new ArrayList<>();
        GeneralFragment generalFragment = GeneralFragment.newInstance(phone);
        ParamaterFragment paramaterFragment = ParamaterFragment.newInstance(""+phoneId);
        CommentFragment commentFragment = CommentFragment.newInstance(""+phoneId);

        mFragmentList.add(generalFragment);
        mFragmentList.add(paramaterFragment);
        mFragmentList.add(commentFragment);
        //初始化adapter
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),mFragmentList);
       //设置adapter
        viewPager.setAdapter(myPagerAdapter);
viewPager.setOffscreenPageLimit(2);//缓存2页
        //设置tablayout
        tabLayout.setupWithViewPager(viewPager);
        //设置指示器
      tabLayout.setAnimatedIndicator(new DachshundIndicator(tabLayout));

    }
    public void getInfoFromNet(String parameter)
    {

        if(Global.isNetAvailable)
        {
            //设置进度框
            progressDialog.show();
            new LoveThread(parameter).start();
        }else {
            MyUtil.toastMessage(PhoneDetailActivity.this,"网络不可用！");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * 用于网络请求数据的子线程
     */
    class LoveThread extends Thread {
        private String type;
        public LoveThread(String type) {
            this.type = type;

        }

        @Override
        public void run() {
            UserDao userDao = new UserDao();
            String result = userDao.isLove(Global.isLoveUrl,Global.user.getUserId()+"",phoneId+"",type);
           if(result.equals("add_success"))
           {
               Message msg = new Message();
               msg.what = ADD_SUCCESS;

               mHandler.sendMessage(msg);
           }else if(result.equals("add_failed"))
           {
               Message msg = new Message();
               msg.what = ADD_FAILED;

               mHandler.sendMessage(msg);
           }else if(result.equals("delete_success"))
           {
               Message msg = new Message();
               msg.what = DELETE_SUCCESS;

               mHandler.sendMessage(msg);
           }else if(result.equals("delete_failed"))
           {Message msg = new Message();
               msg.what = DELETE_FAILED;

               mHandler.sendMessage(msg);

           }else if(result.equals("love"))
           {Message msg = new Message();
               msg.what = LOVE;

               mHandler.sendMessage(msg);

           }else if(result.equals("unlove")) {
               Message msg = new Message();
               msg.what = UNLOVE;

               mHandler.sendMessage(msg);
           }else {
               Message msg = new Message();
               msg.what = FAILED;

               mHandler.sendMessage(msg);
           }



        }
    }
    private static final int ADD_SUCCESS = 1;
    private static final int ADD_FAILED = 2;
    private static final int DELETE_FAILED = 3;
    private static final int DELETE_SUCCESS= 4;
    private static final int LOVE = 5;
    private static final int UNLOVE= 6;
    private static final int FAILED= 7;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_SUCCESS:
                    MyUtil.toastMessage(PhoneDetailActivity.this, "喜欢了一个手机！");
                    tvLoveCount.setText(phone.getPhoneLoveCount()+1+"");
                   ivLove.setSelected(true);
                    isSelected = true;
                    //发送广播，重新请求数据
                    Intent intent = new Intent();

                    intent.putExtra("broad_key", "love_success");
                    intent.setAction("update_data");
                    sendBroadcast(intent);
                    break;

                case ADD_FAILED:
                    MyUtil.toastMessage(PhoneDetailActivity.this, "网络不太好哦！");
                    break;
                case DELETE_SUCCESS:
                    MyUtil.toastMessage(PhoneDetailActivity.this, "取消喜欢！");
                    tvLoveCount.setText(phone.getPhoneLoveCount()-1+"");
                    ivLove.setSelected(false);
                    isSelected = false;
                    //发送广播，重新请求数据
                    Intent intent2 = new Intent();
                    intent2.putExtra("broad_key", "unlove_success");
                    intent2.setAction("update_data");
                    sendBroadcast(intent2);
                    break;
                case DELETE_FAILED:
                    MyUtil.toastMessage(PhoneDetailActivity.this, "网络不太好哦！");
                    break;
                case LOVE://已经喜欢了该手机

                    ivLove.setSelected(true);
                    isSelected = true;
                    break;
                case UNLOVE://还未喜欢了该手机
                    ivLove.setSelected(false);
                    isSelected = false;
                    break;
                case FAILED://失败
                    break;
            }
            //设置进度框
            if(progressDialog.isShowing())
            {
            progressDialog.dismiss();
            }
        }


    };
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (DachshundTabLayout) findViewById(R.id.tab_layout);
        ivComment = (ImageView) findViewById(R.id.iv_comment);
        ivLove = (ImageView) findViewById(R.id.iv_love);
        tvLoveCount = (TextView) findViewById(R.id.tv_love_count);
        tvCommentCount = (TextView) findViewById(R.id.tv_comment_count);
        progressDialog = CustomProgressDialog.newInstance(this, "加载中...", false, null);
    }
}
