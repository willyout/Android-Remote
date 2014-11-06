package com.willandlily.androidrc.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Handler;

public class NetThread extends Thread {

	private Handler handler;
	private String cmd;
	private DefaultHttpClient httpClient;

	public NetThread(Handler handler) {
		super();
		this.handler = handler;
	}

	public NetThread(Handler handler, DefaultHttpClient httpClient) {
		this(handler);
		this.httpClient = httpClient;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (this) {
				try {
					if (this.cmd == null)
						wait();
					HttpGet httpGet = new HttpGet(cmd);

					HttpResponse httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						((NetHandler) handler).sendMessage(EntityUtils
								.toString(httpResponse.getEntity()));
					}
				} catch (Exception e) {
					((NetHandler) handler).sendMessage(e.toString());
				} finally {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
