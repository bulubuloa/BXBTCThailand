package com.quachhoang.bitcointhailandprices.ormdb;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class CoinNotify{
    private @Setter @Getter CoinNode coinNode;
    private @Setter @Getter boolean isNotify;

    public CoinNotify(CoinNode coinNode, boolean isNotify){
        this.coinNode = coinNode;
        this.isNotify = isNotify;
    }
}