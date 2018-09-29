package com.jgw.printerposdemo.printer;

import com.artifex.mupdf.MuPDFCore;
import com.jgw.printerposdemo.R;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PdfPrintActivity extends BaseActivity implements OnClickListener {
	private Button btnPrintPdf;
	private Button btnPrintthis;
	private Button btnPrevious;
	private Button btnNext;
	private Button btnChosePdf;
	private ImageView ivShowPdf;
	private TextView tvShowPage;
	private LinearLayout header;
	private int currentCount = -1;
	private int pageCount = -1;
	private Bitmap bitmap;
	private String TAG = "com.printer.demo.ui";
	private String path = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdf_print);
		btnPrintPdf = (Button) findViewById(R.id.btn_printpdf);
		btnPrevious = (Button) findViewById(R.id.btn_pre);
		btnNext = (Button) findViewById(R.id.btn_next);
		btnPrintthis = (Button) findViewById(R.id.btn_printthis);
		btnChosePdf = (Button) findViewById(R.id.btn_choose_pdf_file);
		btnChosePdf.setOnClickListener(this);
		btnPrintPdf.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnPrintthis.setOnClickListener(this);
		ivShowPdf = (ImageView) findViewById(R.id.iv_showpdf);
		tvShowPage = (TextView) findViewById(R.id.tv_showpage);
		header = (LinearLayout) findViewById(R.id.ll_headerview_Pdf_Printactivity);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		initHeader();

	}

	@Override
	protected void onResume() {
		super.onResume();
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
		setHeaderCenterText(header, getString(R.string.headview_PdfPrint));
	}

	int count = 0;

	@Override
	public void onClick(View v) {
		if (PrinterInstance.mPrinter == null && !SettingActivity.isConnected) {
			Toast.makeText(PdfPrintActivity.this, getString(R.string.no_connected), 0).show();
			return;
		}
		switch (v.getId()) {
		case R.id.btn_printpdf:
			if (path == null) {
				Toast.makeText(PdfPrintActivity.this, "请先选择pdf文件", Toast.LENGTH_SHORT).show();
				return;
			}
			Toast.makeText(PdfPrintActivity.this, "准备打印整份pdf，请稍候...", Toast.LENGTH_SHORT).show();
			// 打印整份pdf
			Log.i(TAG, "yxz at PdfPrintActivity.java onClick() case R.id.btn_printpdf: pdf文件共有 " + pageCount + "页");
			new Thread() {
				public void run() {
					for (count = 0; count < pageCount; count++) {
						if (SettingActivity.isConnected) {
							PointF pageSize = core.getPageSize(count);
							float pageW = pageSize.x;
							float pageH = pageSize.y;
							bitmap = Bitmap.createBitmap((int) pageW, (int) pageH, Bitmap.Config.ARGB_8888);
							core.drawPage(count, bitmap, (int) pageW, (int) pageH, 0, 0, (int) pageW, (int) pageH);
							bitmap = Utils.zoomImage(bitmap, PrinterConstants.paperWidth);
							PrinterInstance.mPrinter.printImage(bitmap, PAlign.NONE, 0, 128);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				};
			}.start();

			break;
		case R.id.btn_printthis:
			if (path == null) {
				Toast.makeText(PdfPrintActivity.this, "请先选择pdf文件", Toast.LENGTH_SHORT).show();
				return;
			}
			Toast.makeText(PdfPrintActivity.this, "准备打印，请稍候...", Toast.LENGTH_SHORT).show();

			// 打印当前页码的pdf
			if (currentCount >= pageCount) {
				currentCount = pageCount - 1;
			}
			if (currentCount <= 0) {
				currentCount = 0;
			}
			PointF pageSize = core.getPageSize(currentCount);
			float pageW = pageSize.x;
			float pageH = pageSize.y;
			Log.i(TAG, "yxz at PdfPrintActivity.java onclick() case R.id.btn_printthis pageW:" + pageW + "  pageH:"
					+ pageH);
			bitmap = Bitmap.createBitmap((int) pageW, (int) pageH, Bitmap.Config.ARGB_8888);
			core.drawPage(currentCount, bitmap, (int) pageW, (int) pageH, 0, 0, (int) pageW, (int) pageH);
			new InnerAsyncTask().execute();
			break;
		case R.id.btn_pre:
			if (path == null) {
				Toast.makeText(PdfPrintActivity.this, "请先选择pdf文件", Toast.LENGTH_SHORT).show();
				return;
			}
			// 显示上一页pdf
			currentCount--;
			if (currentCount <= 0) {
				currentCount = 0;
				showImage(currentCount);
				tvShowPage.setText("1/" + pageCount);
			} else {
				showImage(currentCount);
				tvShowPage.setText(currentCount + 1 + "/" + pageCount);
			}
			break;
		case R.id.btn_next:
			if (path == null) {
				Toast.makeText(PdfPrintActivity.this, "请先选择pdf文件", Toast.LENGTH_SHORT).show();
				return;
			}
			// 显示下页pdf
			currentCount++;
			if (currentCount >= pageCount) {
				currentCount = pageCount;
				showImage(currentCount);
				tvShowPage.setText(core.countPages() + "/" + pageCount);
			} else {
				showImage(currentCount);
				tvShowPage.setText(currentCount + 1 + "/" + pageCount);

			}
			break;
		case R.id.btn_choose_pdf_file:
			Intent intent_item_pdf = new Intent(PdfPrintActivity.this, ItemPdfPdfactivity.class);
			startActivityForResult(intent_item_pdf, 1);
			break;
		}

	}

	private void showImage(int count) {
		Log.d(TAG, "yxz at PdfPrintActivity.java showImage() count:" + count);
		PointF pageSize = core.getPageSize(count);
		float pageW = pageSize.x;
		float pageH = pageSize.y;
		Log.d(TAG, "yxz at PdfPrintActivity.java showImage() pageW:" + pageW + "  pageH:" + pageH);
		Bitmap bitmap = Bitmap.createBitmap((int) pageW, (int) pageH, Bitmap.Config.ARGB_8888);
		core.drawPage(count, bitmap, (int) pageW, (int) pageH, 0, 0, (int) pageW, (int) pageH);
		ivShowPdf.setImageBitmap(bitmap);
	}

	private class InnerAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Log.i(TAG, "yxz at PdfPrintActivity.java InnerAsyncTask 图片原始宽：" + bitmap.getWidth() + " 图片原始高："
					+ bitmap.getHeight());
			bitmap = Utils.zoomImage(bitmap, PrinterConstants.paperWidth);
			Log.i(TAG, "yxz at PdfPrintActivity.java InnerAsyncTask 缩放后宽：" + bitmap.getWidth() + " 缩放后高："
					+ bitmap.getHeight());
			 PrinterInstance.mPrinter.printImage(bitmap, PAlign.NONE, 0, 128);
			PrinterInstance.mPrinter.printColorImg2Gray(bitmap, PAlign.NONE, 0, false);
			return null;
		}

	}

	private MuPDFCore core = null;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			path = data.getStringExtra("data_return");
			if (path == null) {
				Toast.makeText(PdfPrintActivity.this, ".pdf文件路径为空", Toast.LENGTH_SHORT).show();
				ivShowPdf.setImageResource(R.drawable.ic_launcher);
			} else {
				try {
					core = new MuPDFCore(this, path);
				} catch (Exception e) {
					e.printStackTrace();
				}
				/********** 以下处理转成图片 ***********/
				pageCount = core.countPages();
				Log.i(TAG, "yxz at PdfPrintActivity.java onActivityResult() pageCount:" + pageCount);
				currentCount = 0;
				Log.i(TAG, "yxz at PdfPrintActivity.java onActivityResult() currentCount:" + currentCount);
				showImage(currentCount);
				tvShowPage.setText(1 + "/" + pageCount);
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

}
