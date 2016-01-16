package com.quachhoang.bitcointhailandprices.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quachhoang.bitcointhailandprices.R;
import com.quachhoang.bitcointhailandprices.SuperVAR;
import com.quachhoang.bitcointhailandprices.data.CustomTextView;
import com.quachhoang.bitcointhailandprices.interfuck.AccessMainActivity;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class FragmentAbout extends Fragment {

    private AccessMainActivity accessMainActivity;
    private ActionBar actionBar;
    private CustomTextView customTextView;
    private Button bttRate;

    public FragmentAbout(){}

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
            setActionBarView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(accessMainActivity.getNowFragmentPosition() == SuperVAR.POSITION_FRAGMENT_ABOUT){
            setActionBarView();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        this.customTextView = (CustomTextView)rootView.findViewById(R.id.fragment_about_text);
        this.bttRate = (Button) rootView.findViewById(R.id.fragment_about_bttRate);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        setActionBarView();
        this.customTextView.setText("If you find this App useful, please do leave a kind review. We intend to keep this App up to date as much add as fast as possible, and we will never charge our users for this free forever App");
        this.bttRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });
    }
}