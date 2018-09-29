package com.jgw.printerposdemo.printer.utils;

import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.PrinterConstants.CommandTSPL;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.exception.ParameterErrorException;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.WriteException;

import android.content.Context;

public class PrintLabelDrink {
	public void doPrintTSPL(final PrinterInstance iPrinter, final Context mContext) {
		new Thread() {
			public void run() {
				int left = PrefUtils.getInt(mContext, "leftmargin", 0);
				int top = PrefUtils.getInt(mContext, "topmargin", 0);
				int numbers = PrefUtils.getInt(mContext, "printnumbers", 1);
				int isBeep = PrefUtils.getInt(mContext, "isBeep", 0);
				int isOpenCash = PrefUtils.getInt(mContext, "isOpenCash", 0);
				try {
					// 设置标签纸大小
					iPrinter.pageSetupTSPL(PrinterConstants.SIZE_58mm, 56 * 8, 45 * 8);
					// 清除缓存区内容
					iPrinter.sendStrToPrinterTSPL("CLS\r\n");
					// 设置标签的参考坐标原点
					if (left == 0 || top == 0) {
						// 不做设置，默认
					} else {
						iPrinter.sendStrToPrinterTSPL("REFERENCE " + left * 8 + "," + top * 8 + "\r\n");
					}
					// 打印第一行的内容
					iPrinter.setPrinterTSPL(CommandTSPL.DENSITY, 10);
					iPrinter.drawTextTSPL(0, 0, 56 * 8, 8 * 8, PAlign.CENTER, PAlign.CENTER, true, 2, 2, null,
							"蜜果蜜制鲜饮");
					// 打印第二行内容
					iPrinter.setPrinterTSPL(CommandTSPL.DENSITY, 1);
					iPrinter.drawTextTSPL(0, 8 * 8, 56 * 8, 8 * 8 * 2, PAlign.END, PAlign.CENTER, true, 2, 2, null,
							"蜜果奶茶（中）");
					// 打印第三行内容
					iPrinter.drawTextTSPL(20, 8 * 8 * 2, true, 2, 2, null, "价格减二");
					// 打印第四行内容
					iPrinter.drawTextTSPL(0, 8 * 8 * 3, 56 * 8, 8 * 8 * 4, PAlign.CENTER, PAlign.CENTER, true, 2, 2,
							null, "￥6.00");
					// 打印第五行内容
					iPrinter.drawTextTSPL(0, 8 * 8 * 4, 56 * 8, 8 * 8 * 5, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							null, PrefUtils.getSystemTime2());
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
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
