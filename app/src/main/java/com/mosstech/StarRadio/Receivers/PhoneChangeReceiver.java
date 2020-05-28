package com.mosstech.StarRadio.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import static android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY;

/**
 * Created by Mert on 2.11.2016.
 */

public class PhoneChangeReceiver extends BroadcastReceiver {

    private PhoneChangeListener mListener;

    public PhoneChangeReceiver(PhoneChangeListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        if(intent.getAction() == null || mListener == null)
            return;

        //Network Check
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION))
            mListener.onNetworkChange(intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY,false));

        //Headset Check
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            mListener.onHeadsetPlugChange(state == 0);
        }

        //call check
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
            mListener.onCall();
    }

}


