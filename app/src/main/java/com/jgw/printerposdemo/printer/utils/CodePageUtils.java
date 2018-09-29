package com.jgw.printerposdemo.printer.utils;

import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import com.jgw.printerposdemo.R;
import com.printer.sdk.PrinterConstants.Command;
import com.printer.sdk.PrinterInstance;

public class CodePageUtils
{

	private int cTypeid=0;

	 public void selectCodePage(final Context context,final PrinterInstance mPrinter)
     {
   	  LayoutInflater inflater = LayoutInflater.from(context);
         View BarcodeView = inflater.inflate(R.layout.codepage, null);
         final Spinner page_types = (Spinner)BarcodeView.findViewById(R.id.page_types);


         final ArrayAdapter<CharSequence> pagetypes =ArrayAdapter.createFromResource(context,R.array.codepages, android.R.layout.simple_spinner_item);
         page_types.setAdapter(pagetypes);

         page_types.setPromptId(R.string.codepageTest);

         page_types.setOnItemSelectedListener(new OnItemSelectedListener()
         {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{
				cTypeid=arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub
			}});

         AlertDialog.Builder builder = new AlertDialog.Builder(context);
         builder.setCancelable(false);
         builder.setTitle(R.string.codepageTest);
         builder.setView(BarcodeView);

         builder.setPositiveButton(R.string.print, new DialogInterface.OnClickListener()
         {
 			@Override
 			public void onClick(DialogInterface dialog, int which)
 			{

                 System.out.println("cTypeid="+cTypeid);
                 try
                 {
										byte [] realData = new byte[]{
							(byte)0x80,(byte)0x81,(byte)0x82,(byte)0x83,
							(byte)0x84,(byte)0x85,(byte)0x86,(byte)0x87,
							(byte)0x88,(byte)0x89,(byte)0x8A,(byte)0x8B,
							(byte)0x8C,(byte)0x8D,(byte)0x8E,(byte)0x8F,

							(byte)0x90,(byte)0x91,(byte)0x92,(byte)0x93,
							(byte)0x94,(byte)0x95,(byte)0x96,(byte)0x97,
							(byte)0x98,(byte)0x99,(byte)0x9A,(byte)0x9B,
							(byte)0x9C,(byte)0x9D,(byte)0x9E,(byte)0x9F,

							(byte)0xA0,(byte)0xA1,(byte)0xA2,(byte)0xA3,
							(byte)0xA4,(byte)0xA5,(byte)0xA6,(byte)0xA7,
							(byte)0xA8,(byte)0xA9,(byte)0xAA,(byte)0xAB,
							(byte)0xAC,(byte)0xAD,(byte)0xAE,(byte)0xAF,

							(byte)0xB0,(byte)0xB1,(byte)0xB2,(byte)0xB3,
							(byte)0xB4,(byte)0xB5,(byte)0xB6,(byte)0xB7,
							(byte)0xB8,(byte)0xB9,(byte)0xBA,(byte)0xBB,
							(byte)0xBC,(byte)0xBD,(byte)0xBE,(byte)0xBF,

							(byte)0xC0,(byte)0xC1,(byte)0xC2,(byte)0xC3,
							(byte)0xC4,(byte)0xC5,(byte)0xC6,(byte)0xC7,
							(byte)0xC8,(byte)0xC9,(byte)0xCA,(byte)0xCB,
							(byte)0xCC,(byte)0xCD,(byte)0xCE,(byte)0xCF,

							(byte)0xD0,(byte)0xD1,(byte)0xD2,(byte)0xD3,
							(byte)0xD4,(byte)0xD5,(byte)0xD6,(byte)0xD7,
							(byte)0xD8,(byte)0xD9,(byte)0xDA,(byte)0xDB,
							(byte)0xDC,(byte)0xDD,(byte)0xDE,(byte)0xDF,

							(byte)0xE0,(byte)0xE1,(byte)0xE2,(byte)0xE3,
							(byte)0xE4,(byte)0xE5,(byte)0xE6,(byte)0xE7,
							(byte)0xE8,(byte)0xE9,(byte)0xEA,(byte)0xEB,
							(byte)0xEC,(byte)0xED,(byte)0xEE,(byte)0xEF,

							(byte)0xF0,(byte)0xF1,(byte)0xF2,(byte)0xF3,
							(byte)0xF4,(byte)0xF5,(byte)0xF6,(byte)0xF7,
							(byte)0xF8,(byte)0xF9,(byte)0xFA,(byte)0xFB,
							(byte)0xFC,(byte)0xFD,(byte)0xFE,(byte)0xFF,
					        };

			              if(cTypeid!=47)
				          {

		                      mPrinter.printText(context.getResources().getString(R.string.print)+pagetypes.getItem(cTypeid).toString()+context.getResources().getString(R.string.str_show));
		                      mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

		                      mPrinter.sendBytesData(new byte[]{(byte)0x1c,(byte)0x2E});

		                      mPrinter.sendBytesData(new byte[]{(byte)0x1B,(byte)0x74,(byte)cTypeid});
		                      mPrinter.sendBytesData(realData);
		                      mPrinter.sendBytesData(new byte[]{(byte)0x0A});

		                      mPrinter.sendBytesData(new byte[]{(byte)0x1c,(byte)0x26});
		                      mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

       				    }else
       				    {
       					    for(cTypeid=0;cTypeid<46;cTypeid++)
       					    {
       						     if(cTypeid>14 || cTypeid<11)
       						     {

	        						  mPrinter.printText(context.getResources().getString(R.string.print)+pagetypes.getItem(cTypeid).toString()+""+context.getResources().getString(R.string.str_show));
	    		                      mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

	    		                      mPrinter.sendBytesData(new byte[]{(byte)0x1c,(byte)0x2E});

	    		                      mPrinter.sendBytesData(new byte[]{(byte)0x1B,(byte)0x74,(byte)cTypeid});
	    		                      mPrinter.sendBytesData(realData);
	    		                      mPrinter.sendBytesData(new byte[]{(byte)0x0A});

	    		                      mPrinter.sendBytesData(new byte[]{(byte)0x1c,(byte)0x26});
	    		                      mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
       						 }
       					}
       				}

                     Field field = dialog.getClass()
  				            .getSuperclass().getDeclaredField(
  				                     "mShowing" );
  				    field.setAccessible( true );
  				     //   将mShowing变量设为false，表示对话框已关�?
  				    field.set(dialog, false );

           }catch(Exception e)
           {

           }

		}});

         builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener()
         {
 			@Override
 			public void onClick(DialogInterface dialog, int which)
 			{
 				 Field field;
				try {
					field = dialog.getClass()
					            .getSuperclass().getDeclaredField(
					                     "mShowing" );

					field.setAccessible( true );
				     //   将mShowing变量设为false，表示对话框已关�?
				    field.set(dialog, true );
				    dialog.dismiss();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

 			}

 		});

         builder.create();
         final AlertDialog dialog=builder.show();

     }

}
