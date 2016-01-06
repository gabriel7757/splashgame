package com.gabri.coach.application2;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.gabri.coach.application2.database.DataBaseHelper;
import com.rodri.coach.application2.R;

public class ConfigSpinnerCoach extends SherlockActivity implements OnClickListener, OnItemSelectedListener
{

	private Spinner mDivisionSpinner;
	private Button btnAdd;
	DataBaseHelper db;
	private EditText edittext;
	ArrayAdapter<String> adapter;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	setTheme(CoachActivity.TEMA);
    	super.onCreate(savedInstanceState);
    	
    	db = new DataBaseHelper(ConfigSpinnerCoach.this);
    	
    	setContentView(R.layout.config_spinner);
    	
    	ActionBar ab = getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
        ab.setHomeButtonEnabled(true);
        
        mDivisionSpinner = (Spinner) findViewById(R.id.spinner1);
        btnAdd = (Button) findViewById(R.id.btnadd);
        edittext = (EditText) findViewById(R.id.editText1);
        
        loadSpinner();
        
        Button clearButton = (Button) findViewById(R.id.clear);
        
        btnAdd.setOnClickListener(this);
        mDivisionSpinner.setOnItemSelectedListener(this);
        
        clearButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
            	//adapter.remove(object); 
            eliminarDivisiones();
            }

        });
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

	@Override
	public void onClick(View v) 
	{
		String division = edittext.getText().toString().trim();

		if (TextUtils.isEmpty(division)) 
		{
			// showToast("Please Enter Your Name");
			edittext.setError("Por favor introduzca una division nueva.");
			edittext.requestFocus();
		} 
		else 
		{
			db.insertDivisiones(division);
			edittext.setText("");

			// Hiding the keyboard
			InputMethodManager inputmangager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmangager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

			loadSpinner();
		}
	}
	
	private void loadSpinner() 
	{
		// here i used Set Because Set doesn't allow duplicates.
		Set<String> set = db.getAllDivisiones();

		List<String> list = new ArrayList<String>(set);

		adapter = new ArrayAdapter<String>(ConfigSpinnerCoach.this,
				android.R.layout.simple_spinner_item, list);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		

		mDivisionSpinner.setAdapter(adapter);
		mDivisionSpinner.setWillNotDraw(false);
	}
	
    @Override
 	public boolean onOptionsItemSelected(MenuItem item) 
    {
 		switch (item.getItemId()) 
 		{
 			
 		case android.R.id.home:
            Intent intent = new Intent(this, EquiposEditCoach.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
 		}
 		return super.onOptionsItemSelected(item);
 	}
    public void eliminarDivisiones()
    {
	new AlertDialog.Builder(this)
	      .setIcon(android.R.drawable.ic_dialog_alert)
	      .setTitle("Eliminar")
	      .setMessage("Desea eliminar todas las divisiones?")
	      .setNegativeButton(android.R.string.cancel, null)//sin listener
	      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() 
	      {
	    	  //un listener que al pulsar elimina de la bbdd todas las divisiones actuales.
	        @Override
	        public void onClick(DialogInterface dialog, int which)
	        {
	          //Elimina
	        	db.deleteDivisiones();
	        	loadSpinner();
	        }
	      })
	      .show();  
    }
}
