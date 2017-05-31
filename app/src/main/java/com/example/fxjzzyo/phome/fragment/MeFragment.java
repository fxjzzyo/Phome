package com.example.fxjzzyo.phome.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.activity.AboutActivity;
import com.example.fxjzzyo.phome.activity.FeedBackActivity;
import com.example.fxjzzyo.phome.activity.LoginActivity;
import com.example.fxjzzyo.phome.activity.LoveActivity;
import com.example.fxjzzyo.phome.activity.MainActivity;
import com.example.fxjzzyo.phome.activity.MotifyIPActivity;
import com.example.fxjzzyo.phome.activity.MotifyMeActivity;
import com.example.fxjzzyo.phome.activity.MotifyPassActivity;
import com.example.fxjzzyo.phome.beanDao.PhoneDao;
import com.example.fxjzzyo.phome.beanDao.UserDao;
import com.example.fxjzzyo.phome.database.PhoneDB;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.Phone;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.utils.SharedPreferenceUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;
import com.loopj.android.image.SmartImageView;
import com.loopj.android.image.WebImage;
import com.loopj.android.image.WebImageCache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.fxjzzyo.phome.javabean.Global.user;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

private ImageView ivMotifyMe;
    private TextView tvUserName,tvLoveCount;
    private SmartImageView sivUserPic;
    private List<Phone> lovePones;
    private RelativeLayout rlMotifyIp,rlLove,rlFeedBack,rlAbout,rlLogout,rlMotifyPass,rlClearCache;
    private BroadcastReceiver mBroadcastReceiver;

    private CustomProgressDialog progressDialog;
    public MeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TAG", "--mefragment----onCreate-----");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        progressDialog = CustomProgressDialog.newInstance(getActivity(), "加载中...", false,null);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("TAG", "---mefragment---onCreateView-----");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("TAG", "---mefragment---onActivityCreateView-----");
        initView();

        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG", "---mefragment---onResume-----");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("TAG", "---mefragment---onPause-----");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("TAG", "---mefragment---onStop-----");
    }

    private void initEvent() {
        if(null!=user)//已登录
        {
            System.out.println("user name-----------------"+user.getUserName());
            tvUserName.setText(user.getUserName());
            int userId = user.getUserId();
            sivUserPic.setImageUrl(Global.phonePicUrl+"?picName="+userId+"&type=useHead",R.drawable.me_unselected);

        }
        rlLove.setOnClickListener(new MyOnClickListener());
        rlMotifyIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getActivity(), MotifyIPActivity.class);
                startActivity(intent3);
            }
        });
        rlFeedBack.setOnClickListener(new MyOnClickListener());
        rlAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent4);
            }
        });
        rlClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneDB phoneDB = new PhoneDB(getActivity());
                phoneDB = phoneDB.open();
                boolean res = phoneDB.deleteAllPhones();
                if(res)
                {
                    Toast.makeText(getActivity(), "清除成功！", Toast.LENGTH_SHORT).show();
                    phoneDB.close();
                }
            }
        });
        rlLogout.setOnClickListener(new MyOnClickListener());
        rlMotifyPass.setOnClickListener(new MyOnClickListener());
        ivMotifyMe.setOnClickListener(new MyOnClickListener());
        //点击用户名，如果没登录，跳转到登录页，否则跳转到修改me页
        tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.user!=null)
                {
                    Intent intent4 = new Intent(getActivity(), MotifyMeActivity.class);
                    startActivity(intent4);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        //注册广播
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("receive broad mefragment");
                String str = intent.getStringExtra("broad_key");
                if(str.equals("love_success")||str.equals("unlove_success"))
                {
                    //更新网络数据
                    getLovesFromNet(Global.user.getUserId());
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("update_data");
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
    }
class MyOnClickListener implements View.OnClickListener{

    @Override
    public void onClick(View view) {
        if(Global.user==null)
        {
            MyUtil.toastMessage(getActivity(),"你还没有登录！");
            return;
        }
        switch (view.getId())
        {
            case R.id.rl_love:
                //跳转到展示喜爱手机页
                    if(lovePones!=null)
                    {
                        Intent intent1 = new Intent(getActivity(), LoveActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("lovePhones", (ArrayList<? extends Parcelable>) lovePones);
                        intent1.putExtra("lovePhone", bundle);
                        startActivity(intent1);
                    }else {
                        if(Global.isNetAvailable)
                        {
                            //从网络获取该用户喜欢的手机
                            new GetUserLovesThread(Global.user.getUserId()).start();
                            Toast.makeText(MeFragment.this.getActivity(),"正在请求网络",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MeFragment.this.getActivity(),"网络不可用！",Toast.LENGTH_SHORT).show();
                        }
                    }

                break;
            case R.id.rl_feed_back:
                Intent intent2 = new Intent(getActivity(), FeedBackActivity.class);

                startActivity(intent2);
                break;
            case R.id.rl_motify_pass:
                Intent intent3 = new Intent(getActivity(), MotifyPassActivity.class);

                startActivity(intent3);
                break;
            case R.id.iv_motify_me:
                Intent intent4 = new Intent(getActivity(), MotifyMeActivity.class);

                startActivityForResult(intent4,1);
                break;

            case R.id.rl_logout:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                Global.user = null;
                //设置下次取消自动登录
                SharedPreferenceUtil.saveData(MeFragment.this.getActivity(),SharedPreferenceUtil.IS_AUTO_LOGIN,null);

                getActivity().finish();
                break;

        }
    }
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1&&resultCode == 1)
        {
            String result = data.getStringExtra("result");
            String newName = data.getStringExtra("newName");

            if(!result.equals("failed"))
            {
                tvUserName.setText(newName);
                SharedPreferenceUtil.saveData(getActivity(),SharedPreferenceUtil.KEY_NAME,newName);
            }


        }else if(requestCode == 1&&resultCode == 2)
        {
            boolean newHead = data.getBooleanExtra("newHead",false);
            if(newHead)
            {
                //重新加载图片

                WebImageCache cache = new WebImageCache(getActivity());

                WebImage.removeFromCache(Global.phonePicUrl+"?picName="+user.getUserId()+"&type=useHead");
                cache.clear();
                sivUserPic.setImageUrl(Global.phonePicUrl+"?picName="+user.getUserId()+"&type=useHead",R.drawable.me_unselected);

            }
        }



    }

    private void initData() {
        //获取用户信息
        if(null!=user)//已登录
        {
           int userId = user.getUserId();
           getLovesFromNet(userId);

        }


    }

    /**
     * 从网络获取数据
     * @param userId
     */
    private void getLovesFromNet(int userId){
        if(Global.isNetAvailable)
        {
            //设置进度框
            progressDialog.show();
            //从网络获取该用户喜欢的手机
            new GetUserLovesThread(userId).start();
        }else {
            Toast.makeText(MeFragment.this.getActivity(),"网络不可用！",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 用于网络请求数据的子线程
     */
    class GetUserLovesThread extends Thread {
        private int userId;
        public GetUserLovesThread(int userId) {
            this.userId = userId;
        }

        @Override
        public void run() {
            UserDao userDao = new UserDao();
            String result = userDao.getUserLoves(Global.getUserLovesUrl,userId+"");
            if(result!=null)//获取数据成功
            {
                Message msg = new Message();
                msg.what = SUCCESS;
                Bundle bundle = new Bundle();
                bundle.putString("lovePhone",result);
                msg.setData(bundle);
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
                    MyUtil.toastMessage(getActivity(), "获取数据成功！");
                    //获取json字符串
                    Bundle bundle = msg.getData();
                    String jsonPhones = (String) bundle.get("lovePhone");
                    //解析json数据
                    parseJson(jsonPhones);
                    //设置喜欢的个数
                    if(lovePones!=null)
                    {

                    tvLoveCount.setText(lovePones.size()+"个");
                    }

                    break;

                case FAILED:
                    MyUtil.toastMessage(getActivity(), "获取数据失败！");

                    break;
            }
            //恢复进度框
            if(progressDialog.isShowing())
            {
            progressDialog.dismiss();

            }
        }

        private void parseJson(String jsonPhones) {
            List<Phone> mPhones =  JSON.parseArray(jsonPhones, Phone.class);
            if(mPhones!=null)
            {
                lovePones = mPhones;
            }
        }
    };
    private void initView() {

        View view = getView();
        sivUserPic = (SmartImageView) view.findViewById(R.id.siv_user_pic);
       ivMotifyMe = (ImageView) view.findViewById(R.id.iv_motify_me);
        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        rlLove = (RelativeLayout) view.findViewById(R.id.rl_love);
        rlMotifyIp = (RelativeLayout) view.findViewById(R.id.rl_motify_ip);
        rlFeedBack = (RelativeLayout) view.findViewById(R.id.rl_feed_back);
        rlAbout= (RelativeLayout) view.findViewById(R.id.rl_about);
        rlLogout = (RelativeLayout) view.findViewById(R.id.rl_logout);
        tvLoveCount = (TextView) view.findViewById(R.id.tv_love_count);
        rlMotifyPass = (RelativeLayout) view.findViewById(R.id.rl_motify_pass);
        rlClearCache = (RelativeLayout) view.findViewById(R.id.rl_clear_cache);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消注册
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
}
