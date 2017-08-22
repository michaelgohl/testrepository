package de.guj.ems.mobile.sdk.views;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.AbsListView;

/**
 * Alternative class for adviews within lists
 * 
 * List view elements must have layout params of type AbsListView.LayoutParams
 * which this class provides.
 * 
 * @author stein16
 * 
 */
public class GuJEMSListAdView extends GuJEMSAdView {

	public GuJEMSListAdView(Context context) {
		super(context);
	}

	public GuJEMSListAdView(Context context, AttributeSet set) {
		super(context, set);
	}

	public GuJEMSListAdView(Context context, AttributeSet set, boolean load) {
		super(context, set, load);
	}

	public GuJEMSListAdView(Context context, int resId) {
		super(context, resId);
	}

	public GuJEMSListAdView(Context context, int resId, boolean load) {
		super(context, resId, load);
	}

	public GuJEMSListAdView(Context context, Map<String, ?> customParams,
			int resId) {
		super(context, customParams, resId);
	}

	public GuJEMSListAdView(Context context, Map<String, ?> customParams,
			int resId, boolean load) {
		super(context, customParams, resId, load);
	}
	
	public GuJEMSListAdView(Context context, String [] keywords,
			int resId, boolean load) {
		super(context, keywords, null, resId, load);
	}
	
	@Override
	public ViewGroup.LayoutParams getNewLayoutParams(int w, int h) {
		return new AbsListView.LayoutParams(w, h);
	}

}
