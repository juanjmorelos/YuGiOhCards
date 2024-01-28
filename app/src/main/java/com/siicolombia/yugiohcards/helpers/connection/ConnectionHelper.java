package com.siicolombia.yugiohcards.helpers.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionHelper extends BroadcastReceiver {
    public static boolean isOnline = false ;
    NetworkListener networkListener;

    public ConnectionHelper(NetworkListener networkListener) {
        this.networkListener = networkListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            isOnline = isOnline(context);
            if(networkListener !=null){
                networkListener.onNetworkChangeListener(isOnline);
            }
        }catch (NullPointerException e){
            isOnline = false ;
            e.printStackTrace();
        }
    }

    public boolean isOnline(Context context){
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            return networkInfo != null && networkInfo.isConnected();
        }catch (NullPointerException e){
            e.printStackTrace();
            return false ;
        }

    }
}
