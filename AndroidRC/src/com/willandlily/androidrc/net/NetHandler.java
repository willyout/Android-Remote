package com.willandlily.androidrc.net;

import com.willandlily.androidrc.app.AndroidRCActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class NetHandler extends Handler {

	private Activity activity;

	public NetHandler(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case Constants.response:
			((AndroidRCActivity) this.activity).setResponseEditText(message.getData().getString(Constants.resKey));
			((AndroidRCActivity) this.activity).setButton(true);
			break;
		case Constants.timeout:
			((AndroidRCActivity) this.activity).setResponseEditText("Timeout");
			break;
		default:
			break;
		}
	}

	public void sendResponseMessage(String mess) {
		Message message = this.obtainMessage();
		message.what = Constants.response;
		Bundle bundle = new Bundle();
		bundle.putString(Constants.resKey, mess);
		message.setData(bundle);
		this.sendMessage(message);
	}
	
	public void sendDeviceMessage(String mess) {
		Message message = this.obtainMessage();
		message.what = Constants.response;
		Bundle bundle = new Bundle();
		bundle.putString(Constants.resKey, mess);
		message.setData(bundle);
		this.sendMessage(message);
	}
	public void sendTimeOutMessage() {
		Message message = this.obtainMessage();
		message.what = Constants.timeout;
		Bundle bundle = new Bundle();
		message.setData(bundle);
		this.sendMessage(message);
	}

	public void sendMessage(String mess) {
		Message message = this.obtainMessage();
		message.what = Constants.response;
		Bundle bundle = new Bundle();
		bundle.putString(Constants.resKey, mess);
		message.setData(bundle);
		this.sendMessage(message);
	}
}
