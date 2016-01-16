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
import com.quachhoang.bitcointhailandprices.data.MenuEntry;
import com.quachhoang.bitcointhailandprices.data.MenuSection;
import com.quachhoang.bitcointhailandprices.interfuck.MenuItem;

import java.util.ArrayList;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class MenuAdapter extends ArrayAdapter<MenuItem> {
    private ArrayList<MenuItem> arrayList;
    private LayoutInflater layoutInflater;

    public MenuAdapter(Context context, ArrayList<MenuItem> arrayList){
        super(context,0,arrayList);
        this.arrayList = arrayList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        MenuItem menuItem = arrayList.get(position);
        if(menuItem.getMenuType() == SuperVAR.MENU_ENTRY){
            MenuEntry menuEntry = (MenuEntry)menuItem;
            view = layoutInflater.inflate(R.layout.menu_entry, null);
            TextView entryTitle = (TextView)view.findViewById(R.id.menu_entry_txtName);
            ImageView entryImg = (ImageView)view.findViewById(R.id.menu_entry_imvIcon);

            entryTitle.setText(menuEntry.getTitle());
            entryImg.setImageResource(menuEntry.getImageId());
        }else if(menuItem.getMenuType() == SuperVAR.MENU_SECTION){
            MenuSection menuSection = (MenuSection)menuItem;
            view = layoutInflater.inflate(R.layout.menu_section, null);
            view.setOnClickListener(null);
            view.setOnLongClickListener(null);
            view.setLongClickable(false);

            TextView sectionTitle = (TextView)view.findViewById(R.id.menu_section_txtName);
            sectionTitle.setText(menuSection.getTitle());
        }
        return view;
    }
}