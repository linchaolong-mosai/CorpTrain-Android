package com.itutorgroup.liveh2h.train.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TokenExpireReceiver extends BroadcastReceiver {
    public static String action = "com.mosai.corporatetraing.tokenexpire";
    public TokenExpireReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        if(intent.getAction().equals(action)){
            if(tokenExpireCallback!=null){
                tokenExpireCallback.teCallback();
            }
        }
    }
    private TokenExpireCallback tokenExpireCallback;
    public void setTokenExpireCallback(TokenExpireCallback tokenExpireCallback){
        this.tokenExpireCallback = tokenExpireCallback;
    }
    public interface TokenExpireCallback{
        void teCallback();
    }
}
