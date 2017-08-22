package de.guj.ems.test.app.ThirdParty;

import android.content.Context;

import de.guj.ems.mobile.sdk.util.ThirdPartyTargetingConnector;

/**
 * Created by gohl2 on 24.08.2016.
 */
public class ExternalTargetingConnector extends ThirdPartyTargetingConnector {
    public ExternalTargetingConnector(Context c) {
        super(c);
    }

    protected int getAge() {
        return 29;
    }

    protected int getBirthyear() {
        return 1986;
    }

    protected int getGender() {
        return 1;
    }
}
