package com.gabri.coach.application2;

import java.util.Calendar;

import com.rodri.coach.application2.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.gabri.coach.application2.database.CoachDBAdapter;


public class EntrenamientosCoach extends SherlockListActivity
{
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

   
    private static final int DELETE_ID = Menu.FIRST ;
    private static final int UPDATE_ID = Menu.FIRST + 1;
    
    private Long equipoId;
    private TextView mDateDisplay;
	private int mYear;
    private int mMonth;
    private int mDay;
    private CoachDBAdapter entrenamientosDbHelper;
    
	 @Override
	  public void onCreate(Bundle savedInstanceState)
		{
		 	setTheme(CoachActivity.TEMA);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.entrenamientos_lista);
			
			ActionBar ab = getSupportActionBar();
			ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
			ab.setDisplayHomeAsUpEnabled(true);
			
			Bundle extras = getIntent().getExtras();
			equipoId = extras.getLong("equipo_seleccionado");
			
			entrenamientosDbHelper = new CoachDBAdapter(this);
			entrenamientosDbHelper.open();
			mostrarEntrenamientos();
			registerForContextMenu(getListView());
		}
	 
	 private void mostrarEntrenamientos()
		{
			Cursor notesCursor = entrenamientosDbHelper.fetchAllEntrenamientos(equipoId);
	    	startManagingCursor(notesCursor);
	    	
	    	// Create an array to specify the fields we want to display in the list (only TITLE)
	        String[] from = new String[]
	        		{
	        		CoachDBAdapter.KEY_ENTRENAMIENTO_NOMBRE, 
	        		CoachDBAdapter.KEY_ENTRENAMIENTO_FECHA,
	        		CoachDBAdapter.KEY_ENTRENAMIENTO_HORA,
	        		};

	        // and an array of the fields we want to bind those fields to (in this case just text1)
	        int[] to = new int[]
	        		{
	        		R.id.text_lista_entrenamiento, 
	        		R.id.fecha_entrenamiento,
	        		R.id.hora_entrenamiento
	        		};

	        // Now create a simple cursor adapter and set it to display
	        SimpleCursorAdapter notes = 
	            new SimpleCursorAdapter(this, R.layout.entrenamientos_row, notesCursor, from, to);
	        setListAdapter(notes);
		}
	 
	 	@Override
	 	public boolean onCreateOptionsMenu(Menu menu) 
	    {
	 		MenuInflater inflater = getSupportMenuInflater();
	 		inflater.inflate(R.menu.listmenu_entre, menu);
	 		return true;
	 	}
	 	
	 	@Override
	 	public boolean onOptionsItemSelected(MenuItem item) 
	    {
	 		int itemId = item.getItemId();
			if (itemId == R.id.menu_insert_entrenamiento) {
				crearEntrenamiento();
				return true;
			} else if (itemId == R.id.ver_jugadores) {
				VerListaJugadores();
				return true;
			} else if (itemId == R.id.ver_calendario) {
				VerCalendario();
				return true;
			} else if (itemId == android.R.id.home) {
				Intent intent = new Intent(this, EquiposCoach.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			}
	 		
	 		return super.onOptionsItemSelected(item);
	 	}
	    
	   /*Override
	    public boolean onMenuItemSelected(int featureId, MenuItem item)
	    {
	        switch(item.getItemId()) 
	        {
	            case menu_insert_entrenamiento:
	                crearJugador();
	                return true;
	        }

	        return super.onMenuItemSelected(featureId, item);
	    }*/
	    
	    @Override
	    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) 
	    {
	        super.onCreateContextMenu(menu, v, menuInfo);
	        menu.add(0, DELETE_ID, 0, R.string.menu_delete_entrenamiento);
	        menu.add(0, UPDATE_ID, 1, R.string.menu_update_entrenamiento);
	    }

	    @Override
	    public boolean onContextItemSelected(android.view.MenuItem item) 
	    {
	    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    	
	    	switch(item.getItemId()) 
	    	{
	            case DELETE_ID:
	            	//entrenamientosDbHelper.deleteEntrenamiento(info.id);
	            	eliminarEntrenamiento((int)info.id);
	            	mostrarEntrenamientos();
	                return true;
	            case UPDATE_ID:
	            	Intent i = new Intent(this, EntrenamientosEditCoach.class);
	                i.putExtra(CoachDBAdapter.KEY_ROWID_EN, info.id);
	            	i.putExtra("equipo_seleccionado", equipoId);
	            	i.putExtra("modo", ACTIVITY_EDIT);
	                startActivityForResult(i, ACTIVITY_EDIT);
	            	return true;
	        }
	        return super.onContextItemSelected(item);
	    }
	    
	    private void crearEntrenamiento()
	    {
	    	Intent i = new Intent(this, EntrenamientosEditCoach.class);
	    	i.putExtra("equipo_seleccionado", equipoId);
	    	i.putExtra("modo", ACTIVITY_CREATE);
	        startActivityForResult(i, ACTIVITY_CREATE);
	       
	    }
	    private void VerListaJugadores()
	    {
	    	Intent i = new Intent(this, JugadoresCoach.class);
	    	i.putExtra("equipo_seleccionado", equipoId);
	        startActivityForResult(i, ACTIVITY_EDIT);       
	    }
	    
	    private void VerCalendario()
	    {
	      	final Calendar c = Calendar.getInstance();
		    mYear = c.get(Calendar.YEAR);
		    mMonth = c.get(Calendar.MONTH);
		    mDay = c.get(Calendar.DAY_OF_MONTH);
		    
	    	Intent intent = new Intent (this, CalendarView.class);
	    	intent.putExtra("date", c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH));
	    	startActivity(intent);
	    }
	    
		private void cargarDatosFecha()
	    {
	    	// capture our View elements
	        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);


	        // get the current date
	        final Calendar c = Calendar.getInstance();
	        mYear = c.get(Calendar.YEAR);
	        mMonth = c.get(Calendar.MONTH);
	        mDay = c.get(Calendar.DAY_OF_MONTH);

	        // display the current date (this method is below)
	        updateDisplay();
	    }
		
		private void updateDisplay() 
		{
		        mDateDisplay.setText(new StringBuilder()
		                    // Month is 0 based so add 1
		                    .append(mDay).append("-")
		                    .append(mMonth + 1).append("-")
		                    .append(mYear).append(""));
		}
	    
	    
	    @Override
	    protected void onListItemClick(ListView l, View v, int position, long id)
	    {
	    	super.onListItemClick(l, v, position, id);
	        
	        Intent intentEntrenamientosEjercicios = new Intent(this, EntrenamientosEjerciciosCoach.class);
	        startActivity(intentEntrenamientosEjercicios);
	        
	    }

	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
	    {
	        super.onActivityResult(requestCode, resultCode, intent);
	        mostrarEntrenamientos();
	    }
	    
	    private void eliminarEntrenamiento(int id_entrenamiento)
		{
			// Objetos. 
			AlertDialog.Builder mensaje_dialogo = new AlertDialog.Builder(this);  	
			
			// Variables.
			final int v_id_entrenamiento = id_entrenamiento;
			    
			mensaje_dialogo.setTitle("Importante");  
			mensaje_dialogo.setMessage("Estas seguro de eliminar este entrenamiento?");            
			mensaje_dialogo.setCancelable(false);  
			mensaje_dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() 
			{  
		        public void onClick(DialogInterface dialogo1, int id) 
		        {  
		            try
		            {    		
		            	entrenamientosDbHelper.deleteEntrenamiento(v_id_entrenamiento);    		    
		    		    mostrarEntrenamientos();
		    		}
		            catch(Exception e)
		            {
		    		     Toast.makeText(getApplicationContext(), "Error al eliminar!!!", Toast.LENGTH_SHORT).show();
		    			 e.printStackTrace();
		    		}
		            finally
		    		{	    			
		            	entrenamientosDbHelper.close();
		    	    }
		        }  
		    });  
			mensaje_dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() 
			{  
		        public void onClick(DialogInterface dialogo1, int id)
		        {  
		        	mostrarEntrenamientos();
		        }  
		    });            
			mensaje_dialogo.show();  
		}
	    
}
