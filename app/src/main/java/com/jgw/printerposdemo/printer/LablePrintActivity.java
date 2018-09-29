package com.jgw.printerposdemo.printer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.R.string;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.jgw.printerposdemo.R;
import com.jgw.printerposdemo.printer.global.GlobalContants;
import com.jgw.printerposdemo.printer.utils.PrefUtils;
import com.jgw.printerposdemo.printer.utils.PrintLabel100;
import com.jgw.printerposdemo.printer.utils.PrintLabel58;
import com.jgw.printerposdemo.printer.utils.PrintLabel80;
import com.jgw.printerposdemo.printer.utils.PrintLabel80_1;
import com.jgw.printerposdemo.printer.utils.PrintLabelDrink;
import com.jgw.printerposdemo.printer.utils.PrintLabelExpress;
import com.jgw.printerposdemo.printer.utils.PrintLabelFruit;
import com.jgw.printerposdemo.printer.utils.PrintLabelMaterial;
import com.jgw.printerposdemo.printer.utils.PrintLabelStorage;
import com.jgw.printerposdemo.printer.utils.PrintLablel;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.PrinterConstants.CommandTSPL;
import com.printer.sdk.PrinterConstants.Connect;
import com.printer.sdk.exception.ParameterErrorException;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.WriteException;
import com.printer.sdk.utils.XLog;

public class LablePrintActivity extends BaseActivity
		implements OnClickListener, OnItemSelectedListener, OnCheckedChangeListener {
	private String TAG = "LablePrintActivity";
	private Button btnPrintLable100mm;
	private Button btnPrintLable80mm;
	private Button btnPrintLable50mm;
	private Button btnPrintLable;
	private LinearLayout header;
	public static final int PRINT_START = 0x1557; // 15:57
	public static final int PRINT_DONE = 0x1558;
	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);

	protected Handler printerHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			return false;
		}
	});

	PrinterInstance mPritner;
	public static PrinterInstance myPrinter;
	private boolean isConnected = false;
	private int interfaceType = 0;
	// 与TSPL指令有关的控件
	private Button btnFruitLabel;
	private Button btnDrinkLabel;
	private Button btnMaterialLabel;
	private Button btnExpressLabel;
	private Button btnStorageLabel;

	private Context mContext;
	/**
	 * 与TSPL指令相关的控件
	 */
	private RadioGroup rgOrderSet;
	private RadioButton rb_order_cpcl;
	private RadioButton rb_order_tspl;
	private LinearLayout llTSPL;
	/**
	 * 与TSPL指令相关的控件
	 */

	// TODO 因为暂时不能自定义标签的宽高，所以暂时不设置这俩个属性
	// private EditText etLabelWidth;
	// private EditText etLabelHeight;
	private EditText etGapWidth;
	private EditText etGapOffset;
	private EditText etPrintSpeed;
	private EditText etPrintLevel;
	private EditText etLabelOffset;
	private EditText etPrintNumbers;
	private EditText etPrintLeftMargin;
	private EditText etPrintTopMargin;
	private Spinner spTear;
	private Spinner spOpenCashBox;
	private Spinner spIsBeep;
	private ArrayAdapter<CharSequence> tearData_Adapter;
	private ArrayAdapter<CharSequence> OpenCashBoxAdapter;
	private ArrayAdapter<CharSequence> IsBeepAdapter;
	private String tearSet = "撕纸";
	private String OpenCashSet = "禁止开钱箱";
	private String IsBeepSet = "禁止蜂鸣器";

	/**
	 * @describe 判断设置TSPL指令是否成功
	 * @param // mPrinter调用PrinterInstance中的方法设置TSPL相关参数
	 * @param mPrinter
	 * @return true：设置成功 false：设置失败
	 */
	private boolean setPrinterTSPL(PrinterInstance mPrinter) {
		boolean isSettingSuccess = false;
		String gapWidth = etGapWidth.getText().toString();
		String gapOffset = etGapOffset.getText().toString();
		String printSpeed = etPrintSpeed.getText().toString();
		String printLevel = etPrintLevel.getText().toString();
		String printLabelOffset = etLabelOffset.getText().toString();
		String printNumbers = etPrintNumbers.getText().toString();
		String printLeftMargin = etPrintLeftMargin.getText().toString();
		String printTopMargin = etPrintTopMargin.getText().toString();
		for (int i = 0; i < 1; i++) {
			try {
				// 处于TSPL指令模式下
				if (llTSPL.getVisibility() == View.VISIBLE) {
					// 设置标签间的缝隙大小
					if (gapWidth == null || gapWidth.equals("") || gapOffset == null || gapOffset.equals("")) {
						Toast.makeText(mContext, "间隙宽度和间隙偏移量不能为空", 0).show();
						break;
					} else {
						int gapWidthTSPL = Integer.parseInt(gapWidth);
						int gapOffsetTSPL = Integer.parseInt(gapOffset);
						mPrinter.setGAPTSPL(gapWidthTSPL, gapOffsetTSPL);
						PrefUtils.setInt(mContext, "gapwidthtspl", gapWidthTSPL);
						PrefUtils.setInt(mContext, "gapoffsettspl", gapOffsetTSPL);
					}
					// 设置打印机打印速度
					if (printSpeed == null || printSpeed.equals("")) {
						Toast.makeText(mContext, "打印机打印速度不能设置为空", 0).show();
						break;
					} else {
						int printSpeedTSPL = Integer.parseInt(printSpeed);
						mPrinter.setPrinterTSPL(CommandTSPL.SPEED, printSpeedTSPL);
						PrefUtils.setInt(mContext, "printspeed", printSpeedTSPL);
					}
					// 设置打印机打印浓度
					if (printLevel == null || printLevel.equals("")) {
						Toast.makeText(mContext, "打印机打印浓度不能设置为空", 0).show();
						break;
					} else {
						int printLevelSpeedTSPL = Integer.parseInt(printLevel);
						mPrinter.setPrinterTSPL(CommandTSPL.DENSITY, printLevelSpeedTSPL);
						PrefUtils.setInt(mContext, "printlevel", printLevelSpeedTSPL);
					}
					// 设置标签偏移量
					if (printLabelOffset == null || printLabelOffset.equals("")) {
						Toast.makeText(mContext, "标签偏移量不能设置为空", 0).show();
						break;
					} else {
						// 标签偏移量（点数）
						int labelOffsetTSPL = Integer.parseInt(printLabelOffset) * 8;
						mPrinter.setPrinterTSPL(CommandTSPL.SHIFT, labelOffsetTSPL);
						PrefUtils.setInt(mContext, "labeloffsettspl", labelOffsetTSPL / 8);

					}
					// 设置打印机打印份数
					if (printNumbers == null || printNumbers.equals("")) {
						Toast.makeText(mContext, "打印机打印数量不能设置为空或者为负数", 0).show();
						break;
					} else {
						int printNumbersTSPL = Integer.parseInt(printNumbers);
						PrefUtils.setInt(mContext, "printnumbers", printNumbersTSPL);
					}

					// 设置打印内容初始位置
					if (printLeftMargin == null || printLeftMargin.equals("") || printTopMargin == null
							|| printLeftMargin.equals("")) {
						Toast.makeText(mContext, "标签偏移量不能为空或者为负数", 0).show();
						break;
					} else {
						int marginLeft = Integer.parseInt(printLeftMargin);
						PrefUtils.setInt(mContext, "leftmargin", marginLeft);
						int marginTop = Integer.parseInt(printTopMargin);
						PrefUtils.setInt(mContext, "topmargin", marginTop);

					}

					// // 撕纸设置
					// if (tearSet == null || tearSet.equals("")) {
					// break;
					// } else if (tearSet.equals("撕纸")) {
					// mPrinter.setPrinterTSPL(CommandTSPL.TEAR, 1);
					// mPrinter.setPrinterTSPL(CommandTSPL.PEEL, 0);
					// PrefUtils.setInt(mContext, "tearAndpeel", 0);
					// } else if (tearSet.equals("剥纸")) {
					// mPrinter.setPrinterTSPL(CommandTSPL.PEEL, 1);
					// PrefUtils.setInt(mContext, "tearAndpeel", 1);
					// } else if (tearSet.equals("关")) {
					// mPrinter.setPrinterTSPL(CommandTSPL.TEAR, 0);
					// mPrinter.setPrinterTSPL(CommandTSPL.PEEL, 0);
					// PrefUtils.setInt(mContext, "tearAndpeel", 2);
					//
					// }
					// 开关钱箱设置
					if (OpenCashSet == null || OpenCashSet.equals("")) {
						break;
					} else if (OpenCashSet.equals(R.string.String_judge1)) {
						PrefUtils.setInt(mContext, "isOpenCash", 0);
					} else if (OpenCashSet.equals(R.string.String_judge2)) {
						PrefUtils.setInt(mContext, "isOpenCash", 1);
					} else if (OpenCashSet.equals(R.string.String_judge3)) {
						PrefUtils.setInt(mContext, "isOpenCash", 2);
					}
					// 开关蜂鸣器设置
					if (IsBeepSet == null || IsBeepSet.equals("")) {
						break;
					} else if (IsBeepSet.equals(R.string.String_judge4)) {
						PrefUtils.setInt(mContext, "isBeep", 2);
					} else if (IsBeepSet.equals(R.string.String_judge5)) {
						PrefUtils.setInt(mContext, "isBeep", 1);
					} else if (IsBeepSet.equals(R.string.String_judge6)) {
						PrefUtils.setInt(mContext, "isBeep", 0);

					}

					isSettingSuccess = true;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				Toast.makeText(mContext, "向打印机写入数据异常", 0).show();
				e.printStackTrace();
			} catch (PrinterPortNullException e) {
				Toast.makeText(mContext, "打印机为空异常", 0).show();
				e.printStackTrace();
			} catch (ParameterErrorException e) {
				Toast.makeText(mContext, "传入参数异常", 0).show();
				e.printStackTrace();
			} catch (Exception e) {
				Toast.makeText(mContext, "其他未知异常", 0).show();
				e.printStackTrace();
			}
		}

		return isSettingSuccess;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lable_print);
		init();
		header = (LinearLayout) findViewById(R.id.ll_headerview_LablePrintactivity);
		btnPrintLable100mm = (Button) findViewById(R.id.btn_100mm);
		btnPrintLable80mm = (Button) findViewById(R.id.btn_80mm);
		btnPrintLable50mm = (Button) findViewById(R.id.btn_58mm);
		btnPrintLable = (Button) findViewById(R.id.btn_lable);
		btnPrintLable100mm.setOnClickListener(this);
		btnPrintLable80mm.setOnClickListener(this);
		btnPrintLable50mm.setOnClickListener(this);
		btnPrintLable.setOnClickListener(this);
		// TSPL指令相关的控件初始化
		btnFruitLabel = (Button) findViewById(R.id.btn_fruit_tspl);
		btnDrinkLabel = (Button) findViewById(R.id.btn_drink_tspl);
		btnMaterialLabel = (Button) findViewById(R.id.btn_material_tspl);
		btnExpressLabel = (Button) findViewById(R.id.btn_express_tspl);
		btnStorageLabel = (Button) findViewById(R.id.btn_storage_tspl);

		btnFruitLabel.setOnClickListener(this);
		btnDrinkLabel.setOnClickListener(this);
		btnMaterialLabel.setOnClickListener(this);
		btnExpressLabel.setOnClickListener(this);
		btnStorageLabel.setOnClickListener(this);
		mPritner = PrinterInstance.mPrinter;
		initHeader();
		// 判断设置界面是不是选中TSPL指令集的选择框,从而选择界面上需要显示的标签
		Log.i(TAG, "zl at LablePrintActivity.java  onCreate()----> before if ");
		rb_order_cpcl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "zl at LablePrintActivity.java  onCreate()----> rb_order_tspl__cpcl"
						+ rb_order_cpcl.isChecked());
				btnPrintLable100mm.setVisibility(View.VISIBLE);
				btnPrintLable80mm.setVisibility(View.VISIBLE);
				btnPrintLable50mm.setVisibility(View.VISIBLE);
				btnPrintLable.setVisibility(View.VISIBLE);

				btnFruitLabel.setVisibility(View.GONE);
				btnDrinkLabel.setVisibility(View.GONE);
				btnMaterialLabel.setVisibility(View.GONE);
				btnExpressLabel.setVisibility(View.GONE);
				btnStorageLabel.setVisibility(View.GONE);
			}
		});
		rb_order_tspl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnPrintLable100mm.setVisibility(View.GONE);
				btnPrintLable80mm.setVisibility(View.GONE);
				btnPrintLable50mm.setVisibility(View.GONE);
				btnPrintLable.setVisibility(View.GONE);

				btnFruitLabel.setVisibility(View.VISIBLE);
				btnDrinkLabel.setVisibility(View.VISIBLE);
				btnMaterialLabel.setVisibility(View.VISIBLE);
				btnExpressLabel.setVisibility(View.VISIBLE);
				btnStorageLabel.setVisibility(View.VISIBLE);

			}
		});
		// if (rb_order_tspl.isChecked()) {
		// Log.i(TAG,
		// "zl at LablePrintActivity.java onCreate()---->
		// rb_order_tspl__1"+rb_order_tspl.isChecked());
		// btnPrintLable100mm.setVisibility(View.GONE);
		// btnPrintLable80mm.setVisibility(View.GONE);
		// btnPrintLable50mm.setVisibility(View.GONE);
		// btnPrintLable.setVisibility(View.GONE);
		//
		// btnFruitLabel.setVisibility(View.VISIBLE);
		// btnDrinkLabel.setVisibility(View.VISIBLE);
		// btnMaterialLabel.setVisibility(View.VISIBLE);
		// btnExpressLabel.setVisibility(View.VISIBLE);
		// btnStorageLabel.setVisibility(View.VISIBLE);
		// }else if (rb_order_cpcl.isChecked()) {
		// Log.i(TAG,
		// "zl at LablePrintActivity.java onCreate()---->
		// rb_order_tspl__cpcl"+rb_order_cpcl.isChecked());
		// btnPrintLable100mm.setVisibility(View.VISIBLE);
		// btnPrintLable80mm.setVisibility(View.VISIBLE);
		// btnPrintLable50mm.setVisibility(View.VISIBLE);
		// btnPrintLable.setVisibility(View.VISIBLE);
		//
		// btnFruitLabel.setVisibility(View.GONE);
		// btnDrinkLabel.setVisibility(View.GONE);
		// btnMaterialLabel.setVisibility(View.GONE);
		// btnExpressLabel.setVisibility(View.GONE);
		// btnStorageLabel.setVisibility(View.GONE);
		// }
		//

	}

	private void init() {
		/**
		 * 与TSPL指令相关的设置
		 */
		mContext = LablePrintActivity.this;
		rgOrderSet = (RadioGroup) findViewById(R.id.rg_orderset);
		rb_order_tspl = (RadioButton) findViewById(R.id.rb_order_tspl);
		rb_order_cpcl = (RadioButton) findViewById(R.id.rb_order_cpcl);
		llTSPL = (LinearLayout) findViewById(R.id.ll_tspls);
		rgOrderSet.setOnCheckedChangeListener(this);
		// TODO 暂时不设置标签的宽和高
		// etLabelWidth = (EditText) findViewById(R.id.et_label_width);
		// etLabelHeight = (EditText) findViewById(R.id.et_label_height);
		etGapWidth = (EditText) findViewById(R.id.et_gap_width);
		etGapOffset = (EditText) findViewById(R.id.et_gap_offset);
		etPrintSpeed = (EditText) findViewById(R.id.et_print_speed);
		etPrintLevel = (EditText) findViewById(R.id.et_print_level);
		etLabelOffset = (EditText) findViewById(R.id.et_print_label_offset);
		etPrintNumbers = (EditText) findViewById(R.id.et_print_numbers);
		etPrintLeftMargin = (EditText) findViewById(R.id.et_left_margin);
		etPrintTopMargin = (EditText) findViewById(R.id.et_top_margin);
		spTear = (Spinner) findViewById(R.id.spinner_tear);
		tearData_Adapter = ArrayAdapter.createFromResource(LablePrintActivity.this, R.array.tear_data,
				android.R.layout.simple_spinner_item);
		tearData_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spTear.setAdapter(tearData_Adapter);
		spTear.setOnItemSelectedListener(this);
		spOpenCashBox = (Spinner) findViewById(R.id.spinner_cash);
		OpenCashBoxAdapter = ArrayAdapter.createFromResource(LablePrintActivity.this, R.array.cash_data,
				android.R.layout.simple_spinner_item);
		OpenCashBoxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spOpenCashBox.setAdapter(OpenCashBoxAdapter);
		spOpenCashBox.setOnItemSelectedListener(this);
		spIsBeep = (Spinner) findViewById(R.id.spinner_beep);
		IsBeepAdapter = ArrayAdapter.createFromResource(LablePrintActivity.this, R.array.beep_data,
				android.R.layout.simple_spinner_item);
		IsBeepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spIsBeep.setAdapter(IsBeepAdapter);
		spIsBeep.setOnItemSelectedListener(this);
		// 根据是不是选中了TSPL指令来选择是不是显示TSPL指令标签
		if (DemoApplication.isSettingTSPL) {
			rgOrderSet.check(R.id.rb_order_tspl);
		} else {
			rgOrderSet.check(R.id.rb_order_cpcl);
		}
		interfaceType = PrefUtils.getInt(mContext, GlobalContants.INTERFACETYPE, 0);
		// 初始化TSPL设置界面
		getSavedTSPL();

	}

	// 用于接受连接状态消息的 Handler
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Connect.SUCCESS:
				isConnected = true;

				// TOTO 暂时将TSPL指令设置参数的设置放在这
				if (setPrinterTSPL(myPrinter)) {
					if (interfaceType == 0) {
						Toast.makeText(mContext, R.string.settingactivitty_toast_bluetooth_set_tspl_successful, 0)
								.show();
					} else if (interfaceType == 1) {
						Toast.makeText(mContext, R.string.settingactivity_toast_usb_set_tspl_succefful, 0).show();
					}
				}
				break;
			}
		}
	};

	/**
	 * 获取TSPL指令下各个指令的内容
	 */
	private void getSavedTSPL() {
		XLog.i(TAG, "ZL at LablePrintActivity.java getSavedTSPL()------>begain");
		etPrintNumbers.setText(PrefUtils.getInt(mContext, "printnumbers", 1) + "");
		etPrintLeftMargin.setText(PrefUtils.getInt(mContext, "leftmargin", 0) + "");
		etPrintTopMargin.setText(PrefUtils.getInt(mContext, "topmargin", 0) + "");
		etPrintLevel.setText(PrefUtils.getInt(mContext, "printlevel", 7) + "");
		etPrintSpeed.setText(PrefUtils.getInt(mContext, "printspeed", 12) + "");
		etGapWidth.setText(PrefUtils.getInt(mContext, "gapwidthtspl", 0) + "");
		etGapOffset.setText(PrefUtils.getInt(mContext, "gapoffsettspl", 0) + "");
		etLabelOffset.setText(PrefUtils.getInt(mContext, "labeloffsettspl", 0) + "");
		spTear.setSelection(PrefUtils.getInt(mContext, "tearAndpeel", 0), true);
		spIsBeep.setSelection(PrefUtils.getInt(mContext, "isBeep", 0), true);
		spOpenCashBox.setSelection(PrefUtils.getInt(mContext, "isOpenCash", 0), true);
		XLog.i(TAG, "ZL at LablePrintActivity.java getSavedTSPL()------>end");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// if (GlobalContants.ISCONNECTED) {
		// if ("".equals(GlobalContants.DEVICENAME)
		// || GlobalContants.DEVICENAME == null) {
		// headerConnecedState.setText(R.string.unknown_device);
		//
		// } else {
		//
		// headerConnecedState.setText(GlobalContants.DEVICENAME);
		// }
		//
		// }

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
		setHeaderLeftImage(header, new OnClickListener() {// 初始化了
			// headerConnecedState
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		headerConnecedState.setText(getTitleState());
		setHeaderCenterText(header, getString(R.string.headview_LablePrint));
	}

	@Override
	public void onClick(View v) {
		if (mPritner == null && !SettingActivity.isConnected) {
			Toast.makeText(LablePrintActivity.this, getString(R.string.no_connected), 0).show();
			return;
		}
		switch (v.getId()) {
		case R.id.btn_100mm:
			new Thread(new Runnable() {
				@Override
				public void run() {
					new PrintLabel100().doPrint(mPritner);
				}
			}).start();

			break;
		case R.id.btn_80mm:
			new Thread(new Runnable() {
				@Override
				public void run() {
					new PrintLabel80().doPrint(mPritner);
				}
			}).start();

			break;
		case R.id.btn_58mm:
			new Thread(new Runnable() {
				@Override
				public void run() {
					new PrintLabel58().doPrint(mPritner, getResources());
				}
			}).start();

			break;
		case R.id.btn_lable:
			new Thread(new Runnable() {
				@Override
				public void run() {
					new PrintLablel().doPrint(mPritner);
				}
			}).start();

			break;
		case R.id.btn_fruit_tspl:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO 开发蔬菜水果标签打印模版
					new PrintLabelFruit().doPrintTSPL(mPritner, getApplicationContext());
				}
			}).start();

			break;
		case R.id.btn_drink_tspl:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO 开发奶茶标签打印模版
					new PrintLabelDrink().doPrintTSPL(mPritner, getApplicationContext());
				}
			}).start();

			break;
		case R.id.btn_material_tspl:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO 开发材料运输标签打印模版
					new PrintLabelMaterial().doPrintTSPL(mPritner, getApplicationContext());
				}
			}).start();

			break;
		case R.id.btn_express_tspl:

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					// TODO 开发快递单号标签打印模版
					new PrintLabelExpress().doPrintTSPL(mPritner, getApplicationContext());
				}
			}).start();

			break;
		case R.id.btn_storage_tspl:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO 开发仓储行业打印模版
					new PrintLabelStorage().doPrintTSPL(mPritner, getApplicationContext());
				}
			}).start();

			break;
		}

	}

	private String getFilePath() {

		String PATH_LOGCAT = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
			PATH_LOGCAT = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyPicture";
			XLog.i(TAG, "ZL at LablePrintActivity.java getFilePath() sdka ");
		} else {// 如果SD卡不存在，就保存到本应用的目录下
			PATH_LOGCAT = this.getFilesDir().getAbsolutePath() + File.separator + "MyPicture";
			XLog.i(TAG, "ZL at LablePrintActivity.java getFilePath() neicun ");
		}
		File dir = new File(PATH_LOGCAT);
		if (!dir.exists()) {
			dir.mkdir(); // 创建文件夹
		}
		String remp_dir = PATH_LOGCAT + File.separator + "tmpPhoto.jpg";
		XLog.i(TAG, "ZL at LablePrintActivity.java getFilePath() remp_dir: " + remp_dir);
		return remp_dir;
	}

	private void print() {
		if (PrinterInstance.mPrinter == null) {
			Toast.makeText(LablePrintActivity.this, "请连接打印机", Toast.LENGTH_SHORT).show();
		} else {
			new PrintThread().start();
		}
	}

	private class PrintThread extends Thread {
		String codeStr, destinationStr, countStr, weightStr, volumeStr, dispatchModeStr, businessModeStr, packModeStr,
				receiverAddress;

		public PrintThread() {
			codeStr = "DF1234567890";
			destinationStr = "西安长线";
			countStr = "1";
			weightStr = "2";
			volumeStr = "1";
			dispatchModeStr = "派送";
			businessModeStr = "定时达";
			packModeStr = "袋装";
			receiverAddress = "陕西省西安市临潼区秦始皇陵兵马俑一号坑五排三列俑";
		}

		@Override
		public void run() {
			Looper.prepare();
			try {
				printerHandler.obtainMessage(PRINT_START).sendToTarget();
				printing(codeStr, destinationStr, countStr, weightStr, volumeStr, dispatchModeStr, businessModeStr,
						packModeStr, receiverAddress);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				printerHandler.obtainMessage(PRINT_DONE).sendToTarget();
			}
			Looper.loop();
		}
	}

	public void printing(String codeStr, String destinationStr, String countStr, String weightStr, String volumeStr,
			String dispatchModeStr, String businessModeStr, String packModeStr, String receiverAddress)
			throws Exception {
		String centerName = "西安分拔中心"; // 目的地分拨
		String centerCode = "0292001"; // 目的地分拨编号

		String userSite = "测试二级网点" + "(" + dateFormat.format(new Date()) + ")"; // 出发网点

		int count = Integer.parseInt(countStr);
		for (int c = 1; c <= count; c++) {
			// 子单号
			String serialNum = String.format("%03d", c);
			String subCodeStr = codeStr + serialNum + centerCode;
			String serialStr = "第" + c + "件";
			//
			// LablePrintUtils.doPrint(this, mPritner, codeStr,
			// businessModeStr, centerName, destinationStr, userSite,
			// receiverAddress, countStr, serialStr, dispatchModeStr,
			// packModeStr, subCodeStr);
		}

		// onPrintSucceed();
	}

	/**
	 * @describe 判断点击的是TSPL按钮还是cpcl按钮
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (group == rgOrderSet) {
			if (checkedId == R.id.rb_order_cpcl) {
				llTSPL.setVisibility(View.GONE);
				DemoApplication.isSettingTSPL = false;
			} else if (checkedId == R.id.rb_order_tspl) {
				llTSPL.setVisibility(View.VISIBLE);
				DemoApplication.isSettingTSPL = true;
			}
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		// if (parent == spinner_printer_type) {
		// PrefUtils.setInt(mContext, GlobalContants.PRINTERID, position);
		// String printerName = getResources().getStringArray(
		// R.array.printertype)[position];
		// if (printerName.contains("T10") || printerName.contains("POS58")
		// || printerName.contains("T7")) {
		// rg__select_paper_size.check(R.id.rb_58mm);
		// } else if (printerName.contains("L31")
		// || printerName.contains("T9")
		// || printerName.contains("POS885")
		// || printerName.contains("EU80")) {
		// rg__select_paper_size.check(R.id.rb_80mm);
		// } else if (printerName.contains("L51")) {
		// rg__select_paper_size.check(R.id.rb_100mm);
		// }
		// } else if (parent == spinner_interface_type) {
		// }

		if (parent == spTear) {
			tearSet = spTear.getSelectedItem().toString();
		}
		if (parent == spOpenCashBox) {
			OpenCashSet = spOpenCashBox.getSelectedItem().toString();
		}
		if (parent == spIsBeep) {
			IsBeepSet = spIsBeep.getSelectedItem().toString();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}
