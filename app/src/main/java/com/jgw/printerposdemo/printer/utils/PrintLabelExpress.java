package com.jgw.printerposdemo.printer.utils;


import com.jgw.printerposdemo.R;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterConstants.PRotate;
import com.printer.sdk.exception.ParameterErrorException;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.WriteException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PrintLabelExpress {

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
					iPrinter.pageSetupTSPL(PrinterConstants.SIZE_108mm, 800, 1500);
					// 清除缓存区内容
					iPrinter.printText("CLS\r\n");
					// 设置标签的参考坐标原点
					if (left == 0 || top == 0) {
						// 不做设置，默认
					} else {
						iPrinter.sendStrToPrinterTSPL("REFERENCE " + left * 8 + "," + top * 8 + "\r\n");
					}

					// 打印内容
					// 画所有的线条
					// 上框的上边框横线
					iPrinter.drawLineTSPL(40, 40, 720, 1);
					// 上框的左边框的竖线
					iPrinter.drawLineTSPL(40, 40, 1, 840);
					// 上框的右框线竖线
					iPrinter.drawLineTSPL(760, 40, 1, 840);
					// 上框的下边框横线
					iPrinter.drawLineTSPL(40, 880, 720, 1);
					// 下框的上边框横线
					iPrinter.drawLineTSPL(40, 889, 720, 1);
					// 下框的左边框竖线
					iPrinter.drawLineTSPL(40, 889, 1, 511);
					// 下框的右边框竖线
					iPrinter.drawLineTSPL(760, 889, 1, 511);
					// 下框的下边框横线
					iPrinter.drawLineTSPL(40, 1400, 720, 1);
					// 上框里的第一条横线
					iPrinter.drawLineTSPL(40, 136, 720, 1);
					// 上框里的第二条横线（三号便利箱那条短线）
					iPrinter.drawLineTSPL(472, 192, 288, 1);
					// 上框里的第二条横线
					iPrinter.drawLineTSPL(40, 296, 720, 1);
					// 上框里的第三条横线
					iPrinter.drawLineTSPL(40, 400, 720, 1);
					// 上框里的第四条横线
					iPrinter.drawLineTSPL(40, 504, 720, 1);
					// 上框里的第五条横线
					iPrinter.drawLineTSPL(40, 608, 720, 1);
					// 上框里的第六条横线
					iPrinter.drawLineTSPL(40, 792, 720, 1);
					// 上框里的第一条竖线（目的地，收件人。。。。）
					iPrinter.drawLineTSPL(88, 296, 1, 312);
					// 上框里的竖线（托寄物）
					iPrinter.drawLineTSPL(88, 792, 1, 88);
					// 上框里的竖线（收件员）
					iPrinter.drawLineTSPL(336, 792, 1, 88);
					// 上框里的竖线（签名）
					iPrinter.drawLineTSPL(512, 792, 1, 88);
					// 上框里的竖线（三号便利箱）
					iPrinter.drawLineTSPL(472, 136, 1, 160);
					// 下框里的第一条横线
					iPrinter.drawLineTSPL(40, 1016, 720, 1);
					// 下框里的第二条横线
					iPrinter.drawLineTSPL(40, 1104, 720, 1);
					// 下框里的第三条横线
					iPrinter.drawLineTSPL(40, 1192, 720, 1);
					// 下框里的第四条横线
					iPrinter.drawLineTSPL(40, 1232, 720, 1);
					// 下框里的第五条横线
					iPrinter.drawLineTSPL(40, 1272, 720, 1);
					// 下框里的竖线（收件人）
					iPrinter.drawLineTSPL(88, 1016, 1, 176);
					// 下框里的竖线（衣物）
					iPrinter.drawLineTSPL(216, 1192, 1, 80);
					// 下框里的竖线（顺丰快递）
					iPrinter.drawLineTSPL(304, 889, 1, 127);
					// 下框里的竖线（付款方式）
					iPrinter.drawLineTSPL(400, 1192, 1, 80);
					// 下框里的竖线（寄付）
					iPrinter.drawLineTSPL(576, 1192, 1, 80);
					// // 上框 一维条码
					iPrinter.drawBarCodeTSPL(12, 160, PBarcodeType.CODE128, 100, true, PRotate.Rotate_0, 1, 1,
							"001 160 68");
					// 上框 图片
					Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.goodwork);
					iPrinter.drawBitmapTSPL(472, 192, 760, 296, PAlign.CENTER, PAlign.CENTER, 0, bmp);
					// 三号便利箱
					iPrinter.drawTextTSPL(480, 156, true, 1, 1, PRotate.Rotate_0, "三号便利箱/袋");
					// 上框 目的地
					iPrinter.drawTextTSPL(40, 297, 88, 331, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"目");
					iPrinter.drawTextTSPL(40, 331, 88, 365, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"的");
					iPrinter.drawTextTSPL(40, 365, 88, 399, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"地");
					// 上框 7312
					iPrinter.drawTextTSPL(108, 296, 760, 400, PAlign.START, PAlign.CENTER, true, 3, 3, PRotate.Rotate_0,
							"7312");
					// 上框 收件人
					iPrinter.drawTextTSPL(40, 401, 88, 435, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"收");
					iPrinter.drawTextTSPL(40, 435, 88, 469, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"件");
					iPrinter.drawTextTSPL(40, 469, 88, 503, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"人");
					iPrinter.drawTextTSPL(108, 410, true, 1, 1, PRotate.Rotate_0, "熊二 13913913913");
					iPrinter.drawTextTSPL(108, 440, true, 1, 1, PRotate.Rotate_0, "湖南省湘潭市雨湖区学府路与建设路口交汇口666号南山科");
					iPrinter.drawTextTSPL(108, 470, true, 1, 1, PRotate.Rotate_0, "技产业基地B栋88楼湖南省湘潭市雨湖区学府路与建设路口");
					// 上框 寄件人
					iPrinter.drawTextTSPL(40, 505, 88, 539, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"寄");
					iPrinter.drawTextTSPL(40, 539, 88, 573, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"件");
					iPrinter.drawTextTSPL(40, 573, 88, 607, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"人");
					iPrinter.drawTextTSPL(108, 514, true, 1, 1, PRotate.Rotate_0, "熊大  13913913913");
					iPrinter.drawTextTSPL(108, 544, true, 1, 1, PRotate.Rotate_0, "深圳市雨湖区学府路与建设路口交汇口666号南山科技产业");
					iPrinter.drawTextTSPL(108, 574, true, 1, 1, PRotate.Rotate_0, "基地B栋16楼");
					// 上框 付款方式
					iPrinter.drawTextTSPL(44, 608, true, 1, 1, PRotate.Rotate_0, "付款方式：  寄付");
					iPrinter.drawTextTSPL(44, 638, true, 1, 1, PRotate.Rotate_0, "月结帐号：");
					iPrinter.drawTextTSPL(44, 668, true, 1, 1, PRotate.Rotate_0, "第三方地区：");
					iPrinter.drawTextTSPL(44, 698, true, 1, 1, PRotate.Rotate_0, "计费重量: 5kg");
					iPrinter.drawTextTSPL(44, 728, true, 1, 1, PRotate.Rotate_0, "费用合计：42元");
					iPrinter.drawTextTSPL(44, 758, true, 1, 1, PRotate.Rotate_0, "定时派送日期：");
					iPrinter.drawTextTSPL(440, 608, true, 1, 1, PRotate.Rotate_0, "声明价值：2000元");
					iPrinter.drawTextTSPL(440, 638, true, 1, 1, PRotate.Rotate_0, "报价费用：10元");
					iPrinter.drawTextTSPL(440, 668, true, 1, 1, PRotate.Rotate_0, "包装费用：");
					iPrinter.drawTextTSPL(440, 698, true, 1, 1, PRotate.Rotate_0, "运费：22元");
					iPrinter.drawTextTSPL(440, 728, true, 1, 1, PRotate.Rotate_0, "长*宽*高");
					// 上框 托寄物
					iPrinter.drawTextTSPL(40, 792, 88, 821, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"托");
					iPrinter.drawTextTSPL(40, 821, 88, 850, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"寄");
					iPrinter.drawTextTSPL(40, 850, 88, 880, PAlign.CENTER, PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
							"物");
					// 上框 衣物
					iPrinter.drawTextTSPL(108, 792, 760, 880, PAlign.START, PAlign.CENTER, true, 2, 2, PRotate.Rotate_0,
							"衣物");
					// 上框 收件员，寄件日期，派件员
					// iPrinter.drawTextTSPL(338, 792, 512, 821, PAlign.START,
					// PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
					// "收件员：42625");
					// iPrinter.drawTextTSPL(338, 821, 512, 850, PAlign.START,
					// PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
					// "寄件日期：03月");
					// iPrinter.drawTextTSPL(338, 850, 512, 880, PAlign.START,
					// PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
					// "派件员：");
					iPrinter.drawTextTSPL(338, 795, true, 1, 1, PRotate.Rotate_0, "收件员：42625");
					iPrinter.drawTextTSPL(338, 825, true, 1, 1, PRotate.Rotate_0, "寄件日期：03月");
					iPrinter.drawTextTSPL(338, 855, true, 1, 1, PRotate.Rotate_0, "派件员：");
					// 上框 签名：
					iPrinter.drawTextTSPL(515, 795, true, 1, 1, PRotate.Rotate_0, "签名：");
					iPrinter.drawTextTSPL(512, 860, true, 1, 1, PRotate.Rotate_0, "       年   月   日");
					// 下框 图片
					iPrinter.drawBitmapTSPL(40, 889, 304, 1016, PAlign.CENTER, PAlign.CENTER, 0, bmp);
					// 下框 一维条码
					iPrinter.drawBarCodeTSPL(288, 900, PBarcodeType.CODE128, 80, true, PRotate.Rotate_0, 1, 1,
							"001 160 68");
					// 下框 收件人
					iPrinter.drawTextTSPL(40, 1016, 88, 1045, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "收");
					iPrinter.drawTextTSPL(40, 1045, 88, 1074, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "件");
					iPrinter.drawTextTSPL(40, 1074, 88, 1104, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "人");
					iPrinter.drawTextTSPL(108, 1016, true, 1, 1, PRotate.Rotate_0, "熊大  13913913913");
					iPrinter.drawTextTSPL(108, 1045, true, 1, 1, PRotate.Rotate_0, "湖南省湘潭市雨湖区学府路与建设路口交汇口666号南山科");
					iPrinter.drawTextTSPL(108, 1074, true, 1, 1, PRotate.Rotate_0, "技产业基地B栋88楼湖南省湘潭市雨湖区学府路与建设路口");
					// 下框 寄件人
					iPrinter.drawTextTSPL(40, 1104, 88, 1133, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "寄");
					iPrinter.drawTextTSPL(40, 1133, 88, 1162, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "件");
					iPrinter.drawTextTSPL(40, 1162, 88, 1192, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "人");
					iPrinter.drawTextTSPL(108, 1104, true, 1, 1, PRotate.Rotate_0, "熊大  13913913913");
					iPrinter.drawTextTSPL(108, 1148, true, 1, 1, PRotate.Rotate_0, "深圳市雨湖区学府路与建设路口交汇口666号南山科技产业");
					// 下框 表格
					iPrinter.drawTextTSPL(40, 1192, 216, 1232, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "托寄物");
					iPrinter.drawTextTSPL(40, 1232, 216, 1272, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "数量");
					iPrinter.drawTextTSPL(216, 1192, 400, 1232, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "衣物");
					iPrinter.drawTextTSPL(216, 1232, 400, 1272, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "1");
					iPrinter.drawTextTSPL(400, 1192, 576, 1232, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "付款方式");
					iPrinter.drawTextTSPL(400, 1232, 576, 1272, PAlign.CENTER, PAlign.CENTER, true, 1, 1,
							PRotate.Rotate_0, "费用合计");
					iPrinter.drawTextTSPL(650, 1200, true, 1, 1, PRotate.Rotate_0, "寄付");
					iPrinter.drawTextTSPL(650, 1240, true, 1, 1, PRotate.Rotate_0, "42元");
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
