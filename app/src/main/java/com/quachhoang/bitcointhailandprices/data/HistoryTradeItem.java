package com.quachhoang.bitcointhailandprices.data;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class HistoryTradeItem {
    private @Setter @Getter int tradeType;
    private @Setter @Getter String tradeAmout, tradeRate, tradeTime;

    public HistoryTradeItem(int type, String amout, String price, String time){
        this.tradeType = type;
        this.tradeRate = price;
        this.tradeAmout = amout;
        this.tradeTime = time;
    }
}