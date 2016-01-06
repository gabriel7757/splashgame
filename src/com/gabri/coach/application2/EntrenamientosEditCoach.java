package com.gabri.coach.application2;


import com.rodri.coach.application2.R;
import java.util.Calendar;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.gabri.coach.application2.database.CoachDBAdapter;
import com.gabri.coach.applitacion2.util.Mensaje;


public class EntrenamientosEditCoach extends SherlockActivity
{
	//date
	private EditText mEntrenamientoText;
	private TextView mDateDisplay;
    private Button mPickDate;
    
    private int mYear;
    private int mMonth;
    private int mDay;

    static final int DATE_DIALOG_ID = 0;
    //time
    private TextView tvDisplayTime;
	private TimePicker timePicker1;
	private Button btnChangeTime;

	private int hour;
	private int minute;

	static final int TIME_DIALOG_ID = 999;
	
    private Long mRowIdEnt;
	private Long mRowIdEquipo;
	private int modo;
	private CoachDBAdapter mDbHelper;
	private Mensaje mensaje;
	
	 protected void onCreate(Bundle savedInstanceState) 
	    {
		 	setTheme(CoachActivity.TEMA);
	        super.onCreate(savedInstanceState);
	        
	        mensaje = new Mensaje(getApplicationContext());
	        
	        ActionBar ab = getSupportActionBar();
			
			ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
			
			ab.setDisplayHomeAsUpEnabled(true);
			
	      //Abrimos la base de datos para escribir
			mDbHelper = new CoachDBAdapter(this);
			mDbHelper.open();
	        
			
			Bundle extras = getIntent().getExtras();
			mRowIdEquipo = extras.getLong("equipo_seleccionado");
			modo = extras.getInt("modo");
			
	        setContentView(R.layout.entrenamiento_edit);
	        setCurrentTimeOnView();
	        addListenerOnButton();
	        /*if(modo==0)
	        {
	        	setTitle(R.string.nuevo_entrenamiento);
	        }
	        else
	        {
		        setTitle(R.string.edit_entrenamiento);
	        }*/
	        
	        
	      //Capturar EditText de entrenamientos
	        mEntrenamientoText = (EditText) findViewById(R.id.title_entrenamiento);
	        
	        Button Button_Save = (Button) findViewById(R.id.btn_save_entrenamiento);
				
			
			mRowIdEnt = (savedInstanceState == null) ? null :
	            (Long) savedInstanceState.getSerializable(CoachDBAdapter.KEY_ROWID_EN);
			if (mRowIdEnt == null) 
			{		
				if(modo == 0)
				{
					mRowIdEnt = null;
					cargarDatosFecha();
				}
				else
				{
					mRowIdEnt = extras.getLong(CoachDBAdapter.KEY_ROWID_EN);
					cargarDatosFecha();
				}
				
			}
			
			populateFields();
			
			Button_Save.setOnClickListener(new View.OnClickListener() 
			{

		            public void onClick(View view) 
		            {
		            	if (verificarCampoEntrenamiento()) 
						{	
			                setResult(RESULT_OK);
			                saveState();
			                finish();
						}
		            	else
		            	{
		            		if (mEntrenamientoText.getText().toString().equals("")) 
							{
								mensaje.mostrarMensajeCorto("Asigne un nombre al Entrenamiento!!!");
							}		
		            	}
		            }
		            
		        	private boolean verificarCampoEntrenamiento() 
					{
						if (mEntrenamientoText.getText().toString().equals("")) 
						{
							return false;
						}
						return true;
					}

		    });	        
	    }
	    
	    private void cargarDatosFecha()
	    {
	    	// capture our View elements
	        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
	        mPickDate = (Button) findViewById(R.id.pickDate);

	        // add a click listener to the button
	        mPickDate.setOnClickListener(new View.OnClickListener() 
	        {
	            public void onClick(View v) 
	            {
	                showDialog(DATE_DIALOG_ID);
	            }
	        });

	        // get the current date
	        final Calendar c = Calendar.getInstance();
	        mYear = c.get(Calendar.YEAR);
	        mMonth = c.get(Calendar.MONTH);
	        mDay = c.get(Calendar.DAY_OF_MONTH);

	        // display the current date (this method is below)
	        updateDisplay();
	    }
	    
	 // updates the date in the TextView
	    private void updateDisplay() 
	    {
	        mDateDisplay.setText(new StringBuilder()
	                    // Month comienza en 0, le sumamos +1
	                    .append(mDay).append("-")
	                    .append(mMonth + 1).append("-")
	                    .append(mYear).append(""));
	    }
	    

	 // the callback received when the user "sets" the date in the dialog
	  private DatePickerDialog.OnDateSetListener mDateSetListener =
	            new DatePickerDialog.OnDateSetListener() 
	  {

	                public void onDateSet(DatePicker view, int year, 
	                                      int monthOfYear, int dayOfMonth) 
	                {
	                    mYear = year;
	                    mMonth = monthOfYear;
	                    mDay = dayOfMonth;
	                    updateDisplay();
	                }
	            };

	  
	  private void populateFields()
	  {
			 if (mRowIdEnt != null) 
			 {
		            Cursor note = mDbHelper.fetchEntrenamiento(mRowIdEnt);
		            startManagingCursor(note);
		            mEntrenamientoText.setText(note.getString(
		                    note.getColumnIndexOrThrow(CoachDBAdapter.KEY_ENTRENAMIENTO_NOMBRE)));
		            mDateDisplay.setText(note.getString(
		                    note.getColumnIndexOrThrow(CoachDBAdapter.KEY_ENTRENAMIENTO_FECHA)));
		            tvDisplayTime.setText(note.getString(
		                    note.getColumnIndexOrThrow(CoachDBAdapter.KEY_ENTRENAMIENTO_HORA)));
		      }
	  }
	  
	  @Override
	  protected void onSaveInstanceState(Bundle outState) 
	  {
	      super.onSaveInstanceState(outState);
	      saveState();
	      outState.putSerializable(CoachDBAdapter.KEY_ROWID_EN,mRowIdEnt);
	  }
		
	/*	@Override
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
		        String nombreEntrenamiento = mEntrenamientoText.getText().toString();
		        String fecha_entrenamiento = mDateDisplay.getText().toString();
		        String hora_entrenamiento = tvDisplayTime.getText().toString();
		        
		        if (mRowIdEnt == null) 
		        {
		            long id = mDbHelper.createEntrenamiento(mRowIdEquipo, nombreEntrenamiento, 
		            		fecha_entrenamiento, hora_entrenamiento);
		            if (id > 0) 
		            {
		            	mRowIdEnt = id;
		            }
		        } 
		        else 
		        {
		            mDbHelper.updateEntrenamiento(mRowIdEnt, nombreEntrenamiento, 
		            		fecha_entrenamiento, hora_entrenamiento);
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
	            Intent intent = new Intent(EntrenamientosEditCoach.this, EntrenamientosCoach.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	 		}
	 		
	 		return super.onOptionsItemSelected(item);
	 	}
	 	
		// display current time
		public void setCurrentTimeOnView() 
		{

			tvDisplayTime = (TextView) findViewById(R.id.tvTime);
			timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

			final Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);

			// set current time into textview
			tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":")
					.append(pad(minute)));

			// set current time into timepicker
			timePicker1.setCurrentHour(hour);
			timePicker1.setCurrentMinute(minute);

		}

		public void addListenerOnButton() 
		{

			btnChangeTime = (Button) findViewById(R.id.btnChangeTime);

			btnChangeTime.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					showDialog(TIME_DIALOG_ID);
				}
			});

		}

		@Override
		protected Dialog onCreateDialog(int id) 
		{
			switch (id) 
			{
			case TIME_DIALOG_ID:
				// set time picker as current time
				return new TimePickerDialog(this, timePickerListener, 
						hour, minute, false);
			 case DATE_DIALOG_ID:
                 return new DatePickerDialog(this,
                             mDateSetListener,
                             mYear, mMonth, mDay);
			}
			return null;
		}

		private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int selectedHour,
					int selectedMinute) 
			{
				hour = selectedHour;
				minute = selectedMinute;

				// set current time into textview
				tvDisplayTime.setText(new StringBuilder().append(pad(hour))
						.append(":").append(pad(minute)));

				// set current time into timepicker
				timePicker1.setCurrentHour(hour);
				timePicker1.setCurrentMinute(minute);

			}
		};

		private static String pad(int c) 
		{
			if (c >= 10)
				return String.valueOf(c);
			else
				return "0" + String.valueOf(c);
		}
}

