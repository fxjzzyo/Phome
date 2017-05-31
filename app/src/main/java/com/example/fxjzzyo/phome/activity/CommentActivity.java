package com.example.fxjzzyo.phome.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.beanDao.PhoneDao;
import com.example.fxjzzyo.phome.beanDao.UserDao;
import com.example.fxjzzyo.phome.fragment.ParamaterFragment;
import com.example.fxjzzyo.phome.javabean.Comment;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.Phone;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;
import com.tuyenmonkey.mkloader.MKLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity {
    private Button btnSubmit;
    private RatingBar ratingBar;
    private EditText etCommnet;
    private static final int MAX_COMMENT_LENGTH = 250;

    private String phoneId;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        phoneId = getIntent().getStringExtra("phoneId");
    }

    private void initEvent() {


        //点击提交按钮
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.user == null) {
                    MyUtil.toastMessage(CommentActivity.this, "请先登录！");
                    Intent intent = new Intent(CommentActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                String commnetContent = etCommnet.getText().toString().trim();
                if (commnetContent.length() > MAX_COMMENT_LENGTH) {
                    MyUtil.toastMessage(CommentActivity.this, "评论字数超出最大限制！");
                    return;
                }
                String rate = String.valueOf(ratingBar.getRating());
                String time = getCurrentTime();
                Comment comment = new Comment();
                comment.setComment(commnetContent);

                comment.setPhoneId(Integer.parseInt(phoneId));
                comment.setUserId(Global.user.getUserId());
                comment.setUserName(Global.user.getUserName());
                comment.setRate(rate);
                comment.setCommentTime(time);
                if (Global.isNetAvailable) {
                    //提交到服务器
                    submitToServer(comment);
                } else {
                    Toast.makeText(CommentActivity.this, "网络不可用！", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = new Date(System.currentTimeMillis());

        return simpleDateFormat.format(date);
    }

    private void submitToServer(Comment comment) {
        if (Global.isNetAvailable) {
            //设置进度框
            progressDialog.show();
            new SubmitCommentThread(comment).start();
        } else {
            Toast.makeText(this, "网络不可用！", Toast.LENGTH_SHORT).show();
        }

    }



    /**
     * 用于网络请求数据的子线程
     */
    class SubmitCommentThread extends Thread {
        private Comment comment;

        public SubmitCommentThread(Comment comment) {
            this.comment = comment;
        }

        @Override
        public void run() {


            UserDao userDao = new UserDao();

            String result = userDao.submitCommentToServer(Global.submitCommentUrl, comment);
            if (result.equals("add_success"))//获取数据成功
            {
                Message msg = new Message();
                msg.what = SUCCESS;

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
                    MyUtil.toastMessage(CommentActivity.this, "提交成功！");


                    //发送广播，通知有了新评论
                    Intent intent2 = new Intent();
                    intent2.putExtra("broad_key", "comment_success");
                    intent2.setAction("update_data");
                    sendBroadcast(intent2);
                    break;

                case FAILED:
                    MyUtil.toastMessage(CommentActivity.this, "提交失败！");

                    break;
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }


    };

    private void initView() {
        setTitle("写评论");
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        etCommnet = (EditText) findViewById(R.id.et_comment);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        progressDialog = CustomProgressDialog.newInstance(this, "上传中...", false, null);
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
}
