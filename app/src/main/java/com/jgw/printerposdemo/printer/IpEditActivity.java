package com.jgw.printerposdemo.printer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jgw.printerposdemo.R;
import com.jgw.printerposdemo.printer.global.GlobalContants;


public class IpEditActivity extends Activity {
	// private MyDialog dialog;
	private LinearLayout layout;
	private int[] mIDs = { R.id.ip_edit_1, R.id.ip_edit_2, R.id.ip_edit_3, R.id.ip_edit_4 };
	private EditText ipEdit1, ipEdit2, ipEdit3, ipEdit4;
	private TextView tv_wifi_name;
	private EditText[] ipEdits = { ipEdit1, ipEdit2, ipEdit3, ipEdit4 };
	private String wifiName = null;
	private static String ipAddress = "192.168.0.100";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip_edit);
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		registerReceiver(mReceiver, filter);

		MyTextWatcher[] mTextWatcher = new MyTextWatcher[4];
		for (int i = 0; i < 4; i++) {
			ipEdits[i] = (EditText) findViewById(mIDs[i]);
			mTextWatcher[i] = new MyTextWatcher(ipEdits[i]);
			ipEdits[i].addTextChangedListener(mTextWatcher[i]);
		}

		String[] split = ipAddress.split("\\.");

		Log.i("ip", "split:" + split.length);
		for (int i = 0; i < split.length; i++) {
			ipEdits[i].setText(split[i]);
		}
		// dialog=new MyDialog(this);
		layout = (LinearLayout) findViewById(R.id.exit_layout);
		tv_wifi_name = (TextView) findViewById(R.id.tv_wifi_name);
		// 得到上个activity传回的当前所处wifi的名称
		Intent intent = getIntent();
		wifiName = "current wifi:" + intent.getStringExtra(GlobalContants.WIFINAME);
		tv_wifi_name.setText(wifiName);
		ipEdits[3].requestFocus();

	}

	class MyTextWatcher implements TextWatcher {
		public EditText mEditText;

		public MyTextWatcher(EditText mEditText) {
			super();
			this.mEditText = mEditText;
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (s.length() == 3) {
				if (Integer.parseInt(mEditText.getEditableText().toString()) > 255) {
					mEditText.setText("255");
				}
				if (this.mEditText == ipEdits[0]) {
					ipEdits[1].requestFocus();

				} else if (this.mEditText == ipEdits[1]) {
					ipEdits[2].requestFocus();

				} else if (this.mEditText == ipEdits[2]) {
					ipEdits[3].requestFocus();
				}
				if (this.mEditText == ipEdits[3]) {
					ipEdits[3].setSelection(3);
				}

			}
			// else if (s.length() == 0) {
			// if (this.mEditText == ipEdits[3]) {
			// ipEdits[2].requestFocus();
			// ipEdits[2].setSelection(ipEdits[2].length());
			// } else if (this.mEditText == ipEdits[2]) {
			// ipEdits[1].requestFocus();
			// ipEdits[1].setSelection(ipEdits[1].length());
			// } else if (this.mEditText == ipEdits[1]) {
			// ipEdits[0].requestFocus();
			// ipEdits[0].setSelection(ipEdits[0].length());
			// }
			// }
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

	}

	// 确认wifi热点和打印机IP正确后去连接打印机
	public void connect(View v) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			sb.append(ipEdits[i].getEditableText());
			if (i != 3) {
				sb.append(".");
			}
		}
		Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();

		Intent intent = new Intent();
		intent.putExtra("ip_address", sb.toString());
		ipAddress = sb.toString();
		Log.i("ip", "ip:" + ipAddress);
		// Set result and finish this Activity
		setResult(Activity.RESULT_OK, intent);
		this.finish();
	}

	// 切换wifi热点
	public void switchWiFi(View v) {
		startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {// wifi连接上与否
				Log.i("yxz", "网络状态改变");
				NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
					Log.i("yxz", "wifi网络连接断开");
				} else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {

					WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
					WifiInfo wifiInfo = wifiManager.getConnectionInfo();
					// 获取当前wifi名称
					System.out.println("连接到网络 " + wifiInfo.getSSID());
					wifiName = "current wifi:" + wifiInfo.getSSID();
					tv_wifi_name.setText(wifiName);

				}

			}

		}
	};

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	};
}
