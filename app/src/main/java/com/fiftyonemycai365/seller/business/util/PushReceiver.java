package com.fiftyonemycai365.seller.business.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fiftyonemycai365.seller.business.ui.InformCenterActivity;
import com.fiftyonemycai365.seller.business.ui.OrderDetailsActivity;
import com.fiftyonemycai365.seller.business.ui.WebViewActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;

public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            // 接收Registeration

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            // 接收到推送下来的自定义消息
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            // 接收到推送下来的通知
            refresh(context, extras);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            // 用户点击打开了通知
            startIntebt(context, extras);
        }

    }

    private void refresh(final Context context, String extras) {
        try {
            JSONObject jsonObject = new JSONObject(extras);

            String sound = "";
            if (jsonObject.has("sound")) {
                sound = jsonObject.getString("sound");
                if (!TextUtils.isEmpty(sound)) {
                    if (sound.compareTo("music1.caf") == 0) {
                        playLocalMp3(context, R.raw.push_music1);
                    }else if(sound.compareTo("xg5_neworder.caf") == 0){
                        playLocalMp3(context, R.raw.xg5_push_music1);
                    }else if(sound.compareTo("xg5_pay.caf") == 0){
                        playLocalMp3(context, R.raw.xg5_push_music2);
                    }else if(sound.compareTo("xg5_send.caf") == 0){
                        playLocalMp3(context, R.raw.xg5_push_music3);
                    }else if (sound.compareTo("bxlneworder.caf") == 0){
                        playLocalMp3(context, R.raw.bxl);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void startIntebt(Context context, String extras) {
        Intent i = null;

        try {
            if (!O2OUtils.isLogin(context)){
                return;
            }
            JSONObject jsonObject = new JSONObject(extras);
            String type = "";
            String args = "";
            if (jsonObject.has("type"))
                type = jsonObject.getString("type");
            if (jsonObject.has("args"))
                args = jsonObject.getString("args");
            if (type.equals("1")) {

                i = new Intent(context, InformCenterActivity.class);

            } else if (type.equals("2")) {
                i = new Intent(context, WebViewActivity.class);
                i.putExtra(Constants.EXTRA_URL, args);

            } else if (type.equals("3")) {
                i = new Intent(context, OrderDetailsActivity.class);
                i.putExtra("orderId", Integer.parseInt(args));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (i != null) {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            JPushInterface.clearAllNotifications(context);
        }

    }

    static MediaPlayer mediaPlayer = null;

    public void playLocalMp3(Context context, int musicId) {
        /**
         * 创建音频文件的方法：
         * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
         * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
         *   mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
         */

        mediaPlayer = MediaPlayer.create(context, musicId);
        mediaPlayer.stop();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();//释放音频资源
            }
        });
        try {
            //在播放音频资源之前，必须调用Prepare方法完成些准备工作
            mediaPlayer.prepare();
            //开始播放音频
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
