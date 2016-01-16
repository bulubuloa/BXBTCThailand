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
public class CoinItemAdapter extends ArrayAdapter<CoinNode> {
    public final String TAG = "CoinItemAdapter";
    private ArrayList<CoinNode> items;
    private LayoutInflater inflater;

    public CoinItemAdapter (Context ctx, ArrayList<CoinNode> items){
        super(ctx,0,items);
        this.items = items;
        this.inflater = (LayoutInflater)ctx.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public android.view.View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            CoinNode msi = items.get(position);

            view = inflater.inflate (R.layout.node_coin,null);

            TextView ItemName = (TextView) view.findViewById(R.id.nodecoin_txtCoinName);
            TextView ItemPrice = (TextView)view.findViewById(R.id.nodecoin_txtCoinPrice);
            TextView ItemSumBids = (TextView)view.findViewById(R.id.nodecoin_sumbids);
            TextView ItemSumAsks = (TextView)view.findViewById(R.id.nodecoin_sumasks);
            TextView ItemHighBids = (TextView)view.findViewById(R.id.nodecoin_highbids);
            TextView ItemHighAsks = (TextView)view.findViewById(R.id.nodecoin_highasks);
            ImageView IconCoin  = (ImageView)view.findViewById(R.id.nodecoin_imgCoin);

            ItemName.setText(msi.getCoinName());
            ItemPrice.setText(msi.getCoinPrice());
            ItemHighAsks.setText(msi.getHighAsk());
            ItemHighBids.setText(msi.getHighBids());
            ItemSumAsks.setText(msi.getSumAsks()+ " asks");
            ItemSumBids.setText(msi.getSumBids()+ " bids");

            if(msi.getCoinId() > 19){
                IconCoin.setImageResource(SuperVAR.CoinDrawable[0]);
            }else{
                IconCoin.setImageResource(SuperVAR.CoinDrawable[msi.getCoinId()]);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return view;
    }
}