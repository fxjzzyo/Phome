package com.example.fxjzzyo.phome.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.utils.MyUtil;

public class MotifyIPActivity extends AppCompatActivity {
    private TextView tvCurIp;
    private EditText etIp1, etIp2, etIp3, etIp4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motify_ip);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        tvCurIp = (TextView) findViewById(R.id.tv_cur_ip);
        etIp1 = (EditText) findViewById(R.id.et_new_ip1);
        etIp2 = (EditText) findViewById(R.id.et_new_ip2);
        etIp3 = (EditText) findViewById(R.id.et_new_ip3);
        etIp4 = (EditText) findViewById(R.id.et_new_ip4);
        tvCurIp.setText(Global.ip);
        setTitle("更改IP");
    }

    public void ok(View view) {
        String ip1 = etIp1.getText().toString().trim();
        String ip2 = etIp2.getText().toString().trim();
        String ip3 = etIp3.getText().toString().trim();
        String ip4 = etIp4.getText().toString().trim();
        String newIp = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
        if (ip1.isEmpty() || ip2.isEmpty() || ip3.isEmpty() || ip4.isEmpty()) {
            MyUtil.toastMessage(this, "IP格式有误！");
            return;
        }
        tvCurIp.setText(newIp);
        Global.ip = newIp;
        Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
    }

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
