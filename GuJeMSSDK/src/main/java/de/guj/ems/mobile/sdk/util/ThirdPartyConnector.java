package de.guj.ems.mobile.sdk.util;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by gohl2 on 23.08.2016.
 */
public class ThirdPartyConnector {
    private static ThirdPartyConnector ourInstance = new ThirdPartyConnector();

    public final static int facebook = 1;
    public final static int targeting = 2;
    public final static int teads = 3;

    private ArrayList<ThirdPartyConnectorInterface> callbacks = new ArrayList<ThirdPartyConnectorInterface>();

    public void registerCallback(ThirdPartyConnectorInterface c) {
        if (!callbacks.contains(c)) {
            callbacks.add(c);
        }
    }
    public void removeCallback(ThirdPartyConnectorInterface c) {
        callbacks.remove(c);
    }

    public static ThirdPartyConnector getInstance() {
        return ourInstance;
    }

    public void callByType(int type, Object... o) {
        if (!callbacks.isEmpty()) {
            for (int i = 0; i < callbacks.size(); i++) {
                ThirdPartyConnectorInterface tpc = callbacks.get(i);
                if (tpc.isTypeOf(type)) {
                    tpc.call(o);
                }
            }
        }
    }
}
