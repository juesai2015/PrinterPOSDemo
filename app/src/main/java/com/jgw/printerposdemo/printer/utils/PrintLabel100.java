package com.jgw.printerposdemo.printer.utils;

import android.util.Log;

import com.printer.sdk.PrinterConstants.LableFontSize;
import com.printer.sdk.PrinterConstants.LablePaperType;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterConstants.PRotate;
import com.printer.sdk.PrinterInstance;

public class PrintLabel100 {


	private static  int MULTIPLE = 11;
	private static final int line_width_border = 1;
	private static final int page_width = 75 * MULTIPLE;
	private static final int page_height = 80 * MULTIPLE;
	private static final int margin_horizontal = (int) (1.5 * MULTIPLE);
	private static final int top_left_x = margin_horizontal;
	private static final int margin_vertical = 3 * MULTIPLE;
	private static final int top_left_y = margin_vertical;//32
	private static final int border_width = page_width - 2 * margin_horizontal;
	private static final int border_height = (int) (page_height - 5 * margin_vertical);
	private static final int top_right_x = top_left_x + border_width;
	private static final int bottom_left_y = top_left_y + border_height;
	private static final int bottom_right_y = bottom_left_y;
	private static final int bottom_right_x = top_right_x;
	private static final int row36_column1_width = 10 * MULTIPLE;
	private static final int row37_column3_width = 20 * MULTIPLE;
	private static final int row36_sep1_x = top_left_x + row36_column1_width;
	private static final int row37_sep2_x = top_right_x - row37_column3_width;
	private static final int[] row_height = { 9 * MULTIPLE, 9 * MULTIPLE, 9 * MULTIPLE,9 * MULTIPLE,9 * MULTIPLE };
	private int startX;
	private int startX1;

	public  void doPrint(PrinterInstance iPrinter){


		iPrinter.pageSetup(LablePaperType.Size_100mm,page_width, page_height);
		drawBox(iPrinter);
		drawVerticalSeparator(iPrinter);
		drawRowContent(iPrinter);
		iPrinter.print(PRotate.Rotate_0, 1);

	}

	private  void drawBox(PrinterInstance iPrinter) {
		int border_top_left_y = top_left_y;
//		int border_top_left_y = top_left_y + row_height[0];
		iPrinter.drawBorder(3, top_left_x, top_left_y, bottom_right_x, bottom_right_y);
		drawHorizontalSeparator(iPrinter);

	}
	private void drawHorizontalSeparator(PrinterInstance iPrinter) {

		int temp = top_left_y; //
//		int temp = top_left_y+row_height[0]; //
		for (int i = 0; i < row_height.length; i++)
		{
			temp += row_height[i];
			int start_x = top_left_x;
			int end_x = top_right_x;
			Log.i("temp", "第"+(i+1)+"次");
			iPrinter.drawLine(line_width_border, start_x, temp, end_x, temp, false);
			/*if(i!= 3){
				iPrinter.drawLine(line_width_border, start_x, temp, end_x, temp);
			}else{
				iPrinter.drawLine(line_width_border, row37_sep2_x, temp, end_x, temp);
			}*/

		}
	}

	private void drawVerticalSeparator(PrinterInstance iPrinter) {

		startX = top_left_x + ((top_right_x - top_left_x) / 10)*3;
		startX1 = top_left_x + ((top_right_x - top_left_x) / 10)*5;
		int startY = top_left_y+row_height[0];
		int endY =  top_left_y+row_height[0]*5;
		iPrinter.drawLine(line_width_border, startX, startY,startX, endY, false);

		iPrinter.drawLine(line_width_border, startX1, endY,startX1, bottom_right_y, false);

	}

	private void drawRowContent(PrinterInstance iPrinter) {

		//第一行内容
		iPrinter.drawText(top_left_x, top_left_y, top_right_x, top_left_y+row_height[0], PAlign.CENTER, PAlign.CENTER,
				"XX物流",LableFontSize.Size_64, 1, 0, 0, 0, PRotate.Rotate_0);
		//第二行内容 左边栏内容
		iPrinter.drawText(top_left_x, top_left_y+row_height[0], startX, top_left_y+row_height[0]*2, PAlign.CENTER, PAlign.CENTER,
				"寄件人", LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);
//		Log.i("sp", "startX:"+startX+"top_right_x:"+top_right_x);
		//第二行内容 右边栏内容
		iPrinter.drawText(startX, top_left_y+row_height[0], bottom_right_x, top_left_y+row_height[0]*2, PAlign.CENTER, PAlign.CENTER,
				"王西 010-1234567",  LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);

		//第三行内容  左边栏内容
		iPrinter.drawText(top_left_x, top_left_y+row_height[0]*2, startX, top_left_y+row_height[0]*3, PAlign.CENTER, PAlign.CENTER,
				"收件人",  LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);
		//第三行内容 右边栏内容
		iPrinter.drawText(startX, top_left_y+row_height[0]*2, top_right_x, top_left_y+row_height[0]*3, PAlign.CENTER, PAlign.CENTER,
				"业务经理 杨凯 156 1212 9516",  LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);
		//第四行内容  左边栏内容
		iPrinter.drawText(top_left_x, top_left_y+row_height[0]*3, startX, top_left_y+row_height[0]*4, PAlign.CENTER, PAlign.CENTER,
								"收件人地址",  LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);

		//第四行内容 右边栏内容
		iPrinter.drawText(startX, top_left_y+row_height[0]*3, top_right_x, top_left_y+row_height[0]*4, PAlign.START, PAlign.CENTER,
								"  XX市XX区XX号院XX号楼XX层",  LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);
		//第五行内容  左边栏内容
		iPrinter.drawText(top_left_x, top_left_y+row_height[0]*4, startX, top_left_y+row_height[0]*5, PAlign.CENTER, PAlign.CENTER,
						"收件人签名",  LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);

		//第六行内容 右边栏内容
		iPrinter.drawText(startX, top_left_y+row_height[0]*4, top_right_x, top_left_y+row_height[0]*5, PAlign.CENTER, PAlign.CENTER,
						"业务经理 杨凯 156 1212 9516",  LableFontSize.Size_32, 0, 0, 0, 0, PRotate.Rotate_0);
		//第6行左侧条码
		iPrinter.drawBarCode(top_left_x+35, top_left_y+row_height[0]*5, startX1+35, bottom_right_y-40, PAlign.CENTER, PAlign.CENTER, 0, 0,
				"010-1234567",  PBarcodeType.CODE128, 1, 80, PRotate.Rotate_0);
		//第6行左侧条码下面的内容
		iPrinter.drawText(top_left_x, top_left_y+row_height[0]*5+90, startX1, bottom_right_y, PAlign.CENTER, PAlign.CENTER,
								"010-12345678", LableFontSize.Size_24, 0, 0, 0, 0, PRotate.Rotate_0);
		iPrinter.drawQrCode(startX1+10, top_left_y+row_height[0]*5+30,
				"https://www.baidu.com/",
				PRotate.Rotate_0, startX, startX);

		//第6行左侧条码下面的内容
		iPrinter.drawText(startX1+180, top_left_y+row_height[0]*5, bottom_right_x, bottom_right_y, PAlign.CENTER, PAlign.CENTER,
										"扫一扫     关注我", LableFontSize.Size_24, 0, 0, 0, 0, PRotate.Rotate_0);


	}




}
