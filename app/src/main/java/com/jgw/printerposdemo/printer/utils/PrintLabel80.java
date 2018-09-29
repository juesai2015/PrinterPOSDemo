package com.jgw.printerposdemo.printer.utils;

import android.util.Log;

import com.printer.sdk.PrinterConstants.LableFontSize;
import com.printer.sdk.PrinterConstants.LablePaperType;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterConstants.PRotate;
import com.printer.sdk.PrinterInstance;

public class PrintLabel80 {

	private static int MULTIPLE = 8;
	private static final int line_width_border = 2;
	private static final int page_width = 75 * MULTIPLE;
	private static final int page_height = 90 * MULTIPLE;
	private static final int margin_horizontal = 2 * MULTIPLE;
	private static final int top_left_x = margin_horizontal;
	private static final int margin_vertical = 2 * MULTIPLE;
	private static final int top_left_y = margin_vertical;// 32
	private static final int border_width = page_width - 2 * margin_horizontal;
	private static final int border_height = page_height - 2 * margin_vertical;
	private static final int top_right_x = top_left_x + border_width;
	private static final int bottom_left_y = top_left_y + border_height;
	private static final int bottom_right_y = bottom_left_y;
	private static final int bottom_right_x = top_right_x;
	private static final int row36_column1_width = 10 * MULTIPLE;
	private static final int row37_column3_width = 20 * MULTIPLE;
	private static final int row36_sep1_x = top_left_x + row36_column1_width;
	private static final int row37_sep2_x = top_right_x - row37_column3_width;
	private static final int[] row_height = { 8 * MULTIPLE * 2, 10 * MULTIPLE, 10 * MULTIPLE, 10 * MULTIPLE,
			10 * MULTIPLE, 10 * MULTIPLE };
	private static final String TAG = "PrintLabel";

	public void doPrint(final PrinterInstance iPrinter) {
		new Thread() {
			public void run() {
				for (int i = 0; i < 1; i++) {
					iPrinter.pageSetup(LablePaperType.Size_80mm, page_width, page_height);
					drawBox(iPrinter);
					drawVerticalSeparator(iPrinter);
					drawRowContent(iPrinter);
					iPrinter.print(PRotate.Rotate_0, 1);
				}
			};
		}.start();

	}

	private void drawRowContent(PrinterInstance iPrinter) {

		int area_start_x = top_left_x + (top_right_x - top_left_x) / 2;
		int area_start_y = top_left_y;
		// 第一行内容
		iPrinter.drawText(area_start_x, area_start_y, bottom_right_x, top_left_y + row_height[0] - line_width_border,
				PAlign.CENTER, PAlign.CENTER, "定时达1", LableFontSize.Size_64, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第二行内容
		iPrinter.drawText(top_left_x, top_left_y + row_height[0], bottom_right_x,
				top_left_y + row_height[0] + row_height[1], PAlign.CENTER, PAlign.CENTER, "DF1234567890",
				LableFontSize.Size_64, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第三行内容 左边栏内容
		iPrinter.drawText(top_left_x + 6, top_left_y + row_height[0] + row_height[1], top_left_x + row_height[1],
				top_left_y// +3弥补边框线宽误差
						+ row_height[0] + row_height[1] * 2,
				PAlign.CENTER, PAlign.CENTER, "目的 分拨 ", LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第三行内容 中间栏内容
		iPrinter.drawText(top_left_x + row_height[1], top_left_y + row_height[0] + row_height[1],
				top_left_x + 3 * (top_right_x - top_left_x) / 4, top_left_y + row_height[0] + row_height[1] * 2,
				PAlign.CENTER, PAlign.CENTER, "西安分拨中心", LableFontSize.Size_32, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第三行内容 右边栏内容
		iPrinter.drawText(top_left_x + 3 * (top_right_x - top_left_x) / 4, top_left_y + row_height[0] + row_height[1],
				bottom_right_x, top_left_y + row_height[0] + row_height[1] * 2, PAlign.CENTER, PAlign.CENTER, "第1件",
				LableFontSize.Size_24, 0, 0, 0, 0, PRotate.Rotate_0);

		// 第四行内容 左边栏内容
		iPrinter.drawText(top_left_x + 6, top_left_y + row_height[0] + row_height[1] * 2,
				top_left_x + row_height[1] + 3, top_left_y + row_height[0] + row_height[1] * 3, PAlign.CENTER,
				PAlign.CENTER, "到达 网点 ", LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第四行内容 中间栏内容
		iPrinter.drawText(top_left_x + row_height[1], top_left_y + row_height[0] + row_height[1] * 2,
				top_left_x + 3 * (top_right_x - top_left_x) / 4, top_left_y + row_height[0] + row_height[1] * 3,
				PAlign.CENTER, PAlign.CENTER, "西安长线", LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);
		// 第四行内容 右边栏内容
		iPrinter.drawText(top_left_x + 3 * (top_right_x - top_left_x) / 4,
				top_left_y + row_height[0] + row_height[1] * 2, bottom_right_x,
				top_left_y + row_height[0] + row_height[1] * 3, PAlign.CENTER, PAlign.CENTER, "1",
				LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第五行内容 左边栏内容
		iPrinter.drawText(top_left_x + 6, top_left_y + row_height[0] + row_height[1] * 3,
				top_left_x + row_height[1] + 3, top_left_y + row_height[0] + row_height[1] * 4, PAlign.CENTER,
				PAlign.CENTER, "出发 网点 ", LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第五行内容 中间栏内容
		iPrinter.drawText(top_left_x + row_height[1], top_left_y + row_height[0] + row_height[1] * 3 + 6,
				top_left_x + 3 // +6弥补线宽的误差
						* (top_right_x - top_left_x) / 4,
				top_left_y + row_height[0] + row_height[1] * 4, PAlign.CENTER, PAlign.CENTER,
				"测试二级网点         (2016-05-31 19:02)", LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);
		// 第五行内容 右边栏内容
		iPrinter.drawText(top_left_x + 3 * (top_right_x - top_left_x) / 4,
				top_left_y + row_height[0] + row_height[1] * 3, bottom_right_x,
				top_left_y + row_height[0] + row_height[1] * 5, PAlign.CENTER, PAlign.CENTER, "袋装",
				LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

		// 第六行内容 左边栏内容
		iPrinter.drawText(top_left_x + 6, top_left_y + row_height[0] + row_height[1] * 4,
				top_left_x + row_height[1] + 3, top_left_y + row_height[0] + row_height[1] * 5, PAlign.CENTER,
				PAlign.CENTER, "详细 地址 ", LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第六行内容 中间栏内容
		iPrinter.drawText(top_left_x + row_height[1], top_left_y + row_height[0] + row_height[1] * 4,
				top_left_x + 3 * (top_right_x - top_left_x) / 4, top_left_y + row_height[0] + row_height[1] * 5,
				PAlign.CENTER, PAlign.CENTER, "陕西省西安市临潼区秦始皇陵兵马俑一号坑五排三列俑", LableFontSize.Size_24, 0, 0, 0, 0,
				PRotate.Rotate_0);

		// 打印第七行条码

		iPrinter.drawBarCode(top_left_x, top_left_y + row_height[0] + row_height[1] * 5,
				top_left_x + 3 * (top_right_x - top_left_x) / 4, bottom_right_y, PAlign.CENTER, PAlign.CENTER, 0, 0,
				"DF12345678900010292001", PBarcodeType.CODE128, 1, 70, PRotate.Rotate_0);

		// 打印第七行条码下方的文字
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] + row_height[1] * 5 + 110,
				top_left_x + 3 * (top_right_x - top_left_x) / 4, bottom_right_y, PAlign.CENTER, PAlign.CENTER,
				"DF12345678900010292001", LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

		// 第五行内容 右边栏内容
		iPrinter.drawText(top_left_x + 3 * (top_right_x - top_left_x) / 4,
				top_left_y + row_height[0] + row_height[1] * 5, bottom_right_x, bottom_right_y, PAlign.CENTER,
				PAlign.CENTER, "派送", LableFontSize.Size_48, 1, 0, 0, 0, PRotate.Rotate_0);
	}

	private void drawHorizontalSeparator(PrinterInstance iPrinter, int start_x, int end_x) {

		int temp = top_left_y; //
		for (int i = 0; i < row_height.length; i++) {
			temp += row_height[i];
			// int start_x = top_left_x;
			// int end_x = top_right_x;
			// Log.i("temp", "第"+(i+1)+"次");
			if (i == 4)
				end_x = top_left_x + 3 * (top_right_x - top_left_x) / 4;
			else
				end_x = bottom_right_x;
			iPrinter.drawLine(2, start_x, temp, end_x, temp, true);
			/*
			 * if(i!= 3){ iPrinter.drawLine(line_width_border, start_x, temp,
			 * end_x, temp); }else{ iPrinter.drawLine(line_width_border,
			 * row37_sep2_x, temp, end_x, temp); }
			 */
		}
	}

	//
	private void drawVerticalSeparator(PrinterInstance iPrinter) {

		int start_x = top_left_x + row_height[1];
		int start_y = top_left_y + row_height[0] + row_height[1];
		int end_x = start_x;
		int end_y = top_left_y + row_height[0] + row_height[1] * 5;
		// 从左边数起第一条分割线
		iPrinter.drawLine(line_width_border, start_x, start_y, end_x, end_y, true);
		// 从左边数起第二条分割线
		start_x = top_left_x + (top_right_x - top_left_x) / 2;
		start_y = top_left_y;
		end_x = start_x;
		end_y = start_y + row_height[0];
		Log.i(TAG, "start_x；" + start_x + "end_x：" + end_x);
		iPrinter.drawLine(line_width_border, start_x, start_y, end_x, end_y, true);
		// 从左边数起第三条分割线
		start_x = top_left_x + 3 * (top_right_x - top_left_x) / 4;
		start_y = top_left_y + row_height[0] + row_height[1];
		end_x = start_x;
		end_y = bottom_right_y;
		iPrinter.drawLine(line_width_border, start_x, start_y, end_x, end_y, true);

	}

	private void drawBox(PrinterInstance iPrinter) {
		int border_top_left_y = top_left_y;
		iPrinter.drawBorder(3, top_left_x, border_top_left_y, bottom_right_x, bottom_right_y);
		drawHorizontalSeparator(iPrinter, top_left_x, bottom_right_x);

	}

}
