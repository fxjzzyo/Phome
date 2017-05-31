package com.example.fxjzzyo.phome.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.adapter.MyListViewAdapter;
import com.example.fxjzzyo.phome.javabean.Phone;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.List;

public class LoveActivity extends AppCompatActivity {
private ListView lvLove;
    private List<Phone> lovePhones;
    private MyListViewAdapter myListViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        initView();
        initData();
    }

    private void initData() {
        setTitle("我喜欢的");
        Bundle bundle = getIntent().getBundleExtra("lovePhone");
        lovePhones =bundle.getParcelableArrayList("lovePhones");

        if(lovePhones!=null)
        {
            myListViewAdapter = new MyListViewAdapter(this,lovePhones);
            lvLove.setAdapter(myListViewAdapter);
        }
        lvLove.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Phone phone = lovePhones.get(i);
                //根据id跳转到该手机的详细页
                Intent intent = new Intent(LoveActivity.this, PhoneDetailActivity.class);
                //把手机对象传过去
                Bundle bundle = new Bundle();
                bundle.putParcelable("phone", phone);
                intent.putExtra("phone", bundle);
                startActivity(intent);
            }
        });

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
    private void initView() {
        lvLove = (ListView) findViewById(R.id.lv_love);
    }
}
