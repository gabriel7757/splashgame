package com.gabri.coach.application2;

import com.rodri.coach.application2.R;
//import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.AlertDialog;
//import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import android.util.Log;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.gabri.coach.application2.database.CoachDBAdapter;
import com.gabri.coach.application2.database.DataBaseHelper;
import com.gabri.coach.applitacion2.util.Mensaje;



public class EquiposEditCoach extends SherlockActivity implements OnItemSelectedListener
{
	private static final int SELECCIONAR_CAMARA=0;
	private static final int SELECCIONAR_GALERIA=1;
	
	private ImageView imagenLogo;
	private String ruta_img;
	private Mensaje mensaje;
	private EditText mNombreText;
	private Spinner mDivisionSpinner;
	private Long mRowId;
	private CoachDBAdapter mDbHelper;
	DataBaseHelper db;
	ArrayAdapter<String> adapter;
	private int hay_imagen;
	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
		 	hay_imagen =0;
		 	setTheme(CoachActivity.TEMA);
	    	super.onCreate(savedInstanceState);
	    	
	    	db = new DataBaseHelper(EquiposEditCoach.this); // PARA ACCEDER A DATOS DEL SPINNER
	    	
	    	mensaje = new Mensaje(getApplicationContext());
	    	
	    	ActionBar ab = getSupportActionBar();				
			ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);				
			ab.setDisplayHomeAsUpEnabled(true);
	    	
	    	mDbHelper = new CoachDBAdapter(this); // Acceder a métodos de los equipos y obtener la info.
	    	mDbHelper.open();
	    	
	    	setContentView(R.layout.equipo_edit);
	    	//setTitle(R.string.edit_equipo);
	    	
	    	mNombreText = (EditText) findViewById(R.id.title);
	    	mDivisionSpinner = (Spinner) findViewById(R.id.spinner_division);
	    	imagenLogo= (ImageView) findViewById(R.id.imagenLogo);
	    	
	    	loadSpinner();
	    	
	    	//borrar.setOnClickListener(this);
	    	mDivisionSpinner.setOnItemSelectedListener(this);
	    	
	    	/*mDivisionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
	    	{
	            
	            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, 
	            		int position, long id) 
	            {
	            	//Toast.makeText(parentView.getContext(), "Has seleccionado " +
	            	  //        parentView.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();            	   
	            }
	                                 
	            public void onNothingSelected(AdapterView<?> parentView) 
	            {
	            	
	            }
	        });        */
	        
	        Button confirmButton = (Button) findViewById(R.id.confirm);
	        Button refreshButton = (Button) findViewById(R.id.refresh);
	        Button addLogoButtonGaleria = (Button) findViewById(R.id.botonAgregarLogoGaleria);
	        Button addLogoButtonCamara = (Button) findViewById(R.id.botonAgregarLogoCamara);
	        
	        mRowId = (savedInstanceState == null) ? null :
	            (Long) savedInstanceState.getSerializable(CoachDBAdapter.KEY_ROWID);
	        
			if (mRowId == null) 
			{
				Bundle extras = getIntent().getExtras();
				mRowId = extras != null ? extras.getLong(CoachDBAdapter.KEY_ROWID)
										: null;
			}
			
			populateFields();

	        confirmButton.setOnClickListener(new View.OnClickListener() 
	        {

	            public void onClick(View view) 
	            {
	            	if (verificarCampoNombre()) 
					{											
							setResult(RESULT_OK);
							saveState();
							finish();	
					}		
	            	else
					{
						if (mNombreText.getText().toString().equals("")) 
						{
							mensaje.mostrarMensajeCorto("Introduzca el nombre del Equipo!!!");
						}				
					}
	            }

				private boolean verificarCampoNombre() 
				{
					if (mNombreText.getText().toString().equals("")) 
					{
						return false;
					}
					return true;
				}

	        });
	        
	        refreshButton.setOnClickListener(new View.OnClickListener() 
	        {

	            public void onClick(View view) 
	            {
	            	loadSpinner();
	            }

	        });
	        
	        addLogoButtonGaleria.setOnClickListener(new View.OnClickListener() 
	        {

	            public void onClick(View view) 
	            {	       
	            	ventanaImagen();             	
	            }

	        });
	        
	        addLogoButtonCamara.setOnClickListener(new View.OnClickListener() 
	        {

	            public void onClick(View view) 
	            {	       
	            	CapturarImagen();             	
	            }

	        });
	   			
	    }
	 
	 
	 private void populateFields()
	 {
		 if (mRowId != null) 
		 {
	            Cursor todo = mDbHelper.fetchEquipo(mRowId);
	            startManagingCursor(todo);
	            
	            String division = todo.getString(todo
						.getColumnIndexOrThrow(CoachDBAdapter.KEY_DIVISION));
	            
	            for (int i=0; i<mDivisionSpinner.getCount();i++)
	            {

					String pos = (String) mDivisionSpinner.getItemAtPosition(i); 
					Log.e(null, pos +" " + division);
					if (pos.equalsIgnoreCase(division))
					{
						mDivisionSpinner.setSelection(i);
					}
				}
	            
	            mNombreText.setText(todo.getString(
	                    todo.getColumnIndexOrThrow(CoachDBAdapter.KEY_NOMBRE)));	 
	            
	          
	            ruta_img = (todo.getString(
	                    todo.getColumnIndexOrThrow(CoachDBAdapter.KEY_LOGO)));
	            
	            if(ruta_img=="")
	            {
	            	imagenLogo.setImageResource(R.drawable.contact);
	            }
	            else
	            	{
	            	Bitmap bitmap = getBitmap(ruta_img);
	            	imagenLogo.setImageBitmap(bitmap);
	            	hay_imagen=1;
	            	}
		 		}
	            /*if( ruta_img !=null)
	            {
	            	
				Bitmap bitmap = getBitmap(ruta_img);

				// La imagen no puede ser mayor a 4252 de ancho o alto.
				if (bitmap.getHeight() >= 4252 || bitmap.getWidth() >= 4252) 
				{
					bitmap = Bitmap.createScaledBitmap(
							bitmap,
							(bitmap.getHeight() >= 4252) ? 4252 : bitmap
									.getHeight(),
							(bitmap.getWidth() >= 4252) ? 4252 : bitmap
									.getWidth(), true);
					
				}
				imagenLogo.setImageBitmap(bitmap);
		 		}
			     else 
		            {
			    	 imagenLogo.setImageResource(R.drawable.contact);
		            }
	      }*/
	 }
	 
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
	 @Override
	 protected void onSaveInstanceState(Bundle outState) 
	 {
	        super.onSaveInstanceState(outState);
	       // saveState();
	        outState.putSerializable(CoachDBAdapter.KEY_ROWID,mRowId);
	 }

	   /* @Override
	  protected void onPause() 
	  {
	        super.onPause();
	        saveState(); 
	  }
	    
	    @Override
	  protected void onResume() 
	  {
	        super.onResume();
	        populateFields();   
	  }*/
	    

	    private void saveState() 
	    {
	        String title = mNombreText.getText().toString();
	        String datoSpinner = (String) mDivisionSpinner.getSelectedItem();
	        
	        if(hay_imagen==0)
	        {
	        	ruta_img="/CoachApplication2/res/drawable/contact";

	        }
	   
	        			    	
	        if (mRowId == null) 
	        {
	            long id = mDbHelper.createEquipo(ruta_img, title, datoSpinner);
	            
	            if (id > 0) 
	            {
	                mRowId = id;
	            }
	            mensaje.mostrarMensajeCorto("ruta imgn insertada OK: " + ruta_img);
	        } 
	        else 
	        {
	            mDbHelper.updateEquipo(mRowId, ruta_img, title, datoSpinner);
	        }	        
	    }

		@Override
		public void onItemSelected(AdapterView<?> parentView, View selectedItemView, 
         		int position, long id) 
		{
			//db.deleteDivisiones(id);
			//loadSpinner();
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) 
		{
			// TODO Auto-generated method stub		
		}

		
		private void loadSpinner() 
		{
			// here i used Set Because Set doesn't allow duplicates.
			Set<String> set = db.getAllDivisiones();

			List<String> list = new ArrayList<String>(set);

			adapter = new ArrayAdapter<String>(EquiposEditCoach.this,
					android.R.layout.simple_spinner_item, list);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			mDivisionSpinner.setAdapter(adapter);
			mDivisionSpinner.setWillNotDraw(false);

		}
		
		
	    @Override
	 	public boolean onCreateOptionsMenu(Menu menu) 
	    {
	        
	        MenuInflater inflater = getSupportMenuInflater();
	        inflater.inflate(R.menu.menu_config_spinner, menu);
	        return true;        
	 	}
	    	    

	    @Override
	 	public boolean onOptionsItemSelected(MenuItem item) 
	    {
	 		int itemId = item.getItemId();
			if (itemId == R.id.menu_config) {
				configurarSpinner();
				return true;
			} else if (itemId == android.R.id.home) {
				Intent intent = new Intent(this, EquiposCoach.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			}
	 		return super.onOptionsItemSelected(item);
	 	}
	    
	    private void configurarSpinner() 
	 	{
	 		Intent i = new Intent(this, ConfigSpinnerCoach.class);
	 		startActivityForResult(i, 0);
	 	}
	    
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
	    
	    public String getNombreImagen() 
	    {

			long captureTime = System.currentTimeMillis();
			return "CoachApp_" + captureTime + ".jpg";

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
	    						ruta_img = c.getString(1);
	    					}
	    					File f = new File(ruta_img);
	    					if (f.exists()) 
	    					{
	    						Bitmap myBitmap = BitmapFactory.decodeFile(ruta_img);
	    						imagenLogo.setImageBitmap(myBitmap);
	    					}
	    					hay_imagen=1;
	    				}	    				
	    				break;
	    				
	    			case SELECCIONAR_CAMARA:

	    				if (resultCode != 0) 
	    				{
	    					Bundle extras = data.getExtras();
	    					Bitmap bmp = (Bitmap) extras.get("data");

	    					if (bmp != null) 
	    					{

	    						ruta_img = getNombreImagen();

	    						File f = getFileStreamPath(ruta_img);

	    						if (f.exists())
	    							f.delete();
	    						try 
	    						{

	    							FileOutputStream out = this.openFileOutput(ruta_img,
	    									MODE_PRIVATE);
	    							bmp.compress(CompressFormat.JPEG, 100, out);
	    							out.close();

	    							ruta_img = f.getAbsolutePath();
	    						} 
	    						
	    						catch (FileNotFoundException e) 
	    						{
	    							Log.e("e","FileNotFoundException generado usando la camera");
	    						} 
	    						catch (IOException e) 
	    						{
	    							Log.e("error", "IOException generado usando la camera");
	    						}
	    						imagenLogo.setImageBitmap(bmp);
	    						hay_imagen=1;
	    					}
	    				}
	    				break;
	    			}
	    		}
}

