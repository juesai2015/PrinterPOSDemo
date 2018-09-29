package com.jgw.printerposdemo.printer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jgw.printerposdemo.R;

public class ItemPdfPdfactivity extends Activity implements OnItemClickListener {
	private ListView listView;
	private String TAG = "com.print.demo";
	private Context mContext;
	private ArrayAdapter<String> fileAdapter;
	ArrayList<String> list = null;
	private List<File> fileList = new ArrayList<File>();
	String pdf_text = null;
	String str = null;
	private ProgressDialog daDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itempdf);
		mContext = this;
		init();
		daDialog = ProgressDialog.show(ItemPdfPdfactivity.this, "Handing the PDF file....", "Please Wait", true, false,
				null);
		// new Thread(){
		// public void run(){
		Log.i("pdfActivity", "zl at onclick-------->begain");
		fileAdapter = new ArrayAdapter<String>(ItemPdfPdfactivity.this, R.layout.device_item);
		Log.i("pdfActivity", "zl at onclick-------->fileAdapter");
		// onSearch();
		Log.i("pdfActivity", "zl at onclick-------->onsearch");
		// 设置listview中的内容
		listView.setAdapter(fileAdapter);
		Log.i("pdfActivity", "zl at onclick-------->listView.setAdapter");
		// }
		// }.start();
		listView.setOnItemClickListener(this);
		new Thread() {
			public void run() {
				onSearch();
			};
		}.start();

	}

	// new Thread(){
	// public void run(){
	//
	// }
	// }.start();
	public void onSearch() {

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			final File sdFile = Environment.getExternalStorageDirectory();
			getAllFiles(sdFile);
			daDialog.dismiss();
			Log.i("mainactivity", "zl --onSearch()----list.size:" + list.size());
		} else {
			Toast.makeText(ItemPdfPdfactivity.this, "SD卡不存在", 0).show();
		}
	}

	/* 遍历接收一个文件路径，然后把文件子目录中的所有文件遍历并输出来 */
	private void getAllFiles(final File root) {
		// new Thread() {
		// public void run() {
		File files[] = root.listFiles();
		list = new ArrayList<String>();
		if (files != null) {
			for (final File f : files) {
				if (f.isDirectory()) {
					getAllFiles(f);
				} else {
					if (f.getAbsolutePath().endsWith(".pdf")) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								fileList.add(f);
								fileAdapter.add(f.getName());
								Log.i("mainactivity", "zl --getAllFiles()----fileAdapter.add(f.getAbsolutePath()):"
										+ f.getAbsolutePath());
							}
						});

					}
				}
			}
		}
		// }
		// }.run();
	}

	private void init() {
		listView = (ListView) super.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		pdf_text = fileList.get(position).getAbsolutePath();
		Intent intent_item_pdfActivity = new Intent();
		intent_item_pdfActivity.putExtra("data_return", pdf_text);
		setResult(RESULT_OK, intent_item_pdfActivity);

		finish();
	}

}
