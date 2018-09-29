package com.jgw.printerposdemo.printer.utils;

import android.util.Log;

import com.printer.sdk.PrinterConstants.LableFontSize;
import com.printer.sdk.PrinterConstants.LablePaperType;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterConstants.PRotate;
import com.printer.sdk.PrinterInstance;

public class PrintLabel80_1 {
	// 材料80mm

	private static int MULTIPLE = 2;
	private static final int line_width_border = 2; // 边框
	private static final int page_width = 260 * MULTIPLE; // 内容宽
	private static final int page_height = 165 * MULTIPLE; // 内容高
	private static final int margin_horizontal = 2 * MULTIPLE; // 水平边距
	private static final int top_left_x = margin_horizontal; // 左上角x坐标，左上角为原点
	private static final int margin_vertical = 2 * MULTIPLE; // 垂直边距
	private static final int top_left_y = margin_vertical; // 左上角y坐标
	private static final int border_width = page_width - 2 * margin_horizontal; // 边框宽度
	private static final int border_height = page_height - 2 * margin_vertical; // 边框高度
	private static final int top_right_x = top_left_x + border_width; // 右上角x坐标
	private static final int bottom_left_y = top_left_y + border_height; // 左下角y坐标
	private static final int bottom_right_y = bottom_left_y; // 右下角y坐标
	private static final int bottom_right_x = top_right_x; // 右下角x坐标
	private static final int row36_column1_width = 10 * MULTIPLE;
	private static final int row37_column3_width = 20 * MULTIPLE;
	private static final int row36_sep1_x = top_left_x + row36_column1_width;
	private static final int row37_sep2_x = top_right_x - row37_column3_width;
	private static final int[] row_height = { 10 * MULTIPLE * 2, 27 * MULTIPLE, 27 * MULTIPLE, 27 * MULTIPLE,
			27 * MULTIPLE };
	private static final String TAG = "PrintLabel";

	public void doPrint(PrinterInstance iPrinter) {

		iPrinter.pageSetup(LablePaperType.Size_80mm, page_width, page_height);
		drawBox(iPrinter);
		// drawVerticalSeparator(iPrinter);
		drawRowContent(iPrinter);
		iPrinter.print(PRotate.Rotate_0, 1);

	}

	private void drawRowContent(PrinterInstance iPrinter) {

		// int area_start_x = top_left_x + (top_right_x - top_left_x) / 2;
		// int area_start_y = top_left_y;
		// 第一行左一内容
		// iPrinter.drawText(top_left_x, top_left_y, top_left_x + 2 *
		// (border_width) / 3 + 20, top_left_y + row_height[1],
		// PAlign.START, PAlign.END, "全程路运 12-02 04:22浙江",
		// LableFontSize.Size_32, 1, 0, 0, 0, PRotate.Rotate_0);
		iPrinter.drawText(top_left_x, top_left_y, "全程路运 12-02 04:22浙江", LableFontSize.Size_32, PRotate.Rotate_0, 1, 0,
				0);
		// 第一行左二内容
		/*
		 * iPrinter.drawText(top_left_x + 5 * (border_width) / 9, top_left_y ,
		 * top_left_x + 2 * (border_width) / 3, top_left_y + row_height[1] ,
		 * PAlign.CENTER, PAlign.CENTER, "浙江", LableFontSize.Size_32, 1, 0, 0,
		 * 0, PRotate.Rotate_0);
		 */
		// 第一行右二内容
		iPrinter.drawText(top_left_x + 2 * (border_width) / 3 + 20, top_left_y,
				top_left_x + 7 * (border_width) / 9 + 50, top_left_y + row_height[1], PAlign.END, PAlign.END, "邮特",
				LableFontSize.Size_32, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第一行右一内容
		iPrinter.drawText(top_left_x + 7 * (border_width) / 9 + 30, top_left_y, top_right_x, top_left_y + row_height[1],
				PAlign.END, PAlign.END, "2014", LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第二行内容
		iPrinter.drawText(top_left_x + 7 * (border_width) / 9, top_left_y + row_height[1], top_right_x,
				top_left_y + row_height[1] * 2, PAlign.CENTER, PAlign.END, "特快", LableFontSize.Size_32, 1, 0, 0, 0,
				PRotate.Rotate_0);
		// 第三行左一内容
		iPrinter.drawText(top_left_x, top_left_y + row_height[1] * 2, top_left_x + 5 * (border_width) / 9,
				top_left_y + row_height[1] * 3, PAlign.START, PAlign.END, "义乌市 ", LableFontSize.Size_48, 1, 0, 0, 0,
				PRotate.Rotate_0);
		// 第三行左二内容
		iPrinter.drawText(top_left_x + 5 * (border_width) / 9, top_left_y + row_height[1] * 2 + 26,
				top_left_x + 2 * (border_width) / 3, top_left_y + row_height[1] * 3, PAlign.START, PAlign.START, "局收",
				LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第三行右二内容
		iPrinter.drawText(top_left_x + 2 * (border_width) / 3, top_left_y + row_height[1] * 2 + 26,
				top_left_x + 7 * (border_width) / 9, top_left_y + row_height[1] * 3, PAlign.END, PAlign.START, "号码",
				LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第三行右一内容
		iPrinter.drawText(top_left_x + 7 * (border_width) / 9, top_left_y + row_height[1] * 2, top_right_x,
				top_left_y + row_height[1] * 3, PAlign.CENTER, PAlign.END, "58280" + " ", LableFontSize.Size_24, 1, 0,
				2, 0, PRotate.Rotate_0);
		// 第四行左一内容
		iPrinter.drawText(top_left_x, top_left_y + row_height[1] * 3, top_left_x + 5 * (border_width) / 9,
				top_left_y + row_height[1] * 4, PAlign.START, PAlign.END, "江西集散  7件", LableFontSize.Size_32, 1, 0, 0, 0,
				PRotate.Rotate_0);
		// 第四行左二内容
		iPrinter.drawText(top_left_x + 5 * (border_width) / 9, top_left_y + row_height[1] * 3 + 26,
				top_left_x + 2 * (border_width) / 3, top_left_y + row_height[1] * 4, PAlign.START, PAlign.START, "局发",
				LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

		// 第四行右二内容
		iPrinter.drawText(top_left_x + 2 * (border_width) / 3, top_left_y + row_height[1] * 3 + 26,
				top_left_x + 7 * (border_width) / 9, top_left_y + row_height[1] * 4, PAlign.END, PAlign.START, "重量",
				LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
		// 第四行右一内容
		iPrinter.drawText(top_left_x + 7 * (border_width) / 9, top_left_y + row_height[1] * 3, top_right_x,
				top_left_y + row_height[1] * 4, PAlign.CENTER, PAlign.END, " " + "10" + "  ", LableFontSize.Size_24, 1,
				0, 2, 0, PRotate.Rotate_0);
		// 打印第五行条码

		// iPrinter.drawBarCode(top_left_x, top_left_y + row_height[1] * 4,
		// bottom_right_x, bottom_right_y - 30,
		// PAlign.START, PAlign.END, 0, 0,
		// "33000166-32206100-1-131-5828-0060-3-0", PBarcodeType.CODE128, 1, 60,
		// PRotate.Rotate_0);
		iPrinter.drawBarCode(0, top_left_y + row_height[1] * 4, "33000166322061001131582800603",
				PBarcodeType.CODE128, 1, 60, PRotate.Rotate_0);

		// 打印第五行条码下方的文字
		iPrinter.drawText(top_left_x, top_left_y + bottom_right_y - 30, bottom_right_x, bottom_right_y, PAlign.START,
				PAlign.START, "33000166-32206100-1-131-5828-0060-3", LableFontSize.Size_24, 1, 0, 0, 0,
				PRotate.Rotate_0);

	}

	private void drawHorizontalSeparator(PrinterInstance iPrinter, int start_x, int end_x) {

		int temp = top_left_y; //
		for (int i = 1; i < row_height.length; i++) {
			temp += row_height[i];
			int start_x1 = top_left_x + 7 * (border_width) / 9;
			int end_x1 = top_left_x + 5 * (border_width) / 9;
			// Log.i("temp", "第"+(i+1)+"次");
			iPrinter.drawLine(2, start_x, temp, end_x1, temp, true);
			/*
			 * if(i > 2 ){ iPrinter.drawLine(1, start_x1, temp,
			 * end_x,temp,true); }
			 */
			/*
			 * if(i!= 3){ iPrinter.drawLine(line_width_border, start_x, temp,
			 * end_x, temp); }else{ iPrinter.drawLine(line_width_border,
			 * row37_sep2_x, temp, end_x, temp); }
			 */
		}
	}

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
		// iPrinter.drawBorder(3, top_left_x, border_top_left_y, bottom_right_x,
		// bottom_right_y);
		drawHorizontalSeparator(iPrinter, top_left_x, bottom_right_x);

	}

}