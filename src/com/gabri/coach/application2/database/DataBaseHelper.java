package com.gabri.coach.application2.database;

//Rodrigo Sanchez Monroy
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;



//Esta clase nos ayudará a definir la estructura de la base de datos

public class DataBaseHelper extends SQLiteOpenHelper 
{
    
	//KEYS de la taba del spinner divisiones
	public static final String KEY_SPINNER = "divisiones";
	public static final String KEY_ROWID_SPINNER = "_id";
	
	//KEYS de la taba del spinner spinner_posiciones
	public static final String KEY_SPINNER_POSICIONES = "posiciones";
	public static final String KEY_ROWID_SPINNER_POSICIONES = "_id";
		 
 
    private static final String DATABASE_TABLE_SPINNER = "spinner";
    private static final String DATABASE_TABLE_SPINNER_POSICIONES = "spinner_posiciones";

    
		private static final String DATABASE_NAME = "applicationdata";
		private static final int DATABASE_VERSION = 2;
		private Context contexto;
	    
		//Consulta para crear la base de datos
		
		private static final String DATABASE_CREATE_EQUIPOS = "create table equipos (_id integer primary key autoincrement, " + "ruta_imagen TEXT, nombre TEXT, division TEXT);";
		private static final String DATABASE_CREATE_SPINNER = "create table spinner (_id integer primary key autoincrement, " + "spinner TEXT);";
		private static final String DATABASE_CREATE_SPINNER_POSICIONES = "create table spinner_posiciones (_id integer primary key autoincrement, " + "spinner_posiciones TEXT);";
		private static final String DATABASE_CREATE_JUGADORES ="create table jugadores (_id integer primary key autoincrement, "
		        + " ruta_imagen TEXT,  apellidos TEXT, posicion TEXT, telefono TEXT, email TEXT, caracteristicas TEXT,nombre TEXT,equipoid integer not null);";
		private static final String DATABASE_CREATE_EJERCICIOS = "create table ejercicios (_id integer primary key autoincrement, "
		        + "nombre text not null, njugadores text not null, descripcion text not null);";
		private static final String DATABASE_CREATE_ENTRENAMIENTOS =
			     "create table entrenamientos (_id integer primary key autoincrement, "
			    + "nombre text not null, equipoid integer not null, fecha_entrenamiento TEXT, hora_entrenamiento TEXT);";
	 
		// constructor obligatorio
		public DataBaseHelper(Context context) 
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.contexto = context;
		}
	 
		// Este método sobrecargado se llama al momento en el que se crea la BD
		// crea las tablas si no existen
		@Override
		public void onCreate(SQLiteDatabase database) 
		{
			database.execSQL(DATABASE_CREATE_EQUIPOS);
			database.execSQL(DATABASE_CREATE_SPINNER);
			
			database.execSQL("INSERT INTO spinner(_id, spinner) VALUES(1,'')");
			database.execSQL("INSERT INTO spinner(_id, spinner) VALUES(2,'Primera Division')");
			database.execSQL("INSERT INTO spinner(_id, spinner) VALUES(3,'Segunda Division')");
			database.execSQL("INSERT INTO spinner(_id, spinner) VALUES(4,'Segunda Division B')");
			database.execSQL("INSERT INTO spinner(_id, spinner) VALUES(5,'Tercera')");
			database.execSQL("INSERT INTO spinner(_id, spinner) VALUES(6,'Regional')");
			    
			database.execSQL(DATABASE_CREATE_SPINNER_POSICIONES);
			
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(1,'')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(2,'POR')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(3,'DF')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(4,'LD')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(5,'MP')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(6,'MC')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(7,'MCD')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(8,'MD')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(9,'MI')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(10,'CRR')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(11,'DC')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(12,'ED')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(13,'EI')");
			database.execSQL("INSERT INTO spinner_posiciones(_id, spinner_posiciones) VALUES(14,'SP')");

			database.execSQL(DATABASE_CREATE_JUGADORES);
			database.execSQL(DATABASE_CREATE_EJERCICIOS);
			database.execSQL(DATABASE_CREATE_ENTRENAMIENTOS);
		}
	 
		// Método sobrecargado que se llama cada vez que se actualiza la BD
		// Sirve para manejar las versiones de la misma.
		// alter table de sql.
		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) 
		{
			Log.w(DataBaseHelper.class.getName(),"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS equipos");
			database.execSQL("DROP TABLE IF EXISTS spinner");
			database.execSQL("DROP TABLE IF EXISTS spinner_posiciones");
			database.execSQL("DROP TABLE IF EXISTS jugadores");
			database.execSQL("DROP TABLE IF EXISTS ejercicios");
			database.execSQL("DROP TABLE IF EXISTS entrenamientos");
			
			
			
			onCreate(database);
		}
		
		
		
		/*============================== METODOS PARA EL SPINNER ==============================*/
		// método para insertar Divisiones de fútbol, el parámetro que recibe es la cadena alfanumérica con la división a insertar
		
		public void insertDivisiones(String divisiones) 
		{
		  //instancia de la clase SQLiteDatabase. Asignamos el método de escritura en BD.
		  SQLiteDatabase db = this.getWritableDatabase();
            
		  //OJO encapsulo la base de datos por si acaso hay algún problema al abrirla 
		  if(db!=null){
			//instancia de la clase ContentValues para almacenar conjuntos de datos 
		    ContentValues values = new ContentValues();
			values.put(DATABASE_TABLE_SPINNER, divisiones); //put(key,value) donde almaceno y el valor a almacenar.
			db.insert(DATABASE_TABLE_SPINNER, null, values); //nombre de la tabla , si necesitamos insertar valores nulos (nullColumnHack) deja pasar nulo,el ContentValues
		
			db.close(); //cerrar la instancia de la base de datos en ejecucción
	  	  }
		}
		
		public void deleteDivisiones() 
		{
		   //delete(tabla, claúsula where, argumentos where)
		   this.getWritableDatabase().delete(DATABASE_TABLE_SPINNER, null, null);
		}

		public Set<String> getAllDivisiones() 
		{
			Set<String> set = new HashSet<String>();
			
			String selectQuery = "select * from " + DATABASE_TABLE_SPINNER+ " ORDER BY "+ KEY_ROWID_SPINNER+" DESC";
			
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) 
			{
				do 
				{
					set.add(cursor.getString(1));
				} while (cursor.moveToNext());
			}

			cursor.close();
			db.close();

			return set;
		}
			
		/*========================Fin metodos spinner===========================*/
		
		 
	/*============================== METODOS PARA EL SPINNER POSICIONES CAMPO JUGADORES ==============================*/
		
		/*public void insertPosiciones(String posiciones) 
		{
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(DATABASE_TABLE_SPINNER_POSICIONES, posiciones);
			db.insert(DATABASE_TABLE_SPINNER_POSICIONES, null, values);
		
			db.close();
		}
		
		public void deleteDivisiones() 
		{
		   
		   this.getWritableDatabase().delete(DATABASE_TABLE_SPINNER, null, null);
		}*/

		public Set<String> getAllPosiciones()
		{
			Set<String> set = new HashSet<String>();

			String selectQuery = "select * from " + DATABASE_TABLE_SPINNER_POSICIONES+ " ORDER BY "+ KEY_ROWID_SPINNER_POSICIONES+" DESC";

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) 
			{
				do 
				{
					set.add(cursor.getString(1));
				} while (cursor.moveToNext());
			}

			cursor.close();
			db.close();

			return set;
		}
			
		/*========================Fin metodos spinner posiciones campo===========================*/
		/* public Jugadores fetchUnJugador(long rowId)
			{
				
				String[] columnas = new String[]{KEY_ROWID_JUG, KEY_NOMBRE_JUG, KEY_APELLIDOS_JUG, KEY_POSICION, KEY_TLFONO_JUG, KEY_EMAIL_JUG, KEY_CARACTERISTICAS, KEY_RUTA_IMG};
				
				Cursor cursor = this.getReadableDatabase().query(DATABASE_TABLE_JUGADORES, columnas, KEY_EQUIPOID + "=" + rowId, null, null, null, null);
				
				if(cursor != null) 
				{
				    cursor.moveToFirst();
				}
				return cursor;
			}
		 */
		 
		
	}

