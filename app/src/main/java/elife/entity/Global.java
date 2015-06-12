package elife.entity;

import java.io.File;

import android.os.Environment;

public class Global {
	public static final String SERVICE_URI ="http://executivelife.com.au/MobileService/DataUpload.svc";
	//public static final String SERVICE_URI = "http://10.0.2.2:3272/DataUpload.svc";
	public static String svar1, svar2;
	public static int[] myarray1 = new int[10];
	public static String SDCardImagePath = Environment
			.getExternalStorageDirectory() + File.separator + "ELife";
	public static String LocalImagePath = "";

}
