package de.guj.ems.mobile.sdk.controllers;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.google.android.gms.ads.doubleclick.PublisherAdView;

import de.guj.ems.mobile.sdk.util.SdkLog;
import de.guj.ems.mobile.sdk.util.SdkUtil;
import de.guj.ems.mobile.sdk.util.ThirdPartyConnector;

/**
 * Add e|MS specific functionality by providing an AppEventListener interface
 * 
 * @author stein16
 * 
 */
public class GuJEMSAdInterface {
	
	public static GuJEMSAdInterface getInstance() {
		if (GuJEMSAdInterface.instance == null) {
			GuJEMSAdInterface.instance = new GuJEMSAdInterface();
		}
		return GuJEMSAdInterface.instance;
	}

	private static GuJEMSAdInterface instance = null;

	private final static String TAG = "GuJEMSAdInterface";

	/**
	 * Makes the phone vibrate once for l milliseconds. If the vibrate
	 * permission is not granted in the AndroidManifest.xml, an exception is
	 * thrown and caught.
	 * 
	 * @param l
	 *            length of vibration
	 */
	@JavascriptInterface
	public void vibrateOnce(long l) {
		SdkLog.i(TAG, "ems_vibrate: " + l + " ms");
		try {
			Context c = SdkUtil.getContext();
			if (c.getPackageManager().checkPermission(permission.VIBRATE,
					c.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
				throw new SecurityException(
						"Vibrate Permission not granted in Manifest");
			}
			Vibrator v = (Vibrator) c
					.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(l);
		} catch (Exception e) {
			SdkLog.e(TAG, "Vibration not possible in this app.", e);
		}
	}

	/**
	 * Makes the phone vibrate as in the pattern given in l. If the vibrate
	 * permission is not granted in the AndroidManifest.xml, an exception is
	 * thrown and caught. The pattern starts with a length of x milliseconds for
	 * an initial pause. Each length is followed by another value indicating the
	 * pause until the next vibration. To vibrate twice for 100ms initially with
	 * a pause of 200ms between the vibration tones, the pattern would thus be
	 * [0,100,200,100].
	 * 
	 * @param l
	 *            pattern of vibration
	 */
	@JavascriptInterface
	public void vibratePattern(long[] l) {
		SdkLog.i(TAG, "ems_vibrate: pattern called.");
		try {
			Context c = SdkUtil.getContext();
			if (c.getPackageManager().checkPermission(permission.VIBRATE,
					c.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
				throw new SecurityException(
						"Vibrate Permission not granted in Manifest");
			}
			Vibrator v = (Vibrator) c
					.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(l, -1);
		} catch (Exception e) {
			SdkLog.e(TAG, "Vibration not possible in this app.", e);
		}
	}

	@JavascriptInterface
	public void hideAdView(PublisherAdView view) {
		SdkLog.i(TAG, "ems_hideadview called.");
		view.setVisibility(View.GONE);
	}

	public void doAppEvent(PublisherAdView adView, String name, String info) {
		if ("vibratePattern".equals(name)) {
			try {
				String [] vals = info.split(",");
				long [] l = new long[vals.length];
				for (int i = 0; i < vals.length; i++) {
					l[i] = Long.parseLong(vals[i]);
				}
				vibratePattern(l);
			}
			catch (Exception e) {
				SdkLog.e(TAG, "Error parsing length value for vibration", e);
			}			
		}
		else if ("vibrateOnce".equals(name)) {
			try {
				long l = Long.parseLong(info);
				vibrateOnce(l);
			}
			catch (Exception e) {
				SdkLog.e(TAG, "Error parsing length value for vibration", e);
			}
		} else if ("hideAdView".equals(name)) {
			hideAdView(adView);
		} else if ("handOverAdViewToFacebook".equals(name)) {
			hideAdView(adView);
			ThirdPartyConnector.getInstance().callByType(ThirdPartyConnector.facebook, adView, info);
		}
	}

}
