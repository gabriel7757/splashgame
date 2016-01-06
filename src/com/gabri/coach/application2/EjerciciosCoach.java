package com.gabri.coach.application2;

import com.actionbarsherlock.view.MenuInflater;
import com.rodri.coach.application2.R;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.gabri.coach.application2.database.CoachDBAdapter;



public class EjerciciosCoach extends SherlockListActivity
{
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int DELETE_ID = Menu.FIRST;
    private static final int UPDATE_ID = Menu.FIRST + 1;
	
	private CoachDBAdapter ejerciciosDbHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(CoachActivity.TEMA);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ejercicios_lista);
		
		ActionBar ab = getSupportActionBar();
		
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
		
		ab.setDisplayHomeAsUpEnabled(true);
		
		//Abrimos la base de datos de ejercicios
		ejerciciosDbHelper = new CoachDBAdapter(this);
		ejerciciosDbHelper.open();
		mostrarEjercicios();
		registerForContextMenu(getListView());		
	}
	
	private void mostrarEjercicios()
    {
    	Cursor notesCursor = ejerciciosDbHelper.getAllEjercicios();
    	startManagingCursor(notesCursor);
    	
    	// Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{CoachDBAdapter.KEY_EJERCICIO_NOMBRE};

        //int[] to = new int[]{R.id.texto};

        // Now create a simple cursor adapter and set it to display
        //SimpleCursorAdapter notes = 
        //    new SimpleCursorAdapter(this, R.layout.ejercicios_row, notesCursor, from, to);
        //setListAdapter(notes);
    	
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
        switch(item.getItemId()) 
        {
            case R.id.menu_insertar_ejercicio:
                crearEjercicio();
                return true;
                
            case android.R.id.home:
                Intent intent = new Intent(this, CoachActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
 	public boolean onCreateOptionsMenu(Menu menu) 
    {
    	MenuInflater inflater = getSupportMenuInflater();
    	inflater.inflate(R.menu.listmenu_ej, menu);
 		return true;
 	}
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) 
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete_ejercicio);
        menu.add(0, UPDATE_ID, 1, R.string.menu_update_ejercicio);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) 
    {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	
    	switch(item.getItemId()) 
    	{
            case DELETE_ID:
                ejerciciosDbHelper.borrarEjercicio(info.id);
                mostrarEjercicios();
                return true;
            case UPDATE_ID:
            	Intent i = new Intent(this, PizarraCoach.class);
                i.putExtra(CoachDBAdapter.KEY_ROWID_EJ, info.id);
                startActivityForResult(i, ACTIVITY_EDIT);
            	return true;
        }
        return super.onContextItemSelected(item);
    }
    
    //NUEVO EJERCICIO
    private void crearEjercicio()
    {
    	Intent i = new Intent(this, PizarraCoach.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    
}
