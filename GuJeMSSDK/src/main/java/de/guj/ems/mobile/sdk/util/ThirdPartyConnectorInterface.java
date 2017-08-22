package de.guj.ems.mobile.sdk.util;

/**
 * Created by gohl2 on 23.08.2016.
 */
public interface ThirdPartyConnectorInterface {
    public boolean isTypeOf(int t);
    public void call(Object... o);
}
