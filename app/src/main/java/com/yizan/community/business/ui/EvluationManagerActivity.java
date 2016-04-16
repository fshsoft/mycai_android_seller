package com.yizan.community.business.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.EvaPackInfo;
import com.fanwe.seallibrary.model.EvaPackResult;
import com.yizan.community.business.event.CommentEvent;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.TagManage;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.app.FragmentUtil;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;

/**
 * Created by admin on 2015/11/4.
 * 评价管理 activity
 */
public class EvluationManagerActivity extends BaseActivity implements BaseActivity.TitleListener ,View.OnClickListener{

    private RelativeLayout mAll,mBad;
    private TextView mAllText,mBadText,mScoreText;
    private FragmentManager fragmentManager;
    private AllEvaluationFragment allEvaluationFragment;
    private BadEvaluationFragment badEvaluationFragment;
    private RatingBar rating_bar;
    private boolean mIsLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_evluation_manager);
        setTitleListener(this);

        initViews();
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
        EventBus.getDefault().register(this);
        setPageTag(TagManage.EVLUATION_MANAGER_ACTIVITY);
    }

    private void initViews() {
        rating_bar = mViewFinder.find(R.id.rating_bar);
        mAll = mViewFinder.find(R.id.rl_all_evluation);
        mAll.setOnClickListener(this);
        mBad = mViewFinder.find(R.id.rl_no_reply);
        mBad.setOnClickListener(this);
        mAllText = mViewFinder.find(R.id.all_evluation);
        mBadText = mViewFinder.find(R.id.no_reply);
        mScoreText = mViewFinder.find(R.id.total_score_text);
        getData();
    }

    private void setTabSelection(int index){
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index){
            case 0:
                FragmentUtil.turnToFragment(getSupportFragmentManager(),R.id.frame_content,AllEvaluationFragment.class,null,false);
                mAll.setBackgroundResource(R.drawable.cornor_border_theme_meichandise);
                mAllText.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                FragmentUtil.turnToFragment(getSupportFragmentManager(),R.id.frame_content,BadEvaluationFragment.class,null,false);
                mBad.setBackgroundResource(R.drawable.cornor_border_theme_shop);
                mBadText.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void clearSelection(){
        mAll.setBackgroundResource(R.drawable.cornor_border);
        mAllText.setTextColor(getResources().getColor(R.color.bgTheme));
        mBad.setBackgroundResource(R.drawable.cornor_border);
        mBadText.setTextColor(getResources().getColor(R.color.bgTheme));
    }

    private void hideFragment(FragmentTransaction transaction){
        if (allEvaluationFragment != null){
            transaction.hide(allEvaluationFragment);
        }
        if (badEvaluationFragment != null){
            transaction.hide(badEvaluationFragment);
        }
    }

    private void getData(){
        if(mIsLoading){
            return;
        }
        HashMap<String,String> data = new HashMap<String,String>();
        data.put("type",String.valueOf(1));
        data.put("page", String.valueOf(1));
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            return;
        }
        mIsLoading = true;
        ApiUtils.post(EvluationManagerActivity.this, URLConstants.SELLER_EVALIST, data, EvaPackResult.class, new Response.Listener<EvaPackResult>() {

            @Override
            public void onResponse(EvaPackResult response) {
                mIsLoading = false;
                if (response.data != null){
                    initData(response.data);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mIsLoading = false;
                ToastUtils.show(EvluationManagerActivity.this,R.string.msg_error);
            }
        });
    }

    private void initData(EvaPackInfo eva) {
        mAllText.setText(getString(R.string.msg_not_reply,String.valueOf(eva.unReply)));
        mBadText.setText(getString(R.string.msg_yet_reply, String.valueOf(eva.reply)));
        mScoreText.setText(String.valueOf(eva.score));
        rating_bar.setRating(eva.score);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.msg_comment_manage));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_all_evluation:
                setTabSelection(0);
                break;
            case R.id.rl_no_reply:
                setTabSelection(1);
                break;
        }
    }

    public void onEventMainThread(CommentEvent event){
        getData();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
