package com.quachhoang.bitcointhailandprices.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.quachhoang.bitcointhailandprices.NotificationClass;
import com.quachhoang.bitcointhailandprices.R;
import com.quachhoang.bitcointhailandprices.SuperVAR;
import com.quachhoang.bitcointhailandprices.asynctask.APIRequestAsync;
import com.quachhoang.bitcointhailandprices.interfuck.APIRequestHandler;
import com.quachhoang.bitcointhailandprices.ormdb.CoinNode;
import com.quachhoang.bitcointhailandprices.ormdb.Settings;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class AutoRequestBX extends Service {
    private boolean isStop = false;
    private StringBuilder stringBuilder;
    private APIRequestAsync apiRequestAsync;
    private APIRequestHandler apiRequestHandler;
    private Settings settings;
    private PowerManager.WakeLock wakeLock;

    private CoinTimerCheck coinTimerCheck;
    private Timer timer;
    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LoadCoinRequestAction();
        }
    };

    private class CoinTimerCheck extends TimerTask{
        @Override
        public void run() {
            handler.post(runnable);
        }
    }

    private void initControl(){
        acquireWakeLock();
        if(wakeLock.isHeld()){
            this.wakeLock.release();
        }

        this.stringBuilder = new StringBuilder();
        List<Settings> settingsList = Settings.listAll(Settings.class);
        if(settingsList.size() == 0){
            isStop = true;
            this.stopSelf();
        }else {
            this.settings = Settings.listAll(Settings.class).get(0);
            if(!settings.isNotify()){
                isStop = true;
                this.stopSelf();
            }
        }

        this.apiRequestHandler = new APIRequestHandler() {
            @Override
            public void onComplete(boolean success, JSONObject result) {
                if(success){
                    LoadCoinSuccessAction(result);
                }
            }
        };

        this.handler = new Handler();
        this.timer = new Timer();
        this.coinTimerCheck = new CoinTimerCheck();
        this.timer.scheduleAtFixedRate(coinTimerCheck,5000,settings.getTimeUpdate()*60*1000);
    }

    private void LoadCoinRequestAction(){
        try{
            String paramRequest = "";
            this.apiRequestAsync = new APIRequestAsync(apiRequestHandler, SuperVAR.REQUEST_API_METHOD_GET,SuperVAR.URL_GET_MARKET_DATA,paramRequest,null);
            this.apiRequestAsync.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void LoadCoinSuccessAction(JSONObject jsonObject){
        try{
            stringBuilder = new StringBuilder();
            Iterator<?> keys = jsonObject.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                JSONObject ob = jsonObject.getJSONObject(key);

                CoinNode coinNode = new CoinNode();
                coinNode.setCoinId(Integer.parseInt(key));
                coinNode.setCoinName(ob.getString("secondary_currency"));
                coinNode.setCoinPrice(BigDecimal.valueOf(ob.getDouble("last_price")).toPlainString());

                if(settings.getListCoinNotify().contains(coinNode.getCoinName())){
                    stringBuilder.append(coinNode.getCoinName()+": "+coinNode.getCoinPrice()+" ,");
                }
                notifyMessage(stringBuilder.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void notifyMessage(String message){
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
        nBuilder.setContentTitle("BX Update Prices");
        nBuilder.setContentText(message);
        nBuilder.setTicker("New BX Update Prices");
        nBuilder.setAutoCancel(true);
        nBuilder.setSmallIcon(R.mipmap.ic_launcher);

        Intent intent = new Intent(this, NotificationClass.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationClass.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        nBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(6591, nBuilder.build());
    }

    public void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"Request New Invoice WakeLock");
        wakeLock.acquire();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        try{
            if(!isStop){
                Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
                restartServiceIntent.setPackage(getPackageName());
                PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
        handler.removeCallbacksAndMessages(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initControl();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}