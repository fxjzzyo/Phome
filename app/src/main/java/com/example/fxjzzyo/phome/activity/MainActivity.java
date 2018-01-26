package com.example.fxjzzyo.phome.activity;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fxjzzyo.phome.R;
import com.example.fxjzzyo.phome.adapter.MyFragmentPagerAdapter;
import com.example.fxjzzyo.phome.fragment.HomeFragment;
import com.example.fxjzzyo.phome.fragment.MeFragment;
import com.example.fxjzzyo.phome.fragment.PhoneFragment;
import com.example.fxjzzyo.phome.javabean.Global;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,TextView.OnEditorActionListener {
    private static final String TAG = "TAG";
    private ViewPager mViewPager;
    private ImageView ivHome, ivPhone, ivMe;
    private TextView tvHome, tvPhone, tvMe;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    public static MainActivity mainActivityInstance;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "MainActivity----onCreate");
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        initView();

        initData();
        initEvent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "MainActivity----onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "MainActivity----onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "MainActivity----onDestroy");
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initData() {
        this.mainActivityInstance = this;
        //初始化fragment
        mFragments = new ArrayList<>();
        HomeFragment homeFragment = HomeFragment.newInstance("home", "home");
        PhoneFragment phoneFragment = PhoneFragment.newInstance("phone", "phone");
        MeFragment meFragment = MeFragment.newInstance("me", "me");

        mFragments.add(homeFragment);
        mFragments.add(phoneFragment);
        mFragments.add(meFragment);
        //初始化适配器
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        //为ViewPager设置设适配器
        mViewPager.setAdapter(myFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        //为viewPager添加监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position == 1)
                {
                    if(positionOffset!=0)
                    {

                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                setItemSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //初始化Fragment为首页
        setItemSelected(0);
    }

    private void initEvent() {
        ivHome.setOnClickListener(this);
        ivPhone.setOnClickListener(this);
        ivMe.setOnClickListener(this);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        ivHome = (ImageView) findViewById(R.id.iv_home);
        ivPhone = (ImageView) findViewById(R.id.iv_phone);
        ivMe = (ImageView) findViewById(R.id.iv_me);
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvMe = (TextView) findViewById(R.id.tv_me);


    }

    /**
     * 设置选中项
     *
     * @param position
     */
    private void setItemSelected(int position) {
        //先reset所有项
        ivHome.setSelected(false);
        ivPhone.setSelected(false);
        ivMe.setSelected(false);
        resetTextColor();
        //再设置选择项
        switch (position) {
            case 0:
                ivHome.setSelected(true);
                tvHome.setTextColor(getResources().getColor(R.color.lightBlue));
                setTitle("首页");
                break;
            case 1:
                ivPhone.setSelected(true);
                tvPhone.setTextColor(getResources().getColor(R.color.lightBlue));
                setTitle("手机");
                break;
            case 2:
                ivMe.setSelected(true);
                tvMe.setTextColor(getResources().getColor(R.color.lightBlue));
                setTitle("我的");
                break;
            default:
                break;
        }
        mViewPager.setCurrentItem(position);
    }

    public void resetTextColor() {
        tvMe.setTextColor(getResources().getColor(R.color.Gray));
        tvPhone.setTextColor(getResources().getColor(R.color.Gray));
        tvHome.setTextColor(getResources().getColor(R.color.Gray));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home:
                setItemSelected(0);
                break;
            case R.id.iv_phone:
                setItemSelected(1);
                break;
            case R.id.iv_me:
                setItemSelected(2);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("tag", "-------onOptionsItemSelected--------");
        switch (item.getItemId())
        {
            case R.id.menu_order_comment:
                item.setIcon(R.drawable.circle_selected);
                if(Global.orderType.equals("love_count"))
                {
                    Global.orderType = "comment_count";
                    //发送广播，通知刷新界面
                    Intent intent2 = new Intent();
                    intent2.putExtra("broad_key", "order_type");
                    intent2.setAction("update_data");
                    sendBroadcast(intent2);
                }
                break;
            case R.id.menu_order_love:
                item.setIcon(R.drawable.circle_selected);
                if(Global.orderType.equals("comment_count"))
                {
                    Global.orderType = "love_count";
                    //发送广播，通知刷新界面
                    Intent intent2 = new Intent();
                    intent2.putExtra("broad_key", "order_type");
                    intent2.setAction("update_data");
                    sendBroadcast(intent2);
                }

                break;
            default:
                break;
        }
        return true;

    }



    /**
     * 显示overflower菜单图标
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        Log.i("tag", "-------onMenuOpened--------"+menu);
        if(menu!=null)
        {
            menu.findItem(R.id.menu_order_love).setIcon(R.drawable.circle_unselected);
            menu.findItem(R.id.menu_order_comment).setIcon(R.drawable.circle_unselected);

            if(Global.orderType.equals("love_count"))
            {
                menu.findItem(R.id.menu_order_love).setIcon(R.drawable.circle_selected);
            }else {
                menu.findItem(R.id.menu_order_comment).setIcon(R.drawable.circle_selected);
            }
        }

        return true;
    }
    //通过点击软键盘可以搜索
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            search();
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i("tag", "-------onCreateOptionsMenu--------");
        // 加入含有search view的菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        // 获取SearchView对象
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        System.out.println("fet Search View.");
        if (searchView == null) {
            Log.e("SearchView", "Fail to get Search View.");
            return true;
        }
        searchView.setIconifiedByDefault(true); // 缺省值就是true，可能不专门进行设置，false和true的效果图如下，true的输入框更大

        // 获取搜索服务管理器
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // searchable activity的component name，由此系统可通过intent进行唤起
        ComponentName cn = new ComponentName(this, SearchResultActivity.class);
        // 通过搜索管理器，从searchable activity中获取相关搜索信息，就是searchable的xml设置。如果返回null，表示该activity不存在，或者不是searchable
        SearchableInfo info = searchManager.getSearchableInfo(cn);
        if (info == null) {
            Log.e("SearchableInfo", "Fail to get search info.");
        }
        // 将searchable activity的搜索信息与search view关联
        searchView.setSearchableInfo(info);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    /**
     * 通过反射，设置menu显示icon
     *
     * @param view
     * @param menu
     * @return
     */
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        Log.i("tag", "-------onPrepareOptionsPanel--------"+menu);
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }
}
