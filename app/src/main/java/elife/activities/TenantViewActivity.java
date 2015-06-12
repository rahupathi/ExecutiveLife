package elife.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.ListView;
import android.widget.ArrayAdapter;

import com.taeligstatus.R;

public class TenantViewActivity extends Activity {

	String mUnitId = "";
	String mUnitCode = "";
	private String mTenantName = "";
	private String mEmailId = "";
	private String mMobileNo = "";
	ListView lvTenantView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenant_view);

		Bundle j = getIntent().getExtras();
		mUnitId = (j.getString("UnitId"));
		mUnitCode = (j.getString("UnitCode"));
		mTenantName = (j.getString("TenantName"));
		mEmailId = (j.getString("EmailId"));
		mMobileNo = (j.getString("MobileNo"));

		this.setTitle("ExecutiveLife: Unit: " + mUnitCode);

		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.parseColor("#305B07"));

		if (j != null) {
		
			lvTenantView = (ListView) findViewById(R.id.lvTVTenantView);
			String stringTenant[] = new String[4];
			// stringTenant[0]= "∞ " + mUnitCode;
			stringTenant[1] = "Name:" + mTenantName;
			stringTenant[2] = "✆ " + mMobileNo;
			stringTenant[3] = "✉ " + mEmailId;

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.my_simple_list_1, android.R.id.text1, stringTenant);
			lvTenantView.setAdapter(adapter);

			// Assign adapter to ListView

			findViewById(R.id.btnCall).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// Toast.makeText(getApplicationContext(),
							// mMobileNo, Toast.LENGTH_LONG).show();
							Intent callIntent = new Intent(Intent.ACTION_CALL);
							String p = "tel:" + mMobileNo;
							callIntent.setData(Uri.parse(p));
							startActivity(callIntent);
						}
					});
		}
	}
}