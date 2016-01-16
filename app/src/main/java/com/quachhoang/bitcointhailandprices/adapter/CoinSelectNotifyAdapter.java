package com.quachhoang.bitcointhailandprices.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.quachhoang.bitcointhailandprices.R;
import com.quachhoang.bitcointhailandprices.SuperVAR;
import com.quachhoang.bitcointhailandprices.ormdb.CoinNotify;

import java.util.ArrayList;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class CoinSelectNotifyAdapter extends BaseAdapter {
    private ArrayList<CoinNotify> arrayList;
    private LayoutInflater layoutInflater;

    public CoinSelectNotifyAdapter(Context context, ArrayList<CoinNotify> arrayList){
        this.arrayList = arrayList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final CoinNotify invoiceItem = arrayList.get(position);
        if(invoiceItem!=null){
            v = layoutInflater.inflate(R.layout.coin_notify_node,null);
            CheckBox invoice = (CheckBox)v.findViewById(R.id.coin_notify_node_cbxCheck);
            ImageView imageView = (ImageView)v.findViewById(R.id.coin_notify_node_icon);

            invoice.setChecked(invoiceItem.isNotify());
            invoice.setText(invoiceItem.getCoinNode().getCoinName());
            if(invoiceItem.getCoinNode().getCoinId() > 19){
                imageView.setImageResource(SuperVAR.CoinDrawable[0]);
            }else{
                imageView.setImageResource(SuperVAR.CoinDrawable[invoiceItem.getCoinNode().getCoinId()]);
            }

            invoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    invoiceItem.setNotify(isChecked);
                }
            });
        }
        return v;
    }
}