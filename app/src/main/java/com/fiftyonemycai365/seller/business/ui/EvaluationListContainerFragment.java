package com.fiftyonemycai365.seller.business.ui;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.viewpagerindicator.TabPageIndicator;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.TabEvaluationListFragmentPagerAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.StaffResultInfo;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.zongyou.library.app.BaseFragment;
import com.zongyou.library.util.NumberUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.volley.RequestManager;

import java.util.HashMap;

/**
 * 评价列表容器Fragment
 *
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-19 下午9:15:07
 */
public class EvaluationListContainerFragment extends BaseFragment implements BaseActivity.TitleListener {
    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;
    private TabEvaluationListFragmentPagerAdapter mAdapter;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_evluation_list_container, container, false);
    }

    @Override
    protected void initView() {
        NetworkImageView image = mViewFinder.find(R.id.evluation_head_image);
        image.setDefaultImageResId(R.drawable.ic_default_head);
        image.setErrorImageResId(R.drawable.ic_default_head);
        UserInfo user = PreferenceUtils.getObject(mFragmentActivity, UserInfo.class);
        image.setImageUrl(user.avatar, RequestManager.getImageLoader());
        ((TextView) mViewFinder.find(R.id.evaluation_head_name)).setText(user.name);
        mIndicator = mViewFinder.find(R.id.indicator);
        mViewPager = mViewFinder.find(R.id.viewpager);
        mAdapter = new TabEvaluationListFragmentPagerAdapter(getActivity(),getFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);

        UserInfo user11 = PreferenceUtils.getObject(getActivity(), UserInfo.class);
        int staffid = user11.id;
        int userid = user11.id;
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("staffId", staffid + "");
        data.put("userId", userid + "");

        ApiUtils.post(mFragmentActivity, URLConstants.EVALUATION_STATISTICS, data, StaffResultInfo.class, new Response.Listener<StaffResultInfo>() {

            @Override
            public void onResponse(StaffResultInfo response) {
                if (O2OUtils.checkResponse(mFragmentActivity, response)) {
                    if (null != response.data) {
                        Log.i("result", response.data.extend.commentTotalCount + "----------------");
                        if (response.data.extend.commentTotalCount != 0) {

                            ((TextView) mViewFinder.find(R.id.eva_good_number)).setText(O2OUtils.numberLengthFormat(
                                    NumberUtils.getPercent(response.data.extend.commentGoodCount, response.data.extend.commentTotalCount, Constants.PATTERN_PERCENT_EVALUATION), 7));
                            ((TextView) mViewFinder.find(R.id.eva_nes_number)).setText(O2OUtils.numberLengthFormat(
                                    NumberUtils.getPercent(response.data.extend.commentNeutralCount, response.data.extend.commentTotalCount, Constants.PATTERN_PERCENT_EVALUATION), 7));
                            ((TextView) mViewFinder.find(R.id.eva_bad_number)).setText(O2OUtils.numberLengthFormat(
                                    NumberUtils.getPercent(response.data.extend.commentBadCount, response.data.extend.commentTotalCount, Constants.PATTERN_PERCENT_EVALUATION), 7));

                            ((ProgressBar) mViewFinder.find(R.id.evluation_progress_good)).setProgress((int) (((float) response.data.extend.commentGoodCount)
                                    / response.data.extend.commentTotalCount * 100));
                            ((ProgressBar) mViewFinder.find(R.id.evluation_progress_bad)).setProgress((int) (((float) response.data.extend.commentBadCount)
                                    / response.data.extend.commentTotalCount * 100));
                            ((ProgressBar) mViewFinder.find(R.id.evluation_progress_nes)).setProgress((int) (((float) response.data.extend.commentNeutralCount)
                                    / response.data.extend.commentTotalCount * 100));

                        }
                        mAdapter.setTitles(new String[]{getActivity().getString(R.string.all_number,response.data.extend.commentTotalCount),
                                getActivity().getString(R.string.good_evaluate,response.data.extend.commentGoodCount),
                                getActivity().getString(R.string.middle_evaluate, response.data.extend.commentNeutralCount),
                                getActivity().getString(R.string.bad_evaluate,response.data.extend.commentBadCount)});
                        mIndicator.notifyDataSetChanged();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.show(mFragmentActivity, R.string.msg_error);
            }
        });
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getActivity().getString(R.string.msg_user_comment));
    }
}
