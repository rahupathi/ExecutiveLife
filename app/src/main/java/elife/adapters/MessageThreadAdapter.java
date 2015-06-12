package elife.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taeligstatus.R;

import elife.entity.MessageThread;

public class MessageThreadAdapter extends ArrayAdapter<MessageThread> {

	private LinearLayout wrapper;
	private List<MessageThread> MessageThreadList=new ArrayList<MessageThread>();
	private TextView txtDescription;
	private TextView txtMe;
	private TextView txtFund;
	private TextView txtMessageInfo;
	
	public MessageThreadAdapter(Context context, int textViewResourceId)
	{
    	super(context, textViewResourceId);
    }
    
	@Override
	public void add(MessageThread object) {
    	MessageThreadList.add(object);
    	super.add(object);
	}
 
	
	@Override
	public int getCount() {
		return this.MessageThreadList.size();
	}

	@Override
	public MessageThread getItem(int index) {
		return MessageThreadList.get(index);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		StringBuilder sb=new StringBuilder();	  
		
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.member_message_thread, parent, false);
		}
		wrapper = (LinearLayout) row.findViewById(R.id.wrapper);
		txtDescription=(TextView)row.findViewById(R.id.txtThreadDescrption);
		txtMe=(TextView)row.findViewById(R.id.txtMe);
		txtFund=(TextView)row.findViewById(R.id.txtFund);
		txtMessageInfo=(TextView)row.findViewById(R.id.txtMessageInfo);
		
		if (MessageThreadList.get(position).getStatus()=="1")
		{
		  sb.append(" ");
		  sb.append(MessageThreadList.get(position).getMessages());
		  txtDescription.setBackgroundResource(R.drawable.bubble_yellow);
		  txtMe.setVisibility(View.VISIBLE);
		  txtMe.setGravity(Gravity.LEFT);
		  txtFund.setVisibility(View.GONE);
		  txtMessageInfo.setText("Posted On: " + MessageThreadList.get(position).getPostDate());
		  wrapper.setGravity(Gravity.LEFT);
	  }
	  else
	  {
		  sb.append(" ");
		  sb.append(MessageThreadList.get(position).getMessages());
		  txtDescription.setBackgroundResource(R.drawable.bubble_green);
		  txtMe.setVisibility(View.GONE);
		  txtFund.setVisibility(View.VISIBLE);
		  txtFund.setGravity(Gravity.RIGHT);
		  wrapper.setGravity(Gravity.RIGHT);
		  txtMessageInfo.setText("Received On: " + MessageThreadList.get(position).getReplyDate());
	  }
	  txtDescription.setText(sb.toString());
	  sb=null;
	  return row;
	}


}
