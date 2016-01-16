package com.quachhoang.bitcointhailandprices.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.quachhoang.bitcointhailandprices.SuperFUNC;
import com.quachhoang.bitcointhailandprices.interfuck.APIRequestHandler;

import org.json.JSONObject;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class APIRequestAsync extends AsyncTask<Void,Void,Void> {
    private APIRequestHandler apiRequestHandler;
    private Exception exception;

    private String apiURL, apiParams;
    private int apiRequestMethod;
    private JSONObject jsonObject;
    private ProgressDialog progressDialog;

    public APIRequestAsync(APIRequestHandler apiRequestHandler, int apiRequestMethod, String apiURL, String apiParams, ProgressDialog progressDialog){
        this.apiRequestHandler = apiRequestHandler;
        this.apiRequestMethod = apiRequestMethod;
        this.apiURL = apiURL;
        this.apiParams = apiParams;
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute() {
        if(progressDialog!=null)
            this.progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            this.jsonObject = SuperFUNC.GetJSONFromAPI(apiRequestMethod,apiURL,apiParams);
        }catch (Exception e){
            this.exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try{
            if(this.exception == null && jsonObject != null){
                apiRequestHandler.onComplete(true,jsonObject);
            }else {
                apiRequestHandler.onComplete(false,jsonObject);
            }
        }catch (Exception e){
            apiRequestHandler.onComplete(false,jsonObject);
        }finally {
            if(progressDialog!=null &&progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}