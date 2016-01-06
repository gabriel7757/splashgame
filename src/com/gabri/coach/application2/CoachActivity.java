package com.gabri.coach.application2;


import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

import java.util.Calendar;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.rodri.coach.application2.R;


public class CoachActivity extends SherlockActivity 
{
	
	
	static final int DATE_DIALOG_ID = 0;
	static final int PICK_DATE_REQUEST = 1;
	public static int TEMA = R.style.Theme_Sherlock_Light_DarkActionBar;
	
	private TextView mDateDisplay;
	private int mYear;
    private int mMonth;
    private int mDay;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		ActionBar ab = getSupportActionBar();
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach);
        
		
		Button btnEquipos = (Button)findViewById(R.id.btn_equipos);
		Button btnEjercicios = (Button)findViewById(R.id.btn_ejercicios);
		Button btnCalendario = (Button)findViewById(R.id.btn_calendario);
		Button btnPizarra = (Button)findViewById(R.id.btn_pizarra);
		
		
		cargarDatosFecha();
		
				 
		btnEquipos.setOnClickListener(new OnClickListener() 
		{
			@Override
		    public void onClick(View v)
		    {
		        VerEquipos();
		    }
		});
				
		btnEjercicios.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				VerEjercicios();			
			}
		});
				
		btnPizarra.setOnClickListener(new OnClickListener() 
        {		
			@Override
			public void onClick(View v) 
			{
				VerPizarra();				
			}
		});
		
		btnCalendario.setOnClickListener(new OnClickListener() 
		{
			@Override
		    public void onClick(View v)
		    {
		        VerCalendario();
		    }
		});
		
	}
	
    private void VerEquipos()
    {
    	 Intent intent = new Intent(CoachActivity.this, EquiposCoach.class);
         startActivity(intent);
    }
    
    private void VerEjercicios()
    {
    	Intent intent = new Intent(CoachActivity.this, EjerciciosCoach.class);
    	startActivity(intent);
    }
    
    private void VerPizarra()
    {
    	Intent intent = new Intent(CoachActivity.this, PizarraCoach.class);
    	startActivity(intent);
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
    

		
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) 
	    {
	        SubMenu sub = menu.addSubMenu("Tema");
	        sub.add(0, R.style.Theme_Sherlock, 0, "Negro");
	        sub.add(0, R.style.Theme_Sherlock_Light, 0, "Blanco");
	        sub.add(0, R.style.Theme_Sherlock_Light_DarkActionBar, 0, "Blanco (Action Bar Negra)");
	        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	        
	       // MenuInflater inflater = getSupportMenuInflater();
	       // inflater.inflate(R.menu.coach, menu);
	        
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) 
	    {
	        if (item.getItemId() == android.R.id.home || item.getItemId() == 0) 
	        {
	            return false;
	        }
	        
	        TEMA = item.getItemId();
	        Toast.makeText(this, "Tema cambiado a \"" + item.getTitle() + "\"", Toast.LENGTH_SHORT).show();
	        return true;
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
    	public boolean onKeyDown(int keyCode, KeyEvent event) 
		{
    	  if (keyCode == KeyEvent.KEYCODE_BACK) 
    	  {
    	   new AlertDialog.Builder(this)
    	      .setIcon(android.R.drawable.ic_dialog_alert)
    	      .setTitle("Salir")
    	      .setMessage("Desea salir de la aplicación?")
    	      .setNegativeButton(android.R.string.cancel, null)//sin listener
    	      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() 
    	      {//un listener que al pulsar, cierre la aplicacion
    	        @Override
    	        public void onClick(DialogInterface dialog, int which)
    	        {
    	          //Salir
    	        	CoachActivity.this.finish();
    	        }
    	      })
    	      .show();
    	    // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
    	    return true;
    	  }
    	//para las demas cosas, se reenvia el evento al listener habitual
    	  return super.onKeyDown(keyCode, event);
    	
    }
}
