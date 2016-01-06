package com.gabri.coach.application2;


import com.gabri.coach.application2.database.CoachDBAdapter;
import com.rodri.coach.application2.R;
import java.util.ArrayList;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class EntrenamientosEjerciciosCoach extends ListActivity
{
	private CoachDBAdapter mDbHelper; 
	private ArrayList<Object> selectedItems = new ArrayList<Object>();
	private ListView listView = null;
	
	 @Override
	public void onCreate(Bundle savedInstanceState) 
	{
	        super.onCreate(savedInstanceState);

	        mDbHelper = new CoachDBAdapter(this);
	        mDbHelper.open();
	        recogerDatosEjercicios();
	 }
	 
	 private void recogerDatosEjercicios()
	 {
		 
		Cursor notesCursor = mDbHelper.getAllEjercicios();
	    startManagingCursor(notesCursor);
	    	
	    // Create an array to specify the fields we want to display in the list (only TITLE)
	    String[] from = new String[]{CoachDBAdapter.KEY_EJERCICIO_NOMBRE};

	    // and an array of the fields we want to bind those fields to (in this case just text1)
	    int[] to = new int[]{android.R.id.text1};
	       
	    setListAdapter(new ArrayAdapter<String>(this,
	           android.R.layout.simple_list_item_multiple_choice, from));
      
	        // Now create a simple cursor adapter and set it to display
	    SimpleCursorAdapter notes = new SimpleCursorAdapter(this, 
	    		android.R.layout.simple_list_item_multiple_choice, notesCursor, from, to);
	        setListAdapter(notes);
	        
	        
	        this.listView = getListView();

	        listView.setItemsCanFocus(false);
	        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	        
	 }
	 
	 	@Override
	 	public boolean onCreateOptionsMenu(Menu menu) 
	    {
	 		getMenuInflater().inflate(R.menu.listmenu_entr_ej, menu);
	 		return true;
	 	}
	  
	  @Override
	  public boolean onMenuItemSelected(int featureId, MenuItem item) 
	  {
		  	int itemId = item.getItemId();
			if (itemId == R.id.menu_guardar) {
				SavedItems();
				return true;
			}

	        return super.onMenuItemSelected(featureId, item);
	   }
	 
	 
	 private void SavedItems()
	 {
		 int count = this.listView.getAdapter().getCount();
		 
		 for(int i=0; i < count; i++)
		 {
			 if(this.listView.isItemChecked(i))
			 {
				 selectedItems.add(this.listView.getItemAtPosition(i));
			 }
		 }
	 }
}
