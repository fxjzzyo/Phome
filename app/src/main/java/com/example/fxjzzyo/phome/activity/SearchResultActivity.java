package com.example.fxjzzyo.phome.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.adapter.MyListViewAdapter;
import com.example.fxjzzyo.phome.beanDao.PhoneDao;
import com.example.fxjzzyo.phome.fragment.HomeFragment;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.javabean.Phone;
import com.example.fxjzzyo.phome.utils.MyUtil;
import com.example.fxjzzyo.phome.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private ListView lvSearch;
    private MyListViewAdapter adapter;
    private List<Phone> phones;

    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        initView();

        initData();
        initEvent();

    }

    private void initEvent() {
        lvSearch.setAdapter(adapter);
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Phone phone = phones.get(i);
                //根据id跳转到该手机的详细页
                Intent intent = new Intent(SearchResultActivity.this, PhoneDetailActivity.class);
                //把手机对象传过去
                Bundle bundle = new Bundle();
                bundle.putParcelable("phone", phone);
                intent.putExtra("phone", bundle);
                startActivity(intent);

            }
        });
    }

    private void initData() {
        phones = new ArrayList<>();
        adapter = new MyListViewAdapter(this, phones);
        doSearchQuery(getIntent());
    }

    private void initView() {
        lvSearch = (ListView) findViewById(R.id.lv_search);
        progressDialog = CustomProgressDialog.newInstance(this, "加载中...", false, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {  //activity重新置顶
        super.onNewIntent(intent);
        doSearchQuery(intent);
    }

    // 对searchable activity的调用仍是标准的intent，我们可以从intent中获取信息，即要搜索的内容
    private void doSearchQuery(Intent intent) {
        if (intent == null)
            return;

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {  //如果是通过ACTION_SEARCH来调用，即如果通过搜索调用
            String queryString = intent.getStringExtra(SearchManager.QUERY); //获取搜索内容
            setTitle("搜索");
            getPhoneByName(queryString);
        }

    }

    private void getPhoneByName(String queryString) {

        if (Global.isNetAvailable) {
            //设置进度框
            progressDialog.show();
            new GetPhoneThread(queryString).start();
        } else {

            Toast.makeText(this, "网络不可用！", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 用于网络请求数据的子线程
     */
    class GetPhoneThread extends Thread {
        String queryString;

        public GetPhoneThread(String queryString) {
            this.queryString = queryString;
        }

        @Override
        public void run() {

            PhoneDao mPhoneDao = new PhoneDao();
            String result = mPhoneDao.getPhonesByName(Global.getPhonesByNameUrl, queryString);
            if (result != null)//获取数据成功
            {
                Message msg = new Message();
                msg.what = SUCCESS;
                Bundle bundle = new Bundle();
                bundle.putString("Phone", result);
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
                    //获取json字符串
                    Bundle bundle = msg.getData();
                    String jsonPhones = (String) bundle.get("Phone");
                    //解析json数据
                    parseJson(jsonPhones);


                    break;

                case FAILED:

                    MyUtil.toastMessage(SearchResultActivity.this, "获取数据失败！");
                    break;
            }
            //恢复进度框
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

            }

        }

        private void parseJson(String jsonPhones) {
            List<Phone> mPhones = null;
            try {
                mPhones = JSON.parseArray(jsonPhones, Phone.class);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SearchResultActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mPhones != null) {
                phones = mPhones;
                //更新List
                adapter.setDataChanged(phones);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }

        return true;
    }
}
