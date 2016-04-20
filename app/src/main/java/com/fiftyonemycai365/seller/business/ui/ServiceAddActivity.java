package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.OSSException;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.tu.crop.CropHelper;
import com.tu.crop.CropParams;
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fiftyonemycai365.seller.business.event.GoodOpEvent;
import com.fanwe.seallibrary.model.Goods;
import com.fanwe.seallibrary.model.GoodsCate;
import com.fanwe.seallibrary.model.StaffInfo;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.ImgUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.OSSUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.volley.RequestManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务添加Activity
 */
public class ServiceAddActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {

    private Goods mGoods;
    private GoodsCate mGoodsCate;

    private ImageSwitcherPopupWindow mPopupWinddow;
    private CropParams mCropParams;
    private NetworkImageView mHeadImage;
    private List<String> mTmpImgs = new ArrayList<>();

    private Goods mTmpGoods;
    private ArrayList<Integer> mStaffIds;

    private String mCurrGoodBiref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_add);
        OSSUtils.init(getApplicationContext());
        setTitleListener(this);
        Object obj = this.getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        if (null == obj) {
            finishActivity();
            return;
        }
        if (obj instanceof Goods) {
            mGoods = (Goods) obj;
            initViewData();
        } else if (obj instanceof GoodsCate) {
            mGoodsCate = (GoodsCate) obj;
        }

        initView();
        setPageTag(TagManage.SERVICE_ADD_ACTIVITY);
    }

    private void initView() {
        mCropParams = new CropParams();
        mCropParams.outputX = 720;
        mCropParams.outputY = 1024;

        EditText et = mViewFinder.find(R.id.et_price);
        et.setFilters(new InputFilter[] { lengthfilter });

        mHeadImage = mViewFinder.find(R.id.iv_img);
        mViewFinder.onClick(R.id.rl_pull, this);
        mViewFinder.onClick(R.id.rl_remove, this);
        mViewFinder.onClick(R.id.rl_preview, this);
        mViewFinder.onClick(R.id.rl_add_head, this);
        mViewFinder.onClick(R.id.tv_worker, this);
        mViewFinder.onClick(R.id.ll_good_brief, this);
        if (mGoods == null) {
            mViewFinder.find(R.id.rl_pull).setVisibility(View.GONE);
            mViewFinder.find(R.id.rl_remove).setVisibility(View.GONE);
            initViewHead("");
        } else {
            initViewHead(mGoods.imgs.get(0));
        }
    }

    private void initViewHead(String img) {
        if(TextUtils.isEmpty(img)){
            mViewFinder.find(R.id.tv_sale_state).setVisibility(View.INVISIBLE);
            mViewFinder.find(R.id.tv_add_pic_notice).setVisibility(View.INVISIBLE);
            return;
        }
        mViewFinder.find(R.id.tv_add_pic_notice).setVisibility(View.VISIBLE);
        NetworkImageView iv = mViewFinder.find(R.id.iv_img);
        if(img.contains("http")){
            iv.setImageUrl(img, RequestManager.getImageLoader());
        }else{
            iv.setImageURI(Uri.fromFile(new File(img)));
            iv.setTag(img);
        }
        if(mGoods != null){
            initOnlineState(mGoods.status);
        }else{
            mViewFinder.find(R.id.tv_sale_state).setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.msg_server_add));
        right.setVisibility(View.VISIBLE);
        ((Button) right).setText(getString(R.string.complete));
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });
    }

    private void initGoodBrief(String html) {
        mCurrGoodBiref = TextUtils.isEmpty(html) ? "" : html;
        WebView webView = mViewFinder.find(R.id.wv_brief);
        webView.loadData(mCurrGoodBiref, "text/html; charset=UTF-8", null);
    }
    private void initViewData() {
        if (mGoods == null) {
            return;
        }
        EditText et = mViewFinder.find(R.id.et_name);
        et.setText(mGoods.name);
        et = mViewFinder.find(R.id.et_time);
        et.setText(String.valueOf(mGoods.duration));
        et = mViewFinder.find(R.id.et_price);
        et.setText(String.valueOf(mGoods.price));

        initGoodBrief(mGoods.brief);

        if (!ArraysUtils.isEmpty(mGoods.imgs)) {
            NetworkImageView iv = mViewFinder.find(R.id.iv_img);
            iv.setImageUrl(mGoods.imgs.get(0), RequestManager.getImageLoader());
        }

        updateStaffList(mGoods.staff);
    }

    private void updateStaffList(List<StaffInfo> list){
        if(ArraysUtils.isEmpty(list)){
           return;
        }
        String names = "";

        mStaffIds = new ArrayList<>();
        for (StaffInfo item : list){
            mStaffIds.add(item.id);
            if(!TextUtils.isEmpty(names)){
                names += ",";
            }
            names += item.name;
        }
        mViewFinder.setText(R.id.tv_worker, names);
    }

    @Override
    public void onClick(View view) {
        hideSoftInputView();
        switch (view.getId()) {
            case R.id.rl_add_head:
                if (null == mPopupWinddow) {
                    mPopupWinddow = new ImageSwitcherPopupWindow(this, this, ServiceAddActivity.class.getName());
                }
                mPopupWinddow.show(view);
                break;
            case R.id.rl_remove:
                goodsOp(3);
                break;
            case R.id.rl_pull:
                goodsOp(mGoods.status == 1?2:1);
                break;
            case R.id.rl_preview:
                Goods goods = createGoods();
                if(null != goods){
                    Intent intent = new Intent(this, GoodDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_DATA, goods);
                    startActivity(intent);
                }
                break;
            case R.id.photograph:
                mPopupWinddow.dismiss();
                mTmpImgs.add(mCropParams.uri.getPath());
                startActivityForResult(CropHelper.buildCaptureIntent(mCropParams.uri), CropHelper.REQUEST_CAMERA);
                break;
            case R.id.photo_album:
                mPopupWinddow.dismiss();
                Intent intent = ImgUtils.buildGalleryIntent();
//                startActivityForResult(CropHelper.buildGalleryIntent(), CropHelper.REQUEST_GALLERY);
                startActivityForResult(intent, CropHelper.REQUEST_GALLERY);
                break;
            case R.id.tv_worker:
                Intent intentWorker = new Intent(this, StaffListActivity.class);
                intentWorker.putExtra(Constants.EXTRA_DATA, 2);
                intentWorker.putExtra(Constants.EXTRA_CHOICE_MODE, false);
                startActivityForResult(intentWorker, 0x104);
                break;
            case R.id.ll_good_brief:
                Intent intent1 = new Intent(this, GoodDetailWebActivity.class);
                if (mGoods != null) {
                    intent1.putExtra(Constants.EXTRA_ID, mGoods.id);
                }
                startActivityForResult(intent1, GoodDetailWebActivity.REQUEST_CODE);
                break;
        }
    }

    private void updateHeadPhoto(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        Bitmap bmp = ImgUtils.getSmallBitmap(path, mCropParams.outputX, mCropParams.outputY);
        if (null != bmp) {
            Uri uri = ImgUtils.createUri(this);
            String tmp = ImgUtils.saveBitmap(uri.getPath(), bmp);
            if (!TextUtils.isEmpty(tmp)) {
                mTmpImgs.add(tmp);
                initViewHead(tmp);
            }
        } else {

            initViewHead(path);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case CropHelper.REQUEST_CAMERA:
                updateHeadPhoto(mCropParams.uri.getPath());
                break;
            case CropHelper.REQUEST_GALLERY:
                Uri photoUri = data.getData();
                if (photoUri == null) {
                    ToastUtils.show(this, getString(R.string.msg_select_image_err));
                    return;
                }
                try {
                    Uri realUri = ImgUtils.uriFormat(this, photoUri);
                    updateHeadPhoto(realUri.getPath());
                }catch (Exception e){
                    ToastUtils.show(this, getString(R.string.msg_select_image_err));
                }
                break;
            case 0x104:
                try{
                    ArrayList<StaffInfo> list = data.getParcelableArrayListExtra(Constants.EXTRA_DATA);
                    updateStaffList(list);

                }catch (Exception e){

                }
                break;
            case GoodDetailWebActivity.REQUEST_CODE:
                if (data != null) {
                    String value = data.getStringExtra(Constants.EXTRA_DATA);
                    initGoodBrief(value);
                }
                break;
        }
    }

    Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String obj = msg.obj.toString();
            if (msg.what == 1) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(ServiceAddActivity.this, R.string.msg_failed_update + obj);
            } else if (msg.what == 2) {
                List<String> list = new ArrayList<>();
                list.add(msg.obj.toString());
                mTmpGoods.imgs = list;
                uploadData();
            }

        }
    };

    private Goods createGoods(){
        Goods goods = new Goods();
        goods.imgs = new ArrayList<>();
        if (mGoods != null) {
            goods.id = mGoods.id;
            goods.imgs.addAll(mGoods.imgs);
            goods.cateId = mGoods.cateId;
        }else if(mGoodsCate != null){
            goods.cateId = mGoodsCate.id;
        }

        if(mHeadImage.getTag() != null) {
            String head = mHeadImage.getTag().toString();
            if (!TextUtils.isEmpty(head)) {
                goods.imgs.clear();
                goods.imgs.add(head);
            }
        }
        if(ArraysUtils.isEmpty(goods.imgs)){
            ToastUtils.show(this, getString(R.string.msg_select_image));
            return null;
        }
        EditText et = mViewFinder.find(R.id.et_name);
        goods.name = et.getText().toString();
        if (goods.name.length() < 2) {
            ToastUtils.show(this, getString(R.string.msg_err_name));
            return null;
        }
        et = mViewFinder.find(R.id.et_price);
        if(!TextUtils.isEmpty(et.getText().toString())) {
            goods.price = Double.valueOf(et.getText().toString()).doubleValue();
        }else{
            ToastUtils.show(this, getString(R.string.msg_import_price));
            return null;
        }

        et = mViewFinder.find(R.id.et_time);
        if(!TextUtils.isEmpty(et.getText().toString())) {
            goods.duration = Integer.valueOf(et.getText().toString()).intValue();
        }else{
            ToastUtils.show(this, getString(R.string.msg_import_time));
            return null;
        }

        goods.brief = mCurrGoodBiref;

        goods.staffIds = mStaffIds;
        if(ArraysUtils.isEmpty(goods.staffIds)){
            ToastUtils.show(this, getString(R.string.msg_select_server_staff));
            return null;
        }
        return goods;
    }
    private void commit() {
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        mTmpGoods = createGoods();

        if(mTmpGoods == null){
            return;
        }

        CustomDialogFragment.getInstance(this.getSupportFragmentManager(), this.getClass().getName());
        String head = mTmpGoods.imgs.get(0);
        if(!head.contains("http")){
            uploadImg(head);
        }else{
            uploadData();
        }
    }

    private void uploadImg(String head) {
        OSSUtils.save(head, new SaveCallback() {
            @Override
            public void onProgress(String arg0, int arg1, int arg2) {
            }

            @Override
            public void onFailure(String arg0, OSSException arg1) {
                Message message = new Message();
                message.what = 1;
                message.obj = arg0 + arg1.getMessage();
                hander.sendMessage(message);
            }

            @Override
            public void onSuccess(String arg0) {
                Message message = new Message();
                message.what = 2;
                message.obj = arg0;
                hander.sendMessage(message);
            }
        });
    }

    private void uploadData() {

        ApiUtils.post(this, URLConstants.GOODS_EDIT, mTmpGoods.getParams(),
                BaseResult.class, new Response.Listener<BaseResult>() {

                    @Override
                    public void onResponse(BaseResult response) {
                        CustomDialogFragment.dismissDialog();
                        if (O2OUtils.checkResponse(ServiceAddActivity.this, response)) {
                            if (mGoods != null) {
                                ToastUtils.show(ServiceAddActivity.this, getString(R.string.msg_update_scceed));
                                EventBus.getDefault().post(new GoodOpEvent(null, false));
                            } else {
                                ToastUtils.show(ServiceAddActivity.this, getString(R.string.msg_succ_add));
                                EventBus.getDefault().post(new GoodOpEvent(null, true));
                            }
                            setResult(Activity.RESULT_OK);
                            finishActivity();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(ServiceAddActivity.this, R.string.msg_error_update);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }


    private void goodsOp(final int op) {

        final List<Integer> ids = new ArrayList<>();
        if (mGoods == null) {
            ToastUtils.show(this, getString(R.string.msg_select_goods));
            return;
        }
        ids.add(mGoods.id);
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        params.put("type", op);
        CustomDialogFragment.getInstance(getSupportFragmentManager(), this.getClass().getName());
        ApiUtils.post(this, URLConstants.GOODS_OP, params,
                BaseResult.class, new Response.Listener<BaseResult>() {

                    @Override
                    public void onResponse(BaseResult response) {
                        if (O2OUtils.checkResponse(
                                ServiceAddActivity.this,
                                response)) {
                            setResult(Activity.RESULT_OK);
                            if(op == 3){
                                finishActivity();
                            }else{
                                initOnlineState(op);
                            }
                        }
                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(ServiceAddActivity.this, R.string.msg_error_update);
                        CustomDialogFragment.dismissDialog();
                    }
                });
    }

    private void initOnlineState(int status){
        TextView tv = mViewFinder.find(R.id.tv_pull);
        Drawable drawable;
        if (status == 1) {
            tv.setText(getString(R.string.msg_sold_out_text));
            drawable = getResources().getDrawable(R.drawable.ic_pull_off);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv.setCompoundDrawables(drawable, null, null, null);
            mViewFinder.find(R.id.tv_sale_state).setVisibility(View.INVISIBLE);
        } else {
            tv.setText(getString(R.string.msg_putaway_text));
            drawable = getResources().getDrawable(R.drawable.ic_pull_on);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv.setCompoundDrawables(drawable, null, null, null);
            mViewFinder.find(R.id.tv_sale_state).setVisibility(View.VISIBLE);
        }
        mGoods.status = status;
    }

    /**
     *  设置小数位数控制
     */
    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String spilt = "\\.";
            String[] splitArray = dValue.split(spilt);
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - 2;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImgUtils.removeFiles(mTmpImgs);
    }
}
