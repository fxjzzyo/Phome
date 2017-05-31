package com.example.fxjzzyo.phome.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.beanDao.PhoneDao;
import com.example.fxjzzyo.phome.beanDao.UserDao;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.Phone;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class MotifyPassActivity extends AppCompatActivity {
private EditText etOldPass,etNewPass,etNewPassConfirm;
//private MKLoader mkLoader;
    private CustomProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motify_pass);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        etOldPass = (EditText) findViewById(R.id.et_old_pass);
        etNewPass = (EditText) findViewById(R.id.et_new_pass);
        etNewPassConfirm = (EditText) findViewById(R.id.et_new_pass_confirm);
        setTitle("修改密码");
    }


    public void ok(View view)
    {
        String oldPass = Global.user.getUserPassword();
        String oPass = etOldPass.getText().toString().trim();
        if(!oldPass.equals(oPass))
        {
            MyUtil.toastMessage(this,"旧密码有误！");
            return;
        }
        //
        String nPass = etNewPass.getText().toString().trim();
        String nPassCon = etNewPassConfirm.getText().toString().trim();
        if(check(nPass,nPassCon))
        {
            //更改后的密码提交服务器
            submitToServer(nPass);
        }



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
    public void submitToServer(String str){
        if(Global.isNetAvailable)
        {
            //设置进度框
//            setLoader(0.7f);
            progressDialog = CustomProgressDialog.show(this, "上传中...", false, null);
            //上传到服务器
            new MotifyUser(str).start();
        }else {
            Toast.makeText(this,"网络不可用！",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 检查输入的信息是否合法
     * @param password
     * @param passwordConfirm
     * @return
     */
    private boolean check(String password, String passwordConfirm) {


        if (password.isEmpty()) {
            MyUtil.toastMessage(this, "密码不能为空！");
            return false;
        }
        if (passwordConfirm.isEmpty()) {
            MyUtil.toastMessage(this, "请确认密码！");
            return false;
        } else {
            if(password.length()!=6)
            {
                MyUtil.toastMessage(this, "请输入6位密码！");
                return false;
            }
            if (!password.equals(passwordConfirm)) {
                MyUtil.toastMessage(this, "密码不一致！");
                return false;
            }
        }
        return true;

    }
    /**
     * 用于网络请求数据的子线程
     */
    class MotifyUser extends Thread {
private String value;

        public MotifyUser(String value) {
            this.value = value;
        }

        @Override
        public void run() {
            UserDao userDao  = new UserDao();
            PhoneDao mPhoneDao = new PhoneDao();
            String result = userDao.motifyUser(Global.motifyUserUrl,Global.user.getUserId()+"",value,"password");
            if(result.equals("success"))//获取数据成功
            {
                Message msg = new Message();
                msg.what = SUCCESS;
                mHandler.sendMessage(msg);
            }else {
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
                    MyUtil.toastMessage(MotifyPassActivity.this ,"修改成功！");
                    MotifyPassActivity.this.finish();
                    break;

                case FAILED:
                    MyUtil.toastMessage(MotifyPassActivity.this, "修改失败！");
                    break;
            }
//            resetLoader(1f);
            progressDialog.dismiss();
        }


    };
}
