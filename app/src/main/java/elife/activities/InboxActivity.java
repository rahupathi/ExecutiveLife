package elife.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.app.Activity;
import android.graphics.Color;

import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.taeligstatus.R;

import elife.adapters.InboxListAdapter;
import elife.entity.ConnectionDetector;
import elife.entity.Global;
import elife.entity.InboxDetail;
import elife.entity.InboxMain;

public class InboxActivity extends Activity {
	List<String> groupList;
	List<InboxDetail> childList;
	Map<String, List<InboxDetail>> laptopCollection;
	ExpandableListView expListView;
	GetInboxDetailTask mInboxTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox_expand_list);

		// createGroupList();
		// createCollection();
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.parseColor("#305B07"));

		GetInboxAsAsync();
	}

	public void GetInboxAsAsync() {

		if (mInboxTask != null) {
			return;
		}
		mInboxTask = new GetInboxDetailTask();
		mInboxTask.execute((Void) null);
		mInboxTask = null;
	}

	
	public void loadChild(List<InboxDetail> laptopModels) {
		childList = new ArrayList<InboxDetail>();
		for (InboxDetail model : laptopModels)
			childList.add(model);
	}

	public void setGroupIndicatorToRight() {
		/* Get the screen width */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;

		expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
				- getDipsFromPixel(5));
	}

	// Convert pixel to dip
	public int getDipsFromPixel(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_back, menu);
		return true;
	}

	public class GetInboxDetailTask extends AsyncTask<Void, Void, Boolean> {

		private String sResult = "";
		private String vException = "";
		private ArrayList<InboxDetail> arraylist = new ArrayList<InboxDetail>();
		ArrayList<InboxMain> arrMain = new ArrayList<InboxMain>();
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
						+ "/GetInboxList/1");

				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-Type", "application/json");

				HttpResponse httpresp = client.execute(httpGet);
				//StatusLine statusLine = httpresp.getStatusLine();
				//int statusCode = statusLine.getStatusCode();

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
			dialog = ProgressDialog.show(InboxActivity.this, "",
					"Loading. Please wait...", true);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mInboxTask = null;
			dialog.dismiss();
			if (success) {
				this.LoadGrid(sResult);
			} else {
				if (!TextUtils.isEmpty(vException))
					Toast.makeText(InboxActivity.this, vException,
							Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mInboxTask = null;
			dialog.dismiss();
			// showProgress(false);
		}

		protected void LoadGrid(String sResult) {

			arraylist = new ArrayList<InboxDetail>();
			arraylist = this.GetInboxInfoAsList(sResult);
			arrMain = new ArrayList<InboxMain>();
			arrMain = GetInboxMainInfoAsList(sResult);

			String sReceivedate = "";

			groupList = new ArrayList<String>();
			laptopCollection = new LinkedHashMap<String, List<InboxDetail>>();
			ArrayList<InboxDetail> arrLoadChilds = null;
			for (int i = 0; i < arrMain.size(); i++) {

				int MessageCnt = 0;
				try {
					MessageCnt = Integer.parseInt(arrMain.get(i)
							.getMessageCount());
				} catch (NumberFormatException nfe) {

				}

				if (MessageCnt > 0) {
					arrLoadChilds = new ArrayList<InboxDetail>();
					// int k = 0;
					for (int j = 0; j < arraylist.size(); j++) {
						if (arrMain
								.get(i)
								.getReceivedOn()
								.toString()
								.equalsIgnoreCase(
										arraylist.get(j).getReceivedDate())
								|| arrMain.get(i).getReceivedOn().toString()
										.equalsIgnoreCase("Old Messages")) {
							arrLoadChilds.add(arraylist.get(j));
						}

					}
					sReceivedate = arrMain.get(i).getReceivedOn() + "  "
							+ arrMain.get(i).getDayName();
					groupList.add(sReceivedate);
					loadChild(arrLoadChilds);
					laptopCollection.put(sReceivedate, childList);
				}
			}

			expListView = (ExpandableListView) findViewById(R.id.lvInbox);
			expListView.setGroupIndicator(null);
			final InboxListAdapter expListAdapter = new InboxListAdapter(
					InboxActivity.this, groupList, laptopCollection);
			expListView.setAdapter(expListAdapter);

			// setGroupIndicatorToRight();
			if (expListAdapter.getChildrenCount(0) >= 1) {
				expListView.expandGroup(0);
			}

			expListView
					.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
						@Override
						public boolean onGroupClick(ExpandableListView parent,
								View v, int groupPosition, long id) {
							if (groupPosition == 0) {
								return true;
							} else {
								return false;
							}
						}
					});

			expListView.setOnChildClickListener(new OnChildClickListener() {

				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					final InboxDetail selected = (InboxDetail) expListAdapter
							.getChild(groupPosition, childPosition);
					Toast.makeText(getBaseContext(), selected.getSubject(),
							Toast.LENGTH_LONG).show();

					return true;
				}
			});

		}

		protected ArrayList<InboxDetail> GetInboxInfoAsList(
				String inboxListString) {

			ArrayList<InboxDetail> results = new ArrayList<InboxDetail>();

			try {

				JSONObject InboxInfoJsonObject = new JSONObject(inboxListString);
				JSONArray InboxInfoArrayDetials = InboxInfoJsonObject
						.getJSONArray("InboxList");
				for (int i = 0; i < InboxInfoArrayDetials.length(); i++) {

					// Get Each Object from Array
					JSONObject tInboxInfo = InboxInfoArrayDetials
							.getJSONObject(i);
					if (tInboxInfo != null) {

						InboxDetail objInboxDetail = new InboxDetail();

						objInboxDetail.setSubject(tInboxInfo.getString(
								"MailSubject").toString());
						objInboxDetail.setMessageId(tInboxInfo.getString(
								"MailDescription").toString());
						objInboxDetail.setTenantName(tInboxInfo.getString(
								"CustomerName").toString());
						objInboxDetail.setUnitCode(tInboxInfo.getString(
								"UnitCode").toString());
						objInboxDetail.setReceivedDate(tInboxInfo.getString(
								"ReceivedDate").toString());
						objInboxDetail.setReceivedTime(tInboxInfo.getString(
								"ReceivedTime").toString());
						results.add(objInboxDetail);
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return results;
		}

		protected ArrayList<InboxMain> GetInboxMainInfoAsList(
				String inboxListString) {

			ArrayList<InboxMain> results = new ArrayList<InboxMain>();

			try {

				JSONObject InboxInfoJsonObject = new JSONObject(inboxListString);
				JSONArray InboxInfoArrayDetials = InboxInfoJsonObject
						.getJSONArray("ReceivedOnList");
				for (int i = 0; i < InboxInfoArrayDetials.length(); i++) {

					// Get Each Object from Array
					JSONObject tInboxInfo = InboxInfoArrayDetials
							.getJSONObject(i);
					if (tInboxInfo != null) {

						InboxMain objInboxMain = new InboxMain();

						objInboxMain.setReceivedOn(tInboxInfo.getString(
								"ReceivedDate").toString());
						objInboxMain.setMessageCount(tInboxInfo.getString(
								"MessageCount").toString());
						objInboxMain.setDayName(tInboxInfo.getString("DayName")
								.toString());

						results.add(objInboxMain);
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return results;
		}

	}

}