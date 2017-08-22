package de.guj.ems.test.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.guj.ems.mobile.sdk.views.GuJEMSNativeContentAdView;

public class NativeFragment extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_native, container, false);

        ((Button)rootView.findViewById(R.id.FragmentNativeStart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    LinearLayout rl = (LinearLayout)rootView.findViewById(R.id.FragmentNativeLayout);

                    GuJEMSNativeContentAdView adview = new GuJEMSNativeContentAdView(
                            getActivity(),
                            R.layout.layout_native,
                            false
                    );
                    final TextView message = (TextView) rootView.findViewById(R.id.FragmentNativeTextView);

                    String adUnitId = util.getStringSettingByKey(GlobalData.preferenceAdUnit);
                    adview.setAdUnitId(adUnitId);
                    adview.load();

                    rl.addView(adview, 2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        return rootView;
    }
}
