package com.willandlily.androidrc.app;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
	AndroidRCApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (AndroidRCApplication)getApplication();
	}
	
}
