package com.gabri.coach.application2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.OutputStream;
import java.io.BufferedOutputStream;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnTouchListener;
//import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
//import android.widget.Toast;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import com.actionbarsherlock.app.SherlockActivity;
import android.content.Context;
import com.actionbarsherlock.app.ActionBar;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.gabri.coach.application2.DialogoColor;
import com.gabri.coach.application2.DialogoColor.OnChangeColor;
import com.gabri.coach.application2.database.CoachDBAdapter;
//mport com.actionbarsherlock.view.onOptionsItemSelected;

//import com.rodri.coach.application2.PizarraLayout;
import com.rodri.coach.application2.R;

import java.awt.*;


public class PizarraCoach extends SherlockActivity implements OnTouchListener, OnLongClickListener
{
	 RelativeLayout pizarraCoach;
	 //public static final int FREE_DRAWING = 1000;
	 //public static final int NO_PINTA = 1001;
	
	 //Definimos la imagen que vamos a arrastrar
	 PizarraCanvas p;
	 //private ImageView imagen;
	 //Variables para centrar la imagen bajo el dedo
	 private int xDelta;
	 private int yDelta;
	 //private Paint mPaint;
	 //private Canvas canvas;
	 Bitmap bmp = null;
	 Bitmap bmp2 = null;
	 int anchoPizarra, altoPizarra;
	 boolean balon = false; //flag
	 boolean lapiz = false; //desactivado / activado de dibujo

	 boolean longClick = true;
	 int anchoCamiseta, altoCamiseta, anchoBalon, altoBalon, anchoCono, altoCono;
	 int objetoActivo = -1;
	 int objetoP = 0;
	 int id=-1, idBalon=0;
	 int numJugadores1 = 0, numJugadores2=0, numConos=0, numBalones=0;
	 int objTotales =0 ;
	 int campo=1;
	 String descripcion;
	 String tipoEjercicio;
	 String nombreFichero;
	 String nombreFicheroImagen;
	 String rutaFichero;
	 String rutaImagen;
	 String rutaYFichero;
	 String rutaYFicheroImagen;
	 Bitmap bitmap;
	 int idEjercicio=0;
	 
	 private static final int camiseta1 = 0;
	 private static final int camiseta2 = 1;
	 private static final int balonn = 2;
	 private static final int conoo = 3;
 	 private static final int lineaa = 4;
	
	 private ArrayList<Jugador1Drawing> jugadores1;
	 private ArrayList<Jugador2Drawing> jugadores2;
	 private ArrayList<Balon> balones;
	 private ArrayList<Cono> conos;
	 private ArrayList<Point> points;
	 private ArrayList<LineDrawing> lineas;
	 private ArrayList<Integer> objetosTotales;
	 
	 
	 Button btnLapiz;
	 static final String noname = "";
	 private String color = "000000";
	 ImageButton btnGeneraJugador1, btnGeneraJugador2;
	 private ObjectOutputStream objeto;
	 private CoachDBAdapter mDbHelper;
	 
	 
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		//setTheme(CoachActivity.TEMA);
        super.onCreate(savedInstanceState);
        
        ActionBar ab = getSupportActionBar();		
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);			
		ab.setDisplayHomeAsUpEnabled(true);
		
        setContentView(R.layout.pizarra_main);
        
        pizarraCoach = (RelativeLayout)findViewById(R.id.viewlayout);
        
        p = new PizarraCanvas(this, campo);
        p.setOnLongClickListener(this);
		p.setOnTouchListener(this);
        
        btnGeneraJugador1 = (ImageButton)findViewById(R.id.btn_genera_jugador1);
        btnGeneraJugador2 = (ImageButton)findViewById(R.id.btn_genera_jugador2);
        ImageButton btnGeneraBalon = (ImageButton)findViewById(R.id.btn_genera_balon);
        ImageButton btnGeneraCono = (ImageButton)findViewById(R.id.btn_genera_cono);
        Button btnDeshacer = (Button)findViewById(R.id.deshacer);
        btnLapiz = (Button)findViewById(R.id.btn_dibujo_libre); 
        
        //surfaceViewFrame = (RelativeLayout) findViewById(R.id.surfaceviewFrame);
		//view = new CanvasView(this);
		//surfaceViewFrame.addView(view, 0);
		        
       // p.setOnLongClickListener(this);
        pizarraCoach.addView(p);
        
        
    	jugadores1 = new ArrayList<Jugador1Drawing>();
		jugadores2 = new ArrayList<Jugador2Drawing>();
		balones = new ArrayList<Balon>();
		lineas = new ArrayList<LineDrawing>();
		conos = new ArrayList<Cono>();
		objetosTotales = new ArrayList<Integer>();
		      
        //Creamos la imagen
    	btnGeneraJugador1.setOnClickListener(new OnClickListener() 
    	{
    		@Override
    	    public void onClick(View v)
    	    {
    			 GeneraImagenJugador1();
    	    }
    	});
    	
    	btnGeneraJugador2.setOnClickListener(new OnClickListener() 
    	{
    		@Override
    	    public void onClick(View v)
    	    {
    			 GeneraImagenJugador2();
    	    }
    	});
    	
    	btnGeneraBalon.setOnClickListener(new OnClickListener() 
    	{
    		@Override
    	    public void onClick(View v)
    	    {
    			 GeneraImagenBalon();
    	    }
    	});
    	
    	btnGeneraCono.setOnClickListener(new OnClickListener() 
    	{
    		@Override
    	    public void onClick(View v)
    	    {
    			 GeneraImagenCono();
    	    }
    	});
    	
    	btnLapiz.setOnClickListener(new OnClickListener() 
    	{
    		@Override
    	    public void onClick(View v)
    	    {
    			Lapiz();
    	    }
    	});       
    
    	btnDeshacer.setOnClickListener(new OnClickListener() 
    	{
    		@Override
    	    public void onClick(View v)
    	    {
    			Deshacer();
    	    }
    	});       
    	
    	
	}
	 
	
	public void GeneraImagenJugador1()
    {
		btnLapiz.setBackgroundDrawable(getResources().getDrawable(R.drawable.lapiz));
		lapiz = false;
		int xJugador1 = 100;
		int yJugador1 = 270;

		if (numJugadores1 < 11) 
		{
			
			id = jugadores1.size();
			
			numJugadores1++;
			
			Jugador1Drawing j1 = new Jugador1Drawing(this, id, xJugador1, yJugador1);
			jugadores1.add(j1);
			pizarraCoach.addView(j1);
			pizarraCoach.invalidate();
			id++;
			objetosTotales.add(camiseta1);
			objTotales++;
			anchoCamiseta = j1.getAnchoCamiseta();
			altoCamiseta = j1.getAltoCamiseta();
			

		}
		else 
		{
				Toast.makeText(this, "¡Ya existen 11 jugadores sobre el terreno!", Toast.LENGTH_SHORT)
						.show();
		}
		
    }
	
	public void GeneraImagenJugador2()
    {
		btnLapiz.setBackgroundDrawable(getResources().getDrawable(R.drawable.lapiz));
		lapiz = false;
		int xJugador2 = 100;
		int yJugador2 = 200;

		if (numJugadores2 < 11) 
		{
			
			id = jugadores2.size();
			
			numJugadores2++;
			
			Jugador2Drawing j2 = new Jugador2Drawing(this, id, xJugador2, yJugador2);
			jugadores2.add(j2);
			pizarraCoach.addView(j2);
			pizarraCoach.invalidate();
			id++;
			objetosTotales.add(camiseta2);
			objTotales++;
			anchoCamiseta = j2.getAnchoCamiseta();
			altoCamiseta = j2.getAltoCamiseta();			
		}
		
		else 
		{
				Toast.makeText(this, "¡Ya existen 11 jugadores sobre el terreno!", Toast.LENGTH_SHORT)
						.show();
		}
		
    }
	
	public void GeneraImagenBalon()
    {
		btnLapiz.setBackgroundDrawable(getResources().getDrawable(R.drawable.lapiz));		
		lapiz = false;
		int xBalon = 100;
		int yBalon = 320;

		if (!balon) 
		{
			idBalon = balones.size();
			
			numBalones++;
			
			Balon b = new Balon(this, idBalon, xBalon, yBalon);
			balones.add(b);
			pizarraCoach.addView(b);
			pizarraCoach.invalidate();
			idBalon++;
			objetosTotales.add(balonn);
			objTotales++;

			balon = true;
			anchoBalon = b.getAncho(); 
			altoBalon = b.getAlto();
		} 
		else 
		{
			Toast.makeText(this, "¡Ya existe un balon sobre el campo!", Toast.LENGTH_SHORT).show();
		}
		
    }
	
	public void GeneraImagenCono()
    {
		btnLapiz.setBackgroundDrawable(getResources().getDrawable(R.drawable.lapiz));
		lapiz = false;
		
		int xCono = 180;
		int yCono = 320;

		if (numConos<20) 
		{

			id = conos.size();
			
			numConos++;
			
			Cono c = new Cono(this, id, xCono, yCono);
			conos.add(c);
			pizarraCoach.addView(c);
			pizarraCoach.invalidate();
			id++;
			objetosTotales.add(conoo);
			objTotales++;

			//balon = true;
			anchoCono = c.getAncho(); 
			altoCono = c.getAlto();
		} 
		else 
		{
			Toast.makeText(this, "¡Ya existen suficientes conos sobre el campo!", Toast.LENGTH_SHORT).show();
		}
		
    }
	
	@SuppressWarnings("deprecation")
	public void Lapiz()
    {	
		if (lapiz) 
		{
			btnLapiz.setBackgroundDrawable(getResources().getDrawable(R.drawable.lapiz));
			lapiz = false;
		} 
		else 
		{
			btnLapiz.setBackgroundDrawable(getResources().getDrawable(R.drawable.lapiz2));
			lapiz = true;
		}
    }
	
	
	public void Deshacer()
	{
		if (objTotales > 0) 
		{
			
			int ultimaView = objTotales; 
			
			switch (objetosTotales.get(objTotales-1)) 
			{

			case camiseta1:

				if (!jugadores1.isEmpty()) 
				{

					int ultimo = jugadores1.size() - 1;
					pizarraCoach.removeViewAt(ultimaView);
					jugadores1.remove(ultimo);
					numJugadores1--;
					objTotales--;
					pizarraCoach.invalidate();
				}
				
			break;
			
			case camiseta2:

				if (!jugadores2.isEmpty())
				{

					int ultimo = jugadores2.size() - 1;
					pizarraCoach.removeViewAt(ultimaView);
					jugadores2.remove(ultimo);
					numJugadores2--;
					objTotales--;
					pizarraCoach.invalidate();
				}
				
			break;

			case balonn:

				if (!balones.isEmpty()) 
				{

					pizarraCoach.removeViewAt(ultimaView);
					
					balones.remove(0);
					balon = false;
					numBalones--;
					objTotales--;
					pizarraCoach.invalidate();
				}

			break;
			
			case conoo:

				if (!conos.isEmpty()) 
				{
					int ultimo = conos.size() - 1;
					pizarraCoach.removeViewAt(ultimaView);					
					conos.remove(ultimo);
					numConos--;
					objTotales--;
					pizarraCoach.invalidate();
				}

			break;
	
			case lineaa:
									
				if(!lineas.isEmpty())
				{
					
				int ultimo = lineas.size() -1;
				pizarraCoach.removeViewAt(ultimaView);
				lineas.remove(ultimo);
				objTotales--;
				pizarraCoach.invalidate();
				}
			break;
			}
			
			int ultima = objetosTotales.size()-1;
			objetosTotales.remove(ultima);
		
		}

}
	
	@Override
    public boolean onTouch(View view, MotionEvent event) 
    {
        	  //Recogemos las coordenadas del dedo
        	  final int X = (int) event.getX();
        	  final int Y = (int) event.getY();
        	          	  
      		  Balon movBalon = null;
      		  Jugador1Drawing movJug1 = null;
      		  Jugador2Drawing movJug2 = null;
      		  Cono movCono= null;

      		
        	 // int pintar=0;
			//Dependiendo de la accion recogida..
        	  switch (event.getAction()) 
        	  {
        	      //Al tocar la pantalla
        	     case MotionEvent.ACTION_DOWN:
        	    	 longClick = true;
        	    	 
        				if (lapiz) 
        				{
        					//crear una linea -----> arrayList<Punto>				
        					points = new ArrayList<Point>();
        					Point point = new Point(event.getX(), event.getY());
        					points.add(point);
        					
        					return true;
        				}         				      			
        				else 
        				{
        					
        					for (Jugador1Drawing j1 : jugadores1) 
        					{
        						if (j1 instanceof Jugador1Drawing) 
        						{
        							movJug1 = j1;
        							
        							if (X > j1.getX() - anchoCamiseta / 2
        									&& X  < j1.getX() + anchoCamiseta / 2       									
        									&& Y > j1.getY() - altoCamiseta / 2
        									&& Y < j1.getY() + altoCamiseta / 2) 
        							{

        								objetoP = 1;
        								objetoActivo = j1.getId();
        								break;
        							}
        						}
        					}
        					 
        					for (Jugador2Drawing j2 : jugadores2) 
        					{
        						if (j2 instanceof Jugador2Drawing) 
        						{
        							movJug2 = j2;
        							
        							if (X > j2.getX() - anchoCamiseta / 2
        									&& X < j2.getX() + anchoCamiseta / 2
        									&& Y > j2.getY() - altoCamiseta / 2
        									&& Y < j2.getY() + altoCamiseta / 2) 
        							{

        								objetoP = 2;
        								objetoActivo = j2.getId();
        								break;
        							}
        						}
        					}
        					
        					for (Balon b : balones) 
        					{
        						if (b instanceof Balon) 
        						{
        							movBalon = b;

        							if (X > b.getX() - anchoBalon / 2 && X < b.getX() + anchoBalon / 2
        									&& Y > b.getY() - altoBalon / 2
        									&& Y < b.getY() + altoBalon / 2) 
        							{

        								objetoP = 3;
        								objetoActivo = b.getId();
        								break;
        							}
        						}
        					}
        					
        					for (Cono c : conos) 
        					{
        						if (c instanceof Cono) 
        						{
        							movCono = c;

        							if (X > c.getX() - anchoCono / 2 && X < c.getX() + anchoCono / 2
        									&& Y > c.getY() - altoCono / 2
        									&& Y < c.getY() + altoCono / 2) 
        							{

        								objetoP = 4;
        								objetoActivo = c.getId();
        								break;
        							}
        						}
        					}
        					
        				}
        				break;

        			case MotionEvent.ACTION_MOVE:
        				longClick = false;
        				
        				if (lapiz) 
        				{    					
        					Point point = new Point(event.getX(), event.getY());
        					points.add(point);   				    					        					
        				} 
        				        			
        				else 
        				{
        					if (objetoActivo != -1) 
        					{
        						if (X < anchoPizarra - (anchoCamiseta / 2) && X > (anchoCamiseta / 2)
        								&& Y < altoPizarra - (altoCamiseta / 2) && Y > (altoCamiseta / 2)) 
        						{
        							
        							if (objetoP == 1) 
        							{
        								movJug1 = jugadores1.get(objetoActivo);
        								movJug1.setX(X);
        								movJug1.setY(Y);
        								movJug1.invalidate();
        							}  

        						    if (objetoP == 2) 
        							{
        						    	movJug2 = jugadores2.get(objetoActivo);
        						    	movJug2.setX(X);
        						    	movJug2.setY(Y);
        						    	movJug2.invalidate();
        							}
        						    
        							if (objetoP == 3) 
        							{
        								movBalon = balones.get(objetoActivo);
        								movBalon.setX(X);
        								movBalon.setY(Y);
        								movBalon.invalidate();
        							} 
        					     						
        						
        						    if (objetoP == 4) 
        							{
        						    	movCono = conos.get(objetoActivo);
        						    	movCono.setX(X);
        						    	movCono.setY(Y);
        						    	movCono.invalidate();
        							}
        						    break;
        						}
        					}
        				}
        				    			
        				break;
       				
        	
        			case MotionEvent.ACTION_UP:
        				//Toast.makeText(this, "¡entro", Toast.LENGTH_LONG).show();
        				//objetoActivo = -1;	
        				if (lapiz) 
        				{    	
        					//Toast.makeText(this, "¡entro LAPIZ", Toast.LENGTH_LONG).show();
        					Point point = new Point(event.getX(), event.getY());
        					points.add(point);    
        					
        					objetosTotales.add(lineaa);
        					objTotales++;
        					LineDrawing l = new LineDrawing(this, points, color);
        					lineas.add(l);
        					pizarraCoach.addView(l);
        					
        					pizarraCoach.invalidate();
        					
        				} 
        				
        				break;
        			}
        			return false;
        		
     }

 	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) 
	{
 			super.onCreateOptionsMenu(menu);

 			MenuInflater inflater = getSupportMenuInflater();
 			inflater.inflate(R.menu.menu_pizarra, menu);
 			
 			//return true;
 			return super.onCreateOptionsMenu(menu);
 			
 	}
  
 	@Override
 	public boolean onOptionsItemSelected(MenuItem item) 
    {
 		switch (item.getItemId())  
 		{ 	
 			case android.R.id.home:
            Intent intent = new Intent(this, CoachActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
            
 			case R.id.menu_limpiar:
 				
 				 new AlertDialog.Builder(this)
			      .setIcon(android.R.drawable.ic_dialog_alert)
			      .setTitle("Limpiar")
			      .setMessage("Desea eliminar todos los elementos de la pizarra?")
			      .setNegativeButton(android.R.string.cancel, null)//sin listener
			      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() 
			      {
			    	  //un listener que al pulsar, cierre la aplicacion
			        @Override
			        public void onClick(DialogInterface dialog, int which)
			        {
			          //Limpia pizarra
			        	if (balon) 
		 				{
							balon = false;
							balones.clear();
						}
					
						jugadores1.clear();
						jugadores2.clear();
						lineas.clear();
						conos.clear();
						pizarraCoach.removeViewsInLayout(1, objTotales);
						//pizarraCoach.removeAllViews();
						objetosTotales.clear();
						objTotales = 0; 
						numJugadores1 = 0;
						numJugadores2=0;
						numConos=0;
						numBalones=0;
						pizarraCoach.invalidate();
									 				
			        }
			      })
			      .show();
 				
			    // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
			    return true;
			    
			  
 				
 				
 			case R.id.menu_color:
 				
 				DialogoColor dialogoColor = new DialogoColor(this, "Seleccione el Color", color);
 				
 				dialogoColor.setOnchangeColor(new OnChangeColor() 
 				{
 					@Override
 					public void changeColor(String nuevo_color) 
 					{
 						// TODO Auto-generated method stub
 						color = nuevo_color;
 					}
 				});
 				dialogoColor.show();

 				break;
 				
 			case R.id.menu_guardar2:
 			
 				
 				
 				
 				Intent iG = new Intent(this, GuardarEjercicio.class);
 				startActivityForResult(iG, 1);
 				
 			break;
 			
 			case R.id.menu_abrir:
 	 			
 				Intent iC = new Intent(this, CargarEjercicio.class);
 				startActivityForResult(iC, 2);
 				
 			break;
 			
 			case R.id.menu_eliminar:
 	 			
 				Intent iB = new Intent(this, EliminarEjercicio.class);
 				startActivityForResult(iB, 3);
 				
 			break;

 		}
 		
 		return super.onOptionsItemSelected(item);
 	}
 	
 	
 	public class PizarraCanvas extends SurfaceView 
 	{
 		
 		int pizarra;
		
		public PizarraCanvas(Context context, int pizarra) 
		{
			super(context);
			
			this.pizarra = pizarra;
			
			SurfaceHolder holder = getHolder();
			holder.addCallback(new SurfaceHolder.Callback() 
			{

				@Override
				public void surfaceDestroyed(SurfaceHolder holder) 
				{
				}

				@Override
				public void surfaceCreated(SurfaceHolder holder) 
				{
					Canvas c = holder.lockCanvas(null);
					onDraw(c);
					holder.unlockCanvasAndPost(c);
				}

				@Override
				public void surfaceChanged(SurfaceHolder holder, int format,
						int width, int height) 
				{
					
				}
			});
		}
		
		@Override
		protected void onDraw(Canvas canvas) 
		{
			DisplayMetrics metrics = new DisplayMetrics();
			
			int screenHeight = metrics.heightPixels;
			int screenWidth = metrics.widthPixels;
			
			screenWidth = getWidth();
			screenHeight = getHeight();

			
			bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.img_pizarrapeq2);

			bitmap = Bitmap.createScaledBitmap(bmp, screenWidth,
					screenHeight, true);

			anchoPizarra = bitmap.getWidth();
			altoPizarra = bitmap.getHeight();

			bmp.getScaledHeight(canvas);
			bmp.getScaledWidth(canvas);
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
		
		public int getPizarra() 
		{
			return pizarra; 
		}

		public void setPizarra(int pizarra) 
		{
			
			this.pizarra = pizarra;
		}
 	
 	}

	
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		
		
		if (requestCode == 1) 
		{

		     if(resultCode == RESULT_OK)
		     {
		     OutputStream out;
		     long id=0;
		     nombreFichero = data.getStringExtra("nombreEjercicio");
		     descripcion = data.getStringExtra("descripcion");
		     tipoEjercicio = data.getStringExtra("tipoEjercicio");
		      //int[] id = null;
		      //id = data.getIntArrayExtra("ids");
		      
		     // for(int ids : id)
		      //{
		      try
		      {
		    	    rutaFichero="sdcard/CoachApplication2/Ejercicios";
					File  file=new File (rutaFichero);
					
			        if(!file.exists())
			            file.mkdirs();

			        rutaYFichero="sdcard/CoachApplication2/Ejercicios"+"/"+nombreFichero;
			        FileOutputStream ejercicio = new FileOutputStream(rutaYFichero); 
					objeto = new ObjectOutputStream(ejercicio);
					
					
					objeto.writeObject(p.getPizarra()); //Vista de la pizarrra
					
					ArrayList<InformacionObjetos> infoJugador1 = new ArrayList<InformacionObjetos>();
					ArrayList<InformacionObjetos> infoJugador2 = new ArrayList<InformacionObjetos>();
					ArrayList<InformacionObjetos> infoBalon = new ArrayList<InformacionObjetos>();
					ArrayList<InformacionObjetos> infoCono = new ArrayList<InformacionObjetos>();
					ArrayList<InformacionObjetos> infoLinea = new ArrayList<InformacionObjetos>();

					for(Jugador1Drawing j1 : jugadores1)
					{
						infoJugador1.add( new InformacionObjetos(camiseta1, j1.getId(),j1.getX(), j1.getY()));
					}
					objeto.writeObject(infoJugador1);
				
					for(Jugador2Drawing j2 : jugadores2)
					{
						infoJugador2.add(new InformacionObjetos(camiseta2, j2.getId(),j2.getX(), j2.getY()));
					}
					objeto.writeObject(infoJugador2);
	
					for(Balon balon : balones)
					{
						infoBalon.add(new InformacionObjetos(balonn,balon.getId(),balon.getX(),balon.getY()));
					}
					objeto.writeObject(infoBalon);
					
					for(Cono cono : conos)
					{
						infoCono.add(new InformacionObjetos(conoo,cono.getId(),cono.getX(),cono.getY()));
					}
					objeto.writeObject(infoCono);
					
					for(LineDrawing l : lineas)
					{
						infoLinea.add(new InformacionObjetos(l.getPoints(),l.getColor()));
					}
					objeto.writeObject(infoLinea);
					objeto.flush();
				   	objeto.close();
				   	Toast.makeText(getApplicationContext(), "¡Ejercicio '"+rutaYFichero+"' guardado con éxito!", Toast.LENGTH_SHORT).show();
		           }
		      
	      		catch (IOException e)
	      		{
					Toast.makeText(getApplicationContext(), "¡Fallo al guardar el ejercicio '"+rutaYFichero+"'!", Toast.LENGTH_SHORT).show();
				}
				   	try
				   	{
				   	
				   		//crea un nuevo directorio si no existe
				    rutaYFicheroImagen="sdcard/CoachApplication2/Imagenes";
				    File  file_imagen =new File (rutaYFicheroImagen);
				   
				    if(!file_imagen.exists())
				    	file_imagen.mkdirs();
				    
				    
				    nombreFicheroImagen=nombreFichero+".jpg"; 
				   	rutaYFicheroImagen=rutaYFicheroImagen+"/"+nombreFicheroImagen;
					Toast.makeText(getApplicationContext(), "¡Imagen '"+rutaYFicheroImagen+"' guardado con éxito!", Toast.LENGTH_SHORT).show();
					out = new BufferedOutputStream(new FileOutputStream(rutaYFicheroImagen));
					
                    //trozo antiguo
					//pizarraCoach.setDrawingCacheEnabled(true);
					//bmp2= Bitmap.createBitmap(pizarraCoach.getDrawingCache());
					//bitmap.compress(Bitmap.CompressFormat.PNG, 90 , out);
	 				//out.flush();
	 				//out.close();
	 				//pizarraCoach.setDrawingCacheEnabled(false);
	 				
					
					
					//paso 1 capturar campo+objetos
					
					View vista =  pizarraCoach.getRootView();
					vista.setDrawingCacheEnabled(true);
					bmp2= Bitmap.createBitmap(vista.getDrawingCache());
					vista.setDrawingCacheEnabled(false);
					
					/*paso 2 almacenar en un archivo .png */
					File imagen = new File(rutaYFicheroImagen);
					FileOutputStream fos;
					try
					{
						fos = new FileOutputStream(imagen);
						bmp2.compress(Bitmap.CompressFormat.JPEG, 90 ,fos);
						fos.flush();
		 				fos.close();
			 			
					}
					catch(FileNotFoundException e2)
					{
						Toast.makeText(this, e2.getMessage(), Toast.LENGTH_SHORT).show();
						
					}
	 				
	 				 // añadir el nuevo ejercicio en la Tabla de ejercicios de la BD
					 //crearEjercicio(String nombre, String ruta, String descripcion, String ruta_imagen, String tipo_ejercicio)
	 				mDbHelper = new CoachDBAdapter(this);
	 				mDbHelper.open();
	 				
					id = mDbHelper.crearEjercicio(nombreFichero, rutaYFichero,  descripcion,  rutaYFicheroImagen,  tipoEjercicio);
			         if (id > 0) 
			            {
			        	 Toast.makeText(this, "Ejercicio Almacenado OK", Toast.LENGTH_SHORT); 
			            }
			         else
			         {
			        	 Toast.makeText(this, "Error al Almacenar el nuevo Ejercicio", Toast.LENGTH_SHORT); 
			         } 				
	 				
					}
		      
				   	
				   	
		      		catch (Exception e)
		      		{
						Toast.makeText(getApplicationContext(), "¡Fallo al guardar la imagen '"+rutaYFicheroImagen+"'!", Toast.LENGTH_SHORT).show();
					}
				   	
				   	
		     }
		//	}
		}
		
		if(requestCode == 2)
		{
			
			if(resultCode == RESULT_OK)
			{
		      nombreFichero = data.getStringExtra("nombreEjercicio");
		      
			try
			{
												
				FileInputStream ejercicio = new FileInputStream("sdcard/CoachApplication2/Ejercicios"+"/"+nombreFichero);
				ObjectInputStream objeto = new ObjectInputStream(ejercicio);
				
				//se reinician las variables
				
				jugadores1.clear();
				jugadores2.clear();
				balones.clear();
				conos.clear();
				lineas.clear();
				objetosTotales.clear();
				pizarraCoach.removeViewsInLayout(1, objTotales);
				objTotales=0;
				numJugadores1=0;
				numJugadores2=0;
				numConos=0;
				balon= false;
				
				int pizarraa = (Integer) objeto.readObject();//cargar el campo guardado...
				
				if(p.getPizarra() != pizarraa)
				{
					p.setPizarra(pizarraa);
					p.invalidate();
					pizarraCoach.removeViewAt(0);
					pizarraCoach.addView(p, 0);
					pizarraCoach.invalidate();
				}

				
				ArrayList<InformacionObjetos> infoJugador1 = new ArrayList<InformacionObjetos>();
				ArrayList<InformacionObjetos> infoJugador2 = new ArrayList<InformacionObjetos>();
				ArrayList<InformacionObjetos> infoBalon = new ArrayList<InformacionObjetos>();
				ArrayList<InformacionObjetos> infoCono = new ArrayList<InformacionObjetos>();
				ArrayList<InformacionObjetos> infoLinea = new ArrayList<InformacionObjetos>();
				
				Jugador1Drawing j1=null;
				Jugador2Drawing j2=null;
				Balon b = null;
				Cono c=null;
				LineDrawing l = null;
				
				
					infoJugador1 = (ArrayList<InformacionObjetos>) objeto.readObject();				
					for(InformacionObjetos objetos : infoJugador1)
					{					
						jugadores1.add(j1=new Jugador1Drawing(this, objetos.getId(), objetos.getX(), objetos.getY()));		
						objTotales++;
						numJugadores1++;
						anchoCamiseta = j1.getAnchoCamiseta();
						altoCamiseta = j1.getAltoCamiseta();
						objetosTotales.add(camiseta1);
						pizarraCoach.addView(j1);
					}
					//		j.cerrar();
				
		
					infoJugador2 = (ArrayList<InformacionObjetos>) objeto.readObject();
					for(InformacionObjetos objetos : infoJugador2)
					{
						jugadores2.add(j2=new Jugador2Drawing(this, objetos.getId(), objetos.getX(), objetos.getY()));
						objTotales++;
						numJugadores2++;
						anchoCamiseta = j2.getAnchoCamiseta();
						altoCamiseta = j2.getAltoCamiseta();
						objetosTotales.add(camiseta2);
						pizarraCoach.addView(j2);
					}
		
					
					infoBalon = (ArrayList<InformacionObjetos>) objeto.readObject();					
					for(InformacionObjetos objetos : infoBalon)
					{
						balones.add(b=new Balon(this, objetos.getId(), objetos.getX(), objetos.getY()));
						balon=true;
						anchoBalon = b.getAncho(); 
						altoBalon = b.getAlto();
						objTotales++;
						numBalones=1;
						objetosTotales.add(balonn);
						pizarraCoach.addView(b);
					}
					
					infoCono = (ArrayList<InformacionObjetos>) objeto.readObject();
					for(InformacionObjetos objetos : infoCono)
					{
						conos.add(c=new Cono(this, objetos.getId(), objetos.getX(), objetos.getY()));
						//balon=true;
						anchoCono = c.getAncho(); 
						altoCono = c.getAlto();
						objTotales++;
						objetosTotales.add(conoo);
						pizarraCoach.addView(c);
					}
					
				    infoLinea = (ArrayList<InformacionObjetos>) objeto.readObject();
					for(InformacionObjetos lin : infoLinea)
					{
						objTotales++;
						lineas.add(l=new LineDrawing(this, lin.getPoints(), lin.getColor()));
						objetosTotales.add(lineaa);
						pizarraCoach.addView(l);
					}
					
					pizarraCoach.invalidate();

				objeto.close();
				
				Toast.makeText(this, "¡Ejercicio cargado con éxito!",Toast.LENGTH_SHORT).show();
				
			}
			catch(Exception e)
			{
				Toast.makeText(this, "Fallo al cargar el ejercicio!"+nombreFichero, Toast.LENGTH_SHORT).show();
			}
		
			}
			else if(resultCode == RESULT_FIRST_USER)
			{
				
				Toast.makeText(this, "¡No hay tácticas guardardas!", Toast.LENGTH_SHORT).show();				
			}			
	}
		else if(requestCode == 3)
		{
		if(resultCode == -1)
		{		
		    nombreFichero = data.getStringExtra("nombreEjercicio");

			if(!nombreFichero.equals(noname))
			{
				
				File file=new File ("sdcard/CoachApplication2/Ejercicios"+"/"+nombreFichero);
				
				if(file.exists())
				{
		        	file.delete();
					Toast.makeText(this,"¡Ejercicio borrado correctamente!", Toast.LENGTH_SHORT).show();
		        }
		        
			}
			else
			{
				Toast.makeText(this,"No selecciono ningun ejercicio", Toast.LENGTH_SHORT).show();
			}
		}
	}
				
}


	@Override
	public boolean onLongClick(View v) 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	  if (keyCode == KeyEvent.KEYCODE_BACK) 
	  {
	   new AlertDialog.Builder(this)
	      .setIcon(android.R.drawable.ic_dialog_alert)
	      .setTitle("Salir")
	      .setMessage("Desea salir de la pizarra?")
	      .setNegativeButton(android.R.string.cancel, null)//sin listener
	      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() 
	      {
	    	  //un listener que al pulsar, cierre la aplicacion
	        @Override
	        public void onClick(DialogInterface dialog, int which)
	        {
	          //Salir
	        	PizarraCoach.this.finish();
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