package com.jgw.printerposdemo.printer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.jgw.printerposdemo.R;
import com.printer.sdk.Barcode;
import com.printer.sdk.PrinterConstants.BarcodeType;
import com.printer.sdk.PrinterConstants.Command;
import com.printer.sdk.PrinterInstance;

public class BarcoePrintActivity extends BaseActivity implements
		OnItemSelectedListener,
		CompoundButton.OnCheckedChangeListener, OnClickListener {
	private RadioButton rbBarCode1;
	private RadioButton rbBarCode2;
	private Spinner spBarCodeType;
	private Spinner spContent;
	private int barType = 1;
	private int barNote = 1;
	private EditText etWidth;
	private EditText etHeight;
	private EditText etCodeContent;
	private TextView tvShowWidth;
	private TextView tvShowHeight;
	private TextView tvWidth;
	private TextView tvHeigh;
	private TextView tvQRSize;

	private Button btnSend;
	private Button btnClearContent;
	private Button btnCode1PrintExample;
	private Button btnCode2PrintExample;
	private Button btnScanPrint;
	private Context mContext;

	private ArrayAdapter<CharSequence> barTypeAdapter;
	private ArrayAdapter<CharSequence> barNoteAdapter;

	private final static int SCANNIN_GREQUEST_CODE = 2;
	private String barcodeContent;

	private LinearLayout header;
	private String TAG = "com.printer.demo.ui";

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_barcode);
		init();
		if (rbBarCode1.isChecked()) {
			barTypeAdapter = ArrayAdapter.createFromResource(
					BarcoePrintActivity.this, R.array.barcode1,
					android.R.layout.simple_spinner_item);
			// 设置样式
			barTypeAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spBarCodeType.setAdapter(barTypeAdapter);
			barNoteAdapter = ArrayAdapter.createFromResource(
					BarcoePrintActivity.this, R.array.barNoteArray,
					android.R.layout.simple_spinner_item);
			// 设置样式
			barNoteAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spContent.setAdapter(barNoteAdapter);
			rbBarCode2.setChecked(false);

		}
	};

	@Override
	protected void onResume() {
		super.onResume();
//		if (GlobalContants.ISCONNECTED) {
//			if ("".equals(GlobalContants.DEVICENAME)
//					|| GlobalContants.DEVICENAME == null) {
//				headerConnecedState.setText(R.string.unknown_device);
//
//			} else {
//
//				headerConnecedState.setText(GlobalContants.DEVICENAME);
//			}
//
//		}

	}

	private void init() {
		mContext = BarcoePrintActivity.this;
		header = (LinearLayout) findViewById(R.id.ll_header_barcode);
		initHeader();
		// 初始化radioButton并给它设置选择监听事件
		rbBarCode1 = (RadioButton) findViewById(R.id.rb_58mm);
		rbBarCode2 = (RadioButton) findViewById(R.id.rb_80mm);
		rbBarCode1.setOnCheckedChangeListener(this);
		rbBarCode2.setOnCheckedChangeListener(this);
		// 初始化spinner并给他设置子项选择监听事件
		spBarCodeType = (Spinner) findViewById(R.id.spinner_barcode_type);
		spContent = (Spinner) findViewById(R.id.spinner_interface_type);
		spBarCodeType.setOnItemSelectedListener(this);
		spContent.setOnItemSelectedListener(this);
		// 初始化设置条码宽度和高度以及输入条码内容的Edittext，
		etHeight = (EditText) findViewById(R.id.et_input_height);
		etWidth = (EditText) findViewById(R.id.et_input_width);
		etCodeContent = (EditText) findViewById(R.id.et_barcode_content);
		// 初始化显示条码宽度和高度的范围的textview
		tvShowWidth = (TextView) findViewById(R.id.tv_input_width);
		tvShowHeight = (TextView) findViewById(R.id.tv_input_height);
		tvWidth = (TextView) findViewById(R.id.tv_barcode_width);
		tvHeigh = (TextView) findViewById(R.id.tv_barcode_height);
		tvQRSize = (TextView) findViewById(R.id.tv_barcode_setting);

		// 初始化Button按钮并给他们设置点击监听事件
		btnSend = (Button) findViewById(R.id.btn_barcodedata_send);
		btnClearContent = (Button) findViewById(R.id.btn_data_default);
		btnCode1PrintExample = (Button) findViewById(R.id.btn_barcode_example);
		btnCode2PrintExample = (Button) findViewById(R.id.btn_qrcode_example);
		btnScanPrint = (Button) findViewById(R.id.btn_scan_print);
		btnSend.setOnClickListener(this);
		btnClearContent.setOnClickListener(this);
		btnCode1PrintExample.setOnClickListener(this);
		btnCode2PrintExample.setOnClickListener(this);
		btnScanPrint.setOnClickListener(this);

	}

	/**
	 * 初始化标题上的信息
	 */
	private void initHeader() {
		setHeaderLeftText(header, getString(R.string.back),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});
		headerConnecedState.setText(getTitleState());
		setHeaderLeftImage(header, new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setHeaderCenterText(header, getString(R.string.headview_BarcodePrint));
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == spBarCodeType) {
			barType = position;
		} else if (parent == spContent) {

			if(rbBarCode1.isChecked()){
				barNote = position;
			}else{
				barNote = position + 5;
				if (barNote > 5) {
					barNote = barNote - position * 2;
				}
			}
		}

		initUI();
	}

	/**
	 * 选择不同的条码，取值范围不同
	 */
	private void initUI() {
		boolean isDimOneChecked = rbBarCode1.isChecked();
		if (isDimOneChecked) {
			tvWidth.setText(R.string.barcode_width);
			tvShowWidth.setText(R.string.width_data_range);
			etWidth.setText(R.string.barcode_width_default_value);
			tvHeigh.setText(R.string.barcode_height);
			tvShowHeight.setText(R.string.height_data_range);
			etHeight.setText(R.string.barcode_height_default_value);
			tvQRSize.setText(R.string.content_position);
			if (barType == 6) {
				// UPC_E编码
				etCodeContent.setText(R.string.upc_e_default_value);
			} else if (barType == 7) {
				// EAN13编码
				etCodeContent.setText(R.string.ean_13_default_value);

			} else if (barType == 8) {
				// EAN8编码
				etCodeContent.setText(R.string.ean_8_default_value);

			} else if (barType == 9) {
				// 全部格式的编码 适配 UPC_E,EAN13,EAN8等所有对内容格式有要求的编码
				etCodeContent.setText(R.string.upc_e_default_value);
			} else {
			}

		} else {
			if (barType == 0) {
				tvWidth.setText(R.string.version);
				tvShowWidth.setText(R.string.verion_range);
				etWidth.setText(R.string.characters_default_value);
				tvShowHeight.setText(R.string.level_range);
				tvHeigh.setText(R.string.level);
				etHeight.setText(R.string.level_default_value);
				tvQRSize.setText(R.string.multiple);

			} else if (barType == 1) {
				tvWidth.setText(R.string.characters_in_line);
				tvShowWidth.setText(R.string.characters_range);
				etWidth.setText(R.string.version_default_value);
				tvHeigh.setText(R.string.level);
				tvShowHeight.setText(R.string.level_range_qrcode);
				etHeight.setText(R.string.level_range_default_value_qrcode);
				tvQRSize.setText(R.string.multiple);
			} else if (barType == 2) {
				tvWidth.setText(R.string.barcode_width);
				tvShowWidth
						.setText(R.string.barcode_width_data_range_datamatrix);
				etWidth.setText(R.string.level_range_default_value_qrcode);
				tvHeigh.setText(R.string.barcode_height);
				tvShowHeight
						.setText(R.string.barcode_height_data_range_datamatrix);
				etHeight.setText(R.string.barcode_height_default_value_datamatrix);
				tvQRSize.setText(R.string.multiple);

			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	/**
	 * 选择一维码或者二维码，条码列表不同
	 */

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// 一维码
		if (buttonView == rbBarCode1 && isChecked) {
			barTypeAdapter = ArrayAdapter.createFromResource(
					BarcoePrintActivity.this, R.array.barcode1,
					android.R.layout.simple_spinner_item);
			// 设置样式
			barTypeAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spBarCodeType.setAdapter(barTypeAdapter);

			barNoteAdapter = ArrayAdapter.createFromResource(
					BarcoePrintActivity.this, R.array.barNoteArray,
					android.R.layout.simple_spinner_item);
			// 设置样式
			barNoteAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spContent.setAdapter(barNoteAdapter);
			rbBarCode2.setChecked(false);
			// 二维码
		} else if (buttonView == rbBarCode2 && isChecked) {
			barTypeAdapter = ArrayAdapter.createFromResource(
					BarcoePrintActivity.this, R.array.barcode2,
					android.R.layout.simple_spinner_item);
			// 设置样式
			barTypeAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spBarCodeType.setAdapter(barTypeAdapter);

			barNoteAdapter = ArrayAdapter.createFromResource(
					BarcoePrintActivity.this, R.array.barEnlargeArray,
					android.R.layout.simple_spinner_item);
			// 设置样式
			barNoteAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spContent.setAdapter(barNoteAdapter);
			rbBarCode1.setChecked(false);
		}
		initUI();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// 发送数据并打印
		case R.id.btn_barcodedata_send:
			if (PrinterInstance.mPrinter == null) {
				Toast.makeText(BarcoePrintActivity.this,
						R.string.no_connected, Toast.LENGTH_SHORT)
						.show();

			} else {
				String content = etCodeContent.getText().toString();
				int width = Integer.parseInt(etWidth.getText().toString());
				int height = Integer.parseInt(etHeight.getText().toString());
				if (rbBarCode1.isChecked()) {
					if (width < 2 || width > 6) {
						width = 2;
					}
					if (height < 1 || width > 255) {
						height = 162;
					}
				} else {

					switch (barType) {
					case 0:
						if(width>40 || width < 0)
							width = 0;
						if(height != 72 || height != 76 || height != 77 || height != 81){
							height = 76;
						}
						break;
					case 1:
						if(width>30 || width < 1)
							width = 1;
						if(height>8 || height < 0){
							height = 0;
						}
						break;
					case 2:
						if(width>144 || width < 8)
							width = 8;
						if(height>144 || height < 0){
							height = 0;
						}
						break;
					default:
						break;
					}

				}
				Barcode barcode = null;
				String codeType = null;
				switch (barType) {
				case 0:
					barcode = new Barcode(
							rbBarCode1.isChecked() ? BarcodeType.CODE128
									: BarcodeType.QRCODE, width, height,
							barNote, content);
					if (rbBarCode1.isChecked()) {
						codeType = "CODE128";
					} else {
						codeType = "QRCODE";
					}
					break;

				case 1:
					barcode = new Barcode(
							rbBarCode1.isChecked() ? BarcodeType.CODE39
									: BarcodeType.PDF417, width, height,
							barNote, content);
					if (rbBarCode1.isChecked()) {
						codeType = "CODE39";
					} else {
						codeType = "PDF417";
					}
					break;
				case 2:
					barcode = new Barcode(
							rbBarCode1.isChecked() ? BarcodeType.CODABAR
									: BarcodeType.DATAMATRIX, width, height,
							barNote, content);
					if (rbBarCode1.isChecked()) {
						codeType = "CODEBAR";
					} else {
						codeType = "DATAMATRIX";
					}
					break;
				case 3:
					barcode = new Barcode(BarcodeType.ITF, width, height,
							barNote, content);
					codeType = "ITF";
					break;
				case 4:
					barcode = new Barcode(BarcodeType.CODE93, width, height,
							barNote, content);
					codeType = "CODE93";
					break;
				case 5:
					barcode = new Barcode(BarcodeType.UPC_A, width, height,
							barNote, content);
					codeType = "UPC_A";
					break;
				case 6:
					barcode = new Barcode(BarcodeType.UPC_E, width, height,
							barNote, content);
					codeType = "UPC_E";
					break;
				case 7:
					barcode = new Barcode(BarcodeType.JAN13, width, height,
							barNote, content);
					codeType = "JAN13";
					break;
				case 8:
					barcode = new Barcode(BarcodeType.JAN8, width, height,
							barNote, content);
					codeType = "JAN8";
					break;
				case 9:
					Barcode barcode1 = new Barcode(BarcodeType.CODE128, width,
							height, barNote, content);
					Barcode barcode2 = new Barcode(
							rbBarCode1.isChecked() ? BarcodeType.CODE39
									: BarcodeType.PDF417, width, height,
							barNote, content);
					Barcode barcode3 = new Barcode(
							rbBarCode1.isChecked() ? BarcodeType.CODABAR
									: BarcodeType.QRCODE, width, height,
							barNote, content);
					Barcode barcode4 = new Barcode(
							rbBarCode1.isChecked() ? BarcodeType.ITF
									: BarcodeType.DATAMATRIX, width, height,
							barNote, content);
					Barcode barcode5 = new Barcode(BarcodeType.CODE93, width,
							height, barNote, content);
					Barcode barcode6 = new Barcode(BarcodeType.UPC_A, width,
							height, barNote, content);
					Barcode barcode7 = new Barcode(BarcodeType.UPC_E, width,
							height, barNote, content);
					Barcode barcode8 = new Barcode(BarcodeType.JAN13, width,
							height, barNote, content);
					barcode = new Barcode(BarcodeType.JAN8, width, height,
							barNote, content);
					PrinterInstance.mPrinter.printText("打印 CODE128 码效果演示：");
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
					PrinterInstance.mPrinter.printBarCode(barcode1);
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

					PrinterInstance.mPrinter.printText("打印 CODE39 码效果演示：");
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
					PrinterInstance.mPrinter.printBarCode(barcode2);
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

					PrinterInstance.mPrinter.printText("打印 CODEBAR 码效果演示：");
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
					PrinterInstance.mPrinter.printBarCode(barcode3);
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

					PrinterInstance.mPrinter.printText("打印 ITF 码效果演示：");
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
					PrinterInstance.mPrinter.printBarCode(barcode4);
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

					PrinterInstance.mPrinter.printText("打印 CODE93 码效果演示：");
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
					PrinterInstance.mPrinter.printBarCode(barcode5);
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

					PrinterInstance.mPrinter.printText("打印 UPC_A 码效果演示：");
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
					PrinterInstance.mPrinter.printBarCode(barcode6);
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

					PrinterInstance.mPrinter.printText("打印 UPC_E 码效果演示：");
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
					PrinterInstance.mPrinter.printBarCode(barcode7);
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

					PrinterInstance.mPrinter.printText("打印 JAN13 码效果演示：");
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
					PrinterInstance.mPrinter.printBarCode(barcode8);
					PrinterInstance.mPrinter.setPrinter(
							Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

					codeType = "JAN8";
				}
				PrinterInstance.mPrinter
						.printText("打印 " + codeType + " 码效果展示：");
				PrinterInstance.mPrinter.setPrinter(
						Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
				PrinterInstance.mPrinter.printBarCode(barcode);
				PrinterInstance.mPrinter.setPrinter(
						Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

			}
			break;
		// 打印一维码示例
		case R.id.btn_barcode_example:
			if (PrinterInstance.mPrinter == null) {
				Toast.makeText(BarcoePrintActivity.this,
						R.string.no_connected, Toast.LENGTH_SHORT)
						.show();

			} else {
				PrinterInstance.mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
				Barcode barcode1 = new Barcode(BarcodeType.CODE128, 2, 150, 2,
						"123456");
				PrinterInstance.mPrinter.printBarCode(barcode1);
			}

			break;
		// 打印二维码示例
		case R.id.btn_qrcode_example:
			if (PrinterInstance.mPrinter == null) {
				Toast.makeText(BarcoePrintActivity.this,
						R.string.no_connected, Toast.LENGTH_SHORT)
						.show();

			} else {
				PrinterInstance.mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
				Barcode barcode2 = new Barcode(BarcodeType.QRCODE, 2, 3, 6,
						"123456");
				PrinterInstance.mPrinter.printBarCode(barcode2);
			}

			break;
		// 清空数据到默认
		case R.id.btn_data_default:
			etCodeContent.setText(R.string.default_code_content);
			if (rbBarCode1.isChecked()) {
				etWidth.setText("2");
				etHeight.setText("162");
				setSpinnerItemSelectedByValue(spContent,
						getString(R.string.default_content_position));
				setSpinnerItemSelectedByValue(spBarCodeType,
						getString(R.string.default_barcode_type));

			} else {
				// if (barType == 0) {
				// tvWidth.setText(R.string.version);
				// etWidth.setText(R.string.level_default_value_qrcode);
				// tvHeigh.setText(R.string.level);
				// tvShowHeight.setText(R.string.level_range);
				// etHeight.setText(R.string.level_default_value);
				// tvQRSize.setText(R.string.multiple);
				// } else if (barType == 1) {
				tvWidth.setText(R.string.characters_in_line);
				tvShowWidth.setText(R.string.characters_range);
				etWidth.setText(R.string.characters_default_value);
				tvHeigh.setText(R.string.level);
				tvShowHeight.setText(R.string.level_range_qrcode);
				etHeight.setText(R.string.level_range_default_value_qrcode);
				tvQRSize.setText(R.string.multiple);
				// } else if (barType == 2) {
				// tvWidth.setText(R.string.barcode_width);
				// tvShowWidth.setText(R.string.barcode_width_data_range_datamatrix);
				// etWidth.setText(R.string.level_default_value_qrcode);
				// tvHeigh.setText(R.string.barcode_height);
				// tvShowHeight.setText(R.string.barcode_height_data_range_datamatrix);
				// etHeight.setText(R.string.barcode_height_default_value_datamatrix);
				// tvQRSize.setText("放大倍数:");
				//
				// }
				// 恢复spinner打印位置为默认
				// spContent.setAdapter(barNoteAdapter);
				// spContent.setSelection(0,true);
				// spContent.setSelected(false);
				setSpinnerItemSelectedByValue(spContent,
						getString(R.string.characters_default_value));
				setSpinnerItemSelectedByValue(spBarCodeType,
						getString(R.string.default_qrcode_type));

			}
			break;
		// 扫一扫打印
		case R.id.btn_scan_print:
			if (PrinterInstance.mPrinter == null && !SettingActivity.isConnected) {
				Toast.makeText(BarcoePrintActivity.this,
						R.string.no_connected, Toast.LENGTH_SHORT)
						.show();

			} else {
				Intent intent = new Intent();
				intent.setClass(mContext, MipcaActivityCapture.class);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
			break;

		}
	}

	/**
	 * 根据值, 设置spinner默认选中:
	 *
	 * @param spinner
	 * @param value
	 */
	public static void setSpinnerItemSelectedByValue(Spinner spinner,
			String value) {
		SpinnerAdapter apsAdapter = spinner.getAdapter(); // 得到SpinnerAdapter对象
		int k = apsAdapter.getCount();
		for (int i = 0; i < k; i++) {
			if (value.equals(apsAdapter.getItem(i).toString())) {
				spinner.setSelection(i, true);// 默认选中项
				break;
			}
		}
	}

	@SuppressLint("ShowToast")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == SCANNIN_GREQUEST_CODE) {
			Log.e(TAG, "执行onActivityResult方法");
			// 得到扫描后的条码的类型
			int barcodeType = data.getExtras().getInt("codetype");
			Log.e(TAG, "条码类型" + barcodeType);

			// 得到扫描后的内容再生成条码并打印
			barcodeContent = data.getExtras().getString(
					BluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
			Log.e(TAG, "--条码内容" + barcodeContent);
			if(barcodeType == 1){

				Toast.makeText(mContext,
						"一维码：" + barcodeContent, 0)
						.show();
			}else{
				Toast.makeText(mContext,
						"二维码：" + barcodeContent + " " + barcodeContent, 0)
						.show();
			}

			// 判端条码类型
			switch (barcodeType) {
			case 1:
				if (PrinterInstance.mPrinter == null) {
					Toast.makeText(BarcoePrintActivity.this,
							R.string.tips_for_not_connect, Toast.LENGTH_SHORT)
							.show();
				} else {
					// 生成CODE128类型的一维码
					Barcode barcode1 = new Barcode(BarcodeType.CODE128, 2, 150,
							2, barcodeContent);
					PrinterInstance.mPrinter.printBarCode(barcode1);

				}
				break;
			case 2:
				if (PrinterInstance.mPrinter == null) {
					Toast.makeText(BarcoePrintActivity.this,
							R.string.tips_for_not_connect, Toast.LENGTH_SHORT)
							.show();
				} else {
					// 生成QRCODE类型的二维码
					Barcode barcode2 = new Barcode(BarcodeType.QRCODE, 2, 3, 6,
							barcodeContent);
					PrinterInstance.mPrinter.printBarCode(barcode2);
				}

				break;
			case 3:
				if (PrinterInstance.mPrinter == null) {
					Toast.makeText(BarcoePrintActivity.this,
							R.string.tips_for_not_connect, Toast.LENGTH_SHORT)
							.show();

				} else {
					// 其他条码类型 //TODO 按CODE128类型的一维码处理
					Barcode barcode3 = new Barcode(BarcodeType.CODE128, 2, 150,
							2, barcodeContent);
					PrinterInstance.mPrinter.printBarCode(barcode3);
				}

				break;

			}
		}
	}

}
