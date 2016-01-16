package com.quachhoang.bitcointhailandprices.data;

import com.quachhoang.bitcointhailandprices.SuperVAR;
import com.quachhoang.bitcointhailandprices.interfuck.MenuItem;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class MenuSection implements MenuItem {
    private @Setter @Getter String title;

    public MenuSection(String title){
        this.title = title;
    }

    @Override
    public String getMenuName() {
        return title;
    }

    @Override
    public int getMenuType() {
        return SuperVAR.MENU_SECTION;
    }
}