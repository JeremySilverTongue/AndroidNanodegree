package com.udacity.silver.sleep.services;

import android.app.IntentService;
import android.content.Intent;

import com.udacity.silver.sleep.data.SleepPreferenceUtils;
import com.udacity.silver.sleep.utilities.Utilities;


public class WakeUpService extends IntentService {


    private static final String SERVICE_NAME = "Wake up service";

    public WakeUpService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Utilities.cancelNotification(this);

        SleepPreferenceUtils.wakeUp(this);
    }
}
