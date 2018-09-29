package com.jgw.printerposdemo.printer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.jgw.printerposdemo.R;
import com.jgw.printerposdemo.printer.global.GlobalContants;
import com.jgw.printerposdemo.printer.utils.XTUtils;

public class SplashActivity extends Activity {
private Handler handler2;
private String apkurl;
private String description;
private String version;
private ProgressDialog dialog;
protected String TAG;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		checkUpdate();
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		handler2=new Handler();
		handler2.postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent=new Intent(SplashActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, 2000
		);


	}

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
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
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
						version = (String) obj.get("version");

						description = (String) obj.get("descrption");
						apkurl = (String) obj.get("apkurl");

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
					mes.what = GlobalContants.URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					mes.what = GlobalContants.NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
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
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 弹出升级对话框
	 */
	protected void showUpdateDialog() {
		// this = Activity.this
		Builder builder = new Builder(SplashActivity.this);
		builder.setTitle("提示升级");
		// builder.setCancelable(false);//强制升级
		builder.setMessage("发现新版本"+version+"\n"+"当前版本"+getPackageName()+"\n"+"是否更新");
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// 进入主页面
				// enterHome();
				dialog.dismiss();

			}
		});
//		builder.setMessage(description);
		builder.setPositiveButton("立刻升级",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// 下载APK，并且替换安装
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							// sdcard存在
							// afnal
							FinalHttp finalhttp = new FinalHttp();
							finalhttp.download(apkurl, Environment
									.getExternalStorageDirectory()
									.getAbsolutePath()
									+ "/PrintDemo.apk",
									new AjaxCallBack<File>() {

										@Override
										public void onFailure(Throwable t,
												int errorNo, String strMsg) {
											t.printStackTrace();
											Toast.makeText(
													getApplicationContext(),
													"下载失败", 1).show();
											super.onFailure(t, errorNo, strMsg);
										}

										@Override
										public void onLoading(long count,
												long current) {
											super.onLoading(count, current);
											// tv_update_info.setVisibility(View.VISIBLE);
											// //当前下载百分比
											// int progress = (int) (current *
											// 100 / count);
											// tv_update_info.setText("下载进度："+progress+"%");
											// dialog.setMax(timeout / 1000);
											// handler.obtainMessage(GlobalContants.PROGRESS_UPDATE).sendToTarget();

											handler.obtainMessage(
													GlobalContants.PROGRESS_UPDATE,
													(int) count, (int) current)
													.sendToTarget();
											Log.i(TAG, "updating!");
										}

										@Override
										public void onSuccess(File t) {
											super.onSuccess(t);
											installAPK(t);
										}

										@Override
										public void onStart() {
											super.onStart();
											Log.i(TAG, "start update!");
											handler.obtainMessage(
													GlobalContants.START_UPDATE)
													.sendToTarget();
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
											intent.setDataAndType(
													Uri.fromFile(t),
													"application/vnd.android.package-archive");
											startActivity(intent);
											android.os.Process
													.killProcess(android.os.Process
															.myPid());
										}
									});
						} else {
							Toast.makeText(getApplicationContext(),
									"没有sdcard，请安装上在试", 0).show();
							return;
						}

					}
				});

		builder.setNegativeButton("下次再说",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// enterHome();// 进入主页面
					}
				});
		builder.show();

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
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
				Toast.makeText(getApplicationContext(), "URL错误", 1).show();

				break;

			case GlobalContants.NETWORK_ERROR:// 网络异常
				// enterHome();
				Toast.makeText(SplashActivity.this, "网络异常", 1).show();
				break;

			case GlobalContants.JSON_ERROR:// JSON解析出错
				// enterHome();
				Toast.makeText(SplashActivity.this, "JSON解析出错", 1).show();
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
}
