package de.guj.ems.test.app.ThirdParty;

import android.widget.Toast;
import android.content.Context;

import de.guj.ems.mobile.sdk.util.ThirdPartyConnector;
import de.guj.ems.mobile.sdk.util.ThirdPartyConnectorInterface;

/**
 * Created by gohl2 on 23.08.2016.
 */
public class TeadsConnector implements ThirdPartyConnectorInterface {
    private Context context = null;

    public TeadsConnector(Context c) {
        this.context = c;
    }

    public boolean isTypeOf(int t) {
        return ThirdPartyConnector.teads == t;
    }

    public void call(Object... o) {
        Toast toast = Toast.makeText(this.context, "Teads kann gestartet werden", Toast.LENGTH_LONG);
        toast.show();
    }
}
