package com.gabri.coach.application2;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.rodri.coach.application2.R;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.gabri.coach.application2.database.CoachDBAdapter;


public class EjerciciosEditCoach extends SherlockActivity
{
	private EditText mNombreEjText;
	private EditText mNjugadoresText;
	private EditText mDescripcionText;
	private Long mRowId;
	private CoachDBAdapter mDbHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(CoachActivity.TEMA);
		super.onCreate(savedInstanceState);
		
		ActionBar ab = getSupportActionBar();
		
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
			
		ab.setDisplayHomeAsUpEnabled(true);
		
		mDbHelper = new CoachDBAdapter(this);
		mDbHelper.open();
		
		setContentView(R.layout.ejercicio_editar);
		//setTitle(R.string.edit_ejercicio);
		
		mNombreEjText = (EditText) findViewById(R.id.ejercicio_nombre);
		mNjugadoresText = (EditText) findViewById(R.id.ejercicio_njugadores);
		mDescripcionText = (EditText) findViewById(R.id.ejercicio_descripcion);
		
		Button Btn_Save = (Button) findViewById(R.id.btn_save_ejercicio);
		Button Btn_Campo = (Button) findViewById(R.id.btn_crear_imagen_ejercicio);
		
		 mRowId = (savedInstanceState == null) ? null :
	            (Long) savedInstanceState.getSerializable(CoachDBAdapter.KEY_ROWID_EJ);
			if (mRowId == null) 
			{
				Bundle extras = getIntent().getExtras();
				mRowId = extras != null ? extras.getLong(CoachDBAdapter.KEY_ROWID_EJ)
										: null;
			}
			
		populateFields();
		
		
		Btn_Save.setOnClickListener(new View.OnClickListener() 
		{

            public void onClick(View view) 
            {
                setResult(RESULT_OK);
                saveState();
                finish();
            }

        });
		
		/*Btn_Campo.setOnClickListener(new View.OnClickListener() 
		{

            public void onClick(View view) 
            {
                mostrarCampo();
            }

        });*/

	}
	
	private void populateFields()
	 {
		 if (mRowId != null) 
		 {
	            Cursor note = mDbHelper.getEjercicio(mRowId);
	            startManagingCursor(note);
	            mNombreEjText.setText(note.getString(
	                    note.getColumnIndexOrThrow(CoachDBAdapter.KEY_EJERCICIO_NOMBRE)));
	            mNjugadoresText.setText(note.getString(
	                    note.getColumnIndexOrThrow(CoachDBAdapter.KEY_EJERCICIO_NJUGADORES)));
	            mDescripcionText.setText(note.getString(
	                    note.getColumnIndexOrThrow(CoachDBAdapter.KEY_EJERCICIO_DESCRIPCION)));
	       }
	 }
	
	/*private void mostrarCampo()
	{
		Intent intent = new Intent(this, EjerciciosCampo.class);
    	startActivity(intent);
	}
	*/
	
	@Override
    protected void onSaveInstanceState(Bundle outState) 
	{
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(CoachDBAdapter.KEY_ROWID_EJ,mRowId);
    }

    /*@Override
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
    	
    	String nombre_ejercicio = mNombreEjText.getText().toString();
    	String njugadores = mNjugadoresText.getText().toString();
        String descripcion = mDescripcionText.getText().toString();
    	
   
        if (mRowId == null) 
        {
            long id = mDbHelper.crearEjercicio(nombre_ejercicio, njugadores, descripcion);
            if (id > 0) 
            {
                mRowId = id;
            }
        } 
        else 
        {
            mDbHelper.updateEjercicio(mRowId, nombre_ejercicio, njugadores, descripcion);
        }
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
            Intent intent = new Intent(EjerciciosEditCoach.this, EjerciciosCoach.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
 		}
 		
 		return super.onOptionsItemSelected(item);
 	}
}
