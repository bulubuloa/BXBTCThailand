package com.quachhoang.bitcointhailandprices.data;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class OrderBookItem {
    private @Setter @Getter int orderType;
    private @Setter @Getter String orderVolume, orderPrice;

    public OrderBookItem(int type, String orderVolume,String orderPrice){
        this.orderType = type;
        this.orderVolume = orderVolume;
        this.orderPrice = orderPrice;
    }
}