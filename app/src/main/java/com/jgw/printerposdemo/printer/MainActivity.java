package com.jgw.printerposdemo.printer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jgw.printerposdemo.R;
import com.jgw.printerposdemo.printer.global.GlobalContants;
import com.jgw.printerposdemo.printer.utils.XTUtils;
import com.printer.sdk.PrinterInstance;

public class MainActivity extends BaseActivity implements View.OnClickListener {
	private static final String TAG = "MainActivity";
	// 设置打印机的LinearLayout
	private LinearLayout llSetPrinter;
	// 打印标签的LinearLayout
	private LinearLayout llLablePrint;
	// 打印文本的LinearLayout
	private LinearLayout llTextPrint;
	// 打印PDF文件的LinearLayout
	private LinearLayout llPDFPrint;
	// 打印条码的LinearLayout
	private LinearLayout llBarcodePrint;
	// 打印图片的LinearLayout
	private LinearLayout llImagePrint;
	private LinearLayout header;
	private static Context mContext;
	private View v1;
	private String apkurl;
	private String description;
	private ProgressDialog dialog;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		v1 = getLayoutInflater().inflate(R.layout.activity_main, null);
		setContentView(v1);
		init();
		mContext = getApplicationContext();
		IntentFilter filter = new IntentFilter();
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		mContext.registerReceiver(mUsbAttachReceiver, filter);

		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		checkUpdate();
		// TODO
		// fu = new FileUtils();
		// fu.createFile(this);// 向SD卡上写入打印文件
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case GlobalContants.SHOW_UPDATE_DIALOG:// 显示升级的对话框
				Log.i(TAG, "显示升级的对话框");
				showUpdateDialog();
				break;
			case GlobalContants.ENTER_HOME:// 进入主页面
				// enterHome();
				break;

			case GlobalContants.URL_ERROR:// URL错误
				// enterHome();
				// Toast.makeText(getApplicationContext(), "URL错误", 1).show();

				break;

			case GlobalContants.NETWORK_ERROR:// 网络异常
				// enterHome();
				// Toast.makeText(MainActivity.this, "网络异常", 1).show();
				break;

			case GlobalContants.JSON_ERROR:// JSON解析出错
				// enterHome();
				// Toast.makeText(MainActivity.this, "JSON解析出错", 1).show();
				break;
			case GlobalContants.PROGRESS_UPDATE:// 更新进度
				dialog.setMax(msg.arg1 / 1024);
				dialog.setProgress(msg.arg2 / 1024);
				break;
			case GlobalContants.FINISH_UPDATE:// 更新完成
				dialog.dismiss();
				break;
			case GlobalContants.START_UPDATE:// 开始更新
				dialog.setProgress(0);
				dialog.setTitle("update...waiting...");
				dialog.show();

				break;

			default:
				break;
			}
		}

	};

	// 初始化界面
	private void init() {

		header = (LinearLayout) findViewById(R.id.ll_header_mainactivity);
		llSetPrinter = (LinearLayout) findViewById(R.id.ll_setting);
		llLablePrint = (LinearLayout) findViewById(R.id.ll_lable_print);
		llTextPrint = (LinearLayout) findViewById(R.id.ll_text_print);
		llPDFPrint = (LinearLayout) findViewById(R.id.ll_pdf_print);
		llBarcodePrint = (LinearLayout) findViewById(R.id.ll_barcode_print);
		llImagePrint = (LinearLayout) findViewById(R.id.ll_image_print);

		// 设置点击监听事件
		llSetPrinter.setOnClickListener(this);
		llLablePrint.setOnClickListener(this);
		llTextPrint.setOnClickListener(this);
		llPDFPrint.setOnClickListener(this);
		llBarcodePrint.setOnClickListener(this);
		llImagePrint.setOnClickListener(this);
		initHeader();
	}

	// 点击监听事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_setting:
			Intent intent = new Intent(MainActivity.this, SettingActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		case R.id.ll_lable_print:
			Intent intent_lable = new Intent(MainActivity.this, LablePrintActivity.class);
			startActivity(intent_lable);
			break;
		case R.id.ll_text_print:
			Intent intent_text = new Intent(MainActivity.this, TextPrintActivity.class);
			startActivity(intent_text);
			break;
		case R.id.ll_pdf_print:
			Intent intent2 = new Intent(MainActivity.this, PdfPrintActivity.class);
			startActivity(intent2);
			break;
		case R.id.ll_barcode_print:
			Intent intent_barcode = new Intent(MainActivity.this, BarcoePrintActivity.class);
			startActivity(intent_barcode);
			break;
		case R.id.ll_image_print:
			Intent intent_image = new Intent(MainActivity.this, PicturePrintActivity.class);
			startActivity(intent_image);
			// String json = null;
			// try {
			// json = jsonToStringFromAssetFolder("ok.json",
			// getApplicationContext());
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// Log.i("PrinterInstance", "json的值第一部分----" + json.substring(0,
			// 5000));
			// Log.i("PrinterInstance",
			// "json的值第二部分----" + json.substring(5001, 10000));
			// Log.i("PrinterInstance",
			// "json的值第三部分----" + json.substring(10001, 15000));
			// Log.i("PrinterInstance",
			// "json的值第四部分----" + json.substring(15001, 20000));
			// Log.i("PrinterInstance",
			// "json的值第五部分----" + json.substring(20001, 25000));
			// Log.i("PrinterInstance",
			// "json的值第六部分----" + json.substring(25001, json.length()));
			//
			// List<byte[]> data = new ArrayList<byte[]>();
			// data = parserFromJson(json);
			// // for (int i = 0; i < data.size(); i++) {
			// // myPrinter.sendBytesData(data.get(i));
			// // }
			// Log.e("PrinterInstance", "执行完发送指令了");
			//
			// break;

		default:
			break;
		}
	}

	public static String jsonToStringFromAssetFolder(String fileName, Context context) throws IOException {
		AssetManager manager = context.getAssets();
		InputStream file = manager.open(fileName);

		byte[] data = new byte[file.available()];
		file.read(data);
		file.close();
		return new String(data, "gbk");
		// return new String(data);
	}

	/**
	 * 初始化标题上的信息
	 */
	private void initHeader() {
		setHeaderLeftText(header, "", null);
		headerConnecedState.setText(getTitleState());
	}

	@Override
	protected void onStart() {
		super.onStart();
		initHeader();
	}

	private static BroadcastReceiver mUsbAttachReceiver = new BroadcastReceiver() {
		@SuppressLint("NewApi")
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.w("fdh", "receiver action: " + action);

			if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
				Toast.makeText(mContext, "USB设备已接入", 0).show();
			} else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				Toast.makeText(mContext, "USB设备已拔出", 0).show();
				if (PrinterInstance.mPrinter != null && SettingActivity.isConnected) {
					PrinterInstance.mPrinter.closeConnection();
					PrinterInstance.mPrinter = null;
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mContext.unregisterReceiver(mUsbAttachReceiver);
	}

	// @Override
	// protected void onStart() {
	// super.onStart();
	// initHeader();
	// }

	/**
	 * 检查是否有新版本，如果有就升级
	 */
	private void checkUpdate() {

		new Thread() {
			public void run() {
				// URLhttp://192.168.1.254:8080/updateinfo.html

				Message mes = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {

					URL url = new URL(getString(R.string.serverurl));
					// 联网
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					int code = conn.getResponseCode();
					if (code == 200) {
						// 联网成功
						InputStream is = conn.getInputStream();
						// 把流转成String
						String result = XTUtils.readFromStream(is);
						Log.i(TAG, "联网成功了" + result);
						// json解析
						JSONObject obj = new JSONObject(result);
						// 得到服务器的版本信息
						String version = (String) obj.get("version");

						description = (String) obj.get("descrption");
						apkurl = (String) obj.get("apkurl1");

						// 校验是否有新版本
						if (getVersionName().equals(version)) {
							// 版本一致，没有新版本，进入主页面
							mes.what = GlobalContants.ENTER_HOME;
						} else {
							// 有新版本，弹出一升级对话框
							mes.what = GlobalContants.SHOW_UPDATE_DIALOG;

						}

					}

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					mes.what = GlobalContants.URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					mes.what = GlobalContants.NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mes.what = GlobalContants.JSON_ERROR;
				} finally {

					long endTime = System.currentTimeMillis();
					// 我们花了多少时间
					long dTime = endTime - startTime;
					// 2000
					if (dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					handler.sendMessage(mes);
					Log.i(TAG, "mes:" + mes);
				}

			};
		}.start();

	}

	/**
	 * 得到应用程序的版本名称
	 */

	private String getVersionName() {
		// 用来管理手机的APK
		PackageManager pm = getPackageManager();

		try {
			// 得到知道APK的功能清单文件
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 弹出升级对话框
	 */
	protected void showUpdateDialog() {
		// this = Activity.this
		Builder builder = new Builder(MainActivity.this);
		builder.setTitle("提示升级");
		// builder.setCancelable(false);//强制升级
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// 进入主页面
				// enterHome();
				dialog.dismiss();

			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("立刻升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				// 下载APK，并且替换安装
				try {
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						// sdcard存在
						// afnal
						FinalHttp finalhttp = new FinalHttp();
						finalhttp.download(apkurl,
								Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrintDemo.apk",
								new AjaxCallBack<File>() {

									@Override
									public void onFailure(Throwable t, int errorNo, String strMsg) {
										super.onFailure(t, errorNo, strMsg);
										t.printStackTrace();
										Toast.makeText(getApplicationContext(), "下载失败", 1).show();
										handler.obtainMessage(GlobalContants.FINISH_UPDATE).sendToTarget();
									}

									@Override
									public void onLoading(long count, long current) {
										// TODO Auto-generated method stub
										super.onLoading(count, current);
										// tv_update_info.setVisibility(View.VISIBLE);
										// //当前下载百分比
										// int progress = (int) (current * 100 /
										// count);
										// tv_update_info.setText("下载进度："+progress+"%");
										// dialog.setMax(timeout / 1000);
										// handler.obtainMessage(GlobalContants.PROGRESS_UPDATE).sendToTarget();

										handler.obtainMessage(GlobalContants.PROGRESS_UPDATE, (int) count,
												(int) current).sendToTarget();
										Log.i(TAG, "updating!");
									}

									@Override
									public void onSuccess(File t) {
										// TODO Auto-generated method stub
										super.onSuccess(t);
										installAPK(t);
									}

									@Override
									public void onStart() {
										super.onStart();
										Log.i(TAG, "start update!");
										handler.obtainMessage(GlobalContants.START_UPDATE).sendToTarget();
									}

									/**
									 * 安装APK
									 * 
									 * @param t
									 */
									private void installAPK(File t) {
										Intent intent = new Intent();
										intent.setAction("android.intent.action.VIEW");
										intent.addCategory("android.intent.category.DEFAULT");
										intent.setDataAndType(Uri.fromFile(t),
												"application/vnd.android.package-archive");
										startActivity(intent);
										android.os.Process.killProcess(android.os.Process.myPid());
									}
								});
					} else {
						Toast.makeText(getApplicationContext(), "没有sdcard，请安装上在试", 0).show();
						return;
					}
				} catch (Exception e) {
					Log.i(TAG, "有异常!");
					e.printStackTrace();
				}
			}
		});
		builder.setNegativeButton("下次再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				// enterHome();// 进入主页面
			}
		});
		builder.show();

	}

	public ArrayList<byte[]> parserFromJson(String jsonString) {
		// 返回值byte[]数组的arrayList集合
		ArrayList<byte[]> datas = null;
		// 每循环一次的str字符串转换成的data[]数组
		byte[] data = null;
		StringBuffer str = new StringBuffer("");
		JSONObject jobj = null;
		JSONArray jarr = null;
		try {
			// 获取JSON
			jobj = new JSONObject(jsonString);
			jarr = jobj.getJSONArray("opts");
			Log.e(TAG, "解析的json字符串的长度为      " + jarr.length());
			// 遍历JSON
			for (int i = 0; i < jarr.length(); i++) {
				Log.e(TAG, "循环次数为      " + i);
				// datas = new ArrayList<byte[]>();
				// data = new byte[jarr.length()];
				JSONObject jItem = jarr.getJSONObject(i);
				// 获取optCode
				String optCode = jItem.getString("optCode");
				// optCode=10为起始指令，第四个参数可以修改
				if (optCode.equals("0")) {
					// 空指令，无实际意义
				} else if (optCode.equals("10")) {
					// 准备指令，预备打印 指令头
					str.append("! 0 200 200 1208 1 PAGE-WIDTH 832\r\n");
				} else if (optCode.equals("11")) {
					// 完成指令，等待打印机打印 指令尾
					str.append("FORM\r\nPRINT\r\n");
				} else if (optCode.equals("12")) {
					// 文字

					int x = jItem.getInt("x");

					int y = jItem.getInt("y");

					int fontType = jItem.getInt("fontType");

					boolean isBold = jItem.getBoolean("isBold");
					String content = jItem.getString("content");

					// TODO
					FontSizes fs = new FontSizes();
					fs.getFontAndSize(fontType);
					str.append("TEXT" + " " + fs.getFont() + " " + fs.getSize() + " " + x + " " + y + " " + content
							+ "\r\n");

					if (isBold) {
						str.append("SETBOLD 1\r\n");
					} else {
						str.append("SETBOLD 0\r\n");
					}

				} else if (optCode.equals("13")) {
					// 线段
					int beginX = jItem.getInt("beginX");
					int beginY = jItem.getInt("beginY");
					int endX = jItem.getInt("endX");
					int endY = jItem.getInt("endY");
					int lineWidth = jItem.getInt("width");
					str.append(
							"LINE" + " " + beginX + " " + beginY + " " + endX + " " + endY + " " + lineWidth + "\r\n");

				} else if (optCode.equals("14")) {
					// 一维条码
					int barcodeType = jItem.getInt("barcodeType");
					int width = jItem.getInt("width");
					double ratio = jItem.getDouble("ratio");
					int height = jItem.getInt("height");
					int x = jItem.getInt("x");
					int y = jItem.getInt("y");
					String number = jItem.getString("number");
					int finalratio = doubleToInt(ratio);
					// TODO
					int barcodeTypeReall = 128;
					if (barcodeType == 1) {
						barcodeTypeReall = 128;
					}
					str.append("BARCODE" + " " + barcodeTypeReall + " " + width + " " + finalratio + " " + height + " "
							+ x + " " + y + " " + number + "\r\n");
				} else if (optCode.equals("15")) {
					// 二维条码
					int x = jItem.getInt("x");
					int y = jItem.getInt("y");
					int qrVersion = jItem.getInt("qrVersion");
					int unitHeight = jItem.getInt("unitHeight");
					int level = jItem.getInt("level");
					String content = jItem.getString("content");
					// TODO
					str.append("BARCODE" + " " + "QR" + " " + x + " " + y + " " + "M 2 U " + unitHeight + " "
							+ levelNumberToChar(level) + qrVersion + "A," + content + "\r\n");

				} else if (optCode.equals("16")) {

					// 点阵图形
					int width = jItem.getInt("width");
					int realWidth = width / 8;

					int height = jItem.getInt("height");

					int x = jItem.getInt("x");

					int y = jItem.getInt("y");

					String octetStr = jItem.getString("octetStr");

					// TODO
					str.append("EG" + " " + realWidth + " " + height + " " + x + " " + y + " " + octetStr + "\r\n");
				}

			}
			String CPCLComand = str.toString();
			Log.e(TAG, "解析晚的第一部分" + CPCLComand.substring(0, 5000));
			Log.e(TAG, "解析晚的第二部分" + CPCLComand.substring(5001, 8000));
			Log.e(TAG, "解析晚的第二部分" + CPCLComand.substring(8001, CPCLComand.length()));
			// data = str.toString().getBytes();
			// datas.add(data);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;

	}

	public static class FontSizes {

		// font = 24 西文 12*24 中文 24*24
		// font = 55 西文 8*16 中文 16*16
		public int font;
		public int size;

		public void getFontAndSize(int fontType) {
			switch (fontType) {
			case 2: // 12*24 24*24
				font = 24;
				size = 00;
				break;
			case 5: // 24*48 48*48
				font = 24;
				size = 11;
				break;
			case 6: // 24点阵 放大两�? 36*72 72*72
				font = 24;
				size = 22;
				break;

			case 1:
				font = 55;// 8*16 16点阵
				size = 00;
				break;
			case 4: // 32点阵�?6*32 32*32 32点阵
				font = 55;
				size = 11;
				break;
			// TODO
			case 3: // 数字英文点阵
				font = 24;
				size = 11;
				break;

			default: // 24*24
				font = 24;
				size = 00;
				break;
			}
		}

		public int getSize() {
			return size;
		}

		public int getFont() {
			return font;
		}

	}

	public int doubleToInt(double ratio) {
		int finalratio = 2;
		if (ratio == 1.5) {
			finalratio = 1;
		} else if (ratio == 2.0) {
			finalratio = 1;
		} else if (ratio == 2.5) {
			finalratio = 2;
		} else if (ratio == 3.0) {
			finalratio = 3;
		} else if (ratio == 3.5) {
			finalratio = 4;
		} else if (ratio == 2.1) {
			finalratio = 21;
		} else if (ratio == 2.2) {
			finalratio = 22;
		} else if (ratio == 2.3) {
			finalratio = 23;
		} else if (ratio == 2.4) {
			finalratio = 24;
		} else if (ratio == 2.5) {
			finalratio = 25;
		} else if (ratio == 2.6) {
			finalratio = 26;
		} else if (ratio == 2.7) {
			finalratio = 27;
		} else if (ratio == 2.8) {
			finalratio = 28;
		} else if (ratio == 2.9) {
			finalratio = 29;
		} else if (ratio == 3.0) {
			finalratio = 30;
		}
		return finalratio;

	}

	public char levelNumberToChar(int level) {
		char chars = 'M';
		switch (level) {
		case 1:
			chars = 'L';
			break;
		case 2:
			chars = 'M';
			break;
		case 3:
			chars = 'Q';
			break;
		case 4:
			chars = 'H';

			break;
		}
		return chars;

	}

}
