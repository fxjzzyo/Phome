package com.example.fxjzzyo.phome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.beanDao.PhoneDao;
import com.example.fxjzzyo.phome.beanDao.UserDao;
import com.example.fxjzzyo.phome.fragment.CommentFragment;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.utils.Utils;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;
import com.tuyenmonkey.mkloader.MKLoader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.data;

public class MotifyMeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivHead;
    private EditText etNewName;
    private String newName;
    private CustomProgressDialog progressDialog;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motify_me);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        etNewName = (EditText) findViewById(R.id.et_new_name);
        ivHead = (ImageView) findViewById(R.id.iv_new_head);
        ivHead.setOnClickListener(this);
        progressDialog = CustomProgressDialog.newInstance(this, "上传中...", false, null);
        setTitle("修改资料");
    }

    public void ok(View view) {
        newName = etNewName.getText().toString().trim();
        if (newName.isEmpty()) {
            MyUtil.toastMessage(this, "用户名不能为空！");
            return;
        }
        //上传到服务器
        submitToServer();
    }

    public void submitToServer() {
        if (Global.isNetAvailable) {
            //设置进度框
            progressDialog = CustomProgressDialog.newInstance(this, "上传中...", false, null);
            progressDialog.show();
            //上传到服务器
            new MotifyUserThread(newName).start();
        } else {
            Toast.makeText(this, "网络不可用！", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * 用于网络请求数据的子线程
     */
    class MotifyUserThread extends Thread {
        private String value;

        public MotifyUserThread(String value) {
            this.value = value;
        }

        @Override
        public void run() {
            UserDao userDao = new UserDao();
            String result = userDao.motifyUser(Global.motifyUserUrl, Global.user.getUserId() + "", value, "userName");
            if (result.equals("success"))//获取数据成功
            {
                Message msg = new Message();
                msg.what = SUCCESS;
                mHandler.sendMessage(msg);
            } else if(result.equals("exist")){//用户名已存在
                Message msg = new Message();
                msg.what = EXIST;
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
    private static final int EXIST = 3;
    private static final int PIC_SUCCESS = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    MyUtil.toastMessage(MotifyMeActivity.this, "修改成功！");
                    Global.user.setUserName(newName);
                    Intent intent = new Intent();
                    intent.putExtra("result", "success");
                    intent.putExtra("newName", newName);
                    setResult(1,intent);
                    MotifyMeActivity.this.finish();
                    break;
                case PIC_SUCCESS:
                    MyUtil.toastMessage(MotifyMeActivity.this, "修改成功！");
                    Intent intent2 = new Intent();
                    intent2.putExtra("newHead", true);
                    setResult(2,intent2);
                    MotifyMeActivity.this.finish();
                    break;
                case FAILED:
                    MyUtil.toastMessage(MotifyMeActivity.this, "修改失败！");

                    break;
                case EXIST:
                    MyUtil.toastMessage(MotifyMeActivity.this, "用户名已存在！");

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
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_new_head:
                showChoosePicDialog();
                break;
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
    /**
     * 显示修改头像的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     *
     */
    protected void setImageToView(Intent data) {
        System.out.println("-----setImageToView---");
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
//            iv_personal_icon.setImageBitmap(photo);
            uploadPic(photo);
        }
    }
private String imagePath;
    private void uploadPic(Bitmap bitmap) {
        System.out.println("-----uploadPic---");
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了

         imagePath = Utils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(Global.user.getUserId()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // 拿着imagePath上传了
            if(Global.isNetAvailable)
            {
                progressDialog.show();
                new Thread(uploadImageRunnable).start();
            }else {
                Toast.makeText(this,"网络不可用！",Toast.LENGTH_SHORT).show();
            }

        }
    }
    /**
     * 使用HttpUrlConnection模拟post表单进行文件
     * 上传平时很少使用，比较麻烦
     * 原理是： 分析文件上传的数据格式，然后根据格式构造相应的发送给服务器的字符串。
     */
    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {
            UserDao userDao = new UserDao();
            System.out.println("shang 传图片到服务器");
           String res =  userDao.submitImageToServer(Global.upImageHeadUrl,Global.user.getUserId()+"", imagePath);
            if(res.equals("success"))
            {
                mHandler.sendEmptyMessage(PIC_SUCCESS);// 执行耗时的方法之后发送消给handler
            }else
            {
                mHandler.sendEmptyMessage(FAILED);
            }

        }
    };
}
