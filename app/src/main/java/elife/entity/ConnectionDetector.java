package elife.entity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
	
	private Context _context;
	
	public ConnectionDetector(Context context){
        this._context = context;
    }
	
	public boolean isConnectingToInternet(){
		boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;
	
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              for (NetworkInfo ni : info) {
            	  if (ni.getTypeName().equalsIgnoreCase("WIFI")){
                      if (ni.isConnected())
                          haveConnectedWifi = true;
            	  }
                  if (ni.getTypeName().equalsIgnoreCase("MOBILE")){
                      if (ni.isConnected())
                          haveConnectedMobile = true;
                  }
              }             
          }
          return haveConnectedWifi || haveConnectedMobile;
	}
}
