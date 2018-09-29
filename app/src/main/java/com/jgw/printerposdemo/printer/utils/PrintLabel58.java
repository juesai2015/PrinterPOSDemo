package com.jgw.printerposdemo.printer.utils;

import java.io.InputStream;


import com.jgw.printerposdemo.R;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterConstants.CommandTSPL;
import com.printer.sdk.PrinterConstants.LableFontSize;
import com.printer.sdk.PrinterConstants.LablePaperType;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterConstants.PRotate;
import com.printer.sdk.PrinterConstants.TwoDarCodeType;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.exception.ParameterErrorException;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.ReadException;
import com.printer.sdk.exception.WriteException;
import com.printer.sdk.utils.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PrintLabel58 {

	private static int MULTIPLE = 5;
	private static final int line_width_border = 2;
	private static final int page_width = 75 * MULTIPLE;// 384����
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

	public void doPrint(final PrinterInstance iPrinter, final Resources resources) {

		new Thread() {
			public void run() {
				iPrinter.pageSetup(LablePaperType.Size_58mm, 384, 540);
				iPrinter.drawText(0, 0, 200, 200, PAlign.START, PAlign.START, "扫一扫二维码连接打印机", LableFontSize.Size_48, 0,
						0, 0, 0, PRotate.Rotate_0);
				;
				iPrinter.drawQrCode(230, 0, "12345678", PRotate.Rotate_0, 6, 1);
				iPrinter.drawBarCode(1, 150, "12345678", PBarcodeType.CODE128, 2, 75, PRotate.Rotate_0);
				Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.ztl);
				iPrinter.drawGraphic(0, 240, Utils.zoomImage(bitmap, 384));
				iPrinter.print(PRotate.Rotate_0, 0);
			}
		}.start();

	}

	public void doPrintTSPL(PrinterInstance iPrinter, Resources resources, Context mContext)
			throws WriteException, PrinterPortNullException, ReadException, ParameterErrorException, Exception {

		// iPrinter.pageSetupTSPL(PrinterConstants.SIZE_108mm, 800, 1500);
		// iPrinter.pageSetupTSPL(PrinterConstants.SIZE_108mm, 816, 1440);
		iPrinter.pageSetupTSPL(PrinterConstants.SIZE_58mm, 56 * 8, 45 * 8);
		// iPrinter.sendStrToPrinterTSPL("SET COUNTER @1 3\r\n");
		// Log.i("yxz", "SET COUNTER @1 3\r\n");
		// iPrinter.sendStrToPrinterTSPL("@1=" + "\"00001\"" + "\r\n");
		// Log.i("yxz", "@1=" + "\"00001\"" + "\r\n");
		// iPrinter.printText("CLS\r\n");
		// iPrinter.sendStrToPrinterTSPL("TEXT 20,20," + "\"TSS24.BF2\"" +
		// "0,1,1,@1\r\n");
		// Log.i("yxz", "TEXT 20,20," + "\"TSS24.BF2\"" + "0,1,1,@1\r\n");

		// 测试页宽页高功能
		iPrinter.printText("CLS\r\n");
		// iPrinter.setPrinterTSPL(CommandTSPL.REPRINT, 1);

		// iPrinter.drawBorderTSPL(2, 10, 10, 700, 800);
		// iPrinter.drawBorderTSPL(2, 60, 60, 400, 400);
		// iPrinter.drawTextTSPL(10, 10, 700, 800, PAlign.CENTER, PAlign.CENTER,
		// true, 1, 1, PRotate.Rotate_0,
		// "我是中国人，我爱新中国");
		// iPrinter.drawTextTSPL(60, 60, 400, 400, PAlign.CENTER, PAlign.CENTER,
		// true, 1, 1, PRotate.Rotate_0,
		// "我是中国人，我爱新中国");
		// iPrinter.setPrinterTSPL(CommandTSPL.SHIFT, 0);
		// iPrinter.setPrinterTSPL(CommandTSPL.HOME, 100);
		// iPrinter.drawLineTSPL(0, 30, 60 * 8, 3);
		// iPrinter.drawLineTSPL(10, 0, 2, 60 * 8);
		// iPrinter.printTSPL(1, 1);
		// 测试画线功能
		// iPrinter.printText("CLS\r\n");
		// // iPrinter.drawLineTSPL(0, 0, 56 * 8, 2);
		// // iPrinter.drawLineTSPL(0, 0, 2, 45 * 8);
		// iPrinter.drawLineTSPL(5 * 8, 5 * 8, 10 * 8, 3);
		// iPrinter.drawLineTSPL(5 * 8, 5 * 8, 60 * 8,4);
		// iPrinter.setBlineTSPL(200, 60*8);
		// iPrinter.printTSPL(1, 1);
		// 测试打印框体功能
		// iPrinter.printText("CLS\r\n");
		// iPrinter.drawBorderTSPL(2, 5 * 8, 5 * 8, 56 * 8, 45 * 8);
		// iPrinter.printTSPL(1, 1);
		// 画圆测试
		// iPrinter.printText("CLS\r\n");
		// iPrinter.drawCircleTSPL(2, 20 * 8, 20 * 8, 20 * 8);
		// iPrinter.printTSPL(1, 1);

		// iPrinter.setPrinterTSPL(CommandTSPL.FORMFEED, 200);
		// iPrinter.setPrinterTSPL(CommandTSPL.PRINTKEY, 0);
		// iPrinter.setPrinterTSPL(CommandTSPL.TEAR, 1);
		// iPrinter.drawTextTSPL(20 * 8, 20 * 8, true, 2, 2, PRotate.Rotate_0,
		// "测试");
		// iPrinter.printSelfTestTSPL();
		// iPrinter.drawCircleTSPL(5, 5 * 8 + 5, 5 * 8 + 5, 5 * 8);
		// iPrinter.drawLineTSPL(0, 0, 1, 50 * 8);
		// iPrinter.selectCountryTSPL(49);
		// iPrinter.setPaperbackOrPaperFeedTSPL(true, 300);
		// iPrinter.setPrinterTSPL(CommandTSPL.OFFSET, 20);

		// iPrinter.setPrinterTSPL(CommandTSPL.SHIFT, 0);
		// iPrinter.drawTextTSPL(100, 100, true, 1, 1, PRotate.Rotate_0,
		// "REVERSE");
		// iPrinter.setPrinterTSPL(CommandTSPL.PEEL, 1);
		// iPrinter.printTSPL(1, 1);
		// iPrinter.printTSPL(1, 1);

		// iPrinter.selectCodePageTSPL(860);
		// iPrinter.setLabelReferenceTSPL(20, 20);
		// Bitmap bmp = BitmapFactory.decodeResource(resources,
		// R.drawable.goodwork);
		// // iPrinter.drawBorderTSPL(3, 60, 60, page_width, page_height);
		// iPrinter.drawBitmapTSPL(5 * 8, 5 * 8, 2, bmp);
		// iPrinter.drawBitmapTSPL(5 * 8, 5 * 8, 56 * 8, 45 * 8, PAlign.START,
		// PAlign.START, 2, bmp);
		// iPrinter.eraseAreaTSPL(10*8, 10*8, 30 * 8, 30 * 8);
		// iPrinter.selectCountryTSPL(61);
		// iPrinter.setGAPTSPL(20, 30);
		// iPrinter.beepTSPL(5, 200);

		// 反白显示测试
		// iPrinter.reverseAreaTSPL(5*8, 5*8, bmp.getWidth(), bmp.getHeight());
		// iPrinter.drawLineTSPL(100*8, 5*8, 1, 45 * 8);
		// iPrinter.drawBorderTSPL(5, 30, 30, 50 * 8, 40* 8);
		// iPrinter.setCharsetNameTSPL("BIG");
		// iPrinter.drawTextTSPL(20 * 8, 20 * 8, false, 1, 1, PRotate.Rotate_0,
		// "中文繁體測試");
		// iPrinter.printText("CLS\r\n");
		// iPrinter.drawTextTSPL(100, 100, true, 1, 1, PRotate.Rotate_0,
		// "REVERSE");
		// iPrinter.reverseAreaTSPL(90, 90, 128, 40);
		// iPrinter.drawTextTSPL(20 * 8, 20 * 8, true, 2, 2, PRotate.Rotate_0,
		// "中文简体测试");
		// iPrinter.setCharsetNameTSPL("big5");
		// iPrinter.drawTextTSPL(10 * 8, 20 * 8, false, 2, 2, PRotate.Rotate_0,
		// "中文繁體測試");
		// iPrinter.drawTextTSPL(20, 20, true, 1, 1, PRotate.Rotate_0, "不放大");
		// iPrinter.drawTextTSPL(20, 50, true, 2, 1, PRotate.Rotate_0,
		// "横向放大一倍");
		// iPrinter.drawTextTSPL(20, 100, true, 1, 2, PRotate.Rotate_0,
		// "纵向放大一倍");
		// iPrinter.drawTextTSPL(20, 200, true, 2, 2, PRotate.Rotate_0,
		// "横向纵向都放大一倍");
		// iPrinter.drawBorderTSPL(2, 5 * 8, 5 * 8, 50 * 8, 40 * 8);
		// iPrinter.drawTextTSPL(5 * 8, 5 * 8, 50 * 8, 40 * 8, PAlign.CENTER,
		// PAlign.CENTER, true, 1, 1, PRotate.Rotate_0,
		// "打印一段文字,这段文字在区域中水平和竖直都居中");
		// iPrinter.drawTextTSPL(5 * 8, 5 * 8, 50 * 8, 40 * 8, PAlign.START,
		// PAlign.START, true, 1, 1, PRotate.Rotate_0,
		// "打印一段文字,这段文字位于区域中水平居左，竖直居上");
		// iPrinter.drawTextTSPL(5 * 8, 5 * 8, 50 * 8, 40 * 8, PAlign.END,
		// PAlign.END, true, 1, 1, PRotate.Rotate_0,
		// "打印一段文字,这段文字位于区域中水平居右，竖直居底");
		// iPrinter.printTSPL(1, 1);

		// iPrinter.drawBarCodeTSPL(10 * 8, 10 * 8, PBarcodeType.CODE39, 10 * 8,
		// true, PRotate.Rotate_0, 2, 2, "1234569");
		// iPrinter.drawBarCodeTSPL(10 * 8, 25 * 8, PBarcodeType.CODE93, 10 * 8,
		// true, PRotate.Rotate_0, 2, 2, "1234569");
		// iPrinter.drawBarCodeTSPL(10 * 8, 40 * 8, PBarcodeType.CODABAR, 10 *
		// 8, true, PRotate.Rotate_0, 2, 2, "1234569");
		// iPrinter.drawBarCodeTSPL(10 * 8, 55 * 8, PBarcodeType.JAN128, 10 * 8,
		// true, PRotate.Rotate_0, 2, 2, "1234569");
		// iPrinter.drawBarCodeTSPL(10 * 8, 25 * 8, PBarcodeType.CODE128, 45 *
		// 8, false, PRotate.Rotate_0, 2, 2, "123");
		// iPrinter.draw2DBarCodeTSPL(20 * 8, 20 * 8, TwoDarCodeType.DMATRIX, 20
		// * 8, 20 * 8, PRotate.Rotate_0,
		// "123456abc");
		// iPrinter.setBlineTSPL(0, 0);
		// iPrinter.printTSPL(1, 1);
		// 文本旋转测试
		// iPrinter.drawTextTSPL(50, 100, true, 1, 1, PRotate.Rotate_0, "不旋转");
		// iPrinter.drawTextTSPL(80, 100, true, 1, 1, PRotate.Rotate_90,
		// "旋转90度");
		// iPrinter.drawTextTSPL(110, 100, true, 1, 1, PRotate.Rotate_180,
		// "旋转180度");
		// iPrinter.drawTextTSPL(150, 100, true, 1, 1, PRotate.Rotate_270,
		// "旋转270度");

		// 区域中打印文字
		// iPrinter.drawBorder(2, 50, 50, 300, 400);
		// iPrinter.drawTextTSPL(50, 50, 300, 400, PAlign.CENTER, PAlign.START,
		// true, 1, 1, null, "竖直居首");
		// iPrinter.drawTextTSPL(50, 50, 300, 400, PAlign.START, PAlign.CENTER,
		// true, 1, 1, null, "居左");
		// iPrinter.drawTextTSPL(50, 50, 300, 400, PAlign.CENTER, PAlign.CENTER,
		// true, 1, 1, null, "居中");
		// iPrinter.drawTextTSPL(50, 50, 300, 400, PAlign.END, PAlign.CENTER,
		// true, 1, 1, null, "居右");
		// iPrinter.drawTextTSPL(50, 50, 300, 400, PAlign.CENTER, PAlign.END,
		// true, 1, 1, null, "竖直底部");

		// iPrinter.drawTextTSPL(30, 30, true, 1, 1, null, "打印文字效果演示：");
		// // // 文本倍高倍宽测试
		// iPrinter.drawTextTSPL(50, 80, true, 1, 1, null, "测试");
		// iPrinter.drawTextTSPL(120, 80, true, 2, 1, null, "测试");
		// iPrinter.drawTextTSPL(250, 80, true, 1, 2, null, "测试");
		// iPrinter.drawTextTSPL(320, 80, true, 2, 2, null, "测试 ");
		// // 文本是不是中文简体测试
		// iPrinter.drawTextTSPL(50, 150, true, 1, 1, null, "测试一段文本是不是简体中文");
		// iPrinter.setCharsetName("BIG5");
		// iPrinter.drawTextTSPL(50, 180, false, 1, 1, null, "繁體輸出");

		// 区域内打字测试

		// iPrinter.drawBorder(2, 50, 50, 300, 600);
		// iPrinter.drawTextTSPL(50, 50, 300, 600, PAlign.START, PAlign.START,
		// true, 1, 1, null, "区域中打印文字测试，测试一下自动折行是否正确");
		// iPrinter.drawTextTSPL(50, 50, 300, 600, PAlign.CENTER, PAlign.START,
		// true, 1, 1, null, "竖直居首");
		// iPrinter.drawTextTSPL(50, 50, 300, 600, PAlign.START, PAlign.CENTER,
		// true, 1, 1, null, "居左");
		// iPrinter.drawTextTSPL(50, 50, 300, 600, PAlign.CENTER, PAlign.CENTER,
		// true, 1, 1, null, "居中");
		// iPrinter.drawTextTSPL(50, 50, 300, 600, PAlign.END, PAlign.CENTER,
		// true, 1, 1, null, "居右");
		// iPrinter.drawTextTSPL(50, 50, 300, 600, PAlign.CENTER, PAlign.END,
		// true, 1, 1, null, "竖直底部");
		// 画线测试
		// iPrinter.drawTextTSPL(30, 30, true, 1, 1, null, "打印线条效果演示：");
		// // 横线
		// iPrinter.drawLineTSPL(30, 70, 300, 1);
		// iPrinter.drawLineTSPL(30, 90, 300, 2);
		// iPrinter.drawLineTSPL(30, 110, 300, 3);
		// iPrinter.drawLineTSPL(30, 130, 300, 4);
		// iPrinter.drawLineTSPL(30, 150, 300, 5);
		// // 竖线
		// iPrinter.drawLineTSPL(30, 160, 1, 200);
		// iPrinter.drawLineTSPL(50, 160, 2, 200);
		// iPrinter.drawLineTSPL(70, 160, 3, 200);
		// iPrinter.drawLineTSPL(90, 160, 4, 200);
		// iPrinter.drawLineTSPL(110, 160, 5, 200);

		// // 画框测试
		// iPrinter.drawTextTSPL(30, 30, true, 1, 1, null, "打印框体效果演示：");
		// iPrinter.drawBorderTSPL(1, 50, 50, 300, 300);
		// iPrinter.drawBorderTSPL(2, 70, 70, 280, 280);
		// iPrinter.drawBorderTSPL(3, 90, 90, 260, 260);
		// iPrinter.drawBorderTSPL(4, 110, 110, 240, 240);
		// iPrinter.drawBorderTSPL(5, 130, 130, 220, 220);
		// // TODO
		// // 画圆测试
		// iPrinter.drawTextTSPL(30, 30, true, 1, 1, null, "打印圆形效果演示：");
		//
		// iPrinter.drawCircleTSPL(4, 200, 200, 200);
		// iPrinter.drawCircleTSPL(4, 100, 100, 200);
		//
		// // 画一维条码测试
		// iPrinter.drawTextTSPL(30, 30, true, 1, 1, null, "打印一维条码效果演示：");
		//
		// // iPrinter.drawBarCodeTSPL(50, 100, PBarcodeType.CODE128, 90, false,
		// // null, 1, 2, "123456789");
		// iPrinter.drawBarCodeTSPL(50, 150, PBarcodeType.CODE128, 90, true,
		// null, 1, 2, "987654321");
		// // 画二维条码测试
		// iPrinter.drawTextTSPL(10, 10, true, 1, 1, null, "打印二维条码不旋转效果演示：");
		// iPrinter.draw2DBarCodeTSPL(100, 100, TwoDarCodeType.DMATRIX, 500,
		// 500, PRotate.Rotate_0, "我的二维码");
		// iPrinter.draw2DBarCodeTSPL(100, 100, TwoDarCodeType.PDF417, 300, 100,
		// PRotate.Rotate_0, "我的二维码");
		// iPrinter.draw2DBarCodeTSPL(400, 400, TwoDarCodeType.PDF417, 300, 100
		// , PRotate.Rotate_90, "我的二维码");
		// iPrinter.draw2DBarCodeTSPL(400, 400, TwoDarCodeType.PDF417, 300, 100
		// , PRotate.Rotate_180, "我的二维码");
		// iPrinter.draw2DBarCodeTSPL(400, 400, TwoDarCodeType.PDF417, 300, 100
		// , PRotate.Rotate_270, "我的二维码");
		// iPrinter.draw2DBarCodeTSPL(40, 50, TwoDarCodeType.QR, 2, 5, null,
		// "我的二维码");
		// iPrinter.draw2DBarCodeTSPL(200, 200, TwoDarCodeType.DMATRIX, 100,
		// 100, null, "我的二维码");
		// iPrinter.draw2DBarCodeTSPL(200, 300, TwoDarCodeType.PDF417, 100, 100,
		// null, "我的二维码");
		// // 打印图片和区域内打印图片测试
		iPrinter.drawTextTSPL(30, 30, true, 1, 1, null, "区域内打印图片效果演示：");
		Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.goodwork);
		// // iPrinter.drawBitmapTSPL(5 * 8, 5 * 8, 1, bmp);
		iPrinter.drawBorderTSPL(3, 50, 50, 400, 350);
		// iPrinter.drawBitmapTSPL(50, 50, 400, 350, PAlign.START, PAlign.START,
		// 0, bmp);
		iPrinter.drawBitmapTSPL(50, 50, 400, 350, PAlign.CENTER, PAlign.CENTER, 0, bmp);
		// iPrinter.drawBitmapTSPL(50, 50, 400, 350, PAlign.END, PAlign.CENTER,
		// 0, bmp);
		// iPrinter.drawBitmapTSPL(50, 50, 400, 350, PAlign.END, PAlign.END, 0,
		// bmp);
		// iPrinter.drawBitmapTSPL(60, 60, page_width, page_height,
		// PAlign.CENTER, PAlign.CENTER, 0, bmp);

		// iPrinter.drawBitmapTSPL(60, 60, 0, bmp);
		// 反白显示测试
		// iPrinter.reverseAreaTSPL(60, 60, bmp.getWidth(), bmp.getHeight());
		// // // 清除影像区部分显示
		// iPrinter.eraseAreaTSPL(50, 50, bmp.getWidth(), bmp.getHeight());
		// 控制打印机进纸或者退纸功能测试
		// iPrinter.setPaperbackOrPaperFeedTSPL(false, 100);

		// 设置打印机的一系列指令测试
		// iPrinter.drawTextTSPL(30, 30, true, 1, 1, null, "测试");
		// Bitmap bmp = BitmapFactory.decodeResource(resources,
		// R.drawable.goodwork);
		// iPrinter.drawBitmapTSPL(50, 50, 0, bmp);
		// 设置打印机打印速度
		// iPrinter.setPrinterTSPL(CommandTSPL.SPEED, 1);
		// iPrinter.setPrinterTSPL(CommandTSPL.SPEED, 10);
		// 走纸到撕纸位置
		// iPrinter.setPrinterTSPL(CommandTSPL.TEAR, 0);
		// iPrinter.setPrinterTSPL(CommandTSPL.TEAR, 1);
		// 打印完一单后的额外走纸距离
		// iPrinter.setPrinterTSPL(CommandTSPL.OFFSET, 0);
		// iPrinter.setPrinterTSPL(CommandTSPL.OFFSET, 20);
		// 设置黑标高度和打印完额外走纸距离
		// iPrinter.setBlineTSPL(10, 20);
		// 偏移量 value点
		// iPrinter.setPrinterTSPL(CommandTSPL.SHIFT, 30);
		// iPrinter.setPrinterTSPL(CommandTSPL.SHIFT, -30);
		// 设置打印机打印自检页
		// iPrinter.sendStrToPrinter("SELFTEST\r\n");
		// GAP标签纸间的垂直距离
		// iPrinter.setGAPTSPL(20, 0);
		// iPrinter.setPrinterTSPL(CommandTSPL.HOME, 0);
		// 错误重打
		// iPrinter.setPrinterTSPL(CommandTSPL.REPRINT, 1);
		// 按键打印
		// iPrinter.setPrinterTSPL(CommandTSPL.PRINTKEY, 1);
		// iPrinter.setPrinterTSPL(CommandTSPL.KEY2, 0);
		// iPrinter.setPrinterTSPL(CommandTSPL.PRINTKEY, 0);
		// iPrinter.setPrinterTSPL(CommandTSPL.KEY1, 1);
		// 打开为按键暂停，关闭后按键无效
		// iPrinter.setPrinterTSPL(CommandTSPL.KEY1, 1);
		// 控制蜂鸣器
		// iPrinter.beepTSPL(1, 100);
		// iPrinter.beepTSPL(5, 200);
		// iPrinter.beepTSPL(10, 300);
		// iPrinter.beepTSPL(15, 400);
		// iPrinter.beepTSPL(19, 500);

		// 将位图下载到打印机，打印下载好的位图
		// Bitmap bmp = BitmapFactory.decodeResource(resources,
		// R.drawable.goodwork);
		// iPrinter.downloadBitmap2PrinterTSPL(true, bmp, "goodwork", mContext);
		// // try {
		// // Thread.sleep(50);
		// // } catch (InterruptedException e) {
		// // e.printStackTrace();
		// // }
		// iPrinter.putBitmapTSPL("goodwork", 30, 30);
		// iPrinter.sendStrToPrinterTSPL("PRINT" + 1 + "," + 1 + "\r\n");

		// // 选择国际代码页
		// iPrinter.selectCodePageTSPL(1250);
		// // 选择国际字符集
		// iPrinter.selectcountryTSPL(61);
		// iPrinter.setPrinterTSPL(CommandTSPL.OFFSET, 0);
		// iPrinter.drawBarCodeTSPL(40, 40, PBarcodeType.JAN3_EAN13, 60, true,
		// PRotate.Rotate_0, 1, 1, "12345678");
		// iPrinter.drawBarCodeTSPL(40, 130, PBarcodeType.JAN8_EAN8, 60, true,
		// PRotate.Rotate_0, 1, 1, "12345678");
		// iPrinter.drawBarCodeTSPL(10, 10, PBarcodeType.CODE128, 60, true,
		// PRotate.Rotate_0, 1, 1, "12345678");
		// iPrinter.drawBarCodeTSPL(10, 10, PBarcodeType.CODE128, 60, true,
		// PRotate.Rotate_0, 1, 1, "12345678");
		// iPrinter.drawLineTSPL(20, 20, 400, 1);
		// iPrinter.setBlineTSPL(40, 96);
		// iPrinter.setGAPTSPL(20, 20);
		// iPrinter.setPrinter(CommandTSPL.DENSITY, 5);
		// iPrinter.setPrinter(CommandTSPL.DENSITY, 15);
		// iPrinter.setPrinterTSPL(CommandTSPL.DENSITY, 6);
		// 查询打印机当前打印状态测试
		// 将位图下载到打印机，打印下载好的位图
		// InputStream in =
		// mContext.getResources().openRawResource(R.raw.goodwork1);
		// iPrinter.downloadBitmap2PrinterTSPL(true, in, "cccc", mContext);
		// try {
		// Thread.sleep(50);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// iPrinter.putBitmapTSPL("cccc", 30, 30);
		// iPrinter.printTSPL(1, 1);
		// iPrinter.setPrinterTSPL(CommandTSPL.PEEL, 0);
		// iPrinter.drawTextTSPL(0, 0, true, 2, 2, PRotate.Rotate_0, "测试");
		// iPrinter.drawLineTSPL(0, 0, 56 * 8, 2);
		// iPrinter.drawBorderTSPL(1, 0, 0, 800, 1500);
		// iPrinter.drawBarCodeTSPL(20, 20, PBarcodeType.JAN3_EAN13, 30, true,
		// PRotate.Rotate_0, 1, 1, "123456");

		/**
		 * 一维条码
		 */
		// iPrinter.drawTextTSPL(20, 20, true, 1, 1, PRotate.Rotate_0,
		// "打印CODE128码展示：");
		// iPrinter.drawBarCodeTSPL(40, 50, PBarcodeType.CODE128, 100, false,
		// PRotate.Rotate_0, 1, 1, "12345");
		// iPrinter.drawTextTSPL(20, 150, true, 1, 1, PRotate.Rotate_0,
		// "打印CODE93码展示：");
		// iPrinter.drawBarCodeTSPL(20, 180, PBarcodeType.CODE93, 50, false,
		// PRotate.Rotate_0, 2, 6, "12345");
		// iPrinter.drawBarCodeTSPL(20, 300, PBarcodeType.CODE93, 50, false,
		// PRotate.Rotate_0, 2, 6, "12345");
		// iPrinter.drawTextTSPL(20, 30, true, 1, 1, PRotate.Rotate_0,
		// "打印CODE39码展示：");
		// iPrinter.drawBarCodeTSPL(40, 300, PBarcodeType.CODE39, 50, true,
		// PRotate.Rotate_0, 1, 1, "12345");
		// iPrinter.printTSPL(1, 1);
		// 对数据格式有要求，该数据可以，“123”不行
		// iPrinter.drawTextTSPL(20, 30, true, 1, 1, PRotate.Rotate_0,
		// "打印UPCA码展示：");
		// iPrinter.drawBarCodeTSPL(20, 150, PBarcodeType.UPC_A, 50, true,
		// PRotate.Rotate_0, 1, 1, "00000012345");
		// 对数据格式有要求，暂未测试
		// iPrinter.drawTextTSPL(20, 30, true, 1, 1, PRotate.Rotate_0,
		// "打印UPCE码展示：");
		// iPrinter.drawBarCodeTSPL(20, 150, PBarcodeType.UPC_E, 50, true,
		// PRotate.Rotate_0, 1, 1, "012345");
		// iPrinter.drawTextTSPL(20, 30, true, 1, 1, PRotate.Rotate_0,
		// "打印CODABAR码展示：");
		// 对数据格式有要求，该数据可以，“A12345A”不行
		// iPrinter.drawBarCodeTSPL(20, 50, PBarcodeType.CODABAR, 50, false,
		// PRotate.Rotate_0, 1, 1, "123");
		// 对数据格式有要求，该数据可以，“12345”不行
		// iPrinter.drawTextTSPL(20, 150, true, 1, 1, PRotate.Rotate_0,
		// "打印EAN13码展示：");
		// iPrinter.drawBarCodeTSPL(20, 180, PBarcodeType.JAN3_EAN13, 50, false,
		// PRotate.Rotate_0, 1, 1, "000012345678");
		// iPrinter.drawTextTSPL(20, 30, true, 1, 1, PRotate.Rotate_0,
		// "打印EAN8码展示：");
		// 对数据格式有要求，该数据可以，“123”不行
		// iPrinter.drawBarCodeTSPL(20, 50, PBarcodeType.JAN8_EAN8, 50, false,
		// PRotate.Rotate_0, 1, 1, "1234567");

		/**
		 * 二维条码
		 */
		// iPrinter.drawTextTSPL(10, 10, true, 1, 1, PRotate.Rotate_0,
		// "打印QRCode，旋转90度演示：");
		// iPrinter.draw2DBarCodeTSPL(50, 50, TwoDarCodeType.QR, 1, 6,
		// PRotate.Rotate_0, "我是中国人");
		// iPrinter.drawTextTSPL(10, 10, true, 1, 1, PRotate.Rotate_0,
		// "打印DATMATIX：");
		// iPrinter.draw2DBarCodeTSPL(50, 50, TwoDarCodeType.DMATRIX, 1, 3,
		// PRotate.Rotate_0, "123456abc");
		// iPrinter.draw2DBarCodeTSPL(100, 100, TwoDarCodeType.DMATRIX, 1, 6,
		// PRotate.Rotate_0, "我是中国人");
		iPrinter.printTSPL(1, 1);
		// iPrinter.setPaperbackOrPaperFeedTSPL(true, 50);

		// return iPrinter.getPrinterNameTSPL();

		// iPrinter.setPrinterTSPL(CommandTSPL.KEY2,1);
		// iPrinter.setPrinterTSPL(CommandTSPL.KEY1, 1);

		// return iPrinter.getPrinterStatusTSPL();
	}

}
