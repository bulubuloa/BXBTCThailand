package com.quachhoang.bitcointhailandprices;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public class SuperVAR {
    public static final int MENU_ENTRY = 1;
    public static final int MENU_SECTION = 2;

    public static final int ORDER_BOOK_SECTION = 1;
    public static final int ORDER_BOOK_BID = 2;
    public static final int ORDER_BOOK_ASK = 3;

    public static final int HISTORY_TRADE_SECTION = 1;
    public static final int HISTORY_TRADE_SELL = 2;
    public static final int HISTORY_TRADE_BUY = 3;

    public static final int POSITION_FRAGMENT_ABOUT = 7;
    public static final int POSITION_FRAGMENT_SETTINGS = 6;
    public static final int POSITION_FRAGMENT_CONVERT = 4;
    public static final int POSITION_FRAGMENT_HISTORY = 3;
    public static final int POSITION_FRAGMENT_ORDER = 2;
    public static final int POSITION_FRAGMENT_MARKET = 1;

    public static int[] CoinDrawable = {
            R.drawable.icon,//
            R.drawable.bitcoin48,
            R.drawable.litecoin48,
            R.drawable.namecoin48,
            R.drawable.dogecoin48,
            R.drawable.peercoin48,
            R.drawable.feathercoin48,
            R.drawable.primecoin48,
            R.drawable.bitcoin48,//8
            R.drawable.zetacoin48,
            R.drawable.bitcoin48,//10
            R.drawable.captcoin48,
            R.drawable.bitcoin48,//12
            R.drawable.hyperstake48,
            R.drawable.pandacoin48,
            R.drawable.cryptonitecoin48,
            R.drawable.bitcoin48,//16
            R.drawable.paycoin48,
            R.drawable.leocoin48,
            R.drawable.quarkcoin48,
    };

    public static final String ERROR_NETWORK_OR_SERVER = "Internet connection error or server not available";
    public static final String ERROR_JSON_SYNTAX = "Json syntax error";
    public static final int DEFAULT_CONNECTION_TIMEOUT = 8000;
    public static final int DEFAULT_READ_DATA_TIMEOUT = 8000;
    public static final int REQUEST_API_METHOD_POST = 1;
    public static final int REQUEST_API_METHOD_GET = 2;

    public static final String URL_GET_MARKET_DATA = "https://bx.in.th/api/";
    public static final String URL_GET_ORDER_BOOK_DATA = "https://bx.in.th/api/orderbook/";
    public static final String URL_GET_HISTORY_TRADE_DATA = "https://bx.in.th/api/trade/";
}
