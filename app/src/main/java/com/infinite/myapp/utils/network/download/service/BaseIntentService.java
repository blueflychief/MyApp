package com.infinite.myapp.utils.network.download.service;

import android.app.IntentService;
import android.content.Intent;

import com.infinite.myapp.utils.MyLogger;


/**
 * Description: This is our app Base IntentService
 */
public class BaseIntentService extends IntentService {

    public static final String TAG = BaseIntentService.class.getSimpleName();


    protected static final String ACTION_TYPE_KEY = "ACTION_TYPE";

    protected static final String RESULT_RECEIVER = "RESULT_RECEIVER";


    public BaseIntentService(String serviceName) {
        super(serviceName);
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MyLogger.e("super intent service onHandleIntent()--->>Enter!");
    }
}
