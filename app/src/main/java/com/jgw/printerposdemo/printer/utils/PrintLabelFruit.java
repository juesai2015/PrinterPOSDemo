package com.jgw.printerposdemo.printer.utils;

import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterConstants.TwoDarCodeType;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.exception.ParameterErrorException;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.WriteException;

import android.content.Context;
import android.util.Log;

public class PrintLabelFruit {

	public void doPrintTSPL(final PrinterInstance iPrinter, final Context mContext) {
		new Thread() {
			public void run() {
				int left = PrefUtils.getInt(mContext, "leftmargin", 0);
				int top = PrefUtils.getInt(mContext, "topmargin", 0);
				int numbers = PrefUtils.getInt(mContext, "printnumbers", 1);
				int isBeep = PrefUtils.getInt(mContext, "isBeep", 0);
				int isOpenCash = PrefUtils.getInt(mContext, "isOpenCash", 0);
				Log.i("yxz", "左边距------------" + left);
				Log.i("yxz", "上边距------------" + top);
				Log.i("yxz", "打印份数------------" + numbers);

				try {
					// 设置标签纸大小
					iPrinter.pageSetupTSPL(PrinterConstants.SIZE_58mm, 56 * 8, 45 * 8);
					// 清除缓存区内容
					iPrinter.printText("CLS\r\n");
					// 设置标签的参考坐标原点
					if (left == 0 || top == 0) {
						// 不做设置，默认
					} else {
						iPrinter.sendStrToPrinterTSPL("REFERENCE " + left * 8 + "," + top * 8 + "\r\n");
					}
					// 横线上方区域
					// 横线上方区域左上方的文字（(0,0)(40*8,7*8)）
					iPrinter.drawTextTSPL(0, 0, 35 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, "HanBu");
					// 横线上方区域左下方的文字 （(0,0)(40*8,7*8)）
					iPrinter.drawTextTSPL(0, 0, 35 * 8, 7 * 8, PAlign.START, PAlign.END, true, 1, 1, null, "汉步");
					// 横线上方区域右侧的文字 （(100,0)(40*8,7*8)）
					iPrinter.drawTextTSPL(100, 0, 35 * 8, 7 * 8, PAlign.CENTER, PAlign.CENTER, true, 1, 2, null,
							"热敏不干胶纸");

					// 横线 (0,7*8+2);
					iPrinter.drawLineTSPL(0, 7 * 8 + 2, 35 * 8, 5);
					// 横线下方区域
					// 左侧的二维码
					iPrinter.draw2DBarCodeTSPL(0, 7 * 8 + 15, TwoDarCodeType.QR, 1, 4, null, "热敏不干胶纸");
					// 右侧第一行汉字
					iPrinter.drawTextTSPL(12 * 8, 7 * 8 + 10, true, 2, 2, null, "SIZE:");
					// 右侧第二行汉字
					iPrinter.drawTextTSPL(12 * 8, 7 * 8 + 55, true, 2, 2, null, "40MM*30MM");
					// 二维码下方的一维条码
					iPrinter.drawBarCodeTSPL(0, 20 * 8 + 4, PBarcodeType.CODE128, 5 * 8, false, null, 1, 2,
							"123456789012");
					iPrinter.drawTextTSPL(0, 25 * 8, 40 * 8, 30 * 8, PAlign.CENTER, PAlign.CENTER, true, 1, 1, null,
							"123456789012");
					// // 判断是否响应钱箱
					// if (isOpenCash == 1) {
					// // 打印前打开钱箱
					// iPrinter.openCashBoxTSPL(1, 2);
					// Thread.sleep(3000);
					// // 打印
					// iPrinter.printTSPL(numbers, 1);
					// } else if (isBeep == 2) {
					// // 打印
					// iPrinter.printTSPL(numbers, 1);
					// // 打印后后打开钱箱
					// iPrinter.openCashBoxTSPL(1, 2);
					// } else {
					// // 打印
					// iPrinter.printTSPL(numbers, 1);
					// }

					// 判断是否响应蜂鸣器
					if (isBeep == 1) {
						// 打印前响
						iPrinter.beepTSPL(1, 1000);
						Thread.sleep(3000);
						// 打印
						iPrinter.printTSPL(numbers, 1);
					} else if (isBeep == 2) {
						// 打印
						iPrinter.printTSPL(numbers, 1);
						// 打印后响
						// Thread.sleep(3000);
						iPrinter.beepTSPL(1, 1000);
					} else {
						// 打印
						iPrinter.printTSPL(numbers, 1);
					}

					// 备份

					// // 设置标签纸大小
					// iPrinter.pageSetupTSPL(PrinterConstants.SIZE_58mm, 40 *
					// 8, 30 *
					// 8);
					// // 清楚缓存区内容
					// iPrinter.printText("CLS\r\n");
					// // 横线上方区域
					// // 横线上方区域左上方的文字（(0,0)(40*8,7*8)）
					// iPrinter.drawTextTSPL(0 + left, 0 + top, 40 * 8 + left, 7
					// * 8 +
					// top, PAlign.START, PAlign.START, true, 1, 1,
					// null, "汉 步");
					// // 横线上方区域左下方的文字 （(0,0)(40*8,7*8)）
					// iPrinter.drawTextTSPL(0 + left, 0 + top, 40 * 8 + left, 7
					// * 8 +
					// top, PAlign.START, PAlign.END, true, 1, 1,
					// null, "HanBu");
					// // 横线上方区域右侧的文字 （(100,0)(40*8,7*8)）
					// iPrinter.drawTextTSPL(100 + left, 0 + top, 40 * 8 + left,
					// 7 * 8 +
					// top, PAlign.CENTER, PAlign.CENTER, true,
					// 1, 2, null, "热敏不干胶纸");
					//
					// // 横线 (0,7*8+2);
					// iPrinter.drawLineTSPL(0 + left, 7 * 8 + 2 + top, 40 * 8,
					// 5);
					// // 横线下方区域
					// // 左侧的二维码
					// iPrinter.draw2DBarCodeTSPL(0 + left, 7 * 8 + 15 + top,
					// TwoDarCodeType.QR, 1, 4, null, "热敏不干胶纸");
					// // 右侧第一行汉字
					// iPrinter.drawTextTSPL(14 * 8 + left, 7 * 8 + 10 + top,
					// true, 2,
					// 2, null, "SIZE:");
					// // 右侧第二行汉字
					// iPrinter.drawTextTSPL(14 * 8 + left, 7 * 8 + 55 + top,
					// true, 2,
					// 2, null, "40MM*30MM");
					// // 二维码下方的一维条码
					// iPrinter.drawBarCodeTSPL(0 + left, 20 * 8 + 4 + top,
					// PBarcodeType.CODE128, 5 * 8, false, null, 1, 2,
					// "123456789012");
					// iPrinter.drawTextTSPL(0 + left, 25 * 8 + top, 40 * 8 +
					// left, 30 *
					// 8 + top, PAlign.CENTER, PAlign.CENTER,
					// true, 1, 1, null, "123456789012");
					// // 打印
					// iPrinter.printTSPL(numbers, 1);
				} catch (WriteException e) {
					e.printStackTrace();
				} catch (PrinterPortNullException e) {
					e.printStackTrace();
				} catch (ParameterErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();

	}
}
