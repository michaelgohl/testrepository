package de.guj.ems.test.app;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import de.guj.ems.mobile.sdk.controllers.IOnAdEmptyListener;
import de.guj.ems.mobile.sdk.controllers.IOnAdErrorListener;
import de.guj.ems.mobile.sdk.controllers.IOnAdSuccessListener;
import de.guj.ems.mobile.sdk.util.SdkLog;

/**
 * Created by gohl2 on 17.11.2015.
 */
public class ListenerEvents implements IOnAdSuccessListener, IOnAdEmptyListener, IOnAdErrorListener, Serializable {

    private static View View = null;
    private static int messageBox = -1;

    public ListenerEvents(View tv, int id) {
        this.View = tv;
        this.messageBox = id;
    }

    public ListenerEvents() {

    }

    @Override
    public void onAdSuccess() {
        if (this.View != null) {
            ((TextView)this.View.findViewById(this.messageBox)).setText("Ad loaded successfully");
        }
        SdkLog.d("AdEventListener", "Ad loaded");
    }

    @Override
    public void onAdEmpty() {
        if (this.View != null) {
            ((TextView)this.View.findViewById(this.messageBox)).setTextColor(Color.YELLOW);
            ((TextView)this.View.findViewById(this.messageBox)).setText("Ad is empty");
        }
        SdkLog.d("AdEventListener", "Ad is empty");
    }

    @Override
    public void onAdError(String msg) {
        if (this.View != null) {
            ((TextView)this.View.findViewById(this.messageBox)).setTextColor(Color.RED);
            ((TextView)this.View.findViewById(this.messageBox)).setText("Ad error: " + msg);
        }
        SdkLog.d("AdEventListener", "Ad error: "+msg);
    }

    @Override
    public void onAdError(String msg, Throwable t) {
        if (this.View != null) {
            ((TextView)this.View.findViewById(this.messageBox)).setTextColor(Color.RED);
            ((TextView)this.View.findViewById(this.messageBox)).setText("Ad error: " + msg);
        }
        SdkLog.d("AdEventListener", "Ad error: " + msg);
    }
}
