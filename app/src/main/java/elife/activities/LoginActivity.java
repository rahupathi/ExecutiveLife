package elife.activities;

import org.apache.http.HttpResponse;
//import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.taeligstatus.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import elife.entity.*;

public class LoginActivity extends Activity {

	private UserLoginTask mAuthTask = null;
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	public static int parseColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.parseColor("#305B07"));

		// Set up the login form.

		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});

	}

	public void setEditTextFocus(boolean isFocused) {
		if (isFocused) {
			mPasswordView.requestFocus();
		}
	}

	public void attemptLogin() {
		// sendNotification();

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		// getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {

			mPasswordView.setError(Html.fromHtml("<font color='red'>"
					+ getString(R.string.error_field_required) + "</font>"));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(Html.fromHtml("<font color='red'>"
					+ getString(R.string.error_invalid_password) + "</font>"));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(Html.fromHtml("<font color='red'>"
					+ getString(R.string.error_field_required) + "</font>"));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			if (mAuthTask != null) {
				return;
			}
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
			// mAuthTask = null;
		}
	}

	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog dialog;
		private String vException = "";
		private String mStaffId = "";
		JSONObject jObj;

		@Override
		protected Boolean doInBackground(Void... params) {

			String Result = "";
			ConnectionDetector cd = new ConnectionDetector(
					getApplicationContext());
			try {
				if (cd.isConnectingToInternet()) {

					HttpParams httpParameters = new BasicHttpParams();

					int timeoutConnection = 40000;

					HttpConnectionParams.setConnectionTimeout(httpParameters,
							timeoutConnection);
					int timeoutSocket = 40000;
					HttpConnectionParams.setSoTimeout(httpParameters,
							timeoutSocket);

					HttpClient client = new DefaultHttpClient(httpParameters);
					client.getParams().getParameter(
							ConnRoutePNames.DEFAULT_PROXY);

					HttpGet httpGet = new HttpGet(Global.SERVICE_URI
							+ "/GetLoginInfo/" + mEmail.toString() + "/"
							+ mPassword.toString());

					httpGet.setHeader("Accept", "application/json");
					httpGet.setHeader("Content-Type", "application/json");
					HttpResponse httpresp = client.execute(httpGet);
					//StatusLine statusLine = httpresp.getStatusLine();
					// int statusCode = statusLine.getStatusCode();

					String result = EntityUtils.toString(httpresp.getEntity());
					jObj = new JSONObject(result);
					Result = jObj.toString();
					if (!TextUtils.isEmpty(Result)) {
						if (!TextUtils.isEmpty(jObj.getString("Message"))) {
							if (jObj.getString("Message").equalsIgnoreCase(
									"NODATA")) {
								vException = "Invalid User ID Or Password";
								return false;
							} else {
								vException = "Server Access Error:"
										+ jObj.getString("ErrorMessage");
								return false;

							}
						} else {
							JSONObject jObjLoginInfo = jObj
									.getJSONObject("LoginInfo");
							if (jObjLoginInfo != null) {
								if (!TextUtils.isEmpty(jObjLoginInfo.getString(
										"StaffId").toString())) {
									mStaffId = jObjLoginInfo.getString(
											"StaffId").toString();
									return true;
								} else {
									vException = "No such staff id found";
									return false;
								}
							} else {
								vException = "UserId does not exists";
								return false;
							}
						}
					} else {
						vException = "Invalid UserID and Login";
						return false;
					}
				} else {
					vException = "Check you Internet Connection";
					return false;
				}
			} catch (Exception e) {
				vException = e.getMessage();
				return false;
			}
		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(LoginActivity.this, "",
					"Signing In. Please wait...", true);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			super.onPostExecute(success);
			if (dialog != null)
				dialog.dismiss();

			if (mAuthTask != null)
				mAuthTask = null;

			if (success) {

				Bundle b = new Bundle();
				b.putString("StaffId", mStaffId);

				/*
				 * Intent i = new Intent(LoginActivity.this,
				 * 
				 * HomeActivity.class); i.putExtras(b); startActivity(i);
				 * finish();
				 */

				Intent intent_name = new Intent();
				intent_name.setClass(getApplicationContext(),
						HomeActivity.class);
				intent_name.putExtras(b);
				startActivity(intent_name);
				finish();
			} else {
				if (!TextUtils.isEmpty(vException))
					Toast.makeText(LoginActivity.this, vException,
							Toast.LENGTH_LONG).show();
			}
		}

		/*
		 * @Override protected void onCancelled() { mAuthTask = null; //
		 * dialog.dismiss(); }
		 */
	}
}
