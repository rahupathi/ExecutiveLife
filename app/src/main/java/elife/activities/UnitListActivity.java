package elife.activities;

import java.util.ArrayList;
import java.util.Locale;
import android.view.View.OnClickListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.taeligstatus.R;

import elife.adapters.UnitViewAdapter;
import elife.entity.*;

public class UnitListActivity extends Activity {

	EditText minputSearch = null;
	private GetUnitDetailTask mUnitTask = null;
	private String mSortBy = "Unit";
	PopupWindow popupMessage;
	Button popupButton, insidePopupButton;
	TextView popupText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unit_list);

		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.parseColor("#305B07"));

		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);

		// Bundle j = getIntent().getExtras();
		// mStaffId =(j.getString("StaffId"));
		GetUnitsAsAsync();
	}

	private PopupWindow pwindo;
	Button btnClosePopup;

	private void initiatePopupWindow() {
		try {
			// We need to get the instance of the LayoutInflater
			LayoutInflater inflater = (LayoutInflater) UnitListActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.popup_unit,
					(ViewGroup) findViewById(R.id.popup_element));
			pwindo = new PopupWindow(layout, 340, 450, true);
			pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

			TextView txtPopupHeader = (TextView) layout
					.findViewById(R.id.txtPopupHeader);
			TextView txtPopupContent = (TextView) layout
					.findViewById(R.id.txtPopupContent);

			txtPopupHeader.setText("Unit Summary");
			txtPopupContent
					.setText(Html
							.fromHtml("<h4>Summary:</h4><br>There are 100 units in active. There are 278 tenants occupied as per lease details.<br>When you do long press you can select the sorting order."));

			btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
			btnClosePopup.setOnClickListener(cancel_button_click_listener);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnClickListener cancel_button_click_listener = new OnClickListener() {
		public void onClick(View v) {
			pwindo.dismiss();

		}
	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		menu.setHeaderTitle("Select Sort Order");
		inflater.inflate(R.menu.unit_context_menu, menu);
		for (int i = 0; i < menu.size(); ++i) {
			MenuItem mi = menu.getItem(i);
			// check the Id as you wish
			if (mi.getItemId() == R.id.unit_item1) {
				if (IdChecked == 1) {
					mi.setChecked(true);
				}
			}
			if (mi.getItemId() == R.id.unit_item2) {
				if (IdChecked == 2) {
					mi.setChecked(true);
				}
			}

		}
	}

	/* Sorting Context Menu display */

	private int IdChecked = 1;

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.unit_item1:
			IdChecked = 1;
			shortBy("Unit");
			return true;

		case R.id.unit_item2:
			IdChecked = 2;
			shortBy("Suburb");
			return true;

		default:
			return super.onContextItemSelected(item);

		}

	}

	public void shortBy(String SortBy) {
		mSortBy = SortBy;
		GetUnitsAsAsync();
	}

	public void GetUnitsAsAsync() {

		if (mUnitTask != null) {
			return;
		}
		mUnitTask = new GetUnitDetailTask();
		mUnitTask.execute((Void) null);
		mUnitTask = null;
	}

	public class GetUnitDetailTask extends AsyncTask<Void, Void, Boolean> {

		private String sResult = "";
		private String vException = "";
		private ArrayList<UnitsDetail> arraylist = new ArrayList<UnitsDetail>();
		private UnitViewAdapter adapter;
		private ListView lView;
		private EditText inputSearch;
		private ProgressDialog dialog;

		@Override
		protected Boolean doInBackground(Void... params) {

			ConnectionDetector cd = new ConnectionDetector(
					getApplicationContext());
			if (!cd.isConnectingToInternet()) {
				vException = "Check Internet connection";
				return false;
			}

			try {
				HttpClient client = new DefaultHttpClient();
				client.getParams().getParameter(ConnRoutePNames.DEFAULT_PROXY);

				HttpGet httpGet = new HttpGet(Global.SERVICE_URI
						+ "/GetUnitList/" + mSortBy);

				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-Type", "application/json");

				HttpResponse httpresp = client.execute(httpGet);

				String tResult = EntityUtils.toString(httpresp.getEntity());
				JSONObject jObj = new JSONObject(tResult);
				if (!TextUtils.isEmpty(jObj.getString("Message"))) {
					if (jObj.getString("Message").equalsIgnoreCase("NODATA")) {
						vException = "Detail not found";
						return false;
					} else {
						vException = "Exception occurs while loading data";
						return false;
					}
				} else {
					sResult = jObj.toString();
					return true;
				}
			} catch (Exception ex) {
				vException = "Error Occurred:" + ex.getMessage();
				return true;
			}

		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(UnitListActivity.this, "",
					"Loading. Please wait...", true);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mUnitTask = null;
			dialog.dismiss();
			if (success) {
				this.LoadGrid(sResult);
			} else {
				if (!TextUtils.isEmpty(vException))
					Toast.makeText(UnitListActivity.this, vException,
							Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mUnitTask = null;
			dialog.dismiss();
			// showProgress(false);
		}

		protected void LoadGrid(String sResult) {
			arraylist = new ArrayList<UnitsDetail>();
			arraylist = this.GetUnitInfoAsList(sResult);

			lView = ((ListView) findViewById(R.id.lvUnitList));
			// adapter= new MemberDependentAdapter(this, CustomerList);

			adapter = new UnitViewAdapter(UnitListActivity.this, arraylist);
			lView.setAdapter(adapter);
			// context menu added here
			registerForContextMenu(lView);

			inputSearch = (EditText) findViewById(R.id.unitSearch);

			inputSearch.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
					String text = inputSearch.getText().toString()
							.toLowerCase(Locale.getDefault());
					adapter.filter(text);
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
				}
			});

			// Bind List Items
			lView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Object o = lView.getItemAtPosition(position);
					UnitsDetail ObjUnitsDetailInfo = (UnitsDetail) o;
					String UnitId = ObjUnitsDetailInfo.getUnitId();
					String sUnitCode = ObjUnitsDetailInfo.getUnitCode();
					Bundle b = new Bundle();
					b.putString("UnitId", UnitId);
					b.putString("UnitCode", sUnitCode);
					Intent i = new Intent(UnitListActivity.this,
							TenantByUnitListActivity.class);
					i.putExtras(b);
					startActivity(i);
					// finish();
				}
			});

			overridePendingTransition(R.anim.activity_open_translate,
					R.anim.activity_close_scale);

		}

		protected ArrayList<UnitsDetail> GetUnitInfoAsList(String unitListString) {
			ArrayList<UnitsDetail> results = new ArrayList<UnitsDetail>();
			try {

				JSONObject UnitInfoJsonObject = new JSONObject(unitListString);
				JSONArray UnitInfoArrayDetials = UnitInfoJsonObject
						.getJSONArray("UnitsList");
				for (int i = 0; i < UnitInfoArrayDetials.length(); i++) {

					// Get Each Object from Array
					JSONObject tUnitInfo = UnitInfoArrayDetials
							.getJSONObject(i);

					String UnitId = tUnitInfo.getString("UnitId");
					String UnitCode = tUnitInfo.getString("UnitCode");
					String StreetAddress = tUnitInfo.getString("StreetAddress");
					String EstateAgentID = tUnitInfo.getString("EstateAgentID");
					String SuburbName = tUnitInfo.getString("SuburbName");
					String PostalCode = tUnitInfo.getString("PostalCode");

					UnitsDetail objUnitDetail = new UnitsDetail();
					objUnitDetail.setUnitId(UnitId);
					objUnitDetail.setUnitCode(UnitCode);
					objUnitDetail.setStreetAddress(StreetAddress);
					objUnitDetail.setEstateAgentID(EstateAgentID);
					objUnitDetail.setSuburbName(SuburbName);
					objUnitDetail.setPostalCode(PostalCode);

					results.add(objUnitDetail);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return results;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_back, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		case R.id.mnuBack:
			popupMessage.dismiss();
			finish();
			return true;

		case R.id.mnuDetail:
			initiatePopupWindow();
			// View v = findViewById(R.layout.unit_list);
			// popupMessage.showAsDropDown(v, 0, 0);
			// popupMessage.showAsDropDown(getWindow().getLayoutInflater().get,0,0);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onPause() {
		super.onPause();
		// closing transition animations
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_open_translate);
	}

}