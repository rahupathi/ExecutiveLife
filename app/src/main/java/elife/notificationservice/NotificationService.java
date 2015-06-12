package elife.notificationservice;


import com.taeligstatus.R;

import elife.activities.*;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class NotificationService extends IntentService{

	public NotificationService() {
		super("NotificationService");
		// TODO Auto-generated constructor stub
	}
	  @Override
	    protected void onHandleIntent(Intent intent) {
	        Log.d("MyService", "About to execute MyTask");
	        new MyTask().execute();
	        this.sendNotification(this);
	    }
	  
	  
	    private class MyTask extends AsyncTask<String, Void, Boolean> {
	        @Override
	         protected Boolean doInBackground(String... strings) {
	                Log.d("MyService - MyTask", "Calling doInBackground within MyTask");
	               return false;
	        }
	    }     
	    
	    private void sendNotification(Context context) {
	        //Intent notificationIntent = new Intent(context, GeneralSettingsActivity.class);
	        //PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
	        //NotificationManager notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	        //Notification notification =  new Notification(R.drawable.hps_new_logo2, "You have Message From HPS", System.currentTimeMillis());
	        //notification.flags |= Notification.FLAG_AUTO_CANCEL;
	        //notification.setLatestEventInfo(context, "Message","You have Important Message From HPS", contentIntent);
	        //notificationMgr.notify(0, notification);
	     }
}
