package de.guj.ems.test.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);		
		
		TextView adUnit = (TextView)findViewById(R.id.settingsValueAdUnit);
		ToggleButton isIndex = (ToggleButton)findViewById(R.id.settingsValueIndex);
		// Inital Set of Settings
		try {
			adUnit.setText(util.getStringSettingByKey(GlobalData.preferenceAdUnit));
			isIndex.setChecked(util.getBooleanSettingByKey(GlobalData.preferenceIndex));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		loadBannerSettings(
				1,
				R.id.settingsRectangle1,
				R.id.settingsBillboard1,
				R.id.settingsDesktopBillboard1,
				R.id.settingsLeaderboard1,
				R.id.settingsTwoOnOne1,
				R.id.settingsThreeOnOne1,
				R.id.settingsContentUrl1
		);
		loadBannerSettings(
				2,
				R.id.settingsRectangle2,
				R.id.settingsBillboard2,
				R.id.settingsDesktopBillboard2,
				R.id.settingsLeaderboard2,
				R.id.settingsTwoOnOne2,
				R.id.settingsThreeOnOne2,
				R.id.settingsContentUrl2
		);
		loadBannerSettings(
				3,
				R.id.settingsRectangle3,
				R.id.settingsBillboard3,
				R.id.settingsDesktopBillboard3,
				R.id.settingsLeaderboard3,
				R.id.settingsTwoOnOne3,
				R.id.settingsThreeOnOne3,
				R.id.settingsContentUrl3
		);
		loadBannerSettings(
				10,
				R.id.settingsRectangle10,
				R.id.settingsBillboard10,
				R.id.settingsDesktopBillboard10,
				R.id.settingsLeaderboard10,
				R.id.settingsTwoOnOne10,
				R.id.settingsThreeOnOne10,
				R.id.settingsContentUrl10
		);
		
		// On Button Save click, save preferences
		Button button = (Button)findViewById(R.id.settingsSubmit);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView adUnit = (TextView)findViewById(R.id.settingsValueAdUnit);
				ToggleButton isIndex = (ToggleButton)findViewById(R.id.settingsValueIndex);
				
				try {
					util.setStringSetting(GlobalData.preferenceAdUnit, adUnit.getText().toString());
					util.setBooleanSetting(GlobalData.preferenceIndex, isIndex.isChecked());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				saveBannerSettings(
						1,
						R.id.settingsRectangle1,
						R.id.settingsBillboard1,
						R.id.settingsDesktopBillboard1,
						R.id.settingsLeaderboard1,
						R.id.settingsTwoOnOne1,
						R.id.settingsThreeOnOne1,
						R.id.settingsContentUrl1
				);
				saveBannerSettings(
						2,
						R.id.settingsRectangle2,
						R.id.settingsBillboard2,
						R.id.settingsDesktopBillboard2,
						R.id.settingsLeaderboard2,
						R.id.settingsTwoOnOne2,
						R.id.settingsThreeOnOne2,
						R.id.settingsContentUrl2
				);
				saveBannerSettings(
						3,
						R.id.settingsRectangle3,
						R.id.settingsBillboard3,
						R.id.settingsDesktopBillboard3,
						R.id.settingsLeaderboard3,
						R.id.settingsTwoOnOne3,
						R.id.settingsThreeOnOne3,
						R.id.settingsContentUrl3
				);
				saveBannerSettings(
						10,
						R.id.settingsRectangle10,
						R.id.settingsBillboard10,
						R.id.settingsDesktopBillboard10,
						R.id.settingsLeaderboard10,
						R.id.settingsTwoOnOne10,
						R.id.settingsThreeOnOne10,
						R.id.settingsContentUrl10
				);
				
				finish();
				
			}
		});
		
	}

	private void loadBannerSettings(int pos, int posRect, int posBB, int posDB, int posLB, int posTO, int posThO, int cUrl) {
		try {
			((CheckBox) findViewById(posRect)).setChecked(util.getBooleanSettingByKey(
					GlobalData.preferenceBannerRectangle + pos
			));
			((CheckBox) findViewById(posBB)).setChecked(util.getBooleanSettingByKey(
					GlobalData.preferenceBannerBillboard + pos
			));
			((CheckBox) findViewById(posDB)).setChecked(util.getBooleanSettingByKey(
					GlobalData.preferenceBannerDesktopBillboard + pos
			));
			((CheckBox) findViewById(posLB)).setChecked(util.getBooleanSettingByKey(
					GlobalData.preferenceBannerLeaderboard + pos
			));
			((CheckBox) findViewById(posTO)).setChecked(util.getBooleanSettingByKey(
					GlobalData.preferenceBannerTwoOnOne + pos
			));
			((CheckBox) findViewById(posThO)).setChecked(util.getBooleanSettingByKey(
					GlobalData.preferenceBannerThreeOnOne + pos
			));
			((EditText) findViewById(cUrl)).setText(util.getStringSettingByKey(
					GlobalData.preferenceContentUrl + pos
			));


		}
		catch (Exception e) {}
	}

	private void saveBannerSettings(int pos, int posRect, int posBB, int posDB, int posLB, int posTO, int posThO, int cUrl) {

		try {
			util.setBooleanSetting(
					GlobalData.preferenceBannerRectangle + pos,
					((CheckBox) findViewById(posRect)).isChecked()
			);
			util.setBooleanSetting(
					GlobalData.preferenceBannerBillboard + pos,
					((CheckBox) findViewById(posBB)).isChecked()
			);
			util.setBooleanSetting(
					GlobalData.preferenceBannerDesktopBillboard + pos,
					((CheckBox) findViewById(posDB)).isChecked()
			);
			util.setBooleanSetting(
					GlobalData.preferenceBannerLeaderboard + pos,
					((CheckBox) findViewById(posLB)).isChecked()
			);
			util.setBooleanSetting(
					GlobalData.preferenceBannerTwoOnOne + pos,
					((CheckBox) findViewById(posTO)).isChecked()
			);
			util.setBooleanSetting(
					GlobalData.preferenceBannerThreeOnOne + pos,
					((CheckBox) findViewById(posThO)).isChecked()
			);
			util.setStringSetting(
					GlobalData.preferenceContentUrl + pos,
					((EditText) findViewById(cUrl)).getText().toString()
			);
		}
		catch (Exception e) {}
	}

}
