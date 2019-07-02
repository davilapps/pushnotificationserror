package com.davilapps.youlocator;

import android.app.Application;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .enableLocalDataStore()
                .build());

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", getString(R.string.gcm_sender_id));
        String devToken = installation.getDeviceToken();
        System.out.println("DEVTOKEN" + devToken);
        installation.saveInBackground();
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
    }




    }


