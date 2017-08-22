package de.guj.ems.test.app;

import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar;

public class TabListener implements ActionBar.TabListener {

	private Fragment fragment;
	
	// The contructor.
	public TabListener(Fragment fragment) {
		this.fragment = fragment;
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.activity_main, fragment);		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub		
	}
}
