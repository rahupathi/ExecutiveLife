package elife.activities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.taeligstatus.R;
import elife.adapters.TenantListAdapter;
import elife.entity.*;

public class TenantByUnitListActivity extends Activity {

	private GetTenantDetailTask mTenantTask = null;
	private String mUnitId = "";
	private String mUnitCode = "";
	private EditText txtEmailContent, txtEmailSubject;
	private String mMailContent, mMailSubject, mMailIds;
	private ProgressDialog SendMaildialog;
	private ArrayList<CustomerDetail> arraylist = new ArrayList<CustomerDetail>();
	private ListView lView;
	private TenantListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenant_list);

		Bundle j = getIntent().getExtras();
		mUnitId = (j.getString("UnitId"));
		mUnitCode = (j.getString("UnitCode"));

		this.setTitle("ExecutiveLife [Unit: " + mUnitCode + "]");

		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.parseColor("#305B07"));

		GetTenantDetailByUnitId();

		findViewById(R.id.btnAttchAndSend).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						if (!IsValidEntry()) {
							return;
						}

						ArrayList<CustomerDetail> arrCustomer = new ArrayList<CustomerDetail>();
						for (int i = 0; i < lView.getChildCount(); i++) {
							View vt = lView.getChildAt(i);
							CheckBox chk = (CheckBox) vt
									.findViewById(R.id.chkSendMail);
							if (chk.isChecked()) {
								CustomerDetail ObjCustomerDetailInfo = (CustomerDetail) lView
										.getItemAtPosition(i);
								if (isValidEmail(ObjCustomerDetailInfo
										.getEmailID())) {
									arrCustomer.add(ObjCustomerDetailInfo);
								}
							}
						}

						if (arrCustomer.size() == 0) {
							Toast.makeText(TenantByUnitListActivity.this,
									"Please select tenant to send mail",
									Toast.LENGTH_LONG).show();
							return;
						}
						MailContent objMailContent = new MailContent();

						Bundle b = new Bundle();

						objMailContent.setMailContent(mMailContent);
						objMailContent.setMailSubject(mMailSubject);
						objMailContent.setFromMailId("rahupathi@gmail.com");

						b.putSerializable("mail", objMailContent);
						b.putSerializable("customerList", arrCustomer);

						b.putString("UnitId", mUnitId);
						b.putString("UnitCode", mUnitCode);

						Intent i = new Intent(TenantByUnitListActivity.this,
								CameraActivity.class);
						i.putExtras(b);
						startActivity(i);
					}
				});

		findViewById(R.id.btnSendMail).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ConnectionDetector cd = new ConnectionDetector(
								getApplicationContext());
						if (cd.isConnectingToInternet()) {
							if (IsValidEntry()) {
								SendEmail();
							}
						} else {
							Toast.makeText(TenantByUnitListActivity.this,
									"Check your internet conenction",
									Toast.LENGTH_LONG).show();
						}
					}
				});

	}

	protected Boolean IsValidEntry() {

		String isSelected = "NO";

		txtEmailSubject = (EditText) findViewById(R.id.txtMailSubject);
		txtEmailContent = (EditText) findViewById(R.id.txtMailcontent);
		ListView lView = ((ListView) findViewById(R.id.lvTenantList));

		if (lView.getChildCount() == 0) {
			Toast.makeText(TenantByUnitListActivity.this,
					"Tanant detail not found to send Mail", Toast.LENGTH_LONG)
					.show();
			return false;
		}

		txtEmailSubject.setError(null);
		String sEmailSubject = txtEmailSubject.getText().toString();
		if (TextUtils.isEmpty(sEmailSubject)) {
			txtEmailSubject.setError(Html
					.fromHtml("<font color='red'>Enter Subject</font>"));
			txtEmailSubject.requestFocus();
			return false;
		}

		txtEmailContent.setError(null);
		String sEmailContent = txtEmailContent.getText().toString();
		if (TextUtils.isEmpty(sEmailContent)) {
			txtEmailContent.setError(Html
					.fromHtml("<font color='red'>Enter Message</font>"));
			txtEmailContent.requestFocus();
			return false;
		}

		mMailSubject = txtEmailSubject.getText().toString();
		mMailContent = txtEmailContent.getText().toString();

		for (int i = 0; i < lView.getChildCount(); i++) {
			View vt = lView.getChildAt(i);
			CheckBox chk = (CheckBox) vt.findViewById(R.id.chkSendMail);
			if (chk.isChecked()) {
				isSelected = "OK";
				CustomerDetail ObjCustomerDetailInfo = (CustomerDetail) lView
						.getItemAtPosition(i);
				if (isValidEmail(ObjCustomerDetailInfo.getEmailID())) {
					isSelected = "Valid";
				}
			}
		}

		if (isSelected.equalsIgnoreCase("NO")) {
			Toast.makeText(TenantByUnitListActivity.this,
					"Select Tenant to send mail", Toast.LENGTH_LONG).show();
			return false;
		}
		if (isSelected.equalsIgnoreCase("OK")) {
			Toast.makeText(TenantByUnitListActivity.this,
					"Valid Email id(s) not found", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	protected void SendEmail() {
		txtEmailContent = (EditText) findViewById(R.id.txtMailcontent);
		txtEmailSubject = (EditText) findViewById(R.id.txtMailSubject);

		mMailContent = txtEmailContent.getText().toString();
		mMailSubject = txtEmailSubject.getText().toString();

		//String stringEmail[]= new String[lView.getChildCount()];

		mMailIds = "rahupathi@gmail.com";

		final MailSender sender = new MailSender("rahupathi@gmail.com",
				"nicelove123");
		new AsyncTask<Void, Void, Boolean>() {
			String vException = "";

			@Override
			public Boolean doInBackground(Void... arg) {
				try {

					for (int i = 0; i < lView.getChildCount(); i++) {
						View vt = lView.getChildAt(i);
						CheckBox chk = (CheckBox) vt
								.findViewById(R.id.chkSendMail);
						if (chk.isChecked()) {
							CustomerDetail ObjCustomerDetailInfo = (CustomerDetail) lView
									.getItemAtPosition(i);
							if (ObjCustomerDetailInfo != null) {
								mMailContent = "Dear <strong>"
										+ ObjCustomerDetailInfo.getTenantName()
										+ "</strong><br><br>&nbsp;&nbsp;"
										+ mMailContent
										+ "<br><br>Thanks<br><b>ExecutiveLife<b><br>";
								sender.sendMail(mMailSubject, mMailContent,
										mMailIds,
										ObjCustomerDetailInfo.getEmailID(),
										null);
							}
						}
					}

				} catch (Exception e) {
					Log.e("SendMail", e.getMessage(), e);
					vException = e.getMessage();
					return false;
				}
				return true;
			}

			@Override
			protected void onPostExecute(final Boolean success) {
				SendMaildialog.dismiss();
				if (success) {
					// Toast.makeText(TenantByUnitListActivity.this,
					// "Mail sent successfully", Toast.LENGTH_LONG).show();
					AlertDialog alert = new AlertDialog.Builder(
							TenantByUnitListActivity.this).create();
					alert.setTitle("ExecutiveLife");
					alert.setMessage("Mail sent successfully");
					alert.setCancelMessage(null);
					alert.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
									startActivity(new Intent(
											getApplicationContext(),
											UnitListActivity.class));
								}
							});
					alert.show();

				} else {
					if (!TextUtils.isEmpty(vException))
						Toast.makeText(TenantByUnitListActivity.this,
								vException, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			protected void onPreExecute() {
				SendMaildialog = ProgressDialog.show(
						TenantByUnitListActivity.this, "",
						"Sending Mail..Please Wait", true);
			}
		}.execute();

	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	public void GetTenantDetailByUnitId() {

		if (mTenantTask != null) {
			return;
		}

		mTenantTask = new GetTenantDetailTask();
		mTenantTask.execute((Void) null);
		mTenantTask = null;

	}

	public class GetTenantDetailTask extends AsyncTask<Void, Void, Boolean> {
		private String vException = "";
		private String sResult = "";

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			// try {
			// Thread.sleep(2000);
			// String Result = "";
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
							+ "/GetTenantListByUnitId/" + mUnitId.toString());

					httpGet.setHeader("Accept", "application/json");
					httpGet.setHeader("Content-Type", "application/json");

					HttpResponse httpresp = client.execute(httpGet);

					// StatusLine statusLine = httpresp.getStatusLine();
					// int statusCode = statusLine.getStatusCode();

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
			dialog = ProgressDialog.show(TenantByUnitListActivity.this, "",
					"Please wait...", true);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mTenantTask = null;
			dialog.dismiss();
			// showProgress(false);
			if (success) {
				this.LoadGrid(sResult);
			} else {
				if (!TextUtils.isEmpty(vException))
					Toast.makeText(TenantByUnitListActivity.this, vException,
							Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mTenantTask = null;
			dialog.dismiss();
			// showProgress(false);
		}

		protected void LoadGrid(String sResult) {
			arraylist = new ArrayList<CustomerDetail>();
			arraylist = GetCustomerInfoAsList(sResult);

			lView = ((ListView) findViewById(R.id.lvTenantList));
			// adapter= new MemberDependentAdapter(this, CustomerList);

			adapter = new TenantListAdapter(TenantByUnitListActivity.this,
					arraylist);
			lView.setAdapter(adapter);

			lView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					CustomerDetail ObjCustomerDetailInfo = (CustomerDetail) lView
							.getItemAtPosition(position);
					if (ObjCustomerDetailInfo != null) {
						Bundle b = new Bundle();
						b.putString("UnitId", mUnitId);
						b.putString("UnitCode", mUnitCode);
						b.putString("TenantName",
								ObjCustomerDetailInfo.getTenantName());
						b.putString("MobileNo",
								ObjCustomerDetailInfo.getMobileNumber());
						b.putString("EmailId",
								ObjCustomerDetailInfo.getEmailID());

						Intent i = new Intent(TenantByUnitListActivity.this,
								TenantViewActivity.class);
						i.putExtras(b);
						startActivity(i);
					}
				}
			});

		}

		private ArrayList<CustomerDetail> GetCustomerInfoAsList(
				String memberListString) {

			ArrayList<CustomerDetail> results = new ArrayList<CustomerDetail>();

			try {

				JSONObject MemberInfoJsonObject = new JSONObject(
						memberListString);

				JSONArray MemberInfoArrayDetials = MemberInfoJsonObject
						.getJSONArray("CustomerDetail");

				for (int i = 0; i < MemberInfoArrayDetials.length(); i++) {

					// Get Each Object from Array
					JSONObject tCustomerInfo = MemberInfoArrayDetials
							.getJSONObject(i);

					String CustomerId = tCustomerInfo.getString("CustomerId");
					String FirstName = tCustomerInfo.getString("FirstName");
					String LastName = tCustomerInfo.getString("LastName");
					String MobileNumber = tCustomerInfo
							.getString("MobileNumber");
					String EmailId = tCustomerInfo.getString("EmailId");
					String TenantName = tCustomerInfo.getString("FirstName")
							+ " " + tCustomerInfo.getString("LastName");

					CustomerDetail objTenantDetail = new CustomerDetail();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_back, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case 0:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}