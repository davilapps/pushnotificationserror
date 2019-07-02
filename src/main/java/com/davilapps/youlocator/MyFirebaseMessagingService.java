package com.davilapps.youlocator;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static String FCMTAG = "FCM Message";
    public static String FCMSTATE = "FCMSTATE";

    JSONObject jsonObject;

    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageSent(String s) {
        Log.v("Fcm", "MyFirebaseMessagingService :: onMessageSent  " + s);
        super.onMessageSent(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            if (remoteMessage != null && remoteMessage.getNotification() != null) {
                RemoteMessage.Notification notification = remoteMessage.getNotification();
                if (notification.getTitle() != null && notification.getBody() != null) {
                    String title = notification.getTitle();
                    String message = notification.getBody();
                    Log.d("FCM", "Title: " + title + " , Message: " + message);
                    showStatusBarNotification(title, message);
                }
            }
        } catch (Exception e) {
            Log.d("FCM", "Error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showStatusBarNotification(String title, String sendMessage) {
        //Code to show Push notification on Toolbar
    }

}