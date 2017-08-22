package de.guj.ems.mobile.sdk.views;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener;
import com.google.android.gms.ads.formats.NativeContentAdView;

import de.guj.ems.mobile.sdk.R;
import de.guj.ems.mobile.sdk.controllers.GuJEMSAdListener;
import de.guj.ems.mobile.sdk.controllers.adserver.DFPSettingsAdapter;
import de.guj.ems.mobile.sdk.util.SdkLog;
import de.guj.ems.mobile.sdk.util.SdkUtil;

public class GuJEMSNativeContentAdView extends GuJEMSAdView {

	private final String TAG = "GuJEMSNativeContentAdView";

	private AdLoader adLoader;

	private DFPSettingsAdapter settings;
	
	private String mAdUnit;
	
	private NativeContentAdView adView;

	public GuJEMSNativeContentAdView(Context context, AttributeSet set, boolean load) {
		super(context, set, load);
	}

	public GuJEMSNativeContentAdView(Context context, AttributeSet set) {
		super(context, set);
	}

	public GuJEMSNativeContentAdView(Context context, int resId, boolean load) {
		super(context, resId, load);
	}

	public GuJEMSNativeContentAdView(Context context, int resId) {
		super(context, resId);
	}

	public GuJEMSNativeContentAdView(Context context, Map<String, ?> customParams,
			int resId, boolean load) {
		super(context, customParams, resId, load);
	}

	public GuJEMSNativeContentAdView(Context context, Map<String, ?> customParams,
			int resId) {
		super(context, customParams, resId);
	}

	public GuJEMSNativeContentAdView(Context context) {
		super(context);
	}

	/**
	 * Translates the ad response to displayed content
	 * @param ad The Google native Ad
	 */
	private void displayContentAd(NativeContentAd ad) {
		SdkLog.d(TAG, settings.hashCode() + " displaying ad " + ad);
		
		// Headline
		if (adView.findViewById(R.id.contentad_headline) != null) {
			adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
			((TextView)adView.getHeadlineView()).setText(ad.getHeadline());
		}
		else {
			SdkLog.e(TAG, settings.hashCode() + " native content ad view has no headline view");
		}

		// Image
		List<NativeAd.Image> images = ad.getImages();
		if (images != null && images.size() > 0 && adView.findViewById(R.id.contentad_image) != null) {
			adView.setImageView(adView.findViewById(R.id.contentad_image));
			((ImageView)adView.getImageView()).setImageDrawable(images.get(0)
					.getDrawable());
		}
		else {
			SdkLog.w(TAG, settings.hashCode() + " there was either no image in the response or no target ImageView in the layout.");
		}

		// Advertiser
		if (adView.findViewById(R.id.contentad_advertiser) != null) {
			adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser) );
			((TextView) adView.getAdvertiserView()).setText(ad.getAdvertiser());
		}
		else {
			SdkLog.e(TAG, settings.hashCode() + " native content ad view has no advertiser view");
		}		

		// Body
		if (adView.findViewById(R.id.contentad_body) != null) {
			adView.setBodyView(adView.findViewById(R.id.contentad_body));
			((TextView) adView.getBodyView()).setText(ad.getBody());
		}
		else {
			SdkLog.e(TAG, settings.hashCode() + " native content ad view has no body view");
		}
		
		// Call to action
		if (adView.findViewById(R.id.contentad_call_to_action) != null) {
			adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
			((TextView) adView.getCallToActionView()).setText(ad.getCallToAction());
		}
		else {
			SdkLog.e(TAG, settings.hashCode() + " native content ad view has no call to action view");
		}
		
		// Logo
		NativeAd.Image logo = ad.getLogo();
		if (logo != null && adView.findViewById(R.id.contentad_logo) != null) {
			adView.setLogoView(adView.findViewById(R.id.contentad_logo));
			((ImageView) adView.getLogoView()).setImageDrawable(logo
					.getDrawable());
		}
		else {
			SdkLog.w(TAG, settings.hashCode() + " there was either no logo in the response or no target ImageView in the layout.");
		}

		adView.setNativeAd(ad);
		this.addView(adView);
		setVisibility(View.VISIBLE);
	}

	@Override
	protected void preLoadInitialize(Context context, AttributeSet set,
			String[] kws, String[] nkws) {
		setVisibility(View.GONE);
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		this.adView = (NativeContentAdView) inflater.inflate(
				R.layout.ems_nativead, (ViewGroup)getParent());
		
		TypedArray tVals = context.obtainStyledAttributes(set,
				R.styleable.GuJEMSNativeContentAdView);
		String adUnit = tVals
				.getString(R.styleable.GuJEMSNativeContentAdView_ems_adUnit);
		tVals.recycle();

		if (set != null && !isInEditMode()) {
			this.settings = new DFPSettingsAdapter();
			this.settings.setup(context, set, kws);
			if (adUnit != null) {
				setAdUnitId(adUnit, 1);
			}			
		}

		if (this.mAdUnit != null) {
			this.setAdLoader(context);
		}

		if (nkws != null) {
			SdkLog.w(TAG, "Negative keywords are no longer supported");
		}
		
		SdkLog.d(TAG, settings.hashCode() + " native ad loader initialized");
	}

	@Override
	protected void preLoadInitialize(Context context, AttributeSet set) {
		setVisibility(View.GONE);
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.adView = (NativeContentAdView) inflater.inflate(
				R.layout.ems_nativead, (ViewGroup)getParent());
		
		TypedArray tVals = context.obtainStyledAttributes(set,
				R.styleable.GuJEMSNativeContentAdView);
		String adUnit = tVals
				.getString(R.styleable.GuJEMSNativeContentAdView_ems_adUnit);
		tVals.recycle();
		
		if (set != null && !isInEditMode()) {
			this.settings = new DFPSettingsAdapter();
			this.settings.setup(context, set);
			if (adUnit != null) {
				this.setAdUnitId(adUnit, 1);
			}
		}

		if (this.mAdUnit != null) {
			this.setAdLoader(context);
		}
		
		SdkLog.d(TAG, settings.hashCode() + " native ad loader initialized");
	}

	private void setAdLoader(Context context) {
		this.adLoader = new AdLoader.Builder(context, mAdUnit != null ? mAdUnit : settings.mapToDfpAdUnit())
				.forContentAd(new OnContentAdLoadedListener() {
					@Override
					public void onContentAdLoaded(NativeContentAd contentAd) {
					displayContentAd(contentAd);
					}
				}).withAdListener(new GuJEMSAdListener(settings))
			.build();
	}

	@Override
	public void load() {
		if (settings != null) {
			SdkUtil.setContext(getContext());
			// Start request if online
			if (SdkUtil.isOnline()) {
				SdkLog.i(TAG, settings.hashCode() + " starting ad request");

				Builder requestBuilder = ((DFPSettingsAdapter) settings)
						.getGoogleRequestBuilder(1, this.hasAdUnitId);
				this.adLoader.loadAd(requestBuilder.build());

			}
			// Do nothing if offline
			else {
				SdkLog.i(TAG, "No network connection - not requesting ads.");
				setVisibility(GONE);
			}
		} else {
			SdkLog.w(TAG, settings.hashCode() + " adView has no settings.");
		}
	}
	
	/**
	 * If you have a Google adUnitId instead of an EMS zoneId, you can set it
	 * here instead of having them mapped to Google DFP.
	 * 
	 * @param adUnitId
	 */
	public void setAdUnitId(String adUnitId) {
		this.setAdUnitId(adUnitId, 1);
	}

	public void setAdUnitId(String adUnitId, int position) {
		this.mAdUnit = getContext().getResources().getString(R.string.ems_dfpNetwork) + adUnitId.replaceAll("/6032/", "").replaceAll("\\/6032\\/", "");
		this.setAdLoader(getContext());
		this.settings.addCustomRequestParameter("pos", position);
		this.hasAdUnitId = true;
	}
}
