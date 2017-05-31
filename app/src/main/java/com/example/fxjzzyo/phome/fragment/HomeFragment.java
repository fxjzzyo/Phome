package com.example.fxjzzyo.phome.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.activity.MainActivity;
import com.example.fxjzzyo.phome.activity.PhoneActivity;
import com.example.fxjzzyo.phome.activity.PhoneDetailActivity;
import com.example.fxjzzyo.phome.adapter.MyListViewAdapter;
import com.example.fxjzzyo.phome.beanDao.PhoneDao;
import com.example.fxjzzyo.phome.database.CoreParamaterDB;
import com.example.fxjzzyo.phome.database.PhoneDB;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.Phone;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ListView mListView;
    private List<Phone> phones;
    private MyListViewAdapter myListViewAdapter;
    private static final String TAG = "TAG";


    private PhoneDB mPhoneDB;

    private BroadcastReceiver mBroadcastReceiver;
    private CustomProgressDialog progressDialog;
    private PtrClassicFrameLayout mPtrFrame;
    private int size;//当前地数据库中该品牌手机的个数，以便于加载更多时确定加载的起始位置
    //标识刷新还是加载更多
    private int REFRUSH = 1;
    private int LOAD_MORE = 0;
    private int flag = -1;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "HomeFragment----onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        progressDialog = CustomProgressDialog.newInstance(getActivity(), "加载中...", false, null);
        initData();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "----HomeFragment----onCreateView");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "--HomeFragment----onAvtivityCreate");
        initView();

        initEvent();
    }

    private void initEvent() {
        myListViewAdapter = new MyListViewAdapter(getContext(), phones);
        mListView.setAdapter(myListViewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Phone phone = phones.get(i);
                //根据id跳转到该手机的详细页
                Intent intent = new Intent(getContext(), PhoneDetailActivity.class);
                //把手机对象传过去
                Bundle bundle = new Bundle();
                bundle.putParcelable("phone", phone);
                intent.putExtra("phone", bundle);
                startActivity(intent);

            }
        });
        //注册广播
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("receive broad Homefragment");
                //从网络请求最新数据
                getHotPhonesFromNet(0);
                flag = REFRUSH;
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("update_data");
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
        //设置刷新控件
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {


                //上拉加载更多
                if (Global.isNetAvailable) {
                    size = phones.size();//获取当前个数
                    System.out.println("-----size------  " + size);
                    flag = LOAD_MORE;
                    new GetPhoneThread(size).start();
                } else {
                    flag = -1;
                    mPtrFrame.refreshComplete();
                    Toast.makeText(getActivity(), "网络不可用！", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                updata();
                if (Global.isNetAvailable) {
                    //下拉刷新，请求网络最新数据
                    flag = REFRUSH;
                    new GetPhoneThread(0).start();
                    System.out.println("---onRefreshBegin--");
                } else {
                    flag = -1;
                    mPtrFrame.refreshComplete();
                    Toast.makeText(getActivity(), "网络不可用！", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return super.checkCanDoLoadMore(frame, mListView, footer);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, mListView, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(true);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);



    }


    private void initData() {
        phones = new ArrayList<>();

        initPhonesByDatabase();


    }

    private void initPhonesByDatabase() {
        mPhoneDB = new PhoneDB(getContext());
        mPhoneDB = mPhoneDB.open();

//    initPhones();
        List<Phone> phoneInDB =  mPhoneDB.fetchSomeHotPhones("0", Global.hotCount, Global.orderType);
        size = phoneInDB.size();
        //如果数据库中没有数据，则请求网络获取热门的前几条数据
        if (size == 0) {

            getHotPhonesFromNet(size);
        } else {
            phones.addAll(phoneInDB);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "-----HomeFragment----onPause");
        if (mPhoneDB != null) {
            mPhoneDB.close();
        }
    }

    private void getHotPhonesFromNet(int begin) {
        if (Global.isNetAvailable) {
            //设置进度框
            if(progressDialog!=null&&!progressDialog.isShowing())
            {
                progressDialog.show();
            }

            new GetPhoneThread(begin).start();
        } else {
            flag = -1;
            MyUtil.toastMessage(HomeFragment.this.getActivity(), "网络不可用！");
        }


    }

    /**
     * 用于网络请求数据的子线程
     */
    class GetPhoneThread extends Thread {
        int begin = 0;

        public GetPhoneThread(int begin) {
            this.begin = begin;
        }

        @Override
        public void run() {

            PhoneDao mPhoneDao = new PhoneDao();
            String result = mPhoneDao.getHotPhonesFromNet(Global.getHotPhonesUrl, begin + "", Global.hotCount, Global.orderType);
            if (result != null)//获取数据成功
            {
                Message msg = new Message();
                msg.what = SUCCESS;
                Bundle bundle = new Bundle();
                bundle.putString("hotPhone", result);
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
                    MyUtil.toastMessage(getActivity(), "获取数据成功！");
                    //获取json字符串
                    Bundle bundle = msg.getData();
                    String jsonPhones = (String) bundle.get("hotPhone");
                    //解析json数据
                    parseJson(jsonPhones);

                    //更新数据库
                    if (flag == LOAD_MORE) {

//                        mPhoneDB.insertSomePhones(phones);//不能把加载更多的数据存入数据库了，因为会发生数据重复
                    } else if(flag == REFRUSH){
                        mPhoneDB.updateTable(phones);
                    }else {
                        mPhoneDB.insertSomePhones(phones);//最开始插入
                    }

                    break;

                case FAILED:

                    MyUtil.toastMessage(getActivity(), "获取数据失败！");
                    break;
            }
            flag = -1;
            //恢复进度框
            if(progressDialog.isShowing())
            {
            progressDialog.dismiss();

            }
            if (mPtrFrame.isRefreshing()) {
                mPtrFrame.refreshComplete();
            }
//            MainActivity.mainActivityInstance.resetLoader(1f);
        }

        private void parseJson(String jsonPhones) {
            Log.i("tag", "parseJson -- flag-->" + flag);
            List<Phone> mPhones = null;
            try {
                mPhones = JSON.parseArray(jsonPhones, Phone.class);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"获取数据失败！",Toast.LENGTH_SHORT).show();
                return;
            }
            if (mPhones != null) {
                if (flag == REFRUSH) {
                    phones = mPhones;
                    //更新List
                    myListViewAdapter.setDataChanged(phones);
                } else if (flag == LOAD_MORE) {
                    phones.addAll(mPhones);
                    myListViewAdapter.setDataChanged(phones);
//                    myListViewAdapter.notifyDataSetChanged();
                    Log.i("tag", "add all -- phone size-->" + phones.size());
                } else {
                    phones = mPhones;
                    //更新List
                    myListViewAdapter.setDataChanged(phones);
                }

            }
        }
    };

    private void initPhones() {

//        phones = new ArrayList<>();
        Phone phone1 = new Phone();
        phone1.setPhoneName("小米note3");
        phone1.setPhoneCommentCount(13);
        phone1.setPhoneRate("5星");
        phone1.setPhonePrice("2099");
        phone1.setPhoneLoveCount(20);


        Phone phone2 = new Phone();
        phone2.setPhoneName("小米note5");
        phone2.setPhoneCommentCount(0);
        phone2.setPhoneRate("5.2星");
        phone2.setPhonePrice("99");
        phone2.setPhoneLoveCount(0);

        Phone phone3 = new Phone();
        phone3.setPhoneName("红米note5");
        phone3.setPhoneCommentCount(32);
        phone3.setPhoneRate("2.4星");
        phone3.setPhonePrice("9");
        phone3.setPhoneLoveCount(23);


        Phone phone4 = new Phone();
        phone4.setPhoneName("红米note5");
        phone4.setPhoneCommentCount(312);
        phone4.setPhoneRate("2.4星");
        phone4.setPhonePrice("9");
        phone4.setPhoneLoveCount(234);


        Phone phone5 = new Phone();
        phone5.setPhoneName("红米note5");
        phone5.setPhoneCommentCount(32);
        phone5.setPhoneRate("2.4星");
        phone5.setPhonePrice("9");
        phone5.setPhoneLoveCount(34);

        Phone phone6 = new Phone();
        phone6.setPhoneName("红米note5");
        phone6.setPhoneCommentCount(32);
        phone6.setPhoneRate("2.4星");
        phone6.setPhonePrice("9");
        phone6.setPhoneLoveCount(234);

        Phone phone7 = new Phone();
        phone7.setPhoneName("红米note5");
        phone7.setPhoneCommentCount(2);
        phone7.setPhoneRate("2.4星");
        phone7.setPhonePrice("9");
        phone7.setPhoneLoveCount(234);

        Phone phone8 = new Phone();
        phone8.setPhoneName("红米note5");
        phone8.setPhoneCommentCount(312);
        phone8.setPhoneRate("2.4星");
        phone8.setPhonePrice("9");
        phone8.setPhoneLoveCount(234);

        mPhoneDB.insertPhone(phone1);
        mPhoneDB.insertPhone(phone2);
        mPhoneDB.insertPhone(phone3);
        mPhoneDB.insertPhone(phone4);
        mPhoneDB.insertPhone(phone5);
        mPhoneDB.insertPhone(phone6);
        mPhoneDB.insertPhone(phone7);
        mPhoneDB.insertPhone(phone8);
      /*  phones.add(phone1);
        phones.add(phone2);
        phones.add(phone3);
        phones.add(phone4);
        phones.add(phone5);
        phones.add(phone6);
        phones.add(phone7);
        phones.add(phone8);*/
    }

    private void initView() {

        View view = getView();
        mListView = (ListView) view.findViewById(R.id.lv);
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.store_house_ptr_frame);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPhoneDB = mPhoneDB.open();

        Log.i("TAG", "---homefragment---onResume-----");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "--homefragmet----onDestroy-----");
        if (mPhoneDB != null) {
            mPhoneDB.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "--homefragmet----onDestroy-----");
        if (mPhoneDB != null) {
            mPhoneDB.close();
        }
//取消注册广播
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
}
