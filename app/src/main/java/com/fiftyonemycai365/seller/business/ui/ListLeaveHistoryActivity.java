package com.fiftyonemycai365.seller.business.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.util.TagManage;

/**
 * Created by admin on 2015/10/29.
 */
public class ListLeaveHistoryActivity extends BaseActivity implements BaseActivity.TitleListener {

//    private List<>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_history);
        setPageTag(TagManage.LIST_LEAVE_HISTORY_ACTIVITY);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.holiday_record));
    }
}
