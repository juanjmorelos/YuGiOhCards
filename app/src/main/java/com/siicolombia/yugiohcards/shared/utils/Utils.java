package com.siicolombia.yugiohcards.shared.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.siicolombia.yugiohcards.R;
import com.siicolombia.yugiohcards.helpers.connection.ConnectionHelper;
import com.siicolombia.yugiohcards.helpers.connection.NetworkListener;

public class Utils {
    public final static String BASE_URL = "https://db.ygoprodeck.com/api/v7/";
    public static BroadcastReceiver broadcastReceiver;

    public static void initNetworkReceiverListener(Activity activity, NetworkListener networkListener){
        broadcastReceiver = new ConnectionHelper(networkListener);
        registerNetworkReceiver(activity);
    }
    public static void registerNetworkReceiver(Activity activity){
        activity.registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    public  static void unregisterNetworkReceiver(Activity activity){
        try {
            activity.unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadUrlImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.image_loader)
                .into(imageView);
    }

    public static void goToFragment(FragmentManager fragmentManager, Fragment currentFragment, Fragment destinationFragment) {
        if (currentFragment == null || destinationFragment == null) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, destinationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void showMessageDialog(String message, Context context) {
        AlertDialog dialog;
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(message);
        alert.setPositiveButton("Accept", null);
        dialog = alert.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.backgroundColor));
    }
}
