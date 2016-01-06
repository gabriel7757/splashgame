package com.gabri.coach.application2;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.rodri.coach.application2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
//import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import android.widget.ArrayAdapter;
import android.widget.ImageView;
//import android.widget.Spinner;
//import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.widget.Spinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
//import android.net.Uri;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.gabri.coach.application2.database.CoachDBAdapter;
import com.gabri.coach.application2.database.DataBaseHelper;
import com.gabri.coach.applitacion2.util.Mensaje;

public class JugadoresEditCoach extends SherlockActivity
{
	
	private static final int SELECCIONAR_CAMARA=0;
	private static final int SELECCIONAR_GALERIA=1;
	
	
	private long mEquipoId;
	private EditText mNombreText;
	private EditText mApellidosText;
	private EditText mTelefonoText;
	private EditText mEmailText;
	private EditText mPosicionText;
	private EditText mCaracteristicasText;
	private String ruta_imagen;
	private ImageView imagenJugador;
	//private Spinner mPosicion;
	public Button butonLimpiar;
	public Button butonGuardar;
	public Button botonImagenPersona;
	private Mensaje mensaje;
	private Bundle extras;
	private CoachDBAdapter mDbHelper;
	private Long rowIdEquipo;
	private Long mRowIdJug;
	private int modo;
	private int hay_imagen;
	private Spinner mPosicion;
	
	DataBaseHelper db;
	ArrayAdapter<String> adapter;
	// para la imagen del jugador.
	private int SELECCIONAR_IMAGEN = 1;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(CoachActivity.TEMA);
		super.onCreate(savedInstanceState);
		
		db = new DataBaseHelper(JugadoresEditCoach.this); // PARA ACCEDER A DATOS DEL SPINNER
		
		ActionBar ab = getSupportActionBar();		
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);			
		ab.setDisplayHomeAsUpEnabled(true);
		
		
		mDbHelper = new CoachDBAdapter(this); // Acceder a métodos de los jugadores y obtener la info.
    	mDbHelper.open();
    	
		setContentView(R.layout.jugador_edit);
		
		hay_imagen =0 ;
		imagenJugador = (ImageView) findViewById(R.id.imagenJugador);
		mNombreText = (EditText) findViewById(R.id.jugador_nombre);
		mApellidosText = (EditText) findViewById(R.id.jugador_apellidos);
		mEmailText = (EditText) findViewById(R.id.jugador_email);	
		//mPosicionText = (EditText) findViewById(R.id.posi);
		mCaracteristicasText = (EditText) findViewById(R.id.jugador_caracteristicas);
		mTelefonoText = (EditText) findViewById(R.id.jugador_telefono);
			
		mPosicion = (Spinner) findViewById(R.id.posi);
		
		
		loadSpinnerPosiciones();
		
		Button butonLimpiar = (Button) findViewById(R.id.botonLimpiar);
	    Button butonGuardar = (Button) findViewById(R.id.botonGuardar);
	    //Button botonImagenPersona = (Button) findViewById(R.id.botonAgregarImagenPersona);
	    Button addFotoButtonGaleria = (Button) findViewById(R.id.botonAgregarFotoGaleria);
        Button addFotoButtonCamara = (Button) findViewById(R.id.botonAgregarFotoCamara);
	
		mensaje = new Mensaje(getApplicationContext());
		
		
		Bundle extras = getIntent().getExtras();
		rowIdEquipo = extras.getLong("equipo_seleccionado");
		modo = extras.getInt("modo");
		
		mRowIdJug = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(CoachDBAdapter.KEY_ROWID_JUG);
		
		if (mRowIdJug == null) 
		{
			
			if(modo == 0)
			{
				mRowIdJug = null;
			}
			else
			{
				mRowIdJug = extras.getLong(CoachDBAdapter.KEY_ROWID_JUG);
			}			
		}
		
		populateFields();
			
		/*botonImagenPersona.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				ventanaImagen();
			}
		});*/
		
		butonLimpiar.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				limpiarCampos();
			}
		});
		//modo = extras.getInt("modo");
		extras = getIntent().getExtras();
		
		
		/*if(modo==0)
		{
			setTitle(R.string.nuevo_jugador);
		}
		else
		{
			setTitle(R.string.edit_jugador);
		}*/

			
		butonGuardar.setOnClickListener(new View.OnClickListener() 
		{		
			public void onClick(View v) 		
			{
				
				if (verificarCampoNombre() &&  verificarCampoApellidos() && verificarCampoTelefono() && verificarCampoEmail()) 
				{					
					
						setResult(RESULT_OK);
						saveState();
						finish();	
				}		
				else
				{
					if (mNombreText.getText().toString().equals("")) 
					{
						mensaje.mostrarMensajeCorto("Introduzca un nombre!!!");
					}
					if (mApellidosText.getText().toString().equals("")) 
					{
						mensaje.mostrarMensajeCorto("Introduzca los apellidos!!!");				
					}
					if (mPosicion.getContext().toString().equals("")) 
					{
						mensaje.mostrarMensajeCorto("Eliga la posicion");
					}
					if (mTelefonoText.getText().toString().equals("")) 
					{
						mensaje.mostrarMensajeCorto("Introduzca un nº de telefono!!!");
					}
					if (mEmailText.getText().toString().equals("")) 
					{
						mensaje.mostrarMensajeCorto("Introduzca un email!!!");
					}
				}
			}
		});
		
        addFotoButtonGaleria.setOnClickListener(new View.OnClickListener() 
        {

            public void onClick(View view) 
            {	       
            	ventanaImagen();             	
            }

        });
        
        addFotoButtonCamara.setOnClickListener(new View.OnClickListener() 
        {

            public void onClick(View view) 
            {	       
            	CapturarImagen();             	
            }

        });      
}
			
	private void populateFields()
	{
		if (mRowIdJug != null) 
		{
		            Cursor note = mDbHelper.fetchJugador(mRowIdJug);
		            startManagingCursor(note);
		            
		            
		            ruta_imagen = (note.getString(
		            		note.getColumnIndexOrThrow(CoachDBAdapter.KEY_RUTA_IMG)));
	            
		            if(ruta_imagen=="")
		            {
		            	imagenJugador.setImageResource(R.drawable.contact);
		            }
		            else
		            	{
		            	Bitmap bitmap = getBitmap(ruta_imagen);
		            	imagenJugador.setImageBitmap(bitmap);
		            	hay_imagen=1;
		            	}
		            
		           
		            mNombreText.setText(note.getString(
		                    note.getColumnIndexOrThrow(CoachDBAdapter.KEY_NOMBRE_JUG)));
		            mApellidosText.setText(note.getString(
		                    note.getColumnIndexOrThrow(CoachDBAdapter.KEY_APELLIDOS_JUG)));
		            
		            String posicion = note.getString(note
							.getColumnIndexOrThrow(CoachDBAdapter.KEY_POSICION));
		            
		            for (int i=0; i<mPosicion.getCount();i++)
		            {

						String pos = (String) mPosicion.getItemAtPosition(i); 
						Log.e(null, pos +" " + posicion);
						if (pos.equalsIgnoreCase(posicion))
						{
							mPosicion.setSelection(i);
						}
					}
		            
		            mEmailText.setText(note.getString(
		                    note.getColumnIndexOrThrow(CoachDBAdapter.KEY_EMAIL_JUG)));
		            mCaracteristicasText.setText(note.getString(
		                    note.getColumnIndexOrThrow(CoachDBAdapter.KEY_CARACTERISTICAS)));
		            mTelefonoText.setText(note.getString(
		                    note.getColumnIndexOrThrow(CoachDBAdapter.KEY_TLFONO_JUG)));	
		    
		   }
	 }
	
	
	
	@Override
    protected void onSaveInstanceState(Bundle outState) 
	{
        super.onSaveInstanceState(outState);
        //saveState();
        outState.putSerializable(CoachDBAdapter.KEY_ROWID_JUG, mRowIdJug);
    }
	
	/*@Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    */
	
	private void saveState() 
	{
		Toast.makeText(this, ruta_imagen, Toast.LENGTH_SHORT).show();
		ruta_imagen = "";
	        String nombreJugador = mNombreText.getText().toString();
			String apellidosJugador = mApellidosText.getText().toString();	       
	        String email = mEmailText.getText().toString();
	        String datoPosicion = (String) mPosicion.getSelectedItem();
	        String caracteristicas = mCaracteristicasText.getText().toString();
	        String telefono = mTelefonoText.getText().toString();
	        
	        if (mRowIdJug == null) 
	        {
	            long id = mDbHelper.createJugador(rowIdEquipo, ruta_imagen, nombreJugador, apellidosJugador, email, datoPosicion, caracteristicas, telefono);
	            
	            if (id > 0) 
	            {
	                mRowIdJug = id;
	            }
	        } 
	        else 
	        {
	            mDbHelper.updateJugador(mRowIdJug, ruta_imagen, nombreJugador, apellidosJugador, email, datoPosicion, caracteristicas, telefono);
	        }
	}
	
	/**
	 * Metodo privado que abre la ventana.
	 */
	 private void ventanaImagen() 
		{
			try 
			{
				Intent i = new Intent(Intent.ACTION_PICK);
				i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						MediaStore.Images.Media.CONTENT_TYPE);
				this.startActivityForResult(i, SELECCIONAR_GALERIA);
			} 
			catch (Exception e) 
			{
				mensaje.mostrarMensajeCorto("Error al seleccionar imagen: " + e.getMessage());
			}
		}
	    
	    private void CapturarImagen() 
		{
	    	Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
			this.startActivityForResult(i, SELECCIONAR_CAMARA);
		}
	    
	    
	private boolean verificarCampoNombre() 
	{
		if (mNombreText.getText().toString().equals("")) 
		{
			return false;
		}
		return true;
	}
	
	private boolean verificarCampoApellidos() 
	{
		if (mNombreText.getText().toString().equals("")) 
		{
			return false;
		}
		return true;
	}
	
//Comprobar el campo spinner se ha seleccionado
	/*private boolean verificarCampoSpinner() 
	{
		Cursor note = mDbHelper.fetchJugador(mRowIdJug);
		String vacioSpinner = note.getString(note
				.getColumnIndexOrThrow(CoachDBAdapter.KEY_DIVISION));
        
        for (int i=0; i<mPosicion.getCount();i++)
        {

			String pos = (String) mPosicion.getItemAtPosition(i); 
			Log.e(null, pos +" " + vacioSpinner);
			if (pos.equalsIgnoreCase(vacioSpinner))
			{
				mPosicion.setSelection(i);
			}
		}
        
        
		if (vacioSpinner.equals("")) 
		{
			return false;
		}
		return true;
	}*/

	
	private boolean verificarCampoTelefono() 
	{
		if (mTelefonoText.getText().toString().equals("")) 
		{
			return false;
		}
		return true;
	}

	/**
	 * Metodo privado que verifica el campo email
	 */
	private boolean verificarCampoEmail() 
	{
		if (mEmailText.getText().toString().equals("")) 
		{
			return false;
		}
		return true;
	}
	
	
	private void limpiarCampos() 
	{
		mNombreText.setText("");
		mTelefonoText.setText("");
		mEmailText.setText("");
		mApellidosText.setText("");
		mCaracteristicasText.setText("");
	}
	
	public boolean estadoEditarJugador()
	{
		// Si extras es diferente a null es porque tiene valores. En este caso
		// es porque se quiere editar una persona.
		if (extras != null) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {

    	switch (requestCode) 
    			{

    			case SELECCIONAR_GALERIA:
    				
    				if (resultCode != 0) 
    				{
    					Cursor c = managedQuery(data.getData(), null, null, null, null);
    					if (c.moveToFirst()) 
    					{
    						ruta_imagen = c.getString(1);
    					}
    					File f = new File(ruta_imagen);
    					if (f.exists()) 
    					{
    						Bitmap myBitmap = BitmapFactory.decodeFile(ruta_imagen);
    						imagenJugador.setImageBitmap(myBitmap);
    					}
    					//hay_imagen=1;
    				}	    				
    				break;
    				
    			case SELECCIONAR_CAMARA:

    				if (resultCode != 0) 
    				{
    					Bundle extras = data.getExtras();
    					Bitmap bmp = (Bitmap) extras.get("data");

    					if (bmp != null) 
    					{

    						ruta_imagen = getNombreImagen();

    						File f = getFileStreamPath(ruta_imagen);

    						if (f.exists())
    							f.delete();
    						try 
    						{

    							FileOutputStream out = this.openFileOutput(ruta_imagen,
    									MODE_PRIVATE);
    							bmp.compress(CompressFormat.JPEG, 100, out);
    							out.close();

    							ruta_imagen = f.getAbsolutePath();
    						} 
    						
    						catch (FileNotFoundException e) 
    						{
    							Log.e("e","FileNotFoundException generado usando la camera");
    						} 
    						catch (IOException e) 
    						{
    							Log.e("error", "IOException generado usando la camera");
    						}
    						imagenJugador.setImageBitmap(bmp);
    						//hay_imagen=1;
    					}
    				}
    				break;
    			}
    		}
	//}
	
	private Bitmap getBitmap(String ruta_imagen) 
	{
		// Objetos.
		File imagenArchivo = new File(ruta_imagen);
		Bitmap bitmap = null;

		if (imagenArchivo.exists())
		{
			bitmap = BitmapFactory.decodeFile(imagenArchivo.getAbsolutePath());
		}
		return bitmap;
	}
	
	private String obtieneRuta(Uri uri) 
	{
		String[] projection = { android.provider.MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
    @Override
 	public boolean onCreateOptionsMenu(Menu menu) 
    {
    	getSupportMenuInflater();
    	
 		return true;
 	}
  
  
 	@Override
 	public boolean onOptionsItemSelected(MenuItem item) 
    {
 		switch (item.getItemId()) 
 		{
 		
 			case android.R.id.home:
            Intent intent = new Intent(JugadoresEditCoach.this, JugadoresCoach.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
 		}
 		
 		return super.onOptionsItemSelected(item);
 	}
 	
 	  public String getNombreImagen() 
	    {

			long captureTime = System.currentTimeMillis();

			return "CoachApp_" + captureTime + ".jpg";

		}
 	  
 	  
 	 private void loadSpinnerPosiciones() 
 	{
 		Set<String> set = db.getAllPosiciones();

 		List<String> list = new ArrayList<String>(set);

 		adapter = new ArrayAdapter<String>(JugadoresEditCoach.this,
 				android.R.layout.simple_spinner_item, list);

 		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

 		mPosicion.setAdapter(adapter);
 		mPosicion.setWillNotDraw(false);

 	}
}
