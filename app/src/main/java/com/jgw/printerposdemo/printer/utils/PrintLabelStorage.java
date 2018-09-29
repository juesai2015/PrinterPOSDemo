package com.jgw.printerposdemo.printer.utils;

import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterConstants.PRotate;
import com.printer.sdk.exception.ParameterErrorException;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.WriteException;

import android.content.Context;

public class PrintLabelStorage {

	public void doPrintTSPL(final PrinterInstance iPrinter, final Context mContext) {
		new Thread() {
			public void run() {
				int left = PrefUtils.getInt(mContext, "leftmargin", 0);
				int top = PrefUtils.getInt(mContext, "topmargin", 0);
				int numbers = PrefUtils.getInt(mContext, "printnumbers", 1);
				int isBeep = PrefUtils.getInt(mContext, "isBeep", 0);
				int isOpenCash = PrefUtils.getInt(mContext, "isOpenCash", 0);
				try {
					// TODO 设置标签纸大小 测试时修改一下PrinterInstance里面的代码
					iPrinter.pageSetupTSPL(PrinterConstants.SIZE_58mm, 55 * 8, 70 * 8);
					// 清除缓存区内容
					iPrinter.sendStrToPrinterTSPL("CLS\r\n");
					// 设置标签的参考坐标原点
					if (left == 0 || top == 0) {
						// 不做设置，默认
					} else {
						iPrinter.sendStrToPrinterTSPL("REFERENCE " + left * 8 + "," + top * 8 + "\r\n");
					}
					// 画框
					iPrinter.drawBorderTSPL(2, 5 * 8, 5 * 8, 50 * 8, 65 * 8);
					// 将框分成三块 画俩条行线
					// 画第一条行线
					iPrinter.drawLineTSPL(5 * 8, 25 * 8, 45 * 8, 2);
					// 画第二条行线
					iPrinter.drawLineTSPL(5 * 8, 45 * 8, 45 * 8, 2);
					// 画第一块中的内容
					// 标题 反白打印

					iPrinter.drawTextTSPL(5 * 8, 5 * 8, 50 * 8, 5 * 8 + 8 * 8, PAlign.CENTER, PAlign.CENTER, true, 2, 2,
							PRotate.Rotate_0, "仓储行业");
					iPrinter.reverseAreaTSPL(15 * 8, 6 * 8 - 4, 25 * 8, 7 * 8);
					// 第一行
					iPrinter.drawTextTSPL(5 * 8 + 3 * 8, 5 * 8 + 8 * 8, 50 * 8, 5 * 8 + 8 * 8 + 4 * 8, PAlign.START,
							PAlign.CENTER, true, 1, 1, PRotate.Rotate_0, "PO:345-896779-0");
					// 第二行
					iPrinter.drawTextTSPL(5 * 8 + 3 * 8, 5 * 8 + 8 * 8 + 4 * 8, 50 * 8, 5 * 8 + 8 * 8 + 4 * 8 + 4 * 8,
							PAlign.START, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0, "Zone:4");
					// 第三行
					iPrinter.drawTextTSPL(5 * 8 + 3 * 8, 5 * 8 + 8 * 8 + 4 * 8 * 2, 50 * 8, 5 * 8 + 8 * 8 + 4 * 8 * 3,
							PAlign.START, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0, "DWCP:968484-23   STORE#49");

					// 画第二块中的内容
					// 第一行的文字
					iPrinter.drawTextTSPL(5 * 8, 25 * 8, 50 * 8, 30 * 8, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "SHIP TO LOC 0614141000531");
					// 第二行的条码
					iPrinter.drawBarCodeTSPL(5 * 8 + 5 * 8, 30 * 8, PBarcodeType.CODE128, 10 * 8, false,
							PRotate.Rotate_0, 1, 1, "0614141");
					// 第三行的文字
					iPrinter.drawTextTSPL(5 * 8, 40 * 8, 50 * 8, 45 * 8, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "(410)0614141000531");

					// 画第三块中的内容
					// 第一行的文字
					iPrinter.drawTextTSPL(5 * 8, 45 * 8, 50 * 8, 50 * 8, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "SSCC 0 0614141 1234567 0");
					// 第二行的条码
					iPrinter.drawBarCodeTSPL(5 * 8 + 5 * 8, 50 * 8, PBarcodeType.CODE128, 10 * 8, false,
							PRotate.Rotate_0, 1, 1, "0614141");
					// 第三行的文字
					iPrinter.drawTextTSPL(5 * 8, 60 * 8, 50 * 8, 65 * 8, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "(00)006141411234567890");

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
				} catch (WriteException e) {
					e.printStackTrace();
				} catch (PrinterPortNullException e) {
					e.printStackTrace();
				} catch (ParameterErrorException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

}
