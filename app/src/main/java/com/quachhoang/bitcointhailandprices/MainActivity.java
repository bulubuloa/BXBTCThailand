package com.quachhoang.bitcointhailandprices;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.quachhoang.bitcointhailandprices.adapter.MenuAdapter;
import com.quachhoang.bitcointhailandprices.data.MenuEntry;
import com.quachhoang.bitcointhailandprices.data.MenuSection;
import com.quachhoang.bitcointhailandprices.fragment.FragmentAbout;
import com.quachhoang.bitcointhailandprices.fragment.FragmentConvertPrices;
import com.quachhoang.bitcointhailandprices.fragment.FragmentHistoryTrade;
import com.quachhoang.bitcointhailandprices.fragment.FragmentMarket;
import com.quachhoang.bitcointhailandprices.fragment.FragmentOrderbook;
import com.quachhoang.bitcointhailandprices.fragment.FragmentSettings;
import com.quachhoang.bitcointhailandprices.interfuck.AccessMainActivity;
import com.quachhoang.bitcointhailandprices.interfuck.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AccessMainActivity{

    private DrawerLayout drawerLayout;
    private CharSequence title, drawerTitle;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Fragment fragmentMarket, fragmentOrderBook, fragmentHistoryTrade, fragmentConvertPrices, fragmentSettings, fragmentAbout;
    private HashMap<Integer,Fragment> fragmentHashMap;
    private FragmentManager fragmentManager;
    private ActionBar actionBar;

    private ListView listView;
    private ArrayList<MenuItem> menuItems;
    private MenuAdapter menuAdapter;
    private int lastFragmentPosition = SuperVAR.POSITION_FRAGMENT_MARKET;

    private InterstitialAd interstitialAd;
    private AdRequest adRequest;

    private void initWidget(){
        this.title = drawerTitle = getTitle();
        this.drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        this.listView = (ListView) findViewById(R.id.list_slidermenu);
    }

    private void initControl(){
        this.menuItems = new ArrayList<MenuItem>();
        this.menuItems.add(new MenuSection("BX.IN.TH MARKET"));
        this.menuItems.add(new MenuEntry("Market Data",R.drawable.marketdata));
        this.menuItems.add(new MenuEntry("Order Book",R.drawable.orderbook));
        this.menuItems.add(new MenuEntry("Recent Trades",R.drawable.recenttrades));
        this.menuItems.add(new MenuEntry("Convert Prices",R.drawable.convertprice));
        this.menuItems.add(new MenuSection("Application"));
        this.menuItems.add(new MenuEntry("Settings",R.drawable.settings));
        this.menuItems.add(new MenuEntry("About",R.drawable.about));
        this.menuAdapter = new MenuAdapter(this,menuItems);
        this.listView.setAdapter(menuAdapter);
        this.listView.setOnItemClickListener(new SlideMenuClickListener());

        this.fragmentManager = getSupportFragmentManager();
        this.fragmentHashMap = new HashMap<Integer, Fragment>();
        this.fragmentAbout = (FragmentAbout)fragmentManager.findFragmentById (R.id.fragment_about);
        this.fragmentSettings = (FragmentSettings)fragmentManager.findFragmentById (R.id.fragment_settings);
        this.fragmentConvertPrices = (FragmentConvertPrices)fragmentManager.findFragmentById (R.id.fragment_convertprices);
        this.fragmentHistoryTrade = (FragmentHistoryTrade)fragmentManager.findFragmentById (R.id.fragment_historytrade);
        this.fragmentOrderBook = (FragmentOrderbook)fragmentManager.findFragmentById (R.id.fragment_orderbook);
        this.fragmentMarket = (FragmentMarket)fragmentManager.findFragmentById (R.id.fragment_market);
        this.fragmentHashMap.put(SuperVAR.POSITION_FRAGMENT_ABOUT,fragmentAbout);
        this.fragmentHashMap.put(SuperVAR.POSITION_FRAGMENT_SETTINGS,fragmentSettings);
        this.fragmentHashMap.put(SuperVAR.POSITION_FRAGMENT_CONVERT,fragmentConvertPrices);
        this.fragmentHashMap.put(SuperVAR.POSITION_FRAGMENT_HISTORY,fragmentHistoryTrade);
        this.fragmentHashMap.put(SuperVAR.POSITION_FRAGMENT_ORDER,fragmentOrderBook);
        this.fragmentHashMap.put(SuperVAR.POSITION_FRAGMENT_MARKET,fragmentMarket);

        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        this.actionBar.setHomeButtonEnabled(true);

        this.actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.app_name){
            @Override
            public void onDrawerClosed(View drawerView) {
                actionBar.setTitle(title);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                actionBar.setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };

        this.actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        this.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        this.drawerLayout.setDrawerListener(actionBarDrawerToggle);
        this.actionBarDrawerToggle.syncState();

        interstitialAd = new InterstitialAd(MainActivity.this);
        interstitialAd.setAdUnitId(getString(R.string.full_ads));
        initAdsFull();

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                initAdsFull();
            }

            @Override
            public void onAdLoaded() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });
    }

    private void initAdsFull(){
        adRequest = new AdRequest.Builder().addTestDevice("37595A0E62DEF104").build();
        interstitialAd.loadAd(adRequest);
    }

    private void showFragment(int position){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        for(int key: fragmentHashMap.keySet()){
            Fragment fragment = fragmentHashMap.get(key);
            if(key == position){
                fragmentTransaction.show(fragment);
            }else {
                fragmentTransaction.hide(fragment);
            }
        }
        fragmentTransaction.commit();

    }

    private void displayView(int position){
        showFragment(position);
        this.lastFragmentPosition = position;
        this.listView.setItemChecked(lastFragmentPosition, true);
        this.listView.setSelection(lastFragmentPosition);
        setTitle(menuItems.get(lastFragmentPosition).getMenuName());
        this.drawerLayout.closeDrawer(listView);
    }

    @Override
    public int getNowFragmentPosition() {
        return lastFragmentPosition;
    }

    @Override
    public void onBackPressed() {
        if(lastFragmentPosition!=SuperVAR.POSITION_FRAGMENT_MARKET){
            displayView(SuperVAR.POSITION_FRAGMENT_MARKET);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        this.actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        this.actionBar.setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidget();
        initControl();

        if(savedInstanceState == null){
            displayView(SuperVAR.POSITION_FRAGMENT_MARKET);
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }
    }
}
