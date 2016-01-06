//Rodrigo Sanchez Monroy
package com.gabri.coach.application2.database;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;

public class CoachDBAdapter 
{
	
	  //KEYs de las tabla de jugadores
    public static final String KEY_RUTA_IMG = "ruta_imagen";
    public static final String KEY_NOMBRE_JUG = "nombre";
    public static final String KEY_APELLIDOS_JUG = "apellidos";
    public static final String KEY_TLFONO_JUG = "telefono";
    public static final String KEY_EMAIL_JUG = "email";
    public static final String KEY_POSICION = "posicion";
	public static final String KEY_CARACTERISTICAS = "caracteristicas";
	public static final String KEY_EQUIPO_ID = "equipoid";
	public static final String KEY_ROWID_JUG = "_id";

    
	//KEYs de las tabla de equipos
	public static final String KEY_LOGO = "ruta_imagen";
    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_DIVISION = "division";
    //
    public static final String KEY_ROWID = "_id";
    

	//KEYs de la tabla de ejercicios
	public static final String KEY_EJERCICIO_NOMBRE = "nombre";
	public static final String KEY_EJERCICIO_NJUGADORES = "njugadores";
	public static final String KEY_EJERCICIO_DESCRIPCION = "descripcion";
	public static final String KEY_EJERCICIO_TIPO = "tipoejercicio";
	public static final String KEY_EJERCICIO_DIA = "dia_ejercicio";
	public static final String KEY_ROWID_EJ = "_id";
	
	//KEYs de la tabla de entrenamientos
	public static final String KEY_ENTRENAMIENTO_NOMBRE = "nombre";
	public static final String KEY_ENTRENAMIENTO_EQUIPOID = "equipoid";
	public static final String KEY_ENTRENAMIENTO_FECHA = "fecha_entrenamiento";
	public static final String KEY_ENTRENAMIENTO_HORA = "hora_entrenamiento";
	public static final String KEY_ROWID_EN = "_id";
	
    
    private static final String DATABASE_TABLE_EQUIPOS = "equipos";
    private static final String DATABASE_TABLE_JUGADORES = "jugadores";
    private static final String DATABASE_TABLE_EJERCICIOS = "ejercicios";
    private static final String DATABASE_TABLE_ENTRENAMIENTOS = "entrenamientos";

	private Context context;
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	
	public CoachDBAdapter(Context context) 
	{
		this.context = context;
	}
	
	public CoachDBAdapter open() throws SQLException 
	{
		dbHelper = new DataBaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}
 
	public void close() 
	{
		dbHelper.close();
	}

	/*========================Metodos para los jugadores===========================*/

	public long createJugador(long rowId, String ruta_imagen, String nombre, String apellidos, String email, 
			String posicion, String caracteristicas, String telefono)
	{
    ContentValues values_jug = new ContentValues();
    values_jug.put(KEY_EQUIPO_ID, rowId);
    values_jug.put(KEY_RUTA_IMG, ruta_imagen);
    values_jug.put(KEY_NOMBRE_JUG, nombre);
    values_jug.put(KEY_APELLIDOS_JUG, apellidos);
    values_jug.put(KEY_POSICION, posicion);
    values_jug.put(KEY_TLFONO_JUG, telefono);
    values_jug.put(KEY_EMAIL_JUG, email);
    values_jug.put(KEY_CARACTERISTICAS, caracteristicas);
    
    return database.insert(DATABASE_TABLE_JUGADORES, null, values_jug);

	}
	
	
	 public boolean updateJugador(long rowId, String ruta_imagen, String nombre, String apellidos, String email, 
			 String posicion, String caracteristicas, String telefono)
	 {
	        ContentValues updateValues_jug = new ContentValues();
	          
	        updateValues_jug.put(KEY_RUTA_IMG, ruta_imagen);
	        updateValues_jug.put(KEY_NOMBRE_JUG, nombre);
	        updateValues_jug.put(KEY_APELLIDOS_JUG, apellidos);
	        updateValues_jug.put(KEY_EMAIL_JUG, email);
	        updateValues_jug.put(KEY_POSICION, posicion);
	        updateValues_jug.put(KEY_CARACTERISTICAS, caracteristicas);
	        updateValues_jug.put(KEY_TLFONO_JUG, telefono);
	        
	        return database.update(DATABASE_TABLE_JUGADORES, updateValues_jug, KEY_ROWID + "=" + rowId, null) > 0;
	 }
	
	public Cursor fetchAllJugadores(long rowId)
	{
        return database.query(DATABASE_TABLE_JUGADORES, new String[] {KEY_ROWID_JUG, KEY_RUTA_IMG, KEY_NOMBRE_JUG, KEY_APELLIDOS_JUG, KEY_POSICION, KEY_TLFONO_JUG, KEY_EMAIL_JUG, 
				KEY_CARACTERISTICAS}, KEY_EQUIPO_ID + "=" + rowId, null, null, null, null);
        
	}
        
 
	 public Cursor fetchJugador(long rowId) throws SQLException 
	 {
		 Cursor mCursor = database.query(true, DATABASE_TABLE_JUGADORES, new String[] 
				 {KEY_ROWID_JUG,  KEY_RUTA_IMG, KEY_NOMBRE_JUG, KEY_APELLIDOS_JUG, KEY_POSICION, KEY_TLFONO_JUG, KEY_EMAIL_JUG, 
					KEY_CARACTERISTICAS}, KEY_ROWID_JUG + "=" + rowId, null, null, null, null, null);
		 
						if (mCursor != null) 
						{
							mCursor.moveToFirst();
						}
						
					return mCursor;				
	  }
	
	 
	 public boolean deleteJugador(long rowId) 
		{
		 	return database.delete(DATABASE_TABLE_JUGADORES, KEY_ROWID_JUG + "=" + rowId, null) > 0;
		}
	
	/* =====================Metodos para los equipos=============================== */
	
	public long createEquipo(String ruta_imagen, String nombre, String division) 
	{
		ContentValues values = new ContentValues();
		values.put(KEY_LOGO, ruta_imagen);
        values.put(KEY_NOMBRE, nombre);
		values.put(KEY_DIVISION, division);
		return database.insert(DATABASE_TABLE_EQUIPOS, null, values);
	}
 
	//Actualiza la tarea
	public boolean updateEquipo(long rowId, String ruta_imagen, String nombre, String division) 
	{
		ContentValues updateValues =  new ContentValues();
		updateValues.put(KEY_LOGO, ruta_imagen);
		updateValues.put(KEY_NOMBRE, nombre);
        updateValues.put(KEY_DIVISION, division);

        return database.update(DATABASE_TABLE_EQUIPOS, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
 
	//Borra la tarea
	public boolean deleteEquipo(long rowId) 
	{
        return database.delete(DATABASE_TABLE_EQUIPOS, KEY_ROWID + "=" + rowId, null) > 0;
	}
 
	//Returna un Cursor que contiene todos los items
	public Cursor fetchAllEquipos() 
	{
        return database.query(DATABASE_TABLE_EQUIPOS, new String[] {KEY_LOGO, KEY_ROWID, KEY_NOMBRE,
                KEY_DIVISION}, null, null, null, null, null);
   }
	
	public ArrayList<String> listaDeEquipos() 
	{
		ArrayList<String> equipos = new ArrayList<String>();
		
		Cursor e = database.query(DATABASE_TABLE_EQUIPOS, new String[] {KEY_LOGO, KEY_ROWID, KEY_NOMBRE,
                KEY_DIVISION}, null, null, null,
				null, null, null);

		int i, total = e.getCount();

		if (total > 0) 
		{

			for (i = 1; i <= total; i++) 
			{
				equipos.add(getNombreEquipo(i));
			}			

			e.close();

			return equipos;

		} 
		/*else 
		{

			e.close();
			return equipos;
		}*/
		return equipos;
	}
	
	public String getNombreEquipo(int id) 
	{

		// TODO Auto-generated method stub
		Cursor c = database.query(DATABASE_TABLE_EQUIPOS, new String[] {KEY_LOGO, KEY_ROWID, KEY_NOMBRE,
                KEY_DIVISION}, KEY_ROWID + "=" + id, null, null, null, null, null);
		String nEquipo = null;

		if (c != null) 
		{
			c.moveToFirst();

			try 
			{
				nEquipo = c.getString(1);
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				c.close();
				//return vacia;
			}
			c.close();
			return nEquipo;
		}
return nEquipo;
		//return vacia;
	}
	  
	
	
 
	//Returna un Cursor que contiene la info del item
	public Cursor fetchEquipo(long rowId) throws SQLException 
	{
		Cursor mCursor = database.query(true, DATABASE_TABLE_EQUIPOS, new String[] 
		{ KEY_LOGO, KEY_ROWID, KEY_NOMBRE, KEY_DIVISION}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
			if (mCursor != null) 
			{
				mCursor.moveToFirst();
			}
			
		return mCursor;
	}
	
 
	 /* MŽtodos para los entrenamientos */
	    

	    public long createEntrenamiento(long rowId,String nombre, 
	    		String dia_entrenamiento, String time_entrenamiento)
	    {
	    	ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_ENTRENAMIENTO_EQUIPOID, rowId);
	        initialValues.put(KEY_ENTRENAMIENTO_NOMBRE, nombre);
	        initialValues.put(KEY_ENTRENAMIENTO_FECHA, dia_entrenamiento);
	        initialValues.put(KEY_ENTRENAMIENTO_HORA, time_entrenamiento);

	        return database.insert(DATABASE_TABLE_ENTRENAMIENTOS, null, initialValues);
	    }

	    /**
	     * Delete the note with the given rowId
	     * 
	     * @param rowId id of note to delete
	     * @return true if deleted, false otherwise
	     */
	    public boolean deleteEntrenamiento(long rowId) 
	    {

	        return database.delete(DATABASE_TABLE_ENTRENAMIENTOS, KEY_ROWID_EN + "=" + rowId, null) > 0;
	    }

	    /**
	     * Return a Cursor over the list of all notes in the database
	     * 
	     * @return Cursor over all notes
	     */
	    public Cursor fetchAllEntrenamientos(long rowId) 
	    {

	        return database.query(DATABASE_TABLE_ENTRENAMIENTOS, new String[] {KEY_ROWID_EN, KEY_ENTRENAMIENTO_NOMBRE,
	                KEY_ENTRENAMIENTO_FECHA, KEY_ENTRENAMIENTO_HORA}, KEY_ENTRENAMIENTO_EQUIPOID + "=" + rowId, null, null, null, null);
	    }
	    
	    public Cursor fetchAllEntrenamientosSin() 
	    {

	        return database.query(DATABASE_TABLE_ENTRENAMIENTOS, new String[] {KEY_ROWID_EN, KEY_ENTRENAMIENTO_NOMBRE,
	                KEY_ENTRENAMIENTO_FECHA, KEY_ENTRENAMIENTO_HORA}, null, null, null, null, null);
	    }

	    /**
	     * Return a Cursor positioned at the note that matches the given rowId
	     * 
	     * @param rowId id of note to retrieve
	     * @return Cursor positioned to matching note, if found
	     * @throws SQLException if note could not be found/retrieved
	     */
	    public Cursor fetchEntrenamiento(long rowId) throws SQLException 
	    {

	        Cursor mCursor = database.query(true, DATABASE_TABLE_ENTRENAMIENTOS, new 
	        		String[] {KEY_ROWID_EN, KEY_ENTRENAMIENTO_NOMBRE,
	                    KEY_ENTRENAMIENTO_FECHA, KEY_ENTRENAMIENTO_HORA},
	                    KEY_ROWID_EN + "=" + rowId, null, null, null, null, null);
	        if (mCursor != null) 
	        {
	            mCursor.moveToFirst();
	        }
	        return mCursor;

	    }
	    
	    public Cursor fetchEntrenamientosDia(String fecha_selecc) throws SQLException 
	    {

	    	 return database.query(DATABASE_TABLE_ENTRENAMIENTOS, new String[] {KEY_ROWID_EN, KEY_ENTRENAMIENTO_NOMBRE,
		                KEY_ENTRENAMIENTO_FECHA, KEY_ENTRENAMIENTO_HORA}, KEY_ENTRENAMIENTO_FECHA + "=" + fecha_selecc, null, null, null, null);

	    }
	    
	    public String getEntrenamientosDia(long id) 
		{

			// TODO Auto-generated method stub
			Cursor c = database.query(DATABASE_TABLE_ENTRENAMIENTOS, new String[] {KEY_ROWID_EN, KEY_ENTRENAMIENTO_NOMBRE,
	                KEY_ENTRENAMIENTO_FECHA, KEY_ENTRENAMIENTO_HORA}, KEY_ENTRENAMIENTO_FECHA + "=" + id, null, null, null, null, null);
			String nEntrenamiento = null;

			if (c != null) 
			{
				c.moveToFirst();

				try 
				{
					nEntrenamiento = c.getString(1);
				} catch (Exception e) 
				{
					// TODO Auto-generated catch block
					c.close();
					//return vacia;
				}
				c.close();
				return nEntrenamiento;
			}
			return nEntrenamiento;
			//return vacia;
		}

	    /**
	     * Update the note using the details provided. The note to be updated is
	     * specified using the rowId, and it is altered to use the title and body
	     * values passed in
	     * 
	     * @param rowId id of note to update
	     * @param title value to set note title to
	     * @param body value to set note body to
	     * @return true if the note was successfully updated, false otherwise
	     */
	    public boolean updateEntrenamiento(long rowId, String nombre, 
	    		String dia_entrenamiento, String time_entrenamiento)
	    {
	        ContentValues args = new ContentValues();
	        args.put(KEY_ENTRENAMIENTO_NOMBRE, nombre);
	        args.put(KEY_ENTRENAMIENTO_FECHA, dia_entrenamiento);
	        args.put(KEY_ENTRENAMIENTO_HORA, time_entrenamiento);
	        
	        return database.update(DATABASE_TABLE_ENTRENAMIENTOS, args, KEY_ROWID_EN + "=" + rowId, null) > 0;
	    }
	    
	    
	  //FUNCIONES PARA LAS SENTENCIAS DE LOS EJERCICIOS
	  		/* Insertar nuevo ejercicio */
	  		public long crearEjercicio(String nombre, String njugadores, String descripcion)
	  		{
	  			ContentValues values = new ContentValues();
	  			values.put(KEY_EJERCICIO_NOMBRE, nombre);
	  			values.put(KEY_EJERCICIO_NJUGADORES, njugadores);
	  			values.put(KEY_EJERCICIO_DESCRIPCION, descripcion);
	  			
	  			/*, String tipoejercicio, String dia_ejercicio
	  			values.put(KEY_EJERCICIO_TIPO, tipoejercicio);
	  			values.put(KEY_EJERCICIO_DIA, dia_ejercicio);*/

	  			return database.insert(DATABASE_TABLE_EJERCICIOS, null, values);
	  		}
	  		public long crearEjercicio(String nombreFichero, String rutaYFichero,  String descripcion,  String rutaYFicheroImagen,  String tipoEjercicio)
	  		{
	  			ContentValues values = new ContentValues();
	  			values.put(KEY_EJERCICIO_NOMBRE, nombreFichero);
	  			values.put(KEY_EJERCICIO_NJUGADORES, rutaYFichero);
	  			values.put(KEY_EJERCICIO_DESCRIPCION, descripcion);
	  			
	  			return database.insert(DATABASE_TABLE_EJERCICIOS, null, values);
	  		}
	  		
	  		
	  		
	  		public boolean updateEjercicio(long rowid, String nombre, String njugadores, String descripcion)
	  		{
	  			ContentValues args = new ContentValues();
	  			args.put(KEY_EJERCICIO_NOMBRE, nombre);
	  			args.put(KEY_EJERCICIO_NJUGADORES, njugadores);
	  			args.put(KEY_EJERCICIO_DESCRIPCION, descripcion);
	  			/*, String tipoejercicio, String dia_ejercicio
	  			args.put(KEY_EJERCICIO_TIPO, tipoejercicio);
	  			args.put(KEY_EJERCICIO_DIA, dia_ejercicio);*/
	  			
	  			return database.update(DATABASE_TABLE_EJERCICIOS, args, KEY_ROWID_EJ + "=" + rowid, null) > 0;
	  		}
	  		
	  		/* Borrar Ejercicio de la tabla */
	  		public boolean borrarEjercicio(long rowid)
	  		{
	  			return database.delete(DATABASE_TABLE_EJERCICIOS, KEY_ROWID_EJ + "=" + rowid, null) > 0;
	  		}
	  		/*Creacion try catch*/
	  		public Cursor getAllEjercicios()
	  		{
	  			
	  			//, KEY_EJERCICIO_TIPO, KEY_EJERCICIO_DIA
	  			return database.query(DATABASE_TABLE_EJERCICIOS, new String[]{KEY_ROWID_EJ, KEY_EJERCICIO_NOMBRE, KEY_EJERCICIO_NJUGADORES, 
	  					KEY_EJERCICIO_DESCRIPCION},	null, null, null, null, null);
	  		}
	  		
	  		public Cursor getEjercicio(long rowid) throws SQLException
	  		{
	  			//, KEY_EJERCICIO_TIPO, KEY_EJERCICIO_DIA
	  			
	  			Cursor mCursor = database.query(true, DATABASE_TABLE_EJERCICIOS, new String[]{KEY_ROWID_EJ, KEY_EJERCICIO_NOMBRE, KEY_EJERCICIO_NJUGADORES, 
	  					KEY_EJERCICIO_DESCRIPCION}, KEY_ROWID_EJ + "=" + rowid, 
	  					null, null, null, null, null);
	  			if(mCursor != null)
	  				mCursor.moveToFirst();
	  			
	  			return mCursor;
	  		}

}
	
	
