package elife.activities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRoutePNames;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.speech.RecognizerIntent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;

import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


import com.taeligstatus.R;

import elife.adapters.MemberDependentAdapter;
import elife.entity.*;

public class MemberInfoListActivity extends Activity {
	protected static final int RESULT_SPEECH = 1;
	private ArrayList<CustomerDetail> arraylist = new ArrayList<CustomerDetail>();
	private GetTenantDetailTask mAuthTask = null;
	private String mSortBy = "Name";
	// private static final String SERVICE_URI =
	// "http://10.0.2.2:3272/Mobile.svc";
    String mMemberSystemId = "";
	private EditText inputSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memberdetails);

		// Button btn = (Button) findViewById(R.id.btnTenantSorting);
		// registerForContextMenu(btn);

		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.parseColor("#305B07"));

		// Bundle j=getIntent().getExtras();
		mMemberSystemId = "1";// (j.getString("StaffId"));
		GetMemberDetailByStaffId();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	private PopupWindow pwindo;
	Button btnClosePopup;

	private void initiatePopupWindow() {
		try {
			// We need to get the instance of the LayoutInflater
			LayoutInflater inflater = (LayoutInflater) MemberInfoListActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.popup_unit,
					(ViewGroup) findViewById(R.id.popup_element));
			pwindo = new PopupWindow(layout, 340, 450, true);
			pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

			TextView txtPopupHeader = (TextView) layout
					.findViewById(R.id.txtPopupHeader);
			TextView txtPopupContent = (TextView) layout
					.findViewById(R.id.txtPopupContent);

			txtPopupHeader.setText("Tenant Summary");
			txtPopupContent
					.setText(Html
							.fromHtml("There are 100 units in active. There are 278 tenants occupied as per lease details.<br>When you do long press you can select the sorting order."));

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

	public void GetMemberDetailByStaffId() {

		if (mAuthTask != null) {
			return;
		}

		mAuthTask = new GetTenantDetailTask();
		mAuthTask.execute((Void) null);
		mAuthTask = null;

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		menu.setHeaderTitle("Sort By");
		inflater.inflate(R.menu.context_menu, menu);
		for (int i = 0; i < menu.size(); ++i) {
			MenuItem mi = menu.getItem(i);
			// check the Id as you wish
			if (mi.getItemId() == R.id.item2) {
				if (IdChecked == 2) {
					mi.setChecked(true);
				}
			}
			if (mi.getItemId() == R.id.item3) {
				if (IdChecked == 3) {
					mi.setChecked(true);
				}
			}
			if (mi.getItemId() == R.id.item1) {
				if (IdChecked == 1) {
					mi.setChecked(true);
				}
			}
		}
	}

	private int IdChecked = 1;

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.item1:
			IdChecked = 1;
			shortBy("Name");
			return true;

		case R.id.item2:
			IdChecked = 2;
			shortBy("Unit");
			return true;

		case R.id.item3:
			IdChecked = 3;
			shortBy("Mobile");

			return true;
		default:
			return super.onContextItemSelected(item);

		}

	}

	public void shortBy(String SortBy) {
		mSortBy = SortBy;
		GetMemberDetailByStaffId();
	}

	public class GetTenantDetailTask extends AsyncTask<Void, Void, Boolean> {
		private String vException = "";
		private String sResult = "";

		private MemberDependentAdapter adapter;
		private ListView lView;
		

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			// try {
			// Thread.sleep(2000);
			//String Result = "";
			// Making HTTP request
			ConnectionDetector cd = new ConnectionDetector(
					getApplicationContext());
			try {
				if (cd.isConnectingToInternet()) {

					HttpParams httpParameters = new BasicHttpParams();
					// Set the timeout in milliseconds until a connection is
					// established.
					// The default value is zero, that means the timeout is not
					// used.
					int timeoutConnection = 40000;
					HttpConnectionParams.setConnectionTimeout(httpParameters,
							timeoutConnection);
					// Set the default socket timeout (SO_TIMEOUT)
					// in milliseconds which is the timeout for waiting for
					// data.
					int timeoutSocket = 40000;
					HttpConnectionParams.setSoTimeout(httpParameters,
							timeoutSocket);

					HttpClient client = new DefaultHttpClient(httpParameters);
					client.getParams().getParameter(
							ConnRoutePNames.DEFAULT_PROXY);
					HttpGet httpGet = new HttpGet(Global.SERVICE_URI
							+ "/GetTenantList/" + mSortBy);

					httpGet.setHeader("Accept", "application/json");
					httpGet.setHeader("Content-Type", "application/json");

					HttpResponse httpresp = client.execute(httpGet);

					//StatusLine statusLine = httpresp.getStatusLine();
					//int statusCode = statusLine.getStatusCode();

					String result = EntityUtils.toString(httpresp.getEntity());
					JSONObject jObj = new JSONObject(result);
					sResult = jObj.toString();
					if (!TextUtils.isEmpty(sResult)) {
						if (!TextUtils.isEmpty(jObj.getString("Message"))) {
							if (jObj.getString("Message").equalsIgnoreCase(
									"NODATA")) {
								// //mAuthTask.onPostExecute(false);
								// Toast.makeText(LoginActivity.this,
								// "Invalid UserID and Login",
								// Toast.LENGTH_LONG).show();
								vException = "Teant detail not found";
								return false;
							} else {
								// mAuthTask.onPostExecute(false);
								// Toast.makeText(LoginActivity.this,
								// "Server Access Error:" +
								// jObjError.getString("Message") ,
								// Toast.LENGTH_LONG).show();
								vException = "Server Access Error:"
										+ jObj.getString("ErrorMessage");
								return false;

							}
						} else {
							vException = "OK";
							return true;
						}
					} else {
						// Toast.makeText(LoginActivity.this,
						// "Invalid UserID and Login",
						// Toast.LENGTH_LONG).show();
						vException = "Invalid out put";
						return false;
					}
				} else {
					// Toast.makeText(getBaseContext(),
					// "No Internet Connection",
					// Toast.LENGTH_SHORT).show();
					vException = "Check you Internet Connection";
					return false;
				}
			} catch (JSONException e) {
				vException = e.getMessage();
				return false;
			} catch (UnsupportedEncodingException e) {
				vException = e.getMessage();
				return false;
			} catch (ClientProtocolException e) {
				vException = e.getMessage();
				return false;
			} catch (ConnectTimeoutException e) {
				vException = "Service Timeout: Could not connect to the service or method.";
				return false;
			} catch (SocketTimeoutException ste) {
				vException = "Service Timeout: Could not connect to the service or method.";
				return false;
			} catch (IOException e) {
				vException = "OK";
				return true;
			}
		}

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(MemberInfoListActivity.this, "",
					"Loading. Please wait...", true);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			dialog.dismiss();
			// showProgress(false);
			if (success) {
				this.LoadGrid(sResult);
			} else {
				if (!TextUtils.isEmpty(vException))
					Toast.makeText(MemberInfoListActivity.this, vException,
							Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			dialog.dismiss();
			// showProgress(false);
		}

		protected void LoadGrid(String sResult) {
			arraylist = new ArrayList<CustomerDetail>();
			arraylist = GetCustomerInfoAsList(sResult);

			lView = ((ListView) findViewById(R.id.ListView01));
			// adapter= new MemberDependentAdapter(this, CustomerList);

			adapter = new MemberDependentAdapter(MemberInfoListActivity.this,
					arraylist);
			lView.setAdapter(adapter);
			registerForContextMenu(lView);

			inputSearch = (EditText) findViewById(R.id.inputSearch);

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

			lView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					CustomerDetail ObjCustomerDetail = (CustomerDetail) lView
							.getItemAtPosition(position);
					if (ObjCustomerDetail != null) {
						String UnitId = ObjCustomerDetail.getUnitId();
						String sUnitCode = ObjCustomerDetail.getUnitCode();

						Bundle b = new Bundle();
						b.putString("UnitId", UnitId);
						b.putString("UnitCode", sUnitCode);
						Intent i = new Intent(MemberInfoListActivity.this,
								TenantByUnitListActivity.class);
						i.putExtras(b);
						startActivity(i);
					}
					// finish();
				}
			});

		}

		private ArrayList<CustomerDetail> GetCustomerInfoAsList(
				String memberListString) {

			ArrayList<CustomerDetail> results = new ArrayList<CustomerDetail>();

			try {
				// Convert String to Json Object
				JSONObject MemberInfoJsonObject = new JSONObject(
						memberListString);

				JSONArray MemberInfoArrayDetials = MemberInfoJsonObject
						.getJSONArray("CustomerDetail");

				for (int i = 0; i < MemberInfoArrayDetials.length(); i++) {

					// Get Each Object from Array
					JSONObject tCustomerInfo = MemberInfoArrayDetials
							.getJSONObject(i);
					String UnitCode = tCustomerInfo.getString("UnitCode");
					String UnitId = tCustomerInfo.getString("UnitId");

					String CustomerId = tCustomerInfo.getString("CustomerId");
					String FirstName = tCustomerInfo.getString("FirstName");
					String LastName = tCustomerInfo.getString("LastName");
					String MobileNumber = tCustomerInfo
							.getString("MobileNumber");
					String EmailId = tCustomerInfo.getString("EmailId");
					String TenantName = tCustomerInfo.getString("FirstName")
							+ " " + tCustomerInfo.getString("LastName");

					CustomerDetail objTenantDetail = new CustomerDetail();
					objTenantDetail.setUnitId(UnitId);
					objTenantDetail.setUnitCode(UnitCode);

					objTenantDetail.setCustomerId(CustomerId);
					objTenantDetail.setFirstName(FirstName);
					objTenantDetail.setLastName(LastName);
					objTenantDetail.setTenantName(TenantName);
					objTenantDetail.setMobileNumber(MobileNumber);
					objTenantDetail.setEmailId(EmailId);

					results.add(objTenantDetail);

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return results;
		}

	}
	public void SpeechSearch()
	{
		Intent intent = new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

		try {
			startActivityForResult(intent, RESULT_SPEECH);
			inputSearch.setText("");
		} catch (ActivityNotFoundException a) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Ops! Your device doesn't support Speech to Text",
					Toast.LENGTH_SHORT);
			t.show();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				inputSearch.setText(text.get(0));
			}
			break;
		}

		}
	}

	private void ShowTextToSpeekSettigns() {
		try {
			Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
			startActivity(languageIntent);
		} catch (ActivityNotFoundException a) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Ops! Your device doesn't support Speech to Text",
					Toast.LENGTH_SHORT);
			t.show();
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.activity_back, menu);
		menu.add(1, 1, 0, "Back").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(1, 2, 1, "Detail").setIcon(android.R.drawable.ic_dialog_info);
		menu.add(2, 3, 2, "Voice").setIcon(android.R.drawable.ic_btn_speak_now);
		menu.add(2, 4, 3, "Settings").setIcon(android.R.drawable.ic_menu_preferences);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case 1:
			finish();
			return true;

		case 2:
			initiatePopupWindow();
			return true;
			
		case 3:
			SpeechSearch();
			return true;

		case 4:
			ShowTextToSpeekSettigns();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}