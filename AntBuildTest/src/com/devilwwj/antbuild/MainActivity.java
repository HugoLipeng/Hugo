package com.devilwwj.antbuild;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity {
	
	private TextView deviceInfoText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MobclickAgent.updateOnlineConfig(this);
		
		deviceInfoText = (TextView) findViewById(R.id.deviceInfoTv);
		
		deviceInfoText.setText(Utils.getDeviceInfo(this));
		// 打印设备信息
		Log.d("deviceInfo:", Utils.getDeviceInfo(this));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
