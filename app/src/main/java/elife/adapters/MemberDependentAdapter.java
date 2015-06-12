package elife.adapters;

import com.taeligstatus.R;

import elife.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MemberDependentAdapter extends BaseAdapter {

	Context mContext;
	LayoutInflater inflater;
	private List<CustomerDetail> customerList = null;
	private ArrayList<CustomerDetail> arraylist;

	public MemberDependentAdapter(Context context,
			List<CustomerDetail> pCustomerList) {

		mContext = context;
		this.customerList = pCustomerList;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<CustomerDetail>();
		this.arraylist.addAll(pCustomerList);
	}

	@Override
	public int getCount() {
		return customerList.size();
	}

	@Override
	public CustomerDetail getItem(int position) {
		return customerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();

			view = inflater.inflate(R.layout.member_list, null);

			holder.txtMemberName = (TextView) view
					.findViewById(R.id.memberName);
			holder.txtMobileNo = (TextView) view.findViewById(R.id.txtMobileNo);
			holder.txtEmailId = (TextView) view.findViewById(R.id.txtEmailId);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.txtMemberName
				.setText(customerList.get(position).getTenantName());
		// holder.txtBirthDate.setText(MemberDependentList.get(position).getBirthDate());
		holder.txtMobileNo.setText("✆ "
				+ customerList.get(position).getMobileNumber().toString());
		holder.txtEmailId.setText("✉ "
				+ customerList.get(position).getEmailID().toString()
				+ " [ Unit:"
				+ customerList.get(position).getUnitCode().toString() + "]");

		return view;
	}

	static class ViewHolder {

		TextView txtMemberName;
		// TextView txtBirthDate;
		TextView txtEmailId;
		TextView txtMobileNo;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		customerList.clear();
		if (charText.length() == 0) {
			customerList.addAll(arraylist);
		} else {
			for (CustomerDetail wp : arraylist) {
				if (wp.getTenantName().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					customerList.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
}
