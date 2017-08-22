package de.guj.ems.mobile.sdk.controllers.adserver;

import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import de.guj.ems.mobile.sdk.controllers.IOnAdEmptyListener;
import de.guj.ems.mobile.sdk.controllers.IOnAdErrorListener;
import de.guj.ems.mobile.sdk.controllers.IOnAdSuccessListener;

/**
 * Interface for the handling of data and settings to finally construct an
 * adserver request.
 * 
 * @author stein16
 * 
 */
public interface IAdServerSettingsAdapter {

	/**
	 * Add a map of custom params to the request. Only String, Integer, Double
	 * allowed.
	 * 
	 * @param params
	 *            Map of parameter names and values
	 */
	public void addCustomParams(Map<String, ?> params);

	/**
	 * Add any custom parameter to the ad request
	 * 
	 * @warn this may override existing parameters so use with caution
	 * @param param
	 *            name of http parameter
	 * @param value
	 *            value of http parameter
	 */
	public void addCustomRequestParameter(String param, double value);

	/**
	 * Add any custom parameter to the ad request
	 * 
	 * @warn this may override existing parameters so use with caution
	 * @param param
	 *            name of http parameter
	 * @param value
	 *            value of http parameter
	 */
	public void addCustomRequestParameter(String param, int value);

	/**
	 * Add any custom parameter to the ad request
	 * 
	 * @warn this may override existing parameters so use with caution
	 * @param param
	 *            name of http parameter
	 * @param value
	 *            value of http parameter
	 */
	public void addCustomRequestParameter(String param, String value);

	/**
	 * Add a predefined string which is appended to the servlet url
	 * 
	 * @param str
	 *            query string extension
	 */
	public void addQueryAppendix(String str);

	/**
	 * Mark settings as processed
	 */
	public void dontProcess();

	/**
	 * Determine whether the settings may be overridden by local json config
	 * 
	 * @return
	 */
	public boolean doProcess();

	/**
	 * Returns a listener object if defined
	 * 
	 * @return listener which reacts to non existant ad
	 */
	public IOnAdEmptyListener getOnAdEmptyListener();

	/**
	 * Returns a listener object if defined
	 * 
	 * @return listener which reacts to ad server errors
	 */
	public IOnAdErrorListener getOnAdErrorListener();

	/**
	 * Returns a listener object if defined
	 * 
	 * @return listener which reacts to successfully loaded ad
	 */
	public IOnAdSuccessListener getOnAdSuccessListener();

	/**
	 * Retrieve a map of all actual request params defined in the settings
	 *
	 * @return map with all configured param values
	 */
	public Map<String, String> getParams();

	/**
	 * Returns an appending string to the query string
	 * 
	 * @return query string extension
	 */
	public String getQueryAppendix();

	/**
	 * Maps a settings attribute to an adserver parameter. For example the
	 * intern parameter "uid" may mapped to the adserver's parameter name "u".
	 * 
	 * @param attr
	 *            the attribute name
	 * @param param
	 *            the adserver's parameter name
	 */
	public void putAttrToParam(String attr, String param);

	/**
	 * Puts a value to an attribute. E.g. the value "999" to the attribute
	 * "zoneId"
	 * 
	 * @param attr
	 *            the attribute name
	 * @param value
	 *            the value
	 */
	public void putAttrValue(String attr, String value);

	/**
	 * Override the listener class
	 * 
	 * @param l
	 *            implementation of listener which reacts to empty ad responses
	 */
	public void setOnAdEmptyListener(IOnAdEmptyListener l);

	/**
	 * Override the listener class
	 * 
	 * @param l
	 *            implementation of listener which reacts to ad server errors
	 */
	public void setOnAdErrorListener(IOnAdErrorListener l);

	/**
	 * Override the listener class
	 * 
	 * @param l
	 *            implementation of listener which reacts to successful ad
	 *            loading
	 */
	public void setOnAdSuccessListener(IOnAdSuccessListener l);

	/**
	 * Initialize view type and declaration specific settings
	 * 
	 * @param context
	 *            app context
	 * @param set
	 *            attributes from xml
	 */
	public void setup(Context context, AttributeSet set);

	/**
	 * Initialize view type and declaration specific settings
	 * 
	 * @param context
	 *            app context
	 * @param savedInstance
	 *            saved attributes
	 */
	public void setup(Context context, Bundle savedInstance);
	
	/**
	 * Initialize view type and declaration specific settings
	 * 
	 * @param context
	 *            app context
	 * @param savedInstance
	 *            saved attributes
	 * @param keywords
	 * 				additional keywords to pass to the adserver            
	 */
	public void setup(Context context, Bundle savedInstance, String [] keywords);
	
	/**
	 * Initialize view type and declaration specific settings
	 * 
	 * @param context
	 *            app context
	 * @param set
	 *            saved attributes
	 * @param keywords
	 * 				additional keywords to pass to the adserver            
	 */
	public void setup(Context context, AttributeSet set, String [] keywords);		

}
