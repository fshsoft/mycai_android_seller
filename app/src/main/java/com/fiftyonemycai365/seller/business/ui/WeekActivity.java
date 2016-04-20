package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.model.WeekInfo;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.WeekListAdapter;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class WeekActivity extends Activity {

	private WeekListAdapter adapter;
	private ListView week_list;
	private RelativeLayout week_all_layout;
	private Button week_cancel, week_confirm;
	private List<WeekInfo> infos;
	private List<String> allweek = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_week_layout);
		allweek = getIntent().getStringArrayListExtra(Constants.ALLWEEKS);
		initView();
	}

	private void initView() {

		week_list = (ListView) findViewById(R.id.week_list);
		week_all_layout = (RelativeLayout) findViewById(R.id.week_all_layout);
		week_cancel = (Button) findViewById(R.id.week_cancel);
		week_confirm = (Button) findViewById(R.id.week_confirm);
		adapter = new WeekListAdapter(WeekActivity.this, getListData());
		week_list.setAdapter(adapter);

		week_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		week_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < infos.size(); i++) {
					if (infos.get(i).getIsChick() == 1) {
						list.add(infos.get(i).getWeeknumbe());
					}
				}
				if (list.size() > 0) {
					Intent data = new Intent();
					data.putStringArrayListExtra(Constants.WEEKS,
							(ArrayList<String>) list);
					setResult(RESULT_OK, data);
					finish();
				} else {

					ToastUtils.show(WeekActivity.this,
							R.string.week_set_repetition);
				}

			}
		});
	}

	private List<WeekInfo> getListData() {
		infos = new ArrayList<WeekInfo>(0);

		WeekInfo info = new WeekInfo();
		info.setWeekname(getString(R.string.Monday));
		info.setWeeknumbe("1");
		infos.add(info);
		info = new WeekInfo();
		info.setWeekname(getString(R.string.Tuesday));
		info.setWeeknumbe("2");
		infos.add(info);
		info = new WeekInfo();
		info.setWeekname(getString(R.string.Wednesday));
		info.setWeeknumbe("3");
		infos.add(info);
		info = new WeekInfo();
		info.setWeekname(getString(R.string.Thursday));
		info.setWeeknumbe("4");
		infos.add(info);
		info = new WeekInfo();
		info.setWeekname(getString(R.string.Friday));
		info.setWeeknumbe("5");
		infos.add(info);
		info = new WeekInfo();
		info.setWeekname(getString(R.string.Saturday));
		info.setWeeknumbe("6");
		infos.add(info);
		info = new WeekInfo();
		info.setWeekname(getString(R.string.Sunday));
		info.setWeeknumbe("0");
		infos.add(info);
		if (allweek != null && allweek.size() > 0) {
			for (int i = 0; i < allweek.size(); i++) {
				for (int j = 0; j < infos.size(); j++) {
					if (allweek.get(i).equals(infos.get(j).getWeeknumbe())) {
						infos.get(j).setIsChick(2);
					}
				}
			}
		}
		return infos;
	}
}
