package com.gabri.coach.application2;

import java.io.File;
import java.util.ArrayList;

import com.rodri.coach.application2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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


public class EliminarEjercicio extends Activity implements OnClickListener
{

	Button aceptar, cancelar;
	ListView lista;
	TextView tvNombre;
	
	String nombreEjercicio;
	int id=0;
	ArrayList<String> archivos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eliminar_ejercicio);
	
		aceptar  = (Button) findViewById(R.id.eliminarAceptar);
		cancelar = (Button) findViewById(R.id.eliminarCancelar);
		lista = (ListView) findViewById(R.id.listaArchivos);
		tvNombre = (TextView) findViewById(R.id.tvNombreEjercicio);
		
		aceptar.setOnClickListener(this);
		cancelar.setOnClickListener(this);
		
		archivos = new ArrayList<String>();
		archivos = rellenarLista();
       
		if(archivos.isEmpty())
		{

        	Toast.makeText(this, "¡No existen ejercicios guardados!", Toast.LENGTH_SHORT).show();
        	finish();
        	
        }
		else
        {

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, archivos);
		lista.setAdapter(adaptador);

		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
		    @Override
		    public void onItemClick(AdapterView<?> av, View v, int pos, long id) 
		    {
		    	
		    	nombreEjercicio = archivos.get(pos);
				tvNombre.setText(nombreEjercicio);
		    }
		});
    	
		
        }
	}
	

	@Override
	public void onClick(View v) 
	{
		int id = v.getId();
		if (id == R.id.eliminarAceptar) {
			if(!nombreEjercicio.equals("")){
				
			AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
			
			dialogo.setTitle("Eliminar archivo");
			dialogo.setMessage("¿Borrar el archivo '"+nombreEjercicio+"' ?");

			dialogo.setPositiveButton("SI",new DialogInterface.OnClickListener() 
			{
						@Override
						public void onClick(DialogInterface dialog,int which) 
						{
							// TODO Auto-generated method stub
							Intent returnIntent = new Intent();
							returnIntent.putExtra("nombreEjercicio", nombreEjercicio); 
							setResult(-1,returnIntent);     
							finish();						
							}
					});
			dialogo.setNegativeButton("NO",new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog,int which) 
				{
					// TODO Auto-generated method stub
					setResult(-2,null);     
					finish();
					
				}
			});
			dialogo.create();
			dialogo.show();	
			
			}
			else
			{
				Toast.makeText(this, "Seleccione un archivo", Toast.LENGTH_SHORT).show();
			}
		} else if (id == R.id.eliminarCancelar) {
			finish();
		}

}
	
	
	public ArrayList<String> rellenarLista()
	{
		
		ArrayList<String> archivos = new ArrayList<String>();
		
		File  file= new File ("sdcard/CoachApplication2/Ejercicios");
        if(file.exists()){
        	
        	File[] ficheros = file.listFiles();
        	
        	if(ficheros.length >0)
        	{
	        	for (int i=0;i<ficheros.length;i++)
	        	{
	        	  archivos.add(ficheros[i].getName());
	        	}
        	}
        }
        
		return archivos;
			
	}


	
}
