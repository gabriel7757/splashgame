package com.gabri.coach.application2;

//import com.rodri.coach.application2.modelos.Jugadores;
//import com.rodri.coach.applitacion2.util.ImagenAdapter;
//import com.rodri.coach.application2.database.DataBaseHelper;
import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.actionbarsherlock.app.ActionBar;
import com.rodri.coach.application2.R;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.gabri.coach.application2.database.CoachDBAdapter;

import android.widget.TextView;


public class JugadoresDatosCoach extends SherlockActivity
{
	
	private String ruta_imagen;
	private ImageView mImagenFoto;
	private TextView mNombreText;
	private TextView mApellidosText;
	private TextView mPosicionText;
	private TextView mTelefonoText;
	private TextView mEmailText;
	private TextView mCaracteristicasText;
	private Long idJug;
	private Bundle extras;
	private CoachDBAdapter jugadorDbHelper;
	private ImageButton llamar;

	//private DataBaseHelper jugadorDbHelper;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.jugadores_datos_row);
	    	
	    	ActionBar ab = getSupportActionBar();
	    	ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
	    	ab.setDisplayHomeAsUpEnabled(true);
	  
	    	//Valor del jugador seleccionado
	    	Intent intent = getIntent();
	    	Bundle extras = getIntent().getExtras();
	    	if (extras == null) return;
	    	
	    	
	    	
	    	//mImagenFoto = (ImageView) findViewById(R.id.ImageView01);	    	
	    	mNombreText = (TextView) findViewById(R.id.jugador_nombre);	    	
	    	mApellidosText = (TextView) findViewById(R.id.jugador_apellidos);
			mTelefonoText = (TextView) findViewById(R.id.text_jugadortelefono);
			mEmailText = (TextView) findViewById(R.id.text_jugadoremail);
			mPosicionText = (TextView) findViewById(R.id.text_posicion);
			mCaracteristicasText = (TextView) findViewById(R.id.text_caracteristicas);
			llamar = (ImageButton)findViewById(R.id.llamada);
	    	
			idJug = extras.getLong("jugador_seleccionado");
			
			llamar.setOnClickListener(new OnClickListener() 
	    	{
	    		@Override
	    	    public void onClick(View v)
	    	    {
	    			 llamarJugador(idJug);
	    	    }
	    	});
			
			
		  	
	    	jugadorDbHelper = new CoachDBAdapter(this);
	    	jugadorDbHelper.open();
	    	
	        mostrarDatosJugador(idJug);
	
	    }
	 
	 	private void mostrarDatosJugador(long id)
		{
			Cursor cursor = jugadorDbHelper.fetchJugador(id);
			startManagingCursor(cursor);
			
			/* ruta_imagen = (cursor.getString(
					 cursor.getColumnIndexOrThrow(CoachDBAdapter.KEY_RUTA_IMG)));
	            
	            if (ruta_imagen != null)
	            {
				Bitmap bitmap = getBitmap(ruta_imagen);
				
				
					if (bitmap.getHeight() >= 4252 || bitmap.getWidth() >= 4252) 
					{
					bitmap = Bitmap.createScaledBitmap(
							bitmap,
							(bitmap.getHeight() >= 4252) ? 4252 : bitmap
									.getHeight(),
							(bitmap.getWidth() >= 4252) ? 4252 : bitmap
									.getWidth(), true);
					
					}
					
				mImagenFoto.setImageBitmap(bitmap);     
				
	            }
	            else 
	            {
	            	mImagenFoto.setImageResource(R.drawable.contact);
	            }*/
	            
		    mNombreText.setText(cursor.getString(cursor.getColumnIndex(CoachDBAdapter.KEY_NOMBRE_JUG)));
		    mApellidosText.setText(cursor.getString(cursor.getColumnIndex(CoachDBAdapter.KEY_APELLIDOS_JUG)));
		    mTelefonoText.setText(cursor.getString(cursor.getColumnIndex(CoachDBAdapter.KEY_TLFONO_JUG)));
		    mEmailText.setText(cursor.getString(cursor.getColumnIndex(CoachDBAdapter.KEY_EMAIL_JUG)));
		    mPosicionText.setText(cursor.getString(cursor.getColumnIndex(CoachDBAdapter.KEY_POSICION)));
		    mCaracteristicasText.setText(cursor.getString(cursor.getColumnIndex(CoachDBAdapter.KEY_CARACTERISTICAS)));		
		}
	 	
	/*	private Bitmap getBitmap(String ruta_imagen) 
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
		}*/
	
	/* @Override
	 public void onActivityResult(int requestCode, int resultCode, Intent data) 
	 {
	 	super.onActivityResult(requestCode, resultCode, data);
	 }*/
	 	
	 	public void llamarJugador(final long id)
	    {
		
	 		AlertDialog.Builder alertDialog = new AlertDialog.Builder(JugadoresDatosCoach.this);
            alertDialog.setMessage("¿Desea realizar la llamada al contacto?");
            alertDialog.setTitle("Llamar a contacto...");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() 
            {
              public void onClick(DialogInterface dialog, int which) 
              {
                try 
                {          			 
                	Cursor cursor = jugadorDbHelper.fetchJugador(id);
       	 		 	String numero=cursor.getString(cursor.getColumnIndex(CoachDBAdapter.KEY_TLFONO_JUG));
                    String number = "tel:" + numero.trim();
                    Toast.makeText(getApplicationContext(), 
                  		  "Llamando al " + numero.trim(), Toast.LENGTH_LONG).show();
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); 
                    startActivity(callIntent);
                } 
                catch (Exception e) 
                {
                	Toast.makeText(getApplicationContext(), 
                			"No se ha podido realizar la llamada", Toast.LENGTH_LONG).show();
                }
              } 
            }); 
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() 
            {
              public void onClick(DialogInterface dialog, int which) 
              {
                Toast.makeText(getApplicationContext(), 
                		"Llamada cancelada", Toast.LENGTH_LONG).show();
              } 
            }); 
            alertDialog.show();
	 		
	 		 
	    }
}


