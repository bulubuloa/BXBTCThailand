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
import com.quachhoang.bitcointhailandprices.data.HistoryTradeItem;

import java.util.ArrayList;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class HistoryTradeAdapter extends ArrayAdapter<HistoryTradeItem> {
    private ArrayList<HistoryTradeItem> items;
    private LayoutInflater inflater;

    public HistoryTradeAdapter(Context ctx, ArrayList<HistoryTradeItem> items){
        super(ctx,0,items);
        this.items = items;
        this.inflater = (LayoutInflater)ctx.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            HistoryTradeItem msi = items.get(position);
            if(msi.getTradeType() == SuperVAR.ORDER_BOOK_SECTION){
                view = inflater.inflate (R.layout.orderbook_item_section,null);
                TextView SectionName = (TextView) view.findViewById(R.id.orderbook_itemsection_sectionname);
                TextView TxtPrice = (TextView) view.findViewById(R.id.orderbook_itemsection_txtprice);
                TextView TxtVolume = (TextView) view.findViewById(R.id.orderbook_itemsection_txtvolume);

                SectionName.setText(msi.getTradeAmout());
                TxtPrice.setText("RATE");
                TxtVolume.setText("AMOUNT");
            }else{
                view = inflater.inflate (R.layout.orderbook_item_entry,null);

                TextView OrderPrice = (TextView) view.findViewById(R.id.orderbook_itementry_price);
                TextView OrderVolume = (TextView)view.findViewById(R.id.orderbook_itementry_volume);
                TextView OrderTime = (TextView)view.findViewById(R.id.orderbook_itementry_time);
                ImageView OrderIcon = (ImageView) view.findViewById(R.id.orderbook_itementry_icon);

                OrderPrice.setText(msi.getTradeRate());
                OrderVolume.setText(msi.getTradeAmout());
                OrderTime.setVisibility(View.GONE);

                if(msi.getTradeType() == SuperVAR.HISTORY_TRADE_SELL){
                    OrderIcon.setImageResource(R.drawable.asks);
                }else{
                    OrderIcon.setImageResource(R.drawable.bids);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return view;
    }
}