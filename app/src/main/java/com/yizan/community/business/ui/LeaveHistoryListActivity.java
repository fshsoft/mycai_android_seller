package com.yizan.community.business.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.yizan.community.business.adapter.LeaveHistoryAdapter;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.StaffLeaveInfo;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fanwe.seallibrary.model.result.StaffLeaveResult;
import com.yizan.community.business.ui.BaseActivity.TitleListener;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请假列表ListActivity
 */
public class LeaveHistoryListActivity extends BaseActivity implements
        TitleListener{

    public static final int PAGE_SIZE = 20;
    protected boolean mLoadMore = false;
    private LeaveHistoryAdapter adapter;
    private ListView leave_history_listvie;
    private int page = 1;
    private List<StaffLeaveInfo> list;
    private View mEmptyView;
    private PopupWindow popupWindow;
    private TextView mDelete;
    private List<Integer> listParams = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_history);
        setTitleListener(this);
        initView();
        loaddata();
        setPageTag(TagManage.LEAVE_HISTORY_LIST_ACTIVITY);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.time_leave_txt);
    }

    private void initView() {
        leave_history_listvie = (ListView) findViewById(R.id.leave_history_listvie);
        list = new ArrayList<StaffLeaveInfo>();
        adapter = new LeaveHistoryAdapter(LeaveHistoryListActivity.this,list);
        mEmptyView = findViewById(android.R.id.empty);
        leave_history_listvie.setEmptyView(mEmptyView);
        leave_history_listvie.setAdapter(adapter);

		leave_history_listvie.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                initPop(view,i);
                return false;
            }
        });

        leave_history_listvie.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (!mLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount && adapter.getCount() >= PAGE_SIZE && adapter.getCount() % PAGE_SIZE == 0) {
                    mLoadMore = true;
                    page += 1;
                    loaddata();
                }
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
            popupWindow = null;
        }

        return super.onTouchEvent(event);
    }

    private void initPop(View view, final int position){
        View convertView = LayoutInflater.from(LeaveHistoryListActivity.this).inflate(R.layout.pop_delete,null);
        mDelete = (TextView)convertView.findViewById(R.id.pop_delete);
        mDelete.setText(getString(R.string.delete));
        convertView.setBackgroundResource(R.drawable.style_edt_boder);
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth()/3;
        popupWindow = new PopupWindow(convertView,width, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int xPos = -popupWindow.getWidth() / 2;
        popupWindow.showAsDropDown(view, xPos, 4);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData(list.get(position).id);
                popupWindow.dismiss();
            }
        });
    }


    private void deleteData(int id) {

        if (!NetworkUtils.isNetworkAvaiable(LeaveHistoryListActivity.this)) {
            ToastUtils.show(LeaveHistoryListActivity.this,
                    R.string.msg_error_network);
            return;
        }
        listParams.add(id);
        HashMap<String,List<Integer>> data = new HashMap<String,List<Integer>>();
        data.put("ids",listParams);
        ApiUtils.post(LeaveHistoryListActivity.this,
                URLConstants.STAFFLEAVE_DELETE, data,
                BaseResult.class, new Listener<BaseResult>() {

                    @Override
                    public void onResponse(BaseResult response) {
                        CustomDialogFragment.dismissDialog();
                        if (O2OUtils.checkResponse(LeaveHistoryListActivity.this, response)) {
                            if (!TextUtils.isEmpty(response.msg)) {
                                ToastUtils.show(LeaveHistoryListActivity.this, response.msg);
                            }
                            page = 1;
                            loaddata();
                        }
                    }

                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomDialogFragment.dismissDialog();
                    }

                });


    }

    private Map<String, String> getParams() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("page", page + "");

        return map;
    }

    private void loaddata() {

        if (NetworkUtils.isNetworkAvaiable(LeaveHistoryListActivity.this)) {


            ApiUtils.post(LeaveHistoryListActivity.this,
                    URLConstants.STAFFLEAVE_LISTS, getParams(),
                    StaffLeaveResult.class, new Listener<StaffLeaveResult>() {

                        @Override
                        public void onResponse(StaffLeaveResult response) {
                            mLoadMore = false;
                            if (!ArraysUtils.isEmpty(response.data)){
                                list.clear();
                                list.addAll(response.data);
                            }


                            adapter.notifyDataSetChanged();
                        }

                    }, new ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ToastUtils.show(LeaveHistoryListActivity.this, R.string.msg_error_del);
                        }

                    });

        } else {
            ToastUtils.show(LeaveHistoryListActivity.this,
                    R.string.msg_error_network);
        }

    }

}
