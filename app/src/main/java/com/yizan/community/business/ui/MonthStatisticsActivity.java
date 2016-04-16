package com.yizan.community.business.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.StatisticsMonth;
import com.fanwe.seallibrary.model.result.StatisticsMonthResult;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2015/10/30.
 */
public class MonthStatisticsActivity extends BaseActivity implements BaseActivity.TitleListener{

    protected boolean mLoadMore = false;
    private ListView mListView;
    private CommonAdapter<StatisticsMonth> mAdapter;
    private List<StatisticsMonth> list = new ArrayList<StatisticsMonth>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_statistics);
        setTitleListener(this);
        initViews();
        setPageTag(TagManage.MONTH_STATISTICS_ACTIVITY);
    }

    private void initViews() {
        mListView = mViewFinder.find(R.id.list_month_statictas);
        mAdapter = new CommonAdapter<StatisticsMonth>(this,list,R.layout.item_month_statistics) {
            @Override
            public void convert(ViewHolder helper, StatisticsMonth item, int position) {
                helper.setText(R.id.statistics_month,item.month+getString(R.string.month));
                helper.setText(R.id.statistics_year,item.year+getString(R.string.year));
                helper.setText(R.id.statistics_deals,getString(R.string.msg_finish_number, String.valueOf(item.num)));
                final String tempStr = getString(R.string.msg_accumulative_money_2);
                final String endStr = getString(R.string.yuan_2, String.valueOf(item.total));
                SpannableString spanText = new SpannableString(tempStr+endStr);
                spanText.setSpan(new ForegroundColorSpan(Color.RED),tempStr.length(),spanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.statistics_income,spanText);
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mViewFinder.find(android.R.id.empty));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MonthStatisticsActivity.this,MonthDetailActivity.class);
                intent.putExtra("month",String.valueOf(list.get(i).month));
                String month = String.valueOf(list.get(i).month);
                if(month.length() == 1){
                    month = "0" + month;
                }
                intent.putExtra("yearMonth",String.valueOf(list.get(i).year)+month);
                startActivity(intent);
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (!mLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount && mAdapter.getCount() >= Constants.PAGE_SIZE
                        && mAdapter.getCount() % Constants.PAGE_SIZE == 0) {
                    loadData();
                }
            }
        });
        loadData();
    }

    protected HashMap<String, String> getLoadParams() {
        HashMap<String, String> data = new HashMap<String, String>(1);
        data.put("page", String.valueOf(list.size() / Constants.PAGE_SIZE + 1));
        return data;
    }

    protected boolean checkLoadState() {
        if (mLoadMore) {
            return false;
        }
        if (!NetworkUtils.isNetworkAvaiable(MonthStatisticsActivity.this)) {
            ToastUtils.show(MonthStatisticsActivity.this, R.string.msg_error_network);
            return false;
        }
        mLoadMore = true;
        return true;
    }

    private void loadData(){
        if (!checkLoadState()) {
            return;
        }
        CustomDialogFragment.getInstance(getSupportFragmentManager(), MonthStatisticsActivity.class.getName());
        ApiUtils.post(MonthStatisticsActivity.this, URLConstants.STATISTICSMONTH, getLoadParams(), StatisticsMonthResult.class, new Response.Listener<StatisticsMonthResult>() {
            @Override
            public void onResponse(StatisticsMonthResult response) {
                CustomDialogFragment.dismissDialog();
                mLoadMore = false;
                if (O2OUtils.checkResponse(MonthStatisticsActivity.this, response)) {
                    if (!ArraysUtils.isEmpty(response.data)) {
                        list.addAll(response.data);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(MonthStatisticsActivity.this, R.string.msg_error);
                mLoadMore = false;
            }
        });



    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.msg_month_statistics));
    }


}
