package com.gabri.coach.application2;


import com.rodri.coach.application2.R;


import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.gabri.coach.application2.database.CoachDBAdapter;



public class EquiposCoach extends SherlockListActivity 
{
	private CoachDBAdapter equipoDbHelper;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST;
	private static final int UPDATE_ID = Menu.FIRST + 1;
	
	String vacio ="";
	//private Cursor cursor;
    //private String equipoId;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	setTheme(CoachActivity.TEMA);
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.equipos_lista);
    	
    	
    	ActionBar ab = getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
        ab.setHomeButtonEnabled(true);
        
    	
    	/* Abrimos la base de datos para obtener los equipos */
    	this.getListView().setDividerHeight(2);
    	equipoDbHelper = new CoachDBAdapter(this);
    	equipoDbHelper.open();
    	mostrarEquipos();
    	registerForContextMenu(getListView());	
    		
    }
    
   
	private void mostrarEquipos()
    {
    	Cursor todoCursor = equipoDbHelper.fetchAllEquipos();
    	
    	startManagingCursor(todoCursor);
    	
    	
    	// Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]
        		{
        		CoachDBAdapter.KEY_LOGO,
        		CoachDBAdapter.KEY_NOMBRE, 
        		CoachDBAdapter.KEY_DIVISION
        		};
     
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]
        		{
        		R.id.ImageView01,
        		R.id.TextViewEquipo, 
        		R.id.TextViewDivision
        		};
    	
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter todos = 
            new SimpleCursorAdapter(this, R.layout.equipo_row, todoCursor, from, to);
        setListAdapter(todos); 	
    	
    }
    
    @Override
 	public boolean onCreateOptionsMenu(Menu menu) 
    {
        
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
        
 	}
        
    @Override
 	public boolean onOptionsItemSelected(MenuItem item) 
    {
 		int itemId = item.getItemId();
		if (itemId == R.id.menu_insertar) {
			crearEquipo();
			return true;
		} else if (itemId == android.R.id.home) {
			Intent intent = new Intent(this, CoachActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
 		return super.onOptionsItemSelected(item);
 	}
    
    
 
 	private void crearEquipo() 
 	{
 		Intent i = new Intent(this, EquiposEditCoach.class);
 		startActivityForResult(i, ACTIVITY_CREATE);
 	}
 	
 	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	        ContextMenuInfo menuInfo) 
	{
	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, DELETE_ID, 0, R.string.menu_delete_equipo);
	    menu.add(0, UPDATE_ID, 1, R.string.menu_update_equipo);
	}
 	
 	@Override
 	public boolean onContextItemSelected(android.view.MenuItem item) 
    {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

 		switch (item.getItemId()) 
 		{
 		case DELETE_ID:
 			eliminarEquipo((int)info.id);
 			//equipoDbHelper.deleteEquipo(info.id);
 			mostrarEquipos();
 			return true;
 			
 		case UPDATE_ID:
        	Intent i = new Intent(EquiposCoach.this, EquiposEditCoach.class);
            i.putExtra(CoachDBAdapter.KEY_ROWID, info.id);
        	i.putExtra("equipo_seleccionado", info.id);
        	i.putExtra("modo", ACTIVITY_EDIT);
            startActivityForResult(i, ACTIVITY_EDIT);
        	return true;
 		}
 		return super.onContextItemSelected(item);
 	}

 	  @Override
 	 protected void onListItemClick(ListView l, View v, int position, long id)
 	 {
 	    super.onListItemClick(l, v, position, id);
 	    
 	    Intent i = new Intent(this, EntrenamientosCoach.class);
 	    i.putExtra("equipo_seleccionado", id);
 	    startActivity(i);	        
 	  }
 	  
 	  
 	 private void eliminarEquipo(int id_equipo)
 	{
 		// Objetos. 
 		AlertDialog.Builder mensaje_dialogo = new AlertDialog.Builder(this);  	
 		
 		// Variables.
 		final int v_id_equipo = id_equipo;
 		    
 		mensaje_dialogo.setTitle("Importante");  
 		mensaje_dialogo.setMessage("Estas seguro de eliminar este equipo?");            
 		mensaje_dialogo.setCancelable(false);  
 		mensaje_dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() 
 		{  
 	        public void onClick(DialogInterface dialogo1, int id) 
 	        {  
 	            try
 	            {    		
 	            	equipoDbHelper.deleteEquipo(v_id_equipo);    		    
 	    		    mostrarEquipos();
 	    		}
 	            catch(Exception e)
 	            {
 	    		     Toast.makeText(getApplicationContext(), "Error al eliminar!!!", Toast.LENGTH_SHORT).show();
 	    			 e.printStackTrace();
 	    		}
 	            finally
 	    		{	    			
 	            	equipoDbHelper.close();
 	    	    }
 	        }  
 	    });  
 		mensaje_dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() 
 		{  
 	        public void onClick(DialogInterface dialogo1, int id)
 	        {  
 	        	mostrarEquipos();
 	        }  
 	    });            
 		mensaje_dialogo.show();  
 	}
}
