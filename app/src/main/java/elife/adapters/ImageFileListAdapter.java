package elife.adapters;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.taeligstatus.R;


import elife.entity.ImageDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageFileListAdapter extends BaseAdapter {

	// Declare Variables
	private Context mContext;
	private LayoutInflater inflater;
	private List<ImageDetail> imagelist = null;
	private ArrayList<ImageDetail> arraylist;

	public ImageFileListAdapter(Context context,
			List<ImageDetail> pImageList) {
		mContext = context;
		this.imagelist = pImageList;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<ImageDetail>();
		this.arraylist.addAll(pImageList);
	}


	public class ViewHolder {
        CheckBox chkDelete;
        TextView txtFileName;
        TextView txtFileDate;
        ImageView imgTaken;
    }

	@Override
	public int getCount() {
		return imagelist.size();
	}

	@Override
	public ImageDetail getItem(int position) {
		return imagelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		
		if (view == null) {
			holder = new ViewHolder();
			
			view = inflater.inflate(R.layout.image_detail, null);
			//holder.chkDelete = (CheckBox) view.findViewById(R.id.chkDelete);
			holder.txtFileName = (TextView) view.findViewById(R.id.txtFileName);
			//holder.txtFileDate = (TextView) view.findViewById(R.id.txtFileDate);
			//holder.imgTaken=(ImageView)view.findViewById(R.id.imgAttacment);
            
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		//String sImagePath=Environment.getExternalStorageDirectory()+File.separator +"ELife";
		//File imgFile = new  File(sImagePath + File.separator + imagelist.get(position).getFileName());
		//if(imgFile.exists()){
		    //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    //holder.imgTaken.setImageBitmap(myBitmap);
		//}
		holder.txtFileName.setText(imagelist.get(position).getFileName());
		//holder.txtFileDate.setText(imagelist.get(position).getFileDate());

	    return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		imagelist.clear();
		if (charText.length() == 0) {
			imagelist.addAll(arraylist);
		} else {
			for (ImageDetail wp : arraylist) {
				if (wp.getFileName().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					imagelist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
