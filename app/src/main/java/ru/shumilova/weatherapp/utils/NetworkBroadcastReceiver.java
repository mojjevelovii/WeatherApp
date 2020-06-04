package ru.shumilova.weatherapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkBroadcastReceiver extends BroadcastReceiver {
    private ConnectivityReceiverListener connectivityReceiverListener;

    public NetworkBroadcastReceiver(ConnectivityReceiverListener listener) {
        connectivityReceiverListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected(context));
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return false;
        }
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
