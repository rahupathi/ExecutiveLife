package elife.adapters;

import java.util.List;
import java.util.Map;
 
import com.taeligstatus.R;

import elife.entity.InboxDetail;

 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class InboxListAdapter extends BaseExpandableListAdapter {
 
    private Activity context;
    private Map<String, List<InboxDetail>> laptopCollections;
    private List<String> laptops;
 
    public InboxListAdapter(Activity context, List<String> laptops,
            Map<String, List<InboxDetail>> laptopCollections) {
        this.context = context;
        this.laptopCollections = laptopCollections;
        this.laptops = laptops;
    }
 
    public Object getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
    }
 
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        final InboxDetail laptop = (InboxDetail)getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
 
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.inbox_expand_detail, null);
        }
 
        TextView txtSubject = (TextView) convertView.findViewById(R.id.txtSubject);
        TextView txtInboxUnit = (TextView) convertView.findViewById(R.id.txtInboxUnit);
        TextView txtSendBy = (TextView) convertView.findViewById(R.id.txtSendBy);
        TextView txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
        TextView txtReceivedTime = (TextView) convertView.findViewById(R.id.txtReceivedTime);
        
        txtInboxUnit.setText(laptop.getTenantName());
        txtSubject.setText(laptop.getSubject());
        txtMessage.setText(laptop.getMessageId());
        txtSendBy.setText(laptop.getUnitCode());
        txtReceivedTime.setText(laptop.getReceivedTime());
        
        return convertView;

        /*
        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener() {
 
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<String> child =
                                    laptopCollections.get(laptops.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
         */
        
    }
 
    public int getChildrenCount(int groupPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).size();
    }
 
    public Object getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }
 
    public int getGroupCount() {
        return laptops.size();
    }
 
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
    	String laptopName = (String) getGroup(groupPosition);
        
        ImageView image = null;
        
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.inbox_group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.laptop);
        //item.setTypeface(null, Typeface.BOLD);
        
        
        image = (ImageView) convertView.findViewById(R.id.expandableIcon);
        
        if(groupPosition != 0){
        	int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
        	image.setImageResource(imageResourceId);
         
        	image.setVisibility(View.VISIBLE);
        } else {
        	image.setVisibility(View.INVISIBLE);
        }
        
        StringBuilder title = new StringBuilder();
        title.append(laptopName.replace("/",".").toString());
        title.append(" (");
		title.append(getChildrenCount(groupPosition));
		title.append(")");
		
		item.setText(title.toString());
		//item.setText(laptopName);
        
        return convertView;
    }
 
    public boolean hasStableIds() {
        return true;
    }
 
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
