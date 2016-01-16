package com.quachhoang.bitcointhailandprices.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quachhoang.bitcointhailandprices.R;
import com.quachhoang.bitcointhailandprices.SuperVAR;
import com.quachhoang.bitcointhailandprices.ormdb.CoinNode;

import java.util.ArrayList;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class SpinnerCoinAdapter extends ArrayAdapter<CoinNode> {
    private ArrayList<CoinNode> items;
    private LayoutInflater inflater;

    public SpinnerCoinAdapter(Context context, ArrayList<CoinNode> arrayList){
        super(context,0,arrayList);
        this.items = arrayList;
        this.inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            CoinNode item = items.get(position);
            view = inflater.inflate (R.layout.spinner_node,null);

            TextView ItemText = (TextView) view.findViewById(R.id.spnCoin_coinName);
            ImageView ItemIcon = (ImageView) view.findViewById(R.id.spnCoin_coinIcon);

            ItemText.setText(item.getCoinName()+" - Last Price: "+item.getCoinPrice());
            if(item.getCoinId() > 19){
                ItemIcon.setImageResource(SuperVAR.CoinDrawable[0]);
            }else{
                ItemIcon.setImageResource(SuperVAR.CoinDrawable[item.getCoinId()]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            CoinNode item = items.get(position);
            view = inflater.inflate (R.layout.spinner_node,null);

            TextView ItemText = (TextView) view.findViewById(R.id.spnCoin_coinName);
            ImageView ItemIcon = (ImageView) view.findViewById(R.id.spnCoin_coinIcon);

            ItemText.setText(item.getCoinName()+" - Last Price: "+item.getCoinPrice());
            if(item.getCoinId() > 19){
                ItemIcon.setImageResource(SuperVAR.CoinDrawable[0]);
            }else{
                ItemIcon.setImageResource(SuperVAR.CoinDrawable[item.getCoinId()]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return view;
    }
}
