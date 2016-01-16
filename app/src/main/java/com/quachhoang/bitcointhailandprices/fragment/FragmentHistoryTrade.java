package com.quachhoang.bitcointhailandprices.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.quachhoang.bitcointhailandprices.R;
import com.quachhoang.bitcointhailandprices.SuperFUNC;
import com.quachhoang.bitcointhailandprices.SuperVAR;
import com.quachhoang.bitcointhailandprices.adapter.HistoryTradeAdapter;
import com.quachhoang.bitcointhailandprices.adapter.SpinnerCoinAdapter;
import com.quachhoang.bitcointhailandprices.asynctask.APIRequestAsync;
import com.quachhoang.bitcointhailandprices.data.HistoryTradeItem;
import com.quachhoang.bitcointhailandprices.interfuck.APIRequestHandler;
import com.quachhoang.bitcointhailandprices.interfuck.AccessMainActivity;
import com.quachhoang.bitcointhailandprices.ormdb.CoinNode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class FragmentHistoryTrade extends Fragment {
    private AccessMainActivity accessMainActivity;
    private View actionBarView;
    private ActionBar actionBar;
    private TextView txtTitle;
    private ImageButton imvRefresh;
    private APIRequestAsync apiRequestAsync;
    private APIRequestHandler apiRequestHandler;
    private boolean initLoad = true;

    private ListView lvCoin;
    private ArrayList<HistoryTradeItem> historyTradeItems;
    private HistoryTradeAdapter historyTradeAdapter;
    private Spinner spnCoin;
    private SpinnerCoinAdapter spinnerCoinAdapter;
    private ArrayList<CoinNode> spnItems;

    private AdView adView;
    private AdRequest adRequest;

    public FragmentHistoryTrade(){}

    private void setActionBarView(){
        this.actionBar.setDisplayShowCustomEnabled(true);
        this.actionBar.setCustomView(actionBarView);
    }

    private void FillSpinnerItems(){
        List<CoinNode> list = CoinNode.listAll(CoinNode.class);
        this.spnItems = new ArrayList<CoinNode>();
        this.spnItems.addAll(list);
        this.spinnerCoinAdapter.notifyDataSetChanged();
    }

    private void initWidget(View view){
        this.lvCoin = (ListView)view.findViewById(R.id.fragment_historyTrade_lvTradeHistory);
        this.spnCoin = (Spinner)view.findViewById(R.id.fragment_historyTrade_spnCoinHistory);
        this.adView = (AdView)view.findViewById(R.id.fragment_historytrade_adView);

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.actionBarView = inflater.inflate(R.layout.toolbar_search,null);
        this.txtTitle = (TextView) actionBarView.findViewById(R.id.toolbar_search_txtTitle);
        this.imvRefresh = (ImageButton) actionBarView.findViewById(R.id.toolbar_search_imvRefresh);
    }

    private void initControl(){
        this.adRequest = new AdRequest.Builder().addTestDevice("37595A0E62DEF104").build();
        adView.loadAd(adRequest);

        this.actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        setActionBarView();

        this.txtTitle.setText("RECENT TRADE");
        this.imvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadCoinRequestAction();
            }
        });

        List<CoinNode> list = CoinNode.listAll(CoinNode.class);
        this.spnItems = new ArrayList<CoinNode>();
        this.spnItems.addAll(list);
        this.spinnerCoinAdapter = new SpinnerCoinAdapter(getActivity(),spnItems);
        this.spnCoin.setAdapter(spinnerCoinAdapter);
        this.spnCoin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnItems.size() > 0){
                    LoadCoinRequestAction();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        this.historyTradeItems = new ArrayList<HistoryTradeItem>();
        this.historyTradeAdapter = new HistoryTradeAdapter(getActivity(),historyTradeItems);
        this.lvCoin.setAdapter(historyTradeAdapter);

        this.apiRequestHandler = new APIRequestHandler() {
            @Override
            public void onComplete(boolean success, JSONObject result) {
                if(success){
                    LoadCoinSuccessAction(result);
                }else {
                    SuperFUNC.showShitInternet(getActivity(), new Runnable() {
                        @Override
                        public void run() {
                            LoadCoinRequestAction();
                        }
                    });
                }
            }
        };
    }

    private void LoadCoinRequestAction(){
        try{
            String paramRequest = "pairing="+spnItems.get(spnCoin.getSelectedItemPosition()).getCoinId();
            ProgressDialog progressDialog = SuperFUNC.InitDialogProcessing(getActivity(), "Loading data from bx.in.th..",false);
            this.apiRequestAsync = new APIRequestAsync(apiRequestHandler,SuperVAR.REQUEST_API_METHOD_GET,SuperVAR.URL_GET_HISTORY_TRADE_DATA,paramRequest,progressDialog);
            this.apiRequestAsync.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void LoadCoinSuccessAction(JSONObject jsonObject){
        try{
            historyTradeItems.clear();

            JSONArray trades = jsonObject.getJSONArray("trades");
            int countTrades = trades.length();
            if(countTrades > 0){
                HistoryTradeItem tradeSection = new HistoryTradeItem(SuperVAR.ORDER_BOOK_SECTION,"HISTORY TRADES","HISTORY TRADES","HISTORY TRADES");
                historyTradeItems.add(tradeSection);

                for (int i = 0; i < countTrades; i++) {
                    JSONObject tradeNode = trades.getJSONObject(i);

                    String rate = BigDecimal.valueOf(tradeNode.getDouble("rate")).toPlainString();
                    String amount = BigDecimal.valueOf(tradeNode.getDouble("amount")).toPlainString();
                    String trade_date = tradeNode.getString("trade_date");
                    String trade_type = tradeNode.getString("trade_type");

                    if(trade_type.equals("sell")){
                        HistoryTradeItem item = new HistoryTradeItem(SuperVAR.HISTORY_TRADE_SELL,amount,rate,trade_date);
                        historyTradeItems.add(item);
                    }else{
                        HistoryTradeItem item = new HistoryTradeItem(SuperVAR.HISTORY_TRADE_BUY,amount,rate,trade_date);
                        historyTradeItems.add(item);
                    }
                }
            }

            JSONArray lowask = jsonObject.getJSONArray("lowask");
            int countLowask = lowask.length();
            if(countLowask > 0){
                HistoryTradeItem lowaskSection = new HistoryTradeItem(SuperVAR.ORDER_BOOK_SECTION,"TOP LOWASK","TOP LOWASK","TOP LOWASK");
                historyTradeItems.add(lowaskSection);

                for (int i = 0; i < countLowask; i++) {
                    JSONObject lowaskNode = lowask.getJSONObject(i);

                    String rate = BigDecimal.valueOf(lowaskNode.getDouble("rate")).toPlainString();
                    String amount = BigDecimal.valueOf(lowaskNode.getDouble("amount")).toPlainString();
                    String trade_date = lowaskNode.getString("date_added");
                    String trade_type = lowaskNode.getString("order_type");

                    if(trade_type.equals("sell")){
                        HistoryTradeItem item = new HistoryTradeItem(SuperVAR.HISTORY_TRADE_SELL,amount,rate,trade_date);
                        historyTradeItems.add(item);
                    }else{
                        HistoryTradeItem item = new HistoryTradeItem(SuperVAR.HISTORY_TRADE_BUY,amount,rate,trade_date);
                        historyTradeItems.add(item);
                    }
                }
            }

            JSONArray highbid = jsonObject.getJSONArray("highbid");
            int countHighbid = highbid.length();
            if(countHighbid > 0){
                HistoryTradeItem highbidSection = new HistoryTradeItem(SuperVAR.ORDER_BOOK_SECTION,"TOP HIGHBID","TOP HIGHBID","TOP HIGHBID");
                historyTradeItems.add(highbidSection);

                for (int i = 0; i < countHighbid; i++) {
                    JSONObject highbidNode = highbid.getJSONObject(i);

                    String rate = BigDecimal.valueOf(highbidNode.getDouble("rate")).toPlainString();
                    String amount = BigDecimal.valueOf(highbidNode.getDouble("amount")).toPlainString();
                    String trade_date = highbidNode.getString("date_added");
                    String trade_type = highbidNode.getString("order_type");

                    if(trade_type.equals("sell")){
                        HistoryTradeItem item = new HistoryTradeItem(SuperVAR.HISTORY_TRADE_SELL,amount,rate,trade_date);
                        historyTradeItems.add(item);
                    }else{
                        HistoryTradeItem item = new HistoryTradeItem(SuperVAR.HISTORY_TRADE_BUY,amount,rate,trade_date);
                        historyTradeItems.add(item);
                    }
                }
            }

            historyTradeAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity){
            a =(Activity) context;
            this.accessMainActivity = (AccessMainActivity)a;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(initLoad){
                initLoad = false;
                initControl();
            }else {
                setActionBarView();
                FillSpinnerItems();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_HISTORY){
            setActionBarView();
            if (adView != null) {
                adView.resume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_HISTORY){
            if (adView != null) {
                adView.pause();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_HISTORY){
            if (adView != null) {
                adView.destroy();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        initWidget(rootView);
        return rootView;
    }
}