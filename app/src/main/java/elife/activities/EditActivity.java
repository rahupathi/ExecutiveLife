package elife.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
 
public class EditActivity extends ListActivity {
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListContent));   
    }
    private static String[] mListContent={"Item 1", "Item 2", "Item 3","Item 1", "Item 2", "Item 3","Item 1", "Item 2", "Item 3","Item 1", "Item 2", "Item 3"};
}