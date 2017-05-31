package com.example.fxjzzyo.phome.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.adapter.MyListViewAdapter;
import com.example.fxjzzyo.phome.beanDao.PhoneDao;
import com.example.fxjzzyo.phome.database.PhoneDB;
import com.example.fxjzzyo.phome.fragment.HomeFragment;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.Phone;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class PhoneActivity extends AppCompatActivity {
    private ListView mListView;
    private List<Phone> mPhoneList;
    private TextView tvTips;//无数据时，背景提示
    private MyListViewAdapter myListViewAdapter;

    private String brand = "小米";//为了演示方便，所有品牌的查询都默认为小米
    private int size;//当前地数据库中该品牌手机的个数，以便于加载更多时确定加载的起始位置
    private PhoneDB mPhoneDB;
    private CustomProgressDialog progressDialog;
    private PtrClassicFrameLayout mPtrFrame;
    //标识刷新还是加载更多
    private int REFRUSH = 1;
    private int LOAD_MORE = 0;
    private int flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        initView();
        initData();
        initEvent();

    }

    private void initEvent() {
        myListViewAdapter = new MyListViewAdapter(this, mPhoneList);
        mListView.setAdapter(myListViewAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Phone phone = mPhoneList.get(i);
                Intent intent = new Intent(PhoneActivity.this, PhoneDetailActivity.class);
                //把手机对象传过去
                Bundle bundle = new Bundle();
                bundle.putParcelable("phone", phone);
                intent.putExtra("phone", bundle);
                startActivity(intent);

            }
        });
        //设置刷新控件
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                //上拉加载更多
                if (Global.isNetAvailable) {
                    List<Phone> phones = mPhoneDB.getPhoneByBrand(brand);
                    size = phones.size();//获取当前本地数据库中该品牌手机的个数
                    new getPhoneThread(size).start();
                    flag = LOAD_MORE;
                } else {
                    mPtrFrame.refreshComplete();
                    Toast.makeText(PhoneActivity.this, "网络不可用！", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉也当成上拉加载，请求更多数据
                if (Global.isNetAvailable) {
                    List<Phone> phones = mPhoneDB.getPhoneByBrand(brand);
                    size = phones.size();//获取当前本地数据库中该品牌手机的个数
                    new getPhoneThread(size).start();
                    flag = LOAD_MORE;
                } else {
                    mPtrFrame.refreshComplete();
                    Toast.makeText(PhoneActivity.this, "网络不可用！", Toast.LENGTH_SHORT).show();
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
        progressDialog = CustomProgressDialog.newInstance(this, "加载中...", false, null);

        //拿到手机品牌
        String brand2 =getIntent().getStringExtra("brand");
//        brand = getIntent().getStringExtra("brand");//为了演示方便，所有品牌的查询都默认为小米
        //设置标题
        setTitle(brand2);
        //根据品牌查询手机
        initPhonesByDatabase();




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
    private void initPhonesByDatabase() {
        mPhoneList = new ArrayList<>();
        mPhoneDB = new PhoneDB(this);
        mPhoneDB = mPhoneDB.open();
//    initPhones();
        List<Phone> phinesInDB = mPhoneDB.getPhoneByBrand(brand);
        size = phinesInDB.size();//获取当前本地数据库中该品牌手机的个数
        Log.i("tag","----phinesInDB size------"+size);
        //如果数据库中没有数据，则请求网络获取前10条数据
        if (size == 0) {
            getSpeciPhonesFromNet();
        }else {
            mPhoneList.addAll(phinesInDB);
        }

    }

    /**
     * 根据手机品牌获取特定手机
     */
    private void getSpeciPhonesFromNet() {
        if (Global.isNetAvailable) {
            progressDialog.show();
            new getPhoneThread(size).start();
        } else {
            MyUtil.toastMessage(PhoneActivity.this, "网络不可用！");
        }


    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.store_house_ptr_frame);
        tvTips = (TextView) findViewById(R.id.tv_tips);
    }



    /**
     * 用于网络请求数据的子线程
     */
    class getPhoneThread extends Thread {
        int begin = 0;

        public getPhoneThread(int begin) {
            this.begin = begin;
        }

        @Override
        public void run() {
            PhoneDao mPhoneDao = new PhoneDao();
            String result = mPhoneDao.getSpeciPhonesByBrand(Global.getSpeciPhonesUrl, brand, begin + "", Global.count);
            if (result != null)//获取数据成功
            {
                Message msg = new Message();
                msg.what = SUCCESS;
                Bundle bundle = new Bundle();
                bundle.putString("speciPhone", result);
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
                    MyUtil.toastMessage(PhoneActivity.this, "获取数据成功！");
                    //获取json字符串
                    Bundle bundle = msg.getData();
                    String jsonPhones = (String) bundle.get("speciPhone");
                    //解析json数据
                  List<Phone> phones =   parseJson(jsonPhones);
                    if (phones != null) {
                        if (flag == LOAD_MORE) {
                            mPhoneList.addAll(phones);
                            //更新List
                            myListViewAdapter.setDataChanged(mPhoneList);
                            //更新数据库
                            if (flag == LOAD_MORE) {
                                mPhoneDB.insertSomePhones(phones);
                            }
                        }

                    }
                    if(mPhoneList.size()==0)
                    {
                        tvTips.setVisibility(View.VISIBLE);
                    }else {
                        tvTips.setVisibility(View.GONE);
                    }


                    break;

                case FAILED:
                    MyUtil.toastMessage(PhoneActivity.this, "获取数据失败！");
                    break;
            }
            if (progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            if (mPtrFrame.isRefreshing()) {
                mPtrFrame.refreshComplete();
            }
        }

        private List<Phone> parseJson(String jsonPhones) {
            List<Phone> phones = JSON.parseArray(jsonPhones, Phone.class);

            return phones;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPhoneDB != null) {
            mPhoneDB.close();
        }

    }
}
