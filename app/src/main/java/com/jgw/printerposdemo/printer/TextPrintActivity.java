package com.jgw.printerposdemo.printer;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.jgw.printerposdemo.R;
import com.jgw.printerposdemo.printer.utils.CodePageUtils;
import com.jgw.printerposdemo.printer.utils.XTUtils;
import com.printer.sdk.Barcode;
import com.printer.sdk.CodePagePrinter;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterConstants.BarcodeType;
import com.printer.sdk.PrinterConstants.Command;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.utils.XLog;

public class TextPrintActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

	private static final String TAG = "TextPrintActivity";
	private LinearLayout header;
	private Button btn_send, btn_print_note, btn_print_codepaper;
	private ToggleButton tb_isHexData;
	private EditText et_input;
	private boolean isHexData = false;
	private static PrinterInstance mPrinter;
	private String input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		XLog.d(TAG, "yxz at TextPrintActivity.java onCreate()");
		setContentView(R.layout.activity_print_text);
		init();
		mPrinter = PrinterInstance.mPrinter;
		input = et_input.getText().toString();
		et_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				input = et_input.getText().toString();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void init() {

		et_input = (EditText) findViewById(R.id.et_input);
		et_input.setText(R.string.textprintactivty_input_content);
		header = (LinearLayout) findViewById(R.id.ll_headerview_textPrint);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
		btn_print_note = (Button) findViewById(R.id.btn_print_note);
		btn_print_note.setOnClickListener(this);
		btn_print_codepaper = (Button) findViewById(R.id.btn_print_codepaper);
		btn_print_codepaper.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		tb_isHexData = (ToggleButton) findViewById(R.id.tb_hex_on);
		tb_isHexData.setOnCheckedChangeListener(this);
		isHexData = tb_isHexData.isChecked();
		initHeader();
	}

	/**
	 * 初始化标题上的信息
	 */
	private void initHeader() {
		setHeaderLeftText(header, getString(R.string.back), new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		headerConnecedState.setText(getTitleState());
		setHeaderCenterText(header, getString(R.string.headview_TextPrint));
		setHeaderLeftImage(header, new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public void onClick(View view) {
		if (PrinterInstance.mPrinter != null && SettingActivity.isConnected) {
			if (view == btn_send) {

				String content = et_input.getText().toString();
				Log.i(TAG, content);
				if (content != null || content.length() != 0) {

					if (isHexData && SettingActivity.isConnected) {
						byte[] srcData = XTUtils.string2bytes2(content);
						mPrinter.sendBytesData(srcData);
					} else if (!isHexData && SettingActivity.isConnected) {
						// // PrinterInstance.mPrinter.printText(content +
						// "\r\n");
						mPrinter.printText(content + "\r\n");
						Locale locale = Locale.getDefault();
						String language = locale.getLanguage();
						String country = locale.getCountry();
					}
				}

			} else if (view == btn_print_note) {
				new Thread(new Runnable() {
					public void run() {
						XTUtils.printNote(TextPrintActivity.this.getResources(), mPrinter);
					}
				}).start();
			} else if (view == btn_print_codepaper) {
				new CodePageUtils().selectCodePage(this, PrinterInstance.mPrinter);
			}
		} else {
			Toast.makeText(TextPrintActivity.this, getString(R.string.no_connected), 0).show();
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (isChecked) {// 16进制开
			isHexData = true;
			byte[] datas;
			try {
				datas = input.getBytes("GBK");
				et_input.setText(XTUtils.bytesToHexString2(datas, datas.length));
				input = et_input.getText().toString();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			isHexData = false;
			if (input == null || input.length() == 0) {
				et_input.setText(R.string.textprintactivty_input_content);
			} else {
				et_input.setText(XTUtils.hexStringToString2(input));
				input = et_input.getText().toString();
			}
		}

	}

}
