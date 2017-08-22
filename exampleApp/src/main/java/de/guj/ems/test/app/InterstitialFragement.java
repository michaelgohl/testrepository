package de.guj.ems.test.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.Serializable;

import de.guj.ems.mobile.sdk.controllers.InterstitialSwitchReceiver;

public class InterstitialFragement extends Fragment implements Serializable {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_interstitial, container, false);

        Button click = (Button)rootView.findViewById(R.id.buttonInterstitial);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String adUnitId = util.getStringSettingByKey(GlobalData.preferenceAdUnit);
                    Intent i = new Intent(getActivity(),
                            InterstitialSwitchReceiver.class);
                    i.putExtra("adUnitId", adUnitId);
                    i.putExtra("ems_geo", Boolean.valueOf(true));

                    ListenerEvents le = new ListenerEvents(
                            rootView, R.id.FragmentInterstitialTextView);
                    i.putExtra("ems_onAdSuccess", le);
                    i.putExtra("ems_onAdEmpty", le);
                    i.putExtra("ems_onAdError", le);

                    getActivity().sendBroadcast(i);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }
}
