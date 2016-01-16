package com.quachhoang.bitcointhailandprices.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.quachhoang.bitcointhailandprices.R;
import com.quachhoang.bitcointhailandprices.SuperFUNC;
import com.quachhoang.bitcointhailandprices.SuperVAR;
import com.quachhoang.bitcointhailandprices.adapter.CoinSelectNotifyAdapter;
import com.quachhoang.bitcointhailandprices.customcontrol.TooltipWindow;
import com.quachhoang.bitcointhailandprices.interfuck.AccessMainActivity;
import com.quachhoang.bitcointhailandprices.ormdb.CoinNode;
import com.quachhoang.bitcointhailandprices.ormdb.CoinNotify;
import com.quachhoang.bitcointhailandprices.ormdb.Settings;
import com.quachhoang.bitcointhailandprices.services.AutoRequestBX;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class FragmentSettings extends Fragment {
    private AccessMainActivity accessMainActivity;
    private ActionBar actionBar;
    private boolean initLoad = true;
    private TooltipWindow tooltipWindow;

    private CheckBox cbxNotify;
    private EditText edtTimeUpdate;
    private GridView cgvCoin;
    private Button bttSaveSettings;
    private ArrayList<CoinNotify> notifyArrayList;
    private CoinSelectNotifyAdapter coinSelectNotifyAdapter;
    private LinearLayout layoutTransparent;
    private Settings settings;

    public FragmentSettings(){}

    private void initWidget(View view){
        this.cbxNotify = (CheckBox)view.findViewById(R.id.fragment_settings_cbxNotify);
        this.edtTimeUpdate = (EditText) view.findViewById(R.id.fragment_settings_edtTimeUpdate);
        this.cgvCoin = (GridView) view.findViewById(R.id.fragment_settings_cgvCoin);
        this.bttSaveSettings = (Button) view.findViewById(R.id.fragment_settings_bttSaveSettings);
        this.layoutTransparent = (LinearLayout) view.findViewById(R.id.fragment_settings_layoutTransparent);
    }

    private void initControl(){
        this.tooltipWindow = new TooltipWindow(getActivity());
        this.actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        this.cbxNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edtTimeUpdate.setEnabled(isChecked);
                cgvCoin.setEnabled(isChecked);
                layoutTransparent.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            }
        });

        this.bttSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SuperFUNC.hideKeyBoard(getActivity());
                    if(validateForm()){
                        StringBuilder stringBuilder = new StringBuilder();
                        int count = notifyArrayList.size();
                        for (int i=0;i<count-1;i++) {
                            CoinNotify coinNotify = notifyArrayList.get(i);
                            if(coinNotify.isNotify())
                                stringBuilder.append(coinNotify.getCoinNode().getCoinName()+",");
                        }
                        CoinNotify coinNotify = notifyArrayList.get(count-1);
                        if(coinNotify.isNotify())
                            stringBuilder.append(coinNotify.getCoinNode().getCoinName());

                        settings.setListCoinNotify(stringBuilder.toString());
                        settings.setNotify(cbxNotify.isChecked() ? true : false);
                        settings.setTimeUpdate(Integer.parseInt(edtTimeUpdate.getText().toString()));
                        settings.save();

                        Intent intent = new Intent(getActivity(), AutoRequestBX.class);
                        if(SuperFUNC.isMyServiceRunning(getActivity(), AutoRequestBX.class)){
                            getActivity().stopService(intent);
                        }
                        if(cbxNotify.isChecked()){
                            getActivity().startService(intent);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        setActionBarView();
        initSettings();
    }

    private void initSettings(){
        this.notifyArrayList = new ArrayList<CoinNotify>();

        List<CoinNode> coinNodeList = CoinNode.listAll(CoinNode.class);
        int count = coinNodeList.size();

        List<Settings> settingsList = Settings.listAll(Settings.class);
        if(settingsList.size() == 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0;i<count-1;i++) {
                stringBuilder.append(coinNodeList.get(i).getCoinName()+",");
                this.notifyArrayList.add(new CoinNotify(coinNodeList.get(i),true));
            }
            stringBuilder.append(coinNodeList.get(count-1).getCoinName());
            this.notifyArrayList.add(new CoinNotify(coinNodeList.get(count-1),true));

            this.settings = new Settings();
            this.settings.setListCoinNotify(stringBuilder.toString());
            this.settings.setNotify(true);
            this.settings.setTimeUpdate(10);
            this.settings.save();
        }else {
            this.settings = settingsList.get(0);

            for (int i=0;i<count-1;i++) {
                boolean isNotify = settings.getListCoinNotify().contains(coinNodeList.get(i).getCoinName());
                this.notifyArrayList.add(new CoinNotify(coinNodeList.get(i),isNotify));
            }
            boolean isNotify = settings.getListCoinNotify().contains(coinNodeList.get(count-1).getCoinName());
            this.notifyArrayList.add(new CoinNotify(coinNodeList.get(count-1),isNotify));
        }

        this.coinSelectNotifyAdapter = new CoinSelectNotifyAdapter(getActivity(),notifyArrayList);
        this.cgvCoin.setAdapter(coinSelectNotifyAdapter);

        this.edtTimeUpdate.setText(settings.getTimeUpdate()+"");
        this.cbxNotify.setChecked(settings.isNotify());
        this.layoutTransparent.setVisibility(settings.isNotify() ? View.GONE : View.VISIBLE);
    }

    private boolean validateForm(){
        if(cbxNotify.isChecked()){
            String strTime = edtTimeUpdate.getText().toString();
            if(strTime.length()==0){
                SuperFUNC.ShowToolTip(tooltipWindow, "Time for update can't be blank", edtTimeUpdate);
                return false;
            }else {
                int timeValue = Integer.parseInt(strTime);
                if(timeValue>1439){
                    SuperFUNC.ShowToolTip(tooltipWindow, "Time for update maximum 1439 minutes", edtTimeUpdate);
                    return false;
                }else if(timeValue<5){
                    SuperFUNC.ShowToolTip(tooltipWindow, "Time for update minimum 5 minutes", edtTimeUpdate);
                    return false;
                }
            }
        }
        return true;
    }

    private void setActionBarView(){
        this.actionBar.setDisplayShowCustomEnabled(false);
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
                initSettings();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_SETTINGS){
            setActionBarView();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        initWidget(rootView);
        return rootView;
    }
}