package com.gabri.coach.application2;

import java.io.File;
import java.util.ArrayList;

import com.rodri.coach.application2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CargarEjercicio extends Activity implements OnClickListener
{

	Button aceptar, cancelar;
	ListView lista;
	TextView tvNombre;
	
	String nombreEjercicio;
	
	ArrayList<String> ejercicios;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cargar_ejercicio);
	
		aceptar  = (Button) findViewById(R.id.cargarAceptar);
		cancelar = (Button) findViewById(R.id.cargarCancelar);
		lista = (ListView) findViewById(R.id.listaEjercicios);
		tvNombre = (TextView) findViewById(R.id.tvNombreEjercicio);
		
		aceptar.setOnClickListener(this);
		cancelar.setOnClickListener(this);
	
		ejercicios = new ArrayList<String>();
		ejercicios = rellenarLista();
       
		if(ejercicios.isEmpty())
		{
        	Toast.makeText(this, "¡No existen ejercicios guardados!", Toast.LENGTH_SHORT).show();
        	finish();       	
        }
		else
		{

        	ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, ejercicios);
    		lista.setAdapter(adaptador);

    		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() 
    		{
    		    @Override
    		    public void onItemClick(AdapterView<?> av, View v, int pos, long id) 
    		    {
    		    	
    		    	nombreEjercicio = ejercicios.get(pos);
    				tvNombre.setText(nombreEjercicio);
    		    }
    		});	
        }
	}
	
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.cargarAceptar) {
			Intent returnIntent = new Intent();
			returnIntent.putExtra("nombreEjercicio", nombreEjercicio);
			setResult(RESULT_OK,returnIntent);
			finish();
		} else if (id == R.id.cargarCancelar) {
			finish();
		}
}
	
	
	public ArrayList<String> rellenarLista()
	{
		
		ArrayList<String> ejercicios = new ArrayList<String>();
		
		File  file= new File ("sdcard/CoachApplication2/Ejercicios");
        if(file.exists())
        {
        	
        	File[] ficheros = file.listFiles();
        	
        	if(ficheros.length >0)
        	{
	        	for (int i=0;i<ficheros.length;i++)
	        	{
	        	  ejercicios.add(ficheros[i].getName());
	        	}
        	}
        }
        
		return ejercicios;			
	}

}