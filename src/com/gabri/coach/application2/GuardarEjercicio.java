package com.gabri.coach.application2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gabri.coach.application2.database.CoachDBAdapter;
import com.gabri.coach.application2.database.DataBaseHelper;
import com.rodri.coach.application2.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class GuardarEjercicio extends Activity implements OnClickListener
{
	Button aceptar, cancelar;
	EditText nombreEjercicio;
	EditText descripcionEjercicio;
	private CoachDBAdapter mDbHelper;
	Spinner mTipoEjercicio;
	DataBaseHelper db;
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stubd
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guardar_ejercicio);
		
		aceptar  = (Button) findViewById(R.id.guardarAceptar);
		cancelar = (Button) findViewById(R.id.guardarCancelar);
		
		nombreEjercicio = (EditText) findViewById(R.id.nombre_ejercicio);
		descripcionEjercicio = (EditText) findViewById(R.id.ejercicio_descripcion);
		mTipoEjercicio = (Spinner)  findViewById(R.id.tipo_ejercicio);
		
		
		mDbHelper = new CoachDBAdapter(this);
		mDbHelper.open();
		 
		db = new DataBaseHelper(GuardarEjercicio.this);
		//loadSpinnerTipoEjercicio();
		
		aceptar.setOnClickListener(this);
		cancelar.setOnClickListener(this);	
	}

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
			
		switch(v.getId())
		{
			case R.id.guardarAceptar:
				 guardarEjercicio();
      			 break;
			case R.id.guardarCancelar:
				 finish();
    			 break;			
		}		
	}
	
	
	 private void guardarEjercicio() 
	    {
	    	
			 Intent returnIntent = new Intent();
			 String ejercicio = nombreEjercicio.getText().toString();
			 String descripcion= descripcionEjercicio.getText().toString();		
			 String tipo_ejercicio= mTipoEjercicio.toString();			 
			
			
			 if(ejercicio.equals(""))
			 {
				 Toast.makeText(this, "Introduzca un nombre de ejercicio", Toast.LENGTH_SHORT);
			 }
			 else
			 {
				 returnIntent.putExtra("nombreEjercicio", ejercicio);
				 returnIntent.putExtra("descripcion", descripcion);
				 returnIntent.putExtra("tipoEjercicio", tipo_ejercicio);
				 setResult(RESULT_OK,returnIntent); 		
				 			
				 finish();
			 }
                       	        
	    }
	
/*
	 private void loadSpinnerTipoEjercicio() 
	 	{
	 		Set<String> set = db.getAllTipoEjercicio();

	 		List<String> list = new ArrayList<String>(set);

	 		adapter = new ArrayAdapter<String>(GuardarEjercicio.this,
	 				android.R.layout.simple_spinner_item, list);

	 		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	 		mTipoEjercicio.setAdapter(adapter);
	 		mTipoEjercicio.setWillNotDraw(false);

	 	}
	 
	*/ 
	
	
}
