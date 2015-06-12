package elife.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taeligstatus.R;

import elife.entity.MessageDetail;

public class MemberMessageAdapter extends BaseAdapter {
	 
	 private ArrayList<MessageDetail> MessageDetailList;
	// private int[] colors = new int[] { 0x30ffffff, 0x30808080 };
	 private int[] altColor=new int[] {0x30124f83,0x30ffffff};
	 
	 private LayoutInflater mInflater;

	 public MemberMessageAdapter(Context context,ArrayList<MessageDetail> results)
	 {
		 MessageDetailList = results;
		 mInflater = LayoutInflater.from(context);
    }
	 
	@Override
	public int getCount() {
		return MessageDetailList.size();
	}

	@Override
	public Object getItem(int position) {
		return MessageDetailList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null){
			convertView = mInflater.inflate(R.layout.member_message_detail, null);
			
			holder = new ViewHolder();			
			holder.txtMessageDate = (TextView) convertView.findViewById(R.id.MessageDate);
			holder.txtMessages = (TextView) convertView.findViewById(R.id.txtMessages);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            convertView.setTag(holder);
		}else {
           holder = (ViewHolder) convertView.getTag();
       }
	   holder.txtMessageDate.setText("Posted On: " + MessageDetailList.get(position).getMessageDate());
	   holder.txtMessages.setText("Subject: " + MessageDetailList.get(position).getSubject());
	   holder.txtStatus.setText(MessageDetailList.get(position).getStatus()); 
	   int colorPos = position % altColor.length;
	   convertView.setBackgroundColor(altColor[colorPos]);

	  	return convertView;
	}
	static class ViewHolder {
       
       TextView txtMessageDate;
       TextView txtMessages;
       TextView txtStatus;
   }


}
