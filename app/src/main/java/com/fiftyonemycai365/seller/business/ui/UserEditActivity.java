package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.OSSException;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.UserResultInfo;
import com.tu.crop.CropHandler;
import com.tu.crop.CropHelper;
import com.tu.crop.CropParams;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.ui.BaseActivity.TitleListener;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.OSSUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.AppUtils;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.StringUtil;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.volley.RequestManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 账号管理Activity
 *
 * @author atlas
 * @time 2015-3-23上午11:17:39
 */
public class UserEditActivity extends BaseActivity implements TitleListener, OnClickListener, CropHandler {
    Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String obj = msg.obj.toString();
            if (msg.what == -1) {
                ToastUtils.show(UserEditActivity.this, R.string.msg_failed_update + obj);
            } else if (msg.what == -2) {
                update(obj);
            }

        }
    };
    private NetworkImageView image;
    private UserInfo mUser;
    private ImageSwitcherPopupWindow mPopupWinddow;
    private CropParams mCropParams;
    private TextView my_name_text,my_mobile_text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        setTitleListener(this);
        initView();
        mUser = PreferenceUtils.getObject(this, UserInfo.class);
        my_name_text.setText(mUser.name);
        mViewFinder.find(R.id.changed_pwd).setOnClickListener(this);
        mViewFinder.find(R.id.changed_mobile).setOnClickListener(this);
        my_mobile_text.setText(mUser.mobile);
        image.setImageUrl(mUser.avatar, RequestManager.getImageLoader());
        OSSUtils.init(getApplicationContext());
        setPageTag(TagManage.USER_EDIT_ACTIVITY);
    }


    private void initView() {
        my_name_text = mViewFinder.find(R.id.my_name_text);
        my_mobile_text = mViewFinder.find(R.id.my_mobile_text);
        mCropParams = new CropParams();
        mCropParams.outputX = 200;
        mCropParams.outputY = 200;
        image = (NetworkImageView) findViewById(R.id.head);
        image.setOnClickListener(this);
        mViewFinder.find(R.id.head_ll).setOnClickListener(this);
    }


    private void update(String thumb) {
        if (NetworkUtils.isNetworkAvaiable(UserEditActivity.this)) {
            Map<String, String> map = new HashMap<String, String>();
            if (StringUtil.hasText(thumb)) {
                map.put("avatar", thumb);
            }else{
                map.put("avatar", mUser.avatar);
            }
            ApiUtils.post(getApplicationContext(), URLConstants.STAFF_UPDATE, map, UserResultInfo.class, new Response.Listener<UserResultInfo>() {
                @Override
                public void onResponse(UserResultInfo response) {
                    CustomDialogFragment.dismissDialog();
                    if (O2OUtils.checkResponse(UserEditActivity.this, response)) {
                        PreferenceUtils.setObject(UserEditActivity.this, response.data);
                        finishActivity();
                    }
                }

            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomDialogFragment.dismissDialog();
                    ToastUtils.show(UserEditActivity.this, R.string.msg_error_save);

                }
            });
        } else {
            ToastUtils.show(UserEditActivity.this, R.string.msg_error_network);
        }
    }

    @Override
    public void onClick(View view) {
        AppUtils.hideSoftInput(UserEditActivity.this);
        switch (view.getId()) {
            case R.id.changed_mobile:
                startActivity(new Intent(UserEditActivity.this,BindCheckMobileActivity.class));
                break;
            case R.id.changed_pwd:
                Intent intent = new Intent(UserEditActivity.this,ChangedPasswordActivity.class);
                intent.putExtra("biaozhi",1);
                startActivity(intent);
                break;
            case R.id.head_ll:
            case R.id.head:
                if (null == mPopupWinddow) {
                mPopupWinddow = new ImageSwitcherPopupWindow(UserEditActivity.this, UserEditActivity.this, UserEditActivity.class.getName());
            }
                mPopupWinddow.show(view);
                break;
            case R.id.photograph:
                mPopupWinddow.dismiss();
                startActivityForResult(CropHelper.buildCaptureIntent(mCropParams.uri), CropHelper.REQUEST_CAMERA);
                break;
            case R.id.photo_album:
                mPopupWinddow.dismiss();
                startActivityForResult(CropHelper.buildGalleryIntent(), CropHelper.REQUEST_GALLERY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case CropHelper.REQUEST_CROP:
            case CropHelper.REQUEST_CAMERA:
            case CropHelper.REQUEST_GALLERY:
                CropHelper.handleResult(this, requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        image.setImageURI(uri);
        image.setTag(uri.getPath());
    }

    @Override
    public void onCropCancel() {

    }

    @Override
    public void onCropFailed(String message) {

    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.user_edit_title);
        ((TextView)right).setText("保存");
        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image.getTag() != null) {
                    CustomDialogFragment.getInstance(getSupportFragmentManager(), UserEditActivity.class.getName());
                    OSSUtils.save(image.getTag().toString(), new SaveCallback() {
                        @Override
                        public void onProgress(String arg0, int arg1, int arg2) {
                        }

                        @Override
                        public void onFailure(String arg0, OSSException arg1) {
                            Message message = new Message();
                            message.what = -1;
                            message.obj = arg0 + arg1.getMessage();
                            hander.sendMessage(message);
                            CustomDialogFragment.dismissDialog();

                        }

                        @Override
                        public void onSuccess(String arg0) {
                            Message message = new Message();
                            message.what = -2;
                            message.obj = arg0;
                            hander.sendMessage(message);
                        }
                    });

                } else {
                    update("");
                }
            }
        });
    }
}
