package elife.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.taeligstatus.R;

import elife.entity.CustomerDetail;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TenantListAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<CustomerDetail> tenantlist = null;
	private ArrayList<CustomerDetail> arraylist;

	public TenantListAdapter(Context context, List<CustomerDetail> pTenantList) {
		mContext = context;
		this.tenantlist = pTenantList;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<CustomerDetail>();
		this.arraylist.addAll(pTenantList);
	}

	public class ViewHolder {
		CheckBox chkSendMail;
		TextView txtTenantName;
		TextView txtTenantEmail;
	}

	@Override
	public int getCount() {
		return tenantlist.size();
	}

	@Override
	public CustomerDetail getItem(int position) {
		return tenantlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();

			view = inflater.inflate(R.layout.tenant_detail, null);
			holder.chkSendMail = (CheckBox) view.findViewById(R.id.chkSendMail);
			holder.txtTenantName = (TextView) view
					.findViewById(R.id.txtTenantName);
			// holder.txtTenantEmail = (TextView)
			// view.findViewById(R.id.txtTenantEmail);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		//String sMobile = "";
		//if (tenantlist.get(position).getMobileNumber().toString() != "") {
		//	sMobile = "Mobile:" + tenantlist.get(position).getMobileNumber();
		//} else {
		//	sMobile = "Mobile:N/A";
		//}
		
		// holder.chkSendMail.setText(tenantlist.get(position).getTenantName());
		// holder.txtTenantEmail.setText(tenantlist.get(position).getEmailID());
		holder.txtTenantName.setText(tenantlist.get(position).getTenantName());

		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		tenantlist.clear();
		if (charText.length() == 0) {
			tenantlist.addAll(arraylist);
		} else {
			for (CustomerDetail wp : arraylist) {
				if (wp.getTenantName().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					tenantlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
