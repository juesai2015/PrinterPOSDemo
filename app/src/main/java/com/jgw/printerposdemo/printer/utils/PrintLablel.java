package com.jgw.printerposdemo.printer.utils;

import android.util.Log;


import com.printer.sdk.PrinterInstance;
import com.printer.sdk.PrinterConstants.LableFontSize;
import com.printer.sdk.PrinterConstants.LablePaperType;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterConstants.PRotate;

public class PrintLablel {

	private static int MULTIPLE = 11;
	private static final int line_width_border = 1;
	private static final int page_width = 75 * MULTIPLE;
	private static final int page_height = 80 * MULTIPLE;
	private static final int margin_horizontal = (int) (1.5 * MULTIPLE);
	private static final int top_left_x = margin_horizontal;
	private static final int margin_vertical = 3 * MULTIPLE;
	private static final int top_left_y = margin_vertical;// 32
	private static final int border_width = page_width - 2 * margin_horizontal;
	private static final int border_height = (int) (page_height - 8
			* margin_vertical - 8);
	private static final int top_right_x = top_left_x + border_width;
	private static final int bottom_left_y = top_left_y + border_height;
	private static final int bottom_right_y = bottom_left_y;
	private static final int bottom_right_x = top_right_x;
	private static final int row36_column1_width = 10 * MULTIPLE;
	private static final int row37_column3_width = 20 * MULTIPLE;
	private static final int row36_sep1_x = top_left_x + row36_column1_width;
	private static final int row37_sep2_x = top_right_x - row37_column3_width;
	private static final int[] row_height = { 6 * MULTIPLE, 6 * MULTIPLE,
			6 * MULTIPLE, 6 * MULTIPLE, 6 * MULTIPLE, 6 * MULTIPLE,
			6 * MULTIPLE, 6 * MULTIPLE };
	private int startX;
	private int startX1;

	public void doPrint(PrinterInstance iPrinter) {

		iPrinter.pageSetup(LablePaperType.Size_80mm,page_width, page_height);
		drawBigBox(iPrinter);
		drawHorizontalSeparator(iPrinter);
		drawVerticalSeparator(iPrinter);
		drawRowContent(iPrinter);
		iPrinter.print(PRotate.Rotate_0, 0);

	}

	private void drawBigBox(PrinterInstance iPrinter) {
		int border_top_left_y = top_left_y;
		// int border_top_left_y = top_left_y + row_height[0];
		iPrinter.drawBorder(3, top_left_x, top_left_y, bottom_right_x,
				bottom_right_y);
		Log.i("fdh", "bottom_right_x0:"+bottom_right_x);

	}

	private void drawHorizontalSeparator(PrinterInstance iPrinter) {

		int temp = top_left_y; //
		// int temp = top_left_y+row_height[0]; //
		for (int i = 0; i < row_height.length; i++) {
			temp += row_height[i];
			int start_x = top_left_x + 10;
			int end_x = (int)(top_left_x + ((top_right_x - top_left_x) / 10) * 6.9);
			Log.i("temp", "第" + (i + 1) + "次");
			iPrinter.drawLine(line_width_border, start_x, temp, end_x, temp,
					false);
			/*
			 * if(i!= 3){ iPrinter.drawLine(line_width_border, start_x, temp,
			 * end_x, temp); }else{ iPrinter.drawLine(line_width_border,
			 * row37_sep2_x, temp, end_x, temp); }
			 */

		}
	}

	private void drawVerticalSeparator(PrinterInstance iPrinter) {

		startX = top_left_x + ((top_right_x - top_left_x) / 10) * 2;
		startX1 = (int)(top_left_x + ((top_right_x - top_left_x) / 10) * 6.9);
		int startY = top_left_y + row_height[0];
		int endY = top_left_y + row_height[0] * 8;
		iPrinter.drawLine(line_width_border, startX, startY, startX, endY,
				false);
		iPrinter.drawLine(line_width_border * 2, top_left_x + 10, startY,
				top_left_x + 10, endY, false);
		iPrinter.drawLine(line_width_border * 2, startX1, startY,
				startX1, endY, false);

		// iPrinter.drawLine(line_width_border, startX1, endY, startX1,
		// bottom_right_y, false);

	}

	private void drawRowContent(PrinterInstance iPrinter) {

		// 第一行内容
		iPrinter.drawText(top_left_x, top_left_y, top_right_x, top_left_y
				+ row_height[0], PAlign.CENTER, PAlign.END, "ZAX",
				LableFontSize.Size_64, 1, 0, 0, 0, PRotate.Rotate_0);
		iPrinter.drawText(top_left_x, top_left_y, top_right_x, top_left_y
				+ row_height[0], PAlign.END, PAlign.CENTER, "中安信科技有限公司",
				LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
		iPrinter.drawText(top_left_x, top_left_y, top_right_x, top_left_y
				+ row_height[0], PAlign.END, PAlign.END, "Zhong An Xin Technology Co.Lsg",
				LableFontSize.Size_16, 0, 0, 0, 0, PRotate.Rotate_0);
		// 第二行内容 左边栏内容
		iPrinter.drawText(top_left_x, top_left_y + row_height[0], startX,
				top_left_y + row_height[0] + row_height[0] / 2, PAlign.CENTER,
				PAlign.END, "规格:", LableFontSize.Size_24, 0, 0, 0, 0,
				PRotate.Rotate_0);
		iPrinter.drawText(top_left_x, top_left_y + row_height[0]
				+ row_height[0] / 2 + 4, startX,
				top_left_y + row_height[0] * 2, PAlign.CENTER, PAlign.START,
				"TYPE:", LableFontSize.Size_24, 0, 0, 0, 0, PRotate.Rotate_0);
		// Log.i("sp", "startX:"+startX+"top_right_x:"+top_right_x);
		// 第二行内容 右边栏内容
		iPrinter.drawText(startX, top_left_y + row_height[0], bottom_right_x,
				top_left_y + row_height[0] * 2, PAlign.CENTER, PAlign.CENTER,
				"T700XX", LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);

		// 第三行内容 左边栏内容
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 2, startX,
				top_left_y + row_height[0] * 2 + row_height[0] / 2,
				PAlign.CENTER, PAlign.END, "型号:", LableFontSize.Size_24, 0, 0,
				0, 0, PRotate.Rotate_0);
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 2
				+ row_height[0] / 2 + 4, startX,
				top_left_y + row_height[0] * 3, PAlign.CENTER, PAlign.START,
				"FILAMENTS:", LableFontSize.Size_24, 0, 0, 0, 0,
				PRotate.Rotate_0);
		// 第三行内容 右边栏内容
		iPrinter.drawText(startX, top_left_y + row_height[0] * 2, top_right_x,
				top_left_y + row_height[0] * 3, PAlign.CENTER, PAlign.CENTER,
				"12K—XXX", LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);
		// 第四行内容 左边栏内容
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 3, startX,
				top_left_y + row_height[0] * 3 + row_height[0] / 2,
				PAlign.CENTER, PAlign.END, "批次:", LableFontSize.Size_24, 0, 0,
				0, 0, PRotate.Rotate_0);
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 3
				+ row_height[0] / 2 + 4, startX,
				top_left_y + row_height[0] * 4, PAlign.CENTER, PAlign.START,
				"LOT NO.", LableFontSize.Size_24, 0, 0, 0, 0, PRotate.Rotate_0);

		// 第四行内容 右边栏内容
		iPrinter.drawText(startX, top_left_y + row_height[0] * 3, top_right_x,
				top_left_y + row_height[0] * 4, PAlign.CENTER, PAlign.CENTER,
				"XXXXXXXXX", LableFontSize.Size_32, 0, 0, 0, 0,
				PRotate.Rotate_0);
		// 第五行内容 左边栏内容
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 4, startX,
				top_left_y + row_height[0] * 4 + row_height[0] / 2,
				PAlign.CENTER, PAlign.END, "序列号:", LableFontSize.Size_24, 0, 0,
				0, 0, PRotate.Rotate_0);
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 4
				+ row_height[0] / 2 + 4, startX,
				top_left_y + row_height[0] * 5, PAlign.CENTER, PAlign.START,
				"CASE NO.", LableFontSize.Size_24, 0, 0, 0, 0, PRotate.Rotate_0);

		// 第五行内容 右边栏内容
		iPrinter.drawText(startX, top_left_y + row_height[0] * 4, top_right_x,
				top_left_y + row_height[0] * 5, PAlign.CENTER, PAlign.CENTER,
				"XXXXXXXXX", LableFontSize.Size_32, 0, 0, 0, 0,
				PRotate.Rotate_0);
		// 第六行内容 左边栏内容
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 5, startX,
				top_left_y + row_height[0] * 5 + row_height[0] / 2,
				PAlign.CENTER, PAlign.END, "时间:", LableFontSize.Size_24, 0, 0,
				0, 0, PRotate.Rotate_0);
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 5
				+ row_height[0] / 2 + 4, startX,
				top_left_y + row_height[0] * 6, PAlign.CENTER, PAlign.START,
				"DATE:", LableFontSize.Size_24, 0, 0, 0, 0, PRotate.Rotate_0);
		// 第六行内容 右边栏内容
		iPrinter.drawText(startX, top_left_y + row_height[0] * 5, top_right_x,
				top_left_y + row_height[0] * 6, PAlign.CENTER, PAlign.CENTER,
				" 8/11/2015 10:50 AM ", LableFontSize.Size_32, 0, 0, 0, 0,
				PRotate.Rotate_0);
		// 第七行内容 左边栏内容
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 6, startX,
				top_left_y + row_height[0] * 6 + row_height[0] / 2,
				PAlign.CENTER, PAlign.END, "重量:", LableFontSize.Size_24, 0, 0,
				0, 0, PRotate.Rotate_0);
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 6
				+ row_height[0] / 2 + 4, startX,
				top_left_y + row_height[0] * 7, PAlign.CENTER, PAlign.START,
				"NET wt:", LableFontSize.Size_24, 0, 0, 0, 0, PRotate.Rotate_0);
		// 第七行内容 右边栏内容
		iPrinter.drawText(startX, top_left_y + row_height[0] * 6, top_right_x,
				top_left_y + row_height[0] * 7, PAlign.CENTER, PAlign.CENTER,
				"4.00 kg", LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);
		// 第八行内容 左边栏内容
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 7, startX,
				top_left_y + row_height[0] * 7 + row_height[0] / 2,
				PAlign.CENTER, PAlign.END, "长度:", LableFontSize.Size_24, 0, 0,
				0, 0, PRotate.Rotate_0);
		iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 7
				+ row_height[0] / 2 + 4, startX,
				top_left_y + row_height[0] * 8, PAlign.CENTER, PAlign.START,
				"LENGTH:", LableFontSize.Size_24, 0, 0, 0, 0, PRotate.Rotate_0);
		// 第八行内容 右边栏内容
		iPrinter.drawText(startX, top_left_y + row_height[0] * 7, top_right_x,
				top_left_y + row_height[0] * 8, PAlign.CENTER, PAlign.CENTER,
				"5000 m", LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);
		//打印第九行条码

		Log.i("fdh", "bottom_right_x:"+bottom_right_x);
		iPrinter.drawBarCode(top_left_x, top_left_y+ row_height[0] * 8, 576, bottom_right_y, PAlign.CENTER, PAlign.CENTER, 0, 0,
				"DF12345678900010292001", PBarcodeType.CODE128, 1, 70, PRotate.Rotate_0);

	}

}
