package com.example.fxjzzyo.phome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.beanDao.UserDao;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;
import com.tuyenmonkey.mkloader.MKLoader;


public class RegiserActivity extends AppCompatActivity {
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private static final int EXIST = 2;
    private EditText etName, etPassword, etPasswordComfirm;
    private CustomProgressDialog progressDialog;
private Handler mHandler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
       switch (msg.what)
       {
           case SUCCESS:
               MyUtil.toastMessage(RegiserActivity.this,"注册成功！");
               //跳到登录页

               RegiserActivity.this.finish();
               break;
           case FAILED:
               MyUtil.toastMessage(RegiserActivity.this,"注册失败！");
               break;
           case EXIST:
               MyUtil.toastMessage(RegiserActivity.this,"用户名已存在！");
               break;
       }
        if(progressDialog.isShowing())
        {
             progressDialog.dismiss();
            progressDialog = null;
        }
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiser);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        initView();

    }

    private void initView() {
        setTitle("注册");
        etName = (EditText) findViewById(R.id.et_name);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPasswordComfirm = (EditText) findViewById(R.id.et_password_comfirm);
        progressDialog = CustomProgressDialog.newInstance(this, "注册中...", false, null);
    }

    /**点击注册按钮
     * @param view
     */
    public void register(View view) {
        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordConfirm = etPasswordComfirm.getText().toString().trim();

       if(check(name, password, passwordConfirm))
       {
           submitToServer(name,password);
       }

    }
    public void submitToServer(String name,String password){
        if(Global.isNetAvailable)
        {
            //设置进度框
            progressDialog = CustomProgressDialog.newInstance(this, "注册中...", false, null);
            progressDialog.show();
            //上传到服务器
            new RegisterThread(name,password).start();
        }else {
            Toast.makeText(this,"网络不可用！",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 用于注册的子线程
     */
    class RegisterThread extends Thread{
        private String name,password;
        public RegisterThread(String name,String password){
            this.name = name;
            this.password = password;
        }
        @Override
        public void run() {
            UserDao userDao = new UserDao();
            String result = userDao.register(Global.registerUrl,name,password);
            if(result.equals("success"))//注册成功
            {
                Message message = new Message();
                message.what = SUCCESS;
                mHandler.sendMessage(message);

            }else if(result.equals("exist"))//注册失败
            {
                Message message = new Message();
                message.what = EXIST;
                mHandler.sendMessage(message);
            }else if(result.equals("failed"))
            {
                Message message = new Message();
                message.what = FAILED;

                mHandler.sendMessage(message);
            }
        }
    }

    /**
     * 检查输入的信息是否合法
     * @param name
     * @param password
     * @param passwordConfirm
     * @return
     */
    private boolean check(String name, String password, String passwordConfirm) {

        if (name.isEmpty()) {
            MyUtil.toastMessage(this, "用户名不能为空！");
            return false;
        }
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
