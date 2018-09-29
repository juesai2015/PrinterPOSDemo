package com.jgw.printerposdemo.printer;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jgw.printerposdemo.R;

public class BaseActivity extends Activity {
	private static boolean isFirst = true;
	protected static String titleConnectState = "";
	/**
	 * ��������textview
	 */
	protected static TextView headerConnecedState;

	public static String getTitleState() {
		return titleConnectState;
	}

	public static void setTitleState(String titleState) {
		BaseActivity.titleConnectState = titleState;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			setTitleState(getResources().getString(R.string.off_line));
			isFirst = false;
		}
	}

	/**
	 * ����2������������headerview��
	 */
	public void setHeaderLeftImage(LinearLayout header, View.OnClickListener listener) {
		ImageView ivback = (ImageView) header.findViewById(R.id.iv_hearderview_left_image);
		ivback.setVisibility(View.VISIBLE);
		if (listener != null) {
			ivback.setOnClickListener(listener);
		}
		ivback.setImageResource(R.drawable.icon_back);
	}

	public void setHeaderLeftText(LinearLayout header, String title, View.OnClickListener listener) {
		TextView tvText = (TextView) header.findViewById(R.id.tv_headerview_left_text);
		headerConnecedState = (TextView) header.findViewById(R.id.tv_headerview_connected);
		if (TextUtils.isEmpty(title)) {
			tvText.setText("");
		} else {
			tvText.setText(title);
		}
		if (listener != null) {
			tvText.setOnClickListener(listener);
		}

	}

	public void setHeaderCenterText(LinearLayout header, String title) {
		TextView tvText = (TextView) header.findViewById(R.id.tv_headerview_center_text);
		headerConnecedState = (TextView) header.findViewById(R.id.tv_headerview_connected);
		if (TextUtils.isEmpty(title)) {
			tvText.setText("");
		} else {
			tvText.setText(title);
		}

	}

	// 判断当前是否使用的是 WIFI网络
	public static boolean isWifiActive(Context icontext) {
		Context context = icontext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info;
		if (connectivity != null) {
			info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
