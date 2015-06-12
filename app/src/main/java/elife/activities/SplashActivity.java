package elife.activities;

import com.taeligstatus.R;

import android.os.Bundle;
import android.os.Handler;

import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.splash);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_bar);          
        //set the title         
        //TextView textView = (TextView)findViewById(R.id.custom_title_text);         
        //textView.setText("Welcome HPS Assistant");
        
       // View title = getWindow().findViewById(android.R.id.title);        
       // View titleBar = (View) title.getParent();
         
        //titleBar.setBackgroundColor(Color.parseColor("#5EFB6E"));
                        
        Handler handler = new Handler();
        
        // run a thread after 2 seconds to start the home screen
        handler.postDelayed(new Runnable() {
 
            @Override
            public void run() {
 
                // make sure we close the splash screen so the user won't come back when it presses back key
 
                finish();
                // start the home screen
 
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(intent);
 
            }
 
        }, 4000); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
 
    }
    
}
