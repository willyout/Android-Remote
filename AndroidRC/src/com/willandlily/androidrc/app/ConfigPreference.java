package com.willandlily.androidrc.app;

import com.willandlily.androidrc.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ConfigPreference extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);
	}

}
