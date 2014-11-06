package com.willandlily.androidrc.app;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.willandlily.androidrc.R;
import com.willandlily.androidrc.net.NetHandler;
import com.willandlily.androidrc.net.NetThread;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AndroidRCActivity extends BaseActivity implements OnClickListener {

	Button buttonSend;
	Button buttonClear;
	Button buttonConfig;
	Button buttonDevice;
	Thread thread;
	EditText textCommand;
	EditText textResponse;
	Handler handler;
	DefaultHttpClient httpClient;
	Activity self;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
		setContentView(R.layout.main);
		Init();
	}

	private void Init() {
		httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
		handler = new NetHandler(this);
		thread = new NetThread(handler, httpClient);

		buttonSend = (Button) findViewById(R.id.ButtonSend);
		buttonClear = (Button) findViewById(R.id.buttonClear);
		buttonConfig = (Button) findViewById(R.id.buttonConfig);
		buttonDevice = (Button) findViewById(R.id.buttonDevice);

		textCommand = (EditText) findViewById(R.id.editText1);
		textResponse = (EditText) findViewById(R.id.editText2);

		buttonConfig.setOnClickListener(this);
		buttonSend.setOnClickListener(this);
		buttonClear.setOnClickListener(this);
		buttonDevice.setOnClickListener(this);

		thread.start();

	}

	public void codeBackUp() {
		buttonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				textResponse.setText("");
				String req = textCommand.getText().toString().trim();
				if (!TextUtils.isEmpty(req)) {
					new Thread(new NetThread(handler)).start();
				}
			}
		});

		buttonClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				textCommand.setText("");
				textResponse.setText("");
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (application.getHost() == null) {
			showConfigActivity();
		}
		switch (v.getId()) {
		case R.id.buttonConfig:
			showConfigActivity();
			break;
		case R.id.ButtonSend:
			textResponse.setText("");
			String req = textCommand.getText().toString().trim();
			if (!TextUtils.isEmpty(req)) {
				setButton(false);
				Log.e("will", application.getHost().getCmdURL(req));
				((NetThread) thread).setCmd(application.getHost().getCmdURL(req));
				synchronized (thread) {
					thread.notify();
				}
				/* two other ways to send data
				new PostCommand().execute(application.getHost().getURL(req));
				new Thread(netThread).start();
				 */
			}
			break;
		case R.id.buttonClear:
			textCommand.setText("");
			textResponse.setText("");
			break;
		case R.id.buttonDevice:
			setButton(false);
			Log.e("will", application.getHost().getServerURL());
			((NetThread) thread).setCmd(application.getHost().getServerURL());
			synchronized (thread) {
				thread.notify();
			}
			break;
		default:
			break;
		}
	}
	
	public void setButton(boolean enable) {
		this.buttonDevice.setEnabled(enable);
		this.buttonSend.setEnabled(enable);
	}

	public void setResponseEditText(String res) {
		this.textResponse.setText(res);
	}

	public void showConfigActivity() {
		Intent intent = new Intent(this, ConfigPreference.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
	}

	public class PostCommand extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			String response = null;
			HttpGet httpGet = new HttpGet(params[0]);
			try {
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					response = EntityUtils.toString(httpResponse.getEntity());
				}
			} catch (Exception e) {
				response = e.toString();
			}
			return response;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			((AndroidRCActivity) self).setResponseEditText(result);
			((AndroidRCActivity) self).setButton(true);
		}

	}
}