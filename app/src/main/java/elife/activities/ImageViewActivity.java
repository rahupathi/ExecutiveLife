package elife.activities;

import java.io.File;

import com.taeligstatus.R;

import elife.entity.Global;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class ImageViewActivity extends Activity {
	private String mImageFileName = "";
	private ImageView imgView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle j = getIntent().getExtras();
		mImageFileName = (j.getString("FileName"));

		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view);

		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.parseColor("#305B07"));

		imgView = (ImageView) findViewById(R.id.imgViewAttacment);
//testete
		// String
		// sImagePath=Environment.getExternalStorageDirectory()+File.separator
		// +"ELife";
		String sImagePath = Global.SDCardImagePath;
		File imgFile = new File(sImagePath + File.separator + mImageFileName);
		if (imgFile.exists()) {
			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());
			imgView.setImageBitmap(myBitmap);
		}
		
		this.findViewById(R.id.btnClosePreview).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
