package de.guj.ems.test.app.ThirdParty;

import android.content.Context;
import android.widget.Toast;

import de.guj.ems.mobile.sdk.util.ThirdPartyConnector;
import de.guj.ems.mobile.sdk.util.ThirdPartyConnectorInterface;

/**
 * Created by gohl2 on 23.08.2016.
 */
public class FacebookAudienceConnector implements ThirdPartyConnectorInterface {
    private Context context = null;

    public FacebookAudienceConnector(Context c) {
        this.context = c;
    }

    public boolean isTypeOf(int t) {
        return ThirdPartyConnector.facebook == t;
    }

    public void call(Object... o) {
        Toast toast = Toast.makeText(this.context, "Facebook Placement Id: "+((String) o[1]), Toast.LENGTH_LONG);
        toast.show();
    }

}
