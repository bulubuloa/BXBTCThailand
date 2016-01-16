package com.quachhoang.bitcointhailandprices;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.quachhoang.bitcointhailandprices.customcontrol.TooltipWindow;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class SuperFUNC {
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void ShowToolTip(TooltipWindow tooltipWindow, String text, View view){
        if(!tooltipWindow.isTooltipShown())
            tooltipWindow.setText(text);
        tooltipWindow.showToolTip(view);
        view.requestFocus();
    }

    public static void hideKeyBoard(Context context){
        try {
            Activity activity = (Activity)context;
            if(activity.getCurrentFocus()!=null) {
                InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }catch (Exception e){}
    }

    public static void showKeyBoard(Context context,View view){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static String getNowDate(){
        String result = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(System.currentTimeMillis()));
        return result;
    }

    public static void showShitInternet(final Context context, final Runnable runnable){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_alert_internet_like_shit, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);

        final Handler handler = new Handler();

        final Button bttquaylai = (Button)promptView.findViewById(R.id.dialog_alert_shit_internet_bttquaylai);
        final Button bttthulai = (Button)promptView.findViewById(R.id.dialog_alert_shit_internet_bttthulai);

        final AlertDialog alert = alertDialogBuilder.create();
        bttquaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        bttthulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                handler.post(runnable);
            }
        });

        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public static ProgressDialog InitDialogProcessing(Context context, String message, boolean touchOutSide){
        ProgressDialog MyDialog = new ProgressDialog(context);
        MyDialog.setMessage(message);
        MyDialog.setCanceledOnTouchOutside(touchOutSide);
        return MyDialog;
    }

    public static JSONObject GetJSONFromAPI(int REQUEST_TYPE, String URL, String params) {
        InputStream inputStream = null;
        JSONObject jsonObject = null;
        String json = "";
        java.net.URL url = null;
        try{
            if(REQUEST_TYPE == SuperVAR.REQUEST_API_METHOD_GET){
                url = new URL(URL+"?"+params);
            }else if(REQUEST_TYPE == SuperVAR.REQUEST_API_METHOD_POST){
                url = new URL(URL);
            }

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setReadTimeout(SuperVAR.DEFAULT_READ_DATA_TIMEOUT);
            httpURLConnection.setConnectTimeout(SuperVAR.DEFAULT_CONNECTION_TIMEOUT);
            httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpURLConnection.setRequestProperty("Cache-Control", "no-store");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            if(REQUEST_TYPE == SuperVAR.REQUEST_API_METHOD_GET){
                httpURLConnection.setRequestMethod("GET");
            }else if(REQUEST_TYPE == SuperVAR.REQUEST_API_METHOD_POST){
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream os = new BufferedOutputStream(httpURLConnection.getOutputStream());
                os.write(params.getBytes("UTF-8"));
                os.flush();
                os.close();
            }

            httpURLConnection.connect();
            inputStream =  new BufferedInputStream(httpURLConnection.getInputStream()) ;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("GetJSONFromAPI",SuperVAR.ERROR_NETWORK_OR_SERVER);
            return null;
        }

        try{
            if(inputStream == null){
                Log.e("GetJSONFromAPI",SuperVAR.ERROR_NETWORK_OR_SERVER);
                return null;
            }else {
                try{
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader,8);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;
                    while((line = bufferedReader.readLine()) != null){
                        stringBuilder.append(line + "\n");
                    }
                    json = stringBuilder.toString();
                }catch(Exception e){
                    e.printStackTrace();
                    Log.e("GetJSONFromAPI",SuperVAR.ERROR_JSON_SYNTAX);
                    return null;
                }finally {
                    try {
                        inputStream.close();
                    } catch (Exception e) {}
                }

                try{
                    jsonObject = new JSONObject(json);
                }catch(Exception e){
                    e.printStackTrace();
                    Log.e("GetJSONFromAPI",SuperVAR.ERROR_JSON_SYNTAX);
                    return null;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("GetJSONFromAPI",SuperVAR.ERROR_JSON_SYNTAX);
            return null;
        }
        return jsonObject;
    }
}