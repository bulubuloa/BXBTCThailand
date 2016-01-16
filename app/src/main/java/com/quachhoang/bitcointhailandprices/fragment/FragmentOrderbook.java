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
import com.quachhoang.bitcointhailandprices.adapter.OrderBookAdapter;
import com.quachhoang.bitcointhailandprices.adapter.SpinnerCoinAdapter;
import com.quachhoang.bitcointhailandprices.asynctask.APIRequestAsync;
import com.quachhoang.bitcointhailandprices.data.OrderBookItem;
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
public class FragmentOrderbook extends Fragment {
    private AccessMainActivity accessMainActivity;
    private View actionBarView;
    private ActionBar actionBar;
    private TextView txtTitle;
    private ImageButton imvRefresh;
    private APIRequestAsync apiRequestAsync;
    private APIRequestHandler apiRequestHandler;
    private boolean initLoad = true;

    private ListView lvCoin;
    private ArrayList<OrderBookItem> orderBookItems;
    private OrderBookAdapter orderBookAdapter;
    private Spinner spnCoin;
    private SpinnerCoinAdapter spinnerCoinAdapter;
    private ArrayList<CoinNode> spnItems;

    private AdView adView;
    private AdRequest adRequest;

    public FragmentOrderbook(){}

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
        this.lvCoin = (ListView)view.findViewById(R.id.fragment_order_lvOrder);
        this.spnCoin = (Spinner)view.findViewById(R.id.fragment_order_spnCoinOrder);
        this.adView = (AdView)view.findViewById(R.id.fragment_order_adView);

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

        this.txtTitle.setText("ORDER BOOK");
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

        this.orderBookItems = new ArrayList<OrderBookItem>();
        this.orderBookAdapter = new OrderBookAdapter(getActivity(),orderBookItems);
        this.lvCoin.setAdapter(orderBookAdapter);

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
            this.apiRequestAsync = new APIRequestAsync(apiRequestHandler,SuperVAR.REQUEST_API_METHOD_GET,SuperVAR.URL_GET_ORDER_BOOK_DATA,paramRequest,progressDialog);
            this.apiRequestAsync.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void LoadCoinSuccessAction(JSONObject jsonObject){
        try{
            orderBookItems.clear();

            JSONArray bids = jsonObject.getJSONArray("bids");
            int countBids = bids.length();
            if(countBids > 0){
                OrderBookItem bidSection = new OrderBookItem(SuperVAR.ORDER_BOOK_SECTION,"BUY ORDER","BUY ORDER");
                orderBookItems.add(bidSection);
                for (int i = 0; i < countBids; i++) {
                    JSONArray bidNode = bids.getJSONArray(i);
                    String price = BigDecimal.valueOf(bidNode.getDouble(0)).toPlainString();
                    String volume = BigDecimal.valueOf(bidNode.getDouble(1)).toPlainString();
                    OrderBookItem item = new OrderBookItem(SuperVAR.ORDER_BOOK_BID,volume,price);
                    orderBookItems.add(item);
                }
            }

            JSONArray asks = jsonObject.getJSONArray("asks");
            int countAsks = asks.length();
            if(countAsks > 0){
                OrderBookItem askSection = new OrderBookItem(SuperVAR.ORDER_BOOK_SECTION,"SELL ORDER","SELL ORDER");
                orderBookItems.add(askSection);
                for (int i = 0; i < countAsks; i++) {
                    JSONArray askNode = asks.getJSONArray(i);
                    String price = BigDecimal.valueOf(askNode.getDouble(0)).toPlainString();
                    String volume = BigDecimal.valueOf(askNode.getDouble(1)).toPlainString();
                    OrderBookItem item = new OrderBookItem(SuperVAR.ORDER_BOOK_ASK,volume,price);
                    orderBookItems.add(item);
                }
            }

            orderBookAdapter.notifyDataSetChanged();
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
    public void onPause() {
        super.onPause();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_ORDER){
            if (adView != null) {
                adView.pause();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_ORDER){
            if (adView != null) {
                adView.destroy();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_ORDER){
            setActionBarView();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        initWidget(rootView);
        return rootView;
    }
}