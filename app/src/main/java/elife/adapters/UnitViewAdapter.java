package elife.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.taeligstatus.R;


import elife.entity.UnitsDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UnitViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<UnitsDetail> Unitlist = null;
	private ArrayList<UnitsDetail> arraylist;

	public UnitViewAdapter(Context context,
			List<UnitsDetail> UnitDetaillist) {
		mContext = context;
		this.Unitlist = UnitDetaillist;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<UnitsDetail>();
		this.arraylist.addAll(Unitlist);
	}


	public class ViewHolder {
        TextView txtUnitCode;
        TextView txtStreet;
        TextView txtSuburbName;
    }

	@Override
	public int getCount() {
		return Unitlist.size();
	}

	@Override
	public UnitsDetail getItem(int position) {
		return Unitlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			
			view = inflater.inflate(R.layout.unit_detail, null);
			holder.txtUnitCode = (TextView) view.findViewById(R.id.txtUnitCode);
            holder.txtStreet = (TextView) view.findViewById(R.id.txtStreetAddress);
            holder.txtSuburbName = (TextView) view.findViewById(R.id.txtSuburbName);
            
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.txtUnitCode.setText(Unitlist.get(position).getUnitCode());
	    //holder.txtBirthDate.setText(MemberDependentList.get(position).getBirthDate());
	    holder.txtStreet.setText(Unitlist.get(position).getStreetAddress());
	    holder.txtSuburbName.setText(Unitlist.get(position).getSuburbNames());
	    
	    //holder.txtBirthDate.setTextColor(Color.WHITE);
	    //holder.txtUnitCode.setTextColor(Color.WHITE);
	   // holder.txtStreet.setTextColor(Color.WHITE);
	   // holder.txtSuburbName.setTextColor(Color.WHITE);

	    // Listen for ListView Item Click
		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		Unitlist.clear();
		if (charText.length() == 0) {
			Unitlist.addAll(arraylist);
		} else {
			for (UnitsDetail wp : arraylist) {
				if (wp.getUnitCode().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					Unitlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
