package com.willandlily.androidrc.app;

import com.willandlily.androidrc.data.Host;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class AndroidRCApplication extends Application implements OnSharedPreferenceChangeListener {

	private SharedPreferences pref;
	private Host host;

	@Override
	public void onCreate() {
		super.onCreate();
		this.pref = PreferenceManager.getDefaultSharedPreferences(this);
		this.pref.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		this.host = null;
	}

	public synchronized Host getHost() {
		if (this.host == null) {
			String hostIP = this.pref.getString("hostIP", null);
			String hostPort = this.pref.getString("hostPort", null);
			if (!TextUtils.isEmpty(hostIP) && !TextUtils.isEmpty(hostPort)) {
				this.host = new Host(hostIP, hostPort);
			}
		}
		return this.host;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
