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
import com.quachhoang.bitcointhailandprices.data.OrderBookItem;

import java.util.ArrayList;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class OrderBookAdapter extends ArrayAdapter<OrderBookItem> {
    private ArrayList<OrderBookItem> items;
    private LayoutInflater inflater;

    public OrderBookAdapter(Context ctx, ArrayList<OrderBookItem> items){
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
            OrderBookItem msi = items.get(position);
            if(msi.getOrderType() == SuperVAR.ORDER_BOOK_SECTION){
                view = inflater.inflate (R.layout.orderbook_item_section,null);
                TextView SectionName = (TextView) view.findViewById(R.id.orderbook_itemsection_sectionname);
                SectionName.setText(msi.getOrderPrice());
            }else{
                view = inflater.inflate (R.layout.orderbook_item_entry,null);

                TextView OrderPrice = (TextView) view.findViewById(R.id.orderbook_itementry_price);
                TextView OrderVolume = (TextView)view.findViewById(R.id.orderbook_itementry_volume);
                TextView OrderTime = (TextView)view.findViewById(R.id.orderbook_itementry_time);
                ImageView OrderIcon = (ImageView) view.findViewById(R.id.orderbook_itementry_icon);

                OrderPrice.setText(msi.getOrderPrice());
                OrderVolume.setText(msi.getOrderVolume());
                OrderTime.setVisibility(View.GONE);

                if(msi.getOrderType() == SuperVAR.ORDER_BOOK_ASK){
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