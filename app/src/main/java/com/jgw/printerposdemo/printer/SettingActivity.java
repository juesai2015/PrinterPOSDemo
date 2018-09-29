package com.jgw.printerposdemo.printer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jgw.printerposdemo.R;
import com.jgw.printerposdemo.printer.global.GlobalContants;
import com.jgw.printerposdemo.printer.utils.PrefUtils;
import com.jgw.printerposdemo.printer.utils.XTUtils;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterConstants.Connect;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.usb.USBPort;
import com.printer.sdk.utils.XLog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SettingActivity extends BaseActivity
        implements OnClickListener, OnItemSelectedListener, OnCheckedChangeListener {
    private LinearLayout header;
    private TextView tvShowPrinterType;
    private TextView tvShowPrinterPortType;
    private Spinner spinner_printer_type;
    private Spinner spinner_interface_type;
    private List<String> data_list;
    private ArrayAdapter<CharSequence> arr_adapter;
    private ArrayAdapter<CharSequence> printType_adapter;
    private final static int SCANNIN_GREQUEST_CODE = 2;
    public static final int CONNECT_DEVICE = 1;
    public static final int ENABLE_BT = 3;
    // 不同蓝牙链接方式的判断依据 “确认连接”
    public static int connectMains = 0;
    protected static final String TAG = "SettingActivity";
    private static Button btn_search_devices, btn_scan_and_connect, btn_selfprint_test, btn_status_test;
    public static boolean isConnected = false;// 蓝牙连接状态
    public static String devicesName = "未知设备";
    private static String devicesAddress;
    private ProgressDialog dialog;
    public static PrinterInstance myPrinter;
    private static BluetoothDevice mDevice;
    private static UsbDevice mUSBDevice;
    private Context mContext;
    private int printerId = 0;
    private int interfaceType = 0;
    private List<UsbDevice> deviceList;
    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";
    private RadioGroup rg__select_paper_size;
    boolean isError;
    private BluetoothAdapter mBtAdapter;
    private TextView tv_device_name, tv_printer_address;
    private IntentFilter bluDisconnectFilter;
    private static boolean hasRegDisconnectReceiver = false;
    private ProgressDialog dialogH;
    private static final String ACTION_UPDATE_CHARACTERS_CANCLE = "canclUpdateCharacters";
    private IntentFilter cancleUpdateCharactersFilter;
    private String strStatus = null;

    /**
     * 设置TSPL指令打印机
     *
     * @param mPrinter
     * @return 是否设置成功
     */

    /**
     * 显示扫描结果
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        XLog.i(TAG, "yxz at SetteingActivity.java onCreate() SettingActivity.this:" + SettingActivity.this);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.e(TAG, "yxz at SetteingActivity.java onCreate()");

    }

    // 用于接受连接状态消息的 Handler
    private Handler mHandler = new Handler() {
        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Connect.SUCCESS:
                    isConnected = true;
                    GlobalContants.ISCONNECTED = isConnected;
                    GlobalContants.DEVICENAME = devicesName;
                    if (interfaceType == 0) {
                        PrefUtils.setString(mContext, GlobalContants.DEVICEADDRESS, devicesAddress);
                        bluDisconnectFilter = new IntentFilter();
                        bluDisconnectFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                        mContext.registerReceiver(myReceiver, bluDisconnectFilter);
                        hasRegDisconnectReceiver = true;
                    }
                    // TOTO 暂时将TSPL指令设置参数的设置放在这
                    // if (setPrinterTSPL(myPrinter)) {
                    // if (interfaceType == 0) {
                    // Toast.makeText(mContext,
                    // R.string.settingactivitty_toast_bluetooth_set_tspl_successful,
                    // 0)
                    // .show();
                    // } else if (interfaceType == 1) {
                    // Toast.makeText(mContext,
                    // R.string.settingactivity_toast_usb_set_tspl_succefful,
                    // 0).show();
                    // }
                    // }
                    break;
                case Connect.FAILED:
                    isConnected = false;

                    Toast.makeText(mContext, R.string.conn_failed, Toast.LENGTH_SHORT).show();
                    XLog.i(TAG, "ZL at SettingActivity Handler() 连接失败!");
                    break;
                case Connect.CLOSED:
                    isConnected = false;
                    GlobalContants.ISCONNECTED = isConnected;
                    GlobalContants.DEVICENAME = devicesName;
                    Toast.makeText(mContext, R.string.conn_closed, Toast.LENGTH_SHORT).show();
                    XLog.i(TAG, "ZL at SettingActivity Handler() 连接关闭!");
                    break;
                case Connect.NODEVICE:
                    isConnected = false;
                    Toast.makeText(mContext, R.string.conn_no, Toast.LENGTH_SHORT).show();
                    break;
                // case 10:
                // if (setPrinterTSPL(myPrinter)) {
                // Toast.makeText(mContext, "蓝牙连接设置TSPL指令成功", 0).show();
                // }
                default:
                    break;
            }

            updateButtonState(isConnected);

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    };
    int count = 0;

    @SuppressWarnings("static-access")
    public void vibrator() {
        count++;
        PrefUtils.setInt(mContext, "count3", count);
        Log.e(TAG, "" + count);
        // Vibrator vib = (Vibrator) SettingActivity.this
        // .getSystemService(Service.VIBRATOR_SERVICE);
        // vib.vibrate(1000);
        // try {
        // Thread.sleep(500);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        MediaPlayer player = new MediaPlayer().create(mContext, R.raw.test);
        // MediaPlayer player2 = new MediaPlayer().create(mContext, R.raw.beep);

        player.start();
        // player2.start();
    }

    /**
     * 初始化界面
     */
    private void init() {
        mContext = SettingActivity.this;
        header = (LinearLayout) findViewById(R.id.ll_headerview_settingactivity);
        // 初始化标题
        initHeader(header);
        // 初始化下拉列表框
        // spinner_printer_type = (Spinner)
        // findViewById(R.id.spinner_printer_type);
        spinner_interface_type = (Spinner) findViewById(R.id.spinner_interface_type);
        // 初始化按钮的点击事件
        btn_search_devices = (Button) findViewById(R.id.btn_search_devices);
        btn_scan_and_connect = (Button) findViewById(R.id.btn_scan_and_connect);
        btn_selfprint_test = (Button) findViewById(R.id.btn_selfprint_test);
        btn_status_test = (Button) findViewById(R.id.btn_status_test);
        // 设置按钮的监听事件
        btn_search_devices.setOnClickListener(this);
        btn_scan_and_connect.setOnClickListener(this);
        btn_selfprint_test.setOnClickListener(this);
        btn_status_test.setOnClickListener(this);

        // 展示设备名和设备地址
        tv_device_name = (TextView) findViewById(R.id.tv_device_name);
        tv_printer_address = (TextView) findViewById(R.id.tv_printer_address);
        // // 适配器
        // arr_adapter = ArrayAdapter.createFromResource(this,
        // R.array.printertype, android.R.layout.simple_spinner_item);
        printType_adapter = ArrayAdapter.createFromResource(this, R.array.interface_type,
                android.R.layout.simple_spinner_item);

        // // 设置样式
        // arr_adapter
        // .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        printType_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 加载适配器
        // spinner_printer_type.setAdapter(arr_adapter);
        spinner_interface_type.setAdapter(printType_adapter);
        // 下拉列表框的监听事件
        // spinner_printer_type.setOnItemSelectedListener(this);
        spinner_interface_type.setOnItemSelectedListener(this);

        rg__select_paper_size = (RadioGroup) findViewById(R.id.rg__select_paper_size);
        rg__select_paper_size.setOnCheckedChangeListener(this);

        // 初始化对话框
        dialog = new ProgressDialog(mContext);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.connecting));
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        getSaveState();
        updateButtonState(isConnected);
        // 初始化进度条对话框
        dialogH = new ProgressDialog(this);
        dialogH.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogH.setCancelable(false);
        dialogH.setCanceledOnTouchOutside(false);

        PrinterConstants.paperWidth = PrefUtils.getInt(mContext, GlobalContants.PAPERWIDTH, 576);
        switch (PrinterConstants.paperWidth) {
            case 384:
                rg__select_paper_size.check(R.id.rb_58mm);
                break;
            case 576:
                rg__select_paper_size.check(R.id.rb_80mm);
                break;
            case 724:
                rg__select_paper_size.check(R.id.rb_100mm);
                break;
            default:
                rg__select_paper_size.check(R.id.rb_80mm);
                break;
        }

    }

    private void getSaveState() {
        XLog.d(TAG, "yxz at SettingActivity.java getSaveState() ---begin");
        isConnected = PrefUtils.getBoolean(SettingActivity.this, GlobalContants.CONNECTSTATE, false);
        printerId = PrefUtils.getInt(mContext, GlobalContants.PRINTERID, 0);
        interfaceType = PrefUtils.getInt(mContext, GlobalContants.INTERFACETYPE, 0);
        // spinner_printer_type.setSelection(printerId);
        spinner_interface_type.setSelection(interfaceType);
        XLog.i(TAG, "zl at MainActivity.java isConnected:" + isConnected);
        XLog.d(TAG, "yxz at SettingActivity.java getSaveState() ---end");

    }

    /**
     * 初始化标题上的信息
     */
    private void initHeader(LinearLayout header) {
        setHeaderLeftText(header, getString(R.string.back), new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        headerConnecedState.setText(getTitleState());
        setHeaderLeftImage(header, new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setHeaderCenterText(header, getString(R.string.headview_setting));

    }

    @Override
    protected void onStart() {
        XLog.e(TAG, "yxz at SettingActivity.java onStart()   progressdialog");
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        XLog.e(TAG, "yxz at SettingActivity.java onResume()   progressdialog");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        XLog.e(TAG, "yxz at SettingActivity.java onRestart()   progressdialog");
    }

    @Override
    protected void onPause() {
        super.onPause();
        XLog.e(TAG, "yxz at SettingActivity.java onPause()   progressdialog");
    }

    @Override
    protected void onStop() {
        super.onStop();
        XLog.e(TAG, "yxz at SettingActivity.java onStop()   progressdialog");

    }

    @Override
    protected void onDestroy() {
        XLog.e(TAG, "yxz at SettingActivity.java onDestroy()   progressdialog");
        super.onDestroy();
        if (interfaceType == 0 && hasRegDisconnectReceiver) {
            mContext.unregisterReceiver(myReceiver);
            hasRegDisconnectReceiver = false;
            // Log.i(TAG, "关闭了广播！");
        }

    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {

        if (v == btn_search_devices) {
            XLog.i(TAG, "YXZ at SettingActivity onClick() isConnected:" + isConnected);
            if (!isConnected) {
                switch (interfaceType) {
                    case 0:// kuetooth
                        new AlertDialog.Builder(this).setTitle(R.string.str_message).setMessage(R.string.str_connlast)
                                .setPositiveButton(R.string.yesconn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        connectMains = 0;
                                        // 重新连接
                                        if (!(mBtAdapter == null)) {
                                            // 判断设备蓝牙功能是否打开
                                            if (!mBtAdapter.isEnabled()) {
                                                // 打开蓝牙功能
                                                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                                startActivityForResult(enableIntent, ENABLE_BT);
                                            } else {
                                                // mDevice
                                                devicesAddress = PrefUtils.getString(mContext, GlobalContants.DEVICEADDRESS,
                                                        "");

                                                if (devicesAddress == null || devicesAddress.length() <= 0) {
                                                    Toast.makeText(SettingActivity.this, "您是第一次启动程序，请选择重新搜索连接！",
                                                            Toast.LENGTH_SHORT).show();
                                                } else {
                                                    connect2BlueToothdevice();

                                                }
                                            }
                                        }

                                    }
                                }).setNegativeButton(R.string.str_resel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                connectMains = 1;
                                if (!(mBtAdapter == null)) {
                                    // 判断设备蓝牙功能是否打开
                                    if (!mBtAdapter.isEnabled()) {
                                        // 打开蓝牙功能
                                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                        startActivityForResult(enableIntent, SettingActivity.ENABLE_BT);
                                    } else {
                                        Intent intent = new Intent(mContext, BluetoothDeviceList.class);
                                        startActivityForResult(intent, CONNECT_DEVICE);

                                    }

                                }

                            }

                        }).show();

                        break;
                    case 1:// USB

                        new AlertDialog.Builder(this).setTitle(R.string.str_message).setMessage(R.string.str_connlast)
                                .setPositiveButton(R.string.yesconn, new DialogInterface.OnClickListener() {
                                    @SuppressLint("InlinedApi")
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
                                        usbAutoConn(manager);
                                    }
                                }).setNegativeButton(R.string.str_resel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                XLog.i(TAG, "yxz at SettingActivity.java "
                                        + "DialogInterface.OnClickListener() isConnected:" + isConnected);

                                Intent intent = new Intent(mContext, UsbDeviceList.class);
                                startActivityForResult(intent, CONNECT_DEVICE);

                            }

                        }).show();
                        break;
                    case 2:// wifi
                        Intent intent = new Intent(mContext, IpEditActivity.class);
                        intent.putExtra(GlobalContants.WIFINAME, getWiFiName());
                        startActivityForResult(intent, CONNECT_DEVICE);
                        break;
                    case 3:// serial port
                        Intent intentSerial = new Intent(mContext, SerialsDeviceList.class);
                        startActivityForResult(intentSerial, CONNECT_DEVICE);
                        break;
                    default:
                        break;
                }
            } else {
                if (myPrinter != null) {
                    myPrinter.closeConnection();
                    myPrinter = null;
                    XLog.i(TAG, "yxz at SettingActivity.java  onClick()  mPrinter:" + myPrinter);
                    if (interfaceType == 0 && hasRegDisconnectReceiver) {
                        mContext.unregisterReceiver(myReceiver);
                        hasRegDisconnectReceiver = false;
                        // Log.i(TAG, "关闭了广播！");
                    }
                }
                tv_device_name.setText(getString(R.string.printerName));
                tv_printer_address.setText(getString(R.string.printerAddress));
            }

        }

        if (v == btn_scan_and_connect) {
            connectMains = 2;
            if (interfaceType != 0) {
                Toast.makeText(mContext, "此功能只适用于部分蓝牙打印机", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isConnected) {
                Toast.makeText(mContext, "当前已经连接到" + devicesName, Toast.LENGTH_SHORT).show();
                return;
            }
            // 判断设备蓝牙功能是否打开
            if (!mBtAdapter.isEnabled()) {
                // 打开蓝牙功能
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, ENABLE_BT);
            } else {
                Intent intent = new Intent();
                intent.setClass(mContext, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        }

        if (v == btn_selfprint_test) {
            // WIFI 由于是网络操作，需要放到线程中，其他通信方式可以放在线程中也可以不必如此
            if (isConnected) {
                new Thread(new Runnable() {
                    public void run() {
                        XTUtils.printTest(getResources(), myPrinter);
                    }

                }).start();
            } else {

                Toast.makeText(mContext, getString(R.string.no_connected), Toast.LENGTH_SHORT).show();
            }
        }
        if (v == btn_status_test) {
            if (isConnected) {
                new Thread(new Runnable() {
                    public void run() {
                        int i = myPrinter.getCurrentStatus();
                        if (i == 0) {
                            strStatus = "打印机状态正常";
                        } else if (i == -1) {
                            strStatus = "接收数据失败";
                        } else if (i == -2) {
                            strStatus = "打印机缺纸";
                        } else if (i == -3) {
                            strStatus = "打印机纸将尽";
                        } else if (i == -4) {
                            strStatus = "打印机开盖";
                        } else if (i == -5) {
                            strStatus = "发送数据失败";
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(mContext, strStatus, Toast.LENGTH_LONG).show();
                                XLog.i(TAG, "zl at SettingActivity.java onClick()------> btn_status_test");
                            }
                        });

                    }

                }).start();
            } else {
                Toast.makeText(mContext, getString(R.string.no_connected), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 使设备震动
     */
    public void vibrators() {

        Vibrator vib = (Vibrator) SettingActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(1000);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static String jsonToStringFromAssetFolder(String fileName, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(fileName);
        byte[] data = new byte[file.available()];
        file.read(data);
        file.close();
        return new String(data, "gbk");

    }

    // 安卓3.1以后才有权限操作USB
    @SuppressLint("ShowToast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == CONNECT_DEVICE) {// 连接设备
            if (interfaceType == 0) {

                devicesAddress = data.getExtras().getString(BluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
                devicesName = data.getExtras().getString(BluetoothDeviceList.EXTRA_DEVICE_NAME);
                connect2BlueToothdevice();

            } else if (interfaceType == 1)// usb
            {
                mUSBDevice = data.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                myPrinter = PrinterInstance.getPrinterInstance(mContext, mUSBDevice, mHandler);
                devicesName = mUSBDevice.getDeviceName();
                devicesAddress = "vid: " + mUSBDevice.getVendorId() + "  pid: " + mUSBDevice.getProductId();
                UsbManager mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
                if (mUsbManager.hasPermission(mUSBDevice)) {
                    myPrinter.openConnection();
                } else {
                    // 没有权限询问用户是否授予权限
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                            new Intent(ACTION_USB_PERMISSION), 0);
                    IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                    filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                    filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                    mContext.registerReceiver(mUsbReceiver, filter);
                    mUsbManager.requestPermission(mUSBDevice, pendingIntent); // 该代码执行后，系统弹出一个对话框
                }

            } else if (interfaceType == 2)// wifi
            {
                devicesName = "Net device";
                devicesAddress = data.getStringExtra("ip_address");
                myPrinter = PrinterInstance.getPrinterInstance(devicesAddress, 9100, mHandler);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myPrinter.openConnection();
                    }
                }).start();
            } else if (interfaceType == 3) {// 串口

                int baudrate = 9600;
                String path = data.getStringExtra("path");
                devicesName = "Serial device";
                devicesAddress = path;
                String com_baudrate = data.getExtras().getString("baudrate");
                if (com_baudrate == null || com_baudrate.length() == 0) {
                    baudrate = 9600;
                }
                baudrate = Integer.parseInt(com_baudrate);
                XLog.i(TAG, "baudrate:" + baudrate);
                myPrinter = PrinterInstance.getPrinterInstance(new File(path), baudrate, 0, mHandler);
                myPrinter.openConnection();
                // myPrinter
                // .printText("测试串口连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭测试蓝牙连续连接--打印--关闭/n");
                // myPrinter.closeConnection();
                Log.i(TAG, "波特率:" + baudrate + "路径:" + path);
            }

        }
        if (requestCode == SCANNIN_GREQUEST_CODE) {

            // 校验扫描到的mac是否合法
            devicesAddress = data.getExtras().getString(BluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
            Log.i(TAG, "devicesAddress:" + devicesAddress);
            devicesAddress = XTUtils.formatTheString(devicesAddress);
            if (BluetoothAdapter.checkBluetoothAddress(devicesAddress)) {
                connect2BlueToothdevice();
            } else {
                Log.e("yxz", devicesAddress);
                Toast.makeText(mContext, "蓝牙mac:" + devicesAddress + "不合法", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                if (!mBtAdapter.isEnabled()) {
                    // 打开蓝牙功能
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, SettingActivity.ENABLE_BT);
                } else {
                    switch (connectMains) {
                        case 0:
                            // mDevice
                            devicesAddress = PrefUtils.getString(mContext, GlobalContants.DEVICEADDRESS, "");

                            if (devicesAddress == null || devicesAddress.length() <= 0) {
                                Toast.makeText(SettingActivity.this, "您是第一次启动程序，请选择重新搜索连接！", Toast.LENGTH_SHORT).show();
                            } else {
                                connect2BlueToothdevice();

                            }
                            break;
                        case 1:
                            Intent intent = new Intent(mContext, BluetoothDeviceList.class);
                            startActivityForResult(intent, CONNECT_DEVICE);
                            break;
                        case 2:
                            Intent scanIntent = new Intent();
                            scanIntent.setClass(mContext, MipcaActivityCapture.class);
                            scanIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(scanIntent, SCANNIN_GREQUEST_CODE);
                            break;
                    }
                }
            } else {
                Toast.makeText(SettingActivity.this, R.string.bt_not_enabled, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void connect2BlueToothdevice() {
        dialog.show();
        mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(devicesAddress);
        devicesName = mDevice.getName();
        myPrinter = PrinterInstance.getPrinterInstance(mDevice, mHandler);
        if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {// 未绑定
            // IntentFilter boundFilter = new IntentFilter();
            // boundFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            // mContext.registerReceiver(boundDeviceReceiver, boundFilter);
            PairOrConnect(true);
        } else {
            PairOrConnect(false);
        }
    }

    private void PairOrConnect(boolean pair) {
        if (pair) {
            IntentFilter boundFilter = new IntentFilter();
            boundFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            mContext.registerReceiver(boundDeviceReceiver, boundFilter);
            boolean success = false;
            try {
                // // 自动设置pin值
                // Method autoBondMethod =
                // BluetoothDevice.class.getMethod("setPin", new Class[] {
                // byte[].class });
                // boolean result = (Boolean) autoBondMethod.invoke(mDevice, new
                // Object[] { "1234".getBytes() });
                // Log.i(TAG, "setPin is success? : " + result);

                // 开始配对 这段代码打开输入配对密码的对话框
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                success = (Boolean) createBondMethod.invoke(mDevice);
                // // 取消用户输入
                // Method cancelInputMethod =
                // BluetoothDevice.class.getMethod("cancelPairingUserInput");
                // boolean cancleResult = (Boolean)
                // cancelInputMethod.invoke(mDevice);
                // Log.i(TAG, "cancle is success? : " + cancleResult);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "createBond is success? : " + success);
        } else {
            new connectThread().start();

        }
    }

    private void updateButtonState(boolean isConnected) {
        XLog.d(TAG, "yxz at SettingActivity.java updateButtonState() ---begin");
        XLog.d(TAG, "yxz at SettingActivity.java updateButtonState() ---isConnected" + isConnected);
        if (isConnected) {
            headerConnecedState.setText(R.string.on_line);
            btn_search_devices.setText(R.string.disconnect);
            setTitleState(mContext.getResources().getString(R.string.on_line));
            Log.i("fdh", getString(R.string.printerName).split(":")[0]);
            Log.i("fdh", getString(R.string.printerAddress).split(":")[0]);
            tv_device_name.setText(getString(R.string.printerName).split(":")[0] + ": " + devicesName);
            tv_printer_address.setText(getString(R.string.printerAddress).split(":")[0] + ": " + devicesAddress);
        } else {
            tv_device_name.setText(getString(R.string.printerName));
            tv_printer_address.setText(getString(R.string.printerAddress));
            btn_search_devices.setText(R.string.connect);
            headerConnecedState.setText(R.string.off_line);
            setTitleState(mContext.getResources().getString(R.string.off_line));
            // mHandler.removeCallbacks(runnable);
            // if (isFirst) {
            //
            // } else {
            // timer.cancel();
            // }
            // Log.i(TAG, "定时器取消了");
            XLog.d(TAG, "yxz at SettingActivity.java updateButtonState() ---end");

        }

        PrefUtils.setBoolean(SettingActivity.this, GlobalContants.CONNECTSTATE, isConnected);

    }

    private BroadcastReceiver boundDeviceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!mDevice.equals(device)) {
                    return;
                }
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.i(TAG, "bounding......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.i(TAG, "bound success");
                        // if bound success, auto init BluetoothPrinter. open
                        // connect.
                        mContext.unregisterReceiver(boundDeviceReceiver);
                        dialog.show();
                        // 配对完成开始连接
                        if (myPrinter != null) {
                            new connectThread().start();
                        }
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.i(TAG, "执行顺序----4");

                        mContext.unregisterReceiver(boundDeviceReceiver);
                        Log.i(TAG, "bound cancel");
                        break;
                    default:
                        break;
                }

            }
        }
    };

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @SuppressLint("NewApi")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.w(TAG, "receiver action: " + action);

            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    mContext.unregisterReceiver(mUsbReceiver);
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                            && mUSBDevice.equals(device)) {
                        myPrinter.openConnection();
                    } else {
                        mHandler.obtainMessage(Connect.FAILED).sendToTarget();
                        Log.e(TAG, "permission denied for device " + device);
                    }
                }
            }
        }
    };

    private class connectThread extends Thread {
        @Override
        public void run() {
            if (myPrinter != null) {
                isConnected = myPrinter.openConnection();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // if (parent == spinner_printer_type) {
        // PrefUtils.setInt(mContext, GlobalContants.PRINTERID, position);
        // String printerName = getResources().getStringArray(
        // R.array.printertype)[position];
        // if (printerName.contains("T10") || printerName.contains("POS58")
        // || printerName.contains("T7")) {
        // rg__select_paper_size.check(R.id.rb_58mm);
        // } else if (printerName.contains("L31")
        // || printerName.contains("T9")
        // || printerName.contains("POS885")
        // || printerName.contains("EU80")) {
        // rg__select_paper_size.check(R.id.rb_80mm);
        // } else if (printerName.contains("L51")) {
        // rg__select_paper_size.check(R.id.rb_100mm);
        // }
        // } else if (parent == spinner_interface_type) {
        // }

        if (parent == spinner_interface_type) {
            PrefUtils.setInt(mContext, GlobalContants.INTERFACETYPE, position);
            interfaceType = position;
            Log.i(TAG, "position:" + position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == rg__select_paper_size) {
            switch (checkedId) {
                case R.id.rb_58mm:
                    PrinterConstants.paperWidth = 384;
                    break;
                case R.id.rb_80mm:
                    PrinterConstants.paperWidth = 576;
                    break;
                case R.id.rb_100mm:
                    PrinterConstants.paperWidth = 816;
                    break;
                default:
                    PrinterConstants.paperWidth = 576;
                    break;
            }
            PrefUtils.setInt(mContext, GlobalContants.PAPERWIDTH, PrinterConstants.paperWidth);
        }

    }

	/*
	 * @Override public void onConfigurationChanged(Configuration newConfig) {
	 * super.onConfigurationChanged(newConfig); if
	 * (this.getResources().getConfiguration().orientation ==
	 * Configuration.ORIENTATION_LANDSCAPE) { // land } else if
	 * (this.getResources().getConfiguration().orientation ==
	 * Configuration.ORIENTATION_PORTRAIT) { // port } }
	 */

    @SuppressLint({ "InlinedApi", "NewApi" })
    public void usbAutoConn(UsbManager manager) {

        doDiscovery(manager);
        if (deviceList.isEmpty()) {
            Toast.makeText(mContext, R.string.no_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        mUSBDevice = deviceList.get(0);
        if (mUSBDevice == null) {
            mHandler.obtainMessage(Connect.FAILED).sendToTarget();
            return;
        }
        myPrinter = PrinterInstance.getPrinterInstance(mContext, mUSBDevice, mHandler);
        devicesName = mUSBDevice.getDeviceName();
        devicesAddress = "vid: " + mUSBDevice.getVendorId() + "  pid: " + mUSBDevice.getProductId();
        UsbManager mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        if (mUsbManager.hasPermission(mUSBDevice)) {
            myPrinter.openConnection();
        } else {
            // 没有权限询问用户是否授予权限
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            mContext.registerReceiver(mUsbReceiver, filter);
            mUsbManager.requestPermission(mUSBDevice, pendingIntent); // 该代码执行后，系统弹出一个对话框
        }

    }

    @SuppressLint("NewApi")
    private void doDiscovery(UsbManager manager) {
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        deviceList = new ArrayList<UsbDevice>();
        for (UsbDevice device : devices.values()) {
            if (USBPort.isUsbPrinter(device)) {
                deviceList.add(device);
            }
        }

    }

    private String getWiFiName() {
        String wifiName = null;
        WifiManager mWifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (!mWifi.isWifiEnabled()) {
            mWifi.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = mWifi.getConnectionInfo();
        wifiName = wifiInfo.getSSID();
        Log.i("yxz", "wifiName" + wifiName);
        wifiName = wifiName.replaceAll("\"", "");
        return wifiName;

    }


    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {

                if (device != null && myPrinter != null && isConnected && device.equals(mDevice)) {
                    myPrinter.closeConnection();
                    mHandler.obtainMessage(Connect.CLOSED).sendToTarget();
                }
            }

        }
    };

    //全切
    public void allCut(View v) {
        if (isConnected) {
            new Thread(new Runnable() {
                public void run() {
                    XTUtils.printNote(getResources(), myPrinter);
                    myPrinter.cutPaper(65, 50);
                }
            }).start();

        } else {
            Toast.makeText(mContext, getString(R.string.no_connected), Toast.LENGTH_SHORT).show();
        }
    }

    //半切
    public void halfCut(View v) {
        if (isConnected) {
            new Thread(new Runnable() {
                public void run() {
                    XTUtils.printNote(getResources(), myPrinter);
                    myPrinter.cutPaper(66, 50);
                }
            }).start();

        } else {

            Toast.makeText(mContext, getString(R.string.no_connected),Toast.LENGTH_SHORT).show();
        }
    }
}

