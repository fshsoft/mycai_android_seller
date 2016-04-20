package com.fiftyonemycai365.seller.business.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.fanwe.seallibrary.model.PointInfo;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.ParamConstants;
import com.fanwe.seallibrary.model.UserInfo;
import com.fiftyonemycai365.seller.business.util.PushUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.AppUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends BaseActivity implements
        OnClickListener, TencentLocationListener {

    private LayoutInflater mLayoutInflater;
    private FragmentTabHost mTabHost;
    private List<TabInfo> mTabInfoList;

    private TencentLocationManager mLocationManager;

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        mLocationManager.removeUpdates(this);
        if (TencentLocation.ERROR_OK == i) {

            PointInfo pointInfo = new PointInfo(tencentLocation.getLatitude(), tencentLocation.getLongitude());

            PreferenceUtils.setObject(getApplicationContext(), pointInfo);
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    class TabInfo {
        public String tabText;
        public int tabSelector;
        public String tabTitle;
        public boolean showLeft;
        public boolean showRight;
        public Class<?> fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        JPinit();
        initTabSpec();

        initLocation();
        setPageTag(TagManage.MAIN_ACTIVITY);

    }

    private void JPinit() {

        // 推送
        PushUtils.initTagsAndAlias(getApplicationContext());

    }


    private void init() {

        setCustomTitle(R.layout.titlebar_main);
        setViewClickListener(R.id.title_right, this);
        setViewClickListener(R.id.title_left, this);
        setViewText(R.id.title, R.string.order);
        setViewVisible(R.id.title_left, View.INVISIBLE);
        setViewVisible(R.id.title_right, View.INVISIBLE);
    }

    private TabInfo creatTab(String title,
                             String text,
                             int selector,
                             boolean showLeft,
                             boolean showRight,
                             Class<?> fragment) {
        TabInfo tabInfo = new TabInfo();
        tabInfo.tabTitle = title;
        tabInfo.tabText = text;
        tabInfo.showLeft = showLeft;
        tabInfo.showRight = showRight;
        tabInfo.tabSelector = selector;
        tabInfo.fragment = fragment;
        return tabInfo;
    }

    private void initTabSpec() {
        mTabInfoList = new ArrayList<>();
        UserInfo user = PreferenceUtils.getObject(this, UserInfo.class);

        mTabInfoList.add(creatTab(getString(R.string.msg_new_order), getString(R.string.msg_new_order), R.drawable.tab_new_order_selector, false, false, OrderNewListFragment.class));
        mTabInfoList.add(creatTab(getString(R.string.msg_order_manage), getString(R.string.msg_order_manage), R.drawable.tab_mgr_order_selector, true, true, OrderManageListFragment.class));
        if ((user.role & 1) > 0) {
            mTabInfoList.add(creatTab(getString(R.string.store), getString(R.string.store), R.drawable.tab_store_selector, false, false, StorePageFragment.class));
        }
        mTabInfoList.add(creatTab(getString(R.string.my), getString(R.string.my), R.drawable.tab_user_selector, false, false, MoreFragment.class));

        // 实例化布局对象
        mLayoutInflater = LayoutInflater.from(this);
        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (int i = 0; i < mTabInfoList.size(); i++) {
            TabSpec tabSpec = mTabHost.newTabSpec(
                    mTabInfoList.get(i).fragment.getName()).setIndicator(
                    getTabItemView(i));
            mTabHost.addTab(tabSpec, mTabInfoList.get(i).fragment, null);
        }

        // 显示
        mTabHost.setCurrentTab(0);

        // 栈上面叠加了很多Fragment的时候，如果想再次点击TabHost的首页，能返回到最初首页的页面的话，那就要把首页的Fragment上面的Fragment的弹出
        for (int i = 0, size = mTabInfoList.size(); i < size; i++) {
            final int j = i;
            mTabHost.getTabWidget().getChildAt(i)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final TabInfo tabInfo = mTabInfoList.get(j);

                            setViewText(R.id.title, tabInfo.tabTitle);
                            setViewVisible(R.id.title_left, tabInfo.showLeft ? View.VISIBLE : View.GONE);
                            setViewVisible(R.id.title_right, tabInfo.showRight ? View.VISIBLE : View.GONE);

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (!tabInfo.showRight) {
                                        closeSearchTitle();
                                    }
                                }
                            }, 50);

                            getSupportFragmentManager().popBackStack(null,
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            mTabHost.setCurrentTab(j);
                        }
                    });
        }
        findViewById(R.id.title_left).setOnClickListener(MainActivity.this);
        SearchView searView = (SearchView) findViewById(R.id.title_right);
        searView.setOnSearchClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewVisible(R.id.title, View.GONE);
                setViewVisible(R.id.title_left, View.GONE);
            }
        });
        searView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(mTabHost.getCurrentTab() == 1) {
                    setViewVisible(R.id.title, View.VISIBLE);
                    setViewVisible(R.id.title_left, View.VISIBLE);
                }
                return false;
            }
        });
        searView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    ToastUtils.show(MainActivity.this, R.string.hint_order_search);
                    return false;
                }
                Intent intent = new Intent(MainActivity.this, OrderSearchListActivity.class);
                intent.putExtra(ParamConstants.KEYWORDS, query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void closeSearchTitle() {
        setViewVisible(R.id.title, View.VISIBLE);
        SearchView searView = (SearchView) findViewById(R.id.title_right);
        if(!searView.isIconified()) {
            searView.setQuery("", false);
            searView.setIconified(true);
        }

    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = mLayoutInflater.inflate(R.layout.tab_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setBackgroundResource(mTabInfoList.get(index).tabSelector);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTabInfoList.get(index).tabText);
        return view;
    }

    private DatePickerDialog mDatePickerDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                Calendar calendar = Calendar.getInstance();
                if (null == mDatePickerDialog) {
                    mDatePickerDialog = new DatePickerDialog(MainActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Intent intent = new Intent(MainActivity.this, OrderSearchListActivity.class);
                            StringBuilder date = new StringBuilder(String.valueOf(year));
                            if(++monthOfYear<10)
                                date.append("0");
                            date.append(monthOfYear);
                            if(dayOfMonth<10)
                                date.append("0");
                            date.append(dayOfMonth);
                            intent.putExtra(ParamConstants.DATE, date.toString());
                            startActivity(intent);
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                }
                if (mDatePickerDialog.isShowing())
                    return;
                else
                    mDatePickerDialog.show();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        AppUtils.startHome(this);
    }

    private void initLocation() {
        // init location
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_NAME);
        request.setInterval(0);
        mLocationManager = TencentLocationManager.getInstance(getApplicationContext());
        final int error = mLocationManager.requestLocationUpdates(request, this);
        switch (error) {
            case 0:
                break;
            default:
                mLocationManager.removeUpdates(this);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        try {
            mLocationManager.removeUpdates(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
