package com.jgw.printerposdemo.printer.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.jgw.printerposdemo.R;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterConstants.Command;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.Table;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class XTUtils {

	private static final String TAG = "XUtils";

	public static byte[] string2bytes(String content) {

		Log.i(TAG, "" + content);
		try {
			content = new String(content.getBytes("gbk"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		char[] charArray = content.toCharArray();
		byte[] tempByte = new byte[512];
		tempByte[0] = 0x34;
		int count = 0;
		for (int i = 0; i < charArray.length; i++) {

			if (charArray[i] == 'x' || charArray[i] == 'X') {

				tempByte[count++] = (byte) (char2Int(charArray[i + 1]) * 16 + char2Int(charArray[i + 2]));
			}

		}
		Log.i(TAG, "---------------");
		byte[] retByte = new byte[count];
		System.arraycopy(tempByte, 0, retByte, 0, count);
		for (int i = 0; i < retByte.length; i++) {
			Log.i(TAG, retByte[i] + "");
		}

		return tempByte;
	}

	private static int char2Int(char data) {
		if (data >= 48 && data <= 57)// 0~9
			data -= 48;
		else if (data >= 65 && data <= 70)// A~F
			data -= 55;
		else if (data >= 97 && data <= 102)// a~f
			data -= 87;
		return Integer.valueOf(data);
	}

	/**
	 * 
	 * @Description: TODO
	 * @param
	 * @return String
	 */
	public static String bytesToHexString(byte[] src, int datalength) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < datalength; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append("0x").append(hv).append(" ");
		}
		stringBuilder.append("0x0a 0x0d");
		return stringBuilder.toString();
	}

	/**
	 * 字符串转换为16进制字符串
	 * 
	 * @param s
	 * @return
	 */

	public static String stringToHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	/**
	 * 
	 * 
	 * 16进制字符串转换为字符串
	 * 
	 * @param s
	 * @return
	 * 
	 */

	public static String hexStringToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 4];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 4, i * 4 + 4).substring(2, 4), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "gbk");
			new String();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	public static void printNote(Resources resources, PrinterInstance mPrinter) {
		mPrinter.initPrinter();
		mPrinter.setFont(0, 0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.printText(resources.getString(R.string.str_note));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
		StringBuffer sb = new StringBuffer();
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setFont(0, 1, 1, 0, 0);
		mPrinter.printText(resources.getString(R.string.shop_company_title) + "\n");
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		// 字号使用默认
		mPrinter.setFont(0, 0, 0, 0, 0);
		sb.append(resources.getString(R.string.shop_num) + "574001\n");
		sb.append(resources.getString(R.string.shop_receipt_num) + "S00003169\n");
		sb.append(resources.getString(R.string.shop_cashier_num) + "s004_s004\n");
		sb.append(resources.getString(R.string.shop_receipt_date) + "2012-06-17\n");
		sb.append(resources.getString(R.string.shop_print_time) + "2012-06-17 13:37:24\n");
		mPrinter.printText(sb.toString()); // 打印

		printTable1(resources, mPrinter); // 打印表格

		sb = new StringBuffer();

		if (PrinterConstants.paperWidth == 384) {
			sb.append(resources.getString(R.string.shop_goods_number) + "                    6.00\n");
			sb.append(resources.getString(R.string.shop_goods_total_price) + "                    35.00\n");
			sb.append(resources.getString(R.string.shop_payment) + "                    100.00\n");
			sb.append(resources.getString(R.string.shop_change) + "                    65.00\n");
		} else if (PrinterConstants.paperWidth == 576) {
			sb.append(resources.getString(R.string.shop_goods_number) + "                                6.00\n");
			sb.append(resources.getString(R.string.shop_goods_total_price) + "                                35.00\n");
			sb.append(resources.getString(R.string.shop_payment) + "                                100.00\n");
			sb.append(resources.getString(R.string.shop_change) + "                                65.00\n");
		} else if (PrinterConstants.paperWidth == 724) {
			sb.append(resources.getString(R.string.shop_goods_number)
					+ "                                            6.00\n");
			sb.append(resources.getString(R.string.shop_goods_total_price)
					+ "                                            35.00\n");
			sb.append(resources.getString(R.string.shop_payment)
					+ "                                            100.00\n");
			sb.append(
					resources.getString(R.string.shop_change) + "                                            65.00\n");
		}

		sb.append(resources.getString(R.string.shop_company_name) + "\n");
		sb.append(resources.getString(R.string.shop_company_site) + "www.jiangsuxxxx.com\n");
		sb.append(resources.getString(R.string.shop_company_address) + "\n");
		sb.append(resources.getString(R.string.shop_company_tel) + "0574-12345678\n");
		sb.append(resources.getString(R.string.shop_Service_Line) + "4008-123-456 \n");

		if (PrinterConstants.paperWidth == 384) {
			sb.append("==============================\n");
		} else if (PrinterConstants.paperWidth == 576) {
			sb.append("==============================================\n");

		} else if (PrinterConstants.paperWidth == 724) {
			sb.append("================================================================\n");
		}

		mPrinter.printText(sb.toString());

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setFont(0, 0, 1, 0, 0);
		mPrinter.printText(resources.getString(R.string.shop_thanks) + "\n");
		mPrinter.printText(resources.getString(R.string.shop_demo) + "\n\n\n");

		mPrinter.setFont(0, 0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

	}

	public static void printTable1(Resources resources, PrinterInstance mPrinter) {

		String column = resources.getString(R.string.note_title);
		Table table = null;
		if (PrinterConstants.paperWidth == 384) {
			table = new Table(column, ";", new int[] { 14, 6, 6, 6 });
		} else if (PrinterConstants.paperWidth == 576) {
			table = new Table(column, ";", new int[] { 18, 10, 10, 12 });
		} else if (PrinterConstants.paperWidth == 724) {
			table = new Table(column, ";", new int[] { 22, 14, 14, 18 });
		}
		table.addRow("" + resources.getString(R.string.bags) + ";10.00;1;10.00");
		table.addRow("" + resources.getString(R.string.hook) + ";5.00;2;10.00");
		table.addRow("" + resources.getString(R.string.umbrella) + ";5.00;3;15.00");
		mPrinter.printTable(table);
	}

	public static void printTest(Resources resources, PrinterInstance mPrinter) {

		mPrinter.initPrinter();

		mPrinter.printText(resources.getString(R.string.str_text));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

		mPrinter.setFont(0, 0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, 0);
		mPrinter.printText(resources.getString(R.string.str_text_left));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);// ��2��

		mPrinter.setPrinter(Command.ALIGN, 1);
		mPrinter.printText(resources.getString(R.string.str_text_center));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);// ��2��

		mPrinter.setPrinter(Command.ALIGN, 2);
		mPrinter.printText(resources.getString(R.string.str_text_right));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3); // ��3��

		mPrinter.setPrinter(Command.ALIGN, 0);
		mPrinter.setFont(0, 0, 0, 1, 0);
		mPrinter.printText(resources.getString(R.string.str_text_strong));
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2); // ��2��

		mPrinter.setFont(0, 0, 0, 0, 1);
		mPrinter.sendBytesData(new byte[] { (byte) 0x1C, (byte) 0x21, (byte) 0x80 });
		mPrinter.printText(resources.getString(R.string.str_text_underline));
		mPrinter.sendBytesData(new byte[] { (byte) 0x1C, (byte) 0x21, (byte) 0x00 });
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2); // ��2��

		mPrinter.setFont(0, 0, 0, 0, 0);
		mPrinter.printText(resources.getString(R.string.str_text_height));
		for (int i = 0; i < 4; i++) {
			mPrinter.setFont(0, i, i, 0, 0);
			mPrinter.printText((i + 1) + resources.getString(R.string.times));

		}
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 1);
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

		for (int i = 0; i < 4; i++) {

			mPrinter.setFont(0, i, i, 0, 0);
			mPrinter.printText(resources.getString(R.string.bigger) + (i + 1) + resources.getString(R.string.bigger1));
			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

		}

		mPrinter.setFont(0, 0, 0, 0, 0);
		mPrinter.setPrinter(Command.ALIGN, 0);
		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
	}

	/**
	 * @param is
	 *            输入流
	 * @return String 返回的字符串
	 * @throws IOException
	 */
	public static String readFromStream(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		is.close();
		String result = baos.toString();
		baos.close();
		return result;
	}

	/**
	 * 将一个字符串中的所有字母均转换为大写
	 * 
	 * @param devicesAddress
	 * @return
	 */
	public static String formatTheString(String devicesAddress) {
		StringBuffer newDevicesAddress = new StringBuffer("");
		for (int i = 0; i < devicesAddress.length(); i++) {
			char chars = devicesAddress.charAt(i);
			String childString = "" + chars;
			childString = childString.toUpperCase();
			newDevicesAddress.append(childString);
		}
		return newDevicesAddress.toString().substring(0, 17);
		// return newDevicesAddress.toString();
	}

	public static boolean isLetter(String str) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[a-zA-Z]+");
		java.util.regex.Matcher m = pattern.matcher(str);
		return m.matches();
	}

	/**
	 * Android获取当前手机IP地址的两种方式
	 */
	// 1

	public static String getIPAddress(Context context) {
		NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {// 当前使用2G/3G/4G网络
				try {
					// Enumeration<NetworkInterface>
					// en=NetworkInterface.getNetworkInterfaces();
					for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
							.hasMoreElements();) {
						NetworkInterface intf = en.nextElement();
						for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
								.hasMoreElements();) {
							InetAddress inetAddress = enumIpAddr.nextElement();
							if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
								return inetAddress.getHostAddress();
							}
						}
					}
				} catch (SocketException e) {
					e.printStackTrace();
				}

			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {// 当前使用无线网络
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());// 得到IPV4地址
				return ipAddress;
			}
		} else {
			// 当前无网络连接,请在设置中打开网络
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(true);
		}
		return null;
	}

	/**
	 * 将得到的int类型的IP转换为String类型
	 *
	 * @param ip
	 * @return
	 */
	public static String intIP2StringIP(int ip) {
		return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
	}

	// 2
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	/**
	 * 
	 * @Description: TODO
	 * @param
	 * @return String
	 */
	public static String bytesToHexString2(byte[] src, int datalength) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < datalength; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		if (!stringBuilder.toString().endsWith("0a0d")) {
			stringBuilder.append("0a0d");
		}
		return stringBuilder.toString();
	}

	/**
	 * 
	 * 
	 * 16进制字符串转换为字符串
	 * 
	 * @param s
	 * @return
	 * 
	 */

	public static String hexStringToString2(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2).substring(0, 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "gbk");
			new String();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	public static byte[] string2bytes2(String content) {

		Log.i(TAG, "" + content);
		try {
			content = new String(content.getBytes("gbk"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		char[] charArray = content.toCharArray();
		byte[] tempByte = new byte[512];
		tempByte[0] = 0x34;
		int count = 0;
		for (int i = 0; i < charArray.length; i = i + 2) {
			tempByte[count++] = (byte) (char2Int(charArray[i]) * 16 + char2Int(charArray[i + 1]));
		}
		Log.i(TAG, "---------------");
		byte[] retByte = new byte[count];
		System.arraycopy(tempByte, 0, retByte, 0, count);
		for (int i = 0; i < retByte.length; i++) {
			Log.i(TAG, retByte[i] + "");
		}
		return tempByte;
	}
}
