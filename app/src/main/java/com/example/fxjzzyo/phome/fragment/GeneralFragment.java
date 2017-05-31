package com.example.fxjzzyo.phome.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.activity.PhoneDetailActivity;
import com.example.fxjzzyo.phome.beanDao.PhoneDao;
import com.example.fxjzzyo.phome.javabean.GeneralPhone;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.Phone;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralFragment extends Fragment {
    private static final String ARG_PARAM2 = "param2";

    private String mPhoneId;//手机id
    private Phone phone;//手机
    private GeneralPhone generalPhone;
    private SmartImageView sivPhonePic;
    private TextView tvPhoneName, tvPhonePrice, tvPhoneRAM, tvScreenSize, tvStorage, tvMarketShare, tvError;
    private RatingBar ratingBar;
    private CustomProgressDialog progressDialog;

    public GeneralFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GeneralFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneralFragment newInstance(Phone phone) {
        GeneralFragment fragment = new GeneralFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM2, phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phone = getArguments().getParcelable(ARG_PARAM2);
            mPhoneId = String.valueOf(phone.getPhoneId());
        }
        progressDialog = CustomProgressDialog.newInstance(getActivity(), "加载中...", false, null);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    private void initData() {
        //根据phoneID从网络获取相应属性
        getGeneralInfoFromNet();
    }

    private void getGeneralInfoFromNet() {
        //根据phoneID从网络获取相应属性
        if (Global.isNetAvailable) {
          progressDialog.show();
            new getGeneralPhoneThread().start();
        } else {
            Toast.makeText(GeneralFragment.this.getActivity(), "网络不可用！", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 用于网络请求数据的子线程
     */
    class getGeneralPhoneThread extends Thread {

        public getGeneralPhoneThread() {
        }

        @Override
        public void run() {
          /*  try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            PhoneDao mPhoneDao = new PhoneDao();
            String result = mPhoneDao.getGeneralPhoneById(Global.getGeneralPhoneInfoUrl, mPhoneId);
            if (result != null)//获取数据成功
            {
                Message msg = new Message();
                msg.what = SUCCESS;
                Bundle bundle = new Bundle();
                bundle.putString("generalPhone", result);
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
//                    Toast.makeText(GeneralFragment.this.getContext(),"获取数据成功！",Toast.LENGTH_SHORT).show();
//                    MyUtil.toastMessage(GeneralFragment.this.getContext(), "获取数据成功！");
                    //获取json字符串
                    Bundle bundle = msg.getData();
                    String jsonPhones = (String) bundle.get("generalPhone");
                    //解析json数据
                    parseJson(jsonPhones);
                    //设置数据到控件
                    setData();
                    break;

                case FAILED:
//                    MyUtil.toastMessage(GeneralFragment.this.getContext(), "获取数据失败！");
                    break;
            }
            if(progressDialog.isShowing())
            {
            progressDialog.dismiss();

            }
        }

        private void parseJson(String jsonPhones) {
            JSONObject jsonObject = JSON.parseObject(jsonPhones);

            generalPhone = JSON.toJavaObject(jsonObject, GeneralPhone.class);

        }
    };

    private void setData() {
        if (generalPhone != null) {
            sivPhonePic.setImageUrl(Global.phonePicUrl + "?picName="+ mPhoneId+ "&type=phonePic", R.drawable.phone1);
            tvPhoneName.setText(generalPhone.getPhoneName());
            tvPhonePrice.setText(generalPhone.getPhonePrice());
            tvPhoneRAM.setText(generalPhone.getPhoneRAM());
            tvScreenSize.setText(generalPhone.getScreenSize());
            tvStorage.setText(generalPhone.getStorage());
            tvMarketShare.setText(generalPhone.getMarketShare());
            tvError.setText(generalPhone.getErrorInfo());
            Float rate = null;
            try {
                rate = Float.valueOf(phone.getPhoneRate());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                rate = 2.5f;
            }
            ratingBar.setRating(rate);
        }
    }

    private void initView() {
        View view = getView();
        sivPhonePic = (SmartImageView) view.findViewById(R.id.siv_phone_pic);
        tvPhoneName = (TextView) view.findViewById(R.id.tv_phone_name);
        tvPhonePrice = (TextView) view.findViewById(R.id.tv_phone_price);
        tvPhoneRAM = (TextView) view.findViewById(R.id.tv_phone_ram);
        tvScreenSize = (TextView) view.findViewById(R.id.tv_screen_size);
        tvStorage = (TextView) view.findViewById(R.id.tv_storage_size);
        tvMarketShare = (TextView) view.findViewById(R.id.tv_market_share);
        tvError = (TextView) view.findViewById(R.id.tv_error);
        ratingBar = (RatingBar) view.findViewById(R.id.rb_rate_size);
    }


}
