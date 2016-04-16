package com.yizan.community.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.yizan.community.business.adapter.StaffListAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.StaffInfo;
import com.fanwe.seallibrary.model.result.StaffListResult;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务、配送人员ListActivity
 */
public class StaffListActivity extends BaseActivity implements BaseActivity.TitleListener {
    private StaffListAdapter mStaffListAdapter;
    private int mType = 1;
    public static final int REQUEST_CODE=101;
    private boolean mSingleChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);
        setTitleListener(this);
        Intent data = getIntent();
        mType = data.getIntExtra(Constants.EXTRA_DATA, 1);
        mSingleChoice=data.getBooleanExtra(Constants.EXTRA_CHOICE_MODE,true);
        mStaffListAdapter = new StaffListAdapter(this, new ArrayList<StaffInfo>());
        ListView lv = mViewFinder.find(R.id.lv_list);
        lv.setAdapter(mStaffListAdapter);
        lv.setEmptyView(mViewFinder.find(android.R.id.empty));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSingleChoice) {
                    mStaffListAdapter.selSignalItem(i, !mStaffListAdapter.getItem(i).bSel);
                } else {
                    mStaffListAdapter.selItem(i, !mStaffListAdapter.getItem(i).bSel);
                }
                mStaffListAdapter.notifyDataSetChanged();
            }
        });
        loadData();
        setPageTag(TagManage.STAFF_LIST_ACTIVITY);
    }


    @Override
    public void setTitle(TextView title, final ImageButton left, View right) {
        title.setText(R.string.title_activity_staff_list);
        right.setVisibility(View.VISIBLE);
        ((Button) right).setText(R.string.select);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<StaffInfo> list = mStaffListAdapter.getSelList();
                if (ArraysUtils.isEmpty(list)) {
                    ToastUtils.show(StaffListActivity.this, R.string.msg_error_select);
                    return;
                }
                Intent intent = new Intent();
                if (mSingleChoice) {
                    intent.putExtra(Constants.EXTRA_DATA, list.get(0).id);
                } else {
                    intent.putExtra(Constants.EXTRA_DATA, list);
                }
                setResult(Activity.RESULT_OK, intent);
                finishActivity();
            }
        });
    }

    private void loadData() {
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("type", mType);
        CustomDialogFragment.getInstance(this.getSupportFragmentManager(), this.getClass().getName());
        ApiUtils.post(this, URLConstants.STAFF_LIST, params,
                StaffListResult.class, new Response.Listener<StaffListResult>() {

                    @Override
                    public void onResponse(StaffListResult response) {
                        if (O2OUtils.checkResponse(
                                StaffListActivity.this,
                                response)) {
                            if (!ArraysUtils.isEmpty(response.data)) {
                                mStaffListAdapter.addAll(response.data);
                            }
                        }
                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(StaffListActivity.this, R.string.msg_error_network);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }
}
