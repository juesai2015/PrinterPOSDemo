package com.jgw.printerposdemo.printer.utils;

import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterConstants.PRotate;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.exception.ParameterErrorException;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.WriteException;

import android.content.Context;

public class PrintLabelMaterial {

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
					iPrinter.pageSetupTSPL(PrinterConstants.SIZE_80mm, 80 * 8, 90 * 8);
					// 清除缓存区内容
					iPrinter.printText("CLS\r\n");
					// 设置标签的参考坐标原点
					if (left == 0 || top == 0) {
						// 不做设置，默认
					} else {
						iPrinter.sendStrToPrinterTSPL("REFERENCE " + left * 8 + "," + top * 8 + "\r\n");
					}
					// 最外面的方框(0,0)--(72*8,90*8)
					iPrinter.drawBorderTSPL(3, 0, 0, 72 * 8, 90 * 8);
					// 大框和小框之间的标题内容
					// 标题中间内容
					iPrinter.drawTextTSPL(0, 0, 72 * 8, 10 * 8, PAlign.CENTER, PAlign.END, true, 3, 3, null, "ZAX");
					// 标题右上方内容
					iPrinter.drawTextTSPL(0, 0, 72 * 8, 10 * 8, PAlign.END, PAlign.CENTER, true, 1, 1, null,
							"中安信科技有限公司");
					// 标调右下方内容
					iPrinter.drawTextTSPL(0, 0, 72 * 8, 10 * 8, PAlign.END, PAlign.END, true, 1, 1, null,
							"Zhong An Xin Technology");
					// 里面的小框(10,10*8)(72*8-10,80*8)
					iPrinter.drawBorderTSPL(2, 10, 10 * 8, 72 * 8 - 10, 80 * 8);
					// 小框里的第一条横线
					iPrinter.drawLineTSPL(10, 10 * 8 * 2, 72 * 8 - 20, 2);
					// 打印第一行左边的内容
					iPrinter.drawTextTSPL(10, 10 * 8 + 5, 180, 10 * 8 * 2, PAlign.CENTER, PAlign.START, true, 1, 1,
							null, "规格:");
					iPrinter.drawTextTSPL(10, 10 * 8, 180, 10 * 8 * 2 - 5, PAlign.CENTER, PAlign.END, true, 1, 1, null,
							"TYPE:");
					// 打印第一行右边的内容
					iPrinter.drawTextTSPL(180, 10 * 8, 72 * 8 - 10, 10 * 8 * 2, PAlign.CENTER, PAlign.CENTER, true, 1,
							1, null, "T700XX");
					// 小框里的第二条行横线
					iPrinter.drawLineTSPL(10, 10 * 8 * 3, 72 * 8 - 20, 2);
					// 打印第二行左边的内容
					iPrinter.drawTextTSPL(10, 10 * 8 * 2 + 5, 180, 10 * 8 * 3, PAlign.CENTER, PAlign.START, true, 1, 1,
							null, "型号:");
					iPrinter.drawTextTSPL(10, 10 * 8 * 2, 180, 10 * 8 * 3 - 5, PAlign.CENTER, PAlign.END, true, 1, 1,
							null, "FILAMNTS:");
					// 打印第二行右边的内容
					iPrinter.drawTextTSPL(180, 10 * 8 * 2, 72 * 8 - 10, 10 * 8 * 3, PAlign.CENTER, PAlign.CENTER, true,
							1, 1, null, "12K-XXX");
					// 小框里的第三条横线
					iPrinter.drawLineTSPL(10, 10 * 8 * 4, 72 * 8 - 20, 2);
					// 打印第三行左边内容
					iPrinter.drawTextTSPL(10, 10 * 8 * 3 + 5, 180, 10 * 8 * 4, PAlign.CENTER, PAlign.START, true, 1, 1,
							null, "批次:");
					iPrinter.drawTextTSPL(10, 10 * 8 * 3, 180, 10 * 8 * 4 - 5, PAlign.CENTER, PAlign.END, true, 1, 1,
							null, "LOT NO.");
					// 打印第三行右边内容
					iPrinter.drawTextTSPL(180, 10 * 8 * 3, 72 * 8 - 10, 10 * 8 * 4, PAlign.CENTER, PAlign.CENTER, true,
							1, 1, null, "XXXXXXXXX");
					// 小框里的第四条横线
					iPrinter.drawLineTSPL(10, 10 * 8 * 5, 72 * 8 - 20, 2);
					// 打印第四行左边的内容
					iPrinter.drawTextTSPL(10, 10 * 8 * 4 + 5, 180, 10 * 8 * 5, PAlign.CENTER, PAlign.START, true, 1, 1,
							null, "序列号:");
					iPrinter.drawTextTSPL(10, 10 * 8 * 4, 180, 10 * 8 * 5 - 5, PAlign.CENTER, PAlign.END, true, 1, 1,
							null, "CASE No.");
					// 打印第四行右边的内容
					iPrinter.drawTextTSPL(180, 10 * 8 * 4, 72 * 8 - 10, 10 * 8 * 5, PAlign.CENTER, PAlign.CENTER, true,
							1, 1, null, "XXXXXXXXX");

					// 小框里的第五条横线
					iPrinter.drawLineTSPL(10, 10 * 8 * 6, 72 * 8 - 20, 2);
					// 打印第五行左边内容
					iPrinter.drawTextTSPL(10, 10 * 8 * 5 + 5, 180, 10 * 8 * 6, PAlign.CENTER, PAlign.START, true, 1, 1,
							null, "时间:");
					iPrinter.drawTextTSPL(10, 10 * 8 * 5, 180, 10 * 8 * 6 - 5, PAlign.CENTER, PAlign.END, true, 1, 1,
							null, "Date:");
					// 打印第五行右边的内容
					iPrinter.drawTextTSPL(180, 10 * 8 * 5, 72 * 8 - 10, 10 * 8 * 6, PAlign.CENTER, PAlign.CENTER, true,
							1, 1, null, PrefUtils.getSystemTime());
					// 小框里的第六条横线
					iPrinter.drawLineTSPL(10, 10 * 8 * 7, 72 * 8 - 20, 2);
					// 打印第六行左边的内容
					iPrinter.drawTextTSPL(10, 10 * 8 * 6 + 5, 180, 10 * 8 * 7, PAlign.CENTER, PAlign.START, true, 1, 1,
							null, "重量:");
					iPrinter.drawTextTSPL(10, 10 * 8 * 6, 180, 10 * 8 * 7 - 5, PAlign.CENTER, PAlign.END, true, 1, 1,
							null, "NET wt.");
					// 打印第六行右边的内容
					iPrinter.drawTextTSPL(180, 10 * 8 * 6, 72 * 8 - 10, 10 * 8 * 7, PAlign.CENTER, PAlign.CENTER, true,
							1, 1, null, "4.00 kg");
					// 打印第七行左边的内容
					iPrinter.drawTextTSPL(10, 10 * 8 * 7 + 5, 180, 10 * 8 * 8, PAlign.CENTER, PAlign.START, true, 1, 1,
							null, "长度:");
					iPrinter.drawTextTSPL(10, 10 * 8 * 7, 180, 10 * 8 * 8 - 5, PAlign.CENTER, PAlign.END, true, 1, 1,
							null, "LENGTH:");
					// 打印第七行右边的内容
					iPrinter.drawTextTSPL(180, 10 * 8 * 7, 72 * 8 - 10, 10 * 8 * 8, PAlign.CENTER, PAlign.CENTER, true,
							1, 1, null, "5000 m");
					// 小框里的竖线
					iPrinter.drawLineTSPL(180, 10 * 8, 2, 10 * 8 * 7);
					// 打印条码
					iPrinter.drawBarCodeTSPL(30, 80 * 8 + 2, PBarcodeType.CODE128, 9 * 8, false, PRotate.Rotate_0, 1, 1,
							"123456BC");
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

		// 备份
		// new Thread() {
		// public void run() {
		// int left = PrefUtils.getInt(mContext, "leftmargin", 0);
		// int top = PrefUtils.getInt(mContext, "topmargin", 0);
		// int numbers = PrefUtils.getInt(mContext, "printnumbers", 1);
		// int isBeep = PrefUtils.getInt(mContext, "isBeep", 0);
		// int isOpenCash = PrefUtils.getInt(mContext, "isOpenCash", 0);
		// try {
		// // 设置标签纸大小
		// iPrinter.pageSetupTSPL(PrinterConstants.SIZE_80mm, 80 * 8, 100 * 8);
		// // 清除缓存区内容
		// iPrinter.printText("CLS\r\n");
		// // 设置标签的参考坐标原点
		// if (left == 0 || top == 0) {
		// // 不做设置，默认
		// } else {
		// iPrinter.sendStrToPrinterTSPL("REFERENCE " + left * 8 + "," + top * 8
		// + "\r\n");
		// }
		// // 最外面的方框(0,0)--(72*8,90*8)
		// iPrinter.drawBorderTSPL(3, 0, 0, 72 * 8, 90 * 8);
		// // 大框和小框之间的标题内容
		// // // 标题中间内容
		// // iPrinter.drawTextTSPL(0, 0, 72 * 8, 10 * 8,
		// // PAlign.CENTER,
		// // PAlign.END, true, 2, 2, null, "ZAX");
		// // // 标题右上方内容
		// // iPrinter.drawTextTSPL(0, 0, 72 * 8, 10 * 8, PAlign.END,
		// // PAlign.CENTER, true, 1, 1, null, "中安信科技有限公司");
		// // // 标调右下方内容
		// // iPrinter.drawTextTSPL(0, 0, 72 * 8, 10 * 8, PAlign.END,
		// // PAlign.END, true, 1, 1, null,
		// // "Zhong An Xin Technology Co.Lsd");
		// // 里面的小框(10,10*8)(72*8-10,80*8)
		// iPrinter.drawBorderTSPL(2, 10, 10 * 8, 72 * 8 - 10, 80 * 8);
		// // 小框里的第一条横线
		// iPrinter.drawLineTSPL(10, 10 * 8 * 2, 72 * 8 - 20, 2);
		// // 打印第一行左边的内容
		// // iPrinter.drawTextTSPL(10, 10 * 8, 180, 10 * 8 * 2,
		// // PAlign.CENTER,
		// // PAlign.START, true, 1, 1, null, "规格:");
		// // iPrinter.drawTextTSPL(10, 10 * 8, 180, 10 * 8 * 2,
		// // PAlign.CENTER,
		// // PAlign.END, true, 1, 1, null, "TYPE:");
		// // 打印第一行右边的内容
		// // iPrinter.drawTextTSPL(180, 10 * 8, 72 * 8 - 10, 10 * 8 *
		// // 2,
		// // PAlign.CENTER, PAlign.CENTER, true, 2, 2, null,
		// // "T700XX");
		// // 小框里的第二条行横线
		// iPrinter.drawLineTSPL(10, 10 * 8 * 3, 72 * 8 - 20, 2);
		// // 打印第二行左边的内容
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 2, 180, 10 * 8 * 3,
		// // PAlign.CENTER, PAlign.START, true, 1, 1, null,
		// // "型号:");
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 2, 180, 10 * 8 * 3,
		// // PAlign.CENTER, PAlign.END, true, 1, 1, null,
		// // "FILAMNTS:");
		// // 打印第二行右边的内容
		// // iPrinter.drawTextTSPL(180, 10 * 8 * 2, 72 * 8 - 10, 10 *
		// // 8 * 3,
		// // PAlign.CENTER, PAlign.CENTER, true, 2, 2,
		// // null, "12K-XXX");
		// // 小框里的第三条横线
		// iPrinter.drawLineTSPL(10, 10 * 8 * 4, 72 * 8 - 20, 2);
		// // 打印第三行左边内容
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 3, 180, 10 * 8 * 4,
		// // PAlign.CENTER, PAlign.START, true, 1, 1, null,
		// // "批次:");
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 3, 180, 10 * 8 * 4,
		// // PAlign.CENTER, PAlign.END, true, 1, 1, null,
		// // "LOT NO.");
		// // 打印第三行右边内容
		// // iPrinter.drawTextTSPL(180, 10 * 8 * 3, 72 * 8 - 10, 10 *
		// // 8 * 4,
		// // PAlign.CENTER, PAlign.CENTER, true, 2, 2,
		// // null, "XXXXXXXXX");
		// // 小框里的第四条横线
		// iPrinter.drawLineTSPL(10, 10 * 8 * 5, 72 * 8 - 20, 2);
		// // 打印第四行左边的内容
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 4, 180, 10 * 8 * 5,
		// // PAlign.CENTER, PAlign.START, true, 1, 1, null,
		// // "序列号:");
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 4, 180, 10 * 8 * 5,
		// // PAlign.CENTER, PAlign.END, true, 1, 1, null,
		// // "CASE No.");
		// // 打印第四行右边的内容
		// // iPrinter.drawTextTSPL(180, 10 * 8 * 4, 72 * 8 - 10, 10 *
		// // 8 * 5,
		// // PAlign.CENTER, PAlign.CENTER, true, 2, 2,
		// // null, "XXXXXXXXX");
		//
		// // 小框里的第五条横线
		// iPrinter.drawLineTSPL(10, 10 * 8 * 6, 72 * 8 - 20, 2);
		// // 打印第五行左边内容
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 5, 180, 10 * 8 * 6,
		// // PAlign.CENTER, PAlign.START, true, 1, 1, null,
		// // "时间:");
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 5, 180, 10 * 8 * 6,
		// // PAlign.CENTER, PAlign.END, true, 1, 1, null,
		// // "Date:");
		// // 打印第五行右边的内容
		// // iPrinter.drawTextTSPL(180, 10 * 8 * 5, 72 * 8 - 10, 10 *
		// // 8 * 6,
		// // PAlign.CENTER, PAlign.CENTER, true, 2, 2,
		// // null, PrefUtils.getSystemTime());
		// // 小框里的第六条横线
		// iPrinter.drawLineTSPL(10, 10 * 8 * 7, 72 * 8 - 20, 2);
		// // 打印第六行左边的内容
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 6, 180, 10 * 8 * 7,
		// // PAlign.CENTER, PAlign.START, true, 1, 1, null,
		// // "重量:");
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 6, 180, 10 * 8 * 7,
		// // PAlign.CENTER, PAlign.END, true, 1, 1, null,
		// // "NET wt.");
		// // 打印第六行右边的内容
		// // iPrinter.drawTextTSPL(180, 10 * 8 * 6, 72 * 8 - 10, 10 *
		// // 8 * 7,
		// // PAlign.CENTER, PAlign.CENTER, true, 2, 2,
		// // null, "4.00 kg");
		// // 打印第七行左边的内容
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 7, 180, 10 * 8 * 8,
		// // PAlign.CENTER, PAlign.START, true, 1, 1, null,
		// // "长度:");
		// // iPrinter.drawTextTSPL(10, 10 * 8 * 7, 180, 10 * 8 * 8,
		// // PAlign.CENTER, PAlign.END, true, 1, 1, null,
		// // "LENGTH:");
		// // 打印第七行右边的内容
		// // iPrinter.drawTextTSPL(180, 10 * 8 * 7, 75 * 8 - 10, 10 *
		// // 8 * 8,
		// // PAlign.CENTER, PAlign.CENTER, true, 2, 2,
		// // null, "5000 m");
		// // 小框里的竖线
		// iPrinter.drawLineTSPL(180, 10 * 8, 2, 10 * 8 * 7);
		// // 打印条码
		// // iPrinter.drawBarCodeTSPL(20, 80 * 8,
		// // PBarcodeType.CODE128, 9 * 8,
		// // true, PRotate.Rotate_0, 1, 1, "123456BC");
		// // // 判断是否响应钱箱
		// // if (isOpenCash == 1) {
		// // // 打印前打开钱箱
		// // iPrinter.openCashBoxTSPL(1, 2);
		// // Thread.sleep(3000);
		// // // 打印
		// // iPrinter.printTSPL(numbers, 1);
		// // } else if (isBeep == 2) {
		// // // 打印
		// // iPrinter.printTSPL(numbers, 1);
		// // // 打印后后打开钱箱
		// // iPrinter.openCashBoxTSPL(1, 2);
		// // } else {
		// // // 打印
		// // iPrinter.printTSPL(numbers, 1);
		// // }
		//
		// // 判断是否响应蜂鸣器
		// if (isBeep == 1) {
		// // 打印前响
		// iPrinter.beepTSPL(1, 1000);
		// Thread.sleep(3000);
		// // 打印
		// iPrinter.printTSPL(numbers, 1);
		// } else if (isBeep == 2) {
		// // 打印
		// iPrinter.printTSPL(numbers, 1);
		// // 打印后响
		// // Thread.sleep(3000);
		// iPrinter.beepTSPL(1, 1000);
		// } else {
		// // 打印
		// iPrinter.printTSPL(numbers, 1);
		// }
		//
		// } catch (WriteException e) {
		// e.printStackTrace();
		// } catch (PrinterPortNullException e) {
		// e.printStackTrace();
		// } catch (ParameterErrorException e) {
		// e.printStackTrace();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// };
		// }.start();
	}

}
