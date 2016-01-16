package com.quachhoang.bitcointhailandprices.ormdb;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class CoinNode extends SugarRecord{
    @Unique
    private @Setter @Getter int coinId;
    private @Setter @Getter int sumBids, sumAsks;
    private @Setter @Getter String coinName, coinPrice, highBids, highAsk,timeUpdated;
}