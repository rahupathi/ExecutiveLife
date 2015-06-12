package elife.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.taeligstatus.R;

import elife.adapters.ImageFileListAdapter;
import elife.entity.CustomerDetail;
import elife.entity.Global;
import elife.entity.ImageDetail;
import elife.entity.MailContent;
import elife.entity.MailSender;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CameraActivity extends Activity {

	private static final int CAMERA_REQUEST = 1888;
	//private String mUnitId;
	private String mUnitCode;
	private ListView lvImageList;
	private ImageFileListAdapter adapter;
	private ArrayList<ImageDetail> arraylist = new ArrayList<ImageDetail>();
	private ArrayList<CustomerDetail> arrCustomer = new ArrayList<CustomerDetail>();
	private ProgressDialog SendMaildialog;
	private MailContent objMailContent = new MailContent();
	private boolean isSDPresent;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {

		Bundle j = getIntent().getExtras();

		arrCustomer = (ArrayList<CustomerDetail>) getIntent()
				.getSerializableExtra("customerList");

		objMailContent = (MailContent) getIntent().getSerializableExtra("mail");

		//mUnitId = (j.getString("UnitId").toString());
		mUnitCode = (j.getString("UnitCode"));

		super.onCreate(savedInstanceState);
		setContentView(R.layout.attachment_list);

		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.parseColor("#305B07"));

		isSDPresent = android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

		Global.SDCardImagePath = this.getApplicationContext().getFilesDir()
				.getPath()
				+ File.separator + "ELife";

		if (!isSDPresent) {
			Global.SDCardImagePath = this.getApplicationContext().getFilesDir()
					.getPath()
					+ File.separator + "ELife";
			Toast.makeText(CameraActivity.this, "SD Card not available",
					Toast.LENGTH_LONG).show();
		}

		// imageView = (ImageView) this.findViewById(R.id.imgCaptured);
		DoLoadList();

		/* take pictures for attachment */
		this.findViewById(R.id.btnTakePictures).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent cameraIntent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(cameraIntent, CAMERA_REQUEST);
					}
				});

		/* send mail with attachments */
		this.findViewById(R.id.btnSendmailWithAttchment).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						SendEmail();
					}
				});

		// ArrayList<String> myList = new ArrayList<String>();

	}

	private void DoLoadList() {
		this.LoadFileList();
		lvImageList = (ListView) findViewById(R.id.lvCapturedImgeList);
		adapter = new ImageFileListAdapter(CameraActivity.this, arraylist);
		lvImageList.setAdapter(adapter);

		// Set View selected Image
		lvImageList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ViewSelectedImage(position);
			}
		});

		// Delete selected Image
		this.findViewById(R.id.btnDeleteImages).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						if (arraylist.size() == 0) {
							Toast.makeText(CameraActivity.this,
									"File(s) not found to delete",
									Toast.LENGTH_LONG).show();
							return;
						}

						if (IsFileSelcted() == false) {

							Toast.makeText(CameraActivity.this,
									"Select file to delete", Toast.LENGTH_LONG)
									.show();

						} else {

							final Builder builder = new Builder(
									CameraActivity.this);
							builder.setTitle("Executive Life");
							builder.setMessage("Are you sure,Want to delete?");
							builder.setPositiveButton(android.R.string.ok,
									new OnClickListener() {
										@Override
										public void onClick(
												final DialogInterface dialog,
												final int which) {
											dialog.dismiss();
											DeleteSelectedImage();
										}
									});
							builder.setNegativeButton(android.R.string.cancel,
									new OnClickListener() {
										@Override
										public void onClick(
												final DialogInterface dialog,
												final int which) {
											dialog.dismiss();
										}
									});
							final AlertDialog dialog = builder.create();
							dialog.show();

						}

					}
				});

	}

	private boolean IsFileSelcted() {
		Boolean isSelected = false;
		for (int i = 0; i < lvImageList.getChildCount(); i++) {
			View vt = lvImageList.getChildAt(i);
			CheckBox chk = (CheckBox) vt.findViewById(R.id.chkFileAttach);
			if (chk.isChecked()) {
				isSelected = true;
			}
		}
		return isSelected;
	}

	private void DeleteSelectedImage() {

		Boolean IsDeleted = false;

		try {

			for (int i = 0; i < lvImageList.getChildCount(); i++) {
				View vt = lvImageList.getChildAt(i);
				CheckBox chk = (CheckBox) vt.findViewById(R.id.chkFileAttach);
				if (chk.isChecked()) {
					ImageDetail ObjImageDetailInfo = (ImageDetail) lvImageList
							.getItemAtPosition(i);
					if (ObjImageDetailInfo != null) {

						String sFile = Global.SDCardImagePath + File.separator
								+ ObjImageDetailInfo.getFileName();

						File objfile = new File(sFile);

						if (objfile.exists()) {
							// Toast.makeText(CameraActivity.this, sFile,
							// Toast.LENGTH_LONG).show();
							if (objfile.delete()) {
								IsDeleted = true;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			Toast.makeText(CameraActivity.this, ex.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		if (IsDeleted) {
			AlertDialog alert = new AlertDialog.Builder(CameraActivity.this)
					.create();
			alert.setTitle("ExecutiveLife");
			alert.setMessage("File(s) deleted successfully");
			alert.setCancelMessage(null);
			alert.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					arraylist = new ArrayList<ImageDetail>();
					LoadFileList();
					lvImageList = (ListView) findViewById(R.id.lvCapturedImgeList);
					adapter = new ImageFileListAdapter(CameraActivity.this,
							arraylist);
					lvImageList.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			});
			alert.show();
		}

	}

	private void ViewSelectedImage(int position) {
		ImageDetail ObjImageDetailInfo = (ImageDetail) lvImageList
				.getItemAtPosition(position);

		if (ObjImageDetailInfo != null) {
			try {

				Bundle b = new Bundle();
				b.putString("FileName", ObjImageDetailInfo.getFileName()
						.toString());

				Intent i = new Intent(CameraActivity.this,
						ImageViewActivity.class);
				i.putExtras(b);
				startActivity(i);

			} catch (Exception ex) {
				Toast.makeText(CameraActivity.this, ex.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private void LoadFileList() {
		String sFolder = Global.SDCardImagePath;
		File file = new File(sFolder);
		if (file.exists()) {
			File list[] = file.listFiles();
			SimpleDateFormat sdf;
			for (int i = 0; i < list.length; i++) {
				String[] separated = list[i].getName().split("-");
				if (separated.length > 0) {
					if (separated[0].equalsIgnoreCase(mUnitCode)) {
						Date lastModDate = new Date(list[i].lastModified());
						ImageDetail tImageDet = new ImageDetail();
						tImageDet.setFileName(list[i].getName());
						sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
						tImageDet.setFileDate(sdf.format(lastModDate));
						// myList.add( list[i].getName());
						arraylist.add(tImageDet);
					}
				}
			}
		}
	}

	public static float megabytesAvailable(File f) {
		StatFs stat = new StatFs(f.getPath());
		long bytesAvailable = (long) stat.getBlockSize()
				* (long) stat.getAvailableBlocks();
		return bytesAvailable / (1024.f * 1024.f);
	}

	protected void SendEmail() {

		final MailSender sender = new MailSender("rahupathi@gmail.com",
				"nicelove123");
		final String fromId = "rahupathi@gmail.com";

		new AsyncTask<Void, Void, Boolean>() {
			String vException;

			@Override
			public Boolean doInBackground(Void... arg) {
				try {
					ArrayList<String> filesDetail = new ArrayList<String>();
					for (int i = 0; i < arrCustomer.size(); i++) {
						CustomerDetail ObjCustomerDetailInfo = (CustomerDetail) arrCustomer
								.get(i);
						if (ObjCustomerDetailInfo != null) {

							/* Attached selected Images with mail content */
							for (int j = 0; j < lvImageList.getChildCount(); j++) {

								View vt = lvImageList.getChildAt(j);

								CheckBox chk = (CheckBox) vt
										.findViewById(R.id.chkFileAttach);

								if (chk.isChecked()) {
									ImageDetail ObjImageList = (ImageDetail) lvImageList
											.getItemAtPosition(j);

									String filename = Global.SDCardImagePath
											+ File.separator
											+ ObjImageList.getFileName();

									File ofile = new File(filename);
									if (ofile.exists()) {
										filesDetail.add(filename);
										// sender.addAttachment(filename, "");
									}
								}
							}

							/* Send Mail */
							if (ObjCustomerDetailInfo.getEmailID() != "")

								sender.sendMail(
										objMailContent.getMailSubject(),
										"Dear <b>"
												+ ObjCustomerDetailInfo
														.getTenantName()
												+ "</b><br><br>&nbsp;&nbsp;"
												+ objMailContent
														.getMailContent()
												+ "<br><br>Thanks<br><b>ExecutiveLife</b><br>",
										objMailContent.getFromMailId(),
										ObjCustomerDetailInfo.getEmailID(),
										filesDetail);
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

					AlertDialog alert = new AlertDialog.Builder(
							CameraActivity.this).create();
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
						Toast.makeText(CameraActivity.this, vException,
								Toast.LENGTH_LONG).show();
				}
			}

			@Override
			protected void onPreExecute() {
				SendMaildialog = ProgressDialog.show(CameraActivity.this, "",
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAMERA_REQUEST) {

			Bitmap photo = (Bitmap) data.getExtras().get("data");
			// imageView.setImageBitmap(photo);

			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
			String sFileName = mUnitCode.toUpperCase() + "-"
					+ sdf.format(today) + ".PNG";

			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			String sFolder = Global.SDCardImagePath;

			boolean success = true;
			File ofile = new File(sFolder);
			if (!ofile.exists()) {
				success = ofile.mkdir();
			}
			if (success) {
				File file = new File(ofile, sFileName);
				try {

					file.createNewFile();
					FileOutputStream fo = null;// new FileOutputStream(file);

					if (isSDPresent) {
						fo = new FileOutputStream(file);
					} else {
						fo = openFileOutput(sFileName,
								getApplicationContext().MODE_PRIVATE);
					}

					photo.compress(Bitmap.CompressFormat.PNG, 100, fo);
					// 5
					fo.write(bytes.toByteArray());
					Toast.makeText(CameraActivity.this,
							"Saved : " + sFolder + File.separator + sFileName,
							Toast.LENGTH_LONG).show();
					fo.close();

					finish();
					startActivity(this.getIntent());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(CameraActivity.this,
							"Unable to save the file", Toast.LENGTH_LONG)
							.show();
				}
			}
		}

	}

}