package com.quachhoang.bitcointhailandprices.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.quachhoang.bitcointhailandprices.R;
import com.quachhoang.bitcointhailandprices.SuperVAR;
import com.quachhoang.bitcointhailandprices.adapter.SpinnerCoinAdapter;
import com.quachhoang.bitcointhailandprices.interfuck.AccessMainActivity;
import com.quachhoang.bitcointhailandprices.ormdb.CoinNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class FragmentConvertPrices extends Fragment {

    private boolean initLoad = true;
    private ActionBar actionBar;
    private AccessMainActivity accessMainActivity;
    private Spinner spnCoinInput, spnCoinOutput;
    private EditText edtCoinInput;
    private TextView edtCoinOutput;
    private Button bttConvert;

    private SpinnerCoinAdapter inputAdapter, outputAdapter;
    private ArrayList<CoinNode> spnItems;

    private AdView adView;
    private AdRequest adRequest;

    public FragmentConvertPrices(){}

    private void initWidget(View view){
        this.spnCoinInput = (Spinner)view.findViewById(R.id.fragment_convertPrices_spnCoinInput);
        this.spnCoinOutput = (Spinner)view.findViewById(R.id.fragment_convertPrices_spnCoinOutput);
        this.edtCoinInput = (EditText) view.findViewById(R.id.fragment_convertPrices_edtCoinInput);
        this.edtCoinOutput = (TextView) view.findViewById(R.id.fragment_convertPrices_edtCoinOutput);
        this.bttConvert = (Button) view.findViewById(R.id.fragment_convertPrices_bttConvert);
        this.adView = (AdView)view.findViewById(R.id.fragment_convertPrices_adView);
    }

    private void initControl(){
        this.adRequest = new AdRequest.Builder().addTestDevice("37595A0E62DEF104").build();
        adView.loadAd(adRequest);

        this.actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        setActionBarView();

        List<CoinNode> list = CoinNode.listAll(CoinNode.class);
        this.spnItems = new ArrayList<CoinNode>();
        this.spnItems.addAll(list);
        this.inputAdapter = new SpinnerCoinAdapter(getActivity(),spnItems);
        this.spnCoinInput.setAdapter(inputAdapter);
        this.outputAdapter = new SpinnerCoinAdapter(getActivity(),spnItems);
        this.spnCoinOutput.setAdapter(outputAdapter);

        this.bttConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtCoinInput.getText().toString().length()>0){
                    try{
                        Double input = Double.parseDouble( edtCoinInput.getText().toString());
                        Double inputRate = Double.parseDouble(spnItems.get(spnCoinInput.getSelectedItemPosition()).getCoinPrice());
                        Double outputRate = Double.parseDouble(spnItems.get(spnCoinOutput.getSelectedItemPosition()).getCoinPrice());
                        edtCoinOutput.setText((input*inputRate / outputRate)+"");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setActionBarView(){
        this.actionBar.setDisplayShowCustomEnabled(false);
    }

    private void FillSpinnerItems(){
        List<CoinNode> list = CoinNode.listAll(CoinNode.class);
        this.spnItems = new ArrayList<CoinNode>();
        this.spnItems.addAll(list);
        this.inputAdapter.notifyDataSetChanged();
        this.outputAdapter.notifyDataSetChanged();
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
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_CONVERT){
            if (adView != null) {
                adView.pause();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_CONVERT){
            if (adView != null) {
                adView.destroy();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_CONVERT){
            setActionBarView();
            if (adView != null) {
                adView.resume();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_convert_prices, container, false);
        initWidget(rootView);
        return rootView;
    }
}