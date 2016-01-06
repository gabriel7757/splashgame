package com.gabri.coach.application2;


import com.rodri.coach.application2.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.gabri.coach.application2.database.CoachDBAdapter;
import com.gabri.coach.applitacion2.util.ImagenAdapter;


public class JugadoresCoach extends SherlockListActivity
{
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int DELETE_ID = Menu.FIRST;
    private static final int UPDATE_ID = Menu.FIRST + 1;
	private static final String Vacio = "";
    
    public long equipoId;
    
    private CoachDBAdapter jugadorDbHelper;
    
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	setTheme(CoachActivity.TEMA);
	super.onCreate(savedInstanceState);
	setContentView(R.layout.jugadores_lista);
	
	ActionBar ab = getSupportActionBar();
	ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
	ab.setDisplayHomeAsUpEnabled(true);
	
	Bundle extras = getIntent().getExtras();
	equipoId = extras.getLong("equipo_seleccionado");
	
	jugadorDbHelper = new CoachDBAdapter(this);
	jugadorDbHelper.open();
	mostrarJugadores();
	registerForContextMenu(getListView());
}
	
    @SuppressWarnings("deprecation")
	private void mostrarJugadores()
    {
    	Cursor notesCursor = jugadorDbHelper.fetchAllJugadores(equipoId);
    	startManagingCursor(notesCursor);
    	//if( CoachDBAdapter.KEY_RUTA_IMG != Vacio)
    	
        String[] from = new String[]
        		{
    			CoachDBAdapter.KEY_RUTA_IMG,
        		CoachDBAdapter.KEY_NOMBRE_JUG, 
        		CoachDBAdapter.KEY_POSICION, 
        		CoachDBAdapter.KEY_EMAIL_JUG
        		};

        int[] to = new int[]
        		{
        		R.id.thumb_persona,
        		R.id.TextViewNombre, 
        		R.id.TextViewPosicion, 
        		R.id.TextViewEmail
        		};
        
        SimpleCursorAdapter notes = 
                new SimpleCursorAdapter(this, R.layout.fila_jugador, notesCursor, from, to);
            setListAdapter(notes);
    }    	
    	
    	/*else
			extracted(notesCursor);      
    }

	private void extracted(Cursor notesCursor) 
	{
		{
            String[] from = new String[]
            		{
        			
            		CoachDBAdapter.KEY_NOMBRE_JUG, 
            		CoachDBAdapter.KEY_POSICION, 
            		CoachDBAdapter.KEY_EMAIL_JUG
            		};

            int[] to = new int[]
            		{      		
            		R.id.TextViewNombre, 
            		R.id.TextViewPosicion, 
            		R.id.TextViewEmail
            		};	
            
            SimpleCursorAdapter notes = 
                    new SimpleCursorAdapter(this, R.layout.fila_jugador, notesCursor, from, to);
                setListAdapter(notes);
    	}
	}*/
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getSupportMenuInflater().inflate(R.menu.listmenu_jug, menu);
        return true;
	}
	

	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) 
	 {
	 	int itemId = item.getItemId();
		if (itemId == R.id.menu_insertar_jug) {
			crearJugador();
			return true;
		} else if (itemId == android.R.id.home) {
			Intent intent = new Intent(this, EntrenamientosCoach.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
	 		return super.onOptionsItemSelected(item);
	 }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	        ContextMenuInfo menuInfo) 
	{
	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, DELETE_ID, 0, R.string.menu_delete_jugador);
	    menu.add(0, UPDATE_ID, 1, R.string.menu_update_jugador);
	}
	
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) 
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch(item.getItemId()) 
		{
	        case DELETE_ID:
	        	eliminarJugador((int)info.id);
	        	mostrarJugadores();
	            return true;
	        case UPDATE_ID:
	        	Intent i = new Intent(this, JugadoresEditCoach.class);
                i.putExtra(CoachDBAdapter.KEY_ROWID_JUG, info.id);
            	i.putExtra("equipo_seleccionado", equipoId);
            	i.putExtra("modo", ACTIVITY_EDIT);
                startActivityForResult(i, ACTIVITY_EDIT);
	        	return true;
	    }
	    return super.onContextItemSelected(item);
	}
	
	  private void crearJugador()
	    {
	    	Intent i = new Intent(this, JugadoresEditCoach.class);
	    	i.putExtra("equipo_seleccionado", equipoId);
	    	i.putExtra("modo", ACTIVITY_CREATE);
	        startActivityForResult(i, ACTIVITY_CREATE);	       
	    }

	
	private void eliminarJugador(int id_jugador)
	{
		// Objetos. 
		AlertDialog.Builder mensaje_dialogo = new AlertDialog.Builder(this);  	
		
		// Variables.
		final int v_id_jugador = id_jugador;
		    
		mensaje_dialogo.setTitle("Importante");  
		mensaje_dialogo.setMessage("Estas seguro de eliminar este jugador?");            
		mensaje_dialogo.setCancelable(false);  
		mensaje_dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() 
		{  
	        public void onClick(DialogInterface dialogo1, int id) 
	        {  
	            try
	            {    		
	            	jugadorDbHelper.deleteJugador(v_id_jugador);    		    
	    		    mostrarJugadores();
	    		}
	            catch(Exception e)
	            {
	    		     Toast.makeText(getApplicationContext(), "Error al eliminar!!!", Toast.LENGTH_SHORT).show();
	    			 e.printStackTrace();
	    		}
	            finally
	    		{	    			
	    			jugadorDbHelper.close();
	    	    }
	        }  
	    });  
		mensaje_dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() 
		{  
	        public void onClick(DialogInterface dialogo1, int id)
	        {  
	        	mostrarJugadores();
	        }  
	    });            
		mensaje_dialogo.show();  
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		
		mostrarDatos(id);   
	}
	
	private void mostrarDatos(long id)
	   {
	      // Llamamos a la Actividad HipotecaFormulario indicando el modo visualización y el identificador del registro
	      Intent i = new Intent(JugadoresCoach.this, JugadoresDatosCoach.class);
	      
	      Intent mostrarDatos = new Intent(this, JugadoresDatosCoach.class);	
			mostrarDatos.putExtra("jugador_seleccionado", id);	
			
		    startActivityForResult(mostrarDatos, ACTIVITY_EDIT);	  
	   }
	
	/*@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onResume()
	{
		super.onResume();;
	}*/
}
