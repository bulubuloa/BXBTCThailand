package com.quachhoang.bitcointhailandprices.ormdb;

import com.orm.SugarRecord;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class Settings extends SugarRecord{
    private @Setter @Getter boolean isNotify;
    private @Setter @Getter int timeUpdate;
    private @Setter @Getter String listCoinNotify;
}