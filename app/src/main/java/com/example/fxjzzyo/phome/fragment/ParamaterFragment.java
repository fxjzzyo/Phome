package com.example.fxjzzyo.phome.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.activity.PhoneDetailActivity;
import com.example.fxjzzyo.phome.beanDao.PhoneDao;
import com.example.fxjzzyo.phome.javabean.BasicFunction;
import com.example.fxjzzyo.phome.javabean.Camera;
import com.example.fxjzzyo.phome.javabean.CoreParamater;
import com.example.fxjzzyo.phome.javabean.GeneralPhone;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.Media;
import com.example.fxjzzyo.phome.javabean.NetSupport;
import com.example.fxjzzyo.phome.javabean.PackList;
import com.example.fxjzzyo.phome.javabean.PhoneBody;
import com.example.fxjzzyo.phome.javabean.PosSensor;
import com.example.fxjzzyo.phome.javabean.ScreenInfo;
import com.example.fxjzzyo.phome.javabean.SystemHard;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParamaterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParamaterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mPhoneId;
    private TextView tvScreenSize,tvCPU,tvRAM,tvStorage,tvBackCamera,tvBatterySize,
    tvOperator,tvNetMode,tvSIM,
    tvScreenSize2,tvScreenColorNum,tvScreenResolution,tvPixelDensity,tvTouchMode,
    tvOS,tvCPU2,tvGHz,tvRAM2,tvStorage2,tvStorageExpansion,
    tvSensorType,tvBackCamera2,tvFrontCamera,tvFlashLight,tvVideo,tvPhotoFeature,
    tvBodyColor,tvPhoneType,tvBodySize,tvBodyWeight,tvBatteryType,tvBatterySize2,tvBatteryChane,
    tvUSB,tvEarphone,tvWireless,tvDailyFunction,tvKeyboardType,tvInputType,tvInputMethord,
    tvVideoFormat,tvMusicFormat,tvPicFormat,tvDocFormat,
    tvGPS,tvSensor,tvSpecialFunction,
    tvPackList;


    private CoreParamater coreParamater;
    private NetSupport netSupport;
    private ScreenInfo screenInfo;
    private SystemHard systemHard;
    private Camera camera;
    private PhoneBody phoneBody;
    private BasicFunction basicFunction;
    private Media media;
    private PosSensor posSensor;
    private PackList packList;

private CustomProgressDialog progressDialog;

    public ParamaterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ParamaterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParamaterFragment newInstance(String param1) {
        ParamaterFragment fragment = new ParamaterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhoneId = getArguments().getString(ARG_PARAM1);
        }
        progressDialog = CustomProgressDialog.newInstance(getActivity(), "加载中...", false, null);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paramater, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    private void initData() {
        //根据id从网络获取数据
        //根据phoneID从网络获取相应属性
        getAllParamaterFromNet();


    }

    private void getAllParamaterFromNet() {
        if(Global.isNetAvailable)
        {
            //设置进度框
           progressDialog.show();

            new getAllParamaterThread().start();
        }else {
            Toast.makeText(ParamaterFragment.this.getActivity(),"网络不可用！",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 用于网络请求数据的子线程
     */
    class getAllParamaterThread extends Thread {

        public getAllParamaterThread() {
        }

        @Override
        public void run() {
            PhoneDao mPhoneDao = new PhoneDao();
            String core = mPhoneDao.getAllParamaterById(Global.getAllParamaterInfoUrl,mPhoneId,"core");
            String netSupport = mPhoneDao.getAllParamaterById(Global.getAllParamaterInfoUrl,mPhoneId,"netSupport");
            String screenInfo = mPhoneDao.getAllParamaterById(Global.getAllParamaterInfoUrl,mPhoneId,"screenInfo");
            String systemHard = mPhoneDao.getAllParamaterById(Global.getAllParamaterInfoUrl,mPhoneId,"systemHard");
            String camera = mPhoneDao.getAllParamaterById(Global.getAllParamaterInfoUrl,mPhoneId,"camera");
            String phoneBody = mPhoneDao.getAllParamaterById(Global.getAllParamaterInfoUrl,mPhoneId,"phoneBody");
            String basicFunction = mPhoneDao.getAllParamaterById(Global.getAllParamaterInfoUrl,mPhoneId,"basicFunction");
            String media = mPhoneDao.getAllParamaterById(Global.getAllParamaterInfoUrl,mPhoneId,"media");
            String posSensor = mPhoneDao.getAllParamaterById(Global.getAllParamaterInfoUrl,mPhoneId,"posSensor");
            String packList = mPhoneDao.getAllParamaterById(Global.getAllParamaterInfoUrl,mPhoneId,"packList");


            if(packList!=null)//获取数据成功
            {
                Message msg = new Message();
                msg.what = SUCCESS;
                Bundle bundle = new Bundle();
                bundle.putString("core",core);
                bundle.putString("netSupport",netSupport);
                bundle.putString("screenInfo",screenInfo);
                bundle.putString("systemHard",systemHard);
                bundle.putString("camera",camera);
                bundle.putString("phoneBody",phoneBody);
                bundle.putString("basicFunction",basicFunction);
                bundle.putString("media",media);
                bundle.putString("posSensor",posSensor);
                bundle.putString("packList",packList);
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
//                    MyUtil.toastMessage(getActivity(), "获取数据成功！");
                    //获取json字符串
                    Bundle bundle = msg.getData();
                    String core = (String) bundle.get("core");
                    String netSupport = (String) bundle.get("netSupport");
                    String screenInfo = (String) bundle.get("screenInfo");
                    String systemHard = (String) bundle.get("systemHard");
                    String camera = (String) bundle.get("camera");
                    String phoneBody = (String) bundle.get("phoneBody");
                    String basicFunction = (String) bundle.get("basicFunction");
                    String media = (String) bundle.get("media");
                    String posSensor = (String) bundle.get("posSensor");
                    String packList = (String) bundle.get("packList");
                    //解析json数据
                    parseJson(core);
                    parseJson(netSupport);
                    parseJson(screenInfo);
                    parseJson(systemHard);
                    parseJson(camera);
                    parseJson(phoneBody);
                    parseJson(basicFunction);
                    parseJson(media);
                    parseJson(posSensor);
                    parseJson(packList);
                    //设置数据到控件
                    setData();
                    break;

                case FAILED:
                    MyUtil.toastMessage(getActivity(), "获取数据失败！");
                    //TODO
                    //下拉刷新，重新加载
                    break;
            }
            if(progressDialog.isShowing())
            {
            progressDialog.dismiss();

            }
        }

        private void parseJson(String jsonPhone) {
            JSONObject jsonObject = JSON.parseObject(jsonPhone);
            switch (jsonPhone)
            {
                case "core":
                    coreParamater = JSON.toJavaObject(jsonObject,CoreParamater.class);
                    break;
                case "netSupport":
                    netSupport = JSON.toJavaObject(jsonObject,NetSupport.class);
                    break;
                case "screenInfo":
                    screenInfo = JSON.toJavaObject(jsonObject,ScreenInfo.class);
                    break;
                case "systemHard":
                    systemHard = JSON.toJavaObject(jsonObject,SystemHard.class);
                    break;
                case "camera":
                    camera = JSON.toJavaObject(jsonObject,Camera.class);
                    break;
                case "phoneBody":
                    phoneBody = JSON.toJavaObject(jsonObject,PhoneBody.class);
                    break;
                case "basicFunction":
                    basicFunction = JSON.toJavaObject(jsonObject,BasicFunction.class);
                    break;
                case "media":
                    media = JSON.toJavaObject(jsonObject,Media.class);
                    break;
                case "posSensor":
                    posSensor = JSON.toJavaObject(jsonObject,PosSensor.class);
                    break;
                case "packList":
                    packList = JSON.toJavaObject(jsonObject,PackList.class);
                    break;

            }


        }
    };

    private void setData() {
        if(coreParamater!=null)
        {
            tvScreenSize.setText(coreParamater.getScreenSize());
            tvCPU.setText(coreParamater.getCpuInfo());
            tvRAM.setText(coreParamater.getRAMSize());
            tvStorage.setText(coreParamater.getStorageCapacity());
            tvBackCamera.setText(coreParamater.getBackCameraPixel());
            tvBatterySize.setText(coreParamater.getBatteryCapacity());

        }
        if(netSupport!=null)
        {
            tvOperator.setText(netSupport.getOperator());
            tvNetMode.setText(netSupport.getNetMode());
            tvSIM.setText(netSupport.getSim());

        }



        if(screenInfo!=null)
        {
            tvScreenSize2.setText(screenInfo.getScreenSize());
            tvScreenColorNum.setText(screenInfo.getScreenColorNum());
            tvScreenResolution.setText(screenInfo.getScreenResolution());
            tvPixelDensity.setText(screenInfo.getPixelDesity());
            tvTouchMode.setText(screenInfo.getTouchWay());

        }

        if(systemHard!=null)
        {
            tvOS.setText(systemHard.getOs());
            tvCPU2.setText(systemHard.getCpu());
            tvGHz.setText(systemHard.getGhz());
            tvRAM2.setText(systemHard.getRam());
            tvStorage2.setText(systemHard.getStorage());
            tvStorageExpansion.setText(systemHard.getStorageExpansion());

        }
        if(camera!=null)
        {
            tvSensorType.setText(camera.getSensorType());
            tvBackCamera2.setText(camera.getBackCamera());
            tvFrontCamera.setText(camera.getFrontCamera());
            tvFlashLight.setText(camera.getFlashLight());
            tvVideo.setText(camera.getVideo());
            tvPhotoFeature.setText(camera.getPhotoFeature());

        }
        if(phoneBody!=null)
        {
            tvBodyColor.setText(phoneBody.getBodyColor());
            tvPhoneType.setText(phoneBody.getPhoneType());
            tvBodySize.setText(phoneBody.getBodySize());
            tvBodyWeight.setText(phoneBody.getBodyWeight());
            tvBatteryType.setText(phoneBody.getBatteryType());
            tvBatterySize2.setText(phoneBody.getBatterySize());
            tvBatteryChane.setText(phoneBody.getBatteryChange());

        }

        if(basicFunction!=null)
        {
            tvUSB.setText(basicFunction.getUsb());
            tvEarphone.setText(basicFunction.getEarphoe());
            tvWireless.setText(basicFunction.getWireless());
            tvDailyFunction.setText(basicFunction.getDailyFunction());
            tvKeyboardType.setText(basicFunction.getKeyboard());
            tvInputType.setText(basicFunction.getInputType());
            tvInputMethord.setText(basicFunction.getInputMethord());

        }

        if(media!=null)
        {
            tvVideoFormat.setText(media.getVideoFormat());
            tvMusicFormat.setText(media.getMusicFormat());
            tvPicFormat.setText(media.getPicFormat());
            tvDocFormat.setText(media.getDocFormat());

        }
        if(posSensor!=null)
        {
            tvGPS.setText(posSensor.getGps());
            tvSensor.setText(posSensor.getSensor());
            tvSpecialFunction.setText(posSensor.getSpecialFunction());

        }
        if(packList!=null)
        {
            tvPackList.setText(packList.getPackList());

        }
    }
    private void initView() {
        View view = getView();
        tvScreenSize = (TextView) view.findViewById(R.id.tv_screen_size);
        tvCPU = (TextView) view.findViewById(R.id.tv_cpu);
        tvRAM = (TextView) view.findViewById(R.id.tv_phone_ram);
        tvStorage = (TextView) view.findViewById(R.id.tv_storage_size);
        tvBackCamera = (TextView) view.findViewById(R.id.tv_back_camera);
        tvBatterySize = (TextView) view.findViewById(R.id.tv_battery_size);

        tvOperator = (TextView) view.findViewById(R.id.tv_operator);
        tvNetMode = (TextView) view.findViewById(R.id.tv_net_mode);
        tvSIM = (TextView) view.findViewById(R.id.tv_sim_type);

        tvScreenSize2 = (TextView) view.findViewById(R.id.tv_screen_size2);
        tvScreenColorNum = (TextView) view.findViewById(R.id.tv_screen_color_num);
        tvScreenResolution = (TextView) view.findViewById(R.id.tv_screen_resolution);
        tvPixelDensity = (TextView) view.findViewById(R.id.tv_pixel_density);
        tvTouchMode = (TextView) view.findViewById(R.id.tv_touch_mode);

        tvOS = (TextView) view.findViewById(R.id.tv_os);
        tvCPU2 = (TextView) view.findViewById(R.id.tv_cpu2);
        tvGHz = (TextView) view.findViewById(R.id.tv_ghz);
        tvRAM2 = (TextView) view.findViewById(R.id.tv_phone_ram2);
        tvStorage2 = (TextView) view.findViewById(R.id.tv_storage_size2);
        tvStorageExpansion = (TextView) view.findViewById(R.id.tv_storge_expansion);

        tvSensorType = (TextView) view.findViewById(R.id.tv_sensor_type);
        tvBackCamera2 = (TextView) view.findViewById(R.id.tv_back_camera2);
        tvFrontCamera = (TextView) view.findViewById(R.id.tv_front_camera);
        tvFlashLight = (TextView) view.findViewById(R.id.tv_flash_light);
        tvVideo = (TextView) view.findViewById(R.id.tv_video);
        tvPhotoFeature = (TextView) view.findViewById(R.id.tv_photo_feature);

        tvBodyColor = (TextView) view.findViewById(R.id.tv_body_color);
        tvPhoneType = (TextView) view.findViewById(R.id.tv_phone_type);
        tvBodySize = (TextView) view.findViewById(R.id.tv_body_size);
        tvBodyWeight = (TextView) view.findViewById(R.id.tv_body_weight);
        tvBatteryType = (TextView) view.findViewById(R.id.tv_battery_type);
        tvBatterySize2= (TextView) view.findViewById(R.id.tv_battery_size2);
        tvBatteryChane = (TextView) view.findViewById(R.id.tv_battery_change);

        tvUSB = (TextView) view.findViewById(R.id.tv_usb);
        tvEarphone = (TextView) view.findViewById(R.id.tv_earphone);
        tvWireless = (TextView) view.findViewById(R.id.tv_wireless_con);
        tvDailyFunction = (TextView) view.findViewById(R.id.tv_daily_function);
        tvKeyboardType = (TextView) view.findViewById(R.id.tv_keyboard_type);
        tvInputType = (TextView) view.findViewById(R.id.tv_input_type);
        tvInputMethord = (TextView) view.findViewById(R.id.tv_input_methord);

        tvVideoFormat = (TextView) view.findViewById(R.id.tv_video_format);
        tvMusicFormat = (TextView) view.findViewById(R.id.tv_music_format);
        tvPicFormat = (TextView) view.findViewById(R.id.tv_pic_format);
        tvDocFormat = (TextView) view.findViewById(R.id.tv_doc_format);

        tvGPS = (TextView) view.findViewById(R.id.tv_gps);
        tvSensor = (TextView) view.findViewById(R.id.tv_sensor);
        tvSpecialFunction = (TextView) view.findViewById(R.id.tv_special_function);
        tvPackList = (TextView) view.findViewById(R.id.tv_pack_list);


    }
}
