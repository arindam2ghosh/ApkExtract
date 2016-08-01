package com.duongkk.apkextract.application;

import com.crittercism.app.Crittercism;

/**
 * Created by MyPC on 7/30/2016.
 */
public class AppController extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Crittercism.initialize(getApplicationContext(), "0d1b2ffa65314ce1be4da57a0b3995ab00555300");
    }
}
