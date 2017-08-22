package de.guj.ems.mobile.sdk.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import de.guj.ems.mobile.sdk.controllers.adserver.AdServerSettingsAdapter;
import de.guj.ems.mobile.sdk.controllers.adserver.DFPSettingsAdapter;
import de.guj.ems.mobile.sdk.util.SdkLog;
import de.guj.ems.mobile.sdk.util.SdkUtil;

public class InterstitialSwitchReceiver extends BroadcastReceiver implements
		AppEventListener, IOnAdSuccessListener, IOnAdEmptyListener,
		IOnAdErrorListener {

	private static final long serialVersionUID = -8422088298217436485L;

	private PublisherInterstitialAd interstitial;

	private boolean interstitalBlock = false;

	private final static String TAG = "InterstitialSwitchReceiver";

	private AdServerSettingsAdapter settings;

	private String adUnitId;

	private IOnAdSuccessListener onAdSuccess;

	private IOnAdEmptyListener onAdEmpty;

	private IOnAdErrorListener onAdError;

	private Intent target;

	private Context context;

	private class AdResponseReceiver extends ResultReceiver {

		private InterstitialSwitchReceiver receiver;

		AdResponseReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {

			if (receiver != null) {
				receiver.onReceiveResult(resultCode, resultData);
			}
		}

		void setReceiver(InterstitialSwitchReceiver receiver) {
			this.receiver = receiver;
		}

	}

	private AdResponseReceiver responseReceiver;

	public InterstitialSwitchReceiver() {
		super();
		responseReceiver = new AdResponseReceiver(new Handler());
		responseReceiver.setReceiver(this);
	}

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		if (SdkUtil.getContext() == null) {
			SdkUtil.setContext(arg0);
		}

		this.context = arg0;

		try {
			this.adUnitId = (String) arg1.getExtras().get("adUnitId");
		} catch (Exception e) {
			this.adUnitId = null;
		}

		// original target when interstitial not available
		this.target = (Intent) arg1.getExtras().get("target");
		if (this.target != null) {
			this.target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}

		// ad space settings
		this.settings = new DFPSettingsAdapter();
		this.settings.setup(SdkUtil.getContext(), arg1.getExtras());

		// proxy the events
		if (this.settings.getOnAdSuccessListener() != null) {
			this.onAdSuccess = this.settings.getOnAdSuccessListener();
		}
		if (this.settings.getOnAdErrorListener() != null) {
			this.onAdError = this.settings.getOnAdErrorListener();
		}
		if (this.settings.getOnAdEmptyListener() != null) {
			this.onAdEmpty = this.settings.getOnAdEmptyListener();
		}
		this.settings.setOnAdSuccessListener(this);
		this.settings.setOnAdEmptyListener(this);
		this.settings.setOnAdErrorListener(this);

		try {
			if (arg1.getExtras().containsKey("ems_kw")) {
				this.settings.addCustomRequestParameter("kw", (String) arg1.getExtras().get("ems_kw"));
			}
		} catch (Exception e) {
		}


		// adserver request
		Builder requestBuilder = ((DFPSettingsAdapter) settings)
				.getGoogleRequestBuilder(0);
		interstitial = new PublisherInterstitialAd(arg0);
		if (this.adUnitId != null) {
			interstitial.setAdUnitId("/6032/"+this.adUnitId.replaceAll("/6032/", "").replaceAll("\\/6032\\/", ""));
		} else {
			this.adUnitId = ((DFPSettingsAdapter) settings).mapToDfpAdUnit();
			interstitial.setAdUnitId(this.adUnitId);
		}

		SdkLog.d(TAG, "Using mapped DFP ad unit " + this.adUnitId);

		interstitial.setAdListener(new GuJEMSAdListener(settings));
		interstitial.setAppEventListener(this);
		interstitial.loadAd(requestBuilder.build());

	}

	@Override
	public void onAppEvent(String arg0, String arg1) {
		SdkLog.d(TAG, "Received app event " + arg0 + "(" + arg1 + ")");
		if (arg0.equals("interstitialBlocker")) {
			this.interstitalBlock = true;
		}
		GuJEMSAdInterface.getInstance().doAppEvent(null, arg0, arg1);
	}

	private void onReceiveResult(int resultCode, Bundle resultData) {
		SdkLog.d(TAG, "onReceiveResult " + resultCode + " " + resultData);
	}

	@Override
	public void onAdSuccess() {
		if (this.onAdSuccess != null) {
			this.onAdSuccess.onAdSuccess();
		}

		if (!this.interstitalBlock) {
			interstitial.show();
		} else {
			this.interstitalBlock = false;
		}
	}

	@Override
	public void onAdError(String msg) {
		if (this.onAdError != null) {
			this.onAdError.onAdError(msg);
		}
		if (target != null) {
			this.context.startActivity(target);
		} else {
			SdkLog.i(TAG, "No target. Back to previous view.");
		}
	}

	@Override
	public void onAdError(String msg, Throwable t) {
		if (this.onAdError != null) {
			this.onAdError.onAdError(msg, t);
		}
		if (target != null) {
			this.context.startActivity(target);
		} else {
			SdkLog.i(TAG, "No target. Back to previous view.");
		}
	}

	@Override
	public void onAdEmpty() {
		if (this.onAdEmpty != null) {
			this.onAdEmpty.onAdEmpty();
		}
		if (target != null) {
			this.context.startActivity(target);
		} else {
			SdkLog.i(TAG, "No target. Back to previous view.");
		}
	}

}
