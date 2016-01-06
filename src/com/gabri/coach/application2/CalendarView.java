package com.gabri.coach.application2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import android.database.Cursor;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.gabri.coach.application2.database.CoachDBAdapter;
import com.rodri.coach.application2.R;


public class CalendarView extends SherlockActivity implements OnClickListener
	{
		private static final String tag = "CalendarView";

		Intent i;
		private ImageView calendarToJournalButton;
		private Button selectedDayMonthYearButton;
		private Button currentMonth;
		private ImageView prevMonth;
		private ImageView nextMonth;
		private GridView calendarView;
		private GridView diasSem;
		private String[] datos = { "D", " L", " M", " X", " J", " V", " S"};
		GridCellAdapter adapter;
		private Calendar _calendar;
		private int month, year;
		private final DateFormat dateFormatter = new DateFormat();
		private static final String dateTemplate = "MMMM yyyy";
		private CoachDBAdapter equipoDbHelper;
		private CoachDBAdapter entrenamientosDbHelper ;
		private Long idEquipo;
		//String entrenamientoDate;
		Button crearEntrenamiento;
		String entrenamientoDate;
		Locale formatolocal;
		
		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState)
			{
			setTheme(CoachActivity.TEMA);
		    super.onCreate(savedInstanceState);
		    
		    ActionBar ab = getSupportActionBar();		
			ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);			
			ab.setDisplayHomeAsUpEnabled(true);
			setContentView(R.layout.simple_calendar_view);

				formatolocal= new Locale("es","ES");
			    //_calendar = Calendar.getInstance(Locale.getDefault());
			    _calendar = Calendar.getInstance(formatolocal);
				
				month = _calendar.get(Calendar.MONTH) + 1;
				year = _calendar.get(Calendar.YEAR);
				//Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

				selectedDayMonthYearButton = (Button) this.findViewById(R.id.selectedDayMonthYear);
				selectedDayMonthYearButton.setText("Seleccionado: ");

				prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
				prevMonth.setOnClickListener(this);

				currentMonth = (Button) this.findViewById(R.id.currentMonth);
				currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

				nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
				nextMonth.setOnClickListener(this);

				calendarView = (GridView) this.findViewById(R.id.calendar);
				diasSem = (GridView) this.findViewById(R.id.diasSem);
				crearEntrenamiento = (Button) findViewById(R.id.crearEntrenamiento);
				// Initialised
	
				ArrayAdapter<String> adaptadorDiasSem =
			        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);			
			 
				diasSem.setAdapter(adaptadorDiasSem);
			
			
				adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
				adapter.notifyDataSetChanged();
				calendarView.setAdapter(adapter);
				
				crearEntrenamiento.setOnClickListener(new OnClickListener() 
		    	{
		    		@Override
		    	    public void onClick(View v)
		    	    {
		    			crearEntrenamiento();
		    	    }
		    	});       
				
			}

		/**
		 * 
		 * @param month
		 * @param year
		 */
		private void setGridCellAdapterToDate(int month, int year)
			{
				adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
				_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
				currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
				adapter.notifyDataSetChanged();
				calendarView.setAdapter(adapter);
			}

		@Override
		public void onClick(View v)
			{
				if (v == prevMonth)
					{
						if (month <= 1)
							{
								month = 12;
								year--;
							}
						else
							{
								month--;
							}
						//Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
						setGridCellAdapterToDate(month, year);
					}
				if (v == nextMonth)
					{
						if (month > 11)
							{
								month = 1;
								year++;
							}
						else
							{
								month++;
							}
						//Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
						setGridCellAdapterToDate(month, year);
					}

			}
		
		@Override
	 	public boolean onOptionsItemSelected(MenuItem item) 
	    {
	 		switch (item.getItemId()) 
	 		{
	 		
	 			case android.R.id.home:
	            Intent intent = new Intent(CalendarView.this, CoachActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	 		}
	 		
	 		return super.onOptionsItemSelected(item);
	 	}
	 	
	 	public void crearEntrenamiento()
	 	{
	 		i = new Intent(this, EntrenamientosEditCoach.class);
	    	//por aki va sin id equipo
			equipoDbHelper = new CoachDBAdapter(this);   	
			try 
			{
				equipoDbHelper.open();
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Cursor c = equipoDbHelper.fetchAllEquipos();
			ArrayList<String> equipos = new ArrayList<String>();
			final ArrayList<Integer> ids = new ArrayList<Integer>();
			c.moveToFirst();
			
			while(!c.isAfterLast()) 
			{
			     equipos.add(c.getString(c.getColumnIndex(equipoDbHelper.KEY_NOMBRE))); //add the item
			     ids.add(c.getInt(c.getColumnIndex(equipoDbHelper.KEY_ROWID))); 
			     c.moveToNext();
			}
			
			equipoDbHelper.close();
			
			if(equipos.size()>=1)
			{
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, equipos);
				AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
				dialogo.setTitle("Seleccionar Equipo");
				dialogo.setAdapter(adapter,new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int pos) 
					{
						idEquipo =  (long)(ids.get(pos));
						i.putExtra("equipo_seleccionado", idEquipo);
						startActivityForResult(i, 1);
					}
					});
				
				dialogo.create();
				dialogo.show();
			}
			else
			{
				Toast.makeText(this, "¡No existen equipos creados!!!", Toast.LENGTH_SHORT).show();
			}
	        
	 	}

		@Override
		public void onDestroy()
			{
				//Log.d(tag, "Destroying View ...");
				super.onDestroy();
			}

		// ///////////////////////////////////////////////////////////////////////////////////////
		// Inner Class
		public class GridCellAdapter extends BaseAdapter implements OnClickListener
			{
			
			
			
				private static final String tag = "GridCellAdapter";
				private final Context _context;

				private final List<String> list;
				private static final int DAY_OFFSET = 1;
				private final String[] weekdays = new String[]{"Dom", "Lun", "Mar", "X", "Jue", "Vie", "Sab"};
				private final String[] months = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
				private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
				private final int month, year;
				private int daysInMonth, prevMonthDays;
				private int currentDayOfMonth;
				private int currentWeekDay;
				private Button gridcell;
				private TextView num_events_per_day;
				private final HashMap eventsPerMonthMap;
				//private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
				//OJO nueva
				private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy",new Locale("es","ES"));
				//private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy",new Locale("ES"));
				
				
				private CoachDBAdapter entrenamientosDbHelper;
				// Days in Current Month
				public GridCellAdapter(Context context, int textViewResourceId, int month, int year)
					{
						super();
						this._context = context;
						this.list = new ArrayList<String>();
						this.month = month;
						this.year = year;

						//Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
						Calendar calendar = Calendar.getInstance();
						setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
						setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
						//Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
						//Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
						//Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

						// Print Month
						printMonth(month, year);

						// Find Number of Events
						eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
					}
				private String getMonthAsString(int i)
					{
						return months[i];
					}

				private String getWeekDayAsString(int i)
					{
						return weekdays[i];
					}

				private int getNumberOfDaysOfMonth(int i)
					{
						return daysOfMonth[i];
					}

				public String getItem(int position)
					{
						return list.get(position);
					}

				@Override
				public int getCount()
					{
						return list.size();
					}

				/**
				 * Prints Month
				 * 
				 * @param mm
				 * @param yy
				 */
				private void printMonth(int mm, int yy)
					{
						//Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
						// The number of days to leave blank at
						// the start of this month.
						int trailingSpaces = 0;
						int leadSpaces = 0;
						int daysInPrevMonth = 0;
						int prevMonth = 0;
						int prevYear = 0;
						int nextMonth = 0;
						int nextYear = 0;

						int currentMonth = mm - 1;
						String currentMonthName = getMonthAsString(currentMonth);
						daysInMonth = getNumberOfDaysOfMonth(currentMonth);

						//Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

						// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
						GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
						//Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

						if (currentMonth == 11)
							{
								prevMonth = currentMonth - 1;
								daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
								nextMonth = 0;
								prevYear = yy;
								nextYear = yy + 1;
								Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
							}
						else if (currentMonth == 0)
							{
								prevMonth = 11;
								prevYear = yy - 1;
								nextYear = yy;
								daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
								nextMonth = 1;
								//Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
							}
						else
							{
								prevMonth = currentMonth - 1;
								nextMonth = currentMonth + 1;
								nextYear = yy;
								prevYear = yy;
								daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
								//Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
							}

						// Compute how much to leave before before the first day of the
						// month.
						// getDay() returns 0 for Sunday.
						int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
						trailingSpaces = currentWeekDay;

						//Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
						//Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
						//Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

						if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1)
							{
								++daysInMonth;
							}
						//int month = _calendar.get(Calendar.MONTH) + 1;
						// Trailing Month days
						for (int i = 0; i < trailingSpaces; i++)
							{
								//Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
								list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
							}

						// Current Month Days
						for (int i = 1; i <= daysInMonth; i++)
							{
								//Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
								if ((i == getCurrentDayOfMonth()))
									{
										if (currentMonth==5)
										{
										list.add(String.valueOf(i) + "-RED" + "-" + getMonthAsString(currentMonth) + "-" + yy);
										currentMonth++;
										}
										else 
										{
										list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);	
										}
									}
								else
									{
										list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
									}
							}

						// Leading Month days
						for (int i = 0; i < list.size() % 7; i++)
							{
								//Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
								list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
							}
					}

				/**
				 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
				 * ALL entries from a SQLite database for that month. Iterate over the
				 * List of All entries, and get the dateCreated, which is converted into
				 * day.
				 * 
				 * @param year
				 * @param month
				 * @return
				 */
				private HashMap findNumberOfEventsPerMonth(int year, int month)
					{
						HashMap map = new HashMap<String, Integer>();
						// DateFormat dateFormatter2 = new DateFormat();
						//						
						// String day = dateFormatter2.format("dd", dateCreated).toString();
						//
						// if (map.containsKey(day))
						// {
						// Integer val = (Integer) map.get(day) + 1;
						// map.put(day, val);
						// }
						// else
						// {
						// map.put(day, 1);
						// }
						return map;
					}

				@Override
				public long getItemId(int position)
					{
						return position;
					}

				@Override
				public View getView(int position, View convertView, ViewGroup parent)
					{
						View row = convertView;
						if (row == null)
							{
								LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
								row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
							}

						// Get a reference to the Day gridcell
						gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
						gridcell.setOnClickListener(this);

						// ACCOUNT FOR SPACING

						//Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
						String[] day_color = list.get(position).split("-");
						String theday = day_color[0];
						String themonth = day_color[2];
						String theyear = day_color[3];
						
						if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
							{
								if (eventsPerMonthMap.containsKey(theday))
									{
										num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
										Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
										num_events_per_day.setText(numEvents.toString());
									}
							}

						// Set the Day GridCell
						gridcell.setText(theday);
						gridcell.setTag(theday + "-" + themonth + "-" + theyear);
						//Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

						if (day_color[1].equals("GREY"))
							{
								gridcell.setTextColor(Color.LTGRAY);
							}
						if (day_color[1].equals("WHITE"))
							{
								gridcell.setTextColor(Color.WHITE);
							}
						if (day_color[1].equals("RED"))
							{
								gridcell.setTextColor(Color.RED);
							}
						return row;
					}
				@Override
				public void onClick(View view)
					{
						String date_month_year = (String) view.getTag();
						entrenamientoDate = (String) view.getTag();
						
						selectedDayMonthYearButton.setText("Seleccionado: " + date_month_year);
						
						VerEntrenamientosDia(entrenamientoDate);
						
						try
						{	
						Date parsedDate=dateFormatter.parse(date_month_year);		
						entrenamientoDate= parsedDate.toString();
						Toast.makeText(_context, "¡No existen equipos creados!!!", Toast.LENGTH_SHORT).show();
						}
						catch (ParseException e)
						{
							e.printStackTrace();
						}

						try
							{
								Date parsedDate = dateFormatter.parse(date_month_year);
								
								//Log.d(tag, "Parsed Date: " + parsedDate.toString());

							}
						catch (ParseException e)
							{
								e.printStackTrace();
							}
						
						//Intent entrenamiento_dia = new Intent (getApplicationContext(), VerDiaCalendario.class);
			        	//entrenamiento_dia.putExtra("dia_seleccionado", "3-9-2013");
			        
			        	//evento.putExtra(DBAdapter.KEY_ROWID, id);
			        	//startActivityForResult(evento, ACTIVITY_EDIT);
			        	//startActivity(entrenamiento_dia);
						
					}

				public int getCurrentDayOfMonth()
					{
						return currentDayOfMonth;
					}

				private void setCurrentDayOfMonth(int currentDayOfMonth)
					{
						this.currentDayOfMonth = currentDayOfMonth;
					}
				public void setCurrentWeekDay(int currentWeekDay)
					{
						this.currentWeekDay = currentWeekDay;
					}
				public int getCurrentWeekDay()
					{
						return currentWeekDay;
					}
			}
		
		public void VerEntrenamientosDia(String fecha)
		{
			//Toast.makeText(this, fecha, Toast.LENGTH_SHORT).show();
			int numItems=0;
			int i;
			
			entrenamientosDbHelper = new CoachDBAdapter(this);   	
			try 
			{
				entrenamientosDbHelper.open();
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Cursor c = entrenamientosDbHelper.fetchAllEntrenamientosSin();
			ArrayList<String> entrenamientos = new ArrayList<String>();
			ArrayList<String> entrenamientosDia = new ArrayList<String>();
			ArrayList<String> fechas = new ArrayList<String>();
			//final ArrayList<Integer> ids = new ArrayList<Integer>();
			c.moveToFirst();
			
			while(!c.isAfterLast()) 
			{
			     entrenamientos.add(c.getString(c.getColumnIndex(entrenamientosDbHelper.KEY_ENTRENAMIENTO_NOMBRE))); //add the item
			     fechas.add(c.getString(c.getColumnIndex(entrenamientosDbHelper.KEY_ENTRENAMIENTO_FECHA)));
			     //ids.add(c.getInt(c.getColumnIndex(equipoDbHelper.KEY_ROWID))); 
			     c.moveToNext();
			}			
			entrenamientosDbHelper.close();
			
			numItems=fechas.size();
			
			/*for(i=0; i<numItems; i++)
			{
				
					Toast.makeText(this, fecha, Toast.LENGTH_SHORT).show();
					Toast.makeText(this, entrenamientos.get(i), Toast.LENGTH_SHORT).show();			
			}*/
			
			for(i=0; i<numItems; i++)
			{
				if(fechas.get(i)==fecha)
				{				
					entrenamientosDia.add(entrenamientos.get(i)); //add the item
					entrenamientos.add(c.getString(c.getColumnIndex(entrenamientosDbHelper.KEY_ENTRENAMIENTO_NOMBRE))); 
				}
				
			}
			
			if(fechas.size()>=1)
			{
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, entrenamientos);
				AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
				dialogo.setTitle("Lista de entrenamientos");
				dialogo.setAdapter(adapter,new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int pos) 
					{
						
					}
					});
				
				dialogo.create();
				dialogo.show();
			}
		}
	}
