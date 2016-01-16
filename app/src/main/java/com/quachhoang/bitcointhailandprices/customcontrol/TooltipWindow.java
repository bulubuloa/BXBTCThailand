package com.quachhoang.bitcointhailandprices.customcontrol;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.quachhoang.bitcointhailandprices.R;
import com.quachhoang.bitcointhailandprices.SuperFUNC;

/**
 * Created by QuachHoang on 1/16/2016.
 */
public class TooltipWindow {
    private static final int MSG_DISMISS_TOOLTIP = 100;
    private Context ctx;
    private PopupWindow tipWindow;
    private View contentView;
    private LayoutInflater inflater;
    private TextView txtText;

    public TooltipWindow(Context ctx) {
        this.ctx = ctx;
        this.tipWindow = new PopupWindow(ctx);
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contentView = inflater.inflate(R.layout.tooltip_layout, null);
        this.txtText =  (TextView)contentView.findViewById(R.id.tooltip_text);
    }

    public void setText(String text){
        txtText.setText(text);
    }

    public void showToolTip(View anchor) {
        anchor.setFocusable(true);
        anchor.requestFocus();
        tipWindow.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        tipWindow.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);

        tipWindow.setOutsideTouchable(true);
        tipWindow.setTouchable(true);
        tipWindow.setFocusable(false);
        tipWindow.setBackgroundDrawable(new BitmapDrawable());
        tipWindow.setContentView(contentView);

        int screen_pos[] = new int[2];
        anchor.getLocationOnScreen(screen_pos);

        Rect anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0] + anchor.getWidth(), screen_pos[1] - anchor.getHeight());

        contentView.measure(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        int contentViewHeight = contentView.getMeasuredHeight();
        int contentViewWidth = contentView.getMeasuredWidth();
        int position_x = anchor_rect.centerX() - (contentViewWidth / 2);
        int position_y = anchor_rect.top - (anchor_rect.height() / 2);

        tipWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, position_x,position_y);
        SuperFUNC.showKeyBoard(ctx, anchor);
        handler.sendEmptyMessageDelayed(MSG_DISMISS_TOOLTIP, 3500);
    }

    public boolean isTooltipShown() {
        if (tipWindow != null && tipWindow.isShowing())
            return true;
        return false;
    }

    public void dismissTooltip() {
        if (tipWindow != null && tipWindow.isShowing())
            tipWindow.dismiss();
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_TOOLTIP:
                    if (tipWindow != null && tipWindow.isShowing())
                        tipWindow.dismiss();
                    break;
            }
        };
    };
}