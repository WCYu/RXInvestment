package com.rxjy.rxinvestment.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.rxjy.rxinvestment.activity.home.JoininNjjActivity;
import com.rxjy.rxinvestment.activity.my.EntryInformationActivity;
import com.rxjy.rxinvestment.activity.my.MessageDetailsActivity;
import com.rxjy.rxinvestment.activity.my.UserInfoModifyActivity;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.entity.NoticBean;
import com.rxjy.rxinvestment.utils.JSONUtils;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hjh on 2018/3/7.
 */

public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + bundle.toString());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String contentstr = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.e(TAG + "接收到的推送数据是：：", contentstr);
            Log.e(TAG + "接收到的推送数据是：：", extras);
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            //接受到推送就底部icon加一
            SetIconMsgNum(context);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 用户点击打开了通知");
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.e("tag_获得的推送消息", extras);
            NoticBean info = JSONUtils.toObject(extras, NoticBean.class);
            int type = Integer.parseInt(info.getType());
            switch (type) {
                case 1:
                    context.startActivity(new Intent(context, UserInfoModifyActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case 2:
                    context.startActivity(new Intent(context, UserInfoModifyActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case 3:
                    context.startActivity(new Intent(context, EntryInformationActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                default:
                    context.startActivity(new Intent(context, MessageDetailsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id", info.getID()));
                    break;
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.e(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    public static String MSG_NUM = App.cardNo + "MSG_NUM";
    public static String msgnum = "msgnum";

    private void SetIconMsgNum(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MSG_NUM, MODE_PRIVATE);
        int num = preferences.getInt(msgnum, 0);
        num = num + 1;
        preferences.edit().putInt(msgnum, num).commit();
        JoininNjjActivity.activity.ShowMsgNum(num);
    }

}