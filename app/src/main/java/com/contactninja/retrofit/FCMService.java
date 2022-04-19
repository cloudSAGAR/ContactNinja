package com.contactninja.retrofit;


import android.util.Log;
import android.widget.Toast;


import com.contactninja.MainActivity;
import com.contactninja.Model.Dashboard.Dashboard;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;



public class FCMService extends FirebaseMessagingService {



    @Override
    public void onNewToken(@NotNull String token) {
      //  Log.e("FCM Refreshed token: CC", token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            //Log.e("Remote Data is",new Gson().toJson(remoteMessage));
            MainActivity.upload(remoteMessage);
        }

    }
}