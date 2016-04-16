package com.yizan.community.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizan.community.business.R;

import com.fanwe.seallibrary.common.ParamConstants;


/**
 * Created by admin on 2015/6/26.
 */
public class InputBoxDialogActivity extends Activity {
    private TextView dialog_text_number;
    private EditText dialog_input_edi;
    private Button dialog_button_ok, dialog_button_cancel;
    public  final static int  INPUTBOX=101;
    public  final static int  RESULT_OK=100;
    private LinearLayout dialog_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_layout);

        initView();
    }

    public void initView() {
        dialog_text_number = (TextView) findViewById(R.id.dialog_text_number);
        dialog_input_edi = (EditText) findViewById(R.id.dialog_input_edi);
        dialog_button_ok = (Button) findViewById(R.id.dialog_button_ok);
        dialog_button_cancel = (Button) findViewById(R.id.dialog_button_cancel);
        dialog_layout= (LinearLayout) findViewById(R.id.dialog_layout);
        dialog_text_number.setText(getString(R.string.msg_import_text_num));
        dialog_input_edi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dialog_text_number.setText(s.length() + getString(R.string.msg_present_import_text_num));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra(ParamConstants.CONTENT,dialog_input_edi.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        dialog_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
