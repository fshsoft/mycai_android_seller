package com.yizan.community.business.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.ParamConstants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.MessageInfo;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fanwe.seallibrary.model.result.MessageListResult;
import com.yizan.community.business.ui.BaseActivity.TitleListener;
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
 * 消息中心Activity
 *
 * @author atlas
 * @email atlas.tufei@gmail.com
 * @time 2015-5-15 下午8:22:35
 */
public class InformCenterActivity extends BaseActivity implements TitleListener {
    protected boolean mLoadMore;
    private ListView mListView;
    private List<MessageInfo> mData = new ArrayList<MessageInfo>();
    private CommonAdapter<MessageInfo> mAdapter;
    private TextView mDelete;
    private PopupWindow popupWindow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);
        initView();
        setPageTag(TagManage.INFORM_CENTER_ACTIVITY);
    }

    private void initView() {
        setTitleListener(this);
        mAdapter = new CommonAdapter<MessageInfo>(InformCenterActivity.this, mData, R.layout.item_inform_center) {

            @Override
            public void convert(ViewHolder helper, MessageInfo item, int position) {
                helper.setText(R.id.msg_title_tv, item.title);
                helper.setText(R.id.msg_content_tv, Html.fromHtml(item.content));
                helper.setText(R.id.msg_date_tv, item.createTime);
                if (item.isRead == 1){
                    helper.getView(R.id.msg_point).setVisibility(View.GONE);
                }else{
                    helper.getView(R.id.msg_point).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

        };
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setEmptyView(findViewById(android.R.id.empty));
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                msgRead(mData.get(position).id);
                MessageInfo info = (MessageInfo) parent.getItemAtPosition(position);
                HashMap<String, Object> param = new HashMap<String, Object>(1);
                long[] ids = new long[]{info.id};
                param.put(ParamConstants.ID, ids);
                msgService(param, 4, false);
                mData.get(position).isRead = 0;
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent();
                switch (info.type) {
                    case 1:
                        intent.setClass(InformCenterActivity.this, InformDetailsActivity.class);
                        intent.putExtra("id", info.id);
                        intent.putExtra("title", info.title);
                        intent.putExtra("date", info.createTime);
                        intent.putExtra("content", info.content);
                        break;
                    case 2:
                        intent.setClass(InformCenterActivity.this, WebViewActivity.class);
                        intent.putExtra("url", info.args);
                        break;
                    case 3:
                        intent.putExtra(Constants.ORDERID, Integer.parseInt(info.args));
                        intent.setClass(InformCenterActivity.this, OrderDetailsActivity.class);
                        break;
                    case 4:
                        intent.putExtra("id", info.args);
                        intent.setClass(InformCenterActivity.this, CommissionListActivity.class);
                        break;
                    default:
                        return;
                }
                startActivity(intent);
            }
        });
        mListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!mLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount && mData.size() >= Constants.PAGE_SIZE && mData.size() % Constants.PAGE_SIZE == 0) {
                    loadList(false);
                    mLoadMore = true;
                }

            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                initPop(view,i);
                return true;
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
        View convertView = LayoutInflater.from(InformCenterActivity.this).inflate(R.layout.pop_delete,null);
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
                msgDelete(mData.get(position).id);
                popupWindow.dismiss();
            }
        });
    }

    //消息阅读
    private void msgRead(int id){
        List<Integer> integerList = new ArrayList<>();
        integerList.add(id);
        HashMap<String,List<Integer>> data = new HashMap<String,List<Integer>>();
        data.put("id", integerList);
        ApiUtils.post(InformCenterActivity.this, URLConstants.MESSAGE_READ, data, BaseResult.class, new Response.Listener<BaseResult>() {
            @Override
            public void onResponse(BaseResult response) {

            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.show(InformCenterActivity.this, R.string.msg_error);
            }
        });
    }

    //消息删除
    private void msgDelete(int id){
        List<Integer> integerList = new ArrayList<>();
        integerList.add(id);
        HashMap<String,List<Integer>> data = new HashMap<String,List<Integer>>();
        data.put("id",integerList);
        ApiUtils.post(InformCenterActivity.this, URLConstants.MESSAGE_DELETE, data, BaseResult.class, new Response.Listener<BaseResult>() {
            @Override
            public void onResponse(BaseResult response) {
                loadList(true);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.show(InformCenterActivity.this,R.string.msg_error);
            }
        });
    }

    private void loadList(final boolean isRefresh) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(ParamConstants.PAGE, String.valueOf(isRefresh ? 1 : (mData.size() / Constants.PAGE_SIZE + 1)));
        msgService(map, 0, isRefresh);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.inform_center_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadList(true);
    }

    private void msgService(HashMap<String, Object> param, final int type, final boolean isRefresh) {
        if (NetworkUtils.isNetworkAvaiable(InformCenterActivity.this)) {
            if (type != 4)
                CustomDialogFragment.getInstance(getSupportFragmentManager(), InformCenterActivity.class.getName());
            String url = URLConstants.MSG_LIST;
            ApiUtils.post(InformCenterActivity.this, url, param, MessageListResult.class, new Response.Listener<MessageListResult>() {
                @Override
                public void onResponse(MessageListResult response) {
                    CustomDialogFragment.dismissDialog();
                    mListView.clearChoices();
                    if (!isRefresh)
                        mLoadMore = false;
                    if (O2OUtils.checkResponse(InformCenterActivity.this, response)) {
                        if (isRefresh) {
                            mData.clear();
                        }
                        if (!ArraysUtils.isEmpty(response.data)) {

                            mData.addAll(response.data);
                        } else {
                            ToastUtils.show(InformCenterActivity.this, R.string.common_not_more);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }

            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomDialogFragment.dismissDialog();
                    if (type == 0)
                        if (isRefresh)
                            mAdapter.notifyDataSetChanged();
                        else {
                            mLoadMore = false;
                        }

                }

            });
        } else
            ToastUtils.show(InformCenterActivity.this, R.string.msg_error_network);
    }

    class MsgOnCheckedChangeListener implements OnCheckedChangeListener {
        private int mPosition;

        public MsgOnCheckedChangeListener(int position) {
            mPosition = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mListView.setItemChecked(mPosition, isChecked);
        }
    }
}
