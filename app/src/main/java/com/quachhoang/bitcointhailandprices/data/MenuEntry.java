package com.quachhoang.bitcointhailandprices.data;

import com.quachhoang.bitcointhailandprices.SuperVAR;
import com.quachhoang.bitcointhailandprices.interfuck.MenuItem;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class MenuEntry implements MenuItem{
    private @Setter @Getter String title;
    private @Setter @Getter int imageId;

    public MenuEntry(String title, int imageId){
        this.title = title;
        this.imageId = imageId;
    }

    @Override
    public String getMenuName() {
        return title;
    }

    @Override
    public int getMenuType() {
        return SuperVAR.MENU_ENTRY;
    }
}