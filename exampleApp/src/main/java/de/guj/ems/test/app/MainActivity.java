package de.guj.ems.test.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import de.guj.ems.mobile.sdk.util.SdkUtil;


public class MainActivity extends Activity {

	ActionBar.Tab BannerTab, InterstitialTab, VideoTab, NativeTab, MData, InFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init util class
        util.init(this);
        SdkUtil.setContext(getApplicationContext()); 
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        BannerTab = actionBar.newTab().setText(R.string.TabBanner);
        InterstitialTab = actionBar.newTab().setText(R.string.TabInter);
        VideoTab = actionBar.newTab().setText(R.string.TabVideo);
        NativeTab = actionBar.newTab().setText(R.string.TabNative);
        InFlow = actionBar.newTab().setText(R.string.TabInFlow);
        
		BannerTab.setTabListener(new TabListener(new BannerFragment()));
        InterstitialTab.setTabListener(new TabListener(new InterstitialFragement()));
        VideoTab.setTabListener(new TabListener(new VideoFragment()));
        NativeTab.setTabListener(new TabListener(new NativeFragment()));
        InFlow.setTabListener(new TabListener(new InFlowFragment()));
        
        actionBar.addTab(BannerTab);
        actionBar.addTab(InterstitialTab);
        actionBar.addTab(VideoTab);
        actionBar.addTab(NativeTab);
        actionBar.addTab(InFlow);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        int height = getWindow().getDecorView().getHeight();
        int width = getWindow().getDecorView().getWidth();
        menu.add("Screen Width: "+width);
        menu.add("Screen Height: "+height);

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
