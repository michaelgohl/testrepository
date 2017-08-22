package de.guj.ems.mobile.sdk.util;

import android.content.Context;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder;

/**
 * Created by gohl2 on 24.08.2016.
 */
public abstract class ThirdPartyTargetingConnector implements ThirdPartyConnectorInterface {
    private Context context = null;

    protected abstract int getGender();
    protected abstract int getBirthyear();
    protected abstract int getAge();

    public ThirdPartyTargetingConnector(Context c) {
        this.context = c;
    }

    public final boolean isTypeOf(int t) {
        return ThirdPartyConnector.targeting == t;
    }

    /**
     * @param o
     * [0] = adRequestBilder
     */
    public final void call(Object... o) {
        int g = this.getGender();
        int a = this.getAge();
        int b = this.getBirthyear();
        if (g > 0) {
            o[0] = ((Builder)o[0]).addCustomTargeting("mdg", g == 1 ? "m" : "f");
        }
        if (b > 0) {
            o[0] = ((Builder)o[0]).addCustomTargeting("mdb", String.valueOf(b));
        }
        if (a > 0) {
            o[0] = ((Builder)o[0]).addCustomTargeting("mda", String.valueOf(a));
        }
    }
}
