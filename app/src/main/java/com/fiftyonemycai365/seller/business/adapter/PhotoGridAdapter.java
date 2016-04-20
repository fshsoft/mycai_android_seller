package com.fiftyonemycai365.seller.business.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.fiftyonemycai365.seller.business.R;
import com.zongyou.library.volley.RequestManager;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

public class PhotoGridAdapter extends CommonAdapter<String> {
	private final String TAG = "PhotoGridAdapter";
	private int MAX_COUNT = 4;
	private IPhotoFunc mPhotoFunc;

	public PhotoGridAdapter(Context context, List<String> datas) {
		super(context, datas, R.layout.item_square_photo);
		addItem("");
	}

	public PhotoGridAdapter(Context context, List<String> datas, int maxCount) {
		super(context, datas, R.layout.item_square_photo);
		MAX_COUNT = maxCount;
		addItem("");
	}

	public void setPhotoFunc(IPhotoFunc photoFunc){
		mPhotoFunc = photoFunc;
	}

	@Override
	public int getCount() {
		if (mDatas.size() > MAX_COUNT) {
			return MAX_COUNT;
		}
		return super.getCount();
	}

	public void addItem(String value) {
		if (!TextUtils.isEmpty(value)) {
			if (mDatas.size() > 0
					&& TextUtils.isEmpty(mDatas.get(mDatas.size() - 1))) {
				mDatas.add(mDatas.size() - 1, value);
			}
		} else {
			if (mDatas.size() > 0
					&& !TextUtils.isEmpty(mDatas.get(mDatas.size() - 1))) {
				mDatas.add(value);
			} else {
				mDatas.add(value);
			}
		}
		notifyDataSetChanged();
	}

	public void removeItem(int pos) {
		if (pos >= 0 && mDatas.size() > pos) {
			mDatas.remove(pos);
			notifyDataSetChanged();
		}
	}

	public List<String> getDatas() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < mDatas.size(); i++) {
			if (!TextUtils.isEmpty(mDatas.get(i))) {
				list.add(mDatas.get(i));
			}
		}
		return list;
	}

	@Override
	public void convert(final ViewHolder helper, String item,int number) {
		try {
			final ImageView ivPhoto = (ImageView) helper.getView(R.id.iv_photo);
			if (TextUtils.isEmpty(item)) {
				ivPhoto.setImageResource(R.drawable.ic_add_photo);
				helper.setViewVisible(R.id.btn_del, View.GONE);
				return;
			}
			int position = 0;
			for (int i=0; i<mDatas.size(); i++){
				if(mDatas.get(i).equals(item)){
					position = i;
					break;
				}
			}
			helper.setViewVisible(R.id.btn_del, View.VISIBLE);
			final int finalPosition = position;
			helper.getView(R.id.btn_del).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									mContext);
							builder.setTitle(mContext.getString(R.string.tip))
									.setMessage(mContext.getString(R.string.msg_is_delele_image))
									.setIcon(android.R.drawable.ic_dialog_alert)
									.setPositiveButton(
											mContext.getString(R.string.ok),
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													if(mPhotoFunc != null){
														mPhotoFunc.removePhoto(finalPosition, getItem(finalPosition));
													}
													removeItem(helper
															.getPosition());

												}
											}).setNegativeButton(mContext.getString(R.string.cancel), null)
									.show();

						}
					});
			if (isLocalFile(item)) {
				ivPhoto.setImageDrawable(Drawable.createFromPath(item));
				return;
			}
			RequestManager.getImageLoader().get(item, new ImageListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.e(TAG, "Load Image: " + error.getMessage());
				}

				@Override
				public void onResponse(ImageContainer response,
						boolean isImmediate) {
					if (response.getBitmap() != null) {
						ivPhoto.setImageBitmap(response.getBitmap());
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean isLocalFile(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		if (path.startsWith("http://")) {
			return false;
		}
		return true;
	}

	public interface IPhotoFunc {
		void removePhoto(int pos, String path);
	}

}