package com.yizan.community.business.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tu.upgrade.DownloadCompleteReceiver;
import com.tu.upgrade.UpgradeManager;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.model.ConfigInfo;
import com.yizan.community.business.ui.BaseActivity.TitleListener;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.PushUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.DeviceUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

/**
 * 设置Activity
 */
public class SettingActivity extends BaseActivity implements TitleListener, OnClickListener {

    private ConfigInfo mConfig;
    private DownloadCompleteReceiver mDownloadCompleteReceiver;
    private TextView versionText;
    private Button msgSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitleListener(this);
        msgSettings = mViewFinder.find(R.id.msg_switch_btn);
        msgSettings.setOnClickListener(this);
        if (PreferenceUtils.getValue(this,"push_open",true)){
            msgSettings.setBackgroundResource(R.drawable.btn_on);
        }else{
            msgSettings.setBackgroundResource(R.drawable.btn_off);
        }
        mConfig = PreferenceUtils.getObject(this, ConfigInfo.class);
        mViewFinder.find(R.id.setting_about_us).setOnClickListener(this);
//        mViewFinder.find(R.id.setting_version).setOnClickListener(this);
//        mViewFinder.find(R.id.setting_bind).setOnClickListener(this);
//        mViewFinder.find(R.id.setting_password_update).setOnClickListener(this);
        mViewFinder.find(R.id.personal_logout).setOnClickListener(this);
        mViewFinder.find(R.id.version_detection).setOnClickListener(this);
        versionText = mViewFinder.find(R.id.version_detection_text);
        versionText.setText(getString(R.string.msg_vertion_hint) + mConfig.appVersion);
        mViewFinder.find(R.id.feed_back).setOnClickListener(this);
//        SlideButton slideButton = mViewFinder.find(R.id.setting_order_slidebutton);
//        slideButton.setStateChanageListener(new SlideButton.StateChanageListener() {
//            @Override
//            public void stateChange(SlideButton sb) {
//                // 接单
//                orderAccept(sb.isOn() ? "0" : "1");
//            }
//        });
        setPageTag(TagManage.SETTING_ACTIVITY);
    }

//    private void orderAccept(String status) {
//        if (!NetworkUtils.isNetworkAvaiable(this)) {
//            ToastUtils.show(this, R.string.msg_error_network);
//            return;
//        }
//        HashMap<String, String> param = new HashMap<String, String>(1);
//        param.put(ParamConstants.STATUS, status);
//        ApiUtils.post(this, URLConstants.USER_ORDER_ACCEPT, param, BaseResult.class, new Response.Listener<BaseResult>() {
//            @Override
//            public void onResponse(BaseResult response) {
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.setting_bind:
//                startActivity(new Intent(SettingActivity.this,BindCheckMobileActivity.class));
//                break;
//            case R.id.setting_password_update:
//                //// FIXME: 15/10/15 修改密码
//                break;
            case R.id.setting_about_us:
                if (null != mConfig && !TextUtils.isEmpty(mConfig.aboutUrl)) {
                    Intent intent = new Intent(SettingActivity.this, WebViewActivity.class);
                    intent.putExtra(Constants.EXTRA_URL, mConfig.aboutUrl);
                    startActivity(intent);
                }
                break;
            case R.id.version_detection:
                versionUpgrade();
                break;
            case R.id.personal_logout:
                logout();
                break;
            case R.id.feed_back:
                startActivity(new Intent(SettingActivity.this, FeedBackActivity.class));
                break;
            case R.id.msg_switch_btn:
                boolean isPushOpen = isPushOpen();
                PushUtils.isPush(this, !isPushOpen);
                initPushSwitchView(!isPushOpen);
                break;
        }
    }

    private void logout() {
        Dialog alertDialog = new AlertDialog.Builder(SettingActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                .setMessage(R.string.msg_tip_logout)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                ToastUtils.show(SettingActivity.this,
                                        R.string.msg_success_logout);
                                O2OUtils.reidrectLogin(SettingActivity.this);
                                PushUtils.initAlias(getApplicationContext());
                                finishActivity();
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        }).create();
        alertDialog.show();
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.setting);
    }

    private void versionUpgrade() {

        if (null != mConfig
                && !TextUtils.isEmpty(mConfig.appVersion)
                && !TextUtils.isEmpty(mConfig.appDownUrl)
                && contrastVertion(mConfig.appVersion)) {
            AlertDialog.Builder db = new AlertDialog.Builder(this,
                    AlertDialog.THEME_HOLO_LIGHT)
                    .setTitle(R.string.upgrade)
                    .setMessage(mConfig.upgradeInfo)
                    .setPositiveButton(R.string.upgrade_now,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    long downloadId = UpgradeManager.updgrade(
                                            SettingActivity.this,
                                            Uri.parse(mConfig.appDownUrl),
                                            getString(R.string.app_name)
                                                    + mConfig.appVersion,
                                            mConfig.upgradeInfo);
                                    mDownloadCompleteReceiver = new DownloadCompleteReceiver(
                                            downloadId);
                                    SettingActivity.this
                                            .registerReceiver(
                                                    mDownloadCompleteReceiver,
                                                    new IntentFilter(
                                                            DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                }
                            });
            if (!mConfig.forceUpgrade)
                db.setNegativeButton(R.string.upgrade_ignore,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
            db.setCancelable(mConfig.forceUpgrade);
            db.create().show();
        } else
            ToastUtils.show(this, R.string.msg_error_last_version);
    }

    private boolean contrastVertion(String newVer) {
        boolean bNeed = false;
        try {
            if (TextUtils.isEmpty(newVer)) {
                return bNeed;
            }
            float newVersion = Float.parseFloat(newVer);
            float currVersion = Float.parseFloat(DeviceUtils.getPackageInfo(this).versionName);
            if(newVersion > currVersion){
                bNeed = true;
            }

        } catch (Exception e) {

        }
        return bNeed;
    }
    private void initPushSwitchView(boolean bOpen){
        PreferenceUtils.setValue(this, "push_open", bOpen);
        if (!bOpen){
            msgSettings.setBackgroundResource(R.drawable.btn_off);
        }else {
            msgSettings.setBackgroundResource(R.drawable.btn_on);
        }
    }

    private boolean isPushOpen(){
        return PreferenceUtils.getValue(this, "push_open", true);
    }

}
