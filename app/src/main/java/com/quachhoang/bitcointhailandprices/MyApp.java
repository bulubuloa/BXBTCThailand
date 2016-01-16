package com.quachhoang.bitcointhailandprices;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
