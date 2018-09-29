package com.jgw.printerposdemo.printer.utils;

import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import com.jgw.printerposdemo.R;
import com.printer.sdk.CanvasPrint;
import com.printer.sdk.FontProperty;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterConstants.Command;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PrinterType;
import com.printer.sdk.PrinterInstance;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;

public class CanvasUtils {
	private static String TAG = "com.printer.demo.utils";

	/**
	 * 画布模版一 普通画布，画布的用法
	 * 
	 * @param resources
	 * @param mPrinter
	 * @param isStylus
	 * @param is58mm
	 */
	public void printCustomImage(Resources resources, PrinterInstance mPrinter, boolean isStylus, boolean is58mm) {
		mPrinter.initPrinter();
		mPrinter.setFont(0, 0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.printText(resources.getString(R.string.str_canvas));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
		CanvasPrint cp = new CanvasPrint();
		/**
		 * 初始化画布，画布的宽度为变量，一般有两个选择：1.58mm型号打印机实际可用是48mm，48*8=384px 2.
		 * 80mm型号打印机实际可用是72mm，72*8=576px，因为画布的高度是无限制的，但从内存分配方面考虑要小于4M比较合适。
		 * 所有预置为宽度的5倍。初始化画笔，默认属性有：1.消除锯齿 2.设置画笔颜色为黑色。
		 *
		 * init 方法包含cp.initCanvas(550)和cp.initPaint（），T9打印宽度为72mm，其他为47mm
		 */
		if (isStylus) {
			cp.init(PrinterType.TIII);
		} else {
			if (is58mm) {
				cp.init(PrinterType.TIII);
			} else {
				cp.init(PrinterType.T9);
			}
		}
		// 非中文使用空格分隔单词
		cp.setUseSplit(true);
		// 阿拉伯文靠右显示
		cp.setTextAlignRight(true);
		/*
		 * 插入图片函数：drawImage(float x,float y, String path) 其中（x,y）是指插入图片的左上顶点坐标。
		 */
		FontProperty fp = new FontProperty();
		fp.setFont(false, false, false, false, 25, null);
		// 通过初始化的字体属性设置画笔
		cp.setFontProperty(fp);
		cp.drawText("Show example of context contains English language is showed in right of parent:");
		fp.setFont(false, false, false, false, 30, null);
		cp.setFontProperty(fp);
		cp.setTextAlignRight(false);
		cp.drawText("打印汉字测试，测试打满一行内容后，是否自动换行，且向左对齐展示。");
		cp.drawText("\n\b\n\n");
		cp.drawImage(BitmapFactory.decodeResource(resources, R.drawable.my_picture));
		if (isStylus) {
			// 针打图形，第二个参数为0倍高倍宽，为1只倍高
			mPrinter.printImageStylus(cp.getCanvasImage(), 1);

		} else {
			mPrinter.printImage(cp.getCanvasImage(), PAlign.NONE, 0, false);
		}
	}

	/**
	 * 画布模版二 图片跟文字并行
	 * 
	 * @param resources
	 * @param mPrinter
	 * @param isStylus
	 * @param is58mm
	 */
	public void printCustomImage2(Resources resources, PrinterInstance mPrinter) {
		// 创建画布
		CanvasPrint cp = new CanvasPrint();
		if (PrinterConstants.paperWidth == 576) {
			// 初始化画布，POS88系列用T9参数即可
			cp.init(PrinterType.T9);
			// 调用下面的方法生成二维码
			Bitmap bitmapCODE39 = createBitmapQR_CODE("123456789", 270, 270);
			// 将二维码画到画布上（0,0）处坐标
			cp.drawImage(0, 0, bitmapCODE39);
			/**
			 * 默认字体
			 */
			// 创建字体
			FontProperty fp = new FontProperty();
			// 字体属性赋值 此处参数个数根据SDK版本不同，有略微差别，酌情增减。
			fp.setFont(true, false, false, false, 40, null);
			// 设置字体
			cp.setFontProperty(fp);
			// 将文字画到画布上指定坐标处
			cp.drawText(250, 80, "默认字体");
			/**
			 * 楷体
			 */
			Typeface kaiti = Typeface.createFromAsset(resources.getAssets(), "kaiti.ttf");
			fp.setFont(true, false, false, false, 40, kaiti);
			// 设置字体
			cp.setFontProperty(fp);
			cp.drawText(250, 130, "楷体展示");
			/**
			 * 宋体
			 */
			Typeface songti = Typeface.createFromAsset(resources.getAssets(), "songti.ttf");
			fp.setFont(true, false, false, false, 40, songti);
			// 设置字体
			cp.setFontProperty(fp);
			cp.drawText(250, 180, "宋体展示");
			/**
			 * 黑体
			 */
			Typeface heiti = Typeface.createFromAsset(resources.getAssets(), "heiti.ttf");
			fp.setFont(true, false, false, false, 40, heiti);
			// 设置字体
			cp.setFontProperty(fp);
			cp.drawText(250, 230, "黑体展示");
			cp.drawLine(0, 240, 576, 240);

		} else if (PrinterConstants.paperWidth == 724) {
			// 100mm纸宽
			cp.init(PrinterType.T7);
			// 调用下面的方法生成二维码
			Bitmap bitmapCODE39 = createBitmapQR_CODE("123456789", 300, 300);
			// 将二维码画到画布上（0,0）处坐标
			cp.drawImage(0, 0, bitmapCODE39);
			/**
			 * 默认字体
			 */
			// 创建字体
			FontProperty fp = new FontProperty();
			// 字体属性赋值 此处参数个数根据SDK版本不同，有略微差别，酌情增减。
			fp.setFont(true, false, false, false, 50, null);
			// 设置字体
			cp.setFontProperty(fp);
			// 将文字画到画布上指定坐标处
			cp.drawText(320, 80, "默认字体");
			/**
			 * 楷体
			 */
			Typeface kaiti = Typeface.createFromAsset(resources.getAssets(), "kaiti.ttf");
			fp.setFont(true, false, false, false, 50, kaiti);
			// 设置字体
			cp.setFontProperty(fp);
			cp.drawText(320, 140, "楷体展示");
			/**
			 * 宋体
			 */
			Typeface songti = Typeface.createFromAsset(resources.getAssets(), "songti.ttf");
			fp.setFont(true, false, false, false, 50, songti);
			// 设置字体
			cp.setFontProperty(fp);
			cp.drawText(320, 200, "宋体展示");
			/**
			 * 黑体
			 */
			Typeface heiti = Typeface.createFromAsset(resources.getAssets(), "heiti.ttf");
			fp.setFont(true, false, false, false, 50, heiti);
			// 设置字体
			cp.setFontProperty(fp);
			cp.drawText(320, 260, "黑体展示");
			cp.drawLine(0, 265, 724, 265);

		} else if (PrinterConstants.paperWidth == 384) {
			// 58mm
			cp.init(PrinterType.TIII);
			// 调用下面的方法生成二维码
			Bitmap bitmapCODE39 = createBitmapQR_CODE("123456789", 150, 150);
			// 将二维码画到画布上（0,0）处坐标
			cp.drawImage(0, 0, bitmapCODE39);
			// 创建字体
			FontProperty fp = new FontProperty();
			/**
			 * 楷体
			 */
			Typeface kaiti = Typeface.createFromAsset(resources.getAssets(), "kaiti.ttf");
			fp.setFont(true, false, false, false, 30, kaiti);
			// 设置字体
			cp.setFontProperty(fp);
			cp.drawText(160, 40, "楷体展示");
			/**
			 * 宋体
			 */
			Typeface songti = Typeface.createFromAsset(resources.getAssets(), "songti.ttf");
			fp.setFont(true, false, false, false, 30, songti);
			// 设置字体
			cp.setFontProperty(fp);
			cp.drawText(160, 80, "宋体展示");
			/**
			 * 黑体
			 */
			Typeface heiti = Typeface.createFromAsset(resources.getAssets(), "heiti.ttf");
			fp.setFont(true, false, false, false, 30, heiti);
			// 设置字体
			cp.setFontProperty(fp);
			cp.drawText(160, 120, "黑体展示");
			cp.drawLine(0, 160, 384, 160);
		}

		// 将画布保存成图片并进行打印
		mPrinter.printImage(cp.getCanvasImage(), PAlign.NONE, 0, false);

	}

	/**
	 * 生成二维码图片
	 * 
	 * @param str
	 *            内容
	 * @param param1
	 *            宽度
	 * @param param2
	 *            高度
	 * @return
	 */
	public static Bitmap createBitmap(String str, int param1, int param2, int type) {
		try {
			// BitMatrix matrix = new
			// MultiFormatWriter().encode(str,BarcodeFormat.PDF417, param1,
			// param2);
			// BitMatrix matrix = new
			// MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE, param1,
			// param2);
			// new MultiFormatWriter().en
			Hashtable<EncodeHintType, Object> hint = new Hashtable<EncodeHintType, Object>();
			if (type == 0) {
				hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			} else if (type == 1) {
				hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
			} else if (type == 2) {
				hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
			} else {
				hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			}
			BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, param1, param1, hint);
			int width = param1;
			// int width = matrix.width;
			int height = param2;
			// int height = matrix.height;
			int[] pixels = new int[width * height];
			for (int y = 0; y < width; ++y) {
				for (int x = 0; x < height; ++x) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000; // black pixel
					} else {
						pixels[y * width + x] = 0xffffffff; // white pixel
					}
				}
			}
			Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bmp.setPixels(pixels, 0, width, 0, 0, width, height);
			Log.i(TAG, "createBitmap-width:" + bmp.getWidth());
			Log.i(TAG, "createBitmap-height:" + bmp.getHeight());
			return bmp;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成QR_CODE类型二维码图片
	 * 
	 * @param str
	 *            内容
	 * @param param1
	 *            宽度
	 * @param param2
	 *            高度
	 * @return
	 */
	public static Bitmap createBitmapQR_CODE(String str, int param1, int param2) {
		try {
			BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, param1, param2);
			int width = matrix.width;
			int height = matrix.height;
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; ++y) {
				for (int x = 0; x < width; ++x) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000; // black pixel
					} else {
						pixels[y * width + x] = 0xffffffff; // white pixel
					}
				}
			}
			Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bmp.setPixels(pixels, 0, width, 0, 0, width, height);
			Log.i(TAG, "createBitmapCODE39-width:" + bmp.getWidth());
			Log.i(TAG, "createBitmapCODE39-height:" + bmp.getHeight());
			return bmp;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

}
