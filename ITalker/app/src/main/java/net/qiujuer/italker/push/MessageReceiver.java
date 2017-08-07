package net.qiujuer.italker.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.BreakIterator;
import android.os.Bundle;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.persistence.Account;
import com.igexin.sdk.PushConsts;
import com.raizlabs.android.dbflow.sql.language.Case;

/**
 * Created by Administrator on 2017/8/6.
 */

/**
 * 个推消息接收器
 */
public class MessageReceiver extends BroadcastReceiver{
    private static final String TAG = MessageReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent==null){
            return;
        }
            Bundle bundle = intent.getExtras();
            switch (bundle.getInt(PushConsts.CMD_ACTION)){
                case PushConsts.GET_CLIENTID:{
                    Log.e(TAG,"GET_CLIENTID:"+bundle);
                    //当Id初始化的时候
                    onClientInit(bundle.getString("clientid"));
                    break;
                }
                case PushConsts.GET_MSG_DATA:{
                    //常规消息送达
                    byte[] payload = bundle.getByteArray("payload");
                    if(payload!=null){
                        String message = new String(payload);
                        Log.e(TAG,"GET_MSG_DATA:"+message);
                        onMessageArrived(message);
                    }
                }
                default:
                    Log.e(TAG,"OTHER:"+bundle.toString());
                    break;
            }
    }

    /**
     * 当ID初始化的时候
     * @param cid 设备ID
     */
    private void onClientInit(String cid){
        Account.setPushId(cid);
        if(Account.isLogin()){
            //账户登录状态，进行一次PushId的帮顶
            AccountHelper.bindPush(null);
        }
    }

    /**
     * 消息到达时
     * @param message 消息
     */
    private void onMessageArrived(String message){
        Factory.dispatchPush(message);
    }
}
