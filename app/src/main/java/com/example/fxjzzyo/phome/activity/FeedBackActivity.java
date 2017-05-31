package com.example.fxjzzyo.phome.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.utils.MyUtil;

public class FeedBackActivity extends AppCompatActivity {
private EditText etFeedBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        etFeedBack = (EditText) findViewById(R.id.et_feed_back);
setTitle("意见反馈");
    }
    public void submit(View view)
    {
        MyUtil.toastMessage(this,"谢谢您的意见！");
        etFeedBack.setText("");
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
