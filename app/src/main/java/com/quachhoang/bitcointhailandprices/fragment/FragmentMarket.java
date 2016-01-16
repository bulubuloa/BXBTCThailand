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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.quachhoang.bitcointhailandprices.R;
import com.quachhoang.bitcointhailandprices.SuperFUNC;
import com.quachhoang.bitcointhailandprices.SuperVAR;
import com.quachhoang.bitcointhailandprices.adapter.CoinItemAdapter;
import com.quachhoang.bitcointhailandprices.asynctask.APIRequestAsync;
import com.quachhoang.bitcointhailandprices.interfuck.APIRequestHandler;
import com.quachhoang.bitcointhailandprices.interfuck.AccessMainActivity;
import com.quachhoang.bitcointhailandprices.ormdb.CoinNode;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class FragmentMarket extends Fragment {
    private AccessMainActivity accessMainActivity;
    private View actionBarView;
    private ActionBar actionBar;
    private TextView txtTitle;
    private ImageButton imvRefresh;
    private APIRequestAsync apiRequestAsync;
    private APIRequestHandler apiRequestHandler;
    private AdView adView;
    private AdRequest adRequest;

    private ListView lvCoin;
    private ArrayList<CoinNode> coinNodes;
    private CoinItemAdapter coinItemAdapter;
    private TextView txtUpdatedTime;

    public FragmentMarket(){}

    private void setActionBarView(){
        this.actionBar.setDisplayShowCustomEnabled(true);
        this.actionBar.setCustomView(actionBarView);
    }

    private void initWidget(View view){
        this.lvCoin = (ListView)view.findViewById(R.id.fragmentMarket_lvCoin);
        this.txtUpdatedTime = (TextView) view.findViewById(R.id.fragmentMarket_txtUpdateTime);
        this.adView = (AdView)view.findViewById(R.id.fragment_market_adView);

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.actionBarView = inflater.inflate(R.layout.toolbar_search,null);
        this.txtTitle = (TextView) actionBarView.findViewById(R.id.toolbar_search_txtTitle);
        this.imvRefresh = (ImageButton) actionBarView.findViewById(R.id.toolbar_search_imvRefresh);
    }

    private void initControl(){
        this.adRequest = new AdRequest.Builder().addTestDevice("37595A0E62DEF104").build();
        adView.loadAd(adRequest);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                adView.loadAd(new AdRequest.Builder().addTestDevice("37595A0E62DEF104").build());
            }
        });*/

        this.actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        setActionBarView();

        this.txtTitle.setText("MARKET DATA");
        this.imvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadCoinRequestAction();
            }
        });

        List<CoinNode> db = CoinNode.listAll(CoinNode.class);
        this.coinNodes = new ArrayList<CoinNode>();
        this.coinNodes.addAll(db);
        this.coinItemAdapter = new CoinItemAdapter(getActivity(),coinNodes);
        this.lvCoin.setAdapter(coinItemAdapter);

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

        if(coinNodes.size() == 0){
            LoadCoinRequestAction();
        }else {
            txtUpdatedTime.setText(coinNodes.get(0).getTimeUpdated());
        }
    }

    private void LoadCoinRequestAction(){
        try{
            String paramRequest = "";
            ProgressDialog progressDialog = SuperFUNC.InitDialogProcessing(getActivity(), "Loading data from bx.in.th..",false);
            this.apiRequestAsync = new APIRequestAsync(apiRequestHandler,SuperVAR.REQUEST_API_METHOD_GET,SuperVAR.URL_GET_MARKET_DATA,paramRequest,progressDialog);
            this.apiRequestAsync.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void LoadCoinSuccessAction(JSONObject jsonObject){
        try{
            for (CoinNode cn : coinNodes){
                cn.delete();
            }
            coinNodes.clear();
            String timeUpdated = "Data updated at "+SuperFUNC.getNowDate();

            Iterator<?> keys = jsonObject.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                JSONObject ob = jsonObject.getJSONObject(key);

                CoinNode coinNode = new CoinNode();
                coinNode.setCoinId(Integer.parseInt(key));
                coinNode.setCoinName(ob.getString("secondary_currency"));
                coinNode.setCoinPrice(BigDecimal.valueOf(ob.getDouble("last_price")).toPlainString());

                JSONObject bids = ob.getJSONObject("orderbook").getJSONObject("bids");
                coinNode.setSumBids(bids.getInt("total"));
                coinNode.setHighBids(BigDecimal.valueOf( bids.getDouble("highbid")).toPlainString());

                JSONObject asks = ob.getJSONObject("orderbook").getJSONObject("asks");
                coinNode.setSumAsks(asks.getInt("total"));
                coinNode.setHighAsk(BigDecimal.valueOf(asks.getDouble("highbid")).toPlainString());

                coinNode.setTimeUpdated(timeUpdated);
                coinNode.save();
                coinNodes.add(coinNode);
            }
            coinItemAdapter.notifyDataSetChanged();
            txtUpdatedTime.setText(timeUpdated);
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
            setActionBarView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_MARKET){
            if (adView != null) {
                adView.pause();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_MARKET){
            setActionBarView();
            if (adView != null) {
                adView.resume();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_MARKET){
            if (adView != null) {
                adView.destroy();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initControl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_market, container, false);
        initWidget(rootView);
        return rootView;
    }
}