package com.example.fxjzzyo.phome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.beanDao.UserDao;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.User;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.utils.SharedPreferenceUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;
import com.tuyenmonkey.mkloader.MKLoader;

import static com.example.fxjzzyo.phome.javabean.Global.user;


public class LoginActivity extends AppCompatActivity {
    private static final int SUCCESS = 1;
    private static final int PASS_ERROR = 0;
    private static final int UN_REGISTER = 2;
    private static final int FAILED = 3;
    private EditText etName, etPassword;
    private CheckBox cbRemember, cbAutoLogin;
    private CustomProgressDialog progressDialog;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                  Bundle bundle =   msg.getData();
                    MyUtil.toastMessage(LoginActivity.this, "登录成功！");
                    user  = new User();
                    user.setUserId(Integer.parseInt((String) bundle.get("userId")));
                    user.setUserName((String) bundle.get("userName"));
                    user.setUserPassword((String) bundle.get("userPass"));
                    //跳到主页
                    jumpToActivity(MainActivity.class);
                    LoginActivity.this.finish();

                    break;
                case PASS_ERROR:
                    MyUtil.toastMessage(LoginActivity.this, "密码错误！");
                    break;
                case UN_REGISTER:
                    MyUtil.toastMessage(LoginActivity.this, "用户未注册！");
                    break;
                case FAILED:
                    MyUtil.toastMessage(LoginActivity.this, "登录失败！");
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
        setContentView(R.layout.activity_login);

        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)//被选中
                {
                    SharedPreferenceUtil.saveData(LoginActivity.this, SharedPreferenceUtil.IS_AUTO_LOGIN, "isAuto");
                    //必须同时记住密码
                    cbRemember.setChecked(true);

                }else {
                    SharedPreferenceUtil.saveData(LoginActivity.this, SharedPreferenceUtil.IS_AUTO_LOGIN, null);
                }
            }
        });
    }

    private void initData() {
        progressDialog = CustomProgressDialog.newInstance(this, "登录中...", false, null);
        //初始化登录框
        initUserEditText();
        //判断是否自动登录
        String isAuto = SharedPreferenceUtil.getData(this,SharedPreferenceUtil.IS_AUTO_LOGIN);
        if(isAuto!=null)
        {
            cbAutoLogin.setChecked(true);
            //调用登录按钮，随便传个view
            login(cbAutoLogin);
        }else
        {
            cbAutoLogin.setChecked(false);
        }
    }

    /**
     * 根据是否记住密码，初始化登录框
     */
    private void initUserEditText() {
        String name = SharedPreferenceUtil.getData(this, SharedPreferenceUtil.KEY_NAME);
        String password = SharedPreferenceUtil.getData(this, SharedPreferenceUtil.KEY_PASSWORD);
        if (name != null) {
            etName.setText(name);
            etName.setSelection(name.length());
            cbRemember.setChecked(true);
        }else {
            cbRemember.setChecked(false);
        }

        if (password != null) {
            etPassword.setText(password);
            etPassword.setSelection(password.length());
        }

    }

    private void initView() {
        setTitle("登录");
        etName = (EditText) findViewById(R.id.et_name);
        etPassword = (EditText) findViewById(R.id.et_password);
        cbAutoLogin = (CheckBox) findViewById(R.id.cb_auto_login);
        cbRemember = (CheckBox) findViewById(R.id.cb_remember_password);
    }

    /**
     * 点击 登录 按钮
     *
     * @param view
     */
    public void login(View view) {
        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (check(name, password)) {

            if(Global.isNetAvailable)
            {
                progressDialog = CustomProgressDialog.newInstance(this, "登录中...", false, null);
                progressDialog.show();


                new LoginThread(name, password).start();
            }else {
                Toast.makeText(this,"网络不可用！",Toast.LENGTH_SHORT).show();
            }

        }

    }



    /**
     * 是否记住用户名，密码
     *
     * @param name
     * @param password
     */
    private void saveUserName(String name, String password) {
        if (cbRemember.isChecked()) {
            SharedPreferenceUtil.saveData(this, SharedPreferenceUtil.KEY_NAME, name);
            SharedPreferenceUtil.saveData(this, SharedPreferenceUtil.KEY_PASSWORD, password);
        } else {
            SharedPreferenceUtil.saveData(this, SharedPreferenceUtil.KEY_NAME, null);
            SharedPreferenceUtil.saveData(this, SharedPreferenceUtil.KEY_PASSWORD, null);
        }

    }

    /**
     * 点击 还没有账号
     *
     * @param view
     */
    public void jumpToRegister(View view) {
        jumpToActivity(RegiserActivity.class);
    }

    /**
     * 点击 随便看看 按钮
     *
     * @param view
     */
    public void scan(View view) {

        jumpToActivity(MainActivity.class);
        LoginActivity.this.finish();

    }

    /**
     * 跳转到某个activity
     * @param cla
     */
    private void jumpToActivity(Class<?> cla) {
        Intent intent = new Intent(LoginActivity.this, cla);
        startActivity(intent);

    }

    /**
     * 检查输入的信息是否合法
     *
     * @param name
     * @param password
     * @return
     */
    private boolean check(String name, String password) {

        if (name.isEmpty()) {
            MyUtil.toastMessage(this, "用户名不能为空！");
            return false;
        }
        if (password.isEmpty()) {
            MyUtil.toastMessage(this, "密码不能为空！");
            return false;
        }else
        {
            if(password.length()!=6)
            {
                MyUtil.toastMessage(this, "请输入6位密码！");
                return false;
            }
        }

        return true;

    }

    /**
     * 用于登录的子线程
     */
    class LoginThread extends Thread {
        private String name, password;

        public LoginThread(String name, String password) {
            this.name = name;
            this.password = password;
        }

        @Override
        public void run() {

            UserDao userDao = new UserDao();
            String result = userDao.userLogin(Global.loginUrl, name, password);
            if(result.equals("pass_error"))//密码错误
            {
                Message message = new Message();
                message.what = PASS_ERROR;
                mHandler.sendMessage(message);
            }else if (result.equals("unregister"))
            {
                Message message = new Message();
                message.what = UN_REGISTER;
                mHandler.sendMessage(message);
            }else if(result.equals("failed"))
            {
                Message message = new Message();
                message.what = FAILED;
                mHandler.sendMessage(message);
            }else//登录成功,返回的result为用户id
            {
                try {
                    Integer.parseInt(result);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    //不能转化为integer类型，说明得到的不是id，而是由于联网不是自己的电脑开的网造成的错误。
                    Message message = new Message();
                    message.what = FAILED;
                    mHandler.sendMessage(message);
                    return;
                }
                //存储用户名密码
                saveUserName(name, password);

                Message message = new Message();
                message.what = SUCCESS;
                Bundle bundle = new Bundle();
                bundle.putString("userId",result);
                bundle.putString("userName",name);
                bundle.putString("userPass",password);
                message.setData(bundle);
                mHandler.sendMessage(message);

            }
        }
    }
}
