package de.guj.ems.test.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import de.guj.ems.mobile.sdk.util.ThirdPartyConnector;
import de.guj.ems.mobile.sdk.views.video.GuJEMSInFlowView;
import de.guj.ems.test.app.ThirdParty.TeadsConnector;


public class InFlowFragment extends android.app.Fragment implements Serializable {
    View currentView = null;
    private TeadsConnector tc = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_inflow, container, false);
        this.currentView = rootView;

        if (tc == null) {
            tc = new TeadsConnector(getContext());
        }

        try {
            String adUnitId = util.getStringSettingByKey(GlobalData.preferenceAdUnit);
            GuJEMSInFlowView inflow = (GuJEMSInFlowView)rootView.findViewById(R.id.InFlowPlayer);
            inflow.setColorToButtons("#00a600");
            ThirdPartyConnector.getInstance().registerCallback(tc);
            inflow.setAdUnit(adUnitId);
        }catch(Exception e) {
            e.printStackTrace();
        }



        return rootView;
    }

    @Override
    public void onDestroyView() {
        ThirdPartyConnector.getInstance().removeCallback(tc);
        super.onDestroyView();
    }
}

